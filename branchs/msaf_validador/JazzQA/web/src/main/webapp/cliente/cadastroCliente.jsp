<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%> 
<%@page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="br.com.dlp.validador.cliente.ClienteAction" %>

<%int index=0;%>

<html:form action="/cliente/cliente" onsubmit="salvar();"> 

	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tiles:insert page="/tilesTemplate/base/baseCadastroFormAtributos.jsp"/>
	<!-- INSERT PARA ATRIBUTOS DE BaseCadastroForm -->
	<tr>
	 <td class="neutro">
	  <table cellpadding="0" cellspacing="0" border="0" width="100%">

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="Cliente.id.dica"/>" >
			<bean:message key="Cliente.id.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="Cliente.id.dica"/>" >&nbsp;
			<bean:write name='<%=Constants.BEAN_KEY%>' property='clienteVOWrapper.id' />				
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="Cliente.cnpj.dica"/>" >
			<bean:message key="Cliente.cnpj.label"/> : teste jboss deploy
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="Cliente.cnpj.dica"/>" >&nbsp;
			<html:text property='clienteVOWrapper.cnpj' styleClass='cc0' altKey='Cliente.cnpj.dica' titleKey="Cliente.cnpj.dica" readonly='false' size='50' />				
		</td>
       </tr>		

	   <tr>
		<td class="tt2r" width="20%" nowrap title="<bean:message key="Cliente.nome.dica"/>" >
			<bean:message key="Cliente.nome.label"/> :
		</td>
		<td class="tt3l" width="80%" title="<bean:message key="Cliente.nome.dica"/>" >&nbsp;
			<html:text property='clienteVOWrapper.nome' styleClass='cc0' altKey='Cliente.nome.dica' titleKey="Cliente.nome.dica" readonly='false' size='50' />				
		</td>
       </tr>		

	  </table>
	 </td>
	</tr>

    <script language="javascript">
    var firstElement = document.clienteForm.elements["clienteVOWrapper.cnpj"];
    firstElement.focus();
    </script>

</html:form>