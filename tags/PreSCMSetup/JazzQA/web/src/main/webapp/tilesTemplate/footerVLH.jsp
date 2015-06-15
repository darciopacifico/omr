<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="br.com.dlp.framework.struts.form.BaseCadastroVLHForm" %>
<%@ page import="br.com.dlp.framework.struts.action.BaseCadastroVLHAction" %>

<tr>
	<td colspan="2" class="tt0C" height="1px;">
		
		<%		
		BaseCadastroVLHForm baseCadastroVLHForm = (BaseCadastroVLHForm)request.getAttribute("baseCadastroFormVLH");
		int paginaVLH = baseCadastroVLHForm.getPaginaVLH();
		int qtdPaginas = baseCadastroVLHForm.getQtdPaginas();
		if(qtdPaginas<=1){
			%>
			<img border="0" style='cursor:hand' alt="Primeira Página" src="../assets/first.gif"/>
			<img border="0" style='cursor:hand' alt="Página Anterior" src="../assets/previous.gif"/>
			<%
		}else{
			%>
			<a href="###" onclick="vlhFirstPage()">   <img border="0" style='cursor:hand' alt="Primeira Página" src="../assets/first.gif"/></a>
			<a href="###" onclick="vlhPreviousPage()"><img border="0" style='cursor:hand' alt="Página Anterior" src="../assets/previous.gif"/></a>
			<%
		}
		if(qtdPaginas>0){
		


			int paginaDe = baseCadastroVLHForm.getFirstPageOnView();
			int paginaAte = baseCadastroVLHForm.getLastPageOnView();
			
			if(paginaDe>0){
				%> <a href="###" onclick="vlhPageByIndex(<%=paginaDe-1%>)">...</a><%
			}
			
			for(int paginaItera = paginaDe; paginaItera<paginaAte; paginaItera++ ){
				if(paginaItera!=paginaVLH){
					%> <a href="###" onclick="vlhPageByIndex(<%=paginaItera%>)"><%=paginaItera+1%></a><%
				}else{
					%> <b><%=paginaItera+1%></b><%
				}
			}
		
			if(paginaAte < qtdPaginas ){
				%> <a href="###" onclick="vlhPageByIndex(<%=paginaAte+1%>)">...</a><%
			}

		

		}else{
			%>
			(zero páginas)
			<%
		}
		if(qtdPaginas<=1){
			%>
			<img border="0" style='cursor:hand' alt="Próxima Página" src="../assets/next.gif"/>
			<img border="0" style='cursor:hand' alt="Última Página"  src="../assets/last.gif"/>
			<%	
		}else{
			%>
			<a href="###" onclick="vlhNextPage()"><img border="0" style='cursor:hand' alt="Próxima Página" src="../assets/next.gif"/></a>
			<a href="###" onclick="vlhLastPage()"><img border="0" style='cursor:hand' alt="Última Página"  src="../assets/last.gif"/></a>
			<%	
		
		}
		%>
		
	</td>
</tr>