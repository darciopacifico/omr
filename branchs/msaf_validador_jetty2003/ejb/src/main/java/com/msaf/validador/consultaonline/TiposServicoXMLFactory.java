package com.msaf.validador.consultaonline;

import java.text.MessageFormat;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;

/**
 * Classe responsável por fazer o switch das validacoes existentes
 * de acordo com o seu respectivo tipo.
 * @author vista
 *
 */
public class TiposServicoXMLFactory {

	// TIPOS DE VALIDACOES
	public static final int SINTEGRA = 1;
	public static final int RECEITA_FEDERAL = 3; 
	public static final int SUFRAMA = 5; 
	public static final int PREF_SP = 7; // em implementacao
	public static final int CRM = 8;
	public static final int ANVISA = 9; 
	public static final int MUNICIPIOS_IBGE = 10; 
	public static final int ANP = 13;
	public static final int COMPLETA = 899;

	public static String TIPO_DOCUMENTO_SINTEGRA_CNPJ = "0";
	public static String TIPO_DOCUMENTO_SINTEGRA_IE = "1";
	
	public static String TIPO_DOCUMENTO_RECEITA_CNPJ = "0";
	public static String TIPO_DOCUMENTO_RECEITA_CPF = "1";
	
public String geraXmlDePedidoValidacaoSintegra(
		String tipoValidacao, 
		String estado, 
		String documento,
		String proxyAtivo,
		String proxyUsuario,
		String proxySenha,
		String proxyHost,
		String proxyPorta
		) {	
	return MessageFormat
			.format(
					"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
					+ "<ParametrosConsulta Versao=\"1.0\">"
					+ "<TipoConsulta>{0}</TipoConsulta>"
					+ "<Estado>{1}</Estado>"
					+ "<Valor>{2}</Valor>"
					+ "<Proxy>"
					+ "<Ativo>{3}</Ativo>"
					+ "<Usuario>{4}</Usuario>"
					+ "<Senha>{5}</Senha>"
					+ "<Host>{6}</Host>"
					+ "<Porta>{7}</Porta>"
					+ "</Proxy>"
					+ "</ParametrosConsulta>",
					tipoValidacao, 
					estado, 
					documento, 
					proxyAtivo,
					proxyUsuario,
					proxySenha,
					proxyHost,
					proxyPorta);
}
	
	
//FIXME: FALTA MONTAR A ESTRUTURA DE CONSULTA PARA A RF
private String geraXmlDePedidoValidacaoFederal(	
		String tipoValidacao, 
		String estado, 
		String documento,
		String proxyAtivo,
		String proxyUsuario,
		String proxySenha,
		String proxyHost,
		String proxyPorta
		) {

	
	return MessageFormat.format(
			"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
			+ "<ParametrosConsulta Versao=\"1.0\">"
			+ "<TipoConsulta>{0}</TipoConsulta>"
			+ "<Estado>{1}</Estado>"
			+ "<Valor>{2}</Valor>"
			+ "<Proxy>"
			+ "<Ativo>{3}</Ativo>"
			+ "<Usuario>{4}</Usuario>"
			+ "<Senha>{5}</Senha>"
			+ "<Host>{6}</Host>"
			+ "<Porta>{7}</Porta>"
			+ "</Proxy>"
			+ "</ParametrosConsulta>",
			tipoValidacao, 
			estado, 
			documento, 
			proxyAtivo,
			proxyUsuario,
			proxySenha,
			proxyHost,
			proxyPorta);
}




private String geraXmlDePedidoValidacaoCRM(
		String documento, 
		String estado, 
		String proxyAtivo, 
		String proxyUsuario, 
		String proxySenha, 
		String proxyHost, 
		String proxyPorta) {

	return MessageFormat
			.format(
					"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?> " +
					"<ParametrosConsulta Versao=\"1.0\">"
							+ "<CRM>{0}</CRM>" 
							+"<Estado>{1}</Estado>" 
							+ "<Proxy>"
							+ "<Ativo>{2}</Ativo>"
							+ "<Usuario>{3}</Usuario>"
							+ "<Senha>{4}</Senha>"
							+ "<Host>{5}</Host>"
							+ "<Porta>{6}</Porta>"
							+ "</Proxy>"
							+ "</ParametrosConsulta>",
							documento, 
							estado,  
							proxyAtivo,
							proxyUsuario,
							proxySenha,
							proxyHost,
							proxyPorta);
}

private String geraXmlDePedidoValidacaoAnvisa(
		String documento, 
		String proxyAtivo, 
		String proxyUsuario, 
		String proxySenha, 
		String proxyHost, 
		String proxyPorta) {

	return MessageFormat
			.format(
					"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
					"<ParametrosConsulta Versao=\"1.0\">"
							+ "<CNPJ>{0}</CNPJ>" 
							+ "<Proxy>"
							+ "<Ativo>{1}</Ativo>"
							+ "<Usuario>{2}</Usuario>"
							+ "<Senha>{3}</Senha>"
							+ "<Host>{4}</Host>"
							+ "<Porta>{5}</Porta>"
							+ "</Proxy>"
							+ "</ParametrosConsulta>",
							documento, 
							proxyAtivo,
							proxyUsuario,
							proxySenha,
							proxyHost,
							proxyPorta);
} 



private String geraXmlDePedidoValidacaoMunicipioIBGE(
		String municipio,
		String estado, 
		String proxyAtivo, 
		String proxyUsuario, 
		String proxySenha, 
		String proxyHost, 
		String proxyPorta) {

	return MessageFormat
			.format(
					"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
					"<ParametrosConsulta Versao=\"1.0\"> "
							+ "<Municipio>{0}</Municipio>" 
							+ "<Estado>{1}</Estado>" 
							
							+ "<Proxy>"
							+ "<Ativo>{2}</Ativo>"
							+ "<Usuario>{3}</Usuario>"
							+ "<Senha>{4}</Senha>"
							+ "<Host>{5}</Host>"
							+ "<Porta>{6}</Porta>"
							+ "</Proxy>"
							+ "</ParametrosConsulta>",
							municipio, 
							estado,
							proxyAtivo,
							proxyUsuario,
							proxySenha,
							proxyHost,
							proxyPorta);
}

private String geraXmlDePedidoValidacaoANP(
		String documento, 
		String proxyAtivo, 
		String proxyUsuario, 
		String proxySenha, 
		String proxyHost, 
		String proxyPorta) {

	return MessageFormat
			.format(
					"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
					"<ParametrosConsulta Versao=\"1.0\">" +
					"<Valor>{0}</Valor>" 
					+ "<Proxy>"
					+ "<Ativo>{1}</Ativo>"
					+ "<Usuario>{2}</Usuario>"
					+ "<Senha>{3}</Senha>"
					+ "<Host>{4}</Host>"
					+ "<Porta>{5}</Porta>"
					+ "</Proxy>"
					+ "</ParametrosConsulta>",
					documento,
					proxyAtivo,
					proxyUsuario,
					proxySenha,
					proxyHost,
					proxyPorta);
} 


private String geraXmlDePedidoValidacaoSumafra(
		String documento, 
		String proxyAtivo, 
		String proxyUsuario, 
		String proxySenha, 
		String proxyHost, 
		String proxyPorta) {

	return MessageFormat
			.format(
					"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
					"<ParametrosConsulta Versao=\"1.0\">" +
					"<Valor>{0}</Valor>" 
					+ "<Proxy>"
					+ "<Ativo>{1}</Ativo>"
					+ "<Usuario>{2}</Usuario>"
					+ "<Senha>{3}</Senha>"
					+ "<Host>{4}</Host>"
					+ "<Porta>{5}</Porta>"
					+ "</Proxy>"
					+ "</ParametrosConsulta>",
					documento,
					proxyAtivo,
					proxyUsuario,
					proxySenha,
					proxyHost,
					proxyPorta);
} 




//private String geraXmlDePedidoValidacaoANP(
//		String documento, 
//		String proxyAtivo, 
//		String proxyUsuario, 
//		String proxySenha, 
//		String proxyHost, 
//		String proxyPorta) {
//
//	return MessageFormat
//			.format(
//					"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
//					"<ParametrosConsulta Versao=\"1.0\">" 
//					+ "<CNPJ>{0}</CNPJ>"
//					+ "<Proxy>"
//					+ "<Ativo>{1}</Ativo>"
//					+ "<Usuario>{2}</Usuario>"
//					+ "<Senha>{3}</Senha>"
//					+ "<Host>{4}</Host>"
//					+ "<Porta>{5}</Porta>"
//					+ "</Proxy>"
//					+ "</ParametrosConsulta>",
//					documento,
//					proxyAtivo,
//					proxyUsuario,
//					proxySenha,
//					proxyHost,
//					proxyPorta);
//
//} 
//Anvisa:
//
//	<?xml version="1.0" encoding="iso-8859-1"?>
//
//	<ParametrosConsulta Versao="1.0">
//
//	  <CNPJ>06273675000271</CNPJ>
//
//	  <Proxy>
//
//	    <Ativo>False</Ativo>
//
//	    <Usuario/>
//
//	    <Senha/>
//
//	    <Host>192.168.10.24</Host>
//
//	    <Porta>8002</Porta>
//
//	  </Proxy>
//
//	</ParametrosConsulta>

