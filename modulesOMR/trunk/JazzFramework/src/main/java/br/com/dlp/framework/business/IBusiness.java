package br.com.dlp.framework.business;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Interface básica para contrato de componentes de negócio.
 * 
 * @author dpacifico
 * 
 * @param <PK>
 * @param <B>
 */
public interface IBusiness<B extends IBaseVO<? extends Serializable>> extends Serializable {
	
	
	<V extends IBaseVO<? extends Serializable>> V complementBean(V valueObject, String... string);

	
	/**
	 * Deleta instancia de baseVO da base de dados
	 * 
	 * @param baseVO
	 * @throws JazzBusinessException
	 */
	void delete(B baseVO) throws JazzBusinessException;
	
	/**
	 * Checa se existe um VO persistido com a chave primária contida no bean informado.
	 * 
	 * @param bean
	 * @return
	 */
	boolean existsByBean(B bean);
	
	/**
	 * Checa se existe um VO persistido com a chave primária informada.
	 * 
	 * @param pk
	 * @return
	 */
	boolean exists(Serializable pk);
	
	/**
	 * Retorna todas as instancias de baseVO persistidas
	 * 
	 * @return
	 */
	List<B> findAll();
	
	
	/**
	 * Traz todos os registros que nao estejam contidos na lista informada
	 * @param notIn
	 * @param fetchProfiles
	 * @return
	 */
	List<B> findAllNotIn(List<B> notIn, String... fetchProfiles);
	
	/**
	 * Procura por texto livre
	 * @param textSearch
	 * @return
	 */
	List<B> findByTextSearch(String textSearch);
	
	
	/**
	 * Retorna, de forma paginada e/ou ordenada, todas as instancias de baseVO persistidas
	 * 
	 * @param firstResult
	 * @param maxResult
	 * @param map
	 * @return
	 */
	List<B> findAll(ExtraArgumentsDTO extraArgumentsDTO);
	
	/**
	 * Localiza baseVO a partir da chave primária contida no bean informado.
	 * 
	 * @param bean
	 * @return
	 * @throws JazzBusinessException
	 */
	B findByBeanPK(B bean);

	
	/**
	 * Localiza baseVO a partir da chave primária contida no bean informado.
	 * 
	 * @param bean
	 * @return
	 * @throws JazzBusinessException
	 */
	List<B> findBeansByBeans(Collection<B> beans, String... fetchProfiles);

	/**
	 * Localiza baseVO a partir de sua chave primária
	 * 
	 * @param pk
	 * @return
	 * @throws JazzBusinessException
	 */
	B findByPK(Serializable pk);
	
	/**
	 * Cria nova instância de IBaseVO pronta para ser preenchida e persistida
	 * 
	 * @return
	 */
	B newVO();
	
	/**
	 * Inclui nova instância de IBaseVO
	 * 
	 * @param baseVO
	 * @return
	 * @throws JazzBusinessException
	 */
	B saveOrUpdate(B baseVO) throws JazzBusinessException;
	
	/**
	 * Atualiza nova instância de baseVO
	 * 
	 * @param baseVO
	 * @return
	 * @throws JazzBusinessException
	 */
	B update(B baseVO) throws JazzBusinessException;


	public abstract void updateValidations(final B baseVO) throws JazzBusinessException;


	public abstract void includeValidations(final B baseVO) throws JazzBusinessException;


	public abstract void includeUpdateValidations(final B baseVO) throws JazzBusinessException;


}
