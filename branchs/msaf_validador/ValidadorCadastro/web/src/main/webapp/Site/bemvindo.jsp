<html>
<head>
<title>portal_mastersaf_final</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../css/estilo.css" rel="stylesheet" type="text/css">
<link href="../css/screensmall.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function teste(){ 
	if(document.getElementById("usuarioLogado").value==""){
		document.getElementById("j_username").focus();
	} else {
		window.location.href ="bemvindo.jsp";
	
	}
}
//Identificador de seleção do tr
var trSelecionado = false;
var trObjetoSelecionado = null;

// Funções globais
function mudaCor(trElemento, ligarCor) {
	if (trSelecionado)
		return;
}



function mudarCorTR(modificaElemento, ligarCor) {

	// Somente efetua a modificação se o elemento não estiver selecionado
	if (trSelecionado)
		return;

	// Variaveis
	var cor = "";

	// Verifica qual a cor a selecionar
	if (ligarCor)
		cor = "#E5E9EC";
	else
		cor = "";

	// Monta a cor do elemento conforme a solicitação
	for (x = 0; x < modificaElemento.cells.length; x++)
		modificaElemento.cells(x).style.backgroundColor = cor;
}

function sincronizarScrollTB(dvOrigemTB, tbScrollResize) {
	tbScrollResize.style.width = parseInt(dvOrigemTB.style.width) - 15;
}

function MM_preloadImages() { // v3.0
	var d = document;
	if (d.images) {
		if (!d.MM_p)
			d.MM_p = new Array();
		var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
		for (i = 0; i < a.length; i++)
			if (a[i].indexOf("#") != 0) {
				d.MM_p[j] = new Image;
				d.MM_p[j++].src = a[i];
			}
	}
}
// abrir links em janela popup

function MM_openBrWindow(theURL, winName, features) { // v2.0
	window.open(theURL, winName, features);
}

// utilizado para limpar o input text quando ganha foco

function limparPadrao(campo) {
	if (campo.value == campo.defaultValue) {
		campo.value = "";
	}
}

function escreverPadrao(campo) {
	if (campo.value == "") {
		campo.value = campo.defaultValue;
	}
}

function MM_swapImgRestore() { // v3.0
	var i, x, a = document.MM_sr;
	for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
		x.src = x.oSrc;
}
function MM_findObj(n, d) { // v4.01
	var p, i, x;
	if (!d)
		d = document;
	if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
		d = parent.frames[n.substring(p + 1)].document;
		n = n.substring(0, p);
	}
	if (!(x = d[n]) && d.all)
		x = d.all[n];
	for (i = 0; !x && i < d.forms.length; i++)
		x = d.forms[i][n];
	for (i = 0; !x && d.layers && i < d.layers.length; i++)
		x = MM_findObj(n, d.layers[i].document);
	if (!x && d.getElementById)
		x = d.getElementById(n);
	return x;
}

