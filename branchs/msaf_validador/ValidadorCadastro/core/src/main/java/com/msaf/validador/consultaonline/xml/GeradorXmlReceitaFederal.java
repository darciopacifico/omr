package com.msaf.validador.consultaonline.xml;

import java.text.MessageFormat;

import com.msaf.validador.consultaonline.TipoDocumento;


public class GeradorXmlReceitaFederal implements GeradorXmlPedidoValidacao {

	
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

		
		if (cnpj != null){
			tipoDocumento = TipoDocumento.RECEITA_CNPJ.getCodigo();
		}else{
			tipoDocumento = TipoDocumento.RECEITA_CPF.getCodigo();
		}
		

		return MessageFormat.format(
				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
						+ "<ParametrosConsulta Versao=\"1.0\">"
						+ "<TipoConsulta>{0}</TipoConsulta>"
						+ "<Estado>{1}</Estado>" + "<Valor>{2}</Valor>"
						+ "<Proxy>" + "<Ativo>{3}</Ativo>"
						+ "<Usuario>{4}</Usuario>" + "<Senha>{5}</Senha>"
						+ "<Host>{6}</Host>" + "<Porta>{7}</Porta>"
						+ "</Proxy>" + "</ParametrosConsulta>", 
				tipoDocumento,
				estado, 
				documento, 
				proxyAtivo, 
				proxyUsuario, 
				proxySenha,
				proxyHost, 
				proxyPorta);

	}

}
