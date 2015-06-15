package com.msaf.validador.consultaonline.xml;

/**
 * Interface que defina a geração de xml para ser realizado a consulta
 * @author ederson
 *
 */
public interface GeradorXmlPedidoValidacao {

	/**
	 * Gera xml correspondente para a pesquisa da dll
	 * @param argumentos
	 * @return
	 */
	public String gerarXml(Object...argumentos);
	
}
