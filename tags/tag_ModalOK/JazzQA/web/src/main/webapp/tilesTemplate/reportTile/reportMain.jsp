<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!-- Contem o nome do hidden de servicos a ser criado -->
<%@ page import="br.com.dlp.framework.struts.action.BaseAction" %>

<html>
  <head>
	<title>
		Formulario com parametros para o relatório
	</title>
    <link rel="stylesheet" href="../../css/new_base.css" type="text/css">
    <script>
       function enviar(tipo){
       	elementService = document.getElementById("<%=BaseAction.PARAM_SERVICE%>");
        elementService.value = "exibirRelatorio";
       
		document.forms[0].relatorioTipo.value=tipo;
        document.forms[0].submit();
       }
    </script>

  </head>

  <body class="corpo" topmargin="2" leftmargin="2" >
    
      <TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	    <tr>
          <td class="tt0l" colspan="10" >
            <b><img src="../../assets/tit_aplic.gif" border=0 >&nbsp;
            <!-- TITULO DA TELA -->
            <tiles:getAsString name="title"/>
            <!-- TITULO DA TELA -->
            </b>
          </td>
        </tr>
	    <tr>
	      <td>
	        <!-- COMEÇO FORMULARIO -->
		       <tiles:get name="reportForm"/>
		    <!-- FIM FORMULARIO -->
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
    
  </body>
</html>
