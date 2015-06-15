package br.com.dlp.jazzomr.selfservice;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzomr.empresa.EmpresaVO;
import br.com.dlp.jazzomr.event.EventBusiness;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationHelper;
import br.com.dlp.jazzomr.exam.ExamBusiness;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.QuestionnaireBusiness;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.person.EAuthority;
import br.com.dlp.jazzomr.person.PessoaBusiness;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.person.VGroupUsuario;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.DissertativeVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.ImageVO;

@Scope(value="session")
@Component
public class SelfRegBean  {

 	public String teste(){
 		return "teste self service";
 	}

 	private PessoaVO pessoaVO;
 	private EmpresaVO empresaVO;
 	
 	protected ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
 	
 	@Autowired
 	private SelfRegBusinessImpl selfRegBusinessImpl;
 	
 	@Autowired
 	private PessoaBusiness pessoaBusiness;
 	
 	@Autowired
	private ParticipationHelper participationHelper;

 	

	@Autowired
	private QuestionnaireBusiness questionnaireBusiness;
	
	@Autowired
	private ExamBusiness examBusiness;
	
	@Autowired
	private EventBusiness eventBusiness;
 	
 	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
 	/**
 	 * Retorna grupos de valida��o para o selfService
 	 * @return
 	 */
	public Set<String> getValidationGroups() {
		
		Set<String> validationGroups = new HashSet<String>(2);
		validationGroups.add("javax.validation.groups.Default");
		validationGroups.add("br.com.dlp.jazzomr.person.VGroupUsuario");
		
		return validationGroups;
	}
 	
 	/**
 	 * Salva novo registro de usu�rio, empresa e grupo 
 	 * @return
 	 * @throws JazzBusinessException
 	 */
 	public String actionSalvar(){

 		if(pessoaVO==null){
 			return "";
 		}
 		pessoaVO.setEmpresaVO(empresaVO);
		
 		if(validatePessoa(pessoaVO)){
 			
 			try {
				selfRegBusinessImpl.saveNewReg(empresaVO, pessoaVO);
				
				
				autenticarSessao(empresaVO, pessoaVO);
				
				
				createDemoExam(pessoaVO,empresaVO);
				
			} catch (Exception e) {
				log.error("Erro ao tentar salvar registro de pessoa/empresa!",e);
				FacesContext context = FacesContext.getCurrentInstance();					
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao tentar salvar novo registro de pessoa/empresa!", "Erro ao tentar salvar novo registro de pessoa/empresa!"));
			}
			
			return "registeredSuccess";
			
 		}else{
 			
 			return "";
 			
 		}
 	}
 	
 	
 	/**
 	 * Autentica a sess�o com o usu�rio recem registrado
 	 * @param empresaVO2
 	 * @param pessoaVO2
 	 */
 	protected void autenticarSessao(EmpresaVO empresaVO2, PessoaVO pessoaVO2) {

 		SecurityContext context = SecurityContextHolder.getContext();
 		
 		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
 		GrantedAuthority authority = new GrantedAuthorityImpl(EAuthority.MASTER_ADM.toString());
 		authorities.add(authority);
 		
 		UserDetails user = new User(pessoaVO2.getLogin(), "", true, true, true, true, authorities); 
 		
		
 		Authentication authentication = new AnonymousAuthenticationToken(pessoaVO2.getLogin(),user,authorities);
 		
 		
		context.setAuthentication(authentication);
		
	}

