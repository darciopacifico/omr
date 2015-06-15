package br.com.dlp.jazzav.anuncio.status;

import br.com.dlp.jazzav.anuncio.PaymentStatusEnum;
import br.com.uol.pagseguro.domain.TransactionStatus;

public class EnumStatusTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(PaymentStatusEnum.valueOf("IN_DISPUTE").ordinal());

		System.out.println(TransactionStatus.IN_DISPUTE.getValue());

		System.out.println(PaymentStatusEnum.valueOf("WAITING_PAYMENT").ordinal());

		System.out.println(TransactionStatus.WAITING_PAYMENT.getValue());
		
		

	}

}
