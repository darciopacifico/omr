package com.msaf.validador.integration.util;


public class Paginacao {
	public static final String LIMITE = "limit";
	public static final String INICIO = "start";

	public static final Paginacao DEFAULT_PAGINACAO = new Paginacao(0, 30);

	private Integer inicio;
	private Integer limite;

	public Paginacao(){
		this.inicio = DEFAULT_PAGINACAO.inicio;
		this.limite = DEFAULT_PAGINACAO.limite;
	}
	
	
	public Paginacao(final Integer inicio, final Integer quantidade) {
		super();
		setInicio(inicio);
		setLimite(quantidade);
	}

	public Integer getInicio() {
		return inicio;
	}

	public void setInicio(final Integer inicio) {
		this.inicio = inicio;
	}

	public Integer getLimite() {
		return limite;
	}

	public void setLimite(final Integer limite) {
		this.limite = limite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
		result = prime * result + ((limite == null) ? 0 : limite.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paginacao other = (Paginacao) obj;
		if (inicio == null) {
			if (other.inicio != null)
				return false;
		} else if (!inicio.equals(other.inicio))
			return false;
		if (limite == null) {
			if (other.limite != null)
				return false;
		} else if (!limite.equals(other.limite))
			return false;
		return true;
	}
}
