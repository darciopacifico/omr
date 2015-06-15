package com.msaf.validador.consultaonline;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.consultaonline.xml.GeradorXmlPedidoValidacao;

/**
 * Classe responsável por fazer o switch das validacoes existentes
 * de acordo com o seu respectivo tipo.
 * @author vista
 *
 */
public class TiposServicoXMLFactory {


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
	public String selecionaXMLTipoValicao(PessoaVO pessoaVO, TpValidVO tpValidVO, String tipoDocumento, DllDadosDTO configuracaoDll) {
		
		Integer tipoValidacao = tpValidVO.getId().intValue(); 
		
		String proxyAtivo=configuracaoDll.getProxyAtivo();
		String proxyUsuario=configuracaoDll.getProxyUsuario();
		String proxySenha=configuracaoDll.getProxySenha();
		String proxyHost=configuracaoDll.getProxyHost();
		String proxyPorta=configuracaoDll.getProxyPorta();
		  
		String cnpj = pessoaVO.getCnpj();

		String xmlConsulta = "";
		
		// pega um enum tipo validacao 
		TipoValidacao enumTipoValidacao = TipoValidacao.getTipoValidacao(tipoValidacao);
		
		// verifica se é um enum válido
		if(enumTipoValidacao != null) {
			
			GeradorXmlPedidoValidacao geradorXml = enumTipoValidacao.getGeradorXml();
			
			// verifica se tem um gerador para esse tipo de validacao
			if(geradorXml != null) {
				xmlConsulta = geradorXml.gerarXml(pessoaVO.getEstado(), tipoDocumento, proxyAtivo, proxyUsuario, proxySenha, proxyHost, proxyPorta, tipoDocumento, cnpj, pessoaVO.getCidade());
			}
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

	
	





}