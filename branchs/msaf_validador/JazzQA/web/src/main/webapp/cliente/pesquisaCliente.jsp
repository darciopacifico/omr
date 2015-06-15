<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%> 
<%@page import="org.apache.struts.taglib.html.Constants" %>
<html:form action="/cliente/cliente" > 

	<%-- coloca baseCadastroFormVLH no escopo para o funcionamento do VLH --%>
	<bean:define toScope="request" id="baseCadastroFormVLH" name="clienteForm" />

	<%-- coloca o form atual no id de request formTelaPesquisa para o 
	funcionamento da tela de pesquisa generica--%>
	<bean:define toScope="request" id="formTelaPesquisa" name="clienteForm" />
	<%-- Guarda a p�gina atual do VLH --%>
	<html:hidden property="paginaVLH"/>

<%-- Guarda o status atual da area de pesquisa --%>
<%
String statusAreaPesquisa = request.getParameter("statusAreaPesquisa");
if ( statusAreaPesquisa==null)
	statusAreaPesquisa="block"; 
%>
<input type="hidden" name="statusAreaPesquisa" id="statusAreaPesquisa" value="<%=statusAreaPesquisa%>"/>
	<tiles:insert page="/tilesTemplate/base/baseCadastroFormAtributos.jsp"/>

	<tr >
		<td><img src="imagens/transp.gif" width="1" height="7px"></td>
	</tr>
	<tr>
		<td class="verAz11b"><%
if ( statusAreaPesquisa.equals("block") ) { %>
	  <img border='0' id="imgExpand" style='cursor:hand' src="../assets/menosVermelho.gif" alt="Ocultar area de pesquisa"  onclick="javascript:ocultaPesquisa();"></img>
<%} else { %>
	  <img border='0' id="imgExpand" style='cursor:hand' src="../assets/maisVermelho.gif"  alt="Expandir area de pesquisa" onclick="javascript:ocultaPesquisa();"></img>
<%} %>
	  &nbsp;Filtro para pesquisa
</td>
	</tr>
	<tr bgcolor="#FFCC66">
		<td><img src="imagens/transp.gif" width="1" height="1"></td>
	</tr>

	<tr>
	 <td class="neutro">

 <div border="0" id="areaPesquisa" style="width:100%; height:100%; overflow-x:auto; overflow-y:auto; display:<%=statusAreaPesquisa%>;">

	  <table cellpadding="0" cellspacing="0" border="0" width="100%">

				<tr>
					<td class="tt2R"  width="30%"  nowrap="nowrap" title="<bean:message key="Cliente.id.dica"/>" >
						<bean:message key="Cliente.id.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="Cliente.id.dica"/>" >
						<html:text property='clienteVOWrapper.pesquisaId' styleClass='cc0' altKey='Cliente.id.dica' titleKey="Cliente.id.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='13' onkeypress='onlyNum();' onchange='' />				
					</td>
				</tr>

				<tr>
					<td class="tt2R"  width="30%"  nowrap="nowrap" title="<bean:message key="Cliente.cnpj.dica"/>" >
						<bean:message key="Cliente.cnpj.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="Cliente.cnpj.dica"/>" >
						<html:text property='clienteVOWrapper.pesquisaCnpj' styleClass='cc0' altKey='Cliente.cnpj.dica' titleKey="Cliente.cnpj.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='50' />				
					</td>
				</tr>

				<tr>
					<td class="tt2R"  width="30%"  nowrap="nowrap" title="<bean:message key="Cliente.nome.dica"/>" >
						<bean:message key="Cliente.nome.label"/> :
					</td>
					<td class="tt3L"  width="70%"  nowrap="nowrap" title="<bean:message key="Cliente.nome.dica"/>" >
						<html:text property='clienteVOWrapper.pesquisaNome' styleClass='cc0' altKey='Cliente.nome.dica' titleKey="Cliente.nome.dica" readonly='false' name='<%=Constants.BEAN_KEY%>' size='50' />				
					</td>
				</tr>

		<tr>
			<td colspan="2" class="tt1R"  nowrap="nowrap" title="<bean:message key="Cliente.nome.dica"/>">
				<html:button property="btnPesquisar" styleClass="bb1" onclick="javascript:pesquisar();travarBotao(this);"  accesskey="p" >
					<bean:message key="pesquisa.padrao.btnpesquisar"/>
				</html:button>
			</td>
		</tr>
	  </table>
