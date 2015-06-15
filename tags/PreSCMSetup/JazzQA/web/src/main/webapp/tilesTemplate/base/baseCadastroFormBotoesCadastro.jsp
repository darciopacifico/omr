<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 

<html:button 
	styleClass="bb0" accesskey="s"
	property="btnSalvar" 
	onclick="javascript:salvar();travarBotao(this);">
	<bean:message key='pesquisa.padrao.btnsalvar'/>
</html:button >

<html:button accesskey="f"
	styleClass="bb1" 
	property="btnFechar" 
	onclick="javascript:fechar();travarBotao(this);">
	<bean:message key='pesquisa.padrao.btnfechar'/>
</html:button >
