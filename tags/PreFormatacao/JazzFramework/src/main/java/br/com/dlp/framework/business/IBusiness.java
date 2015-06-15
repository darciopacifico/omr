package br.com.dlp.framework.business;

import java.io.Serializable;
import java.util.List;

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
public interface IBusiness<B extends IBaseVO<? extends Serializable>> extends Serializable {

	/**
	 * Cria nova instância de IBaseVO pronta para ser preenchida e persistida
	 * @return
	 */
	public B newVO();

	/**
	 * Inclui nova instância de IBaseVO
	 * @param baseVO
	 * @return
	 * @throws BaseBusinessException
	 */
	public B saveOrUpdate(B baseVO) throws BaseBusinessException;

	/**
	 * Atualiza nova instância de baseVO
	 * @param baseVO
	 * @return
	 * @throws BaseBusinessException
	 */
	public B update(B baseVO) throws BaseBusinessException;

	/**
	 * Deleta instancia de baseVO da base de dados
	 * @param baseVO
	 * @throws BaseBusinessException
	 */
	public void delete(B baseVO) throws BaseBusinessException;

	/**
	 * Retorna todas as instancias de baseVO persistidas
	 * @return 
	 */
	public List<B> findAll();

	/**
	 * Retorna, de forma paginada, todas as instancias de baseVO persistidas
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<B> findAll(Integer firstResult, Integer maxResult);

	/**
	 * Localiza baseVO a partir de sua chave primária
	 * @param pk
	 * @return
	 * @throws BaseBusinessException
	 */
	public B findByPK(Serializable pk) throws BaseBusinessException;

	/**
	 * Checa se existe um VO persistido com a chave primária informada.
	 * @param pk
	 * @return
	 */
	public boolean exists(Serializable pk);

	/**
	 * Localiza baseVO a partir da chave primária contida no bean informado. 
	 * @param Bean
	 * @return
	 * @throws BaseBusinessException
	 */
	public B findByPK(B Bean) throws BaseBusinessException;

	/**
	 * Checa se existe um VO persistido com a chave primária contida no bean informado.
	 * @param bean
	 * @return
	 */
	public boolean exists(B bean);

}
