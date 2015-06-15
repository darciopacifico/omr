<!doctype html public "-//w3c//dtd html 4.0 transitional//en" >
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html>
<head>
<link href="css/diPepsico.css" rel="stylesheet" type="text/css" />

<script language="javascript">
//script work around para cobrir a deficiencia de implementar um tipo radio em uma tabela do jsf
function orientaRadio(obj, listagem){
	var radios = document.getElementsByTagName("input");
	for(var x=0; x<radios.length;x++){
		if(radios[x].type == "radio"){
			if(radios[x].id == obj.id){
				radios[x].checked = true;
				setarControleRadio(obj.value, listagem);
			}else{
				if(listagem == 'listaItens'){
					var idProvisorio = obj.id;
					var partesId = idProvisorio.split(":");
					if(partesId.length > 3){
						if(partesId[3] == "radioItens"){
							radios[x].checked = false;
						}
					}
				}
				if(listagem == 'listaAdis'){
					var idProvisorioAdi = obj.id;
					var partesIdAdi = idProvisorioAdi.split(":");
					if(partesIdAdi.length > 3){
						if(partesIdAdi[3] == "radioAdis"){
							radios[x].checked = false;
						}
					}
				}
			}
		}
	}
}

function setarControleRadio(valor, listagem){
	var escondidos = document.getElementsByTagName("input");
	for(var y=0; y<escondidos.length; y++){
		if(escondidos[y].type == "hidden"){
			var idProvisorio = escondidos[y].id;
			var partesId = idProvisorio.split(":");
			if(partesId.length > 1){
				if(partesId[1] == "controleRadio"){
					if(listagem == 'listaItens'){
						escondidos[y].value = valor;
					}
				}else if(partesId[1] == "controleRadioADI"){
					if(listagem == 'listaAdis'){
						escondidos[y].value = valor;
					}
				}
			}
		}
	}
}

//funcoes para modal
function getRightTop(ref) {
    var position = new Object();
   position.top = 0; //ref.offsetTop;
   position.left =0; // ref.offsetLeft+ref.clientWidth+6;
   return position;
}

</script>

<style>
.fontContentTable{
	height:10px;
	font-family: sans-serif;
	color:#000000;
	font-size: 10px;
	font-stretch: normal;
	font-weight: normal;
	cursor:pointer;
	text-decoration: none;
}
</style>

