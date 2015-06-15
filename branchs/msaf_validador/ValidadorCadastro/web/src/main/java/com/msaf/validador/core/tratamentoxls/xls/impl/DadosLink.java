package com.msaf.validador.core.tratamentoxls.xls.impl;

import java.util.ArrayList;
import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.AbstractDadosXLS;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnLink;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public class DadosLink extends AbstractDadosXLS {

	public DadosLink(TipoValidacao tipoValidacao, String prefixo) {
		super(tipoValidacao);
		this.prefixo = prefixo;
	}

	private String prefixo;
	
	private List<Column> valuesBody = new ArrayList<Column>();
	
	private List<Column> valuesHeader = new ArrayList<Column>();
	
	private DadosRetInstVO dados;
	
	@Override
	public List<Column> getValuesBody() {
		this.gerarBody();
		return valuesBody;
	}

	@Override
	public List<Column> getValuesHeader() {
		this.gerarHeader();
		return valuesHeader;
	}

	@Override
	public void setParameter(PessoaVO pessoa, DadosRetInstVO vo) {
		this.dados = vo;
	}

	
	private void gerarBody() {

		if(super.getTipoValidacao() == null) {
			return;
		}
		
		if(this.dados == null || this.dados.getPagina() == null) {
			valuesBody.add(new ColumnString(super.getLabel(prefixo + ".link.empty")));
			return;
		}
		
		String key = prefixo + ".link.texto";
		
		valuesBody.add(new ColumnLink("" + this.dados.getPk()).setTextoLink(super.getLabel(key)));
		
	}

	
	private void gerarHeader(){
		
		if(super.getTipoValidacao() == null) {
			return;
		}
		
		valuesHeader.add(new ColumnString(super.getLabel(prefixo + ".link")));
	}

}
