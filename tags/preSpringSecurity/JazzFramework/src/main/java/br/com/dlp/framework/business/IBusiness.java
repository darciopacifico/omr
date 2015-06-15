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
@WebService
public interface IBusiness<B extends IBaseVO<? extends Serializable>> extends Serializable {
	
	
	<V extends IBaseVO<? extends Serializable>> V complementBean(V valueObject, String... string);

	
	/**
	 * Deleta instancia de baseVO da base de dados
	 * 
	 * @param baseVO
	 * @throws JazzBusinessException
	 */
	@WebMethod(exclude = true)
	void delete(B baseVO) throws JazzBusinessException;
	
	/**
	 * Checa se existe um VO persistido com a chave primária contida no bean informado.
	 * 
	 * @param bean
	 * @return
	 */
	@WebMethod(exclude = true)
	boolean existsByBean(B bean);
	
	/**
	 * Checa se existe um VO persistido com a chave primária informada.
	 * 
	 * @param pk
	 * @return
	 */
	@WebMethod(exclude = true)
	boolean exists(Serializable pk);
	
	/**
	 * Retorna todas as instancias de baseVO persistidas
	 * 
	 * @return
	 */
	@WebMethod(exclude = true)
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
	@WebMethod(exclude = true)
	List<B> findByTextSearch(String textSearch);
	
	
	/**
	 * Retorna, de forma paginada e/ou ordenada, todas as instancias de baseVO persistidas
	 * 
	 * @param firstResult
	 * @param maxResult
	 * @param map
	 * @return
	 */
	@WebMethod(exclude = true)
	List<B> findAll(ExtraArgumentsDTO extraArgumentsDTO);
	
	/**
	 * Localiza baseVO a partir da chave primária contida no bean informado.
	 * 
	 * @param bean
	 * @return
	 * @throws JazzBusinessException
	 */
	@WebMethod(exclude = true)
	B findByBeanPK(B bean);

	
	/**
	 * Localiza baseVO a partir da chave primária contida no bean informado.
	 * 
	 * @param bean
	 * @return
	 * @throws JazzBusinessException
	 */
	@WebMethod(exclude = true)
	List<B> findBeansByBeans(Collection<B> beans, String... fetchProfiles);

	/**
	 * Localiza baseVO a partir de sua chave primária
	 * 
	 * @param pk
	 * @return
	 * @throws JazzBusinessException
	 */
	@WebMethod(exclude = true)
	B findByPK(Serializable pk);
	
	/**
	 * Cria nova instância de IBaseVO pronta para ser preenchida e persistida
	 * 
	 * @return
	 */
	@WebMethod(exclude = true)
	B newVO();
	
	/**
	 * Inclui nova instância de IBaseVO
	 * 
	 * @param baseVO
	 * @return
	 * @throws JazzBusinessException
	 */
	@WebMethod(exclude = true)
	B saveOrUpdate(B baseVO) throws JazzBusinessException;
	
	/**
	 * Atualiza nova instância de baseVO
	 * 
	 * @param baseVO
	 * @return
	 * @throws JazzBusinessException
	 */
	@WebMethod(exclude = true)
	B update(B baseVO) throws JazzBusinessException;


}
