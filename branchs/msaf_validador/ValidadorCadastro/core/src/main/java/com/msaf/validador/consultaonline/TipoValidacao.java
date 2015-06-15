package com.msaf.validador.consultaonline;

import java.util.List;

import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.consultaonline.xml.GeradorXmlANP;
import com.msaf.validador.consultaonline.xml.GeradorXmlAnvisa;
import com.msaf.validador.consultaonline.xml.GeradorXmlCRM;
import com.msaf.validador.consultaonline.xml.GeradorXmlMunicipiosIBGE;
import com.msaf.validador.consultaonline.xml.GeradorXmlPedidoValidacao;
import com.msaf.validador.consultaonline.xml.GeradorXmlReceitaFederal;
import com.msaf.validador.consultaonline.xml.GeradorXmlSUFRAMA;
import com.msaf.validador.consultaonline.xml.GeradorXmlSintegra;

/**
 * Enum que representa os Tipos de validações disponiveis 
 * @author ederson
 *
 */
public enum TipoValidacao {

	
	SINTEGRA(1, new GeradorXmlSintegra())
	, RECEITA_FEDERAL(3, new GeradorXmlReceitaFederal())
	, SUFRAMA(5,new GeradorXmlSUFRAMA())
	, PREFEITURA_SP(7, null)
	, CRM(8, new GeradorXmlCRM())
	, ANVISA(9, new GeradorXmlAnvisa())
	, MUNICIPIOS_IBGE(10, new GeradorXmlMunicipiosIBGE())
	, ANP(13, new GeradorXmlANP())
	, COMPLETA(899, null);
	
	private long codigo;
	
	private GeradorXmlPedidoValidacao geradorXml;
	
	private TipoValidacao(int id, GeradorXmlPedidoValidacao geradorXml) {
		this.codigo = id;
		this.geradorXml = geradorXml;
	}
	
	/**
	 * Retorna o código do tipo da validação
	 * @return
	 */
	public long getCodigo() {
		return this.codigo;
	}

	/**
	 * Pesquisa em uma lista o tipo de validacao do enum
	 * @param list
	 * @return
	 */
	public TpValidVO getTipoValidacao(List<TpValidVO> list) {
		
		if((list == null) || (list.isEmpty())) {
			return null;
		}
		
		TpValidVO vo = new TpValidVO((long) this.codigo);
		
		return list.get(list.indexOf(vo));
	}
	
	/**
	 * Retorna o gerador de xml do tipo de validação 
	 * @return
	 */
	public GeradorXmlPedidoValidacao getGeradorXml() {
		return this.geradorXml;
	}
	

	/**
	 * Retorna um TipoValidacao através do id passado
	 * @param id
	 * @return (null quando não existir)
	 */
	public static TipoValidacao getTipoValidacao(long id) {
		
		TipoValidacao[] values = TipoValidacao.values();
		
		for (TipoValidacao tipoValidacao : values) {
			if(tipoValidacao.getCodigo() == id) {
				return tipoValidacao;
			}
		}
		
		return null;
	}

	/**
	 * Cria um TpValidVO APENAS COM O ID
	 * @return TpValidVO Sómente com o ID preenchido
	 */
	public TpValidVO getTipoValidacao() {
		return new TpValidVO(this.codigo);
	}
	
	
	
}
