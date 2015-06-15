<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="br.com.dlp.framework.struts.action.BaseAction" %>

<html>
<head>
  	<meta http-equiv="Page-Enter" content="blendTrans(Duration=0.1);charset=UTF-8">
	<title>
		PopUp
		<%--
		<bean:write name="baseForm" property="popUpController.tituloPopUp"/>
		--%>
	</title>
  	<script src='../js/funcoesDados.js'></script>
  	<script src='../js/funcoesData.js'></script>
  	<script src='../js/funcoesNavegacao.js'></script>
  	<script src='../js/funcoesNum.js'></script>
	<link rel="stylesheet" href="../css/new_base.css" type="text/css">
</head>


<body class="corpo" topmargin="2" leftmargin="2" >
<html:form action="<%=""+request.getAttribute(BaseAction.PARAM_ACTIONPATH_PARA_POPUP)%>">
<bean:define name="<%=Constants.BEAN_KEY%>" id="baseForm" toScope="request" />
<html:hidden property="paramService" styleId="paramService" />
<TABLE border="0" cellpadding="0" cellspacing="0" width="60%" height="70%">

<tr>
	<td class="tt3L" valign="top">
	<div class="tt5L" id="conteudo" style="width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
		<%-- AQUI COMEÇA O CONTEUDO --%>
		<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
			
			<tr>
				<td class="tt0l" colspan="10" >
					<b>
					<img src="../assets/tit_aplic.gif" border="0"/>&nbsp;
					<bean:write name="baseForm" property="popUpController.tituloPopUp"/>
					</b>
				</td>
			</tr>
			
			<%try{%>
				<%-- INICIO CHAMADA HEADER --%>
				<tiles:get name="header"/>
				<%-- FINAL CHAMADA HEADER --%>
			<%}catch(Exception e){
				e.printStackTrace(response.getWriter());
			}%>			
	
			<%try{%>
				<%-- INICIO CHAMADA BODYyyyyyy --%>
				<script src='<tiles:getAsString name="js" />'></script>
				<tiles:get name="body" flush="true"  />
				
				<%-- FINAL CHAMADA BODY --%>
			<%}catch(Exception e){
				e.printStackTrace(response.getWriter());
			}%>
			
		</table>
		<%-- AQUI TERMINA O CONTEUDO --%>
	</div>
	</td>
</tr>
<tr>
	<td colspan="2" class="tt0C" height="1px;">
		<%-- INSERT PARA BOTOES DE PESQUISA DE BaseCadastroForm --%>
		<tiles:get name="botoes"/>
		<%-- INSERT PARA BOTOES DE PESQUISA DE BaseCadastroForm --%>
	</td>
</tr>

<%-- FINAL  CHAMADA FOOTER --%>
<%try{%>
	<%-- INICIO CHAMADA BODYyyyyyy --%>
	<tiles:get name="footer"/>
	<%-- FINAL CHAMADA BODY --%>
<%}catch(Exception e){
	e.printStackTrace(response.getWriter());
}%>
<%-- FINAL  CHAMADA FOOTER --%>

</TABLE>
	<%try{%>
		<%-- INICIO CHAMADA MENSAGENS DE ERRO --%>
		<tiles:get name="erros"/>
		<%-- FINAL CHAMADA MENSAGENS DE ERRO --%>
	<%}catch(Exception e){
		e.printStackTrace(response.getWriter());
	}%>

</html:form>
</body>
</html>
