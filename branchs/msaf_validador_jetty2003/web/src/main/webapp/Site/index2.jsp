<html>
<head>
<title>portal_mastersaf_final</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="estilo.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}


function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


//utilizado para limpar o input text quando ganha foco

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
//-->
</script>
<script src="Scripts/AC_RunActiveContent.js" type="text/javascript"></script></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('images/bt_login_over.jpg','images/bt_notafiscale_over.jpg','images/bt_preenchimento_over.jpg','images/bt_envio_over.jpg','images/bt_acompanhamento_over.jpg','images/bt_validacao_over.jpg','images/bt_consultor_over.jpg','images/bt_financeiro_over.jpg','images/bt_workflow_over.jpg')">
<!-- ImageReady Slices (portal_mastersaf_final.psd) -->
<table border="0" cellpadding="0" cellspacing="0" id="Table_01">
	<form action="j_security_check" name="user" id="user">  
<!-- 
  <form action="bemvindo.html" method="get" name="user">
 -->  
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
		<td height="46" colspan="6" rowspan="5" class="bemvindo">Bem vindo!</td>
		<img src="images/layers_05.jpg" width="368" height="11" alt=""></td>
		<td colspan="3" rowspan="5">
			<img src="images/layers_06.jpg" width="59" height="46" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="11" alt=""></td>
	</tr>
	<tr>
		<td colspan="10">
			<img src="images/layers_07.jpg" width="305" height="5" alt=""></td>
			<td colspan="3" rowspan="3"><a class="link" href="/index.html" onClick="document.forms.user.submit();return false"><img src="images/bt_login.jpg" alt="Login" name="bt_login" width="56" height="29" border="0"></a></td>
			
		<td rowspan="4">
			<img src="images/layers_09.jpg" width="7" height="35" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="5" alt=""></td>
	</tr>
	<tr>
		<td colspan="3" rowspan="3">
			<img src="images/layers_10.jpg" width="19" height="30" alt=""></td>
		<td colspan="4">
			<input name="j_username" type="text" width="90" height="1" value="Usuário" onfocus="limparPadrao(this);" onblur="escreverPadrao(this);"/></td>
		<td rowspan="3">
			<img src="images/layers_12.jpg" width="12" height="30" alt=""></td>
		<td>
			<input name="j_password" type="password" width="90" height="1" value="Senha" onfocus="limparPadrao(this);" onblur="escreverPadrao(this);"/></td>
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
		<td colspan="21" rowspan="3" valign="top"><script type="text/javascript">
