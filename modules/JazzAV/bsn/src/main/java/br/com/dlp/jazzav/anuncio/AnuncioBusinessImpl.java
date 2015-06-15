package br.com.dlp.jazzav.anuncio;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzav.adesivo.AdesivoVO;
import br.com.dlp.jazzav.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzav.person.EnderecoVO;
import br.com.dlp.jazzav.person.PessoaVO;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Currency;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.Shipping;
import br.com.uol.pagseguro.domain.ShippingType;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.TransactionStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.NotificationService;
import br.com.uol.pagseguro.service.TransactionSearchService;

/**
 * 
 * @author darcio
 *
 */
@Component
@Transactional(propagation=Propagation.REQUIRED)
public class AnuncioBusinessImpl extends AbstractJazzOMRBusinessImpl<AnuncioVO> implements AnuncioBusiness{
	
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private AnuncioDAO anuncioDAO;

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	protected TransactionSearchService tss = new TransactionSearchService();
	
	
	/**
	 * Executa confirmacao do registro do anuncio, a partir do id de transacao informado
	 * @throws JazzAVPaymentException 
	 */
	@Override
	public AnuncioVO confirmarAnuncio(String id_pagseguro) throws TransacaoNaoEncontradaException, JazzAVPaymentException {

		//recupera credenciais para invocar servico pagseguro
		Credentials credentials = getCredentials();
		
		// solicta transacao para o pagseguro atraves da API especifica
		Transaction transaction = getTransaction(id_pagseguro, credentials);


		// recupera o registro do anuncio internamente
		AnuncioVO anuncio = getAnuncioVO(transaction);

		// registra historico de mudanca de status
		registraMudancaStatus(transaction, anuncio, "Confirmacao de registro de solicitacao de pagamento no pagseguro");
		
		return anuncio;
	}
	

	/**
	 * 
	 * @return
	 * @throws JazzAVPaymentException 
	 */
	@Override
	public AnuncioVO findAnuncioVOByTransaction(String id_pagseguro) throws JazzAVPaymentException{
		

		//recupera credenciais para invocar servico pagseguro
		Credentials credentials = getCredentials();
		
		// solicta transacao para o pagseguro atraves da API especifica
		Transaction transaction = getTransaction(id_pagseguro, credentials);

		// recupera o registro do anuncio internamente
		AnuncioVO anuncio = getAnuncioVO(transaction);

		return anuncio;
		
	}
	

	/**
	 * Pesquisa notificacao informada e registra alteracoes de status
	 * @param notificationCode
	 * @throws JazzAVPaymentException 
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public AnuncioVO registraNotificacao(String notificationCode) throws TransacaoNaoEncontradaException, JazzAVPaymentException {
		//recupera credenciais para invocar servico pagseguro
		Credentials credentials = getCredentials();

		// solicta transacao para o pagseguro atraves da API especifica
		Transaction transaction = getTransaction(notificationCode, credentials);
		
		// recupera o registro do anuncio internamente
		AnuncioVO anuncio = getAnuncioVO(transaction);

		// registra historico de mudanca de status
		registraMudancaStatus(transaction, anuncio, "Atualizacao automatica de historico");
		
		return anuncio;
	}


	/**
	 * @param transaction
	 * @param anuncio
	 * @param logMessage TODO
	 * @param res 
	 * @throws IOException 
	 */
	protected void registraMudancaStatus(Transaction transaction, AnuncioVO anuncio, String logMessage) throws JazzAVPaymentException {
		PaymentStatusEnum paymentStatus = getPaymentStatusEnum(transaction);

		AnuncioStatusVO anuncioStatusVO = new AnuncioStatusVO();
		anuncioStatusVO.setDescString(logMessage);
		anuncioStatusVO.setDtInc(new Date());
		anuncioStatusVO.setDtAlt(new Date());
		anuncioStatusVO.setPaymentStatus(paymentStatus);
		anuncio.getHistoricoStatus().add(anuncioStatusVO);

		try {
			saveOrUpdate(anuncio);
			log.debug("registro alterado com sucesso");
		} catch (JazzBusinessException e) {
			
			log.error("Erro ao tentar registrar atualizacao de historico", e);
			throw new JazzAVPaymentException("Erro ao tentar registrar atualizacao de historico", e);
			

		}

	}

	/**
	 * @param transaction
	 * @return
	 */
	protected PaymentStatusEnum getPaymentStatusEnum(Transaction transaction) {
		TransactionStatus status = transaction.getStatus();
		PaymentStatusEnum paymentStatus = PaymentStatusEnum.values()[status.getValue()];
		return paymentStatus;
	}

	/**
	 * @param res 
	 * @param transaction
	 * @return
	 * @throws ServletException 
	 */
	protected AnuncioVO getAnuncioVO(Transaction transaction) throws JazzAVPaymentException {

		String reference = transaction.getReference();
		
		AnuncioVO anuncio;
		if(StringUtils.isNotBlank(reference) && StringUtils.isNumeric(reference)){
			anuncio = findByPK(new Long(reference));
			
			if(anuncio==null){
				log.error("Anuncio com codigo de referencia=" + reference + ", transaction= "+transaction+" nao foi encontrado!");
				throw new JazzAVPaymentException("Anuncio com codigo de referencia=" + reference + ", transaction= "+transaction+" nao foi encontrado!");
			}
			
		}else{
			log.error("Anuncio com codigo de referencia=" + reference + " invalido, transaction= "+transaction);
			throw new JazzAVPaymentException("Anuncio com codigo de referencia=" + reference + " invalido, transaction= "+transaction);
		}
		
		return anuncio;
	}

