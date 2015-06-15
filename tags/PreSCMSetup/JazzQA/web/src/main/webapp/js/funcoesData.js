/*
#######################################################################################
# ARQUIVO DE FUNCOES PARA TRATAMENTO DE DATAS                                         #
# DESENVOLVIDO POR ANSELMO PARRECHIO E JOSE LUIS                                      #
# DATA 05/04/2005                                                                     #
#######################################################################################


Estas express�es regulares v�o do mais simples ao mais completo, da seguinte forma:

Simples � valida apenas o uso de d�gitos, nas posi��es e quantidade certas: 1 a 2 d�gitos
para dia e para m�s, 1 a 4 d�gitos para ano. 

M�dia � testa os d�gitos poss�veis em cada posi��o: o primeiro d�gito do dia, se houver, 
deve ser de 0 a 3 ([0-3]?\d); o primeiro d�gito do m�s, se houver, deve ser 0 ou 1 
([01]?\d); passamos a aceitar apenas 2 ou 4 d�gitos para o ano. 

Avan�ada � garante as faixas de valores corretas para dias 1 a 31 ((0?[1-9]|[12]\d|3[01])) 
e meses 1 a 12 ((0?[1-9]|1[0-2])). E aqui optamos por for�ar os 2 primeiros d�gitos do ano 
(correspondentes ao s�culo), quando fornecidos, a serem 19 ou 20 ((19|20)?\d{2}). 

Completa � valida os dias permitidos de acordo com o m�s. Para este �ltimo, foram criados 
tr�s grupos alternativos de pares dia/m�s: 
Os dias 1 a 29 ((0?[1-9]|[12]\d)) s�o aceitos em todos os meses (1 a 12): (0?[1-9]|1[0-2]) 
Dia 30 � v�lido em todos os meses, exceto fevereiro (02): (0?[13-9]|1[0-2]) 
Dia 31 � permitido em janeiro (01), mar�o (03), maio (05), julho (07), agosto (08), 
outubro (10) e dezembro (12): (0?[13578]|1[02]). 

Tradicional � data no formato DD/MM/AAAA, basicamente � a data Completa, por�m sem a 
opcionalidade do zero � esquerda no dia ou m�s menor que 10. 

A �nica coisa que a express�o mais completa (e complexa) n�o � capaz de testar � a 
validade do dia 29/fev apenas para anos bissextos.
*/

/*
Funcao q testa se a data � valida
Parametros: string da data, formato
*/
function validaData(pStr, pFmt){

	pStr = pStr

	var reDate1 = /^(19|20)?\d{2}$/;
	var reDate2 = /^(0[1-9]|1[0-2])\/(19|20)?\d{2}$/;
	var reDate3 = /^\d{1,2}\/\d{1,2}\/\d{1,4}$/;
	var reDate4 = /^[0-3]?\d\/[01]?\d\/(\d{2}|\d{4})$/;
	var reDate5 = /^(0?[1-9]|[12]\d|3[01])\/(0?[1-9]|1[0-2])\/(19|20)?\d{2}$/;
	var reDate6 = /^((0?[1-9]|[12]\d)\/(0?[1-9]|1[0-2])|30\/(0?[13-9]|1[0-2])|31\/(0?[13578]|1[02]))\/(19|20)?\d{2}$/;
	var reDate7 = /^((0[1-9]|[12]\d)\/(0[1-9]|1[0-2])|30\/(0[13-9]|1[0-2])|31\/(0[13578]|1[02]))\/(19|20)?\d{2}$/;
	var reDate = reDate7;

	eval("reDate = reDate" + pFmt);
	if (!reDate.test(pStr)) {
		if (pStr != null && pStr != "") {
			alert(pStr + " N�O � uma data v�lida.");
		}
	}

} // fim da funcao validaData


/*
Funcao que coloca as barras automaticamente no campo texto, coloca-la no onKeyPress
Parametros: nome do objeto
*/
function formataData(name){

	if (event.keyCode < 48 || event.keyCode > 57) {
		event.returnValue = false;
	}
	var size = document.getElementById(name).value.length;
	var value = document.getElementById(name).value;
	
	if( (size==2) || (size==5) ){
		document.getElementById(name).value = value + '/';
	}

}//fim da funcao

/*
Funcao para colocar a barra automaticamente em campos com mes/ano somente
Parametros: nome do objeto
*/
function formataMesAno(name){

	if (event.keyCode < 48 || event.keyCode > 57) {
		event.returnValue = false;
	}
	
	var size = document.getElementById(name).value.length;
	var value = document.getElementById(name).value;
	
	if(size==2){
		document.getElementById(name).value = value + '/';
	}

}//fim da funcao

function formataAno(name){


	if (event.keyCode < 48 || event.keyCode > 57) {
		event.returnValue = false;
	}
	var size = document.getElementById(name).value.length;
	var value = document.getElementById(name).value;
	
	if (size==4) {
		event.returnValue = false;
	}

	if (size==0 && (event.keyCode < 49 || event.keyCode > 50) ) {
		event.returnValue = false;
	}

	if (size==1 && ((value=="1" && event.keyCode != 57) || (value=="2" && event.keyCode != 48)) ) {
		event.returnValue = false;
	}


}//fim da funcao

