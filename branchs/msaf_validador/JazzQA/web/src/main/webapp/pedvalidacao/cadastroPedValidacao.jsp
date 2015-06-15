<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%> 
<%@page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="br.com.dlp.validador.pedvalidacao.PedValidacaoAction" %>

<%@page import="br.com.dlp.validador.cliente.ClienteAction" %>

<%int index=0;%>

<html:form action="/pedvalidacao/pedvalidacao" onsubmit="salvar();"> 

	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tiles:insert page="/tilesTemplate/base/baseCadastroFormAtributos.jsp"/>
	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tr>
	 <td class="neutro">
	  <table cellpadding="0" cellspacing="0" border="0" width="100%">

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.id.dica"/>" >
			<bean:message key="PedValidacao.id.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.id.dica"/>" >&nbsp;
			<bean:write name='<%=Constants.BEAN_KEY%>' property='pedValidacaoVOWrapper.id' />				
		</td>
       </tr>		




	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.dataDownload.dica"/>" >
			<bean:message key="PedValidacao.dataDownload.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.dataDownload.dica"/>" >&nbsp;
			<html:text property='pedValidacaoVOWrapper.dataDownload' styleClass='cc0' altKey='PedValidacao.dataDownload.dica' titleKey="PedValidacao.dataDownload.dica" readonly='false' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.dataDownload\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.dataSolicitacao.dica"/>" >
			<bean:message key="PedValidacao.dataSolicitacao.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.dataSolicitacao.dica"/>" >&nbsp;
			<html:text property='pedValidacaoVOWrapper.dataSolicitacao' styleClass='cc0' altKey='PedValidacao.dataSolicitacao.dica' titleKey="PedValidacao.dataSolicitacao.dica" readonly='false' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.dataSolicitacao\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.dataTermino.dica"/>" >
			<bean:message key="PedValidacao.dataTermino.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.dataTermino.dica"/>" >&nbsp;
			<html:text property='pedValidacaoVOWrapper.dataTermino' styleClass='cc0' altKey='PedValidacao.dataTermino.dica' titleKey="PedValidacao.dataTermino.dica" readonly='false' size='13' onkeypress='formataData(this.name);' onblur='validaData(this.value, 5)' />				
			 <a href="javascript: void(0);" 
 				 onmouseover="if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;" 
 				 onmouseout="if (timeoutDelay) calendarTimeout();window.status='';"  
 				 onclick='g_Calendar.show(event,"pedvalidacaoForm.elements[\"pedValidacaoVOWrapper.dataTermino\"]", false, "dd/mm/yyyy", "",""); return false;'> 
 				 <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  
		</td>
       </tr>		


	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.qtdeRegistrosArq.dica"/>" >
			<bean:message key="PedValidacao.qtdeRegistrosArq.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.qtdeRegistrosArq.dica"/>" >&nbsp;
			<html:text property='pedValidacaoVOWrapper.qtdeRegistrosArq' styleClass='cc0' altKey='PedValidacao.qtdeRegistrosArq.dica' titleKey="PedValidacao.qtdeRegistrosArq.dica" readonly='false' size='13' onkeypress='onlyNum();' onchange='' />				
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.qtdeRegistrosProc.dica"/>" >
			<bean:message key="PedValidacao.qtdeRegistrosProc.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.qtdeRegistrosProc.dica"/>" >&nbsp;
			<html:text property='pedValidacaoVOWrapper.qtdeRegistrosProc' styleClass='cc0' altKey='PedValidacao.qtdeRegistrosProc.dica' titleKey="PedValidacao.qtdeRegistrosProc.dica" readonly='false' size='13' onkeypress='onlyNum();' onchange='' />				
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.clienteVO.dica"/>" >
			<bean:message key="PedValidacao.clienteVO.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.clienteVO.dica"/>" >&nbsp;
			<bean:write name='<%=Constants.BEAN_KEY%>' property='pedValidacaoVOWrapper.baseVO.clienteVO' />				
		</td>
       </tr>		

	  </table>
	 </td>
	</tr>

    <script language="javascript">
    var firstElement = document.pedvalidacaoForm.elements["pedValidacaoVOWrapper.dataDownload"];
    firstElement.focus();
    </script>

</html:form>