<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%> 
<%@page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="br.com.dlp.validador.cliente.ClienteAction" %>

<%-- UTILIZADO NA ITERACAO DE LISTAS INDEXADAS --%>
<%int index=0;%>

<html:form action="/cliente/cliente" > 

	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tiles:insert page="/tilesTemplate/base/baseCadastroFormAtributos.jsp"/>
	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tr>
	 <td class="neutro">
	  <table cellpadding="0" cellspacing="0" border="0" width="100%">

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="Cliente.id.dica"/>">
			<bean:message key="Cliente.id.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="Cliente.id.dica"/>" >&nbsp;
			<bean:write property='clienteVOWrapper.id' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="Cliente.cnpj.dica"/>">
			<bean:message key="Cliente.cnpj.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="Cliente.cnpj.dica"/>" >&nbsp;
			<bean:write property='clienteVOWrapper.cnpj' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="Cliente.nome.dica"/>">
			<bean:message key="Cliente.nome.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="Cliente.nome.dica"/>" >&nbsp;
			<bean:write property='clienteVOWrapper.nome' name='<%=Constants.BEAN_KEY%>' />				&nbsp;
		</td>
       </tr>		

	  </table>
	 </td>
	</tr>
</html:form>