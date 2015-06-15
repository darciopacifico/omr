<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%> 
<%@page import="org.apache.struts.taglib.html.Constants" %>
<html:form action="/pedvalidacao/pedvalidacao" > 

	<%-- coloca baseCadastroFormVLH no escopo para o funcionamento do VLH --%>
	<bean:define toScope="request" id="baseCadastroFormVLH" name="pedvalidacaoForm" />

	<%-- coloca o form atual no id de request formTelaPesquisa para o 
	funcionamento da tela de pesquisa generica--%>
	<bean:define toScope="request" id="formTelaPesquisa" name="pedvalidacaoForm" />
	<%-- Guarda a p�gina atual do VLH --%>
	<html:hidden property="paginaVLH"/>

<%-- Guarda o status atual da area de pesquisa --%>
<%
String statusAreaPesquisa = request.getParameter("statusAreaPesquisa");
if ( statusAreaPesquisa==null)
	statusAreaPesquisa="block"; 
%>
<input type="hidden" name="statusAreaPesquisa" id="statusAreaPesquisa" value="<%=statusAreaPesquisa%>"/>
	<tiles:insert page="/tilesTemplate/base/baseCadastroFormAtributos.jsp"/>

	<tr >
		<td><img src="imagens/transp.gif" width="1" height="7px"></td>
	</tr>
	<tr>
		<td class="verAz11b"><%
if ( statusAreaPesquisa.equals("block") ) { %>
	  <img border='0' id="imgExpand" style='cursor:hand' src="../assets/menosVermelho.gif" alt="Ocultar area de pesquisa"  onclick="javascript:ocultaPesquisa();"></img>
<%} else { %>
	  <img border='0' id="imgExpand" style='cursor:hand' src="../assets/maisVermelho.gif"  alt="Expandir area de pesquisa" onclick="javascript:ocultaPesquisa();"></img>
<%} %>
	  &nbsp;Filtro para pesquisa
