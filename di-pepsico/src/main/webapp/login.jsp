<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>MasterSAF NFe</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-image: url(img/login/login_bg_new.jpg);
}
-->
</style>
<script type="javascript">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-image: url(img/login/login_bg.jpg);
}
-->        
</script>
<link href="css/main.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        <!--
        
        -->
    </style>
    <link href="css/estilo.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
<!--
.style7 {color: #FFFFFF}
.style8 {
	color: #FFFFFF;
	font-weight: bold;
}
-->
.fontHeaderLogin {
	font-family: sans-serif;
	font-size: 40px;
	color:#ffffff;
	font-weight: bold;
}
</style>
</head>
<body
	onLoad="javascript: document.forms[0].j_username.focus(); return true;">
<table width="100%" height="256" border="0" cellpadding="0"
	cellspacing="0">
	<tr>
		<td align="center" valign="middle">
		<p>&nbsp;</p>
		<p class="fontHeaderLogin">Documento de Importação - Pepsico</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<f:view>
		<h:form id="form1">
		<table cellpadding="3" cellspacing="9">
			<tr>
				<td colspan="2" align="right" class="style5">
				<!--  jsf error message -->
				<p align="center" class="style5 style8">
					<h:outputText value="#{loginMBean.errorMessage}"></h:outputText>
				</p>
				</td>
			</tr>
			<tr>
				<td align="right" class="style4"><span class="style7">Usu&aacute;rio</span>:</td>
				<td><h:inputText id="nmUsuario" styleClass="editLogin" value="#{loginMBean.nmUsuario}" /></td>
			</tr>
			<tr>
				<td align="right" class="style4"><span class="style7">Senha</span>:</td>
				<td><h:inputSecret id="nmSenha" styleClass="editLogin" value="#{loginMBean.nmSenha}" /></td>
			</tr>
			<tr>
				<td colspan="2" align="right" class="style4">
				<h:commandButton id="logar" type="submit" styleClass="editLogin" value="Login" action="#{loginMBean.logar}" /></td>
			</tr>
		</table>
		<table width="466" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td width="50%" height="140" align="left" class="style5">
				<div align="center"><img src="img/login/logo_mil_nfe.png"
					alt="MasterSAF NFE" width="199" height="140" hspace="0" vspace="0" /></div>
				</td>
				<td width="50%" align="center" class="style5"><img
					src="img/login/logo_cscorp.png"
					alt="MasterSAF - www.mastersaf.com.br" width="126" height="62"
					hspace="0" vspace="0" /></td>
			</tr>
		</table>
		<p class="style7"><br />
		<span class="style7">MasterSAF&reg; - Sistema Melhor
		Visualizado em Resolu&ccedil;&atilde;o de 1024 x 768.<br />
		&copy;2008 - todos os direitos reservados. </span></p>
		</h:form>
		</f:view>
		</td>
	</tr>
</table>
</body>
</html>