</head>
<f:view>
<body leftmargin="0" rightmargin="0" topmargin="0">



	<a4j:form id="fomrPage">

		<!-- hiddens de apoio -->
		<h:inputHidden id="controleRadio" value="#{itemMBean.idsItem}"></h:inputHidden>
		

		<!--  regiao do cabecalho do sistema -->
		<rich:toolBar styleClass="toolBarHeader" width="100%">
			<rich:toolBarGroup location="left">
				<h:graphicImage url="img/bg/bg_logoHeader.png" height="27"></h:graphicImage>
			</rich:toolBarGroup>
			<rich:toolBarGroup>
				<h:outputText value="DI-PEPSICO"
					style="font-size:18px;font-weight:bold;"></h:outputText>
			</rich:toolBarGroup>
			<rich:toolBarGroup location="right">
				<h:commandButton title="Página Principal" image="img/bt/bt_home.png"
					action="#{itemMBean.voltar}"></h:commandButton>
				<h:commandLink title="Página Principal" styleClass="fontLinkHeader"
					action="#{itemMBean.voltar}">Página Principal</h:commandLink>
				<h:commandButton title="Logout" image="img/bt/bt_logout.png"
					action="#{loginMBean.logout}"></h:commandButton>
				<h:outputText title="Nome do usuário" styleClass="fontLinkHeader"
					value="#{nfeMBean.nmUsuario}"></h:outputText>
				<h:commandLink title="Logout" styleClass="fontLinkHeader"
					action="#{loginMBean.logout}">[Logout]</h:commandLink>
			</rich:toolBarGroup>
		</rich:toolBar>
		<!--  fim do cabecalho do sistema -->

		<table width="100%">
			<tr>
				<td align="center"><rich:tabPanel width="99%"
					style="text-align:center">
					<rich:tab label="Tela Lista de Itens e DI">
						<rich:toolBar>
							<rich:toolBarGroup>
								<a4j:commandButton title="Listar ADI do Item/DI"
									image="img/bt/bt_adi.png" style="cursor:pointer;"
									action="#{itemMBean.listarAdi}" reRender="errorMessageModalADI,mpEditaDI"
									oncomplete="#{rich:component('mpEditaDI')}.show()"
									rendered="#{itemMBean.mostrarListarADI}" />
								<a4j:commandButton title="Reenviar"
									image="img/bt/bt_reenviar.png" style="cursor:pointer;"
									action="#{nfeMBean.reenviarPorItem}" reRender="errorMessage,sc2">
									<f:param name="idNFPorItem" value="#{itemMBean.idNFE}" />
									<f:param name="idEmpresaPorItem" value="#{itemMBean.idEmpresa}" />
									</a4j:commandButton>
								<h:commandButton title="Voltar" image="img/bt/bt_voltar.png"
									style="cursor:pointer;" action="#{itemMBean.voltar}" />
							</rich:toolBarGroup>
							<rich:toolBarGroup location="right">
								<h:outputText styleClass="fontToolBar" value="Número Empresa: "></h:outputText>
								<h:inputText value="#{itemMBean.empresa.idEmpresa}"
									readonly="true"></h:inputText>

								<h:outputText styleClass="fontToolBar" value="Empresa: "></h:outputText>
								<h:inputText value="#{itemMBean.empresa.nome}"
									style="width:200px;" readonly="true"></h:inputText>

								<h:outputText styleClass="fontToolBar" value="Número Nota: "></h:outputText>
								<h:inputText value="#{itemMBean.nrNota}" readonly="true"></h:inputText>

							</rich:toolBarGroup>
						</rich:toolBar>
						<rich:dataTable id="itemList" width="100%" rows="10"
							value="#{itemMBean.all}" var="itemDi" headerClass="grayHead"
							styleClass="tableClass" cellpadding="0" cellspacing="0"
							rowClasses="linhaTabela" columnClasses="colunaTabela"
							onRowMouseOver="this.style.backgroundColor='#e1e1e1'"
							onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'">
							<f:facet name="header">
								<rich:columnGroup>
									<rich:column width="3%">
										<h:outputText styleClass="fontHeaderTable" value=" " />
									</rich:column>
									<rich:column width="15%">
										<h:outputText styleClass="fontHeaderTable"
											value="Número do Item" />
									</rich:column>
									<rich:column width="15%">
										<h:outputText styleClass="fontHeaderTable"
											value="Número do Documento" />
									</rich:column>
									<rich:column width="17%">
										<h:outputText styleClass="fontHeaderTable"
											value="Data de Registro" />
									</rich:column>
									<rich:column width="20%">
										<h:outputText styleClass="fontHeaderTable"
											value="Local Desembaraço" />
									</rich:column>
									<rich:column width="10%">
										<h:outputText styleClass="fontHeaderTable" value="UF" />
									</rich:column>
									<rich:column width="20%">
										<h:outputText styleClass="fontHeaderTable"
											value="Data Des.Aduaneiro" />
									</rich:column>
								</rich:columnGroup>
							</f:facet>
							<rich:column>
								<h:selectOneRadio id="radioItens"
									onclick="javascript:orientaRadio(this, 'listaItens');">
									<f:selectItem itemValue="#{itemDi.diItemNFEPK.idItem}|:#{itemDi.diItemNFEPK.nrDocumento}|:#{itemDi.diItemNFEPK.idEmpresa}|:#{itemDi.diItemNFEPK.idNf}" />
								</h:selectOneRadio>
							</rich:column>
							<rich:column>
								<a4j:commandLink styleClass="fontContentTable"
									action="#{itemMBean.editarDI}"
									oncomplete="#{rich:component('mpEditaDI')}.show();"
									value="#{itemDi.diItemNFEPK.idItem}" reRender="mpEditaDI">
									<f:param name="nrDocumentoDi" value="#{itemDi.diItemNFEPK.nrDocumento}" />
									<f:param name="idItemDi" value="#{itemDi.diItemNFEPK.idItem}" />
									<f:param name="idEmpresaDi" value="#{itemDi.diItemNFEPK.idEmpresa}" />
									<f:param name="idNFDi" value="#{itemDi.diItemNFEPK.idNf}" />
								</a4j:commandLink>
							</rich:column>
							<rich:column>
								<a4j:commandLink styleClass="fontContentTable"
									action="#{itemMBean.editarDI}"
									oncomplete="#{rich:component('mpEditaDI')}.show();"
									value="#{itemDi.diItemNFEPK.nrDocumento}" reRender="mpEditaDI">
									<f:param name="nrDocumentoDi" value="#{itemDi.diItemNFEPK.nrDocumento}" />
									<f:param name="idItemDi" value="#{itemDi.diItemNFEPK.idItem}" />
									<f:param name="idEmpresaDi" value="#{itemDi.diItemNFEPK.idEmpresa}" />
									<f:param name="idNFDi" value="#{itemDi.diItemNFEPK.idNf}" />
								</a4j:commandLink>
							</rich:column>
							<rich:column>
								<a4j:commandLink styleClass="fontContentTable"
									action="#{itemMBean.editarDI}"
									oncomplete="#{rich:component('mpEditaDI')}.show();"
									value="#{itemDi.registro}" reRender="mpEditaDI">
									<f:param name="nrDocumentoDi" value="#{itemDi.diItemNFEPK.nrDocumento}" />
									<f:param name="idItemDi" value="#{itemDi.diItemNFEPK.idItem}" />
									<f:param name="idEmpresaDi" value="#{itemDi.diItemNFEPK.idEmpresa}" />
									<f:param name="idNFDi" value="#{itemDi.diItemNFEPK.idNf}" />
								</a4j:commandLink>
							</rich:column>
							<rich:column>
								<a4j:commandLink styleClass="fontContentTable"
									action="#{itemMBean.editarDI}"
									oncomplete="#{rich:component('mpEditaDI')}.show();"
									value="#{itemDi.local}" reRender="mpEditaDI">
									<f:param name="nrDocumentoDi" value="#{itemDi.diItemNFEPK.nrDocumento}" />
									<f:param name="idItemDi" value="#{itemDi.diItemNFEPK.idItem}" />
									<f:param name="idEmpresaDi" value="#{itemDi.diItemNFEPK.idEmpresa}" />
									<f:param name="idNFDi" value="#{itemDi.diItemNFEPK.idNf}" />
								</a4j:commandLink>
							</rich:column>
							<rich:column>
								<a4j:commandLink styleClass="fontContentTable"
									action="#{itemMBean.editarDI}"
									oncomplete="#{rich:component('mpEditaDI')}.show();"
									value="#{itemDi.uf}" reRender="mpEditaDI">
									<f:param name="nrDocumentoDi" value="#{itemDi.diItemNFEPK.nrDocumento}" />
									<f:param name="idItemDi" value="#{itemDi.diItemNFEPK.idItem}" />
									<f:param name="idEmpresaDi" value="#{itemDi.diItemNFEPK.idEmpresa}" />
									<f:param name="idNFDi" value="#{itemDi.diItemNFEPK.idNf}" />
								</a4j:commandLink>
							</rich:column>
							<rich:column>
								<a4j:commandLink  styleClass="fontContentTable"
									action="#{itemMBean.editarDI}"
									oncomplete="#{rich:component('mpEditaDI')}.show();"
									value="#{itemDi.saida}" reRender="mpEditaDI">
									<f:param name="nrDocumentoDi" value="#{itemDi.diItemNFEPK.nrDocumento}" />
									<f:param name="idItemDi" value="#{itemDi.diItemNFEPK.idItem}" />
									<f:param name="idEmpresaDi" value="#{itemDi.diItemNFEPK.idEmpresa}" />
									<f:param name="idNFDi" value="#{itemDi.diItemNFEPK.idNf}" />
								</a4j:commandLink>
							</rich:column>


						</rich:dataTable>
						<rich:datascroller align="center" for="itemList" maxPages="50"
							id="sc2" />

						<rich:spacer height="5" />
						<span class="sucessMessage"> <a4j:status id="commonstatus"
							startText="Processando informação, aguarde..."
							styleClass="sucessMessage" stopText="" /> </span>
						<br />
						<h:outputText id="errorMessage" value="#{itemMBean.errorMessage}"
							styleClass="#{itemMBean.styleMessage}"></h:outputText>

					</rich:tab>
				</rich:tabPanel></td>
			</tr>
		</table>


		<!-- regiao do rodape do sistema -->
		<rich:spacer height="5" />
		<table width="100%" height="25px" cellpadding="0" cellspacing="0"
			border="0">
			<tr>
				<td bgcolor="#5e96c5" align="left"><img
					src="img/bg/bg_rodape1.png" /></td>
				<td bgcolor="#5e96c5" align="right"><img
					src="img/bg/bg_rodape2.png" /></td>
			</tr>
		</table>

	</a4j:form>
	<!-- fim do rodape do sistema -->