</div>

	 </td>
	</tr>
	<tr>
	 <td class="neutro">
	  <table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
		<td class="tt0L" width="1%">
			&nbsp;<!--bot�o alterar-->
		</td>
		<td class="tt0L" width="1%">
			&nbsp;<!--bot�o excluir-->
		</td>
		<td class="tt0L" width="1%">
			&nbsp;<!--bot�o consultar-->
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="Cliente.id.dica"/>" >
			<bean:message key="Cliente.id.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[0].campo' value='id' />
			<html:hidden property='IVOWrapper.webQueryOrder[0].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[0].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(0,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="Cliente.cnpj.dica"/>" >
			<bean:message key="Cliente.cnpj.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[1].campo' value='cnpj' />
			<html:hidden property='IVOWrapper.webQueryOrder[1].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[1].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(1,this);'>
		</td>

		<td class="tt0L" nowrap="nowrap" title="<bean:message key="Cliente.nome.dica"/>" >
			<bean:message key="Cliente.nome.label"/>
	    <html:hidden property='IVOWrapper.webQueryOrder[2].campo' value='nome' />
			<html:hidden property='IVOWrapper.webQueryOrder[2].tipoOrdenacao' />
			<img border='0' style='cursor:hand' 
				src='../assets/ordenacao/ordenacao_<bean:write name="<%=Constants.BEAN_KEY%>" property="IVOWrapper.webQueryOrder[2].tipoOrdenacao"/>.GIF'
				alt='<bean:message key="pesquisa.padrao.ordenacao"/>'
				onclick='javascript:ordenar(2,this);'>
		</td>

	</tr>
	<tr bgcolor="#FFCC66">
		<td colspan="50" ><img src="imagens/transp.gif" width="1" height="1"></td>
	</tr>
	<%-- INCREMENTADOR PARA ID ITEM SELECIONADO --%>
	<%int i=0;%>
	<logic:iterate id="basevo" name="formTelaPesquisa"  property="resultadoPesquisa" >
	<tr>
		<td class="tt2C"  nowrap="nowrap">
			<img 
			border='0' 
			style='cursor:hand' 
			src="../assets/img_e.gif" 
			alt="Editar" 
			onclick="javascript:preparaAlterar(<%=i%>);"></img>
		</td>								
		<td class="tt2C"  nowrap="nowrap">
			<img 
			border='0' 
			style='cursor:hand' 
			src="../assets/img_x.gif" 
			alt="Excluir" 
			onclick="javascript:excluir(<%=i%>);"></img>
		</td>
		<td class="tt2C" nowrap="nowrap">
			<img 
			border='0' 
			style='cursor:hand' 
			src="../assets/img_c.gif" 
			alt="Consultar" 
			onclick="javascript:preparaConsultar(<%=i%>);"></img>
		</td>								

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='id' ignore='true' formatKey='formatoInteger' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='cnpj' ignore='true' name='basevo' />				&nbsp;
		</td >

		<td class="tt2L"  nowrap="nowrap">
			<bean:write property='nome' ignore='true' name='basevo' />				&nbsp;
		</td >

	</tr>
	<%i++;%>
	</logic:iterate>

		</tr>
	  </table>
	 </td>
    </tr>

    <script language="javascript">
    var firstElement = document.clienteForm.elements["clienteVOWrapper.pesquisaId"];
    firstElement.focus();
    </script>

</html:form>