<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%> 
<%@page import="br.com.dlp.framework.struts.action.BaseAction" %>
<%@page import="br.com.dlp.framework.struts.form.BaseForm" %>


	<tr>
	 <td class="neutro">
	  <table cellpadding="0" cellspacing="0" border="0" width="100%">
	   <tr>
		<td class="tt8L" width="1%">
			<logic:equal name="<%=BaseAction.PARAM_FORM_ATUAL%>" property="acaoEstado" value="<%=BaseForm.ACAOESTADO_INCLUSAO%>" > 
				<bean:message key="geral.inclusao" />
			</logic:equal>
			<logic:equal name="<%=BaseAction.PARAM_FORM_ATUAL%>" property="acaoEstado" value="<%=BaseForm.ACAOESTADO_ALTERACAO%>" > 
				<bean:message key="geral.alteracao" />
			</logic:equal>
			<logic:equal name="<%=BaseAction.PARAM_FORM_ATUAL%>" property="acaoEstado" value="<%=BaseForm.ACAOESTADO_CONSULTA%>" > 
				<bean:message key="geral.consulta" />
			</logic:equal>
			<logic:equal name="<%=BaseAction.PARAM_FORM_ATUAL%>" property="acaoEstado" value="<%=BaseForm.ACAOESTADO_PESQUISA%>" > 
				<bean:message key="geral.pesquisa" />
			</logic:equal>
		</td>
		<td class="tt8L">
			<script>
				<%-- /*resolvi forçar a primeira agendada */ --%>				
				agendarLimpeza();

			</script>
			
			<div id="divMensagens" >
				<logic:present name="<%=BaseAction.DIALOG_MESSAGES%>">
					<logic:iterate 
						id="dialogMessage" 
						type="br.com.dlp.framework.vo.DialogMessageVO" 
						name="<%=BaseAction.DIALOG_MESSAGES%>"  >
						<bean:write name="dialogMessage" property="date" formatKey="formatoHoraView" />&nbsp;-&nbsp;
						
						<bean:message key="<%=dialogMessage.getMensagem()%>" />
						
						<BR><%=BaseAction.DIALOG_MESSAGE_CLOSING %>
					</logic:iterate>
				</logic:present>
				<logic:notPresent name="<%=BaseAction.DIALOG_MESSAGES%>">
				&nbsp;
				</logic:notPresent>
			</div>

		</td>
		<td class="tt8r" width="1%" nowrap>
		 <img src="../assets/img_e.gif" alt="Editar"> Editar 
		 <img src="../assets/img_x.gif" alt="Excluir"> Excluir
		 <img src="../assets/img_c.gif" alt="Consultar"> Consultar
		</td>
	   </tr>
	  </table>
	 </td>
	</tr>
	