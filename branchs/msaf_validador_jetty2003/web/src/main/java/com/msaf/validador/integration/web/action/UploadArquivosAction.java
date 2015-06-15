package com.msaf.validador.integration.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.exceptions.ArquivoInvalidoException;
import com.msaf.validador.consultaonline.exceptions.TarifacaoException;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorLoginException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.exceptions.ValidadorUploadException;
import com.msaf.validador.consultaonline.exceptions.VersaoExcelIncorreta;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.consumer.util.DateUtil;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.core.server.PedidoValidacaoServer;
import com.msaf.validador.core.tratamentoxls.VerificadorXLSCliente;
import com.msaf.validador.integration.hibernate.jdbc.StatusPediValidacao;
import com.msaf.validador.integration.util.Util;
import com.msaf.validador.integration.web.action.base.BaseAction;
import com.msaf.validador.integration.web.form.UploadArquivosForm;


/**
 * Controller para o m�dulo de manuten��o de pedidos de validacao
 * @author dlopes
 *
 */
public class UploadArquivosAction extends BaseAction {


	protected static final String MSG_KEY_DEFAULT = "default";
	protected static final String MSG_PROP_GERAL = "geral";
	protected static final String FORWARD_TELA_UPLOAD = "telaUpload";
	protected static final String FORWARD_LOGOFF = "logoff";
	private PedidoValidacaoServer pedidoValidacaoServer;
	protected static final String BYTES_ARQUIVO_VALIDACAO_UPLOAD = "ARQUIVO_VALIDACAO_UPLOAD";
	public static final int BUFFER_SIZE = 5000;
	
	
	/**
	 * Inicializar formul�rio com lista de tipos de validacao, reseta formul�rio,
	 * verificar se usu�rio est� logado
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	
		UsuarioVO usuarioVO;
		try {
			usuarioVO = getUsuario(request);
		} catch (ValidadorLoginException e) {
			throw new ValidadorRuntimeException("N�o foi poss�vel idendificar o usu�rio logado!",e);
		}
		
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		uploadArquivosForm.setUsuarioVO(usuarioVO);
		
		setMultBoxTiposValidacao(request, form);
		initMultBoxTiposValidacao(form);
		resetFrom(form);
		
		try {
			getListaPedidosValidacaoCliente(form, request);
		} catch (ValidadorException e) {
			throw new ValidadorRuntimeException("N�o foi poss�vel pesquisar os pedidos de validacao do cliente!",e);
		}
		
		uploadArquivosForm.setHabiliarBtnProcesso(false);
		
		return (mapping.findForward(UploadArquivosAction.FORWARD_TELA_UPLOAD));
	}

	/**
	 * Processa upload do arquivo XLS
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward doUploadECotacao(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		ActionMessages actionMessages = getMessages(request);
		
		List<TpValidVO> tiposSelects = getTipoValidacoesSelecionadas(mapping, form, request, response);
	
		  
		if(tiposSelects.size()==0){
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Selecione um ou mais tipos de valida��o!"));
		}else{
			//separa os tipos de valida��o selecionados
			UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
			uploadArquivosForm.setTiposValidacaoEscolhaOrcamento(tiposSelects);
		}
		
		try{
			long tamanhoArquivo = receberArquivo(mapping, form, request, response);
			
			if(tamanhoArquivo<10){
				actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Selecione um arquivo para upload!"));
			} 
			
			saveMessages(request, actionMessages);
			if(actionMessages.size()>0){
				return (mapping.findForward(UploadArquivosAction.FORWARD_TELA_UPLOAD));
			}
			
			
			validarArquivoRecebido(mapping,  form,  request, response);
			
		}catch(ValidadorUploadException e){
			log.error("Erro ao tentar trazer arquivo por upload!");
			return tratarErroUpload(mapping, form, request, response, e);
		}
		
		
		try{
			cotarValidacao(mapping, form, request, response);
		}catch(ValidadorException e){
			return tratarErroCotacao(mapping, form, request, response, e);
		}
		
		UploadArquivosForm arquivosForm = (UploadArquivosForm) form;
		arquivosForm.setHabiliarBtnProcesso(true);
		arquivosForm.setTipoSelecionado(new String[]{});
		
		setMultBoxTiposValidacao(request, form);
		initMultBoxTiposValidacao(form);
		
		return (mapping.findForward(UploadArquivosAction.FORWARD_TELA_UPLOAD));
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public ActionForward doLogoff(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		request.getSession().invalidate();
		response.sendRedirect("Site/index.jsp");
		return (mapping.findForward(UploadArquivosAction.FORWARD_LOGOFF));
	}


	/**
	 * Limpa dados da tentativa de cota��o da sess�o e prepara mensagem de di�logo para usu�rio
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	protected ActionForward tratarErroCotacao(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ValidadorException validadorException) {
		ActionMessages actionMessages = getMessages(request);
		// TODO ActionMessage ....
		// TODO DETERMINAR MENSAGEM DE ERRO EM FUNCAO DA CAUSA DO ERRO 
		//
		try {
			throw validadorException;
		} catch (TarifacaoException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Erro n�o identificado ao tentar efetuar cota��o!"));
			
		} catch (VersaoExcelIncorreta e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Vers�o do arquivo excel inv�lida!"));
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"O Validador suporta as ves�oes 97, XP e 2003!"));
			 
		} catch (ArquivoInvalidoException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"O arquivo n�o parece ser do tipo planilha excel!"));
			
		} catch (ValidadorException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Erro n�o identificado ao tentar efetuar cota��o!"));
			
		}
		


		saveMessages(request, actionMessages);
		
		return mapping.findForward(UploadArquivosAction.FORWARD_TELA_UPLOAD);
	}

	/**
	 * Em funcao do tamanho do arquivo e das tarifas selecionadas calcula orcamento de validacao. Prepara mensagem de di�logo em caso de sucesso
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @exception TarifacaoException Arquivo inv�lido, arquivo vazio, nenhuma tarifa selecionada etc... 
	 * @throws ArquivoInvalidoException 
	 * 
	 */
	protected void cotarValidacao(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws TarifacaoException, ArquivoInvalidoException {
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;


		int qtdLinhas = contaQuantidadeLinhasArquivo(request);

		if(qtdLinhas==0){
			ActionMessages actionMessages = getMessages(request);
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"O arquivo n�o possui linhas para valida��o!"));
			saveMessages(request, actionMessages);
		}

		
		double totalTarifas = totalizaTarifas(mapping, form, request, response);
		double totalOrcamento = totalTarifas*qtdLinhas;
		
		
		uploadArquivosForm.setQuantidadeDeRegistros(qtdLinhas);
		uploadArquivosForm.setValorTotal(totalOrcamento);
		uploadArquivosForm.setSubtotalTarifas(totalTarifas);
					
	}
	
	/**
	 * Carrega PopUp com os status do pedido do usuario
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward doPopUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<StatusPediValidacao> lista = pedidoValidacaoServer.getStatusPedido(validaIDPedValidacao(request));
		
		List<StatusPediValidacao> listaTratada = new ArrayList<StatusPediValidacao>();
		String tipoRepetido = new String();
		
		for (StatusPediValidacao statusPediValidacao : lista) {
			if(!tipoRepetido.equals(statusPediValidacao.getDescricaoValidacao())){
				tipoRepetido = statusPediValidacao.getDescricaoValidacao();
			} else {
				statusPediValidacao.setDescricaoValidacao("");
				statusPediValidacao.setIdTipoValidacao("");
			}
			listaTratada.add(statusPediValidacao);
		}
		
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		uploadArquivosForm.getUsuarioVO();
		uploadArquivosForm.setListStatusPediValidacao(listaTratada);
		uploadArquivosForm.setPedValidacaoVO(pedidoValidacaoServer.getPedidoValidacao(validaIDPedValidacao(request)));
		
		String dos = mapping.getForward();
		return mapping.findForward("popup");
	}
	
	private Long validaIDPedValidacao(ServletRequest request) {
		String idPedido = request.getParameter("idPedValidacao");
		if(Util.isEmpty(idPedido))   throw new IllegalArgumentException("Id pedido vazio!");
		return Long.parseLong(idPedido);
	}

	/**
	 * Conta quantidade de linhas do arquivo XLS que esta na sess�o.
	 * @param request
	 * @return
	 * @throws TarifacaoException
	 * @throws ArquivoInvalidoException 
	 */
	protected int contaQuantidadeLinhasArquivo(HttpServletRequest request) throws TarifacaoException, ArquivoInvalidoException {
		byte[] arquivoXLS = recuperaArquivoDaSessao(request);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arquivoXLS);
		
		int qtdLinhas;
		
		VerificadorXLSCliente verificadorXLSCliente = new VerificadorXLSCliente();
		try {
			qtdLinhas = verificadorXLSCliente.contarLinhas(byteArrayInputStream);
		} catch (LeitorXLSException e) {
			throw new TarifacaoException("Erro ao tentar determinar tamanho do arquivo!",e);
		}
		return qtdLinhas;
	}

	
	/**
	 * Totaliza Tarifas informadas
	 * @param tiposSelecionados
	 * @return
	 */
	protected double totalizaTarifas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		
		//
		List<TpValidVO> validacoesSelecionadas = uploadArquivosForm.getTiposValidacaoEscolhaOrcamento();
		
		Double totalTarifa = 0d;
		for (TpValidVO tpValidVO : validacoesSelecionadas) {
			double doubleValue = 0; 
			if(null!=tpValidVO.getTarifaFk().getValorTarifa()){
				doubleValue = tpValidVO.getTarifaFk().getValorTarifa().doubleValue();
			}
			totalTarifa = totalTarifa.doubleValue() + doubleValue;
		}
		
		return totalTarifa;
	}

	/**
	 * Retorna os tipos de validacao selecionados pelo usu�rio
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private List<TpValidVO> getTipoValidacoesSelecionadas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		String COMPLETA = "899";
		
		String[] strTiposSelecionados = uploadArquivosForm.getTipoSelecionado();
		List<TpValidVO> tiposValidacaoPossiveis = uploadArquivosForm.getTiposValidacao();
		
		List<TpValidVO> tiposSelecionados = new ArrayList<TpValidVO>();

		for (TpValidVO tpValidVO : tiposValidacaoPossiveis) {
			for (String selecionado : strTiposSelecionados) {
				
				if(COMPLETA.equals(selecionado)){
					return carregaTodosOsTipoValidacaoValidos(tpValidVO);
				} 
				
				String strId = tpValidVO.getId()+"";
				if(selecionado.equals(strId)){
					tiposSelecionados.add(tpValidVO);
				} 
				
			}
		}
		
		return tiposSelecionados;
	}
	/**
	 * Lista todas as valida�oes
	 * @param tpValidVO 
	 * @param tiposValidacaoPossiveis
	 * @return
	 */
	private List<TpValidVO> carregaTodosOsTipoValidacaoValidos(TpValidVO tpValidVO) {
		
		List<TpValidVO> tiposSelecionados;
		tiposSelecionados = new ArrayList<TpValidVO>();
		tiposSelecionados.add(tpValidVO);
		return tiposSelecionados;
	}

	/**
	 * Recupera arquivo XLS armazenado previamente na sess�o
	 * @param request
	 * @return
	 */
	protected byte[] recuperaArquivoDaSessao(HttpServletRequest request) {
		return (byte[]) request.getSession().getAttribute(BYTES_ARQUIVO_VALIDACAO_UPLOAD);
	}

	/**
	 * Limpa dados da tentativa de recep��o do arquivo e envia mensagen de di�logo para usu�rio
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	protected ActionForward tratarErroUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ValidadorUploadException e) {
		ActionMessages actionMessages = getMessages(request);
		actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Erro ao tentar efetuar upload do arquivo!"));
		actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Verifique o tipo e formato do arquivo!!"));
		saveMessages(request, actionMessages);
		
		return mapping.findForward(FORWARD_TELA_UPLOAD);
	}
	
	/**
	 * Recepciona arquivo remoto vindo por download. Escreve o arquivo num array de bytes armazenado em sess�o.
	 * - N�o processa JMS
	 * - N�o faz cota��o
	 */
	protected long receberArquivo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ValidadorUploadException{
		
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		

		//ORIGEM: arquivo vindo do browser do usu�rio por upload
		InputStream inputStream = recuperaInputStreamUpload(uploadArquivosForm);
		//BufferedInputStream  bufferedInputStream = new BufferedInputStream(inputStream,UploadArquivosAction.BUFFER_SIZE);
		BufferedInputStream  bufferedInputStream = new BufferedInputStream(inputStream);

		//DESTINO: array de bytes na memoria
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream,UploadArquivosAction.BUFFER_SIZE);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		
		
		//processa transfer�ncia do arquivo
		long tamanhoArquivo = transfereArquivoXLS(byteArrayOutputStream, bufferedOutputStream, inputStream, bufferedInputStream);
				
		
		//recupera array de bytes referente ao arquivo
		byte[] bytesArquivoXLS = byteArrayOutputStream.toByteArray();
		
		
		//ARMAZENA ARQUIVO NA SESSAO
		request.getSession().setAttribute(UploadArquivosAction.BYTES_ARQUIVO_VALIDACAO_UPLOAD, bytesArquivoXLS);
		
		return tamanhoArquivo;
		
	}

	/**
	 * Executa transfer�ncia de bytes do arquivo de origem para o array de bytes na memoria
	 * @param outputStream
	 * @param bufferedOutputStream
	 * @param inputStream
	 * @param bufferedInputStream
	 * @throws ValidadorUploadException
	 */
	private long transfereArquivoXLS(OutputStream outputStream, BufferedOutputStream bufferedOutputStream, InputStream inputStream, BufferedInputStream bufferedInputStream) throws ValidadorUploadException {
		long tamanhoArquivo = 0;
		int qtdBytesLidos =0;
		try {
			do{
				
				byte[] bytesLidos=new byte[1000];
				qtdBytesLidos = bufferedInputStream.read(bytesLidos);
				
				//conta tamanho do arquivo
				tamanhoArquivo = tamanhoArquivo + qtdBytesLidos;
				
				bufferedOutputStream.write(bytesLidos);
				
			}while(qtdBytesLidos>0);
			bufferedOutputStream.flush();

		} catch (IOException e) {
			throw new ValidadorUploadException("Erro ao processar transfer�ncia de arquivo para array de bytes na memoria!",e);
		}finally{
			try {
				bufferedInputStream.close();
			} catch (IOException e) {
				log.error("Erro ao tentar fechar bufferedInputStream",e);
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("Erro ao tentar fechar inputStream",e);
			}
			
			try {
				bufferedOutputStream.close();
			} catch (IOException e) {
				log.error("Erro ao tentar fechar bufferedOutputStream",e);
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				log.error("Erro ao tentar fechar byteArrayOutputStream",e);
			}
		}
		return tamanhoArquivo;
	}

	/**
	 * @param uploadArquivosForm
	 * @return
	 * @throws ValidadorUploadException
	 */
	protected InputStream recuperaInputStreamUpload(UploadArquivosForm uploadArquivosForm) throws ValidadorUploadException {
		InputStream inputStream;
		try {
			inputStream = uploadArquivosForm.getArquivo().getInputStream();
		} catch (FileNotFoundException e) {
			throw new ValidadorUploadException("Arquivo n�o encontrado para upload!",e);
		} catch (IOException e) {
			throw new ValidadorUploadException("Erro ao tentar recuperar inputStream!",e);
		}
		return inputStream;
	}

	/**
	 * Verifica se arquivo est� no formato esperado
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @exception ArquivoInvalidoException 
	 */
	protected void validarArquivoRecebido(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ArquivoInvalidoException {
		//verifica que arquivo recebido � um excel e est� no formato esperado
		byte[] arquivoXLS = recuperaArquivoDaSessao(request);

				
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arquivoXLS);
		VerificadorXLSCliente verificadorXLSCliente = new VerificadorXLSCliente();
		
		//TODO:caso layout n�o seja o experado, envia mensagem de erro e lan�a exce��o ArquivoInvalidoException
		//throw new ArquivoInvalidoException
	

		
		
	}

	/**
	 * A partir do arquivo contido no servidor (em sess�o ou disco) processa
	 * conte�do do arquivo gerando mensagens JMS
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward doProcessamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages actionMessages = getMessages(request);

		try {
			confirmaProcessamentoArquivo(mapping, form, request, response,actionMessages);
		
		} catch (ValidadorLoginException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Aten��o! Verifique se este usu�rio est� associado a um cliente Mastersaf!"));
		
		} catch (TarifacaoException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Erro ao tentar determinar tarifa��o (arquivo vazio?)!"));
		
		} catch (ValidadorException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Erro n�o especificado ao tentar executar processamento!"));
		
		}
		
		saveMessages(request, actionMessages);

		
		if(actionMessages.size()>0){
			return (mapping.findForward(UploadArquivosAction.FORWARD_TELA_UPLOAD));
		}else{
			//TUDO OK
			UploadArquivosForm arquivosForm = (UploadArquivosForm) form;
			arquivosForm.setHabiliarBtnProcesso(false);
			return init(mapping, arquivosForm, request, response);
		}

	}
	
	/**
	 * Reseta selecao de tipos de valida��o
	 * @param form
	 */
	private void initMultBoxTiposValidacao(ActionForm form) {
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		uploadArquivosForm.setTipoSelecionado(new String[] {});
		
	}

	/**
	 * Reseta formul�rio
	 * @param form
	 */
	private void resetFrom(ActionForm form) {
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		uploadArquivosForm.clean();
	}
	
	/**
	 * Recupera tipos de validacao dispon�veis e atribui ao formul�rio
	 * @param request
	 */
	private void setMultBoxTiposValidacao(HttpServletRequest request, ActionForm actionForm) {
		UploadArquivosForm arquivosForm = (UploadArquivosForm) actionForm;
		
		List<TpValidVO> validacoes = pedidoValidacaoServer.getListaValidacoes();
		 
		arquivosForm.setTiposValidacao(validacoes);
		
	}



	/**
	 * Recupera lista de pedidos de validacao do cliente e atribui ao formul�rio
	 * @param form
	 * @param request
	 * @throws ServletException
	 * @throws Exception
	 */
	private void getListaPedidosValidacaoCliente(ActionForm form, HttpServletRequest request) throws ValidadorException {
		
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;

		List<PedValidacaoVO> listPedidosValidacao = new ArrayList<PedValidacaoVO>();
		ClienteVO cliente = getUsuario(request).getIdCliente();
		listPedidosValidacao = getPedidoValidacaoServer().listaPedidoValidacaoCliente(cliente);
		uploadArquivosForm.setListPedidosValidacao(listPedidosValidacao);
		
	}

	/**
	 * �ltimo passo no cen�rio princial, incluir pedido de validacao. O pedido de
	 * validacao ser� registrado e o arquivo excel ser� transformado em mensagens JMS
	 * 
	 * @param form
	 * @param request
	 * @throws ServletException
	 * @throws IOException
	 */
	private void confirmaProcessamentoArquivo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ActionMessages actionMessages) throws ValidadorException {
		byte[] bytes = recuperaArquivoDaSessao(request);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

		PedValidacaoVO pedValidacaoVO;
		try{
			pedValidacaoVO = montaPedValidacaoVO(mapping, form, request, response);
		}catch(ValidadorLoginException e){
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Login de usu�rio inv�lido!"));
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Verifique se este usu�rio est� associado a um cliente!"));
			return;
		} 
		
		
		
		 
		try {
			getPedidoValidacaoServer().pedidoDeProcessamentoAutorizado(inputStream, pedValidacaoVO);
		} catch (ValidadorRuntimeException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Erro fatal ao tentar processar o arquivo!"));
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Contate o administrador do sistema: Servidor de mensagens indispon�vel!"));
			
		} catch (LeitorXLSException e) {
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Erro ao tentar ler o arquivo enviado!"));
			actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"O arquivo deve ser em Excle e estar no layout especificado!"));
		}
	}

	/**
	 * @param request 
	 * @param form 
	 * @return
	 * @throws TarifacaoException  
	 */
	protected PedValidacaoVO montaPedValidacaoVO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ValidadorException {
		PedValidacaoVO pedValidacaoVO = new PedValidacaoVO();
		
		//pedValidacaoVO.setDataSolicitacao(new Date());
	//	String dateStr = new SimpleDateFormat("dd:MM:yyyy HH:MM").format(new Date());
		String dateStr = DateUtil.format(new Date(), DateUtil.FORMATACAO_DATA_HORA_MINUTO_SEGUNDO);
		
		Date dataSolicitacao = new Date();
		try {
			dataSolicitacao = DateUtil.format(dateStr, DateUtil.FORMATACAO_DATA_HORA_MINUTO_SEGUNDO);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		pedValidacaoVO.setDataSolicitacao(dataSolicitacao);
		
		pedValidacaoVO.setQtdeRegistrosArq(contaQuantidadeLinhasArquivo(request));
		
		UsuarioVO usuarioVO = getUsuario(request);
		ClienteVO clienteVO = usuarioVO.getIdCliente();
		pedValidacaoVO.setClienteFk(clienteVO);
	
		UploadArquivosForm uploadArquivosForm = (UploadArquivosForm) form;
		
		List<TpValidVO> validacoes = uploadArquivosForm.getTiposValidacaoEscolhaOrcamento();
		pedValidacaoVO.setTpValidacoes(validacoes);
		
		return pedValidacaoVO;
	}

	private boolean validarTipoTarifasRetornoSelecionados(UploadArquivosForm uploadForm) {
		return null != uploadForm.getTipoSelecionado() && uploadForm.getTipoSelecionado().length > 0;
	}

	
	
	@Override
	protected UsuarioVO pesquisaUsuario(HttpServletRequest request, String userName) throws ValidadorLoginException {

		UsuarioVO usuarioVO = this.getPedidoValidacaoServer().getUsuarioVO(userName);
		
		return usuarioVO;
	}

	/**
	 * @return the pedidoValidacaoServer
	 */
	public PedidoValidacaoServer getPedidoValidacaoServer() {
		return pedidoValidacaoServer;
	}

	/**
	 * @param pedidoValidacaoServer the pedidoValidacaoServer to set
	 */
	public void setPedidoValidacaoServer(PedidoValidacaoServer pedidoValidacaoServer) {
		this.pedidoValidacaoServer = pedidoValidacaoServer;
	}

	
	
	
}
