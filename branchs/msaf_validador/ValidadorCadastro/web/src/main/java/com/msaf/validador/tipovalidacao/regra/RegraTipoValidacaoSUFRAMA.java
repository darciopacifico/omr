package com.msaf.validador.tipovalidacao.regra;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.CategoriaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ValorCategoriaVO;
import com.msaf.validador.consumer.util.Util;
import com.msaf.validador.core.Factory;
import com.msaf.validador.integration.hibernate.Repositorio;
import com.msaf.validador.integration.hibernate.intf.IValorCategoriaHibernateDAO;
import com.msaf.validador.tipoValidacao.regra.AbstractRegra;

public class RegraTipoValidacaoSUFRAMA extends AbstractRegra {

	public RegraTipoValidacaoSUFRAMA(String nameValidacao){
		this.nameValidacao = nameValidacao;
	}
	
	private final String nameValidacao;
	
	private TipoValidacao tipoValidacao;
	
	@Override
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO vo) {

		Repositorio repositorio = (Repositorio) Factory.getBean("repositorio");
		
		IValorCategoriaHibernateDAO dao = repositorio.getValorCategoriaDAO();
		
		CategoriaVO categoria = new CategoriaVO();
		categoria.setNome(this.nameValidacao);

		ValorCategoriaVO valorCategoria = new ValorCategoriaVO();
		valorCategoria.setCategoria(categoria);
		
		/*
		 * percorre os resultados anteriores para verificar se o estado é de SP
		 */
		for (ResulConsVO resulConsVO : resultadosAnteriores) {

			/*
			 * percorre os resultados anteriores
			 */
			for (DadosRetInstVO dados : resulConsVO.getRegistrosRetorno()) {

				if (!Util.isEmpty(dados.getEstado())) {

					valorCategoria.setNome(dados.getEstado().trim());
					
					if(dao.existisValorCategoria(valorCategoria)){
						return this.tipoValidacao;
					} else {
						return null;
					}
				}
			}
		}
		
		
		
		return null;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;
	}

}
