<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%> 
<%@page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="br.com.dlp.validador.pedvalidacao.PedValidacaoAction" %>

<%-- UTILIZADO NA ITERACAO DE LISTAS INDEXADAS --%>
<%int index=0;%>

<html:form action="/pedvalidacao/pedvalidacao" > 

	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tiles:insert page="/tilesTemplate/base/baseCadastroFormAtributos.jsp"/>
	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tr>
	 <td class="neutro">
	  <table cellpadding="0" cellspacing="0" border="0" width="100%">

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.id.dica"/>">
			<bean:message key="PedValidacao.id.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.id.dica"/>" >&nbsp;
			<bean:write property='pedValidacaoVOWrapper.id' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.dataDownload.dica"/>">
			<bean:message key="PedValidacao.dataDownload.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.dataDownload.dica"/>" >&nbsp;
			<bean:write property='pedValidacaoVOWrapper.dataDownload' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.dataSolicitacao.dica"/>">
			<bean:message key="PedValidacao.dataSolicitacao.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.dataSolicitacao.dica"/>" >&nbsp;
			<bean:write property='pedValidacaoVOWrapper.dataSolicitacao' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.dataTermino.dica"/>">
			<bean:message key="PedValidacao.dataTermino.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.dataTermino.dica"/>" >&nbsp;
			<bean:write property='pedValidacaoVOWrapper.dataTermino' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.qtdeRegistrosArq.dica"/>">
			<bean:message key="PedValidacao.qtdeRegistrosArq.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.qtdeRegistrosArq.dica"/>" >&nbsp;
			<bean:write property='pedValidacaoVOWrapper.qtdeRegistrosArq' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.qtdeRegistrosProc.dica"/>">
			<bean:message key="PedValidacao.qtdeRegistrosProc.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.qtdeRegistrosProc.dica"/>" >&nbsp;
			<bean:write property='pedValidacaoVOWrapper.qtdeRegistrosProc' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="PedValidacao.clienteVO.dica"/>">
			<bean:message key="PedValidacao.clienteVO.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="PedValidacao.clienteVO.dica"/>" >&nbsp;
			<bean:write property='pedValidacaoVOWrapper.pedValidacaoVO.clienteVO' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	  </table>
	 </td>
	</tr>
</html:form>