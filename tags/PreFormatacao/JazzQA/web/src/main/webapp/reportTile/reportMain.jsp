<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!-- Contem o nome do hidden de servicos a ser criado -->
<%@ page import="br.com.dlp.framework.struts.action.BaseAction" %>

<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  	
	<title>
		Formulario com parametros para o relatório
	</title>
    <link rel="stylesheet" href="../css/new_base.css" type="text/css">
    <script>
       function enviar(tipo){
       	elementService = document.getElementById("<%=BaseAction.PARAM_SERVICE%>");
        elementService.value = "exibirRelatorio";
       
		document.forms[0].relatorioTipo.value=tipo;
        document.forms[0].submit();
       }
		function showErro(){
			document.getElementById("divErro").style.display = "block";
			document.getElementById("conteudo").style.display = "none";
		}//fim da funcao
       
    </script>

  </head>

  <body class="corpo" topmargin="2" leftmargin="2" >
    
      <TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	    <tr>
          <td class="tt0l" colspan="10" >
            <b><img src="../assets/tit_aplic.gif" border=0 >&nbsp;
            <!-- TITULO DA TELA -->
            <tiles:getAsString name="title"/>
            <!-- TITULO DA TELA -->
            </b>
          </td>
        </tr>
	    <tr>
	      <td>
		      <div class="tt5L" id="conteudo" style="width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
	   			<%try{%>
			        <!-- COMEÇO FORMULARIO -->
				       <tiles:get name="reportForm"/>
				    <!-- FIM FORMULARIO -->
				<%}catch(Exception e){
					e.printStackTrace(response.getWriter());
				}%>
			  </div>
	      </td>
	    </tr>
	    <tr>
	      <td class="tt6C">
	        <!-- COMEÇO BOTOES -->
		    <tiles:get name="reportButtons"/>
		    <!-- FIM BOTOES -->
	      </td>
	    </tr>

      </TABLE>
      <a href="javascript:showErro();"><img src="../assets/alert_erro.gif" id="imgErro" border="0" alt="Ver erros ou alertas" style="display:none;" LOOP=INFINITE></a>
    	<%try{%>
		<!-- INICIO CHAMADA MENSAGENS DE ERRO -->
		<tiles:get name="erros"/>
		<!-- FINAL CHAMADA MENSAGENS DE ERRO -->
	<%}catch(Exception e){
		e.printStackTrace(response.getWriter());
	}%>
  </body>
</html>