AC_FL_RunContent( 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0','width','775','height','235','src','box1','quality','high','pluginspage','http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash','movie','box1' ); //end AC code
</script><noscript><object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0" width="775" height="235">
          <param name="movie" value="box1.swf">
          <param name="quality" value="high">
          <embed src="box1.swf" quality="high" pluginspage="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" width="775" height="235"></embed>
	    </object></noscript></td>
		<td rowspan="3">
			<img src="images/layers_22.jpg" width="36" height="236" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="19" alt=""></td>
	</tr>
	<tr>
		<td rowspan="22"><table id="Table_3" width="185" height="634" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td colspan="3" align="right" valign="top" background="images/menu_01.jpg" class="fazerlogin"><img src="images/spacer.gif" width="1" height="18"></td>
          </tr>
          <tr>
            <td rowspan="14"><img src="images/menu_02.jpg" width="12" height="418" alt=""></td>
            <td colspan="2"><a href="#" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('notafiscale','','images/bt_notafiscale_over.jpg',1)"><img src="images/bt_notafiscale.jpg" alt="Nota Fiscal Eletr&ocirc;nica" name="notafiscale" width="173" height="24" border="0"></a></td>
          </tr>
          <tr>
            <td rowspan="4"><img src="images/menu_04.jpg" width="5" height="82" alt=""></td>
            <td><a href="#" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('preenchimento','','images/bt_preenchimento_over.jpg',1)"><img src="images/bt_preenchimento.jpg" alt="Preenchimento" name="preenchimento" width="168" height="24" border="0"></a></td>
          </tr>
          <tr>
            <td><a href="#" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('envio','','images/bt_envio_over.jpg',1)"><img src="images/bt_envio.jpg" alt="Envio" name="envio" width="168" height="24" border="0"></a></td>
          </tr>
          <tr>
            <td><a href="#" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('acompanhamento','','images/bt_acompanhamento_over.jpg',1)"><img src="images/bt_acompanhamento.jpg" alt="Acompanhamento" name="acompanhamento" width="168" height="21" border="0"></a></td>
          </tr>
          <tr>
            <td><img src="images/menu_08.jpg" width="168" height="13" alt=""></td>
          </tr>
          <tr>
          <td colspan="2"><a href="bemvindo.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('validacao','','images/bt_validacao_over.jpg',1)"><img src="images/bt_validacao.jpg" alt="Valida&ccedil;&atilde;o" name="validacao" width="173" height="24" border="0"></a></td>
          <!-- 
            <td colspan="2"><a href="../recursosSeguros/index.jsp" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('validacao','','images/bt_validacao_over.jpg',1)"><img src="images/bt_validacao.jpg" alt="Valida&ccedil;&atilde;o" name="validacao" width="173" height="24" border="0"></a></td>
             -->
          </tr>
          <tr>
            <td colspan="2"><img src="images/menu_10.jpg" width="173" height="5" alt=""></td>
          </tr>
          <tr>
            <td colspan="2"><a href="#" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('consultor','','images/bt_consultor_over.jpg',1)"><img src="images/bt_consultor.jpg" alt="Consultor" name="consultor" width="173" height="24" border="0"></a></td>
          </tr>
          <tr>
            <td colspan="2"><img src="images/menu_12.jpg" width="173" height="5" alt=""></td>
          </tr>
          <tr>
            <td colspan="2"><a href="#" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('financeiro','','images/bt_financeiro_over.jpg',1)"><img src="images/bt_financeiro.jpg" alt="Financeiro" name="financeiro" width="173" height="24" border="0"></a></td>
          </tr>
          <tr>
            <td colspan="2"><img src="images/menu_14.jpg" width="173" height="6" alt=""></td>
          </tr>
          <tr>
            <td colspan="2"><a href="#" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('workflow','','images/bt_workflow_over.jpg',1)"><img src="images/bt_workflow.jpg" alt="Workflow" name="workflow" width="173" height="24" border="0"></a></td>
          </tr>
          <tr>
            <td colspan="2"><img src="images/menu_16.jpg" width="173" height="6" alt=""></td>
          </tr>
          <tr>
            <td colspan="2"><img src="images/calendar.jpg" width="173" height="194" alt=""></td>
          </tr>
          <tr>
            <td colspan="3"><img src="images/menu_18.jpg" width="185" height="196" alt=""></td>
          </tr>
                </table></td>
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
		<td colspan="9">
			<img src="images/layers_26.jpg" width="438" height="16" alt=""></td>
		<td>
			<img src="images/layers_27.jpg" width="10" height="16" alt=""></td>
		<td colspan="11">
			<img src="images/layers_28.jpg" width="329" height="16" alt=""></td>
		<td>
			<img src="images/layers_29.jpg" width="36" height="16" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="16" alt=""></td>
	</tr>
	<tr>
		<td rowspan="2">
			<img src="images/layers_30.jpg" width="9" height="36" alt=""></td>
		<td colspan="9" rowspan="19" valign="top"><table id="Table_" width="438" height="401" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td colspan="4"><img src="images/box2_01.jpg" width="438" height="42" alt=""></td>
          </tr>
          <tr>
            <td rowspan="2"><img src="images/box2_02.jpg" width="8" height="359" alt=""></td>
            <td valign="top" background="images/box2_04.jpg"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="2">&nbsp;</td>
              </tr>
              <tr>
                <td width="28%" rowspan="3" valign="top"><a href="#"><img src="images/logo_validador.png" width="117" height="140" border="0" /></a></td>
                <td width="72%" valign="top" class="title_text2_box2"><strong><a href="#" class="title_text2_box2">Validador</a></strong></td>
              </tr>
              <tr>
                <td valign="top" class="text1_box2">Eficiente saneamento de cadastro de clientes e fornecedores.</td>
              </tr>
              <tr>
                <td valign="top" class="text2_box2"><a href="#" class="text2_box2">Fornecem dados que permitem o bloqueio dos fornecedores e clientes que n&atilde;o estiverem habilitados ou em situa&ccedil;&atilde;o irregular. Mant&eacute;m a integridade dos dados inseridos verificando a validade do CNPJ ou IE da consulta...</a></td>
              </tr>
              <tr>
                <td colspan="2">&nbsp;</td>
              </tr>
              <tr>
                <td rowspan="3" valign="top"><a href="#"><img src="images/logo_nfe.png" width="117" height="140" border="0" /></a></td>
                <td><a href="#" class="title_text2_box2"><strong>Nota Fiscal Eletr&ocirc;nica</strong></a></td>
              </tr>
              <tr>
                <td valign="top"><span class="text1_box2">Dinamismo, redu&ccedil;&atilde;o de custos e valoriza&ccedil;&atilde;o do tempo e dos neg&oacute;cios.</span></td>
              </tr>
              <tr>
                <td valign="top"><a href="#" class="text2_box2">A Mastersaf NF-e &eacute; uma solu&ccedil;&atilde;o eficiente, com interface simples e facilmente integrada aos sistemas j&aacute; existentes em sua empresa. A melhor solu&ccedil;&atilde;o para o processo de controle e emiss&atilde;o de documentos fiscais eletr&ocirc;nicos...</a></td>
              </tr>

            </table></td>
            <td><img src="images/box2_04.jpg" width="10" height="335" alt=""></td>
            <td rowspan="2"><img src="images/box2_05.jpg" width="17" height="359" alt=""></td>
          </tr>
          <tr>
            <td colspan="2"><img src="images/box2_06.jpg" width="413" height="24" alt=""></td>
          </tr>
        </table></td>
		<td rowspan="2">
			<img src="images/layers_32.jpg" width="10" height="36" alt=""></td>
		<td colspan="11" rowspan="19" valign="top"><table id="Table_2" width="329" height="401" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td colspan="4"><img src="images/box3_01.jpg" width="329" height="42" alt=""></td>
          </tr>
          <tr>
            <td rowspan="2"><img src="images/box3_02.jpg" width="10" height="359" alt=""></td>
            <td valign="top" background="images/box3_04.jpg"><table width="100%" height="200" border="0" cellpadding="0" cellspacing="0" id="news">
              <tr>
                <td height="5"><img src="images/spacer.gif" width="1" height="5"></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="titlenews"><span>Proposi&ccedil;&otilde;es Legislativas</span></a></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="text2_box2">Acompanhamento di&aacute;rio dos projetos de lei em tramita&ccedil;&atilde;o, para sua empresa estar sempre um passo &agrave; frente no mercado...</a></td>
              </tr>
              <tr>
                <td><img src="images/spacer.gif" width="1" height="5"></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="titlenews"><span>Resumos - Di&aacute;rio Oficial</span></a></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="text2_box2">Uma s&iacute;ntese de tudo o que voc&ecirc; precisa saber sobre Atos Legais publicados nos Di&aacute;rios Oficiais, de forma completa e objetiva...</a></td>
              </tr>
              <tr>
                <td><img src="images/spacer.gif" width="1" height="5"></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="titlenews"><span>Biblioteca Jur&iacute;dica</span></a></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="text2_box2">Um banco de dados de todos os Atos Legais em sua reda&ccedil;&atilde;o original publicados nos Di&aacute;rios Oficiais, em &acirc;mbito Federal...</a></td>
              </tr>
              <tr>
                <td><img src="images/spacer.gif" width="1" height="5"></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="titlenews"><span>Resumos - Di&aacute;rio Oficial</span></a></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="text2_box2">Uma s&iacute;ntese de tudo o que voc&ecirc; precisa saber sobre Atos Legais publicados nos Di&aacute;rios Oficiais, de forma completa e objetiva...</a></td>
              </tr>
              <tr>
                <td><img src="images/spacer.gif" width="1" height="5"></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="titlenews"><span>Proposi&ccedil;&otilde;es Legislativas</span></a></td>
              </tr>
              <tr>
                <td><a href="http://www.legiscenter.com.br" target="_blank" class="text2_box2">Acompanhamento di&aacute;rio dos projetos de lei em tramita&ccedil;&atilde;o, para sua empresa estar sempre um passo &agrave; frente no mercado...</a></td>
              </tr>
              <tr>
                <td><img src="images/spacer.gif" width="1" height="5"></td>
              </tr>
            </table></td>
            <td><img src="images/box3_04.jpg" width="11" height="336" alt=""></td>
            <td rowspan="2"><img src="images/box3_05.jpg" width="13" height="359" alt=""></td>
          </tr>
          <tr>
            <td colspan="2"><img src="images/box3_06.jpg" width="306" height="23" alt=""></td>
          </tr>
        </table></td>
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
		<td rowspan="17">
			<img src="images/layers_45.jpg" width="10" height="365" alt=""></td>
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
		<td rowspan="3">
			<img src="images/layers_74.jpg" width="185" height="56" alt=""></td>
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

<!--  iframe -->
<iframe src="" name="iFrameAviso" id="iFrameMenu" class="aviso" style="display: none;"></iframe>
<div id="div_aviso" class="aviso" style="display: none;">
  <table width="500" border="1" cellpadding="0" cellspacing="0" bordercolor="#333" bgcolor="#FFFFFF">
    <tr>
      <td><table width="500" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#003366" bgcolor="#FFFFFF">
        <tr>
          <td class="title0aviso"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td class="title0aviso">aviso</td>
              <td align="right"><a href="#"><img src="images/bt_fechar.gif" width="22" height="17" border="0" onClick="MM_showHideLayers('iFrameMenu','','hide','div_aviso','','hide')"></a></td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td class="title1aviso">usu&aacute;rio e senha</td>
        </tr>
        <tr>
          <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
              <td width="31%" rowspan="5" align="center"><img src="images/aviso.png" width="32" height="32"></td>
              <td width="69%" height="20" align="left" class="textocomumleft">teste de mensagem</td>
            </tr>
            <tr>
              <td height="20"><span class="textocomumleft">teste de mensagem</span></td>
            </tr>
            <tr>
              <td height="20"><span class="textocomumleft">teste de mensagem</span></td>
            </tr>
            <tr>
              <td height="20"><span class="textocomumleft">teste de mensagem</span></td>
            </tr>
            <tr>
              <td height="20"><span class="textocomumleft">teste de mensagem</span></td>
            </tr>
            <tr>
              <td colspan="2" align="center">&nbsp;</td>
            </tr>
          </table></td>
        </tr>
      </table></td>
    </tr>
  </table>
</div>




</body>
</html>