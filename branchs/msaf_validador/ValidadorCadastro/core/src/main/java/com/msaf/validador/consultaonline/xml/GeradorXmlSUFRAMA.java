package com.msaf.validador.consultaonline.xml;

import java.text.MessageFormat;

public class GeradorXmlSUFRAMA implements GeradorXmlPedidoValidacao {

	
	@Override
	public String gerarXml(Object... argumentos) {

		// para todos os geradores receberem os mesmos parametros este não utiliza o (argumentos[0])
		int index = 0;

		String estado = (String) argumentos[index++];
		String documento = (String) argumentos[index++];
		String proxyAtivo = (String) argumentos[index++];
		String proxyUsuario = (String) argumentos[index++];
		String proxySenha = (String) argumentos[index++];
		String proxyHost = (String) argumentos[index++];
		String proxyPorta = (String) argumentos[index++];
		String tipoDocumento = (String) argumentos[index++];
		String cnpj = (String) argumentos[index++];
		String cidade = (String) argumentos[index++];


		return MessageFormat.format(
				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
						+ "<ParametrosConsulta Versao=\"1.0\">"
						+ "<Valor>{0}</Valor>" + "<Proxy>"
						+ "<Ativo>{1}</Ativo>" + "<Usuario>{2}</Usuario>"
						+ "<Senha>{3}</Senha>" + "<Host>{4}</Host>"
						+ "<Porta>{5}</Porta>" + "</Proxy>"
						+ "</ParametrosConsulta>", 
				documento, 
				proxyAtivo,
				proxyUsuario, 
				proxySenha, 
				proxyHost, 
				proxyPorta);

	}

}
