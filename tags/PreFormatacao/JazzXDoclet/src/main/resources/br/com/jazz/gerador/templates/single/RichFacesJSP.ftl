<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" 
	xmlns:ui="http://java.sun.com/jsf/facelets" 
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core" 
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax"
	xmlns:t="http://myfaces.apache.org/tomahawk" 
	xmlns:rich="http://richfaces.org/rich" 
	xmlns:c="http://java.sun.com/jstl/core">
	
<head>
		<!-- revisar estilo e layout de paginas-->
    <style>
        img {
            border-width: 0;
        }
    </style>
</head>

<f:loadBundle var="msg" basename="br.com.dlp.jazzqa.ResourceBundle" />
<f:loadBundle var="lbl<@gc.componentName "DomainBundle"/>" basename="${domain.package.name}.<@gc.componentName "DomainBundle"/>" />

<f:view>
	<head>
			<title><h:outputText value="<@lblTitulo/>"/></title>
	</head>
	<body>

	<h:form>
		<rich:panel>

			<f:facet name="header">
				<h:outputText value="<@lblTitulo/>"/>
			</f:facet>



			<!--BARRA DE BOTOES -->
			<rich:toolBar height="26" itemSeparator="grid">
				<rich:toolBarGroup>

					<a4j:commandLink action="<@gc.beanElement "actionNovo"/>"   title="<@gc.brc "msg.btnNovo"/>"   reRender="campos">
						<h:graphicImage value="../img/create_doc.gif"/>
					</a4j:commandLink>

					<a4j:commandLink action="<@gc.beanElement "actionSalvar"/>" title="<@gc.brc "msg.btnSalvar"/>" reRender="campos,resultadoDG" >
						<h:graphicImage value="../img/save.gif" />
					</a4j:commandLink>

					<a4j:commandLink action="<@gc.beanElement "actionPesquisar"/>" title="<@gc.brc "msg.btnPesquisar"/>" reRender="resultadoDG" >
						<h:graphicImage value="../img/find.gif"  />
					</a4j:commandLink>

				</rich:toolBarGroup>    
			</rich:toolBar>

			<rich:spacer height="30px;" />

			<!--TABELA DE RESULTADOS-->
			<rich:dataTable value="<@gc.beanElement "resultados"/>" var="resultado" id="resultadoDG" >
				<f:facet name="header">Resultados Consulta</f:facet>
			<@gc.iterateFindArguments "", domain, false; p><#t>
				<rich:column>
					<f:facet name="header"><h:outputText value="<@lbl p/>"/></f:facet>
					<h:outputText value="<@gc.brc>resultado.${p.name}</@gc.brc>"/>
				</rich:column>
				
			</@gc.iterateFindArguments>

				<rich:column>
					<f:facet name="header">&nbsp;</f:facet>
					<a4j:commandButton action="<@gc.beanElement "actionUpdate">(resultado)</@gc.beanElement>" value="<@gc.brc "msg.btnAlterar"/>" reRender="campos" />
				</rich:column>
			
				<rich:column>
					<f:facet name="header">&nbsp;</f:facet>
					<a4j:commandButton action="<@gc.beanElement "actionDelete">(resultado)</@gc.beanElement>" value="<@gc.brc "msg.btnExcluir"/>" reRender="campos,resultadoDG" />
				</rich:column>
			
			</rich:dataTable>


			<!--CAMPOS MANUTENCAO-->
			<h:panelGrid columns="3" id="campos">
			<@gc.iterateFindArguments "", domain, false ; p><#t>
				<h:outputText value="<@lbl p/>"/>
				
				<#-- Chamar macro que renderiza componente para tipo do campo -->
				<h:inputText value="<@gc.beanElement "tmpVO." >${p.name}</@gc.beanElement>" id="${p.name}" />
				<rich:message for="${p.name}" />
				
			</@gc.iterateFindArguments>
			</h:panelGrid>

		</rich:panel>
	</h:form>
	</body>
</f:view>
</html>




<#-- Printa chamada ao message bundle para exibição de label internacionalizado -->
<#macro lbl propertie, javaClass=domain><#t>
	<@gc.brc>lbl<@gc.componentName "DomainBundle"/>.${propertie.name}</@gc.brc><#t>
</#macro>

<#-- Printa chave para o título do módulo -->
<#macro lblTitulo javaClass=domain><#t>
	<@gc.brc>lbl<@gc.componentName "DomainBundle", javaClass/>.pnl<@gc.componentName "DomainBundle", javaClass/></@gc.brc><#t>
</#macro>