	/**
	 * Seleciona um XML de cosulta do tipo de validacao de acordo com o tipo de
	 * validacao enviado.
	 * 
	 * @param numDocumento  - numero documento.
	 * @param tipoValidacao  - tipo de servico a ser requisitado.
	 * @param parameterObject
	 * @param proxyAtivo
	 * @param proxyUsuario
	 * @param proxySenha
	 * @param proxyHost
	 * @param proxyPorta
	 * @return
	 */
	public String selecionaXMLTipoValicao(
			PessoaVO pessoaVO, 
			TpValidVO tpValidVO, 
			String tipoDocumento,
			DllDadosDTO configuracaoDll) {
		
		Integer tipoValidacao = tpValidVO.getId().intValue(); 
		
		String proxyAtivo=configuracaoDll.getProxyAtivo();
		String proxyUsuario=configuracaoDll.getProxyUsuario();
		String proxySenha=configuracaoDll.getProxySenha();
		String proxyHost=configuracaoDll.getProxyHost();
		String proxyPorta=configuracaoDll.getProxyPorta();
		
		String cnpj = pessoaVO.getCnpj();

		String documento = determinaNumDocumento(tipoDocumento, tipoValidacao, pessoaVO);

		String xmlConsulta = new String();
		switch (tipoValidacao.intValue()) {
		case SINTEGRA:
			xmlConsulta = this.geraXmlDePedidoValidacaoSintegra(tipoDocumento, pessoaVO.getEstado(), documento, proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta);
			break;

		case RECEITA_FEDERAL: // FIXME: FALTA MONTAR A ESTRUTURA DE CONSULTA PARA A
			
			if (pessoaVO.getCnpj()!=null){
				tipoDocumento = "0";
			}else{
				tipoDocumento = "1";
			}
			
			
			xmlConsulta = this.geraXmlDePedidoValidacaoFederal(tipoDocumento, pessoaVO.getEstado(), documento, proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta);
			break;

		case CRM:
			xmlConsulta = this.geraXmlDePedidoValidacaoCRM(documento, pessoaVO.getEstado(), proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta);
			break;

		case ANVISA:
			xmlConsulta = this.geraXmlDePedidoValidacaoAnvisa(cnpj, proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta);
			break;

		case MUNICIPIOS_IBGE:
			xmlConsulta = this.geraXmlDePedidoValidacaoMunicipioIBGE(pessoaVO.getCidade(), pessoaVO.getEstado(), proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta);
			break;

		case PREF_SP: // FIXME:PESQUISAR (EM IMPLEMENTACAO)
			break;

		case SUFRAMA: // FIXME:PESQUISAR
			xmlConsulta = this.geraXmlDePedidoValidacaoSumafra(cnpj, proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta);
			break;

		case ANP: // FIXME:PESQUISAR
			xmlConsulta = this.geraXmlDePedidoValidacaoANP(cnpj, proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta);
			break;

		default:
			xmlConsulta = "";
		}

		return xmlConsulta;
	}

	
	
