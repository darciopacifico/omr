<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<!-- jps de mensagens de erro-->
<logic:messagesPresent message="true">
	<tr>
		<td class="tt1c" width="20%" colspan=2>&nbsp;
		   <html:messages id="message" message="true" >
		     <b><bean:write name="message"/><b><br>
		   </html:messages>
		</td>
	</tr>
</logic:messagesPresent>
<html:errors/>
<!-- jps de mensagens de erro-->	