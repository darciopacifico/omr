package br.com.dlp.framework.unittest.pk;

import br.com.dlp.framework.pk.AbstractAutoComparePK;

public class TestePK extends AbstractAutoComparePK {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2954911183685131035L;

	private Long id;

	private Long idPedido;

	private PedidoPK pedidoPK;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public PedidoPK getPedidoPK() {
		return pedidoPK;
	}

	public void setPedidoPK(PedidoPK pedidoPK) {
		this.pedidoPK = pedidoPK;
	}

}
