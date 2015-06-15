/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.projeto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;
import br.com.dlp.jazzqa.projeto.enums.TipoProjetoEnum;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente ProjetoVO
 *
 **/
@Scope(value="session")
@Component
public class ProjetoJSFBean extends AbstractJSFBeanImpl<ProjetoVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
	
	@Autowired
	private ProjetoBusiness projetoBusiness;
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private TipoProjetoEnum tipoProjeto;
	private Date dtInc;
	private Date dtAlt;
	private StatusEnum status;
	
	/**
	 * Pesquisa lista de ProjetoVO e disponibiliza resultados em projetoVOs
	 */
	@Override
	public String actionPesquisar(){
		
		List<ProjetoVO> resultados = projetoBusiness.findProjetoVO(tipoProjeto, dtInc, dtAlt, status);
		setResultados(resultados);
		
		return "exibePesquisa";
	}
	
	
	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[]{"dtInclusaoFrom", "dtInclusaoTo", "descricao","nome","dummyVO"};
	}
	
	/**
	 * Accessor Method
	 * @return tipoProjeto
	 */
	public TipoProjetoEnum getTipoProjeto(){
		return tipoProjeto;
	}
	
	/**
	 * Accessor Method
	 * @return dtInc
	 */
	public Date getDtInc(){
		return dtInc;
	}
	
	/**
	 * Accessor Method
	 * @return dtAlt
	 */
	public Date getDtAlt(){
		return dtAlt;
	}
	
	/**
	 * Accessor Method
	 * @return status
	 */
	public StatusEnum getStatus(){
		return status;
	}
	
	/**
	 * Mutator Method
	 * @param tipoProjeto
	 */
	public void setTipoProjeto(TipoProjetoEnum tipoProjeto){
		this.tipoProjeto = tipoProjeto;
	}
	
	/**
	 * Mutator Method
	 * @param dtInc
	 */
	public void setDtInc(Date dtInc){
		this.dtInc = dtInc;
	}
	
	/**
	 * Mutator Method
	 * @param dtAlt
	 */
	public void setDtAlt(Date dtAlt){
		this.dtAlt = dtAlt;
	}
	
	/**
	 * Mutator Method
	 * @param status
	 */
	public void setStatus(StatusEnum status){
		this.status = status;
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<ProjetoVO> getBusiness() {
		return projetoBusiness;
	}
}