package com.msaf.validador.core.tratamentoxls.xls.impl;

import java.util.ArrayList;
import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.AbstractDadosXLS;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public class DadosANP extends AbstractDadosXLS {

	public DadosANP() {
		super(TipoValidacao.ANP);
	}


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
		
		if(this.dados == null){
			this.dados = new DadosRetInstVO();
		}
		
		valuesBody.add(super.writeValue(this.dados.getNomeFantasia()));
		valuesBody.add(super.writeValue(this.dados.getBandeiraPosto()));
		valuesBody.add(super.writeValue(this.dados.getAutorizacao()));
		valuesBody.add(super.writeValue(this.dados.getDataPublicacao()));
		valuesBody.add(super.writeValue(this.dados.getTipoPosto()));
		valuesBody.add(super.writeValue(this.dados.getHoraConsulta()));
	}

	
	private void gerarHeader(){
		valuesHeader.add(new ColumnString(super.getLabel("anp.nomeFantasia")));
		valuesHeader.add(new ColumnString(super.getLabel("anp.bandeiraPosto")));
		valuesHeader.add(new ColumnString(super.getLabel("anp.autorizacao")));
		valuesHeader.add(new ColumnString(super.getLabel("anp.dataPublicacao")));
		valuesHeader.add(new ColumnString(super.getLabel("anp.tipoPosto")));
		valuesHeader.add(new ColumnString(super.getLabel("anp.horaConsulta")));
	}


}
