package br.com.dlp.jazzav.exam;

import java.io.Serializable;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Especializacao da interface de daos para criação da operacao findByPK com fetchProfile
 * @author darcio
 *
 * @param <B>
 */ 
public interface IJazzOMRDAO <B extends IBaseVO<? extends Serializable>> extends IDAO<B>{
	
 
	/**
	 * findByPK com uso de fetchProfile
	 * @param bean 
	 * @param fetchProfileName
	 * @return
	 */
	B findByPK(B bean, String... fetchProfileName);
	
}