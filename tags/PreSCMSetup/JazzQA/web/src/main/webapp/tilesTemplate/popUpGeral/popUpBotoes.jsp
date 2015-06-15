<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<logic:equal name="baseForm" property="popUpController.multiplaEscolha" value="true">
	<html:submit styleClass="bb0" >
		<bean:message key="popup.botao.confirmaselecao"/>
	</html:submit>
</logic:equal>
<html:button property='' styleClass='bb0' 
	onclick='<%="invocaServico('"+br.com.dlp.framework.struts.action.BaseAction.SERVICE_CONFIRMA_POPUP+"')"%>'
	>
	<bean:message key="popup.botao.voltar"/>
</html:button>
