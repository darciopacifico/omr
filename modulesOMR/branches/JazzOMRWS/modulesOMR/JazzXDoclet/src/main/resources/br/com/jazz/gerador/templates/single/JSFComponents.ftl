<#--MACRO PARA RENDERIZAR OS COMPONENTES JSF DA TELA -->
<#macro renderJSFComponent p, suffix, value, javaClass=domain, regiao="">
	<#t><#assign render=gc.getRendererProperty(p)/><#t>
	<#switch render>
	<#case "TEXT">
				<h:inputText value="${value}" <#nested />/>
				<#break>
	<#case "TEXTAREA">
				<h:inputTextarea value="${value}"<#rt>
				<@directAttribute p, "JazzTextArea", "rows", "", javaClass/><#t>
				<@directAttribute p, "JazzTextArea", "cols", "", javaClass/><#lt> <#nested />/>
				<#break>
	<#case "CALENDAR">
				<rich:calendar value="${value}" <#nested />/>
				<#break>
	<#case "COMBO">
		<#break>
	<#case "GRID">
		<#break>
	<#case "MODAL">
		<#break>
	<#case "POPUP">
		<a4j:commandLink action="<@gc.brc>myMBean.pesquisa${gc.genericReturnType(p)}s()</@gc.brc>" id="${p.name}${suffix}PoUpControl${regiao}" 
			oncomplete="Richfaces.showModalPanel('${p.name}${suffix}PopUp')" reRender="${p.name}${suffix}PopUp"	ajaxSingle="true"  <#nested >>
			<h:outputText value="Escolha"  /><h:outputText value="${value}" />
		</a4j:commandLink>
	<#break>
	<#case "RADIO">
		<#break>
	<#case "SUBMODULE">
		<#break>
	<#case "CHECKBOX">
		<#break>
	<#case "DEFAULT">
		<h:inputText value="<@gc.brc>myMBean.tmpVO.${p.name}</@gc.brc>"/> 
		<#break>
	<#default>
		<h:inputText value="<@gc.brc>myMBean.tmpVO.${p.name}</@gc.brc>"/> 
		<#break>
	</#switch>
</#macro>

<#-- MACRO PARA MapeaR atributos das anotacoes diretamente para atributos tags jsf/jsp, utilizando o mesmo nome de atributo. 
Ex: atributo size de @JazzProp é utilizado em textInputs, mapeado diretamente.  -->
<#macro directAttribute prop, annot, attribute, defValue="", javaClass=domain>
	<#if gc.getAnnotationParam(prop,annot, attribute)?? && gc.getAnnotationParam(prop,annot, attribute)?string!=""><#t>
		${" "}${attribute}="${gc.getAnnotationParam(prop,annot, attribute)}" <#t>
	<#elseif defValue??><#t> 
		<#if defValue!=""><#t>
		${" "}${attribute}="${defValue}" <#t>
		</#if>
	</#if><#t>
</#macro>