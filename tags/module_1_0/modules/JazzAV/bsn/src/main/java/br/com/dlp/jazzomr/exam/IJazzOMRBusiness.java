package br.com.dlp.jazzomr.exam;

import java.io.Serializable;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzomr.empresa.EmpresaVO;

/**
 * Especializacao da interface IBusiness para implementacao de operacao findByPK com fetchProfile
 * @author darcio
 *
 * @param <B>
 */ 
public interface IJazzOMRBusiness<B extends IBaseVO<? extends Serializable>> extends IBusiness<B> {

	/**
	 * findByPK com uso de fetchProfile
	 * @param bean
	 * @param fetchProfileName
	 * @return
	 */
	B findByPK(B bean, String... fetchProfileName);
	
	
	/**
	 * Recupera a empresa da usuário logado momento
	 * @return
	 */
	EmpresaVO getEmpresaUsuarioLogado();

	
}