</td>
	</tr>
	<tr bgcolor="#FFCC66">
		<td><img src="imagens/transp.gif" width="1" height="1"></td>
	</tr>

	<tr>
	 <td class="neutro">

 <div border="0" id="areaPesquisa" style="width:100%; height:100%; overflow-x:auto; overflow-y:auto; display:<%=statusAreaPesquisa%>;">

	  <table cellpadding="0" cellspacing="0" border="0" width="100%">

				<tr>
					<td class="tt2R"  width="30%"  nowrap="nowrap" title="<bean:message key="PedValidacao.id.dica"/>" >
						<bean:message key="PedValidacao.id.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="PedValidacao.id.dica"/>" >
						<html:text property='pedValidacaoVOWrapper.pesquisaId' styleClass='cc0' altKey='PedValidacao.id.dica' titleKey="PedValidacao.id.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='onlyNum();' onchange='' />				
					</td>
				</tr>

				<tr>
					<td class="tt2R" nowrap="nowrap" width="30%" title="<bean:message key="PedValidacao.dataDownload.dica"/>" >
						<bean:message key="PedValidacao.dataDownload.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="PedValidacao.dataDownload.dica"/>"> 
						<bean:message key="pesquisa.padrao.labelDe"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaDataDownloadDe' styleClass='cc0' altKey='PedValidacao.dataDownload.dica' titleKey="PedValidacao.dataDownload.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.pesquisaDataDownloadDe\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  

						<bean:message key="pesquisa.padrao.labelAte"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaDataDownloadAte' styleClass='cc0' altKey='PedValidacao.dataDownload.dica' titleKey="PedValidacao.dataDownload.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.pesquisaDataDownloadAte\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  
					</td>
				</tr>

				<tr>
					<td class="tt2R" nowrap="nowrap" width="30%" title="<bean:message key="PedValidacao.dataSolicitacao.dica"/>" >
						<bean:message key="PedValidacao.dataSolicitacao.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="PedValidacao.dataSolicitacao.dica"/>"> 
						<bean:message key="pesquisa.padrao.labelDe"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaDataSolicitacaoDe' styleClass='cc0' altKey='PedValidacao.dataSolicitacao.dica' titleKey="PedValidacao.dataSolicitacao.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.pesquisaDataSolicitacaoDe\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  

						<bean:message key="pesquisa.padrao.labelAte"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaDataSolicitacaoAte' styleClass='cc0' altKey='PedValidacao.dataSolicitacao.dica' titleKey="PedValidacao.dataSolicitacao.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.pesquisaDataSolicitacaoAte\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  
					</td>
				</tr>

				<tr>
					<td class="tt2R" nowrap="nowrap" width="30%" title="<bean:message key="PedValidacao.dataTermino.dica"/>" >
						<bean:message key="PedValidacao.dataTermino.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="PedValidacao.dataTermino.dica"/>"> 
						<bean:message key="pesquisa.padrao.labelDe"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaDataTerminoDe' styleClass='cc0' altKey='PedValidacao.dataTermino.dica' titleKey="PedValidacao.dataTermino.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.pesquisaDataTerminoDe\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  

						<bean:message key="pesquisa.padrao.labelAte"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaDataTerminoAte' styleClass='cc0' altKey='PedValidacao.dataTermino.dica' titleKey="PedValidacao.dataTermino.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.pesquisaDataTerminoAte\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  
					</td>
				</tr>

				<tr>
					<td class="tt2R" nowrap="nowrap" width="30%" title="<bean:message key="PedValidacao.qtdeRegistrosArq.dica"/>" >
						<bean:message key="PedValidacao.qtdeRegistrosArq.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="PedValidacao.qtdeRegistrosArq.dica"/>"> 
						<bean:message key="pesquisa.padrao.labelDe"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaQtdeRegistrosArqDe' styleClass='cc0' altKey='PedValidacao.qtdeRegistrosArq.dica' titleKey="PedValidacao.qtdeRegistrosArq.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='onlyNum();' onchange='' />				

						<bean:message key="pesquisa.padrao.labelAte"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaQtdeRegistrosArqAte' styleClass='cc0' altKey='PedValidacao.qtdeRegistrosArq.dica' titleKey="PedValidacao.qtdeRegistrosArq.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='onlyNum();' onchange='' />				
					</td>
				</tr>

				<tr>
					<td class="tt2R" nowrap="nowrap" width="30%" title="<bean:message key="PedValidacao.qtdeRegistrosProc.dica"/>" >
						<bean:message key="PedValidacao.qtdeRegistrosProc.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="PedValidacao.qtdeRegistrosProc.dica"/>"> 
						<bean:message key="pesquisa.padrao.labelDe"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaQtdeRegistrosProcDe' styleClass='cc0' altKey='PedValidacao.qtdeRegistrosProc.dica' titleKey="PedValidacao.qtdeRegistrosProc.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='onlyNum();' onchange='' />				

						<bean:message key="pesquisa.padrao.labelAte"/>
						<html:text property='pedValidacaoVOWrapper.pesquisaQtdeRegistrosProcAte' styleClass='cc0' altKey='PedValidacao.qtdeRegistrosProc.dica' titleKey="PedValidacao.qtdeRegistrosProc.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='onlyNum();' onchange='' />				
					</td>
				</tr>

				<tr>
					<td class="tt2R"  width="30%"  nowrap="nowrap" title="<bean:message key="PedValidacao.clienteVO.dica"/>" >
						<bean:message key="PedValidacao.clienteVO.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="PedValidacao.clienteVO.dica"/>" >
						<html:select property='pedValidacaoVOWrapper.pesquisaCliente' styleClass='cc0' altKey='PedValidacao.clienteVO.dica' titleKey="PedValidacao.clienteVO.dica" >
				 <html:option value="-1">		<bean:message key="pesquisa.padrao.labelTodos"/> </html:option>
				<logic:iterate id="clienteVO" name="<%=Constants.BEAN_KEY%>" property="pedValidacaoVOWrapper.listClienteVOs" indexId="idDaVez" >
					<html:option value="<%=""+idDaVez%>">
						<bean:write name="clienteVO"/>
					</html:option>
				</logic:iterate>
			</html:select>
					</td>
				</tr>

		<tr>
			<td colspan="2" class="tt1R"  nowrap="nowrap" title="<bean:message key="PedValidacao.clienteVO.dica"/>">
				<html:button property="btnPesquisar" styleClass="bb1" onclick="javascript:pesquisar();travarBotao(this);"  accesskey="p" >
					<bean:message key="pesquisa.padrao.btnpesquisar"/>
				</html:button>
			</td>
		</tr>
	  </table>
