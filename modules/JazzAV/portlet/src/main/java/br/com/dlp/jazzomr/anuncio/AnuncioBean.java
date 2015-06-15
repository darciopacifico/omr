/**

 * 
 */
package br.com.dlp.jazzomr.anuncio;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;
import br.com.dlp.jazzav.adesivo.AdesivoVO;
import br.com.dlp.jazzav.adesivo.CampoAdesivoVO;
import br.com.dlp.jazzav.anuncio.AnuncioBusiness;
import br.com.dlp.jazzav.anuncio.AnuncioVO;
import br.com.dlp.jazzav.anuncio.CambioEnum;
import br.com.dlp.jazzav.anuncio.CilindradaEnum;
import br.com.dlp.jazzav.anuncio.CombustivelEnum;
import br.com.dlp.jazzav.anuncio.JazzAVPaymentException;
import br.com.dlp.jazzav.anuncio.ModeloAdesivoVO;
import br.com.dlp.jazzav.anuncio.TransacaoNaoEncontradaException;
import br.com.dlp.jazzav.endereco.EstadoVO;
import br.com.dlp.jazzav.endereco.LogradouroVO;
import br.com.dlp.jazzav.opcionais.OpcionalBusiness;
import br.com.dlp.jazzav.person.EAuthority;
import br.com.dlp.jazzav.person.EmailNotFoundException;
import br.com.dlp.jazzav.person.EnderecoVO;
import br.com.dlp.jazzav.person.InvalidResetTokenException;
import br.com.dlp.jazzav.person.NotWaitingForResetException;
import br.com.dlp.jazzav.person.PessoaAlreadyExistException;
import br.com.dlp.jazzav.person.PessoaBusiness;
import br.com.dlp.jazzav.person.PessoaVO;
import br.com.dlp.jazzav.produto.OpcionalVO;
import br.com.dlp.jazzav.produto.TipoOpcionalEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Managed bean responsável pelo controle e conversacao da edicao do anuncio
 * @author darcio
 */
@ManagedBean
@SessionScoped
@Scope(value="session")
@Component
public class AnuncioBean extends AbstractJSFBeanImpl<AnuncioVO> {

	//09EE605E38374CB4B23E704CDCD7D8D2
	protected static final Integer PAGE_ADESIVO = new Integer(0);
	private static final long serialVersionUID = 1831552858889190283L;
	protected final int yearListSize = 80;
	protected final int anoIncModelo = 1;

	protected AnuncioVO anuncioVO = new AnuncioVO();

	Map<TipoOpcionalEnum, List<SelectItem>> mapOpcionais;
	protected Map<Long, ModeloAdesivoVO> mapaModelos = new HashMap<Long, ModeloAdesivoVO>();
	

	private Boolean mostrarRedefinirSenha=false;
	
	@Autowired
	protected AnuncioBusiness anuncioBusiness;
	
	@Autowired
	protected OpcionalBusiness opcionalBusiness;
	
	@Autowired
	private PessoaBusiness pessoaBusiness;
	
	

	
	
	@Autowired
	@Qualifier("jazzAuthManager")
	protected AuthenticationManager authenticationManager;
	

