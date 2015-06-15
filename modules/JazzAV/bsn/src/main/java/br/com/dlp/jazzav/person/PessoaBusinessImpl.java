/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzav.person;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.jazzav.StatusEnum;
import br.com.dlp.jazzav.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzav.endereco.EstadoVO;
import br.com.dlp.jazzav.endereco.LogradouroVO;

/**   
 * Incluir arquivo com comentários
 * 
 * Contrado de Business para o componente PessoaVO
 * 
 **/
@Component
@Transactional(propagation = Propagation.REQUIRED)
public class PessoaBusinessImpl extends AbstractJazzOMRBusinessImpl<PessoaVO> implements PessoaBusiness {

	private static final String SENHA_MAIL = "m4c4c0rabudO";
	protected static final String USUARIO_MAIL = "darcio.pacifico@gmail.com";
	protected static final String FROM_MAIL = "darcio.pacifico@gmail.com";

	private static final long serialVersionUID = -4018418907098545031L;
	public static final int TAMANHO_TOKEN = 30;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	protected PessoaVO findByEmailToken(String email, String resetToken) {

		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaVO.class);

		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("resetToken", resetToken));

		List<PessoaVO> pessoaVOs = hibernateTemplate.findByCriteria(criteria);

		PessoaVO pessoaVO = null;

		if (CollectionUtils.isNotEmpty(pessoaVOs)) {
			pessoaVO = pessoaVOs.get(0);
		}

		return pessoaVO;

	}

	/**
	 * Lista todos os estados
	 */
	@Override
	public List<EstadoVO> findEstados() {

		List<EstadoVO> estados = hibernateTemplate.find(" select e from EstadoVO e ");

		return estados;
	}

	@Override
	public PessoaVO saveOrUpdate(PessoaVO pessoaVO) throws JazzBusinessException {
		checkMailExistence(pessoaVO);

		return super.saveOrUpdate(pessoaVO);
	}

	/**
	 * @param pessoaVO
	 * @throws PessoaAlreadyExistException
	 */
	protected void checkMailExistence(PessoaVO pessoaVO) throws PessoaAlreadyExistException {
		if (isNovaPessoa(pessoaVO)) {
			PessoaVO pessoaBDVO = findByEmail(pessoaVO);

			if (pessoaBDVO != null) {
				throw new PessoaAlreadyExistException("Não foi possivel registrar novo cliente. Já existe uma pessoa registrada com este email!");
			}
		}
	}

	/**
	 * @param pessoaVO
	 * @return
	 */
	protected boolean isNovaPessoa(PessoaVO pessoaVO) {
		return pessoaVO.getPK() == null;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public List<LogradouroVO> findLogradourosByCEP(String cep) {

		List<LogradouroVO> logradouros = hibernateTemplate.find(" select l from LogradouroVO l " + "		left join fetch l.bairroVO b	" + "		left join fetch b.cidadeVO c	"
				+ "		left join fetch c.estadoVO e	" + "where l.cep=? ", cep);

		return logradouros;

	}

	/**
	 * Procura pessoa pelo email (deve ser único, existe uma constraint p/ isso...)
	 * 
	 * @param email
	 * @return
	 */
	protected PessoaVO findByEmail(PessoaVO pessoaVO) {

		PessoaVO pessoaRetorno = null;// não retornar o mesmo objeto pessoa do argumento, pois havera checagem de nulo

		String email = pessoaVO.getEmail();

		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaVO.class);

		criteria.add(Restrictions.eq("email", email));

		List<PessoaVO> pessoaVOs = hibernateTemplate.findByCriteria(criteria);

		if (CollectionUtils.isNotEmpty(pessoaVOs)) {
			pessoaRetorno = pessoaVOs.get(0);
		}

		return pessoaRetorno;

	}

	/**
	 * 
	 */
	@Override
	public PessoaVO revogarAcesso(PessoaVO pessoaVO) throws JazzBusinessException {

		pessoaVO.setStatus(StatusEnum.INACTIVE);

		if (pessoaVO.getPK() != null) {
			pessoaVO = saveOrUpdate(pessoaVO);
		}

		return pessoaVO;
	}

	/**
	 * Checa se existe o login informado
	 */
	@Override
	public Boolean existsLogin(String login) {

		if (StringUtils.isBlank(login)) {
			return false;
		}

		List<Long> pessoaPks = hibernateTemplate.findByNamedParam("select PK from PessoaVO where login = :login", "login", login);

		return CollectionUtils.isNotEmpty(pessoaPks);
	}

	/**
	 * 
	 */
	@Override
	public void includeUpdateValidations(PessoaVO pessoaVO) throws JazzBusinessException {

		/*
		 * String queryString = " select 			eve.PK" + " from 				EventVO eve " + " inner join 	eve.participations par " + " inner join 	par.examVariantVO exv " +
		 * " inner join 	exv.examVO exa " + " where exa = :examVO ";
		 * 
		 * List<Long> eventPKs = hibernateTemplate.findByNamedParam(queryString, "examVO", pessoaVO);
		 * 
		 * if(CollectionUtils.isNotEmpty(eventPKs)){ throw new
		 * JazzOMRBusinessConsistencyException("Este exame não pode ser alterado ou excluído porque está sendo utilizado pelos eventos a seguir "+eventPKs+"!"); }
		 */
		super.includeUpdateValidations(pessoaVO);
	}

	/**
	 * 
	 */
	@Override
	public void deleteValidations(PessoaVO pessoaVO) throws JazzBusinessException {
		this.includeUpdateValidations(pessoaVO);
		super.deleteValidations(pessoaVO);
	}

	@Autowired
	private PessoaDAO pessoaDAO;

	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * 
	 * @author darcio
	 * 
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status) {
		return pessoaDAO.findPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	@Override
	public PessoaVO update(PessoaVO iBaseVO) throws JazzBusinessException {
		PessoaVO pessoa = super.update(iBaseVO);
		pessoa.setNovo(false);
		return pessoa;
	}

	/**
	 * Retorna o registro de pessoa para o Principal logado
	 */
	@Override
	public PessoaVO findPessoa(Principal principal) {

		if (principal == null || principal.getName().equals("Anonymous")) {
			return null;
		}

		String loginName = SecurityHelper.getInstance().getLoginName(principal);

		if (StringUtils.isBlank(loginName)) {
			return null;
		}

		DetachedCriteria criteria = createDetachedCriteria(PessoaVO.class);

		criteria.add(Restrictions.eq("email", loginName));

		List<PessoaVO> pessoas = hibernateTemplate.findByCriteria(criteria);

		PessoaVO pessoaVO = null;

		if (CollectionUtils.isNotEmpty(pessoas)) {
			pessoaVO = pessoas.get(0);
		}

		return pessoaVO;
	}

	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * 
	 * @author darcio
	 * 
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	public Long countPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status) {

		// Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return pessoaDAO.countPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * 
	 * @author darcio
	 * 
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @param extraArgumentsDTO
	 *          Paginacao e Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status,
			ExtraArgumentsDTO extraArgumentsDTO) {
		return pessoaDAO.findPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<PessoaVO> getDao() {
		return pessoaDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public PessoaVO newVO() {
		return new PessoaVO();
	}

	@Override
	public void resetSenha(PessoaVO pessoaVO) throws JazzBusinessException {

		// Gera token para o reset
		String resetToken = RandomStringUtils.random(TAMANHO_TOKEN, true, true);

		// registra token na base
		registraTokenEsperado(pessoaVO, resetToken);

		// envia email para reset de senha
		sendMailResetSenha(pessoaVO, resetToken);

	}

	/**
	 * @param pessoaVO
	 * @param resetToken
	 * @throws JazzBusinessException
	 */
	protected void registraTokenEsperado(PessoaVO pessoaVO, String resetToken) throws JazzBusinessException {

		pessoaVO = findByEmail(pessoaVO);

		if (pessoaVO == null) {
			throw new EmailNotFoundException("Nenhum cliente foi encontrado com o email informado!");
		}

		pessoaVO.setResetToken(resetToken);
		saveOrUpdate(pessoaVO);
	}

	/**
	 * @param pessoaVO
	 * @param resetToken
	 * @throws ResetException
	 */
	protected void sendMailResetSenha(PessoaVO pessoaVO, String resetToken) throws ResetException {
		// Recipient's email ID needs to be mentioned.
		String usuarioMail = USUARIO_MAIL;
		String senhaMail = SENHA_MAIL;

		String to = pessoaVO.getEmail();
		String from = FROM_MAIL;

		String assunto = "Assunto email!";
		String corpoMail = "<html> " + "	<head> " + "		<title>Reset Senha</title> " + "	</head> " + "	<body> " + resetToken + "	</body> " + "</html> ";

		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
		props.put("mail.transport.protocol", "smtp"); // define protocolo de envio como SMTP
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // server SMTP do GMAIL
		props.put("mail.smtp.auth", "true"); // ativa autenticacao
		props.put("mail.smtp.user", from); // usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", "465"); // porta
		props.put("mail.smtp.socketFactory.port", "465"); // mesma porta para o socket
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		SimpleAuth auth = new SimpleAuth(usuarioMail, senhaMail);

		// Get the default Session object.
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, auth);

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
			throw new ResetException("Erro ao tentar enviar email", e);
		} catch (MessagingException e) {
			throw new ResetException("Erro ao tentar enviar email", e);
		}
	}

	/**
	 * Confirma alteracao de senha
	 */
	@Override
	public PessoaVO confirmResetPessoa(PessoaVO pessoaTelaVO) throws EmailNotFoundException, InvalidResetTokenException, NotWaitingForResetException, JazzBusinessException {

		// encontra pessoa do email
		PessoaVO pessoaBDVO = findByEmail(pessoaTelaVO);

		// verifica token
		checkResetToken(pessoaBDVO, pessoaTelaVO);

		// atribui nova senha
		atribuiNovaSenha(pessoaBDVO, pessoaTelaVO);

		// salva alteracoes
		saveOrUpdate(pessoaBDVO);

		return pessoaBDVO;
	}

	/**
	 * Atribui nova senha vinda da UI para o registro de pessoa, alem de apagar do token da BD
	 * 
	 * @param pessoaTelaVO
	 * @param pessoaBDVO
	 */
	protected void atribuiNovaSenha(PessoaVO pessoaBDVO, PessoaVO pessoaTelaVO) {
		pessoaBDVO.setSenha(pessoaTelaVO.getSenha());
		pessoaBDVO.setSenhaConfirm(pessoaTelaVO.getSenhaConfirm());
		pessoaBDVO.setResetToken(null);
	}

	/**
	 * Efetua checagens de token e email
	 * 
	 * @param mailResetToken
	 * @param pessoaBDVO
	 * @throws EmailNotFoundException
	 * @throws NotWaitingForResetException
	 * @throws InvalidResetTokenException
	 */
	protected void checkResetToken(PessoaVO pessoaBDVO, PessoaVO pessoaTelaVO) throws EmailNotFoundException, NotWaitingForResetException, InvalidResetTokenException {

		if (pessoaBDVO == null) {// checa se existe usuario para o email informado
			throw new EmailNotFoundException("Não foi encontrado nenhum usuário registrado com o email informado!");

		} else if (StringUtils.isBlank(pessoaBDVO.getResetToken())) {// checa se este usuário aguarda uma alteracao de senha
			throw new NotWaitingForResetException("Não há resetToken registrado para o usuário informado!");

		} else if (!pessoaBDVO.getResetToken().equals(pessoaTelaVO.getResetToken())) {// checa se o token informado é válido
			throw new InvalidResetTokenException("O token de redefinição de senha informado não é igual ao token esperado!");
		}
	}

	/**
	 * IMplementação simples de autenticator para javax mail
	 * 
	 * @author darcio
	 * 
	 */
	protected class SimpleAuth extends Authenticator {
		public String username = null;
		public String password = null;

		public SimpleAuth(String user, String pwd) {
			username = user;
			password = pwd;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

}  