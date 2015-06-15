package com.msaf.validador.core.server;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.ValidadorFacadeMQImpl;
import com.msaf.validador.consultaonline.exceptions.TarifacaoException;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorLoginException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.CategoriaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ValorCategoriaVO;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.core.tratamentoxls.LeitorXLSCliente;
import com.msaf.validador.integration.hibernate.Repositorio;
import com.msaf.validador.integration.hibernate.bean.PesquisaPedidoValidacao;
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
	}
	
	public PedidoValidacaoServer(Repositorio repositorio) {
		this.repositorio = repositorio;
		
		
	}
	
	
	public PedValidacaoVO getPedidoValidacao(Long pedValidacao) {
		PedValidacaoVO pedValidacaoVO =repositorio.getPedValidacaoDAO().findById(pedValidacao); 
		
		//pedValidacaoVO.getPessoaFK().size();
		 
		return pedValidacaoVO;
		
	}

	@Transactional
	public void pedidoDeProcessamentoAutorizado(InputStream inputStream, PedValidacaoVO pedValidacaoVO) throws LeitorXLSException{
		
		pedValidacaoVO = repositorio.getPedValidacaoDAO().makePersistent(pedValidacaoVO);

		ValidadorFacadeMQImpl validadorFacadeMQImpl = new ValidadorFacadeMQImpl();
		
		LeitorXLSCliente leitorXLS = new LeitorXLSCliente(validadorFacadeMQImpl, pedValidacaoVO);
		
		leitorXLS.processarArquivo(inputStream);
		
		try {
			validadorFacadeMQImpl.destroyQueue();
		} catch (NamingException e) {
			throw new LeitorXLSException("Erro ao tentar finalizar conexao JMS",e);
		} catch (JMSException e) {
			throw new LeitorXLSException("Erro ao tentar finalizar conexao JMS",e);
		}
		
		
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
	
	public List<PedValidacaoVO> listaPedidoValidacaoCliente(PesquisaPedidoValidacao pesquisaPedidoValidacao, Integer pagina) {
		return repositorio.getPedValidacaoDAO().listaPedidosValidacaoCliente(pesquisaPedidoValidacao, pagina);
	}
	
	public Long countListaPedidosValidacaoCliente(PesquisaPedidoValidacao pesquisaPedidoValidacao) {
		return repositorio.getPedValidacaoDAO().countListaPedidosValidacaoCliente(pesquisaPedidoValidacao);
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
	
	
	public List<PessoaVO> getListaPessoaPorPedidoValidacao(PedValidacaoVO vo) {
		return repositorio.getPessoaDAO().listByPedidoValidacao(vo);
		
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

	
	public List<ValorCategoriaVO> pesquisarValorCategoria(CategoriaVO categoria){
		return this.repositorio.getValorCategoriaDAO().pesquisarValorCategoria(categoria);
	}
	
}
