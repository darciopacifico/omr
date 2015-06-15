package com.msaf.validador.core.tratamentoxls.xls.impl;

import java.util.ArrayList;
import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.AbstractDadosXLS;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public class DadosSintegraXLS extends AbstractDadosXLS {

	public DadosSintegraXLS() {
		super(TipoValidacao.SINTEGRA);
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
		
		valuesBody.add(super.writeValue(this.dados.getIe()));
		valuesBody.add(super.writeValue(this.dados.getIesEncontradas()));
		valuesBody.add(super.writeValue(this.dados.getDataInclusao()));
		valuesBody.add(super.writeValue(this.dados.getDataBaixa()));
		valuesBody.add(super.writeValue(this.dados.getDataConsulta()));
		valuesBody.add(super.writeValue(this.dados.getNumeroConsulta()));
		valuesBody.add(super.writeValue(this.dados.getRegimeApuracao()));
		valuesBody.add(super.writeValue(this.dados.getEnquandramentoEmpresa()));
		valuesBody.add(super.writeValue(this.dados.getSituacao()));
		
	}

	
	private void gerarHeader(){
		
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.ie")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.ieEncontrado")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.dataInclusao")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.dataBaixa")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.dataConsulta")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.numeroConsulta")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.regimeApuracao")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.enquadramento")));
		valuesHeader.add(new ColumnString(super.getLabel("sintegra.situacao")));
	}
	
}
