<#import "JSFComponents.ftl" as rf><#t>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml" xmlns:rich="http://richfaces.org/rich" xmlns:a4j="http://richfaces.org/a4j"
	xmlns:ui="http://java.sun.com/jsf/facelets" xmlns:h="http://java.sun.com/jsf/html" xmlns:f="http://java.sun.com/jsf/core"
	xmlns:t="http://myfaces.apache.org/tomahawk" xmlns:c="http://java.sun.com/jstl/core" xmlns:p="http://primefaces.prime.com.tr/ui"
	template="../template/crud/crudTemplate.xhtml">



	<!-- PARAMETROS DO MODULO -->
	<ui:param name="myMBean" value="<@gc.brc>${gc.getComponentVarName('JSFBean')}</@gc.brc>" />
	<ui:param name="tituloModulo" value="<@gc.brc>lbl<@gc.componentName "DomainBundle"/>.pnl<@gc.componentName "DomainBundle"/></@gc.brc>" />

	<!-- DEFINICOES GLOBAIS DO MÓDULO -->
	<ui:define name="repGlobal">
		<f:loadBundle var="lbl<@gc.componentName "DomainBundle"/>" basename="${domain.package.name}.<@gc.componentName "DomainBundle"/>" />
		<f:loadBundle var="msg" basename="br.com.dlp.jazzomr.ResourceBundle" />
		
	</ui:define>


	<#--RENDERIZA FORMULARIO DE PESQUISA-->
	<@gc.isCriarFind domain>
		<!-- DEFINICAO DE PAINEL DE FORMULARIO DE PESQUISA PESQUISA -->
		<ui:define name="repPesquisa">
			<h:panelGrid columns="2">
			<@gc.iterateSearchableProperties "" ; p, suffix>
				<h:outputText value="<@lbl p,suffix/>"/>
				<@rf.renderJSFComponent p, suffix, gc.f_beanElement(p.name+suffix), domain, "Pesq" />
				
			</@gc.iterateSearchableProperties>
			</h:panelGrid>
		</ui:define>

	</@gc.isCriarFind>


	<#-- DEFINICAO DOS CAMPOS PARA PESQUISA INSTANTANEA -->
	<!-- DEFINICAO DOS CAMPOS PARA PESQUISA INSTANTANEA -->
	<ui:define name="repInstantSearch">
	<@gc.isCriarFind domain>
		<@gc.iterateSearchableProperties "" ; p, prefix>
			<#if gc.getBooleanParam(p,"JazzProp","instantSearch")><#t>
			
		<h:inputText value="<@gc.brc>myMBean.${p.name}</@gc.brc>" style="width:150px;">
			<a4j:support action="#{myMBean.invalidateRowCountCache}" ajaxSingle="true" reRender="resultadoDG, pnlPesquisa" event="onchange" />
			<rich:toolTip value="Pesquisa por <@lbl p/>" showDelay="500" />
		</h:inputText>

			</#if>
		</@gc.iterateSearchableProperties>
	</@gc.isCriarFind>
	</ui:define>


	<#--COLUNAS EXIBIDAS NA PESQUISA-->
	<!--COLUNAS EXIBIDAS NA PESQUISA-->
	<ui:define name="repCamposResultado">
	
		<@gc.iterateProperties "", domain, false; p, prefix><#t>

			<#if gc.getBooleanParam(p,"JazzProp","listable")><#t> <#-- propriedades que deve ser exibidas em lista de resultados -->
			<rich:column >
				<f:facet name="header">
					<#if gc.getBooleanParam(p,"JazzProp","sortable")><#t>
						<#-- COLUNA ORDENÁVEL, CABECALHO COM CONTROLE DE ORNDENAÇÃO -->
						<a4j:commandLink action="<@gc.brc>myMBean.dataModel.extraArgumentsDTO.processOrderChoice('${p.name}')</@gc.brc>"
							value="<@lbl p/> <@gc.brc>myMBean.dataModel.getOrderLabel('${p.name}')</@gc.brc>" reRender="resultadoDG" ajaxSingle="true">
							<rich:toolTip value="Ordenar resultados por <@lbl p/>" showDelay="1000" />
						</a4j:commandLink>
					<#else>
						<#-- COLUNA NÃO ORDENÁVEL, CABECALHO SIMPLES -->
						<h:outputText value="<@lbl p/>"/>
					</#if>
				</f:facet>
				<h:outputText value="<@gc.brc>resultado.${p.name}</@gc.brc>"/>
			</rich:column>
			</#if>
	
		</@gc.iterateProperties>

	</ui:define>


	<#--CAMPOS MANUTENCAO-->
	<!--CAMPOS MANUTENCAO-->
	<ui:define name="repCamposManutencao">
		<@gc.iterateProperties "", domain, false; p, prefix>
			<#if gc.hasTagSC(p, "JazzProp")>
				<h:outputText value="<@lbl p/>"/>
				<#if gc.getBooleanParam(p,"JazzProp","readOnly")><#t>
					<h:outputText value="${gc.f_beanElement("tmpVO."+p.name)}"/>
				<#else><#t>
					<@rf.renderJSFComponent p, "", gc.f_beanElement("tmpVO."+p.name) > disabled="<@gc.brc>myMBean.tmpVO==null</@gc.brc>" </@rf.renderJSFComponent>
				</#if><#t><rich:message for="${p.name}" />
			</#if>

		</@gc.iterateProperties>
	</ui:define>

	<#-- DEFINICOES DE POPOUPS -->
	<!-- DEFINICOES DE POPOUPS -->
	<ui:define name="repPopups">

		<@gc.iterateProperties ; p, suffix><#t>
			<#if gc.getAnnotationParam(p,"JazzProp","renderer")="POPUP"><#t>

				<h:outputText value="${gc.f_beanElement("tmpVO."+p.name)}"/>

				<rich:modalPanel id="${p.name}PopUp" width="500" height="300">
		
					<f:facet name="header">
						<h:panelGroup>
							<h:outputText value="Selecione um <@lbl p/>"/>
						</h:panelGroup>
					</f:facet>
		
					<f:facet name="controls">
						<h:panelGroup>
							<h:graphicImage value="/img/close.png" style="cursor:pointer" styleClass="hidelink" id="hidelink" alt="Fechar" />
							<rich:componentControl for="${p.name}PopUp" attachTo="hidelink" operation="hide" event="onclick" />
						</h:panelGroup>
					</f:facet>
		
					<rich:dataGrid columns="4" value="<@gc.brc>myMBean.${p.name}s</@gc.brc>" var="${p.name}Var">
						<a4j:commandLink action="<@gc.brc>myMBean.tmpVO.${p.mutator.name}(${p.name}Var)</@gc.brc>" reRender="${p.name}${suffix}PoUpControl" oncomplete="Richfaces.hideModalPanel('${p.name}PopUp')"
							ajaxSingle="true">
							<h:outputText value="<@gc.brc>${p.name}Var</@gc.brc>" />
						</a4j:commandLink>
					</rich:dataGrid>
					<h:outputText value="Valor atual <@gc.brc>myMBean.tmpVO.${p.name}</@gc.brc>." />
		
				</rich:modalPanel>

			</#if>
		</@gc.iterateProperties>

	</ui:define>

</ui:composition>

<#--
<title><h:outputText value="<@lblTitulo/>"/></title>
<a4j:commandLink action="<@gc.beanElement "actionNovo"/>"   title="<@gc.brc "msg.btnNovo"/>"   reRender="campos"/>
<rich:dataTable value="<@gc.beanElement "resultados"/>" var="resultado" id="resultadoDG" >
<a4j:commandButton action="<@gc.beanElement "actionUpdate(resultado)"/>" value="<@gc.brc "msg.btnAlterar"/>" reRender="campos" />
-->

<#-- Printa chamada ao message bundle para exibição de label internacionalizado -->
<#macro lbl propertie, prefix="", javaClass=domain><#t>
	<#t><@gc.brc>lbl<@gc.componentName "DomainBundle"/>.${propertie.name}${prefix}</@gc.brc><#rt>
</#macro>

<#-- Printa chave para o título do módulo -->
<#macro lblTitulo javaClass=domain><#t>
	<@gc.brc>lbl<@gc.componentName "DomainBundle", javaClass/>.pnl<@gc.componentName "DomainBundle", javaClass/></@gc.brc><#t>
</#macro>