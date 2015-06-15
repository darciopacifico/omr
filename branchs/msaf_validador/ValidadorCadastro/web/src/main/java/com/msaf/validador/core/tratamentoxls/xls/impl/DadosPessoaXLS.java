package com.msaf.validador.core.tratamentoxls.xls.impl;

import java.util.ArrayList;
import java.util.List;

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.AbstractDadosXLS;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public class DadosPessoaXLS extends AbstractDadosXLS {

	public DadosPessoaXLS() {
		super(null);
	}


	private List<Column> valuesBody = new ArrayList<Column>();
	
	private List<Column> valuesHeader = new ArrayList<Column>();
	
	private PessoaVO pessoa;
	
	
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
		this.pessoa = pessoa;
	}

	
	private void gerarBody() {
		valuesBody.add(super.writeValue(pessoa.getIdentif()));
		valuesBody.add(super.writeValue(pessoa.getLogradouro()));
		valuesBody.add(super.writeValue(pessoa.getNumero()));
		valuesBody.add(super.writeValue(pessoa.getBairro()));
		valuesBody.add(super.writeValue(pessoa.getCep()));
		valuesBody.add(super.writeValue(pessoa.getCidade()));
		valuesBody.add(super.writeValue(pessoa.getEstado()));
		valuesBody.add(super.writeValue(pessoa.getCnpj()));
		valuesBody.add(super.writeValue(pessoa.getCpf()));
	}

	
	private void gerarHeader(){
		
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.idcliente")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.logradouro")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.numero")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.bairro")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.cep")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.cidade")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.estado")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.cnpj")));
		valuesHeader.add(new ColumnString(super.getLabel("pessoa.cpf")));
		
	}
	

	
}
