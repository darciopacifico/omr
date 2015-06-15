<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>


<html>
<head>
<link href="css/diPepsico.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="0" rightmargin="0" topmargin="0">
<f:view>
    <h:form>
    	<!--  regiao do cabecalho do sistema -->
    	<rich:toolBar styleClass="toolBarHeader" width="100%">
    		<rich:toolBarGroup location="left">
    			<h:graphicImage url="img/bg/bg_logoHeader.png" height="27" ></h:graphicImage>
    		</rich:toolBarGroup>
    		<rich:toolBarGroup>
    			<h:outputText value="DI-PEPSICO" style="font-size:18px;font-weight:bold;"></h:outputText>
    		</rich:toolBarGroup>
    		<rich:toolBarGroup location="right">
    			<h:commandButton title="Página Principal"
    			image="img/bt/bt_home.png" 
    			action="javascript:window.open('viewPrincipal.jsf,_self');"></h:commandButton>
    			
    			
    			<h:commandLink title="Página Principal" styleClass="fontLinkHeader" action="javascript:window.open('viewPrincipal.jsf,_self');">Página Principal</h:commandLink>
    			
    			<h:commandButton title="Logout" image="img/bt/bt_logout.png" action="#{loginMBean.logout}"></h:commandButton>
    			<h:outputText title="Nome do usuário" styleClass="fontLinkHeader" value="#{nfeMBean.nmUsuario}"></h:outputText>
    			<h:commandLink title="Logout" styleClass="fontLinkHeader" action="#{loginMBean.logout}">[Logout]</h:commandLink>
    		</rich:toolBarGroup>
    	</rich:toolBar>
    	<!--  fim do cabecalho do sistema -->
    	
    	<table width="100%">
    	<tr><td align="center">
    	<rich:tabPanel width="99%" style="text-align:center" >
    	<rich:tab label="Tela Principal">
	    	<rich:toolBar>
	    		<rich:toolBarGroup>
	    			<a4j:commandButton title="Reenviar" image="img/bt/bt_reenviar.png" style="cursor:pointer;" action="#{nfeMBean.reenviar}"  reRender="errorMessage,nfeList,sc2" />
	    		</rich:toolBarGroup>
	    		<rich:toolBarGroup location="right">
		    		<h:outputText styleClass="fontToolBar" value="Empresas: "></h:outputText>
		    		<h:selectOneMenu value="#{nfeMBean.nmEmpresa}" styleClass="inputTypes" style="width:200px;">
		    			<f:selectItems value="#{nfeMBean.selectEmpresa}"></f:selectItems>
		    		</h:selectOneMenu>
		    		<h:outputText styleClass="fontToolBar" value=" Data Inicial: "></h:outputText>
		    		<rich:calendar value="#{nfeMBean.dataInicial}"
                        locale="pt/BR"
                        popup="true"
                        datePattern="dd/MM/yyyy"
                        showApplyButton="false" 
                        cellWidth="24px" cellHeight="22px" style="width:200px;" inputStyle="inputTypes"/>
                        
		    		<h:outputText styleClass="fontToolBar" value=" Data Final: "></h:outputText>
		    		<rich:calendar value="#{nfeMBean.dataFinal}"
                        locale="pt/BR"
                        popup="true"
                        datePattern="dd/MM/yyyy"
                        showApplyButton="false" 
                        cellWidth="24px" cellHeight="22px"  style="width:200px;"  inputStyle="inputTypes"/>
		    		
		    		<a4j:commandButton title="Pesquisar" image="img/bt/bt_pesquisar.png" style="cursor:pointer;" action="#{nfeMBean.pesquisar}" reRender="errorMessage,nfeList,sc2" />
	    		</rich:toolBarGroup>
	    	</rich:toolBar>
	        <rich:dataTable 
	        	id="nfeList" width="100%" rows="10" 
	        	value="#{nfeMBean.all}" var="nfe"
	        	headerClass="grayHead" styleClass="tableClass"
	        	cellpadding="0" cellspacing="0"
	        	rowClasses="linhaTabela"
	        	columnClasses="colunaTabela"
	        	onRowMouseOver="this.style.backgroundColor='#e1e1e1'"
	        	onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
	        	>
	            <f:facet name="header" >
	                <rich:columnGroup >
	                    <rich:column width="5%">
	                        <h:outputText styleClass="fontHeaderTable" value=" " />
	                    </rich:column>
	                    <rich:column width="19%">
	                        <h:outputText styleClass="fontHeaderTable" value="Número Nota" />
	                    </rich:column>
	                    <rich:column width="19%">
	                        <h:outputText styleClass="fontHeaderTable" value="Número Série" />
	                    </rich:column>
	                    <rich:column width="19%">
	                        <h:outputText styleClass="fontHeaderTable" value="Emissão" />
	                    </rich:column>
	                    <rich:column width="19%">
	                        <h:outputText styleClass="fontHeaderTable" value="Valor" />
	                    </rich:column>
	                    <rich:column width="19%">
	                        <h:outputText styleClass="fontHeaderTable" value="Quantidade de Itens" />
	                    </rich:column>
	                </rich:columnGroup>
	            </f:facet>
	            	<rich:column>
	            		<h:selectBooleanCheckbox value="#{nfeMBean.idsNF[nfe.notaFiscalEletronicaPK.idNf]}"></h:selectBooleanCheckbox>
		            </rich:column>
	            <rich:column >
	            	<h:commandLink styleClass="fontContentTable" action="#{itemMBean.chegada}" 
	            		value="#{nfe.nrDocNf}">
	            		<f:param  name="idNF" value="#{nfe.notaFiscalEletronicaPK.idNf}"/>
	            		<f:param  name="idEmpresa" value="#{nfe.notaFiscalEletronicaPK.idEmpresa}"/> 
	            		<f:param  name="nrNota" value="#{nfe.nrDocNf}"/> 
	            	</h:commandLink>
	            </rich:column>
	            <rich:column>
	            	<h:commandLink styleClass="fontContentTable" action="#{itemMBean.chegada}" 
	            		value="#{nfe.serie}">
	            		<f:param  name="idNF" value="#{nfe.notaFiscalEletronicaPK.idNf}"/>
	            		<f:param  name="idEmpresa" value="#{nfe.notaFiscalEletronicaPK.idEmpresa}"/> 
	            		<f:param  name="nrNota" value="#{nfe.nrDocNf}"/> 
	            	</h:commandLink>
	            </rich:column>
	            <rich:column>
	           		<h:commandLink styleClass="fontContentTable" action="#{itemMBean.chegada}" 
	            		value="#{nfe.emissaoFormat}">
	            		<f:param  name="idNF" value="#{nfe.notaFiscalEletronicaPK.idNf}"/>
	            		<f:param  name="idEmpresa" value="#{nfe.notaFiscalEletronicaPK.idEmpresa}"/> 
	            		<f:param  name="nrNota" value="#{nfe.nrDocNf}"/> 
	            	</h:commandLink>
	            </rich:column>
	            <rich:column>
	            	<h:commandLink styleClass="fontContentTable" action="#{itemMBean.chegada}" 
	            		value="#{nfe.valorFormat}">
	            		<f:param  name="idNF" value="#{nfe.notaFiscalEletronicaPK.idNf}"/>
	            		<f:param  name="idEmpresa" value="#{nfe.notaFiscalEletronicaPK.idEmpresa}"/> 
	            		<f:param  name="nrNota" value="#{nfe.nrDocNf}"/> 
	            	</h:commandLink>
	            </rich:column>
	            <rich:column>
					<h:commandLink styleClass="fontContentTable" action="#{itemMBean.chegada}" 
	            		value="#{nfe.gtdItens}">
	            		<f:param  name="idNF" value="#{nfe.notaFiscalEletronicaPK.idNf}"/>
	            		<f:param  name="idEmpresa" value="#{nfe.notaFiscalEletronicaPK.idEmpresa}"/> 
	            		<f:param  name="nrNota" value="#{nfe.nrDocNf}"/> 
	            	</h:commandLink>
	            </rich:column>
	
	        </rich:dataTable>
	        <rich:datascroller align="center" for="nfeList" maxPages="50" id="sc2"/>
	        
	         <rich:spacer height="5"/>
	         <span class="sucessMessage">
	          <a4j:status id="commonstatus"  startText="Processando informação, aguarde..." styleClass="sucessMessage" stopText=""/>
	         </span>
	         <br/>
	         <h:outputText id="errorMessage" value="#{nfeMBean.errorMessage}" styleClass="#{nfeMBean.styleMessage}"></h:outputText>
	        
	        
        </rich:tab>
        </rich:tabPanel>
        </td></tr></table>
    
    
    <!-- regiao do rodape do sistema -->
     <rich:spacer height="5"/>
    <table width="100%" height="25px" cellpadding="0" cellspacing="0" border="0">
    	<tr>
    		<td bgcolor="#5e96c5" align="left">
    			<img src="img/bg/bg_rodape1.png" />
    		</td>
    		<td bgcolor="#5e96c5" align="right">
    			<img src="img/bg/bg_rodape2.png" />
    		</td>
    	</tr>
    </table>
    <!-- fim do rodape do sistema -->
    
    </h:form>
</f:view>

</body>

</html>