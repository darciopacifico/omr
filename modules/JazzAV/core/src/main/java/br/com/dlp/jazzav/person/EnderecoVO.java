package br.com.dlp.jazzav.person;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.dlp.jazzav.endereco.EstadoVO;

/**
 * Informações de endereço do cliente. Apenas entrega...
 * @author darcio
 *
 */
@Embeddable
public class EnderecoVO implements Serializable /* extends AbstractEntityVO<Long> virou embeddable */{

	private static final long serialVersionUID = 8405484643413779713L;

	private String cep;

	private String logradouro;
	private String numero;
	private String complemento;
	
	private String uf="";
	private String cidade;
	private String bairro;
	private String pais="Brasil";
	
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Length(min=5, message="Logradouro deve possuir no mínimo 5 caracteres!")
	@NotBlank(message="O logradouro é obrigatório!")
	public String getLogradouro() {
		return logradouro;
	}
	
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	@Length(min=2, message="A cidade deve ser informada!")
	@NotBlank(message="A cidade deve ser informada!")
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@NotBlank(message="Informe o número do logradouro!")
	@Length(max=10)
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
}
