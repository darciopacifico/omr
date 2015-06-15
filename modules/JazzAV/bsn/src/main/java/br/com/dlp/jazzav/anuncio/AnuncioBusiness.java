package br.com.dlp.jazzav.anuncio;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;

import br.com.dlp.jazzav.exam.IJazzOMRBusiness;

/**
 * 
 * @author darcio
 *
 */
public interface AnuncioBusiness extends IJazzOMRBusiness<AnuncioVO> {

	List<ModeloAdesivoVO> findAllModelos();

	ModeloAdesivoVO getModeloPadrao();

	URL efetuarPagamento(AnuncioVO anuncio) throws JazzAVPaymentException;

	AnuncioVO confirmarAnuncio(String id_pagseguro) throws TransacaoNaoEncontradaException, JazzAVPaymentException;

	AnuncioVO registraNotificacao(String notificationCode) throws JazzAVPaymentException, TransacaoNaoEncontradaException ;

	AnuncioVO findAnuncioVOByTransaction(String id_pagseguro) throws JazzAVPaymentException;
	
	
	
}
