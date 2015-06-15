<?xml version="1.0" encoding="ISO-8859-1"?>
<html	>
	
	<head>
		<title>Exam Center - Login</title>
	
		<style>
			* {
				margin:auto;
			}
		 
			span, a, label{
				font-family: Arial, sans-serif; font-size: 12px; font-weight: bold;
			}
		
			span{
				
			}
		
			.divReg { 
				position:relative; 
				height: 630px; 
				width: 724px; 
				background: #91b1c7 url('../img/menu/repearVertical.png'); 
				background-repeat: repeat-y; 
				margin: auto; 
				top:30px;
			}
			
			.divReg form {
				position: relative; 
				top: 10%; 
				text-align: center;
			}
			
			.divReg fieldset {
				width: 650px; 
				position: inherit;
				margin: auto;
			}
			
			.divReg legend, label {
				font-family: Arial, sans-serif; color: #eeeeee; font-size: 12px; font-weight: bold;
			}

			.divReg table {
				margin:auto;
			}
			
			.divReg td {
				text-align: left;
				font-family: Arial, sans-serif; 
				font-size: 12px;
				color: white;
			}
			
			.divReg label {
				font-family: Arial, sans-serif; color: #eeeeee;
			}
			
		</style>
		
	</head>
	
	<body style="background: #eeeeee url('./img/menu/examCenterGray.png'); text-align: center; background-repeat: repeat;"  onload='document.frmLogin.j_username.focus();'>
		<div style=" position:relative; height: 390px; width: 530px; background: url('./img/menu/repearVertical.png'); background-repeat: repeat; margin: auto; top:170px; ">
			<img src="./img/menu/corretorLogo.png" alt="" />
			<form id="frmLogin" name="frmLogin" method="post" action="j_spring_security_check" style="position: relative; top: 10%; text-align: center;">
				<fieldset style="width: 330px; position: inherit;margin: auto; ">
					<legend style="font-family: Arial, sans-serif; color: #eeeeee; font-size: 12px; font-weight: bold;">Autentica&ccedil;&atilde;o</legend>
					<table style="margin:auto; ">
						<tr>
							<td style="text-align: right;">
								<label for="form-login" style="font-family: Arial, sans-serif; color: #eeeeee;">Login:</label>
							</td>
							<td>
								<input type="text" name="j_username" id="form-login" />
							</td>
						</tr>
						<tr>
							<td style="text-align: right;">
								<label for="form-senha" style="font-family: Arial, sans-serif; color: #eeeeee;">Senha:</label>
							</td>
							<td>
								<input type="password" name="j_password" id="form-senha" />
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align: center;">
								<input type='checkbox' name='_spring_security_remember_me' id='rememberMe'  />
								<label for="rememberMe" title="Armazena senha no seu browser" >Lembrar minha senha.</label> 
								<input type="submit" value="Enviar" />
							</td>
						</tr>
	
						<tr>
							<td colspan="2" style="text-align: center;">
								<br/>
								<a href="./selfservice/SelfServiceRegister.jsf" >Não possui usuário? Registre-se</a>
							</td>
						</tr>
	
						<tr>
							<td colspan="2" style="text-align: center;">
								<br/>
								<span style="color: #eeeeee;">
								<%
								String loginFailed = request.getParameter("loginFailed");
							    if (loginFailed!=null && loginFailed.equals("true")) {
							        out.println("<span>Usuário/senha inválidos!</span>");
							    }else{
							    	out.println("<span>&nbsp;</span>");
							    }
								%>
								</span>
							</td>
						</tr>
					</table>
				</fieldset>
				<br/>
				<span style="font-size:10px;color:white;" >LinkCompany Consultoria em TI</span>
				<br/>
				<span style="font-size:10px;color:white;" >darcio@linkcompany.com.br</span>
			</form>
		</div>

	</body>
</html>
