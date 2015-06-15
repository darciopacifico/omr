package br.com.dlp.framework.business;

import java.io.Serializable;
import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Generic abstract business object implementation.
 * 
 * @author dpacifico
 * 
 */
public abstract class AbstractBusinessImpl<B extends IBaseVO<? extends Serializable>> implements IBusiness<B> {
	
	private static final long serialVersionUID = -5087585943211384211L;
	
	/**
	 * Excluir generico.
	 */
	public void delete(final B iBaseVO) throws BaseBusinessException {
		this.deleteValidations(iBaseVO);
		
		this.getDao().delete(iBaseVO);
		
	}
	
	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * BaseBusinessException e depois este deve ser lancado.
	 */
	public void deleteValidations(final B baseVO) throws BaseBusinessException {
	}
	
	/**
	 * Generic exists using bean.
	 */
	public boolean existsByBean(final B bean) {
		return this.getDao().exists(bean);
	}
	
	/**
	 * Generic exists.
	 */
	public boolean exists(final Serializable pk) {
		return this.getDao().exists(pk);
	}
	
	/**
	 * Find all generic.
	 */
	public List<B> findAll() {
		return this.getDao().findAll();
	}
	
	/**
	 * teste.
	 * 
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<B> findAllPaginated(final Integer firstResult, final Integer maxResult) {
		return this.getDao().findAll(firstResult, maxResult);
	}
	
	/**
	 * Generic findByPK using Bean.
	 */
	public B findByBeanPK(final B bean) throws BaseBusinessException {
		return this.getDao().findByPK(bean);
		
	}
	
	/**
	 * Generic findByPK.
	 */
	public B findByPK(final Serializable chave) {
		
		return this.getDao().findByPK(chave);
		
	}
	
	/**
	 * Retorna o DAO da implementação concreta do módulo.
	 * 
	 * @return the dao
	 */
	protected abstract IDAO<B> getDao();
	
	/**
	 * Implemente aqui as validaAAes de negAcio referentes aos metodos de inclusao e alteracao do seu modulo. As exceptions de validacao de
	 * negAcio devem ser acumuladas num BaseBusinessException e depois este deve ser lancado.
	 */
	public void includeUpdateValidations(final B id) throws BaseBusinessException {
	}
	
	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * BaseBusinessException e depois este deve ser lancado.
	 */
	public void includeValidations(final B baseVO) throws BaseBusinessException {
	}
	
	/**
	 * Implementacao padrAo de newVO() - vide comentArio do metodo getVOClass().
	 * 
	 * @return Novo objeto do tipo (Class) especificado no metodo getVOClass()
	 */
	public abstract B newVO();;
	
	/**
	 * dfghfgh.
	 * 
	 * @param baseVO
	 * @return
	 * @throws BaseBusinessException
	 */
	public B saveOrUpdate(B baseVO) throws BaseBusinessException {
		
		this.includeUpdateValidations(baseVO);
		this.includeValidations(baseVO);
		
		B savedBaseVO = this.getDao().saveOrUpdate(baseVO);
		
		return savedBaseVO;
	};
	
	/**
	 * Alterar generico.
	 */
	public B update(B iBaseVO) throws BaseBusinessException {
		this.includeUpdateValidations(iBaseVO);
		this.updateValidations(iBaseVO);
		
		B savedBaseVO = null;
		try {
			savedBaseVO = this.getDao().update(iBaseVO);
		} catch (Exception e) {
		}
		
		return savedBaseVO;
	};
	
	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * BaseBusinessException e depois este deve ser lancado.
	 */
	public void updateValidations(final B baseVO) throws BaseBusinessException {
	}
	
}
