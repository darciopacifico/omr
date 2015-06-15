

import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.jazzav.anuncio.AnuncioVO;
import br.com.dlp.jazzav.opcionais.OpcionalBusiness;
import br.com.dlp.jazzav.opcionais.OpcionalDAO;
import br.com.dlp.jazzav.person.PessoaVO;
import br.com.dlp.jazzav.produto.OpcionalVO;


@ContextConfiguration(locations = "/ApplicationContext_JazzFramework.xml")
public class TestPersistence extends AbstractTestNGSpringContextTests{
	
	
	
	
	@Resource
	public OpcionalDAO opcionalDAO;
	
	@Resource(name="jazzDS")
	public Object resource;
	
	@Resource
	public SessionFactory sessionFactory;	

	@Resource
	OpcionalBusiness opcionalBusiness; 

	
	public TestPersistence() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Test
	public void testValidation(){
		
		PessoaVO pessoaVO = new PessoaVO();

		pessoaVO.setEmail("d2222222222222222dddddddddddddddda@g.com");
		pessoaVO.setSenha("2232");
		pessoaVO.setSenhaConfirm("2232");
		
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		
		Validator validator = validatorFactory.getValidator();
		
		Set<ConstraintViolation<PessoaVO>> critics = validator.validate(pessoaVO);
		
		
		
		for (ConstraintViolation<PessoaVO> constraintViolation : critics) {
			System.out.println(constraintViolation);
		}

		
		
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testRandom(){
		
		
		String random = RandomStringUtils.random(30, true, true);
		
		System.out.println(random);
		
	}	
	
	
	
	
	class SimpleAuth extends Authenticator {  
    public String username = null;  
    public String password = null;  
  
  
    public SimpleAuth(String user, String pwd) {  
        username = user;  
        password = pwd;  
    }  
  
    protected PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication (username,password);  
    }  
}   

	
	
	
	@Test
	public void sendMail(){
		  final String SENHA_MAIL = "m4c4c0rabudO";
		  final String USUARIO_MAIL = "darcio.pacifico@gmail.com";
		  final String FROM_MAIL = "darcio.pacifico@gmail.com";

		
		
		String resetToken = RandomStringUtils.random(20, true, true);
		

		// Recipient's email ID needs to be mentioned.
		String usuarioMail = USUARIO_MAIL;
		String senhaMail = SENHA_MAIL;

		String to = "darcio.pacifico@gmail.com";
		String from = FROM_MAIL;
		
		String assunto = "Assunto email!";
		String corpoMail = 
				"<html> "+
				"	<head> "+
				"		<title>Reset Senha</title> "+
				"	</head> "+
				"	<body> "+
				"		<a href='http://localhost:8080/JazzAV.portlet/resetSenha?token="+resetToken+"'>Clique aqui para resetar sua senha</a> "+
				"	</body> "+
				"</html> ";
				
		
		
		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
    props.put("mail.transport.protocol", "smtp"); //define protocolo de envio como SMTP  
    props.put("mail.smtp.starttls.enable","true");   
    props.put("mail.smtp.host", "smtp.gmail.com"); //server SMTP do GMAIL  
    props.put("mail.smtp.auth", "true"); //ativa autenticacao  
    props.put("mail.smtp.user", from); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)  
    props.put("mail.debug", "true");  
    props.put("mail.smtp.port", "465"); //porta  
    props.put("mail.smtp.socketFactory.port", "465"); //mesma porta para o socket  
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
    props.put("mail.smtp.socketFactory.fallback", "false");  		
		

    SimpleAuth auth = new SimpleAuth (usuarioMail,senhaMail);  
		

