package com.msaf.validador.core.tratamentoxls.xls.impl;

import java.util.ArrayList;
import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.AbstractDadosXLS;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public class DadosReceitaXLS extends AbstractDadosXLS {

	public DadosReceitaXLS() {
		super(TipoValidacao.RECEITA_FEDERAL);
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
		
		valuesBody.add(super.writeValue(this.dados.getRazaoSocial()));
		valuesBody.add(super.writeValue(this.dados.getNomeFantasia()));
		valuesBody.add(super.writeValue(this.dados.getLogradouro()));
		valuesBody.add(super.writeValue(this.dados.getNumero()));
		valuesBody.add(super.writeValue(this.dados.getComplemento()));
		valuesBody.add(super.writeValue(this.dados.getCep()));
		valuesBody.add(super.writeValue(this.dados.getBairro()));
		valuesBody.add(super.writeValue(this.dados.getMunicipio()));
		valuesBody.add(super.writeValue(this.dados.getEstado()));
		valuesBody.add(super.writeValue(this.dados.getCodigoAE()));
		valuesBody.add(super.writeValue(this.dados.getDescricaoAE()));
		valuesBody.add(super.writeValue(this.dados.getCodigoNJ()));
		valuesBody.add(super.writeValue(this.dados.getDescricaoNJ()));
		valuesBody.add(super.writeValue(this.dados.getDataSituacao()));
		valuesBody.add(super.writeValue(this.dados.getDataAbertura()));
		valuesBody.add(super.writeValue(this.dados.getDataConsulta()));
		valuesBody.add(super.writeValue(this.dados.getSituacao()));
		valuesBody.add(super.writeValue(this.dados.getSituacaoEspecial()));
		valuesBody.add(super.writeValue(this.dados.getDataSituacaoEspecial()));
	}

	
	private void gerarHeader(){
		
		valuesHeader.add(new ColumnString(super.getLabel("receita.razaoSocial")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.nomeFantasia")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.logradouro")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.numero")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.complemento")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.cep")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.bairro")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.municipio")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.estado")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.codigoAE")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.descricaoAE")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.codigoNJ")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.descricaoNJ")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.dataSituacao")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.dataAbertura")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.dataConsulta")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.situacao")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.situacaoEspecial")));
		valuesHeader.add(new ColumnString(super.getLabel("receita.dataSituacaoEspecial")));
	}

}