	/**
	 * @param res 
	 * @param notificationCode
	 * @param credentials
	 * @return
	 * @throws JazzAVPaymentException 
	 */
	protected Transaction getTransaction(String notificationCode, Credentials credentials) throws JazzAVPaymentException  {
		Transaction transaction;
		try {
			transaction = tss.searchByCode(credentials, notificationCode);
		} catch (PagSeguroServiceException e) {
			
		
			log.error("Erro ao tentar recuperar a transacao referente a notificacao (notificationCode=" + notificationCode + ")", e);
			throw new JazzAVPaymentException("Erro ao tentar recuperar a transacao referente a notificacao (notificationCode=" + notificationCode + ")", e);
		}
		return transaction;
	}
	
	
	@Override
	public List<ModeloAdesivoVO> findAllModelos() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ModeloAdesivoVO.class);
		
		List<ModeloAdesivoVO> modelos = hibernateTemplate.findByCriteria(criteria);
		
		return modelos;
	}
	
	/**
	 * 
	 */
	@Override
	public ModeloAdesivoVO getModeloPadrao() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ModeloAdesivoVO.class);
		
		List<ModeloAdesivoVO> modelos = hibernateTemplate.findByCriteria(criteria,0,1);
		ModeloAdesivoVO modeloAdesivoVO=null;
		
		if(modelos.size()>0){
			modeloAdesivoVO = modelos.get(0);
		}
		
		return modeloAdesivoVO;
		
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<AnuncioVO> getDao() {
		return anuncioDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public AnuncioVO newVO() {
		return new AnuncioVO();
	}
	
	
	/**
	 * 
	 * @param anuncio
	 * @return 
	 * @throws JazzAVPaymentException 
	 */
	@Override
	public URL efetuarPagamento(AnuncioVO anuncio) throws JazzAVPaymentException{
		//09EE605E38374CB4B23E704CDCD7D8D2
		
		try {
			saveOrUpdate(anuncio);
		} catch (JazzBusinessException e1) {
			throw new JazzRuntimeException("Não é possível salvar o anuncio antes de submeter para meio de pagamento!");
		}
		
		PessoaVO pessoaVO = anuncio.getPessoaVO();
		EnderecoVO enderecoVO = pessoaVO.getEnderecoVO();
		
		if(anuncio.getPK()==null){
			throw new JazzRuntimeException("Não é possível submeter o pagamento se o anuncio não foi salvo e possui PK!");
		}
		
		PaymentRequest paymentRequest = new PaymentRequest();  
		
		paymentRequest.setReference(anuncio.getPK()+"");
		paymentRequest.setCurrency(Currency.BRL);
		
		
		BigDecimal shipAmount = new BigDecimal("5.00");
		
		
		paymentRequest.addItem("1", "Par de Adesivos VENDE-SE", 1, new BigDecimal("4.00"), AnuncioVO.PESO_PADRAO,  shipAmount );
		
		

		
		paymentRequest.setShippingType(ShippingType.PAC);
		
		Sender sender = new Sender();
		sender.setName(pessoaVO.getNome());
		sender.setEmail(pessoaVO.getEmail());
		Phone phone = new Phone("11", pessoaVO.getTelefone1());
		sender.setPhone(phone);
		
		paymentRequest.setSender(sender);
		
		fillShippingAddress(paymentRequest, enderecoVO);
		
		Credentials credentials=getCredentials();
		
		
		URL url;
		try { //https://pagseguro.uol.com.br/v2/checkout/payment.html?code=99795A445E5E413884192F9ED21BDFCC
			
			URL redirectURL = new URL("http://www.adesivovendese.com.br/confirmacao/confirmacao.jsf");
			paymentRequest.setRedirectURL(redirectURL );
		
			
			
			
			url = paymentRequest.register(credentials);
			
		} catch (PagSeguroServiceException e) {
			throw new JazzAVPaymentException(e,"Erro ao tentar registrar pedido de pagamento!");
		} catch (MalformedURLException e) {
			throw new JazzAVPaymentException(e,"Erro ao tentar especificar a url de retorno!");
		}
		
		return url;
		
	}

	/**
	 * @return
	 */
	protected AccountCredentials getCredentials() {
		return new AccountCredentials("darcio.pacifico@gmail.com", "09EE605E38374CB4B23E704CDCD7D8D2");
	}

	/**
	 * @param paymentRequest
	 * @param enderecoVO
	 */
	protected void fillShippingAddress(PaymentRequest paymentRequest,
			EnderecoVO enderecoVO) {
		String country= enderecoVO.getPais();
		String state = enderecoVO.getUf();
		String city = enderecoVO.getCidade();
		String district = enderecoVO.getBairro();
		String postalCode = enderecoVO.getCep();
		String street = enderecoVO.getLogradouro();
		String number = enderecoVO.getNumero();
		String complement = enderecoVO.getComplemento();
		paymentRequest.setShippingAddress(country, state, city, district, postalCode, street, number, complement);
	}

}