</div>

	 </td>
	</tr>
	<tr>
	 <td class="neutro">
	  <table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
		<td class="tt0L" width="1%">
			&nbsp;<!--bot�o alterar-->
		</td>
		<td class="tt0L" width="1%">
			&nbsp;<!--bot�o excluir-->
		</td>
		<td class="tt0L" width="1%">
			&nbsp;<!--bot�o consultar-->
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="PedValidacao.id.dica"/>" >
			<bean:message key="PedValidacao.id.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[0].campo' value='id' />
			<html:hidden property='IVOWrapper.webQueryOrder[0].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[0].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(0,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="PedValidacao.dataDownload.dica"/>" >
			<bean:message key="PedValidacao.dataDownload.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[1].campo' value='dataDownload' />
			<html:hidden property='IVOWrapper.webQueryOrder[1].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[1].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(1,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="PedValidacao.dataSolicitacao.dica"/>" >
			<bean:message key="PedValidacao.dataSolicitacao.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[2].campo' value='dataSolicitacao' />
			<html:hidden property='IVOWrapper.webQueryOrder[2].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[2].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(2,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="PedValidacao.dataTermino.dica"/>" >
			<bean:message key="PedValidacao.dataTermino.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[3].campo' value='dataTermino' />
			<html:hidden property='IVOWrapper.webQueryOrder[3].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[3].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(3,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="PedValidacao.qtdeRegistrosArq.dica"/>" >
			<bean:message key="PedValidacao.qtdeRegistrosArq.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[4].campo' value='qtdeRegistrosArq' />
			<html:hidden property='IVOWrapper.webQueryOrder[4].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[4].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(4,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="PedValidacao.qtdeRegistrosProc.dica"/>" >
			<bean:message key="PedValidacao.qtdeRegistrosProc.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[5].campo' value='qtdeRegistrosProc' />
			<html:hidden property='IVOWrapper.webQueryOrder[5].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[5].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(5,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="PedValidacao.clienteVO.dica"/>" >
			<bean:message key="PedValidacao.clienteVO.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[6].campo' value='clienteVO' />
			<html:hidden property='IVOWrapper.webQueryOrder[6].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[6].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(6,this);'>
		</td>

	</tr>
	<tr bgcolor="#FFCC66">
		<td colspan="50" ><img src="imagens/transp.gif" width="1" height="1"></td>
	</tr>
	<%-- INCREMENTADOR PARA ID ITEM SELECIONADO --%>
	<%int i=0;%>
	<logic:iterate id="basevo" name="formTelaPesquisa"  property="resultadoPesquisa" >
	<tr>
		<td class="tt2C"  nowrap="nowrap">
			<img 
			border='0' 
			style='cursor:hand' 
			src="../assets/img_e.gif" 
			alt="Editar" 
			onclick="javascript:preparaAlterar(<%=i%>);"></img>
		</td>								
		<td class="tt2C"  nowrap="nowrap">
			<img 
			border='0' 
			style='cursor:hand' 
			src="../assets/img_x.gif" 
			alt="Excluir" 
			onclick="javascript:excluir(<%=i%>);"></img>
		</td>
		<td class="tt2C" nowrap="nowrap">
			<img 
			border='0' 
			style='cursor:hand' 
			src="../assets/img_c.gif" 
			alt="Consultar" 
			onclick="javascript:preparaConsultar(<%=i%>);"></img>
		</td>								

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='id' ignore='true' formatKey='formatoInteger' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='dataDownload' ignore='true' formatKey='formatoDataView' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='dataSolicitacao' ignore='true' formatKey='formatoDataView' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='dataTermino' ignore='true' formatKey='formatoDataView' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='qtdeRegistrosArq' ignore='true' formatKey='formatoInteger' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='qtdeRegistrosProc' ignore='true' formatKey='formatoInteger' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='clienteVO' ignore='true' name='basevo' />				&nbsp;
		</td >

	</tr>
	<%i++;%>
	</logic:iterate>

		</tr>
	  </table>
	 </td>
    </tr>

    <script language="javascript">
    var firstElement = document.pedvalidacaoForm.elements["pedValidacaoVOWrapper.pesquisaId"];
    firstElement.focus();
    </script>

</html:form>