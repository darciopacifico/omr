/**
 * 
 */
package br.com.dlp.jazzomr.base;

import br.com.dlp.jazzomr.empresa.EmpresaVO;

/**
 * Contrato para diferenciar vos que devem pertencer a uma organização
 * @author darcio
 */
public interface IOrganizationRestrictVO {

	public abstract void setEmpresaVO(EmpresaVO empresaVO);

	public abstract EmpresaVO getEmpresaVO();


	
	
	
}
