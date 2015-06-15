package br.com.dlp.jazzomr.jr.util;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Interface padrao para criador de qr code
 * @author darcio
 *
 */
public interface IQRCodeGenerator extends Serializable{
 
	/**
	 * Cria imagem de qrcode contendo o valor informado
	 * @param value
	 * @param w width
	 * @param h heigth
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract BufferedImage generateQRCode(String value, int w, int h);

	/**
	 * Cria imagem de qrcode contendo o valor informado em tamanho padroa 80x80
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract BufferedImage generateQRCode(String value);

}