package br.com.dlp.jazzav.anuncio;




/**
 * Status de transacao de pagamento, compativel com pagseguro br.com.uol.pagseguro.domain.TransactionStatus
 * @author darcio
 *
 */
public enum PaymentStatusEnum {

	INITIATED				(0, "Iniciado" 			), 
	WAITING_PAYMENT	(1, "Aguardando Pagamento!" ), 
	IN_ANALYSIS			(2, "Em análise" 		),
	PAID						(3, "Pago" 						),
	AVAILABLE				(4, "Disponível" 			), 
	IN_DISPUTE			(5, "Em disputa" 			),
	REFUNDED				(6, "Reembolsado" 				),
	CANCELLED				(7, "Cancelado" 			),
	ERROR						(99, "Erro" 			);
	
	private int code;
	private String desc;
	
	private PaymentStatusEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	
}
