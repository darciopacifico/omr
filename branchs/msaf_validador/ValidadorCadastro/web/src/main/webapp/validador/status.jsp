<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean"prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<bean:define name="uploadArquivosForm" property="listStatusPediValidacao" id="listStatusPediValidacao"/> 

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<bean:define name="uploadArquivosForm" property="listStatusPediValidacao" id="listStatusPediValidacao"/>
<bean:define name="uploadArquivosForm" property="usuarioVO" id="usuarioVO"/>
    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html>
<html:form action="/UploadArquivosAction.do" method="post" enctype="multipart/form-data">


<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Status id Pedido Validacao</title>
	
	<script type="text/javascript">

	function abreResultados(){
		isAtualizarAutomaticamente(false);
		window.open('/msaf.validador.web/downloadArquivoServlet?idPedValidacao=<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.id"/>');
	}

	function downloadCertificados(){
		isAtualizarAutomaticamente(false);
		window.open('/msaf.validador.web/paginaRetornoServlet?dispathMethod=gerarLote&id=<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.id"/>');
	}
	
	function isAtualizarAutomaticamente(atualizaAutomatico){
		var permiteReload = document.getElementById("hdPermiteReload");
		permiteReload.checked=atualizaAutomatico;
	}
	
	
	function permiteFazerReload(){
		var permiteReload = document.getElementById("hdPermiteReload");
		return permiteReload.checked==true;
	}
	
	function fazerReload(){
		var permiteReload = document.getElementById("hdPermiteReload");
		if(permiteFazerReload()){
			//alert("Reload permitido");
			//alert(permiteReload.checked);
			//permiteReload.checked=false;
			location.reload(true);
		}else{
			//alert("Reload nao permitido");
			//alert(""+permiteReload.checked);
		}
	}

	
	setInterval("fazerReload();",5000);
	</script>
	
	<link href="css/estilo.css" rel="stylesheet" type="text/css">
	<link href="js/principal.js" rel="javascript" type="text/javascript">
	<link href="js/upload.js" rel="javascript" type="text/javascript">
</head>

<body > 
<table width="600" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
    <div align="center">
    <TABLE width="600" border=1 align="center" cellPadding=3 cellSpacing=0 borderColor=#003366>
      <TR align=middle valign=center>
        <TD colspan="5" class="title0">resultado geral do pedido de valida&ccedil;&atilde;o</TD>
      <TR align=middle valign=center>
        <TD colspan="5" class="title1">dados do cliente e or&ccedil;amento</TD>
      <TR align=middle valign=center>
        <TD class="title2"><div align="right">cliente:</div></TD>
        <TD colspan="4" class="registro01">
        <div align="left">
 								<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.clienteFk.nome"/>    
        </div>
       </TD>

      <TR align=middle valign=center>
        <TD class="title2"><div align="right">usuário:</div></TD>
        <TD colspan="4" class="registro01">
        <div align="left">
			<bean:write name="usuarioVO" property="login" />    
        </div>
       </TD>


      <TR align=middle valign=center>
        <TD class="title2"><div align="right">Pedido Validação</div></TD>
        <TD colspan="4" class="registro01">
        <div align="left">
 								<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.id"/>    
        </div>
        </TD>
      <TR>
        <TD class="title2"><div align="right">quantidade de registros:</div></TD>
        <TD colspan="4" class="registro01">
        <div align="left">
								<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.qtdeRegistrosArq"/>
        </div>
        </TD>
      </TR>


      <TR>
        <TD class="title2"><div align="right">Processamento:</div></TD>
        <TD colspan="4" class="registro01">
        <div align="left">
								<bean:write 	name="uploadArquivosForm" property="percentualCompleto" format="#0.0" />% completo.
        </div>
        </TD>
      </TR>      



      <%--
      
      <TR>
        <TD class="title2"><div align="right">Tempo médio por registro:</div></TD>
        <TD colspan="4" class="registro01">
        <div align="left">
								1,5 segundos
        </div>
        </TD>
      </TR>      
      
      --%>
      


      <TR vAlign=center align=middle borderColor=#cccccc>
        <TD colspan="5" bordercolor="#003366" class="title1"><div align="left">resultado dos registros <spam style="font-size: 8px; font-weight: 8px;">(<input type="checkbox" name="hdPermiteReload"  id="hdPermiteReload" checked  /> Atualização Automática)</spam> </div></TD>
      <TR valign=center align=middle>
        <TD width="167" class="title2">tipo de valida&ccedil;&atilde;o</TD>
        <TD width="313" colspan="3" class="title2">descri&ccedil;&atilde;o</TD>
        <TD width="90" align="center" class="title2">total</TD>
      </TR>
      	<logic:iterate  name="listStatusPediValidacao" id="listStatusPediValidacao">

			<c:set var="title" value="Código de Erro: ${listStatusPediValidacao.idTipoValidacao}" />

			<c:if test="${empty listStatusPediValidacao.idTipoValidacao}">
				<c:set var="title" value="" />
			</c:if>


	       <TR title="${title}">
			<TD class="title2">
				<div align="left" class="registro01">
	              <bean:write name='listStatusPediValidacao' property='labelValidacao'/>
	            </div> 
	        </TD> 
			<TD class="opcoes">
				<div align="left" class="registro01">
	              <bean:write name='listStatusPediValidacao' property='descricaoResultado'/>
	            </div>  
	        </TD>  
			<TD colspan="3" class="opcoes">
				<div align="center" class="registro01">
	              <bean:write name='listStatusPediValidacao' property='qtTotal'/>
	            </div>  
	        </TD> 
	   
	    </logic:iterate>
