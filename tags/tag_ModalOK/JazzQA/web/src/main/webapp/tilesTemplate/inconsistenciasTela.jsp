<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@page import="br.com.dlp.framework.struts.action.BaseAction"%>

<style>
.erro {
	z-index:1; 
	position:absolute; 
	border-width: 1; 
	border-color: #c0c0c0; 
	left:150px; 
	top:95px; 
	width:600px; 
	height:500px; 
	filter: alpha(Opacity=90, FinishOpacity=15, Style=0); 
 	overflow-x:auto; overflow-y:auto;
 	display:none;
}
</style>
 
<%-- ESTA É A VARIAVEL DE INCREMENTO DAS MENSAGENS, NÃO APAGUE --%>
<%int i=0; %>

<%-- Inicio RENDERIZA MENSAGENS CRIADAS POR ActionMessages --%>
</div>
<div class="erro" id="divErro">
<table cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td class="tt4L"><b>ALERTA</b></td>
	<td class="tt4R" width="1%"><a href="javascript:fechaDivErro();"><img src="../assets/ico_close_on.gif" border="0"></a></td>
</tr>
</table>
<logic:messagesPresent>
<script>
document.getElementById("divErro").style.display = "block";
document.getElementById("conteudo").style.display = "none";
</script>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="tt1c" width="20%" colspan=2 >&nbsp;
			<b><bean:message key="mensagensDeErro" /><b><br>
		</td>
	</tr>
	<html:messages id="message"  >
	<tr>
		<td class="tt1l" width="20%" colspan=2>&nbsp;
			<bean:write name='message' filter='false' />
		</td>
	</tr>
	</html:messages>
</logic:messagesPresent>
<%-- fim RENDERIZA MENSAGENS CRIADAS POR ActionMessages --%>

<%-- Inicio RENDERIZA MENSAGENS de EXCECOES_NEGOCIO --%>
<logic:present name='<%=BaseAction.EXCECOES_NEGOCIO%>'>
<script>
document.getElementById("divErro").style.display = "block";
document.getElementById("conteudo").style.display = "none";
</script>

<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="tt1l" width="20%" colspan=2>&nbsp;
			<b><bean:message key="ErrosDeNegocio" /><b><br>
		</td>
	</tr>
	<logic:iterate id="excecaoDaVez" name="<%=BaseAction.EXCECOES_NEGOCIO%>" >
		<tr>
			<td class="tt1l" width="20%" colspan=2>&nbsp;
				<logic:present name="excecaoDaVez" property="messageKey" >
					<bean:message key="inconsistenciasTela.jsp.inconsistenciao" />
					<bean:message  name="excecaoDaVez" property="messageKey"/>
				</logic:present>
				<logic:present name="excecaoDaVez" property="message" >
					<bean:message key="inconsistenciasTela.jsp.causa" />
					<bean:write name="excecaoDaVez" property="message"/>
				</logic:present>
			</td>
		</tr>
	</logic:iterate>
</logic:present>
<%-- Fim RENDERIZA MENSAGENS RENDERIZA MENSAGENS de EXCECOES_NEGOCIO --%>

<%-- Inicio RENDERIZA MENSAGENS de EXCECOES_TECNICAS --%>
<logic:present name='<%=BaseAction.EXCECOES_TECNICAS%>'>
<script>
document.getElementById("divErro").style.display = "block";
document.getElementById("conteudo").style.display = "none";
</script>

<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="tt1l" width="20%" colspan=2 >&nbsp;
			<b><bean:message key="ErrosTecnicos" /><b><br>
		</td>
	</tr>
	<logic:iterate id="excecao" name="<%=BaseAction.EXCECOES_TECNICAS%>">
		<tr>
			<td class="tt1l" width="20%" colspan=2 style="color:blue" >&nbsp;
				<%i++;%>
				<a class="ll1" name="hrefDetalhes<%=i%>" title="Detalhes da Exceção" href="javascript:visibilidade('hrefDetalhes<%=i%>','trDetalhes<%=i%>');"><img src="../assets/maisVermelho.gif" border="0"></a>&nbsp;

				<logic:present name="excecao" property="messageKey" >
					<bean:write name="excecao"/>:<bean:write name="excecao" property="messageKey" ignore="true" /><BR>
				</logic:present>
				
				<bean:write name="excecao"/>:<bean:write name="excecao" property="message"/><BR>
			</td>
		</tr>
		
		
		
		
			<tr name='trDetalhes<%=i%>' id='trDetalhes<%=i%>' style="{display='none'}" >
			<td class="tt2l" >
				<%
				do{%>
					<br>
					<b><bean:write name="excecao"/></b><br>
					
					<logic:iterate id="statckTraceElement" name="excecao" property="stackTrace" >
						<bean:write name="statckTraceElement"/><br>
					</logic:iterate>
					
					<%
					excecao = ((Throwable)excecao).getCause();
					if(excecao!=null){
						pageContext.setAttribute("excecao",excecao);
					}
					
				}while(excecao!=null);%>
				<%--
				--%>
			</td>
		</tr>
	</logic:iterate>
</logic:present>
<%-- Fim RENDERIZA MENSAGENS RENDERIZA MENSAGENS de EXCECOES_TECNICAS --%>

<%-- Inicio RENDERIZA MENSAGENS RENDERIZA MENSAGENS de EXCECOES_OUTRAS --%>
<logic:present name='<%=BaseAction.EXCECOES_OUTRAS%>'>
<script>
document.getElementById("divErro").style.display = "block";
document.getElementById("conteudo").style.display = "none";
</script>

<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="tt1l" width="20%" colspan=2 >&nbsp;
			<b><bean:message key="ErrosOutros" /><b><br>
		</td>
	</tr>
	<logic:iterate id="excecao" name="<%=BaseAction.EXCECOES_OUTRAS%>" >
		<tr>
			<td class="tt1l" width="20%" colspan=2 style="color:blue" >&nbsp;
				<%i++;%>
				<a class="ll1" name="hrefDetalhes<%=i%>" title="Detalhes da Exceção" href="javascript:visibilidade('hrefDetalhes<%=i%>','trDetalhes<%=i%>');"><img src="../assets/maisVermelho.gif" border="0"></a>&nbsp;

				<logic:present name="excecao" property="messageKey" >
					<bean:write name="excecao"/>:<bean:write name="excecao" property="messageKey" ignore="true" /><BR>
				</logic:present>
				
				<bean:write name="excecao"/>:<bean:write name="excecao" property="message"/><BR>
			</td>
		</tr>
			<tr name='trDetalhes<%=i%>' id='trDetalhes<%=i%>' style="{display='none'}" >
			<td class="tt2l" >
			<%--
				<%
				do{%>
					<br>
					<b><bean:write name="excecao"/></b><br>
					
					<logic:iterate id="statckTraceElement" name="excecao" property="stackTrace" >
						<bean:write name="statckTraceElement"/><br>
					</logic:iterate>
					
					<%
					excecao = ((Throwable)excecao).getCause();
					if(excecao!=null){
						pageContext.setAttribute("excecao",excecao);
					}
					
				}while(excecao!=null);%>
			--%>
			</td>
		</tr>
	</logic:iterate>
</logic:present>
<tr>
<td class="tt0C">&nbsp;</td>
</tr>
</table>
</div>
<%-- Fim RENDERIZA MENSAGENS RENDERIZA MENSAGENS de EXCECOES_OUTRAS --%>