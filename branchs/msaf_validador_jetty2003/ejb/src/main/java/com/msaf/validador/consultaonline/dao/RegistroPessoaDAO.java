package com.msaf.validador.consultaonline.dao;

import java.util.List;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;

public interface RegistroPessoaDAO {

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#criarRegistroPessoa(persistencia.vos.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#criarRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	public abstract void criarRegistroPessoa(PessoaVO registroPessoa);

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#buscarDadosRetornoId(java.lang.Long)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarDadosRetornoId(java.lang.Long)
	 */
	public abstract PessoaVO buscarRegistroPessoaId(Long id);

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#atualizarRegistroPessoa(persistencia.vos.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#atualizarRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	public abstract void atualizarRegistroPessoa(PessoaVO registroPessoa);

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#removerRegistroPessoa(persistencia.vos.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#removerRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	public abstract void removerRegistroPessoa(PessoaVO registroPessoa);

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#buscarTodos()
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarTodos()
	 */
	public abstract List buscarTodos();

	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarPorNome(java.lang.String)
	 */
	public abstract PessoaVO buscarPorNome(String dadosRetornoNome);

}