<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean"prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Simulador de Validação do WebService</title>

	<link href="css/estilo.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="readme_files/screensmall.css" type="text/css" media="screen">
	<script src='js/principal.js' language="javascript"></script>
	<script type="text/javascript">
	function MM_jumpMenu(targ,selObj,restore){ //v3.0
	  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
	  if (restore) selObj.selectedIndex=0;
	}
	function doValidar(){
	  elementService = document.getElementById("dispatchMethod");
	    if (elementService != null){
	       elementService.value = "doValidar";
	    }
	  elementService.form.submit();
	}
	</script>
</head>
<body> 
<html:form action="/TesteWebServiceAction.do" method="post">
<input type="hidden" name="dispatchMethod" id="dispatchMethod" value="" />
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabelaexterna">
  <tr>
    <td valign="top"><table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#000066">
      <tr>
        <td valign="top"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#003366">
          <tr>
            <td height="30" class="title0">simulador de validação</td>
          </tr>
          <tr>
            <td height="30" class="title1">test-drive para validação de cadastro</td>
          </tr>
          <tr>
            <td height="30" valign="top" background="images/bg_simulador.jpg">
              <table width="600" border="0" align="center" cellpadding="1" cellspacing="0">
                <tr>
                  <td>&nbsp;</td>
                  <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                  <td height="21">&nbsp;</td>
                  <td colspan="4" align="right" valign="middle" class="campos">Preencha nos <strong>campos em azul</strong>: <img src="imagens/leg_campo_azul.jpg" width="54" height="16" align="baseline" /></td>
                </tr>
                <tr>
                  <td height="29">&nbsp;</td>
                  <td colspan="4" align="right" class="campos">Os <strong>campos em cinza</strong> exibem o resultado da consulta: <img src="imagens/leg_campo_cinza.jpg" width="54" height="16" align="baseline" /></td>
                </tr>
                <tr>
                  <td width="150">&nbsp;</td>
                  <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                  <td height="31" class="campos">Tipo de Validação:</td>
                  <td colspan="4">
                  
                  <html:select styleClass="ef" property="tipo" name="testeWebServiceForm" >
                    <html:option value="3">Receita Federal</html:option>
                    <html:option value="1">Sintegra</html:option>
                  </html:select></td>
                </tr>
                <tr>
                  <td height="5" colspan="5"><img src="images/spacer.gif" width="1" height="5" /></td>
                  </tr>
                <tr>
                  <td class="campos">CNPJ:</td>
                  <td colspan="2"><html:text styleClass="simulador_val" property="pessoaVO.cnpj" size="25" maxlength="22" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                  <td class="campos">IE:</td>
                  <td><html:text styleClass="simulador_val" property="pessoaVO.ie" size="20" maxlength="15" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                </tr>
                <tr>
                  <td class="campos">CNPJ de retorno:</td>
                  <td colspan="2"><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.cnpj" size="25" maxlength="22"/></td>
                  <td class="campos">IE de retorno:</td>
                  <td><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.ie" size="20" maxlength="15"/></td>
                </tr>  
                <tr>
                  <td height="5" colspan="5" class="campos"><img src="images/spacer.gif" alt="" width="1" height="5" /></td>
                </tr>
                <tr>
                  <td class="campos">Razão Social:</td>
                  <td colspan="4"><label>
                    <html:text styleClass="simulador_val" property="pessoaVO.razaoSocial" size="60" maxlength="50" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/>
                  </label></td>
                </tr>
                <tr>
                  <td class="campos">Razão Social de retorno:</td>
                  <td colspan="4"><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.razaoSocial" size="60" maxlength="50"/></td>
                </tr>
                <tr>
                  <td height="5" colspan="5" class="campos"><img src="images/spacer.gif" width="1" height="5" /></td>
                  </tr>
                <tr>
                  <td class="campos">Logradouro:</td>
                  <td colspan="4"><html:text styleClass="simulador_val" property="pessoaVO.logradouro" size="60" maxlength="50" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                </tr>
                <tr>
                  <td class="campos">Logradouro de retorno:</td>
                  <td colspan="4"><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.logradouro" size="60" maxlength="50"/></td>
                </tr>
                <tr>
                  <td height="5" colspan="5" class="campos"><img src="images/spacer.gif" alt="" width="1" height="5" /></td>
                </tr>
                <tr>
                  <td class="campos">Número:</td>
                  <td width="141"><html:text styleClass="simulador_val" property="pessoaVO.numero" size="8" maxlength="5" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                  <td colspan="2" class="campos">Complemento:</td>
                  <td><html:text styleClass="simulador_val" property="pessoaVO.complemento" size="15" maxlength="10" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                </tr>
                <tr>
                  <td class="campos">Número de retorno:</td>
                  <td><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.numero" size="8" maxlength="5"/></td>
                  <td colspan="2" class="campos">Complemento de retorno:</td>
                  <td><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.complemento" size="15" maxlength="10"/></td>
                </tr>
                <tr>
                  <td height="5" colspan="5" class="campos"><img src="images/spacer.gif" alt="" width="1" height="5" /></td>
                </tr>
                <tr>
                  <td class="campos">Bairro:</td>
                  <td colspan="2"><html:text styleClass="simulador_val" property="pessoaVO.bairro" size="25" maxlength="22" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                  <td width="155" class="campos">CEP:</td>
                  <td width="145"><html:text styleClass="simulador_val" property="pessoaVO.cep" size="20" maxlength="10" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                </tr>
                <tr>
                  <td class="campos">Bairro de retorno:</td>
                  <td colspan="2"><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.bairro" size="25" maxlength="22"/></td>
                  <td class="campos">CEP de retorno:</td>
                  <td><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.cep" size="20" maxlength="10"/></td>
                </tr>
                <tr>
                  <td height="5" colspan="5" class="campos"><img src="images/spacer.gif" alt="" width="1" height="5" /></td>
                </tr>
                <tr>
                  <td class="campos">Município:</td>
                  <td colspan="2"><html:text styleClass="simulador_val" property="pessoaVO.cidade" size="25" maxlength="22" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                  <td class="campos">Estado:</td>
                  <td><html:text styleClass="simulador_val" property="pessoaVO.estado" size="20" maxlength="10" onfocus="mudacor(this.name);" onblur="voltacor(this.name);"/></td>
                </tr>
                <tr>
                  <td class="campos">Município de retorno:</td>
                  <td colspan="2"><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.cidade" size="25" maxlength="22"/></td>
                  <td class="campos">Estado de retorno:</td>
                  <td><html:text styleClass="simulador_val_des" disabled="true" property="dadosRetInstVO.estado" size="20" maxlength="10"/></td>
                </tr>
                <tr>
                  <td colspan="5" class="campos">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="5" class="campos">&nbsp;</td>
                  </tr>
                <tr>
                  <td colspan="5" align="center" class="campos"><input name="consutar" type="button" class="ef" id="01" onclick="doValidar();"  value="          Consultar          "  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                    <input name="limpar" type="reset" class="ef" id="012" value="   Limpar Formulário   " /></td>
                </tr>
                <tr>
                  <td colspan="5" class="campos">&nbsp;</td>
                </tr>
                
                 <tr>
                   <td colspan="5" class="campos">
 	            		<html:messages id="erro" message="true" property="geral" >
							- <bean:write name="erro"/>
						</html:messages>
                   </td>
                  </tr>
                
                <tr>
                	<td colspan="5" class="campos">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="5" class="campos">&nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>


</html:form>
</body>
	<script type="text/javascript">
		document.getElementById("razaosocial").focus()
		function mudacor(campoatual){
			var cor = "1px solid #069"
			document.getElementById(campoatual).style.border = cor;
		}
		function voltacor(campoatual){
			var cor2 = "1px solid #D5E8F7"
			document.getElementById(campoatual).style.border = cor2;
		}
	</script>
</html:html>