	/**
 	 * Cria demo de exame 
 	 * @param pessoaVO
 	 * @param empresaVO
 	 * @throws JazzBusinessException 
 	 */
	protected void createDemoExam(PessoaVO pessoaVO, EmpresaVO empresaVO) throws JazzBusinessException {
		
		QuestionnaireVO enemCiencHumanas = createENEMCienciasHumanas(empresaVO);
		QuestionnaireVO enemCiencNatureza= createENEMCienciasNatureza(empresaVO);
	 
		questionnaireBusiness.saveOrUpdate(enemCiencNatureza);
		questionnaireBusiness.saveOrUpdate(enemCiencHumanas);
		
		
		ExamVO exameDemo = new ExamVO();
		exameDemo.setRelatorioVO(empresaVO.getRelatorioVOs().get(0));
		exameDemo.setDescription("Exame Demo - ENEM 2010");
		exameDemo.setEmpresaVO(empresaVO);
		exameDemo.getQuestionnaires().add(enemCiencNatureza);
		exameDemo.getQuestionnaires().add(enemCiencHumanas);
		
		examBusiness.saveOrUpdate(exameDemo);
		
		
		participationHelper.setNumeroDeVariacoes(3);
		
		List<PessoaVO> pessoasSelecionadas = new ArrayList<PessoaVO>();
		pessoasSelecionadas.add(pessoaVO);
		participationHelper.setPessoasSelecionadas(pessoasSelecionadas);
		
				
		List<ExamVO> examSelecionados = new ArrayList<ExamVO>();
		examSelecionados.add(exameDemo);
		participationHelper.setExamSelecionados(examSelecionados);
		
		
		EventVO eventVO = new EventVO();
		eventVO.setDescription("Evento Demo - ENEM 2010");
		eventVO.setDtInicio(new Date(2012,03,23));
		eventVO.setDtFim(new Date(2012,04,23));
		eventVO.setEmpresaVO(empresaVO);
		eventBusiness.saveOrUpdate(eventVO);
		
		
		participationHelper.processParticipations(eventVO);
		
		
		System.out.println(enemCiencHumanas);
		
	}
 	
 	



