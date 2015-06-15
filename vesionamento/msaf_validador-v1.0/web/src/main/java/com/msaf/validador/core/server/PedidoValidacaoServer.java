package com.msaf.validador.core.server;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.ValidadorFacadeMQImpl;
import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.exceptions.TarifacaoException;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorLoginException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.core.tratamentoxls.LeitorXLSCliente;
import com.msaf.validador.integration.hibernate.Repositorio;
import com.msaf.validador.integration.hibernate.jdbc.StatusPediValidacao;
import com.msaf.validador.integration.util.Util;

/**
 * 
 * 
 * @author dlopes
 *
 */
@Transactional
public class PedidoValidacaoServer {
	
	private Repositorio repositorio;

	
	/**
	 * Construtor default
	 */
	public PedidoValidacaoServer(){
		System.out.println("criando");
	}
	
	public PedidoValidacaoServer(Repositorio repositorio) {
		this.repositorio = repositorio;
		
		
	}
	
	
	public PedValidacaoVO getPedidoValidacao(Long pedValidacao) {
		
		return repositorio.getPedValidacaoDAO().findById(pedValidacao);
		
	}

	@Transactional
	public void pedidoDeProcessamentoAutorizado(InputStream inputStream, PedValidacaoVO pedValidacaoVO) throws LeitorXLSException{
		
		pedValidacaoVO = repositorio.getPedValidacaoDAO().makePersistent(pedValidacaoVO);

		LeitorXLSCliente leitorXLS = new LeitorXLSCliente(new ValidadorFacadeMQImpl(), pedValidacaoVO);
		
		leitorXLS.processarArquivo(inputStream);
		
		//FIXME TemporarioParaTeste com um chedbox
		Long idTipoValidacao = 1L;
		idTipoValidacao = 1L;
//		TipoPedidoValidacao opcoesValidacao = new TipoPedidoValidacao(pedValidacao.getId(),idTipoValidacao);
//		repositorio.getTipoPedidoValidacaoDAO().makePersistent(opcoesValidacao);
		
		
		
		
	}

	/**
	 * Em funcao da quantidade de linhas do arquivo e das validacoes escolhidas,
	 * totaliza a cotação da validacao
	 * 
	 * @param totalLinhasXLS
	 * @param listaValidacao
	 * @return
	 * @throws IOException
	 */
	public BigDecimal totalizaCotacaoValidacao(Integer totalLinhasXLS, List<TpValidVO> listaValidacao) throws ValidadorException{
		
		BigDecimal totalTarifas = totalizaTarifas(listaValidacao);
		BigDecimal totalMonetario = new BigDecimal(0);

		if(totalTarifas!=null && totalLinhasXLS!=null){
			
			totalMonetario = totalTarifas.multiply(new BigDecimal(totalLinhasXLS));
			
		}
		return totalMonetario;
		
	}
	/**
	 * @param listaValidacao
	 * @return
	 */
	protected BigDecimal totalizaTarifas(List<TpValidVO> listaValidacao) throws TarifacaoException {
		if(Util.isEmpty(listaValidacao)) throw new TarifacaoException("Lista de Validações não pode ser vazia!");
		
		BigDecimal totalTarifas = new BigDecimal(0);
		
		for (TpValidVO tipoValidacaoVO : listaValidacao) {
			BigDecimal valorTarifa = recuperaValorTarifa(tipoValidacaoVO);
			
			totalTarifas = totalTarifas.add(valorTarifa);
		}
		return totalTarifas;
	}
	
	
	/**
	 * Recupera valor da tarifa referente ao tipo de validação informado no argumento
	 * FIXME: fazer com que o valor da tarifa já venha da sessão, sem precisar ser consultado novamente
	 * 
	 * @param tipoValidacaoVO
	 * @param idTpVal
	 */
	protected BigDecimal recuperaValorTarifa(TpValidVO tipoValidacaoVO) {
		BigDecimal valorTarifa;

		TpValidVO tpValidVO = repositorio.getTpValidDAO().findById(tipoValidacaoVO.getId());
		valorTarifa = tpValidVO.getTarifaFk().getValorTarifa();
			
		return valorTarifa;
	}
	
	public List<PedValidacaoVO> listaPedidoValidacaoCliente(ClienteVO cliente) {
		return repositorio.getPedValidacaoDAO().listaPedidosValidacaoCliente(cliente);
	}
	
	public List<TpValidVO> getListaValidacoes() {
		List<TpValidVO> tipos = repositorio.getTpValidDAO().validacoeComerciais();
		return tipos;
	}
	
	
	public List<TpValidVO> getListaValidacoesTodas() {
		List<TpValidVO> tipos = repositorio.getTpValidDAO().findAll();
		return tipos;
	}

	public List<StatusPediValidacao> getStatusPedido(Long validaIDPedValidacao) {
		return repositorio.getStatusPedValidacaoJDBC().getListaStatusPedidoValidacao(validaIDPedValidacao.toString());
		
	}

	/**
	 * Pesquisa usuário
	 * @param userName
	 * @return
	 * @throws ValidadorLoginException 
	 */
	public UsuarioVO getUsuarioVO(String userName) throws ValidadorLoginException {
		UsuarioVO usuarioVO = repositorio.getUsuarioDAO().findByUserName(userName);
		
		return usuarioVO;
	}
	
	
	public Repositorio getRepositorio() {
		return repositorio;
	}

	public void setRepositorio(Repositorio repositorio) {
		this.repositorio = repositorio;
	}

}
