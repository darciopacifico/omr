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
 	 * Retorna grupos de validação para o selfService
 	 * @return
 	 */
	public Set<String> getValidationGroups() {
		
		Set<String> validationGroups = new HashSet<String>(2);
		validationGroups.add("javax.validation.groups.Default");
		validationGroups.add("br.com.dlp.jazzomr.person.VGroupUsuario");
		
		return validationGroups;
	}
 	
 	/**
 	 * Salva novo registro de usuário, empresa e grupo 
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
				
			} catch (JazzBusinessException e) {
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
 	 * Autentica a sessão com o usuário recem registrado
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
		questionnaireVO.setDescription("CIÊNCIAS HUMANAS E SUAS TECNOLOGIAS");

		{
		
			String qDesc = "Muitos processos erosivos se concentram nas encostas, principalmente aqueles motivados pela água e pelo vento. No entanto, os reflexos também são sentidos nas áreas de baixada, onde geralmente há ocupação urbana. Um exemplo desses reflexos na vida cotidiana de muitas cidades brasileiras é:";
			
			String[] alt1 = new String[]{
			"a maior ocorrência de enchentes, já que os rios assoreados comportam menos água em seus leitos.",
			"a contaminação da população pelos sedimentos trazidos pelo rio e carregados de matéria orgânica.",
			"o desgaste do solo em áreas urbanas, causado pela redução do escoamento superficial pluvial na encosta.",
			"a maior facilidade de captação de água potável para o abastecimento público, já que é maior o efeito do escoamento sobre a infiltração.",
			"o aumento da incidência de doenças como a amebíase na população urbana, em decorrência do escoamento de água poluída do topo das encostas."
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
			String qDesc = "Antes, eram apenas as grandes cidades que se apresentavam como o império da técnica, objeto de modificações, suspensões, acréscimos, cada vez mais sofisticadas e carregadas de artifício. Esse mundo artificial inclui, hoje, o mundo rural. \n\n"+
											"SANTOS, M. A Natureza do Espaço. São Paulo: Hucitec, 1996. Considerando a transformação mencionada no texto, uma consequência socioespacial que caracteriza o atual mundo rural brasileiro é:";
			
			String[] alt1 = new String[]{
					"o aumento do aproveitamento de solos menos férteis",
					"a redução do processo de concentração de terras",
					"a ampliação do isolamento do espaço rural.",
					"a estagnação da fronteira agrícola do país",
					"a diminuição do nível de emprego formal."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = 
					"A maioria das pessoas daqui era do campo. Vila Maria é hoje exportadora de trabalhadores. Empresários de Primavera do Leste, Estado de Mato Grosso, procuram o bairro de Vila Maria para conseguir mão de obra. É gente indo distante daqui 300, 400 quilômetros para ir trabalhar, para ganhar sete conto por dia. (Carlito, 43 anos, maranhense, entrevistado em 22/03/98).\n\n" +
					"Ribeiro, H. S. O migrante e a cidade: dilemas e conflitos. Araraquara: Wunderlich, 2001 (adaptado).\n\n "+
					"O texto retrata um fenômeno vivenciado pela agricultura brasileira nas últimas décadas do século XX, consequência:";
			
			String[] alt1 = new String[]{
					"dos impactos sociais da modernização da agricultura.",
					"da recomposição dos salários do trabalhador rural.",
					"da exigência de qualificação do trabalhador rural.",
					"da diminuição da importância da agricultura.",
					"da diminuição da importância da agricultura."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
		
		{
			String qDesc = "Os lixões são o pior tipo de disposição final dos resíduos sólidos de uma cidade, representando um grave problema ambiental e de saúde pública. Nesses locais, o lixo é jogado diretamente no solo e a céu aberto, sem nenhuma norma de controle, o que causa, entre outros problemas, a contaminação do solo e das águas pelo chorume (líquido escuro com alta carga poluidora, proveniente da decomposição da matéria orgânica presente no lixo)."+
					"\n\n RICARDO, B.; CANPANILLI, M. Almanaque Brasil"+
					"\n\n Socioambiental 2008. São Paulo, Instituto Socioambiental, 2007."+ 
					"\n\n Considere um município que deposita os resíduos sólidos produzidos por sua população em um lixão. Esse procedimento é considerado um problema de saúde pública porque os lixões:";
			
			String[] alt1 = new String[]{
					"são locais propícios à proliferação de vetores de doenças, além de contaminarem o solo e as águas",
					"causam problemas respiratórios, devido ao mau cheiro que provém da decomposição.",
					"provocam o fenômeno da chuva ácida, devido aos gases oriundos da decomposição da matéria orgânica.",
					"são instalados próximos ao centro das cidades, afetando toda a população que circula diariamente na área.",
					"são responsáveis pelo desaparecimento das nascentes na região onde são instalados, o que leva à escassez de água."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
		
		
		{
			String qDesc = "Faça uma dissertação com o seguinte tema: O poder de transformação da leitura (ENEM 2006).";
			createQuestion(empresaVO, questionnaireVO, qDesc, "REDAÇÃO");
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
		questionnaireVO.setDescription("CIÊNCIAS DA NATUREZA E SUAS TECNOLOGIAS");

		{
			String qDesc = "Em nosso cotidiano, utilizamos as palavras “calor” e “temperatura” de forma diferente de como elas são usadas no meio científico. Na linguagem corrente, calor é identificado como “algo quente” e temperatura mede a “quantidade de calor de um corpo”. Esses significados, no entanto, não conseguem explicar diversas situações que podem ser verificadas na prática."+ 
						"\n\nDo ponto de vista científico, que situação prática mostra a limitação dos conceitos corriqueiros de calor e temperatura?";
			
			String[] alt1 = new String[]{
					"A temperatura da água pode ficar constante durante o tempo em que estiver fervendo.",
					"Uma mãe coloca a mão na água da banheira do bebê para verificar a temperatura da água.",
					"A chama de um fogão pode ser usada para aumentar a temperatura da água em uma panela.",
					"A água quente que está em uma caneca é passada para outra caneca a fim de diminuir sua temperatura",
					"Um forno pode fornecer calor para uma vasilha de água que está em seu interior com menor temperatura do que a dele."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = 
					"As ondas eletromagnéticas, como a luz visível e as ondas de rádio, viajam em linha reta em um meio homogêneo. Então, as ondas de rádio emitidas na região litorânea do Brasil não alcançariam a região amazônica do Brasil por causa da curvatura da Terra. Entretanto sabemos que é possível transmitir ondas de rádio entre essas localidades devido à ionosfera."+ 
					"\n\n Com ajuda da ionosfera, a transmissão de ondas planas entre o litoral do Brasil e a região amazônica é possível por meio da:";
			
			String[] alt1 = new String[]{
					"reflexão.",
					"refração.",
					"difração.",
					"polarização.",
					"interferência."};

			createQuestion(empresaVO, questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = "Alguns anfíbios e répteis são adaptados à vida subterrânea. Nessa situação, apresentam algumas características corporais como, por exemplo, ausência de patas, corpo anelado que facilita o deslocamento no subsolo e, em alguns casos, ausência de olhos."+
				"\n\nSuponha que um biólogo tentasse explicar a origem das adaptações mencionadas no texto utilizando conceitos da teoria evolutiva de Lamarck. Ao adotar esse ponto de vista, ele diria que:";
			
			String[] alt1 = new String[]{
					"a ausência de olhos teria sido causada pela falta de uso dos mesmos, segundo a lei do uso e desuso.",
					"as características citadas no texto foram originadas pela seleção natural.",
					"o corpo anelado é uma característica fortemente adaptativa, mas seria transmitida apenas à primeira geração de descendentes.",
					"as patas teriam sido perdidas pela falta de uso e, em seguida, essa característica foi incorporada ao patrimônio genético e então transmitida aos descendentes.",
					"as características citadas no texto foram adquiridas por meio de mutações e depois, ao longo do tempo, foram selecionadas por serem mais adaptadas ao ambiente em que os organismos se encontram."};

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
				pessoaVO.setMsgCriticLogin("Já existe um usuário registrado com este login!");
				
			}else{
				pessoaVO.setMsgCriticLogin("Login válido!");
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
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Senha em branco! Informação obrigatória!", "Senha em branco! Informação obrigatória!"));
			isValid=false;
		}else if(!senha.equals(senhaConfirm)){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "A confirmação de senha não coincide com a senha digitada!", "A confirmação de senha não coincide com a senha digitadaBBB!"));
			isValid=false;
		}

		String login = pessoaVO2.getLogin();
		if(pessoaBusiness.existsLogin(login)){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Já existe um usuário com o login '"+login+"'! Por favor, tente outro login!", "Já existe um usuário com o login '"+login+"'! Por favor, tente outro login!"));
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
