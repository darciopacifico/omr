<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
	<head>
		<meta http-equiv="Page-Enter" content="blendTrans(Duration=0.1)">
		<title>
			<tiles:getAsString name="title" />
		</title>
		<script src='../js/funcoesDados.js' language="javascript"></script>
		<script src='../js/funcoesData.js' language="javascript"></script>
		<script src='../js/funcoesNavegacao.js' language="javascript"></script>
		<script src='../js/funcoesNum.js' language="javascript"></script>
		<script src="../js/simplecalendar.js" language="javascript"></script>
		
		<link rel="stylesheet" href="../css/new_base.css" type="text/css">
		<link rel="stylesheet" href="../css/calendar.css" type="text/css">
	</head>

<body class="corpo" topmargin="2" leftmargin="2"
	onkeypress="onKeyPress(window.event.keyCode)">

<div id="divMenu" class="menu" style="height: 300px; width: 50px; border-color: black; ">
	<tiles:get name="menu" />
</div>

<div border="0" id="conteudo"
	style="width: 100%; height: 100%; overflow-x: auto; overflow-y: auto;">
<TABLE border="2" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="neutro" valign="top"><%-- AQUI COMEÇA O CONTEUDO --%>
		<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td class="tt0l"><b> <img src="../assets/tit_aplic.gif"
					border=0>&nbsp; <%-- TITULO DA TELA --%> <tiles:getAsString
					name="title" />teste 1234 <%-- TITULO DA TELA --%> </b></td>
			</tr>
			<%-- INICIO CHAMADA HEADER --%>
			<tiles:get name="header" />
			<%-- FINAL CHAMADA HEADER --%>
			<%try{%>
			<%-- INICIO CHAMADA BODY --%>
			<script src='<tiles:getAsString name="js" />'></script>
			<tiles:get name="body" flush="true" />
			<%-- FINAL CHAMADA BODY --%>
			<%}catch(Exception e){
  %><pre>
			<%
  e.printStackTrace(response.getWriter());
  %>
			</pre>
			<%
}%>

			<%try{%>
			<%-- INICIO CHAMADA BARRA DE STATUS --%>
			<tiles:get name="barraDeStatus" flush="true" />
			<%-- FINAL CHAMADA BARRA DE STATUS --%>
			<%}catch(Exception e){
  %><pre>
			<%
  e.printStackTrace(response.getWriter());
  %>
			</pre>
			<%
}%>


		</table>
		<%-- AQUI TERMINA O CONTEUDO --%></td>
	</tr>

	<tr>
		<td class="neutro">
		<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td class="tt2C" width="1%"><a href="javascript:showErro();"><img
					src="../assets/alert_erro.gif" id="imgErro" border="0"
					alt="Ver erros ou alertas" style="display: none;" LOOP=INFINITE></a>
				</td>
				<td class="tt1R"><%-- INSERT PARA BOTOES DE PESQUISA DE BaseCadastroForm --%>
				<tiles:get name="botoes" /> <%-- INSERT PARA BOTOES DE PESQUISA DE BaseCadastroForm --%>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<%try{%>
	<%-- INICIO CHAMADA FOOTER --%>
	<tiles:get name="footer" />
	<%-- FINAL  CHAMADA FOOTER --%>
	<%}catch(Exception e){
  %><pre>
	<%
  e.printStackTrace(response.getWriter());
  %>
	</pre>
	<%
}%>


</TABLE>
</div>

<%try{%>
<%-- INICIO CHAMADA MENSAGENS DE ERRO --%>
<tiles:get name="erros" />
<%-- FINAL CHAMADA MENSAGENS DE ERRO --%>
<%}catch(Exception e){
  %><pre>
<%
  e.printStackTrace(response.getWriter());
  %>
</pre>
<%
}%>



</body>
</html>