		// Get the default Session object.
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props,auth);

		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);

		// Set From: header field of the header.
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(assunto);

			message.setContent(corpoMail, "text/html");
			
			// Send message
			Transport.send(message);

		} catch (AddressException e) {
			logger.error("",e);
		} catch (MessagingException e) {
			logger.error("",e);
			
		}

	}
	
	/**
	 * 
	 */
	@Test	
	public void testPropValidation(){
		
		PessoaVO pessoaVO = new PessoaVO();

		pessoaVO.setEmail("darcio@gmail.com");
		
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		
		String email = pessoaVO.getEmail();
		
		Validator validator = validatorFactory.getValidator();
		
		Set<ConstraintViolation<PessoaVO>> critics = validator.validateProperty(pessoaVO, "email");
		
		
		
		for (ConstraintViolation<PessoaVO> constraintViolation : critics) {
			System.out.println(constraintViolation);
		}

		
	}

	@Test	
	public void testTokenizerOpcionais() {
		List<OpcionalVO> opcionais = opcionalBusiness.findAll();


	}

	@Test
	public void testFind() throws JazzBusinessException {
	
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(AnuncioVO.class);
		criteria.add(Restrictions.eq("PK", 36L));
		
		AnuncioVO anuncioVO = (AnuncioVO) criteria.uniqueResult();
		
		//session.close();

		
		System.out.println("opcionais: "+anuncioVO.getVeiculoVO().getOpcionais());
		
		
		session.flush();
		
		
		
	}
	
	
	@Test
	public void testInsert() throws JazzBusinessException {
		
		Session session = sessionFactory.openSession();
		

		AnuncioVO anuncioVO = new AnuncioVO();
		//AdesivoVO adesivoVO = anuncioVO.getAdesivoVO();
		
		/*
		ModeloAdesivoVO modeloAdesivoVO = new ModeloAdesivoVO();
		modeloAdesivoVO.setPK(1l);
		modeloAdesivoVO.setNome("teste");
		*/
		//adesivoVO.setModeloAdesivoVO(modeloAdesivoVO );
		
		/*
		Map<String, CampoAdesivoVO> campos = new HashMap<String, CampoAdesivoVO>();
				
		CampoAdesivoVO campoAdesivoVO1 = new CampoAdesivoVO(AdesivoVO.LINHA_ANO					,"10/10");
		CampoAdesivoVO campoAdesivoVO2 = new CampoAdesivoVO(AdesivoVO.LINHA_SUBTITULO		,"1.4 Flex R$12.000");
		CampoAdesivoVO campoAdesivoVO3 = new CampoAdesivoVO(AdesivoVO.LINHA_OPCIONAIS		,"Ar DH Trio Teto\nTratar com João");
		CampoAdesivoVO campoAdesivoVO4 = new CampoAdesivoVO(AdesivoVO.LINHA_CONTATOS		,"(11)99999-9999\n(11)98888-8888");
		CampoAdesivoVO campoAdesivoVO5 = new CampoAdesivoVO(AdesivoVO.LINHA_LINK 				,"http://bit.ly/");
		CampoAdesivoVO campoAdesivoVO6 = new CampoAdesivoVO(AdesivoVO.LINHA_PLACA 			,"DYG-3255");
		
		campos.put(AdesivoVO.LINHA_ANO			, campoAdesivoVO1);
		campos.put(AdesivoVO.LINHA_SUBTITULO, campoAdesivoVO2);
		campos.put(AdesivoVO.LINHA_OPCIONAIS, campoAdesivoVO3);
		campos.put(AdesivoVO.LINHA_CONTATOS	, campoAdesivoVO4);
		campos.put(AdesivoVO.LINHA_LINK 		, campoAdesivoVO5);
		campos.put(AdesivoVO.LINHA_PLACA 		, campoAdesivoVO6);
		
		adesivoVO.setCampos(campos);
		

		
		List<OpcionalVO> opcionais = opcionalDAO.findAll();
		
		
		VeiculoVO veiculoVO = new VeiculoVO();
		veiculoVO.setAnoFab(10);
		veiculoVO.setAnoMod(10);
		veiculoVO.setCambio(CambioEnum.MANUAL);
		veiculoVO.setCilindrada(CilindradaEnum._1_4);
		veiculoVO.setCombustivel(CombustivelEnum.FLEX);
		veiculoVO.setCor("Preto");
		//veiculoVO.setOpcionais(opcionais);
		veiculoVO.setPlaca("DYG-3255");
		veiculoVO.setPorta(2);
		veiculoVO.setQuilometragem(73000l);
		
		anuncioVO.setVeiculoVO(veiculoVO);
		
		
		anuncioVO.setValor(20000d);
		
		PessoaVO pessoaVO = new PessoaVO();
		pessoaVO.setNome("teste");
		//anuncioVO.setPessoaVO(pessoaVO );
		 */
		
		session.saveOrUpdate(anuncioVO);


		
		
		
		session.flush();
		session.close();
		
	
	}
	
	@BeforeTest
	public void setUp() {
		
	
	}
	
	
}
