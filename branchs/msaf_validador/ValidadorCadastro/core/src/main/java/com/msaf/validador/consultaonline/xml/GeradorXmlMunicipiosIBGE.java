package com.msaf.validador.consultaonline.xml;

import java.text.MessageFormat;

public class GeradorXmlMunicipiosIBGE implements GeradorXmlPedidoValidacao {

	@Override
	public String gerarXml(Object... argumentos) {

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
						+ "<ParametrosConsulta Versao=\"1.0\"> "
						+ "<Municipio>{0}</Municipio>" + "<Estado>{1}</Estado>"

						+ "<Proxy>" + "<Ativo>{2}</Ativo>"
						+ "<Usuario>{3}</Usuario>" + "<Senha>{4}</Senha>"
						+ "<Host>{5}</Host>" + "<Porta>{6}</Porta>"
						+ "</Proxy>" + "</ParametrosConsulta>", 
				cidade,
				estado, 
				proxyAtivo, 
				proxyUsuario, 
				proxySenha, 
				proxyHost,
				proxyPorta);

	}

}
