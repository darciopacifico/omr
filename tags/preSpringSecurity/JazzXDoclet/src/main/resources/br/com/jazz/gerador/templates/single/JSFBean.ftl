<@gc.comentarioArquivoJava/>
${domain.package};

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl;

/**
 * <@gc.comentarioClasse/>
 *
 * Implementação de Bean JSF para o componente ${domain.name}
 *
 **/
@Scope(value="session")
@Component
<@gc.classSignature type="class" /> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private <@gc.componentName "Business" /> <@gc.componentVarName "Business" />;

	<@gc.relacionamentoOne ; p, sufix><#-- QUALQUER DESIGN DE SOLUCAO PARA RELACIONAMENTOS "UM" PRECISA DE ACESSO AO BUSINESS DO MODULO -->
	@Autowired
	private <@gc.componentName "Business" p.type.javaClass /> <@gc.componentVarName "Business" p.type.javaClass />;
	</@gc.relacionamentoOne>

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	<@gc.propPesquisaveis/>	 

	<@gc.relacionamentoOne ; p, sufix>
		<#if gc.getAnnotationParam(p,"JazzProp","renderer").designSolucao=="LISTAGEM_SESSAO" ||
		     gc.getAnnotationParam(p,"JazzProp","renderer").designSolucao=="PESQUISA_DINAMICA"><#t><#--para ambos eh necessario atributo para guardar os resultados das pesquisa-->
	/**
	 * Listagen para exibição do ${gc.getRendererProperty(p)} de relacionamento de ${p.name}.   
	 */
	private List<${gc.genericReturnType(p)}> ${p.name}${sufix}s = new ArrayList<${gc.genericReturnType(p)}>(0);
		</#if><#t>
	</@gc.relacionamentoOne>

	
	<@gc.relacionamentoOne domain, false; p, sufix><#-- LISTAGEM_SESSAO, PESQUISA_DINAMICA, INPLACE; -->
		<#if gc.getAnnotationParam(p,"JazzProp","renderer").designSolucao=="LISTAGEM_SESSAO"><#t>

	/**
	 * Retorna todos os objetos dummy para renderização do popup
	 * @return
	 */
	public String pesquisa${gc.genericReturnType(p)}s(){
		List<${gc.genericReturnType(p)}> ${p.name}s = <@gc.componentVarName "Business" p.type.javaClass />.findAll();
		this.${p.name}s = ${p.name}s;
		return null;
	}
		</#if><#t>
		
		<#if gc.getAnnotationParam(p,"JazzProp","renderer").designSolucao=="PESQUISA_DINAMICA"><#t>

	/**
	 * Retorna todos os objetos dummy para renderização do popup
	 * @return
	 */
	public String pesquisa${gc.genericReturnType(p)}s(String textSearch){
		List<${gc.genericReturnType(p)}> ${p.name}s = <@gc.componentVarName "Business" p.type.javaClass />.findByTextSearch(textSearch);
		this.${p.name}s = ${p.name}s;
		return null;
	}
		</#if><#t>
	</@gc.relacionamentoOne>
	

	<@gc.isCriarFind domain><#t>
	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo.
	 * Pesquisa e retorna lista de ${domain.name} com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<${domain.name}> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
	
		List<${domain.name}> resultados = <@gc.componentVarName "Business" />.<@gc.metodoFind/>(<#rt>
		<@gc.iterateSearchableProperties "," ; p, prefix>${p.name}${prefix}</@gc.iterateSearchableProperties>, extraArgumentsDTO<#t>);
		
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
		return <@gc.componentVarName "Business" />.<@gc.metodoCount/>(<#rt>
		<@gc.iterateSearchableProperties "," ; p, prefix>${p.name}${prefix}</@gc.iterateSearchableProperties><#t>);
	}
	</@gc.isCriarFind><#t>
	
	<#-- PRINTA METODOS ACESSORES PARA REALIZACAO DO FORMULARIO DE PESQUISA -->
	<@gc.propPesquisaveisAccessors/>	 
	
	<#-- PRINTA METODOS MODIFICADORES PARA REALIZACAO DO FORMULARIO DE PESQUISA -->
	<@gc.propPesquisaveisMutators/>	 


	<@gc.relacionamentoOne ; p, sufix>
		<#if gc.getAnnotationParam(p,"JazzProp","renderer").designSolucao=="LISTAGEM_SESSAO" ||
		     gc.getAnnotationParam(p,"JazzProp","renderer").designSolucao=="PESQUISA_DINAMICA"><#t><#--para ambos eh necessario getter e setter-->
	/**
	 * Recupera listagem para montagem de tela
	 * @return
	 */
	public List<${gc.genericReturnType(p)}> get${gc.genericReturnType(p)}s() {
		return ${p.name}${sufix}s;
	}
	
	/**
	 * Atribui listagem para montagem de tela
	 * @param dummyVOs
	 */
	public void set${gc.genericReturnType(p)}s(List<${gc.genericReturnType(p)}> ${p.name}${sufix}s) {
		this.${p.name}${sufix}s = ${p.name}${sufix}s;
	}
		</#if><#t>
	</@gc.relacionamentoOne>

	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[]{<#t><@gc.iterateSearchableProperties "," ; p, prefix>"${p.name}${prefix}"</@gc.iterateSearchableProperties>};
	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<${domain.name}> getBusiness() {
		return this.<@gc.componentVarName "Business" />;
	}
}