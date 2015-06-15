package com.msaf.validador.core.tratamentoxls.xls;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosANP;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosAnvisaXLS;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosIBGEXLS;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosLink;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosPessoaXLS;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosReceitaXLS;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosSintegraXLS;
import com.msaf.validador.core.tratamentoxls.xls.impl.DadosSuframaXLS;

public class DadosXLSService {

	private List<DadosXLS> dados = new ArrayList<DadosXLS>();

	public DadosXLSService() {

		/*
		 * TODO REGISTRAR NA ORDEM QUE DEVE SER GERADO OS DADOS Colocar no
		 * spring
		 */

		registre(new DadosPessoaXLS());
		registre(new DadosReceitaXLS());
		registre(new DadosIBGEXLS());
		registre(new DadosSintegraXLS());
		registre(new DadosSuframaXLS());
		registre(new DadosAnvisaXLS());
		registre(new DadosANP());
		
		registre(new DadosLink(TipoValidacao.RECEITA_FEDERAL, "receita"));
		registre(new DadosLink(TipoValidacao.MUNICIPIOS_IBGE, "ibge"));
		registre(new DadosLink(TipoValidacao.SINTEGRA, "sintegra"));
		registre(new DadosLink(TipoValidacao.SUFRAMA, "suframa"));
		registre(new DadosLink(TipoValidacao.ANVISA, "anvisa"));
		registre(new DadosLink(TipoValidacao.ANP, "anp"));
	}

	public List<Column> getValuesHeader() {

		List<Column> list = new ArrayList<Column>();

		for (DadosXLS xls : dados) {
			list.addAll(xls.getValuesHeader());
		}

		return list;
	}

	public List<Column> getValuesBody() {

		List<Column> list = new ArrayList<Column>();

		for (DadosXLS xls : dados) {
			list.addAll(xls.getValuesBody());
		}

		return list;
	}

	public void prepareBody(PessoaVO pessoa, Set<DadosRetInstVO> dadosVO) {

		for (DadosRetInstVO dadosRetInstVO : dadosVO) {
			
			for (DadosXLS xls : dados) {
				
				if(xls.getTipoValidacao() == null) {
					xls.setParameter(pessoa, null);
					
				}else if(dadosRetInstVO.getTipoValidacaoFk().equals(xls.getTipoValidacao().getTipoValidacao())) {
					
					xls.setParameter(pessoa, dadosRetInstVO);
				}
			}
		}

	}

	private void registre(DadosXLS xls) {
		dados.add(xls);
	}

}
