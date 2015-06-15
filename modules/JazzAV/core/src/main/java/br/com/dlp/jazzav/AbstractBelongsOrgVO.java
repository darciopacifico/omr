/**
 * 
 */
package br.com.dlp.jazzav;

import java.io.Serializable;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * VO abstrato para especialização de outros VOs que devem, obrigatoriamente, pertencer a uma empresa.
 * @author darcio
 */
@MappedSuperclass
public abstract class AbstractBelongsOrgVO<P extends Serializable>  extends AbstractLogEntityVO<P> implements IOrganizationRestrictVO {

	private static final long serialVersionUID = -6955634507456966507L;
	
	/*
	@Expose(deserialize=false,serialize=false)
	private EmpresaVO empresaVO;
	*/
	
	public AbstractBelongsOrgVO() {
	}

	public AbstractBelongsOrgVO(P pk) {
		super(pk);
	}

	
	/*
	@NotNull(message="O registro não foi associado a uma empresa")
	@ManyToOne
	public EmpresaVO getEmpresaVO() {
		return empresaVO;
	}

	public void setEmpresaVO(EmpresaVO empresaVO) {
		this.empresaVO = empresaVO;
	}
	*/
}
