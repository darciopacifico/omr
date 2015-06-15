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
 	 * Retorna grupos de validaÁ„o para o selfService
 	 * @return
 	 */
	public Set<String> getValidationGroups() {
		
		Set<String> validationGroups = new HashSet<String>(2);
		validationGroups.add("javax.validation.groups.Default");
		validationGroups.add("br.com.dlp.jazzomr.person.VGroupUsuario");
		
		return validationGroups;
	}
 	
 	/**
 	 * Salva novo registro de usu·rio, empresa e grupo 
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
 	 * Autentica a sess„o com o usu·rio recem registrado
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
		questionnaireVO.setDescription("CI NCIAS HUMANAS E SUAS TECNOLOGIAS");

		{
		
			String qDesc = "Muitos processos erosivos se concentram nas encostas, principalmente aqueles motivados pela ·gua e pelo vento. No entanto, os reflexos tambÈm s„o sentidos nas ·reas de baixada, onde geralmente h· ocupaÁ„o urbana. Um exemplo desses reflexos na vida cotidiana de muitas cidades brasileiras È:";
			
			String[] alt1 = new String[]{
			"a maior ocorrÍncia de enchentes, j· que os rios assoreados comportam menos ·gua em seus leitos.",
			"a contaminaÁ„o da populaÁ„o pelos sedimentos trazidos pelo rio e carregados de matÈria org‚nica.",
			"o desgaste do solo em ·reas urbanas, causado pela reduÁ„o do escoamento superficial pluvial na encosta.",
			"a maior facilidade de captaÁ„o de ·gua pot·vel para o abastecimento p˙blico, j· que È maior o efeito do escoamento sobre a infiltraÁ„o.",
			"o aumento da incidÍncia de doenÁas como a amebÌase na populaÁ„o urbana, em decorrÍncia do escoamento de ·gua poluÌda do topo das encostas."
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
			String qDesc = "Antes, eram apenas as grandes cidades que se apresentavam como o impÈrio da tÈcnica, objeto de modificaÁıes, suspensıes, acrÈscimos, cada vez mais sofisticadas e carregadas de artifÌcio. Esse mundo artificial inclui, hoje, o mundo rural. \n\n"+
											"SANTOS, M. A Natureza do EspaÁo. S„o Paulo: Hucitec, 1996. Considerando a transformaÁ„o mencionada no texto, uma consequÍncia socioespacial que caracteriza o atual mundo rural brasileiro È:";
			
			String[] alt1 = new String[]{
					"o aumento do aproveitamento de solos menos fÈrteis",
					"a reduÁ„o do processo de concentraÁ„o de terras",
					"a ampliaÁ„o do isolamento do espaÁo rural.",
					"a estagnaÁ„o da fronteira agrÌcola do paÌs",
					"a diminuiÁ„o do nÌvel de emprego formal."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = 
					"A maioria das pessoas daqui era do campo. Vila Maria È hoje exportadora de trabalhadores. Empres·rios de Primavera do Leste, Estado de Mato Grosso, procuram o bairro de Vila Maria para conseguir m„o de obra. Aâ gente indo distante daqui 300, 400 quilÙmetros para ir trabalhar, para ganhar sete conto por dia. (Carlito, 43 anos, maranhense, entrevistado em 22/03/98).\n\n" +
					"Ribeiro, H. S. O migrante e a cidade: dilemas e conflitos. Araraquara: Wunderlich, 2001 (adaptado).\n\n "+
					"O texto retrata um fenÙmeno vivenciado pela agricultura brasileira nas ˙ltimas dÈcadas do sÈculo XX, consequÍncia:";
			
			String[] alt1 = new String[]{
					"dos impactos sociais da modernizaÁ„o da agricultura.",
					"da recomposiÁ„o dos sal·rios do trabalhador rural.",
					"da exigÍncia de qualificaÁ„o do trabalhador rural.",
					"da diminuiÁ„o da import‚ncia da agricultura.",
					"da diminuiÁ„o da import‚ncia da agricultura."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
		
		{
			String qDesc = "Os lixıes s„o o pior tipo de disposiÁ„o final dos resÌduos sÛlidos de uma cidade, representando um grave problema ambiental e de sa˙de p˙blica. Nesses locais, o lixo È jogado diretamente no solo e a cÈu aberto, sem nenhuma norma de controle, o que causa, entre outros problemas, a contaminaÁ„o do solo e das ·guas pelo chorume (lÌquido escuro com alta carga poluidora, proveniente da decomposiÁ„o da matÈria org‚nica presente no lixo)."+
					"\n\n RICARDO, B.; CANPANILLI, M. Almanaque Brasil"+
					"\n\n Socioambiental 2008. S„o Paulo, Instituto Socioambiental, 2007."+ 
					"\n\n Considere um municÌpio que deposita os resÌduos sÛlidos produzidos por sua populaÁ„o em um lix„o. Esse procedimento È considerado um problema de sa˙de p˙blica porque os lixıes:";
			
			String[] alt1 = new String[]{
					"s„o locais propÌcios ‡ proliferaÁ„o de vetores de doenÁas, alÈm de contaminarem o solo e as ·guas",
					"causam problemas respiratÛrios, devido ao mau cheiro que provÈm da decomposiÁ„o.",
					"provocam o fenÙmeno da chuva ·cida, devido aos gases oriundos da decomposiÁ„o da matÈria org‚nica.",
					"s„o instalados prÛximos ao centro das cidades, afetando toda a populaÁ„o que circula diariamente na ·rea.",
					"s„o respons·veis pelo desaparecimento das nascentes na regi„o onde s„o instalados, o que leva ‡ escassez de ·gua."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
		 
		
		{
			String qDesc = "FaÁa uma dissertaÁ„o com o seguinte tema: O poder de transformaÁ„o da leitura (ENEM 2006).";
			createQuestion(empresaVO, questionnaireVO, qDesc, "REDA«√ÉO");
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
		questionnaireVO.setDescription("CI NCIAS DA NATUREZA E SUAS TECNOLOGIAS");

		{
			String qDesc = "Em nosso cotidiano, utilizamos as palavras ‚Äúcalor‚Äù e ‚Äútemperatura‚Äù de forma diferente de como elas s„o usadas no meio cientÌfico. Na linguagem corrente, calor È identificado como ‚Äúalgo quente‚Äù e temperatura mede a ‚Äúquantidade de calor de um corpo‚Äù. Esses significados, no entanto, n„o conseguem explicar diversas situaÁıes que podem ser verificadas na pr·tica."+ 
						"\n\nDo ponto de vista cientÌfico, que situaÁ„o pr·tica mostra a limitaÁ„o dos conceitos corriqueiros de calor e temperatura?";
			
			String[] alt1 = new String[]{
					"A temperatura da ·gua pode ficar constante durante o tempo em que estiver fervendo.",
					"Uma m„e coloca a m„o na ·gua da banheira do bebÍ para verificar a temperatura da ·gua.",
					"A chama de um fog„o pode ser usada para aumentar a temperatura da ·gua em uma panela.",
					"A ·gua quente que est· em uma caneca È passada para outra caneca a fim de diminuir sua temperatura",
					"Um forno pode fornecer calor para uma vasilha de ·gua que est· em seu interior com menor temperatura do que a dele."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = 
					"As ondas eletromagnÈticas, como a luz visÌvel e as ondas de r·dio, viajam em linha reta em um meio homogÍneo. Ent„o, as ondas de r·dio emitidas na regi„o litor‚nea do Brasil n„o alcanÁariam a regi„o amazÙnica do Brasil por causa da curvatura da Terra. Entretanto sabemos que È possÌvel transmitir ondas de r·dio entre essas localidades devido ‡ ionosfera."+ 
					"\n\n Com ajuda da ionosfera, a transmiss„o de ondas planas entre o litoral do Brasil e a regi„o amazÙnica È possÌvel por meio da:";
			
			String[] alt1 = new String[]{
					"reflex„o.",
					"refraÁ„o.",
					"difraÁ„o.",
					"polarizaÁ„o.",
					"interferÍncia."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = "Alguns anfÌbios e rÈpteis s„o adaptados ‡ vida subterr‚nea. Nessa situaÁ„o, apresentam algumas caracterÌsticas corporais como, por exemplo, ausÍncia de patas, corpo anelado que facilita o deslocamento no subsolo e, em alguns casos, ausÍncia de olhos."+
				"\n\nSuponha que um biÛlogo tentasse explicar a origem das adaptaÁıes mencionadas no texto utilizando conceitos da teoria evolutiva de Lamarck. Ao adotar esse ponto de vista, ele diria que:";
			
			String[] alt1 = new String[]{
					"a ausÍncia de olhos teria sido causada pela falta de uso dos mesmos, segundo a lei do uso e desuso.",
					"as caracterÌsticas citadas no texto foram originadas pela seleÁ„o natural.",
					"o corpo anelado È uma caracterÌstica fortemente adaptativa, mas seria transmitida apenas ‡ primeira geraÁ„o de descendentes.",
					"as patas teriam sido perdidas pela falta de uso e, em seguida, essa caracterÌstica foi incorporada ao patrimÙnio genÈtico e ent„o transmitida aos descendentes.",
					"as caracterÌsticas citadas no texto foram adquiridas por meio de mutaÁıes e depois, ao longo do tempo, foram selecionadas por serem mais adaptadas ao ambiente em que os organismos se encontram."};

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
				pessoaVO.setMsgCriticLogin("J· existe um usu·rio registrado com este login!");
				
			}else{
				pessoaVO.setMsgCriticLogin("Login v·lido!");
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
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Senha em branco! InformaÁ„o obrigatÛria!", "Senha em branco! InformaÁ„o obrigatÛria!"));
			isValid=false;
		}else if(!senha.equals(senhaConfirm)){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "A confirmaÁ„o de senha n„o coincide com a senha digitada!", "A confirmaÁ„o de senha n„o coincide com a senha digitadaBBB!"));
			isValid=false;
		}

		String login = pessoaVO2.getLogin();
		if(pessoaBusiness.existsLogin(login)){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "J· existe um usu·rio com o login '"+login+"'! Por favor, tente outro login!", "J· existe um usu·rio com o login '"+login+"'! Por favor, tente outro login!"));
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