</body>


<!-- modals de edicao -->


	<rich:modalPanel id="mpEditaDI" minHeight="200" minWidth="450"
		height="450" width="650">
		<f:facet name="header">
			<h:outputText value="Editar DI" />
		</f:facet>
		<f:facet name="controls">
		</f:facet>
			<a4j:form id="modalEdita" ajaxSubmit="true">
			
			<!-- primeiro modal -->
			<a4j:outputPanel id="primeiroModal" ajaxRendered="true" layout="block">
			<rich:panel  style="border:0px;" rendered="#{itemMBean.mostrarModal1}">
			<a4j:support reRender="primeiroModal,segundoModal,terceiroModal"></a4j:support>
			
			<h:panelGrid columns="4" style="text-align:center"
				width="590px">
				<h:outputText styleClass="font10" value="Número do Item"></h:outputText>
				<h:outputText styleClass="font10" value="Número do Documento"></h:outputText>
				<h:outputText styleClass="font10" value="Código do Exportador"></h:outputText>
				<h:outputText styleClass="font10" value="Data do Registro"></h:outputText>
				<h:inputText immediate="true" value="#{itemMBean.nrItemModal}"
					styleClass="inputTypes" readonly="true"></h:inputText>
				<h:inputText immediate="true" value="#{itemMBean.nrDocumentoModal}"
					styleClass="inputTypes" maxlength="10"></h:inputText>
				<h:inputText immediate="true" value="#{itemMBean.cdExportadorModal}"
					styleClass="inputTypes" maxlength="60"></h:inputText>
				<rich:calendar value="#{itemMBean.dtRegistroModal}"
                        locale="pt/BR"
                        popup="true"
                        datePattern="dd/MM/yyyy"
                        showApplyButton="false" 
                        cellWidth="24px" cellHeight="22px" style="width:200px;" inputStyle="inputTypes"/>
			</h:panelGrid>
			<h:panelGrid columns="3" style="text-align:center"
				width="590px">
				<h:outputText styleClass="font10" value="Local Desembaraço"></h:outputText>
				<h:outputText styleClass="font10" value="UF"></h:outputText>
				<h:outputText styleClass="font10" value="Data Des. Aduanero"></h:outputText>
				<h:inputText immediate="true" value="#{itemMBean.dsLocalModal}"
					style="width:300px" styleClass="inputTypes" maxlength="60"></h:inputText>
				<h:inputText immediate="true" value="#{itemMBean.cdUfModal}"
					style="width:20px" styleClass="inputTypes" maxlength="2"></h:inputText>
				<rich:calendar value="#{itemMBean.dtAduaneroModal}"
                        locale="pt/BR"
                        popup="true"
                        datePattern="dd/MM/yyyy"
                        showApplyButton="false" 
                        cellWidth="24px" cellHeight="22px" style="width:200px;" inputStyle="inputTypes"/>
			</h:panelGrid>
			<h:panelGrid columns="3" style="text-align:center;" width="590px">
				<a4j:commandButton action="#{itemMBean.salvarDi}" value="Salvar"
					reRender="itemList,sc2,errorMessageModal"></a4j:commandButton>
				<rich:spacer width="10px"></rich:spacer>
				<a4j:commandButton id="fecharDI" action="#{itemMBean.limparMSG}" value="Fechar" reRender="itemList,sc2">
					 <rich:componentControl for="mpEditaDI" attachTo="fecharDI" operation="hide" event="onclick" />
				</a4j:commandButton>
			</h:panelGrid>
			<h:panelGrid columns="1" width="590px">
				<h:outputText id="errorMessageModal"
					value="#{itemMBean.errorMessage}"
					styleClass="#{itemMBean.styleMessage}"></h:outputText>
			</h:panelGrid>
			
			</rich:panel>
			</a4j:outputPanel>
			<!-- fim primeiro modal -->
			
			<!-- segundo modal -->
			<a4j:outputPanel  id="segundoModal" ajaxRendered="true" layout="block">
			<rich:panel  style="border:0px;"  rendered="#{itemMBean.mostrarModal2}">
			<a4j:support reRender="primeiroModal,segundoModal,terceiroModal"></a4j:support>
			<h:inputHidden id="controleRadioADI" value="#{itemMBean.idsAdi}"></h:inputHidden>
		
			<h:panelGrid  width="590">
				<rich:dataTable id="adiList" width="95%" rows="10"
							value="#{itemMBean.adiList}" var="adi" headerClass="grayHead"
							styleClass="tableClass" cellpadding="0" cellspacing="0"
							rowClasses="linhaTabela" columnClasses="colunaTabela"
							onRowMouseOver="this.style.backgroundColor='#e1e1e1'"
							onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
							style="border:1px solid #c0c0c0;">
							
							
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column width="3%">
								<h:outputText styleClass="fontHeaderTable" value=" " />
							</rich:column>
							<rich:column width="32%">
								<h:outputText styleClass="fontHeaderTable"
									value="Número da Adição" />
							</rich:column>
							<rich:column width="32%">
								<h:outputText styleClass="fontHeaderTable"
									value="Código Fabricante Estrangeiro" />
							</rich:column>
							<rich:column width="33%">
								<h:outputText styleClass="fontHeaderTable"
									value="Valor do Desconto" />
							</rich:column>
						</rich:columnGroup>
					</f:facet>
					<rich:column>
						<h:selectOneRadio id="radioAdis"
							onclick="javascript:orientaRadio(this, 'listaAdis');">
							<f:selectItem itemValue="#{adi.aditivoDIItemNFEPK.idAdicao}" />
						</h:selectOneRadio>
					</rich:column>
					<rich:column>
						<a4j:commandLink styleClass="fontContentTable"
							action="#{itemMBean.editaAdi}"
							value="#{adi.aditivoDIItemNFEPK.idAdicao}"
							reRender="primeiroModal,segundoModal,terceiroModal">
							<f:param name="idAdicaoADI" value="#{adi.aditivoDIItemNFEPK.idAdicao}" />
							<f:param name="idEmpresaADI" value="#{adi.aditivoDIItemNFEPK.idEmpresa}" />
							<f:param name="idItemADI" value="#{adi.aditivoDIItemNFEPK.idItem}" />
							<f:param name="idNFADI" value="#{adi.aditivoDIItemNFEPK.idNf}" />
							<f:param name="nrDocumentoADI" value="#{adi.aditivoDIItemNFEPK.nrDocumento}" />
						</a4j:commandLink>
					</rich:column>
					<rich:column>
						<a4j:commandLink styleClass="fontContentTable"
							action="#{itemMBean.editaAdi}"
							value="#{adi.fabricante}"
							reRender="primeiroModal,segundoModal,terceiroModal" >
							<f:param name="idAdicaoADI" value="#{adi.aditivoDIItemNFEPK.idAdicao}" />
							<f:param name="idEmpresaADI" value="#{adi.aditivoDIItemNFEPK.idEmpresa}" />
							<f:param name="idItemADI" value="#{adi.aditivoDIItemNFEPK.idItem}" />
							<f:param name="idNFADI" value="#{adi.aditivoDIItemNFEPK.idNf}" />
							<f:param name="nrDocumentoADI" value="#{adi.aditivoDIItemNFEPK.nrDocumento}" />
						</a4j:commandLink>
					</rich:column>
					<rich:column>
						<a4j:commandLink styleClass="fontContentTable"
							action="#{itemMBean.editaAdi}"
							value="#{adi.desconto}"
							reRender="primeiroModal,segundoModal,terceiroModal">
							<f:param name="idAdicaoADI" value="#{adi.aditivoDIItemNFEPK.idAdicao}" />
							<f:param name="idEmpresaADI" value="#{adi.aditivoDIItemNFEPK.idEmpresa}" />
							<f:param name="idItemADI" value="#{adi.aditivoDIItemNFEPK.idItem}" />
							<f:param name="idNFADI" value="#{adi.aditivoDIItemNFEPK.idNf}" />
							<f:param name="nrDocumentoADI" value="#{adi.aditivoDIItemNFEPK.nrDocumento}" />
						</a4j:commandLink>
					</rich:column>
				</rich:dataTable>
				<rich:datascroller align="center" for="adiList" maxPages="50" id="sc3" />
			</h:panelGrid>
			
			<h:panelGrid columns="3" style="text-align:center;" width="590px">
				<a4j:commandButton id="novoADI"  value="Novo" action="#{itemMBean.novoAdi}" immediate="true" reRender="primeiroModal,segundoModal,terceiroModal" oncomplete="#{rich:component('segundoModal')}.hide();" ></a4j:commandButton>
				<a4j:commandButton  value="Deletar" action="#{itemMBean.deletaAdi}" reRender="errorMessageModalADI,adiList,sc3"></a4j:commandButton>
				<a4j:commandButton id="fecharADIList" action="#{itemMBean.limparMSG}" value="Fechar">
					 <rich:componentControl for="mpEditaDI" attachTo="fecharADIList" operation="hide" event="onclick" />	
				</a4j:commandButton>
			</h:panelGrid>
			<rich:spacer height="10px"></rich:spacer>
			<h:panelGrid columns="1" width="590px">
				<h:outputText id="errorMessageModalADI"
					value="#{itemMBean.errorMessage}"
					styleClass="#{itemMBean.styleMessage}"></h:outputText>
			</h:panelGrid>
		</rich:panel>
		</a4j:outputPanel>
		<!-- fim segundo modal -->
			
			
			<!-- terceiro modal -->
			<a4j:outputPanel  id="terceiroModal" ajaxRendered="true" layout="block">
			<rich:panel style="border:0px;" rendered="#{itemMBean.mostrarModal3}">
			<a4j:support reRender="primeiroModal,segundoModal,terceiroModal"></a4j:support>
			
			<h:panelGrid columns="3" style="text-align:center;" width="590px">
				<h:outputText styleClass="font10" value="Número ADI"></h:outputText>
				<h:outputText styleClass="font10" value="Número Sequencial"></h:outputText>
				<h:outputText styleClass="font10" value="Valor do Desconto"></h:outputText>
				
				<h:inputText immediate="true" value="#{itemMBean.nrAdiModal}"
					styleClass="inputTypes" readonly="true">
					</h:inputText>
				<h:inputText immediate="true" value="#{itemMBean.nrSequencialAdiModal}"
					styleClass="inputTypes" maxlength="3">
					</h:inputText>
				<h:inputText immediate="true" value="#{itemMBean.vlDescontoAdiModal}"
					styleClass="inputTypes" style="width:110px" maxlength="10">
				</h:inputText>
			</h:panelGrid>
			<rich:spacer height="10px"></rich:spacer>
			<h:panelGrid columns="1" style="text-align:center;" width="590px">
				<h:outputText styleClass="font10" value="Código do fabricante Estrangeiro"></h:outputText>
				
				<h:inputText immediate="true" value="#{itemMBean.cdEstrangeiroAdiModal}"
					styleClass="inputTypes" style="width:480px" maxlength="60"></h:inputText>
			</h:panelGrid>
			
			<h:panelGrid columns="3" style="text-align:center;" width="590px" >
				<a4j:commandButton action="#{itemMBean.salvarADI}" value="Salvar" reRender="errorMessageModalEditADI,adiList,sc3"></a4j:commandButton>
				<a4j:commandButton id="voltarADIEdit" action="#{itemMBean.voltarListaADI}" value="Voltar" reRender="primeiroModal,segundoModal,terceiroModal"></a4j:commandButton>
			</h:panelGrid>

			<h:panelGrid columns="1" width="590px">
				<h:outputText id="errorMessageModalEditADI"
					value="#{itemMBean.errorMessage}"
					styleClass="#{itemMBean.styleMessage}"></h:outputText>
			</h:panelGrid>
		</rich:panel>
		</a4j:outputPanel>
			
			
			</a4j:form>
	</rich:modalPanel>


	


</f:view>
</html>