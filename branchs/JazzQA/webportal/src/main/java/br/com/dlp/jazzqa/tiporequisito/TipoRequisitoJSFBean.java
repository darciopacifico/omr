/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente TipoRequisitoVO
 *
 **/
@Scope(value="session")
@Component
public class TipoRequisitoJSFBean extends AbstractPaginatedJSFBeanImpl<TipoRequisitoVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
	
	@Autowired
	private TipoRequisitoBusiness tipoRequisitoBusiness;
	
	@Autowired
	private DummyBusiness dummyBusiness;
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	
	private Date dtInclusaoFrom;
	private Date dtInclusaoTo;
	private String descricao;
	private String nome;
	private DummyVO dummyVO;
	
	/**
	 * Listagen para exibição do POPUP de relacionamento de dummyVO.
	 */
	private List<DummyVO> dummyVOs = new ArrayList<DummyVO>(0);
	
	
	
	/**
	 * Retorna todos os objetos dummy para renderização do popup
	 * @return
	 */
	public String pesquisaDummyVOs(){
		List<DummyVO> dummyVOs = dummyBusiness.findAll();
		this.dummyVOs = dummyVOs;
		return null;
	}
	
	
	
	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo.
	 * Pesquisa e retorna lista de TipoRequisitoVO com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<TipoRequisitoVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
		
		List<TipoRequisitoVO> resultados = tipoRequisitoBusiness.findTipoRequisitoVO(
				dtInclusaoFrom, dtInclusaoTo, descricao, nome, dummyVO, extraArgumentsDTO);
		
		return resultados;
	}
	
	/**
	 * Implementação de contagem de registros específica para este módulo.
	 * Este valor será cacheado até que o método invalidateRowCountCache() seja acionado.
	 * Na solução de design proposta o cache de rowCount é válido até que os argumentos de pesquisa sejam modificados.
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl#rowCount()
	 */
	@Override
	protected Long rowCount() {
		return tipoRequisitoBusiness.countTipoRequisitoVO(
				dtInclusaoFrom, dtInclusaoTo, descricao, nome, dummyVO);	}
	
	
	/**
	 * Accessor Method
	 * @return dtInclusaoFrom
	 */
	public Date getDtInclusaoFrom(){
		return dtInclusaoFrom;
	}
	
	/**
	 * Accessor Method
	 * @return dtInclusaoTo
	 */
	public Date getDtInclusaoTo(){
		return dtInclusaoTo;
	}
	
	/**
	 * Accessor Method
	 * @return descricao
	 */
	public String getDescricao(){
		return descricao;
	}
	
	/**
	 * Accessor Method
	 * @return nome
	 */
	public String getNome(){
		return nome;
	}
	
	/**
	 * Accessor Method
	 * @return dummyVO
	 */
	public DummyVO getDummyVO(){
		return dummyVO;
	}
	
	
	
	/**
	 * Mutator Method
	 * @param dtInclusaoFrom
	 */
	public void setDtInclusaoFrom(Date dtInclusaoFrom){
		this.dtInclusaoFrom = dtInclusaoFrom;
	}
	
	/**
	 * Mutator Method
	 * @param dtInclusaoTo
	 */
	public void setDtInclusaoTo(Date dtInclusaoTo){
		this.dtInclusaoTo = dtInclusaoTo;
	}
	
	/**
	 * Mutator Method
	 * @param descricao
	 */
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}
	
	/**
	 * Mutator Method
	 * @param nome
	 */
	public void setNome(String nome){
		this.nome = nome;
	}
	
	/**
	 * Mutator Method
	 * @param dummyVO
	 */
	public void setDummyVO(DummyVO dummyVO){
		this.dummyVO = dummyVO;
	}
	
	
	
	/**
	 * Recupera listagem para montagem de tela
	 * @return
	 */
	public List<DummyVO> getDummyVOs() {
		return dummyVOs;
	}
	
	/**
	 * Atribui listagem para montagem de tela
	 * @param dummyVOs
	 */
	public void setDummyVOs(List<DummyVO> dummyVOs) {
		this.dummyVOs = dummyVOs;
	}
	
	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[]{
				"dtInclusaoFrom", "dtInclusaoTo", "descricao", "nome", "dummyVO"};	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<TipoRequisitoVO> getBusiness() {
		return tipoRequisitoBusiness;
	}
}