package com.msaf.validador.core.tratamentoxls.xls.impl;

import java.util.ArrayList;
import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.AbstractDadosXLS;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public class DadosAnvisaXLS  extends AbstractDadosXLS {

	public DadosAnvisaXLS() {
		super(TipoValidacao.ANVISA);
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
		
		valuesBody.add(super.writeValue(this.dados.getSituacao()));
		valuesBody.add(super.writeValue(this.dados.getAutorizacao()));
	}

	
	private void gerarHeader(){
		valuesHeader.add(new ColumnString(super.getLabel("anvisa.situacao")));
		valuesHeader.add(new ColumnString(super.getLabel("anvisa.autorizacao")));
	}
}
