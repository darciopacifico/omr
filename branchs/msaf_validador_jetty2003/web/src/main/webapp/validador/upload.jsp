<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean"prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<bean:define name="uploadArquivosForm" property="listPedidosValidacao" id="listPedidosValidacao"/>
<bean:define name="uploadArquivosForm" property="usuarioVO" id="usuarioVO"/>


 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>UpLoad Arquivos</title>
	

<link href="css/estilo.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="readme_files/screensmall.css" type="text/css" media="screen">
<script src='js/principal.js' language="javascript"></script>
<script src='js/upload.js' language="javascript"></script>

</head>
<body > 


<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td><html:form action="/UploadArquivosAction.do" method="post" enctype="multipart/form-data">
      <TABLE width="100%" border=1 align="center" cellPadding=0 cellSpacing=0 bordercolor="#003366">
        <TR>
          <TD height=30 class="title0">validador</TD>
        </TR>
        <TR>
          <TD height=30 class="title1">upload do arquivo</TD>
        </TR>
        
        

        
        
        <TR>
          <TD height=30>
          	<div align="center">
                	<table width="400" border="0" align="center" cellpadding="1" cellspacing="0">
	                  <tr>
	                    <td colspan="3">&nbsp;</td>
	                  </tr>
	                  
	                  
	                  
						       <tr>
											<td  class="registro01" style="text-align:center; width: 40%;">
												<a href="layout/layout_entrada_msaf_validador.xls">
													<img height="42" width="44" border="0" src="Site/images/modeloxls.gif"/>
													<br>
													LayoutValidador.xls
												</a>
											</td>
											<td  class="registro01">
												<p class="registro01" align="left" >
													Baixe o layout padrão para o sistema Validador Mastersaf
												</p>
											</td>
																			
										</td>

									</tr>
	                  <tr style="height: 70px; vertical-align: bottom; text-align: left;">
	                    <td colspan="3" class="textocomumleft"><div align="left">&nbsp;&nbsp;&nbsp;&nbsp; Por favor, escolha o arquivo para ser analisado</div></td>
	                  </tr>
	                  <tr style="text-align: left;">
	                    <td colspan="3">&nbsp;&nbsp;&nbsp;
	                    <input type="hidden" name="dispatchMethod" id="dispatchMethod" value="" />
											<html:file property="arquivo" styleClass="efe" title="Arquivo Excel" style="width: 350px;"/>
	                  </tr>
	                  <tr>
	                    <td colspan="3">&nbsp;</td>
	                  </tr>
								  </table>
								  <table width="400" border="0" align="center" cellpadding="1" cellspacing="1" >

				
										<tr class="textocomumleft" >
											<logic:iterate name="uploadArquivosForm"
												property="tiposValidacao" id="tipo">
												<td width="170" nowrap="nowrap"  style="background-color:#E9F1FEB; background:#CAE4FA; "  ><html:multibox property="tipoSelecionado">
													<bean:write name="tipo" property="id" />
												</html:multibox><bean:write name="tipo" property="descricao" /></td>
											</logic:iterate>
										</tr>

										<tr class="textocomumleft" nowrap="nowrap"  >
											<logic:iterate name="uploadArquivosForm" property="tiposValidacao" id="tipo">
												<td align="center" bgcolor="#EBF4FE"><bean:write name="tipo" property="tarifaFk.valorTarifa" format="R$#0.00"/></td>
											</logic:iterate>
										</tr>
									
										
                  </table>
                  
                 	<table width="400" border="0" cellpadding="1" cellspacing="1">
	                  <tr>
	                    <td style="height: 40px; text-align: center;">
			                  <input type="button" id="01" value="   Fazer Or&ccedil;amento   " class="ef" onClick="doUploadECotacao();" />
	                    </td>
	                  </tr>
										
										<html:messages id="erro" message="true" property="geral" >
		                  <tr>
		                    <td class="textocomum" style="text-align: letf;">
												- <bean:write name="erro"/>
		                    </td>
		                  </tr>
										</html:messages>
										
									</table>                  
									
									
									
                  <br>
									
                </p>
                </div></TD>
          </TR>
        </TABLE>
      <TABLE width="100%" border=1 align="center" cellPadding=0 cellSpacing=0 bordercolor="#003366">
        <TR>
          <TD height=30><TABLE borderColor=#003366 cellSpacing=0 cellPadding=5 width="100%" border=0>
            <TR vAlign=center align=middle >
              <td colspan="3" class="title1">or&ccedil;amento</td>
            </tr>
            <TR vAlign=center align=middle borderColor=#cccccc>
              <td class="title2"> quantidade de registros</td>
              <td class="title2"> valor</td>
              <td class="title2">&nbsp;</td>
            </TR>
            <TR>
              <td class="registro01">
              	<logic:empty name="uploadArquivosForm" property="quantidadeDeRegistros">
					&nbsp;--&nbsp;
				</logic:empty>
				<logic:notEmpty name="uploadArquivosForm" property="quantidadeDeRegistros">
					<bean:write name="uploadArquivosForm" property="quantidadeDeRegistros"/>
				</logic:notEmpty>
              </td>
              <td class="registro01">
              	<logic:empty name="uploadArquivosForm" property="valorTotal">
					&nbsp;--&nbsp;
				</logic:empty>
				<logic:notEmpty name="uploadArquivosForm" property="valorTotal">
					<bean:write name="uploadArquivosForm" property="valorTotal" format="R$#0.00"/>
				</logic:notEmpty>
			  </td>
              <td height="30"><div align="center">
                <input type="button" id="02" value="   Executar Validação   " class="ef" onClick="doProcessamento();" />
              </div></td>
            </TR>
            </TABLE></TD>
        </TR>
        </TABLE>
      <div align="center">
      <TABLE width="100%" border=1 align="center" cellPadding=0 cellSpacing=0 bordercolor="#003366">
        <TR>
          <TD height=30><TABLE width="100%" border=0 align="center" cellPadding=5 cellSpacing=0 borderColor=#003366>
                <TR vAlign=center align=middle borderColor=#cccccc>
                  <TD colspan="6" bordercolor="#003366" class="title1">registros</TD>
                <TR valign=center align=middle>
                  <TD class="title2">pedido</TD>
                  <%--
                  <TD class="title2">status</TD>
                  --%>
                  <TD class="title2" style="width: 10%;">qtd.  registros</TD>
				  <TD class="title2">tipo de valida&ccedil;&atilde;o</TD>
                  <TD class="title2" style="width: 40%;">data da solicita&ccedil;&atilde;o</TD>
				  <TD class="title2">usuário</TD>
                  <TD class="title2">cliente</TD>
                  <%--
                  <TD class="title2">processo</TD>
                  --%>
                </TR>
                <logic:iterate  name="listPedidosValidacao" id="listPedidosValidacao">
                
	                <TR> 
	                  <TD class="registro01">
	            	                  <a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
	                  		<bean:write name='listPedidosValidacao' property='id'/>
	                  	</a>
	                  	
	                  </TD>
	                  <%--
	                  <TD class="registro01">
	                  
	                  <a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
	                  		<logic:empty name="listPedidosValidacao" property="dataTermino">
													<img src="imagens/processando.gif" width="16" height="16" border="0">
												</logic:empty>
												<logic:notEmpty name="listPedidosValidacao" property="dataTermino">
													<img src="imagens/finalizado.gif" width="16" height="16" border="0">
												</logic:notEmpty>
	                  	</a>
	                  </TD>
	                  --%>
	                  
	                  
	                  <TD class="registro01">
						<a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
	                  		<bean:write name="listPedidosValidacao" property="qtdeRegistrosArq"/>
	                  	</a>
	                  </TD>


	                  <TD class="registro01">
						<a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
							<bean:write name="listPedidosValidacao" property="listaValidacaoFormatada"/>
	                  	</a>
	                  </TD>


	                  <TD class="registro01">
						<a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
	                  		<bean:write name="listPedidosValidacao" property="dataSolicitacao" format="dd/MM/yyyy HH:mm:ss" />
	                  	</a>
	                  </TD>

	                  <TD class="registro01">
						<a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
	                  		<bean:write name="usuarioVO" property="login" />
	                  	</a>
	                  </TD>

	                  <TD class="registro01">
	                  <a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
	                  		<bean:write name="listPedidosValidacao" property="clienteFk.nome"/>
	                  	</a>	
	                  </TD>
	                </TR>
	                
                </logic:iterate>
                <TR>
                  <TD height="5" colspan="6" align="right" valign="middle"><img src="spacer.gif" width="1" height="5"></TD>
                </TR>
                <TR>
                  <TD colspan="6" align="right" valign="middle" class="title1">LEGENDA</TD>
                </TR>
                <TR>
                  <TD colspan="6" align="right" valign="middle"><div align="right" class="legenda">
                    <div align="left">
                    	<img src="imagens/processando.gif" width="24" height="24"> Processando 
                    	<img src="imagens/finalizado.gif" width="24" height="24"> Finalizado 
                    	<img src="imagens/erro.gif" width="24" height="24"> Erro
                    </div>
                  </div></TD>
                </TR>
            </TABLE></TD>
          </TR>
        </TABLE>
		</div>
		
		
		
		
    </html:form></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html:html>
</html>