<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="br.com.dlp.framework.struts.form.BaseCadastroVLHForm" %>
<%@ page import="br.com.dlp.framework.struts.action.BaseCadastroVLHAction" %>
<%@ page import="br.com.dlp.framework.struts.form.BaseForm" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>
<%@ page import="br.com.dlp.framework.popup.VLHPopUpController" %>
<%@ page import="br.com.dlp.framework.popup.PopUpController" %>
<%@ page import="br.com.dlp.framework.struts.action.BaseAction" %>

<tr>
	<td colspan="2" class="tt0C" height="30px;">
	 	<a href="javascript:showErro();">
	 		<img src="../assets/alert_erro.gif" 
	 			id="imgErro" 
	 			border="0" 
	 			alt="Ver erros ou alertas" 
	 			style="display:none;" 
	 			LOOP=INFINITE>
	 	</a>
	 	
	 	<%
	 	/*fazer include de footer paginador caso o popUpController seja VLHPopUpController*/
	 	BaseForm baseForm = (BaseForm)request.getAttribute(Constants.BEAN_KEY);
	 	PopUpController popUpController = baseForm.getPopUpController();
	 	
	 	if(popUpController instanceof VLHPopUpController ){
		 	%>
		 	<jsp:include page="popUpFooterVLH.jsp"/>
		 	<%
	 	}
	 	%>

	</td>
</tr>
	