<!-- 
      <TR>
        <TD align="center" class="title2">total</TD>
        <TD colspan="3" class="total">&nbsp;</TD>
        <TD class="total">&nbsp;</TD>
      </TR>
 -->      
      <TR>
        <TD colspan="5" class="title1">download dos registros</TD>
      </TR>
      <TR>
        <TD colspan="5" class="registro01"><br>

					<logic:empty name="uploadArquivosForm" property="listStatusPediValidacao">
						<table width="80%" border="0" align="center" cellpadding="3" cellspacing="5">
							<tr>
								<td colspan="2" align="center">&nbsp;
								</td>
							</tr>
							<tr>
								<td height="40" align="center">Clique no &iacute;cone abaixo para salvar a planilha Excel(XLS).
								</td>
								<td align="center">Clique no &iacute;cone abaixo para salvar o pacote de certificados(ZIP).
								</td>
							</tr>
							<tr>
								<td height="66" align="center">
									<span style="vertical-align: bottom;">
										<img src="imagens/xlsdownload_off.png" border="0" width="28" height="33">
										<br>
										<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.id"/>-validacoes.xls
									</span>
								</td>
								<td align="center">
									<span style="vertical-align: bottom;">
										<img src="imagens/certificado_lote1_off.png" border="0" width="28" height="33">
										<br>
										<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.id"/>-certificados.zip
									</span>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">&nbsp;
								</td>
							</tr>
						</table>
					</logic:empty>

					<logic:notEmpty name="uploadArquivosForm" property="listStatusPediValidacao">
						<table width="80%" border="0" align="center" cellpadding="3" cellspacing="5">
							<tr>
								<td colspan="2" align="center">&nbsp;
								</td>
							</tr>
							<tr>
								<td height="40" align="center">Clique no &iacute;cone abaixo para salvar a planilha Excel(XLS).
								</td>
								<td align="center">Clique no &iacute;cone abaixo para salvar o pacote de certificados(ZIP).
								</td>
							</tr>
							<tr>
								<td height="66" align="center">
									<span style="vertical-align: bottom;">
										<a href="javascript:abreResultados();">
											<img src="imagens/xlsdownload.png" border="0" width="28" height="33">
										</a> 
										<br>
										<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.id"/>-validacoes.xls
									</span>
								</td>
								<td align="center">
									<span style="vertical-align: bottom;">
										<a href="javascript:downloadCertificados();">
											<img src="imagens/certificado_lote1.png" border="0" width="28" height="33">
										</a> 
										<br>
										<bean:write 	name="uploadArquivosForm" property="pedValidacaoVO.id"/>-certificados.zip
									</span>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">&nbsp;
								</td>
							</tr>
						</table>
					</logic:notEmpty>
        </TD>
      </TR>
    </TABLE>
    </html:form></td>
  </tr>
  
  
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html:html>
</html>