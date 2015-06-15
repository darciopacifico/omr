<@gc.comentarioArquivoJava/>
${domain.package};

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;

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

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	<@gc.propPesquisaveis/>	 

	<@gc.isCriarFind domain><#t>
	/**
	 * Pesquisa lista de ${domain.name} e disponibiliza resultados em ${domain.name?uncap_first}s 
	 */
	public String actionPesquisar(){
	
		List<${domain.name}> resultados = <@gc.componentVarName "Business" />.<@gc.metodoFind/>(<#rt>
		<@gc.iterateSearchableProperties "," ; p, prefix>${p.name}${prefix}</@gc.iterateSearchableProperties><#t>);

		setResultados(resultados);

		return "exibePesquisa";
	}
	</@gc.isCriarFind><#t>

	<#-- PRINTA METODOS ACESSORES PARA REALIZACAO DO FORMULARIO DE PESQUISA -->
	<@gc.propPesquisaveisAccessors/>	 
	<#-- PRINTA METODOS MODIFICADORES PARA REALIZACAO DO FORMULARIO DE PESQUISA -->
	<@gc.propPesquisaveisMutators/>	 

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<${domain.name}> getBusiness() {
		return this.<@gc.componentVarName "Business" />;
	}
}