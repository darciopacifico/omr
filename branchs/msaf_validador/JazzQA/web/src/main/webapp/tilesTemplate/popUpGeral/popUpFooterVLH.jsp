<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="br.com.dlp.framework.struts.form.BaseCadastroVLHForm" %>
<%@ page import="br.com.dlp.framework.struts.action.BaseCadastroVLHAction" %>
<%@ page import="br.com.dlp.framework.struts.form.BaseForm" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>
<%@ page import="br.com.dlp.framework.popup.VLHPopUpController" %>
<%@ page import="br.com.dlp.framework.struts.action.BaseAction" %>


	 	<html:hidden property="popUpController.paginarPara" />
		<%		
		BaseForm baseForm = (BaseForm)request.getAttribute(Constants.BEAN_KEY);
		VLHPopUpController vlhPopUpController = (VLHPopUpController)baseForm.getPopUpController();
		
		int paginaVLH = vlhPopUpController.getPaginaVLH();
		int qtdPaginas = vlhPopUpController.getQtdPaginas();
		
		if(qtdPaginas<=1){
			%>
			<img border="0" style='cursor:hand' alt="Primeira P�gina" src="../assets/first.gif"/>
			<img border="0" style='cursor:hand' alt="P�gina Anterior" src="../assets/previous.gif"/>
			<%
		}else{
			%>
			<a href="###" onclick="vlhFirstPage()">   <img border="0" style='cursor:hand' alt="Primeira P�gina" src="../assets/first.gif"/></a>
			<a href="###" onclick="vlhPreviousPage()"><img border="0" style='cursor:hand' alt="P�gina Anterior" src="../assets/previous.gif"/></a>
			<%
		}
		if(qtdPaginas>0){
		
		
		
			int paginaDe = vlhPopUpController.getFirstPageOnView();
			int paginaAte = vlhPopUpController.getLastPageOnView();
			
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
			(zero p�ginas)
			<%
		}
		
		if(qtdPaginas<=1){
			%>
			<img border="0" style='cursor:hand' alt="Pr�xima P�gina" src="../assets/next.gif"/>
			<img border="0" style='cursor:hand' alt="�ltima P�gina"  src="../assets/last.gif"/>
			<%	
		}else{
			%>
			<a href="###" onclick="vlhNextPage()"><img border="0" style='cursor:hand' alt="Pr�xima P�gina" src="../assets/next.gif"/></a>
			<a href="###" onclick="vlhLastPage()"><img border="0" style='cursor:hand' alt="�ltima P�gina"  src="../assets/last.gif"/></a>
			<%	
		
		}
		%>
		