	public String getConfirmarPagamento(){
		
		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		FacesContext context = FacesContext.getCurrentInstance();
		
		String id_pagseguro = req.getParameter("id_pagseguro");
		
		AnuncioVO anuncioVO;
		try {
			anuncioVO = anuncioBusiness.confirmarAnuncio(id_pagseguro);
			setAnuncioVO(anuncioVO);
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Seu pedido foi registrado com sucesso! Logo lhe enviaremos emails de confirmação!", ""));
			
		} catch (JazzBusinessException e) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Seu pedido foi registrado com sucesso!!", ""));
		}
		
		
		return id_pagseguro;
		
	}
	
	/**
	 * Reseta senha do cliente
	 */
	public void resetSenha() {

		FacesContext context = FacesContext.getCurrentInstance();

		// valida email e retorna colecao de criticas, caso haja alguma
		Set<ConstraintViolation<PessoaVO>> constraints = validaEmailPessoa();

		// nenhum erro no email
		if (constraints!=null) {

			try {

				// envia email resetando senha
				pessoaBusiness.resetSenha(getPessoaVO());

				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Um email de recuperação de senha foi enviado para " + getPessoaVO().getEmail() + "!", ""));

			} catch (EmailNotFoundException e) {
				log.warn("Email não encontrado em nossas base de clientes! " + getPessoaVO(), e);

				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Email não encontrado em nossas base de clientes!",
						"Email não encontrado em nossas base de clientes!"));
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor, registre-se!", "Por favor, registre-se!"));

			} catch (JazzBusinessException e) {
				log.error("Não foi possível enviar email de recuperação de senha. Tente novamente mais tarde. " + getPessoaVO(), e);

				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao tentar enviar email de recuperacao de senha!", ""));
			}
		}

	}

	/**
	 * 
	 * @return
	 */
	public String confirmReset() {
		FacesContext context = FacesContext.getCurrentInstance();
		String flow = "resetConfirm";

		try {
			PessoaVO pessoaVO = pessoaBusiness.confirmResetPessoa(getPessoaVO());

			//200113 alterei o fluxo, movendo as duas linhas abaixo que estavam após o catch p/ cá...
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha redefinida com sucesso!", ""));
			autenticaUsuario(pessoaVO);
			
		} catch (EmailNotFoundException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Email não encontrado!", ""));
			flow = null;

		} catch (InvalidResetTokenException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Token inválido!", ""));
			flow = null;

		} catch (NotWaitingForResetException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Redefinição de senha não esperado!", ""));
			flow = null;
			
		} catch (JazzBusinessException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro desconhecido ao tentar redefinir senha. Tente novamente mais tarde! ("+e.getMessage()+")!", ""));
			flow = null;
			
		}


		// anuncio
		return flow;
	}

	/**
	 * @return
	 */
	protected Set<ConstraintViolation<PessoaVO>> validaEmailPessoa() {
		FacesContext context2 = FacesContext.getCurrentInstance();

		Validator validator = validatorFactory.getValidator();

		Set<ConstraintViolation<PessoaVO>> constraints = validator.validateProperty(getPessoaVO(), "email");

		if ((constraints)!=null) {
			for (ConstraintViolation<PessoaVO> constraintViolation : constraints) {
				context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, constraintViolation.getMessage(), constraintViolation.getMessage()));
			}
		}
		return constraints;
	}


	
		
	/**
	 * Registra a pessoa e já efetua login
	 * 
	 * @return
	 */
	public String salvarPessoa() {

		String returnString = null;

		try {

			boolean valid = validateVO(getPessoaVO());

			getPessoaVO().getAuthorities().add(EAuthority.COMPRADOR);

			if (valid) {
				pessoaBusiness.saveOrUpdate(getPessoaVO());
				returnString = "checkout";

				autenticaUsuario(getPessoaVO());

			}

		} catch (PessoaAlreadyExistException e) {

			log.error("Já existe um usuário registrado com este email!", e);

			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage("txtEmail", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Já existe um usuário registrado com este email! ",""));
			context.addMessage("txtEmail", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Para redefinir sua senha clique em 'Redefinir Senha'! ",""));
			
			
			setMostrarRedefinirSenha(true);

		} catch (JazzBusinessException e) {

			log.error("Erro ao tentar registrar usuario!", e);
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar registrar usuario!");

		} catch (ConstraintViolationException e) {

			log.error("Erro ao tentar registrar usuario!", e);
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar registrar usuario!");

		} catch (Exception e) {
	
			log.error("Erro ao tentar registrar usuario!", e);
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar registrar usuario!");
	
		}

		return returnString;

	}

	/**
	 * Autentica o usuário recem criado
	 * 
	 * @param pessoaVO2
	 */
	protected void autenticaUsuario(PessoaVO pessoaVO2) {

		autenticaUsuario(pessoaVO2.getEmail(), pessoaVO2.getSenha());

	}

	/**
	 * Autentica o usuário recem criado
	 * 
	 * @param usuario
	 * @param senha
	 */
	protected void autenticaUsuario(String usuario, String senha) {
		// forca autenticacao
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario, senha);

		token.setDetails(new WebAuthenticationDetails(request));

		Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}


	/***
	 * MOstrar redefinir senha
	 * @return
	 */
	public Boolean getMostrarRedefinirSenha() {
		
		return mostrarRedefinirSenha;
		
	}

	public void setMostrarRedefinirSenha(Boolean mostrarRedefinirSenha) {
		this.mostrarRedefinirSenha = mostrarRedefinirSenha;
	}



	
	
	
	public void efetuarPagamento(){
		URL url;
		
		try {
			url = anuncioBusiness.efetuarPagamento(anuncioVO);
			
		} catch (JazzAVPaymentException e) {
			
			log.error("Erro ao tentar registrar pagamanento para o anuncio "+anuncioVO.getPK(),e);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar registrar pedido de pagamento!",""));
		
			return ;
		}

		log.debug(url.toString());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url.toString());
		} catch (IOException e) {

			log.error("Erro ao tentar redirecionar para pagina do meio de pagamento ("+url+")");
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar redirecionar para pagina do meio de pagamento!",""));

		}
		
	}
	
	
	
	/**
	 * 
	 */
	public void completarEndereco(){
		
		String cep = anuncioVO.getPessoaVO().getEnderecoVO().getCep();
		
		//tira todos os caracteres não digito
		cep = cep.replaceAll("\\D", "");
		
		List<LogradouroVO> logradouros = pessoaBusiness.findLogradourosByCEP(cep);
		
		if(logradouros.size()>0){
			
			LogradouroVO logradouroVO = logradouros.get(0);
			
			anuncioVO.getPessoaVO().getEnderecoVO().setCidade(logradouroVO.getBairroVO().getCidadeVO().getDescricao());
			anuncioVO.getPessoaVO().getEnderecoVO().setComplemento(logradouroVO.getComplemento());
			anuncioVO.getPessoaVO().getEnderecoVO().setLogradouro(logradouroVO.getLogradouro());
			anuncioVO.getPessoaVO().getEnderecoVO().setUf(logradouroVO.getBairroVO().getCidadeVO().getEstadoVO().getSigla());
			anuncioVO.getPessoaVO().getEnderecoVO().setLogradouro(logradouroVO.getLogradouro());
			
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CEP não encontrado! Por favor, tente novamente ou preencha o enderço ",""));
		}
		
		
		
	}



	/**
	 * 
	 */
	protected void completarTelefones() {
		
		
		CampoAdesivoVO campoContato = anuncioVO.getAdesivoVO().getCampos().get(AdesivoVO.LINHA_CONTATOS);
		
		if(campoContato!=null && !anuncioVO.getPessoaVO().isPossuiTelefone()){

				anuncioVO.getPessoaVO().parseDDDTelefone(campoContato.getValor());
				setPessoaVO(anuncioVO.getPessoaVO());
			
		}
	}



	/**
	 * Lista estados e cria itens SelectItem para preencher combo
	 * TODO: CACHE
	 * @return
	 */
	public List<String> listaEstados(){
	
		List<EstadoVO> estados = pessoaBusiness.findEstados();
		
		List<String> stadosStr = new ArrayList<String>(estados.size());
		
		for (EstadoVO estadoVO : estados) {
			

			stadosStr.add(estadoVO.getSigla());
			
		}
		
		return stadosStr;
		
	}
	
	
	/**
	 * Muda a selecao de modelo de adesivo
	 * @param modelo
	 */
	public void switchModel(ModeloAdesivoVO modelo){
		
		this.getAnuncioVO().getAdesivoVO().setModeloAdesivoVO(modelo);
		
	}

	/**
	 * iniciar processo de checkout
	 * @return
	 */
	public String checkout(){
		
		//associarUsuarioAoAnuncio();
		
		completarTelefones();
		
		String string = "checkout";
		return string;
	} 
	

	/**
	 * Associa a pessoa logada ao anuncio que está sendo editado
	 */
	protected void associarUsuarioAoAnuncio() {
		
		PessoaVO pessoaLogada = getPessoaLogadaVO();
		
		anuncioVO.setPessoaVO(pessoaLogada);
		
	}



	/**
	 * Salva o anuncio que esta sendo editado
	 * Caso o cliente nao esteja logado, salva no cookie ou numero de sessao do usuario
	 */
	public void salvaAnuncio(){
			
		try {
			
			PessoaVO pessoaVO = getPessoaLogadaVO();
						
			anuncioVO = anuncioBusiness.saveOrUpdate(anuncioVO);
			
			System.out.println(anuncioVO.getPK());
			
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar salvar o anúncio: "+e.getMessage());
			log.error("Erro ao tentar salvar o anúncio!",e);
		}
	}


	
	/**
	 * 
	 * @return
	 */
	public Map<TipoOpcionalEnum, List<SelectItem>> getMapOpcionais(){
		
		
		if(mapOpcionais==null){
			
			mapOpcionais = new HashMap<TipoOpcionalEnum, List<SelectItem>>(TipoOpcionalEnum.values().length);
			
			List<OpcionalVO> opcionais = opcionalBusiness.findAll();
			
			for (OpcionalVO opcionalVO : opcionais) {
				
				List<SelectItem> itensOcionais = getListaOpcionais(mapOpcionais,opcionalVO);
				
				SelectItem item = createSelectItem(opcionalVO);
				
				itensOcionais.add(item);
				
			}
		}
		
		return mapOpcionais;
	}

	
	public List<SelectItem> getOpcionais(TipoOpcionalEnum tipoOpcionalEnum){
		return getMapOpcionais().get(tipoOpcionalEnum);
	}
	

	/**
	 * 
	 * @return
	 */
	public List<TipoOpcionalEnum> getTiposOpcionais(){
		
		return getEnumsType(TipoOpcionalEnum.class);
		
	}
	

	protected SelectItem createSelectItem(OpcionalVO opcionalVO) {
		SelectItem item = new SelectItem();
		item.setValue(opcionalVO);
		item.setLabel(opcionalVO.getNome());
		return item;
	}


	

	protected List<SelectItem> getListaOpcionais(Map<TipoOpcionalEnum, List<SelectItem>> mapOpcionais,OpcionalVO opcionalVO) {
		
		
		List<SelectItem> itensOcionais = mapOpcionais.get(opcionalVO.getTipoOpcional());
		
		if(itensOcionais==null){
			itensOcionais = new ArrayList<SelectItem>();
			mapOpcionais.put(opcionalVO.getTipoOpcional(), itensOcionais);
		}
		
		return itensOcionais;
	}
	
	
	
	/**
	 * Recupera lista do enum cambio
	 * @return
	 */
	public List<SelectItem> getCambioList() {
		return getEnums(CambioEnum.class);
	}
	
	
	/**
	 * Recupera lista do enum cambio
	 * @return
	 */
	public List<SelectItem> getCilindradaList() {
		return getEnums(CilindradaEnum.class);
	}
	
	/**
	 * Recupera lista do enum cambio
	 * @return
	 */
	public List<SelectItem> getCombustivelList() {
		return getEnums(CombustivelEnum.class);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<SelectItem> getAnosFab(){
		
		List<SelectItem> anos = new ArrayList<SelectItem>(yearListSize+1);
		
		int anoMax = Calendar.getInstance().get(Calendar.YEAR)+1;
		
		for(int i=0; i<yearListSize ;i++){
			
			Integer ano = anoMax-i;
			
			String label = (ano+"").substring(2);
			
			
			SelectItem anoItem = new SelectItem(ano, label);
			
			anos.add(anoItem );
			
		}
		
		
		return anos;
		
	}
	
	
	/**
	 * Retorna lista dos modelos de adesivo disponíveis
	 * @return
	 */
	public List<ModeloAdesivoVO> getModelos(){
		List<ModeloAdesivoVO> modelos = anuncioBusiness.findAllModelos();
		
		for (ModeloAdesivoVO modeloAdesivoVO : modelos) {
			mapaModelos.put(modeloAdesivoVO.getPK(), modeloAdesivoVO);
		}
		
		return modelos;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<SelectItem> getAnosMod(){
		
		List<SelectItem> anos = new ArrayList<SelectItem>(yearListSize+1);
		
		int anoMax = Calendar.getInstance().get(Calendar.YEAR)+1;
		
		Integer anoFab = getAnuncioVO().getVeiculoVO().getAnoFab();
		
		anoFab = anoFab==null?anoMax:anoFab;

		getAnuncioVO().getVeiculoVO().setAnoMod(anoFab);
		
		if(anoFab<anoMax){
			anoMax = anoFab+anoIncModelo;
		}
		
		int anoMin = anoFab-anoIncModelo;
		
		for(int ano=anoMax; ano>=anoMin ;ano--){
			
			String label = (ano+"").substring(2);
			
			SelectItem anoItem = new SelectItem(ano, label);
			
			
			
			anos.add(anoItem );
		}
		
		return anos;
		
	}
	
	
	/**
	 * Desenha imagem do modelo de adesivo escolhido
	 * @param out
	 * @param data
	 * @throws AVImageGeneratorException 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void paintModeloAnuncio(OutputStream out, Object data) throws AVImageGeneratorException {
		
		
		ModeloAdesivoVO modeloAdesivoVO = this.getAnuncioVO().getAdesivoVO().getModeloAdesivoVO();
		
		fillOutputWithImg(out, modeloAdesivoVO, 400, 360);
		
	}
	
	
	/**
	 * Desenha imagem do modelo de adesivo escolhido
	 * @param out
	 * @param data
	 * @throws AVImageGeneratorException 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void paintModeloAnuncioCheckout(OutputStream out, Object data) throws AVImageGeneratorException {
		
		ModeloAdesivoVO modeloAdesivoVO = this.getAnuncioVO().getAdesivoVO().getModeloAdesivoVO();
		
		fillOutputWithImg(out, modeloAdesivoVO, 400, 360);
	}
	
	
	/**
	 * 
	 * @param out
	 * @param modeloAdesivoVO
	 * @throws AVImageGeneratorException 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void paintModeloSample(OutputStream pOut, Object object) throws AVImageGeneratorException  {
		
		
		Long idModelo = (Long) object;
		
		ModeloAdesivoVO modeloAdesivoVO = mapaModelos.get(idModelo);
		 
		fillOutputWithImg(pOut, modeloAdesivoVO, 110, 70);
		
	}

	/**
	 * 
	 * @param out
	 * @param modeloAdesivoVO
	 * @param width TODO
	 * @param height TODO
	 * @throws AVImageGeneratorException 
	 * @throws IOException
	 * @throws TemplateException
	 */
	protected void fillOutputWithImg(OutputStream out, ModeloAdesivoVO modeloAdesivoVO, int width, int height) throws AVImageGeneratorException  {
		
		if(modeloAdesivoVO==null){
			modeloAdesivoVO = getModeloPadrao();
			
			if(modeloAdesivoVO==null){
				return ;
			}

			this.getAnuncioVO().getAdesivoVO().setModeloAdesivoVO((ModeloAdesivoVO) modeloAdesivoVO);
			
		}
		
		Map rootMap = anuncioVO.getCampos();

		writeJasperModel(out, width, height, rootMap, modeloAdesivoVO);
	}


	/**
	 * 
	 * @param out
	 * @param width
	 * @param height
	 * @param rootMap
	 * @param adesivoModel
	 * @throws AVImageGeneratorException 
	 */
	protected void writeJasperModel(OutputStream out, int width, int height, Map rootMap, ModeloAdesivoVO modeloAdesivoVO) throws AVImageGeneratorException {
		
		
		try {
		
			JasperReport compiled = getJasperReport(modeloAdesivoVO);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiled, rootMap, new JREmptyDataSource(1));

			int pageWidth = width;
			float scale = (float)width / (float)jasperPrint.getPageWidth();
			int pageHeight = (int)((float)jasperPrint.getPageHeight()*(float)scale);
			
			
			BufferedImage pageImage = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_RGB);
			JRGraphics2DExporter exporter = new JRGraphics2DExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, pageImage.getGraphics());
			
			exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, scale);
			
			exporter.setParameter(JRExporterParameter.PAGE_INDEX, PAGE_ADESIVO);
			exporter.exportReport();
			ImageIO.write(pageImage, "png", out);
			
		} catch (JRException e) {
			throw new AVImageGeneratorException(e,"Erro ao tentar gerar imagem de adesivo a partir de jasper report!");
		} catch (IOException e) {
			throw new AVImageGeneratorException(e,"Erro ao tentar gerar imagem de adesivo a partir de jasper report!");
		}
		
	}



	/**
	 * Recupera jasperReport do modelo de adesivo informado.
	 * @param modeloAdesivoVO
	 * @return
	 * @throws JRException
	 */
	protected JasperReport getJasperReport(ModeloAdesivoVO modeloAdesivoVO) throws JRException {
		
		
		/*
		InputStream jrxmlStream;
		try {
			jrxmlStream = new FileInputStream("/home/darcio/workspace/modules/JazzAV/bsn/src/main/resources/modeloAdes1.jrxml");
			//jrxmlStream = new FileInputStream("C:\\darcio\\trabalho\\frameworkJava\\modules\\JazzAV\\bsn\\src\\main\\resources\\modeloAdes1.jrxml");
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Erro ao tentar encontrar jasperreport",e);
		}
		JasperReport compiled = JasperCompileManager.compileReport(jrxmlStream);
		
		return compiled;
		*/
		
		

		
		JasperReport compiled=null;
		
		if(modeloAdesivoVO!=null && modeloAdesivoVO.getModeloAdesivoCompilado()==null){
		
			//ainda nao carregado como objeto JasperReport
			byte[] arquivoModelo = modeloAdesivoVO.getFile().getLob();
			InputStream jasperIS = new ByteArrayInputStream(arquivoModelo);
			
			
			compiled = JasperCompileManager.compileReport(jasperIS);
			
			//compiled =  (JasperReport) JRLoader.loadObject(jasperIS);
		
			modeloAdesivoVO.setModeloAdesivoCompilado(compiled);
			
		}

		return (JasperReport) modeloAdesivoVO.getModeloAdesivoCompilado();
		
		
	}


	/**
	 * Descarrega ultima versão do adesivo 
	 */
	public void unloadAdesivoModel(){
		
		getAnuncioVO().getAdesivoVO().getModeloAdesivoVO().setModeloAdesivoCompilado(null);
	}
	
	/**
	 * 
	 * @param out
	 * @param width
	 * @param height
	 * @param rootMap
	 * @param arquivoSVG
	 * @throws IOException
	 * @throws TemplateException
	 */
	protected void writeSVGModel(OutputStream out, float width, float height, Map rootMap, byte[] arquivoSVG) throws AVImageGeneratorException  {
		
		InputStream svgStream = new ByteArrayInputStream(arquivoSVG);
		String modeloAnuncio;
		try {
			modeloAnuncio = IOUtils.toString(svgStream);
		

			String strSVG = modeloAnuncio.replaceAll("&quot;", "\"");
			
			StringReader reader = new StringReader(strSVG);
			
			Configuration cfg = new Configuration();
			
			Template template = new Template("teste", reader, cfg,"UTF-16");
			 
			
			StringWriter writer = new StringWriter();
			
			template.process(rootMap, writer);
			
			svgStream = IOUtils.toInputStream(writer.getBuffer().toString());
			
			Transcoder transcoder = new PNGTranscoder(); 
			
			Map hintMap = new HashMap();
			
			hintMap.put(PNGTranscoder.KEY_WIDTH, width);
			//hintMap.put(PNGTranscoder.KEY_HEIGHT, height);
			
			transcoder.setTranscodingHints(hintMap);
			
			TranscoderInput in = new TranscoderInput(svgStream);
			TranscoderOutput tout = new TranscoderOutput(out);
			
			try {
				transcoder.transcode(in, tout);
			} catch (TranscoderException e) {
				log.error("Erro ao tentar transcrever imagem para outputstream",e);
			}
			
			out.flush();
			out.close();

			
		} catch (IOException e) {
			throw new AVImageGeneratorException(e,"Erro ao tentar gerar imagem de adesivo a partir de arquivo vetorial SVG!");
			
		} catch (TemplateException e) {
			throw new AVImageGeneratorException(e,"Erro ao tentar gerar imagem de adesivo a partir de arquivo vetorial SVG!");
		}
				
	}


	
	/**
	 * Recupera o modelo padrão de adesivo
	 * @return
	 */
	protected ModeloAdesivoVO getModeloPadrao() {
		
		ModeloAdesivoVO modeloAdesivoVO = anuncioBusiness.getModeloPadrao();
		
		return modeloAdesivoVO;
	}


	/**
	 * Contem regra de recuperacao ou criacao de anuncio para o clientes.
	 * Caso o cliente possua um anuncio em edicao, recupera-o
	 * Caso possua um anuncio concluído, perguntar se deseja reimprimir o adesivo ou criar um novo
	 * @return
	 */
	public AnuncioVO getAnuncioVO() {
		
		//se nao ha uma definicao de pessoa para o anuncio atual
		if(pessoaNotDefined()){
			
			//recupera pessoa logada
			PessoaVO pessoaVO = getPessoaLogadaVO();
			
			if(pessoaVO!=null){
				//se encontrou, atribui....
				
				if(pessoaVO.getEnderecoVO()==null){
					pessoaVO.setEnderecoVO(new EnderecoVO());
				}
				
				anuncioVO.setPessoaVO(pessoaVO);
			}
		}
		
		return anuncioVO;
	}



	/**
	 * @return
	 */
	protected boolean pessoaNotDefined() {
		return anuncioVO.getPessoaVO()==null || anuncioVO.getPessoaVO().getPK()==null;
	}
	
	
	/**
	 * Recupera pessoa logada 
	 * @return
	 */
	public PessoaVO getPessoaLogadaVO() {

		Principal principal = getPrincipal();
		
		PessoaVO pessoaVO=null;
		if(principal!=null && !principal.getName().equals("Anonymous")){
			pessoaVO = pessoaBusiness.findPessoa(principal);
		}

		return pessoaVO;
	}
	
	
	public void setAnuncioVO(AnuncioVO anuncioVO) {
		this.anuncioVO = anuncioVO;
	}



	/**
	 * Monta lista de selectItem para qualquer enum informado
	 * @param <E>
	 * @param enumClass
	 * @return
	 */
 	public <E extends Enum<E>> List<SelectItem> getEnums(Class<E> enumClass){
 		
 		List<SelectItem> itens = new ArrayList<SelectItem>();
 		
		EnumSet<E> enumSet = EnumSet.allOf(enumClass); 
 		
 		for (E enumItem : enumSet) {
			
 			SelectItem selectItem = new SelectItem();
 			
 			selectItem.setValue(enumItem);
 			selectItem.setLabel(enumItem+"");
 			
 			itens.add(selectItem);
 			
		}
 		
 		return itens;
 	}


	/**
	 * Monta lista de selectItem para qualquer enum informado
	 * @param <E>
	 * @param enumClass
	 * @return
	 */
 	public <E extends Enum<E>> List<E> getEnumsType(Class<E> enumClass){
 		
 		List<E> itens = new ArrayList<E>();
 		
		EnumSet<E> enumSet = EnumSet.allOf(enumClass); 
		
		itens.addAll(enumSet);
 		
 		return itens;
 	}

		
	@Override
	public List<? extends Enum> autorizeAnyOf() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String actionPesquisar() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String[] getCamposLimparPesquisa() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected IBusiness<AnuncioVO> getBusiness() {
		// TODO Auto-generated method stub
		return null;
	}



	public PessoaVO getPessoaVO() {
		return getAnuncioVO().getPessoaVO();
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.getAnuncioVO().setPessoaVO(pessoaVO);
	}


	


	
}