	/**
	 * sobrecarga do método para as demais consultas que nao necessitam de tipoDocumento.
	 * @param pessoaVO
	 * @param tpValidVO
	 * @param configuracaoDLL
	 * @return
	 */
	public String selecionaXMLTipoValicao(PessoaVO pessoaVO,
			TpValidVO tpValidVO, DllDadosDTO configuracaoDLL) {
		
		return this.selecionaXMLTipoValicao(pessoaVO, tpValidVO, "", configuracaoDLL);

	}

	
	
	/**
	 * Determina número do documento que será utilizando na montagem do XML de entrada.
	 * @param tipoDocumento
	 * @param tipoValidacao
	 * @param pessoaVO
	 * @return
	 */
	protected String determinaNumDocumento(String tipoDocumento, Integer tipoValidacao, PessoaVO pessoaVO) {
		
		String numDocumento;

		
		if (tipoValidacao.equals(SINTEGRA) && tipoDocumento.equals(TiposServicoXMLFactory.TIPO_DOCUMENTO_SINTEGRA_IE)) {
			numDocumento = pessoaVO.getIe();
			
		} else if (tipoValidacao.equals(RECEITA_FEDERAL) && pessoaVO.getCnpj() == null) {
			numDocumento = pessoaVO.getCpf();
			
		} else {
			numDocumento = pessoaVO.getCnpj();
			
		}
		
		return numDocumento;
	}




}