<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	<tr>
		<td class="tt1L" style="width:1%">
			&nbsp;
		</td>
		<td class="tt1L">
			<b>Itens</b>
		</td>
	</tr>
 
	<logic:iterate 
		name="baseForm" 
		property="popUpController.popUpItemVOs" 
		id="popUpItemVO" >
	<tr>

		<td class="tt1L" style="width:1%">
			<html:checkbox 
				onclick="avaliaTipoPopUp(this, false)" 
				name="popUpItemVO" 
				property="checked" 
				indexed="true" />
		</td>
		<td class="tt2l"> 
			<bean:write name="popUpItemVO" property="value"/>&nbsp;
		</td>
	</tr>
	</logic:iterate>
