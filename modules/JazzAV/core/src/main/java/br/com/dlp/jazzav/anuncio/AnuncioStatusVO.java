package br.com.dlp.jazzav.anuncio;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.dlp.jazzav.AbstractLogEntityVO;
import br.com.dlp.jazzav.person.PessoaVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * Guarda historico de status de um anuncio
 * @author darcio
 *
 */
@Entity
@JazzClass(name = "AnuncioStatus")
public class AnuncioStatusVO extends AbstractLogEntityVO<Long> {

	private static final long serialVersionUID = -6006867130668960810L;

	private PaymentStatusEnum paymentStatus;
	private String descString;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}

	@Enumerated(EnumType.STRING)
	public PaymentStatusEnum getPaymentStatus() {
		return paymentStatus;
	}


	public String getDescString() {
		return descString;
	}

	public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public void setDescString(String descString) {
		this.descString = descString;
	}

}
