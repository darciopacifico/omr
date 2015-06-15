<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="br.com.dlp.framework.struts.action.BaseAction" %>
<%@page import="br.com.dlp.framework.popup.PopUpArgument" %>

<tr>
	<td class="tt1L" colspan="2" >
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<logic:iterate 
			id="argument" 
			name="<%=Constants.BEAN_KEY%>" 
			property="popUpController.arguments" 
			type="br.com.dlp.framework.popup.PopUpArgument">
			<tr>
				<td class="tt1L" width="20%" nowrap="nowrap">
				<logic:present name="argument" property="messageKey" >
					<b><bean:message key="<%=argument.getMessageKey()+""%>"/></b>
				</logic:present>
				<logic:notPresent name="argument" property="messageKey" >
					<b><bean:write name="argument" property="message"/></b>
				</logic:notPresent>
					
				</td>
				<td class="tt2l" >
				

				<%
					String onblur;
					String onkeypress;
					String onchange;
					
					String validacaoFormacacao = argument.getValidacaoFormacacao();
					
					if(validacaoFormacacao.equals(PopUpArgument.VALIDACAO_FORMATACAO_DATA)){
						onblur="validaData(this.value, 5);";
						onkeypress="formataData(this.name);";
						onchange="";
					
					}else if(validacaoFormacacao.equals(PopUpArgument.VALIDACAO_FORMATACAO_DECIMAL)){
						onblur="";
						onkeypress="onlyDec();";
						onchange="formataNum(this.name)";
					
					}else if(validacaoFormacacao.equals(PopUpArgument.VALIDACAO_FORMATACAO_INTEIRO)){
						onblur="";
						onkeypress="onlyNum();";
						onchange="";
					
					}else if(validacaoFormacacao.equals(PopUpArgument.VALIDACAO_FORMATACAO_STRING)){
						onblur="";
						onkeypress="";
						onchange="";
					
					}else{
						onblur="";
						onkeypress="";
						onchange="";
					
					}
					
				%>
				
				
				<logic:present name="argument" property="titleKey" >
					<html:text name="argument" styleClass="cc0" property="value" indexed="true" 
						titleKey="<%=argument.getTitleKey()+""%>" 
						onblur="<%=onblur%>" onkeypress="<%=onkeypress%>" onchange="<%=onchange%>" />
				</logic:present>
				<logic:notPresent name="argument" property="titleKey" >
					<html:text name="argument" styleClass="cc0" property="value" indexed="true" 
						title="<%=argument.getTitle()+""%>" 
						onblur="<%=onblur%>" onkeypress="<%=onkeypress%>" onchange="<%=onchange%>"/>
				</logic:notPresent>
				
				</td>
			</tr>
		</logic:iterate>
			<tr>
				<td class="tt1L" width="20%" nowrap="nowrap" colspan="2">
					<input type="button"
						class="bb0" 
						onclick="invocaServico('<%=BaseAction.SERVICE_PESQUISA_POPUP%>')"
						value="<bean:message key="pesquisa.padrao.btnpesquisar"/>">
				</td>
			</tr>
		</table >
	
			
		
	</td>
</tr>