function MM_swapImage() { // v3.0
	var i, j = 0, x, a = MM_swapImage.arguments;
	document.MM_sr = new Array;
	for (i = 0; i < (a.length - 2); i += 3)
		if ((x = MM_findObj(a[i])) != null) {
			document.MM_sr[j++] = x;
			if (!x.oSrc)
				x.oSrc = x.src;
			x.src = a[i + 2];
		}
}
function MM_showHideLayers() { // v9.0
	var i, p, v, obj, args = MM_showHideLayers.arguments;
	for (i = 0; i < (args.length - 2); i += 3)
		with (document)
			if (getElementById && ((obj = getElementById(args[i])) != null)) {
				v = args[i + 2];
				if (obj.style) {
					obj = obj.style;
					v = (v == 'show') ? 'visible' : (v == 'hide') ? 'hidden'
							: v;
				}
				obj.visibility = v;
			}
}
</script>
<script src="Scripts/AC_RunActiveContent.js" type="text/javascript"></script></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('images/bt_login_over.jpg')">
<!-- ImageReady Slices (portal_mastersaf_final.psd) -->
<table border="0" cellpadding="0" cellspacing="0" id="Table_01">
  <form action="" method="get">
	<tr>
		<td rowspan="8">
			<img src="images/layers_01.jpg" width="185" height="85" alt=""></td>
		<td colspan="7">
			<img src="images/layers_02.jpg" width="396" height="10" alt=""></td>
		<td colspan="16">
			<img src="images/layers_03.jpg" width="426" height="10" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="10" alt=""></td>
	</tr>
	<tr>
		<td colspan="6" rowspan="5">
			<img src="images/layers_04.jpg" width="395" height="46" alt=""></td>
		<td colspan="14">
			<img src="images/layers_05.jpg" width="368" height="11" alt=""></td>
		<td colspan="3" rowspan="5">
			<img src="images/layers_06.jpg" width="59" height="46" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="11" alt=""></td>
	</tr>
	<tr>
		<td colspan="10">
			<img src="images/layers_07.jpg" width="305" height="5" alt=""></td>
		<td colspan="3" rowspan="3"><a class="link" href="../UploadArquivosAction.do?dispatchMethod=doLogoff" onClick="document.forms.user.submit();return false"><img src="images/bt_logoff.jpg" alt="Login" name="bt_login" width="56" height="29" border="0"></a></td>
		<td rowspan="4">
			<img src="images/layers_09.jpg" width="7" height="35" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="5" alt=""></td>
	</tr>
	<tr>
		<td colspan="3" rowspan="3">
			<img src="images/layers_10.jpg" width="19" height="30" alt=""></td>
		<td colspan="4">
			<input class="login" name="usuario" type="text" width="90" height="1"/></td>
		<td rowspan="3">
			<img src="images/layers_12.jpg" width="12" height="30" alt=""></td>
		<td>
			<input class="login" name="senha" type="password" width="90" height="1"/></td>
		<td rowspan="3">
			<img src="images/layers_14.jpg" width="10" height="30" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="17" alt=""></td>
	</tr>
	<tr>
		<td colspan="4" rowspan="2">
			<img src="images/layers_15.jpg" width="132" height="13" alt=""></td>
		<td rowspan="2">
			<img src="images/layers_16.jpg" width="132" height="13" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="7" alt=""></td>
	</tr>
	<tr>
		<td colspan="3">
			<img src="images/layers_17.jpg" width="56" height="6" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="6" alt=""></td>
	</tr>
	<tr>
		<td colspan="23">
			<img src="images/layers_18.jpg" width="822" height="10" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="10" alt=""></td>
	</tr>
	<tr>
		<td rowspan="3">
			<img src="images/layers_19.jpg" width="9" height="236" alt=""></td>
		<td colspan="21" rowspan="23" align="center">
        
        <iframe name="conteudo" src="../UploadArquivosAction.do?dispatchMethod=init" width="777" height="652" frameborder="0">
        </iframe>

   	</td>
		<td rowspan="3">
			<img src="images/layers_22.jpg" width="36" height="236" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="19" alt=""></td>
	</tr>
	<tr>
		<td rowspan="25">
        <%@ include file="menu_inc.html" %>
        </td>
  <td>
			<img src="images/spacer.gif" width="1" height="20" alt=""></td>
	</tr>
	<tr>
	  <td>
			<img src="images/spacer.gif" width="1" height="197" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/layers_25.jpg" width="9" height="16" alt=""></td>
	  <td>
			<img src="images/layers_29.jpg" width="36" height="16" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="16" alt=""></td>
	</tr>
	<tr>
		<td rowspan="2">
			<img src="images/layers_30.jpg" width="9" height="36" alt=""></td>
	  <td rowspan="19">
			<img src="images/layers_34.jpg" width="36" height="401" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="8" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="28" alt=""></td>
	</tr>
	<tr>
		<td rowspan="17">
			<img src="images/layers_43.jpg" width="9" height="365" alt=""></td>
	  <td>
			<img src="images/spacer.gif" width="1" height="16" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="7" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="30" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="4" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="27" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="25" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="11" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="39" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="4" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="1" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="22" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="24" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="21" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="9" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="1" height="29" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="53" alt=""></td>
	</tr>
	<tr>
	  <td>
		  <img src="images/spacer.gif" width="1" height="43" alt=""></td>
	</tr>
	<tr>
		<td colspan="23">
			<img src="images/layers_75.jpg" width="822" height="9" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="9" alt=""></td>
	</tr>
	<tr>
		<td colspan="23">
			<img src="images/layers_76.jpg" width="822" height="25" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="25" alt=""></td>
	</tr>
	<tr>
		<td colspan="23">
			<img src="images/layers_77.jpg" width="822" height="22" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="22" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="185" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="9" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="13" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="106" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="11" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="242" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="14" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="13" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="5" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="33" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="10" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="13" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="76" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="12" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="132" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="10" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="42" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="8" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="6" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="7" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="8" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="15" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="36" height="1" alt=""></td>
		<td></td>
	</tr>
  </form>
</table>
<!-- End ImageReady Slices -->
</body>
</html>