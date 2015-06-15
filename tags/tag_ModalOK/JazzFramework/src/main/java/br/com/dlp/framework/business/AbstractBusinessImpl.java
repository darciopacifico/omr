package br.com.dlp.framework.business;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Generic abstract business object implementation
 * 
 * @author dpacifico
 * 
 */
public abstract class AbstractBusinessImpl<B extends IBaseVO<? extends Serializable>> implements IBusiness<B> {

	private static final long serialVersionUID = -5087585943211384211L;

	/**
	 * Implementacao padrAo de newVO() - vide comentArio do metodo getVOClass()
	 * 
	 * @return Novo objeto do tipo (Class) especificado no metodo getVOClass()
	 * @throws BaseBusinessException
	 *           Excecao de negAcio ao tentar criar o novo objeto
	 * @throws BaseTechnicalError
	 *           Excecao tAcnica ao tentar criar o novo objeto
	 * @throws RemoteException
	 *           Excecao padrAo do contrato RMI/EJB 2.x (uma m.... por sinal!)
	 */
	public abstract B newVO();

	/**
	 * Retorna o DAO da implementação concreta do módulo
	 * @return the dao
	 */
	protected abstract IDAO<B> getDao();

	/**
	 * 
	 * @param baseVO
	 * @return
	 * @throws BaseBusinessException
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 */
	public B saveOrUpdate(B baseVO) throws BaseBusinessException {

		includeUpdateValidations(baseVO);
		includeValidations(baseVO);

		baseVO = getDao().saveOrUpdate(baseVO);

		return baseVO;
	}

	/**
	 * Alterar generico
	 */
	public B update(B iBaseVO) throws BaseBusinessException {
		includeUpdateValidations(iBaseVO);
		updateValidations(iBaseVO);

		iBaseVO = getDao().update(iBaseVO);

		return iBaseVO;
	}

	/**
	 * Excluir generico
	 */
	public void delete(B iBaseVO) throws BaseBusinessException {
		deleteValidations(iBaseVO);

		getDao().delete(iBaseVO);

	}

	/**
	 * Generic findByPK
	 */
	public B findByPK(Serializable chave) {

		B iBaseVO = getDao().findByPK(chave);
		return iBaseVO;

	}

	/**
	 * Generic findByPK using Bean
	 */
	public B findByPK(B bean) throws BaseBusinessException {

		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		B iBaseVO = getDao().findByPK(bean);
		return iBaseVO;

	}

	/**
	 * Generic exists
	 */
	public boolean exists(Serializable pk) {
		return getDao().exists(pk);
	}

	/**
	 * Generic exists using bean
	 */
	public boolean exists(B bean) {
		return getDao().exists(bean);
	}

	/**
	 * Find all generic
	 */
	public List<B> findAll() {
		List<B> list = getDao().findAll();
		return list;
	}

	/**
	 * 
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<B> findAll(Integer firstResult, Integer maxResult) {
		List<B> list = getDao().findAll(firstResult, maxResult);
		return list;
	}

	/**
	 * Implemente aqui as validaAAes de negAcio referentes aos metodos de inclusao e alteracao do seu modulo. As exceptions de
	 * validacao de negAcio devem ser acumuladas num BaseBusinessException e depois este deve ser lancado
	 */
	public void includeUpdateValidations(B id) throws BaseBusinessException {
	};

	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * BaseBusinessException e depois este deve ser lancado
	 */
	public void includeValidations(B baseVO) throws BaseBusinessException {
	};

	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * BaseBusinessException e depois este deve ser lancado
	 */
	public void updateValidations(B baseVO) throws BaseBusinessException {
	};

	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * BaseBusinessException e depois este deve ser lancado
	 */
	public void deleteValidations(B baseVO) throws BaseBusinessException {
	}

}