package com.msaf.validador.integration.hibernate.impl;

import java.util.List;

import javax.persistence.Query;

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.integration.hibernate.base.DAOGenericoHibernate;
import com.msaf.validador.integration.hibernate.intf.IDadosRetInstDAO;


public class DadosRetInstHibernateDAO  
	extends DAOGenericoHibernate<DadosRetInstVO, Long> implements IDadosRetInstDAO {

	
	@SuppressWarnings("unchecked")
	public List<DadosRetInstVO> findByPessoa(Long idPessoa) {

		
		Query query = this.getEntityManager().createQuery("Select d.pk,d.cpf,d.situacaoCadastral,d.codigoConsulta,d.nome,d.bairro,d.cep,d.cidade,d.cnpj,d.complemento,d.dataBaixa,d.dataConsulta,d.dataInclusao,d.enquandramentoEmpresa,d.estado,d.ie,d.ie2,d.sumafra,d.dataInscSuframa,d.incentivo,d.dataValidade,d.atividadeEconomica,d.tipoIncentivo,d.tsentaTSA,d.CRM,d.tipoInscricao,d.especialidade,d.codigoIBGE,d.nomeFantasia,d.bandeiraPosto,d.autorizacao,d.dataPublicacao,d.tipoPosto,d.horaConsulta,d.dataAbertura,d.codigoAE,d.descricaoAE,d.codigoNJ,d.descricaoNJ,d.dataSituacao,d.situacaoEspecial,d.dataSituacaoEspecial,d.iesEncontradas,d.logradouro,d.numero,d.numeroConsulta,d.qtdeEncontrada,d.razaoSocial,d.regimeApuracao,d.situacao,d.identif,d.tipoDocumento,d.tipoResultadoFk,d.tipoValidacaoFk from DadosRetInstVO d where d.pessoaVO.id = :idPessoa");
		
		query.setParameter("idPessoa", idPessoa);
		
		List<DadosRetInstVO> pedidos = query.getResultList();
		
		return pedidos;
	}

	
	@SuppressWarnings("unchecked")
	public List<DadosRetInstVO> findPagesByPessoa(Long idPessoa) {
		
		Query query = this.getEntityManager().createQuery("Select d from DadosRetInstVO d where d.pessoaVO.id = :idPessoa");
		
		query.setParameter("idPessoa", idPessoa);
		
		List<DadosRetInstVO> pedidos = query.getResultList();
		
		return pedidos;
	}
	
}
