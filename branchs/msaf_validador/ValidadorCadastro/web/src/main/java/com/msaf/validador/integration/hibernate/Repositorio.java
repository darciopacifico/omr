package com.msaf.validador.integration.hibernate;

import com.msaf.validador.integration.hibernate.intf.IClienteDAO;
import com.msaf.validador.integration.hibernate.intf.IDadosRetInstDAO;
import com.msaf.validador.integration.hibernate.intf.IInstituicaoDAO;
import com.msaf.validador.integration.hibernate.intf.IPedValidacaoDAO;
import com.msaf.validador.integration.hibernate.intf.IPessoaDAO;
import com.msaf.validador.integration.hibernate.intf.ITarifaDAO;
import com.msaf.validador.integration.hibernate.intf.ITpResultDAO;
import com.msaf.validador.integration.hibernate.intf.ITpValidDAO;
import com.msaf.validador.integration.hibernate.intf.IUsuarioDAO;
import com.msaf.validador.integration.hibernate.intf.IValorCategoriaHibernateDAO;
import com.msaf.validador.integration.hibernate.jdbc.IStatusPedValidacaoJDBC;

public class Repositorio {

	private IPedValidacaoDAO pedValidacaoDAO;
	private IClienteDAO clienteDAO;
	private ITpValidDAO tpValidDAO;
	private ITpResultDAO tpResultDAO;
	private IInstituicaoDAO instituicaoDAO;
	private ITarifaDAO tarifaDAO;
	private IDadosRetInstDAO dadosRetInstDAO;
	private IPessoaDAO pessoaDAO;
	private IUsuarioDAO usuarioDAO;
	private IStatusPedValidacaoJDBC statusPedValidacaoJDBC;

	private IValorCategoriaHibernateDAO valorCategoriaDAO;
	
	public IValorCategoriaHibernateDAO getValorCategoriaDAO() {
		return valorCategoriaDAO;
	}
	public void setValorCategoriaDAO(IValorCategoriaHibernateDAO valorCategoriaDAO) {
		this.valorCategoriaDAO = valorCategoriaDAO;
	}
	public ITpResultDAO getTpResultDAO() {
		return tpResultDAO;
	}
	public void setTpResultDAO(ITpResultDAO tpResultDAO) {
		this.tpResultDAO = tpResultDAO;
	}
	
	public IStatusPedValidacaoJDBC getStatusPedValidacaoJDBC() {
		return statusPedValidacaoJDBC;
	}
	public void setStatusPedValidacaoJDBC(
			IStatusPedValidacaoJDBC statusPedValidacaoJDBC) {
		this.statusPedValidacaoJDBC = statusPedValidacaoJDBC;
	}
	public IUsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}
	public void setUsuarioDAO(IUsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
	public IPessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}
	public void setPessoaDAO(IPessoaDAO registroPessoaDAO) {
		this.pessoaDAO = registroPessoaDAO;
	}
	public IInstituicaoDAO getInstituicaoDAO() {
		return instituicaoDAO;
	}
	public void setInstituicaoDAO(IInstituicaoDAO instituicaoDAO) {
		this.instituicaoDAO = instituicaoDAO;
	}
	public ITpValidDAO getTpValidDAO() {
		return tpValidDAO;
	}
	public void setTpValidDAO(ITpValidDAO tipoValidacaoDAO) {
		this.tpValidDAO = tipoValidacaoDAO;
	}
	
	public ITarifaDAO getTarifaDAO() {
		return tarifaDAO;
	}
	public void setTarifaDAO(ITarifaDAO tarifaDAO) {
		this.tarifaDAO = tarifaDAO;
	}

	public IClienteDAO getClienteDAO() {
		return clienteDAO;
	}
	public void setClienteDAO(IClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}
	public IDadosRetInstDAO getDadosRetInstDAO() {
		return dadosRetInstDAO;
	}
	public void setDadosRetInstDAO(
			IDadosRetInstDAO dadosRetornoInstituicaoDAO) {
		this.dadosRetInstDAO = dadosRetornoInstituicaoDAO;
	}
	public IPedValidacaoDAO getPedValidacaoDAO() {
		return pedValidacaoDAO;
	}
	public void setPedValidacaoDAO(IPedValidacaoDAO pedidoValidacaoDAO) {
		this.pedValidacaoDAO = pedidoValidacaoDAO;
	}

}
