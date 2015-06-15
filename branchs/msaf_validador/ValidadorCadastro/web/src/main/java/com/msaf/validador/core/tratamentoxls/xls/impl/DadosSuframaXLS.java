package com.msaf.validador.core.tratamentoxls.xls.impl;

import java.util.ArrayList;
import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.AbstractDadosXLS;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public class DadosSuframaXLS extends AbstractDadosXLS {

	public DadosSuframaXLS() {
		super(TipoValidacao.SUFRAMA);
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
		
		valuesBody.add(super.writeValue(this.dados.getSumafra()));
		valuesBody.add(super.writeValue(this.dados.getDataInscSuframa()));
		valuesBody.add(super.writeValue(this.dados.getTipoIncentivo()));
		valuesBody.add(super.writeValue(this.dados.getDataConsulta()));
		valuesBody.add(super.writeValue(this.dados.getSituacao()));
	}

	
	private void gerarHeader(){
		
		valuesHeader.add(new ColumnString(super.getLabel("suframa.codigoSuframa")));
		valuesHeader.add(new ColumnString(super.getLabel("suframa.dataInscricao")));
		valuesHeader.add(new ColumnString(super.getLabel("suframa.tipoIncentivo")));
		valuesHeader.add(new ColumnString(super.getLabel("suframa.dataConsulta")));
		valuesHeader.add(new ColumnString(super.getLabel("suframa.situacao")));
	}


}
