<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.mastersaf.com.br/paginacao" prefix="p"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<bean:define name="uploadArquivosForm" property="listPedidosValidacao" id="listPedidosValidacao" />
<bean:define name="uploadArquivosForm" property="usuarioVO"	id="usuarioVO" />


<html:html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>UpLoad Arquivos</title>

		<link rel="stylesheet" href="<c:url value="/css/screensmall.css" />" type="text/css" media="screen">
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/estilo.css" />">

		<script src='<c:url value="/js/principal.js" />' language="javascript"></script>

		<!-- Calendar -->
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery-calendar.css" />">

		<script src='<c:url value="/js/jquery.js" />' language="javascript"></script>
		<script src='<c:url value="/js/jquerycalendar.js" />' language="javascript"></script>


		<!-- Fim Calendar -->

		<script src='<c:url value="/js/upload.js" />' language="javascript"></script>



	<script type='text/javascript' src='dwr/interface/PedidoValidacaoAjax.js'></script>
    <script type='text/javascript' src='dwr/engine.js'></script>
    <script type='text/javascript' src='dwr/util.js'></script>

		<script language="javascript">

			function formatarPorcentagem(valor) {

				var str = "" + valor;
				var position = str.indexOf(".");
				
				if(position > 0) {
					return str.substring(0, position);
				} else {
					return valor;
				}
			}

		
			function carregarPorcentagem() {

				var elementos = document.forms[0].porcentagem;
				var flagCompleto = document.forms[0].flagCompleto;

				if(elementos) {
					var idDiv;

					dwr.engine.setErrorHandler(function(){});
					
					DWREngine.beginBatch();
					for(var index=0; index < elementos.length; index++) {
						
						idDiv = "porcentagemText_" + elementos[index].value;

						if(flagCompleto[index].value == "false") {
							pesquisarPorcentagem(idDiv, elementos[index].value, flagCompleto[index]);
						}
					}

					DWREngine.endBatch();
				}
				
			}

			function pesquisarPorcentagem(idDiv, idPedido, elementoFlag) {

				PedidoValidacaoAjax.obterPercentual(idPedido,
					function (result) {
						
						var divPorcentagem = document.getElementById(idDiv); 

						if(result == null) {
							divPorcentagem.innerHTML = "0 %";
						} else {
							divPorcentagem.innerHTML = formatarPorcentagem(result) + " %";
						}

						if(result >= 100) {
							elementoFlag.value = "true";
						}
						
					}
				);
			}
			
			setInterval("carregarPorcentagem();",5000);
		</script>

	</head>
	<body>

	<script language="javascript" type="text/javascript">
	
		$(document).ready(function(){
			$(".data_inicial").calendar({autoPopUp: 'both', buttonImageOnly: true, buttonImage: 'imagens/calendar.gif', buttonText: 'Calendar'});
			$(".data_final").calendar({autoPopUp: 'both', buttonImageOnly: true, buttonImage: 'imagens/calendar.gif', buttonText: 'Calendar'});
		});

		
		function paginar(page) {
			document.getElementById("page").value=page;
			doAction('init');
		}

		function pesquisar() {
			document.getElementById("page").value="0";
			doAction('init');
		}
		
	</script>


	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<html:form action="/UploadArquivosAction.do" method="post" enctype="multipart/form-data">

				<input type="hidden" name="dispatchMethod" id="dispatchMethod" value="" />	

				<input type="hidden" name="page" id="page" value="" />

				<!-- Upload -->
				<div id="table_upload">
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
									<table width="400" border="0" align="center" cellpadding="1"cellspacing="0">
										<tr>
											<td colspan="3">&nbsp;</td>
										</tr>
										<tr>
											<td class="registro01" style="text-align: center; width: 40%;">
												<a href="layout/layout_entrada_msaf_validador.xls"> 
													<img height="42" width="44" border="0" src="Site/images/modeloxls.gif" /> 
													<br>
													LayoutValidador.xls 
												</a>
											</td>
											<td class="registro01">
												<p class="registro01" align="left">
													Baixe o layout padrão para o sistema Validador Mastersaf
												</p>
											</td>
										</tr>
	
										<tr style="height: 70px; vertical-align: bottom; text-align: left;">
											<td colspan="3" class="textocomumleft">
												<div align="left">&nbsp;&nbsp;&nbsp;&nbsp; Por favor, escolha o arquivo para ser analisado</div>
											</td>
										</tr>
										<tr style="text-align: left;">
											<td colspan="3">&nbsp;&nbsp;&nbsp; 
												 
												<html:file property="arquivo" styleClass="efe" title="Arquivo Excel" style="width: 350px;" />
											</td>
										</tr>
										<tr>
											<td colspan="3">&nbsp;</td>
										</tr>
									</table>
	
									<table width="400" border="0" align="center" cellpadding="1" cellspacing="1">
										<tr class="textocomumleft">
											<logic:iterate name="uploadArquivosForm" property="tiposValidacao" id="tipo">
												<td width="170" nowrap="nowrap" style="background-color: #E9F1FEB; background: #CAE4FA;">
													<html:multibox property="tipoSelecionado" styleId="checkBoxTipoPesquisa" onclick="controlaCheckBoxTipoConsulta()">
														<bean:write name="tipo" property="id" />
													</html:multibox>
													<bean:write name="tipo" property="descricao" />
												</td>
											</logic:iterate>
										</tr>
				
										<tr class="textocomumleft" nowrap="nowrap">
											<logic:iterate name="uploadArquivosForm" property="tiposValidacao" id="tipo">
												<td align="center" bgcolor="#EBF4FE">
													<bean:write name="tipo" property="tarifaFk.valorTarifa" format="R$#0.00" />
												</td>
											</logic:iterate>
										</tr>
	
									</table>
	
									<table width="400" border="0" cellpadding="1" cellspacing="1">
										<tr>
											<td style="height: 40px; text-align: center;">
												<input type="button" id="01" value="   Fazer Or&ccedil;amento   " class="ef" onClick="doUploadECotacao();" />
											</td>
										</tr>
				
										<html:messages id="erro" message="true" property="geral">
											<tr>
												<td class="textocomum" style="text-align: letf;">
													- <bean:write name="erro" />
												</td>
											</tr>
										</html:messages>
									</table>
	
									<br>
								</div>
							</TD>
						</TR>
					</TABLE>
				</div>

				<!-- Orçamnto -->
				<div id="table_orcamento">

					<TABLE width="100%" border=1 align="center" cellPadding=0 cellSpacing=0 bordercolor="#003366">
						<TR>
							<TD height=30>
								<TABLE borderColor=#003366 cellSpacing=0 cellPadding=5 width="100%" border=0>
									<TR vAlign=center align=middle>
										<td colspan="3" class="title1">or&ccedil;amento</td>
									</tr>
									<TR vAlign=center align=middle borderColor=#cccccc>
										<td class="title2">quantidade de registros</td>
										<td class="title2">valor</td>
										<td class="title2">&nbsp;</td>
									</TR>
									<TR>
										<td class="registro01">
											<logic:empty name="uploadArquivosForm" property="quantidadeDeRegistros">
												&nbsp;--&nbsp;
											</logic:empty> 
											<logic:notEmpty name="uploadArquivosForm" property="quantidadeDeRegistros">
												<bean:write name="uploadArquivosForm" property="quantidadeDeRegistros" />
											</logic:notEmpty>
										</td>
										<td class="registro01">
											<logic:empty name="uploadArquivosForm" property="valorTotal">
												&nbsp;--&nbsp;
											</logic:empty>
											<logic:notEmpty name="uploadArquivosForm" property="valorTotal">
												<bean:write name="uploadArquivosForm" property="valorTotal" format="R$#0.00" />
											</logic:notEmpty>
										</td>
										<td height="30">
											<div align="center">
												<input type="button" id="02" value="Executar Validação" class="ef" onClick="doProcessamento();" />
												&nbsp;&nbsp;
												<input type="button" id="01" value="Cancelar Or&ccedil;amento" class="ef" onClick="doAction('doCancelCotacao');" />

											</div>
										</td>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</div>

					<c:if test="${uploadArquivosForm.habiliarBtnProcesso}">
						<script language="JavaScript">
							aternarVisualizacao("table_upload","table_orcamento");
						</script>
					</c:if>

					<c:if test="${!uploadArquivosForm.habiliarBtnProcesso}">
						<script language="JavaScript">
							aternarVisualizacao("table_orcamento", "table_upload");
						</script>
					</c:if>


				<div align="center">
					<!-- Registros -->
					<TABLE width="100%" border=1 align="center" cellPadding=0 cellSpacing=0 bordercolor="#003366">
						<TR>
							<TD height=30>
	
								<TABLE width="100%" border=0 align="center" cellPadding=5 cellSpacing=0 borderColor=#003366>
	                				<TR vAlign=center align=middle borderColor=#cccccc>
	                  					<TD colspan="7" bordercolor="#003366" class="title1">registros</TD>
									</TR>

									<tr>
										<TD colspan="7" align="center" class="title2">
											De 
											<input type="text" name="dataInicioSolicitacao" value="${uploadArquivosForm.dataInicioSolicitacao}" class="data_inicial" readonly="readonly" />
											At&eacute; 
											<input type="text" name="dataFimSolicitacao" value="${uploadArquivosForm.dataFimSolicitacao}" class="data_final" readonly="readonly"/>
											<input type="button" class="ef" onClick="pesquisar()" value="pesquisar" />
										</TD>
									</tr>

									<TR valign=center align=middle>
										<TD class="title2">pedido</TD>
										<TD class="title2" style="width: 10%;">qtd.  registros</TD>
										<TD class="title2">tipo de valida&ccedil;&atilde;o</TD>
										<TD class="title2" style="width: 20%;">data da solicita&ccedil;&atilde;o</TD>
										<TD class="title2">% processado</TD>
										<TD class="title2">usuário</TD>
										<TD class="title2">cliente</TD>
									</TR>
	
					                <logic:iterate  name="listPedidosValidacao" id="listPedidosValidacao">
					                
										<TR> 
											<TD class="registro01">
						            	    	<a class="registro01" href="#" onClick="MM_openBrWindow('/msaf.validador.web/UploadArquivosAction.do?dispatchMethod=doPopUp&idPedValidacao=<bean:write name='listPedidosValidacao' property='id'/>','','width=660,height=600')">
						                  			<bean:write name='listPedidosValidacao' property='id'/>
						                  		</a>
											</TD>
						                  
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
												<div id="porcentagemText_<bean:write name='listPedidosValidacao' property='id'/>">
												</div>
												<input type="hidden" value="<bean:write name='listPedidosValidacao' property='id'/>" id="porcentagem" name="porcentagem">
												<input type="hidden" value="false" id="flagCompleto" name="flagCompleto">
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
	
	                				<TR vAlign=center align=middle borderColor="#cccccc" height="40px">
	                  					<TD colspan="7" align="center" class="title2">
											<p:pagina variableJavascript="paginar" styleClassName="pagnum" maxResult="10" pagina="${uploadArquivosForm.page}" quantidadeRegistro="${uploadArquivosForm.quantidadeRegistros}" />
										</TD>
									</TR>
								</table>
							</TD>
						<TR>
							<TD height="5" colspan="6" align="right" valign="middle"><img
								src="spacer.gif" width="1" height="5"></TD>
						</TR>
						<TR>
							<TD colspan="6" align="right" valign="middle" class="title1">LEGENDA</TD>
						</TR>
						<TR>
							<TD colspan="6" align="right" valign="middle">
								<div align="right" class="legenda">
									<div align="left">
										<img src="imagens/processando.gif" width="24" height="24"> 
										Processando <img src="imagens/finalizado.gif" width="24" height="24">
										Finalizado <img src="imagens/erro.gif" width="24" height="24">
										Erro
									</div>
								</div>
							</TD>
						</TR>
					</TABLE>
				</div>
				</html:form>
			</TD>
		</TR>
	</TABLE>

<script languege="javascript">
	carregarPorcentagem();	
</script>

	</body>

</html:html>
