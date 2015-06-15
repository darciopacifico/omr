<@gc.comentarioArquivoJava/>
${domain.package};

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

/**
 * <@gc.comentarioClasse/>
 *
 * Implementação de DAO para o componente ${domain.name}
 *
 **/
@Component
<@gc.classSignature type="class" implements='${gc.getComponentName("DAO")}' /> {

	private static final long serialVersionUID = 4188683927282245182L;

<@gc.isCriarFind><#--testa se há atributos pesquisáveis-->
	<@gc.comentariosFind/>
	public <@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>){<#--cria metodo com os atributos pesquisaveis do VO-->
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		<@criteriaRestrictions />
		
		return findByCriteria(criteria);
		
	}
	
	<@gc.comentariosFind/>
	public Long <@gc.metodoCount/>(<@gc.argumentosFind/>){<#--cria metodo com os atributos pesquisaveis do VO-->
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		<@criteriaRestrictions />
		
		criteria.setProjection(Projections.rowCount() );
		
		List list = findByCriteria(criteria);
		
		return (Long) list.get(0);
		
	}

	<@gc.comentariosFind>
	 * @param QueryOrder Ordenação de pesquisa
	</@gc.comentariosFind>
	public <@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>, ExtraArgumentsDTO extraArgumentsDTO){<#--cria metodo com os atributos pesquisaveis do VO mais ordenação-->
	
		DetachedCriteria criteria = createDetachedCriteria();
		
		<@criteriaRestrictions />
		
		order(criteria, extraArgumentsDTO);
		return findByCriteria(criteria, extraArgumentsDTO);

	}
</@gc.isCriarFind>
}

<#-- printa as restricoes de um critera, de acordo com o tipo de comparador a ser utilizado (LIKE, EQUALS, RANGE)-->
<#macro criteriaRestrictions criteria="criteria", javaClass=domain >
	<@gc.iterateSearchableProperties "", javaClass, false; prop><#t>
	 <#if gc.hasTagSC(prop, "JazzProp", "comparison", "EQUALS") >
	 	eqRestriction(${prop.name}, ${criteria}, "${prop.name}");
		<#elseif gc.hasTagSC(prop, "JazzProp", "comparison", "LIKE")>
		ilikeRestriction(${prop.name}, ${criteria}, "${prop.name}");
		<#elseif gc.hasTagSC(prop, "JazzProp", "comparison", "RANGE")>
		rangeRestriction(${prop.name}From, ${prop.name}To, ${criteria}, "${prop.name}");
		<#else>
		//comparador de ${javaClass}.${prop.name} não reconhecido (). Verifique a tag @JazzProp(comparison="????") nesta propriedade! 
		</#if>
	</@gc.iterateSearchableProperties><#t>
</#macro>