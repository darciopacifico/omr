package br.com.dlp.framework.business;

import java.io.Serializable;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import br.com.dlp.framework.exception.BaseBusinessException;
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
	
	/**
	 * Deleta instancia de baseVO da base de dados
	 * 
	 * @param baseVO
	 * @throws BaseBusinessException
	 */
	@WebMethod(exclude = true)
	void delete(B baseVO) throws BaseBusinessException;
	
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
	 * Retorna, de forma paginada, todas as instancias de baseVO persistidas
	 * 
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	@WebMethod(exclude = true)
	List<B> findAllPaginated(Integer firstResult, Integer maxResult);
	
	/**
	 * Localiza baseVO a partir da chave primária contida no bean informado.
	 * 
	 * @param bean
	 * @return
	 * @throws BaseBusinessException
	 */
	@WebMethod(exclude = true)
	B findByBeanPK(B bean) throws BaseBusinessException;
	
	/**
	 * Localiza baseVO a partir de sua chave primária
	 * 
	 * @param pk
	 * @return
	 * @throws BaseBusinessException
	 */
	@WebMethod(exclude = true)
	B findByPK(Serializable pk) throws BaseBusinessException;
	
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
	 * @throws BaseBusinessException
	 */
	@WebMethod(exclude = true)
	B saveOrUpdate(B baseVO) throws BaseBusinessException;
	
	/**
	 * Atualiza nova instância de baseVO
	 * 
	 * @param baseVO
	 * @return
	 * @throws BaseBusinessException
	 */
	@WebMethod(exclude = true)
	B update(B baseVO) throws BaseBusinessException;
	
}