	/**
	 * @param empresaVO
	 * @return
	 */
	protected QuestionnaireVO createENEMCienciasHumanas(EmpresaVO empresaVO) {
		QuestionnaireVO questionnaireVO = new QuestionnaireVO();
		questionnaireVO.setEmpresaVO(empresaVO);
		questionnaireVO.setDescription("CI�NCIAS HUMANAS E SUAS TECNOLOGIAS");

		{
		
			String qDesc = "Muitos processos erosivos se concentram nas encostas, principalmente aqueles motivados pela �gua e pelo vento. No entanto, os reflexos tamb�m s�o sentidos nas �reas de baixada, onde geralmente h� ocupa��o urbana. Um exemplo desses reflexos na vida cotidiana de muitas cidades brasileiras �:";
			
			String[] alt1 = new String[]{
			"a maior ocorr�ncia de enchentes, j� que os rios assoreados comportam menos �gua em seus leitos.",
			"a contamina��o da popula��o pelos sedimentos trazidos pelo rio e carregados de mat�ria org�nica.",
			"o desgaste do solo em �reas urbanas, causado pela redu��o do escoamento superficial pluvial na encosta.",
			"a maior facilidade de capta��o de �gua pot�vel para o abastecimento p�blico, j� que � maior o efeito do escoamento sobre a infiltra��o.",
			"o aumento da incid�ncia de doen�as como a ameb�ase na popula��o urbana, em decorr�ncia do escoamento de �gua polu�da do topo das encostas."
			};
			
			QuestionVO questionVO = createQuestion(empresaVO, qDesc, alt1);
			
			BufferedImage img = loadImage("/reports/morro.png");
			ImageVO imageVO = new ImageVO();
			imageVO.setTitulo("Figura 1");
			imageVO.setImage(img);
			
			questionVO.getImageVOs().add(imageVO);
			
			questionnaireVO.getQuestions().add(questionVO);
		}
		
		{
			String qDesc = "Antes, eram apenas as grandes cidades que se apresentavam como o imp�rio da t�cnica, objeto de modifica��es, suspens�es, acr�scimos, cada vez mais sofisticadas e carregadas de artif�cio. Esse mundo artificial inclui, hoje, o mundo rural. \n\n"+
											"SANTOS, M. A Natureza do Espa�o. S�o Paulo: Hucitec, 1996. Considerando a transforma��o mencionada no texto, uma consequ�ncia socioespacial que caracteriza o atual mundo rural brasileiro �:";
			
			String[] alt1 = new String[]{
					"o aumento do aproveitamento de solos menos f�rteis",
					"a redu��o do processo de concentra��o de terras",
					"a amplia��o do isolamento do espa�o rural.",
					"a estagna��o da fronteira agr�cola do pa�s",
					"a diminui��o do n�vel de emprego formal."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = 
					"A maioria das pessoas daqui era do campo. Vila Maria � hoje exportadora de trabalhadores. Empres�rios de Primavera do Leste, Estado de Mato Grosso, procuram o bairro de Vila Maria para conseguir m�o de obra. A� gente indo distante daqui 300, 400 quil�metros para ir trabalhar, para ganhar sete conto por dia. (Carlito, 43 anos, maranhense, entrevistado em 22/03/98).\n\n" +
					"Ribeiro, H. S. O migrante e a cidade: dilemas e conflitos. Araraquara: Wunderlich, 2001 (adaptado).\n\n "+
					"O texto retrata um fen�meno vivenciado pela agricultura brasileira nas �ltimas d�cadas do s�culo XX, consequ�ncia:";
			
			String[] alt1 = new String[]{
					"dos impactos sociais da moderniza��o da agricultura.",
					"da recomposi��o dos sal�rios do trabalhador rural.",
					"da exig�ncia de qualifica��o do trabalhador rural.",
					"da diminui��o da import�ncia da agricultura.",
					"da diminui��o da import�ncia da agricultura."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
		
		{
			String qDesc = "Os lix�es s�o o pior tipo de disposi��o final dos res�duos s�lidos de uma cidade, representando um grave problema ambiental e de sa�de p�blica. Nesses locais, o lixo � jogado diretamente no solo e a c�u aberto, sem nenhuma norma de controle, o que causa, entre outros problemas, a contamina��o do solo e das �guas pelo chorume (l�quido escuro com alta carga poluidora, proveniente da decomposi��o da mat�ria org�nica presente no lixo)."+
					"\n\n RICARDO, B.; CANPANILLI, M. Almanaque Brasil"+
					"\n\n Socioambiental 2008. S�o Paulo, Instituto Socioambiental, 2007."+ 
					"\n\n Considere um munic�pio que deposita os res�duos s�lidos produzidos por sua popula��o em um lix�o. Esse procedimento � considerado um problema de sa�de p�blica porque os lix�es:";
			
			String[] alt1 = new String[]{
					"s�o locais prop�cios � prolifera��o de vetores de doen�as, al�m de contaminarem o solo e as �guas",
					"causam problemas respirat�rios, devido ao mau cheiro que prov�m da decomposi��o.",
					"provocam o fen�meno da chuva �cida, devido aos gases oriundos da decomposi��o da mat�ria org�nica.",
					"s�o instalados pr�ximos ao centro das cidades, afetando toda a popula��o que circula diariamente na �rea.",
					"s�o respons�veis pelo desaparecimento das nascentes na regi�o onde s�o instalados, o que leva � escassez de �gua."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
		 
		
		{
			String qDesc = "Fa�a uma disserta��o com o seguinte tema: O poder de transforma��o da leitura (ENEM 2006).";
			createQuestion(empresaVO, questionnaireVO, qDesc, "REDA�ÃO");
			}		
		return questionnaireVO;
	}
	


	/**
	 * @param imageFile
	 *          TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected BufferedImage loadImage(String imageFile) {
		InputStream is = this.getClass().getResourceAsStream(imageFile);

		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar carregar imagem (" + imageFile + ")!", e);
		}
		return bi;
	}

	/**
	 * @param empresaVO
	 * @return
	 */
	protected QuestionnaireVO createENEMCienciasNatureza(EmpresaVO empresaVO) {
		QuestionnaireVO questionnaireVO = new QuestionnaireVO();
		questionnaireVO.setEmpresaVO(empresaVO);
		questionnaireVO.setDescription("CI�NCIAS DA NATUREZA E SUAS TECNOLOGIAS");

		{
			String qDesc = "Em nosso cotidiano, utilizamos as palavras “calor” e “temperatura” de forma diferente de como elas s�o usadas no meio cient�fico. Na linguagem corrente, calor � identificado como “algo quente” e temperatura mede a “quantidade de calor de um corpo”. Esses significados, no entanto, n�o conseguem explicar diversas situa��es que podem ser verificadas na pr�tica."+ 
						"\n\nDo ponto de vista cient�fico, que situa��o pr�tica mostra a limita��o dos conceitos corriqueiros de calor e temperatura?";
			
			String[] alt1 = new String[]{
					"A temperatura da �gua pode ficar constante durante o tempo em que estiver fervendo.",
					"Uma m�e coloca a m�o na �gua da banheira do beb� para verificar a temperatura da �gua.",
					"A chama de um fog�o pode ser usada para aumentar a temperatura da �gua em uma panela.",
					"A �gua quente que est� em uma caneca � passada para outra caneca a fim de diminuir sua temperatura",
					"Um forno pode fornecer calor para uma vasilha de �gua que est� em seu interior com menor temperatura do que a dele."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = 
					"As ondas eletromagn�ticas, como a luz vis�vel e as ondas de r�dio, viajam em linha reta em um meio homog�neo. Ent�o, as ondas de r�dio emitidas na regi�o litor�nea do Brasil n�o alcan�ariam a regi�o amaz�nica do Brasil por causa da curvatura da Terra. Entretanto sabemos que � poss�vel transmitir ondas de r�dio entre essas localidades devido � ionosfera."+ 
					"\n\n Com ajuda da ionosfera, a transmiss�o de ondas planas entre o litoral do Brasil e a regi�o amaz�nica � poss�vel por meio da:";
			
			String[] alt1 = new String[]{
					"reflex�o.",
					"refra��o.",
					"difra��o.",
					"polariza��o.",
					"interfer�ncia."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = "Alguns anf�bios e r�pteis s�o adaptados � vida subterr�nea. Nessa situa��o, apresentam algumas caracter�sticas corporais como, por exemplo, aus�ncia de patas, corpo anelado que facilita o deslocamento no subsolo e, em alguns casos, aus�ncia de olhos."+
				"\n\nSuponha que um bi�logo tentasse explicar a origem das adapta��es mencionadas no texto utilizando conceitos da teoria evolutiva de Lamarck. Ao adotar esse ponto de vista, ele diria que:";
			
			String[] alt1 = new String[]{
					"a aus�ncia de olhos teria sido causada pela falta de uso dos mesmos, segundo a lei do uso e desuso.",
					"as caracter�sticas citadas no texto foram originadas pela sele��o natural.",
					"o corpo anelado � uma caracter�stica fortemente adaptativa, mas seria transmitida apenas � primeira gera��o de descendentes.",
					"as patas teriam sido perdidas pela falta de uso e, em seguida, essa caracter�stica foi incorporada ao patrim�nio gen�tico e ent�o transmitida aos descendentes.",
					"as caracter�sticas citadas no texto foram adquiridas por meio de muta��es e depois, ao longo do tempo, foram selecionadas por serem mais adaptadas ao ambiente em que os organismos se encontram."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
		return questionnaireVO;
	}


	/**
	 * @param empresaVO
	 * @param questionnaireVO
	 * @param qDesc
	 * @param alt1
	 */
	protected void createQuestion(EmpresaVO empresaVO, QuestionnaireVO questionnaireVO, String qDesc, String[] alts) {
		QuestionVO questionVO = createQuestion(empresaVO, qDesc, alts);
		
		questionnaireVO.getQuestions().add(questionVO);
	}
	


	/**
	 * @param empresaVO
	 * @param questionnaireVO
	 * @param qDesc
	 * @param alt1
	 */
	protected void createQuestion(EmpresaVO empresaVO, QuestionnaireVO questionnaireVO, String qDesc, String dissertativa) {
		QuestionVO questionVO = createQuestion(empresaVO, qDesc, dissertativa);
		
		questionnaireVO.getQuestions().add(questionVO);
	}


	private QuestionVO createQuestion(EmpresaVO empresaVO2, String qDesc, String dissertativa) {
		QuestionVO questionVO = new QuestionVO();
		questionVO.setEmpresaVO(empresaVO);
		questionVO.setDescription(qDesc);

		DissertativeVO dissertativeVO = new DissertativeVO();
		dissertativeVO.setDescription(dissertativa);
		dissertativeVO.setMaxPonto(5);
		dissertativeVO.setQtdLinhas(40);
		dissertativeVO.setRequired(true);

		questionVO.getCriterionVOs().add(dissertativeVO);

		return questionVO;
	}

	/**
	 * @param empresaVO
	 * @param qDesc
	 * @param alt1
	 * @return
	 */
	protected QuestionVO createQuestion(EmpresaVO empresaVO, String qDesc, String[] alt1) {
		QuestionVO questionVO = new QuestionVO();
		questionVO.setEmpresaVO(empresaVO);
		questionVO.setDescription(qDesc);

		boolean correct = true;
		int i=1;
		for (String altDesc : alt1) {
			AlternativeVO alternativeVO = new AlternativeVO();
			alternativeVO.setDescription(altDesc);
			alternativeVO.setCorrect(correct);
			alternativeVO.setOrdem((double) i++);
			questionVO.getCriterionVOs().add(alternativeVO);
			correct = false;
		}
		return questionVO;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String checkLogin(){
		
		PessoaVO pessoaVO = getPessoaVO();

		
		if(pessoaVO!=null && pessoaVO.getLogin()!=null ){

			String pkLogin = pessoaVO.getLogin();
			
			if(StringUtils.isBlank(pkLogin) || pkLogin.trim().length()<4 || pkLogin.trim().length()>20 ){
				pessoaVO.setMsgCriticLogin("O login deve possuir entre 4 e 20 caracteres!");
				
			}else if(pessoaBusiness.existsLogin(pkLogin)){
				pessoaVO.setMsgCriticLogin("J� existe um usu�rio registrado com este login!");
				
			}else{
				pessoaVO.setMsgCriticLogin("Login v�lido!");
			}
		}
		
		return "";
	}
	

 	

	/**
	 * Valida vo de pessoa informado
	 * @param pessoaVO2
	 */
	public boolean validatePessoa(PessoaVO pessoaVO2) {
		
		boolean isValid = true;
		
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<PessoaVO>> constraints = validator.validate(pessoaVO2, Default.class,VGroupUsuario.class);
		if(CollectionUtils.isNotEmpty(constraints)){
			FacesContext context = FacesContext.getCurrentInstance();					
			for (ConstraintViolation<PessoaVO> constraintViolation : constraints) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, constraintViolation.getMessage(), constraintViolation.getMessage()));
				isValid=false;
			}
		}

	
		String senha = pessoaVO.getSenha();
		String senhaConfirm = pessoaVO.getSenhaConfirm();
		if(StringUtils.isBlank(senha)){
			FacesContext context = FacesContext.getCurrentInstance();					
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Senha em branco! Informa��o obrigat�ria!", "Senha em branco! Informa��o obrigat�ria!"));
			isValid=false;
		}else if(!senha.equals(senhaConfirm)){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "A confirma��o de senha n�o coincide com a senha digitada!", "A confirma��o de senha n�o coincide com a senha digitadaBBB!"));
			isValid=false;
		}

		String login = pessoaVO2.getLogin();
		if(pessoaBusiness.existsLogin(login)){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "J� existe um usu�rio com o login '"+login+"'! Por favor, tente outro login!", "J� existe um usu�rio com o login '"+login+"'! Por favor, tente outro login!"));
			isValid=false;
		}
		
		return isValid;
	}
 	
 	public boolean isSuccess(){
 		//return pessoaVO!=null && pessoaVO.getPK()!=null;
 		return false;
 	}
 	
 	
	public PessoaVO getPessoaVO() {
		
		if(pessoaVO==null){
			pessoaVO = new PessoaVO();
		}
		
		return pessoaVO;
	}

	
	public EmpresaVO getEmpresaVO() {
		if(this.empresaVO==null){
			this.empresaVO = new EmpresaVO();
		}
		
		return empresaVO;
	}

	
	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public void setEmpresaVO(EmpresaVO empresaVO) {
		this.empresaVO = empresaVO;
	}
 		
}
