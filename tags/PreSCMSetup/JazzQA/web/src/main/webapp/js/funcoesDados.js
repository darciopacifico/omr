/*
#######################################################################################
# ARQUIVO DE FUNCOES PARA TRATAMENTO DE CAMPOS DE DADOS PESSOAIS COMO EMAIL, FONE ETC.#
# DESENVOLVIDO POR ANSELMO PARRECHIO E JOSE LUIS                                      #
# DATA 05/04/2005                                                                     #
#######################################################################################
*/

/*
Funcao que valida um endereÁo de email
Parametros: string do campo, formato (pode ser 1,2,3)
1 = permite palavra e sinais especiais no nome e varios pontos + @ + uma palavra sem sinal + . + uma palavra sem sinal com tamanho de 2 a 6 ou numero tamanho de 1 a 3 + . + numero com tamanho de 3
2 = permite palavra no nome e varios pontos + @ + uma palavra sem sinal de tamanho de 2 a 63 + . + palavra com tamanho de 2 a 6 ou numero tamanho de 1 a 3 + . + numero com tamanho de 3
3 = permite palavra no nome e varios pontos + @ + uma palavra sem sinal com numeros de tamanho de 0 a 61 + palavra com numeros de tamanho de 2 a 6 ou numero tamanho de 1 a 3 + . + numero com tamanho de 3
*/
function validaEmail(pStr, pFmt){

	var reEmail1 = /^[\w!#$%&'*+\/=?^`{|}~-]+(\.[\w!#$%&'*+\/=?^`{|}~-]+)*@(([\w-]+\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	var reEmail2 = /^[\w-]+(\.[\w-]+)*@(([\w-]{2,63}\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	var reEmail3 = /^[\w-]+(\.[\w-]+)*@(([A-Za-z\d][A-Za-z\d-]{0,61}[A-Za-z\d]\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	var reEmail = reEmail3;

	eval("reEmail = reEmail" + pFmt);
	if(reEmail.test(pStr)){
		return true;
	} else if (pStr != null && pStr != "") {
		alert(pStr + " N√O È um endereÁo de e-mail v·lido.");
	}

} // fim da funcao 

/*
Funcao que formata o telefone, formato : (11) 12345678
Parametros: nome do objeto, 
	    formato
1=12345678
2=1234-5678 ou 123-4567
3=(11) 12345678
4=(11) 1234-5678 ou (11) 123-4567

*/
function formataFone(name, pFmt){

	var fone=document.getElementById(name);
	var size = fone.value.length;
	var value = fone.value;
	
	if ( (pFmt==1 && size>=7) || (pFmt==2 && size>=8) || (pFmt==3 && size>=12) || (pFmt==4 && size>=13) ) {
		event.returnValue = true;
		return;
	}

	if ( (pFmt==1 || pFmt==3) && (event.keyCode < 48 || event.keyCode > 57) ) {
		event.returnValue = false;
		return;
	}
	if ( (pFmt==2 || pFmt==4) && ((event.keyCode < 48 || event.keyCode > 57) && event.keyCode!=45)){
		event.returnValue = false;
		return;
	}
	if ( pFmt==2 && event.keyCode==45 ) {
		if (size==3 || size==4) {
			if (value.indexOf("-")>0) {
				event.returnValue = false;
			}
			else {
				event.returnValue = true;
			}
		} 
		else {
			event.returnValue = false;
		}
		return
	}
	if ( pFmt==4 && event.keyCode==45 ) {
		if (size==8 || size==9) {
			if (value.indexOf("-")>0) {
				event.returnValue = false;
			}
			else {
				event.returnValue = true;
			}
		} 
		else {
			event.returnValue = false;
		}
		return
	}

	if ((pFmt==2 || pFmt==4 ) && size!=4 && size-value.indexOf("-")==5) {
	   event.returnValue = false;
	   return;
	}
	if (pFmt==2 && size==4 && value.indexOf("-")==-1){
	   fone.value = value + "-";
	}
	if (pFmt==4 && size==9 && value.indexOf("-")==-1){
	   fone.value = value + "-";
	}
	if (pFmt==3 || pFmt==4) {
		if (size==4){
		   fone.value = value + " ";
		}
		if (size==3){
		   fone.value = value + ") ";
		}
		if (size==0){
		   fone.value = "(" + value;
		}
	}


}//fim da funcao

/*
Funcao que valida se o telefone informado È valido
Parametros: string do campo, formato (pode ser 1,2,3,4,5,6,7,8)
1 = Numero com tamanho de 7 a 8
2 = Numero com tamanho de 3 ou 4 + - + numero com tamanho de 4
3 = ( + 2 digitos + ) + espaÁo + numero com tamanho de 7 a 8
4 = ( + 2 digitos + ) + espaÁo + numero com tamanho de 3 ou 4 + - + numero com tamanho de 4
5 = Numero com tamanho de 7 a 8 + palavras com sinais especiais
6 = Numero com tamanho de 3 ou 4 + - + numero com tamanho de 4 + palavras com sinais especiais
7 = ( + 2 digitos + ) + espaÁo + numero com tamanho de 3 ou 4 + numero com tamanho de 4 + palavras com sinais especiais
8 = ( + 2 digitos + ) + espaÁo + numero com tamanho de 3 ou 4 + - + numero com tamanho de 4 + palavras com sinais especiais
*/
function validaFone(pStr, pFmt) {

	var reFone1 = /^\d{7,8}$/;
	var reFone2 = /^\d{3,4}\-\d{4}$/;
	var reFone3 = /^[(]\d{2}[)][ ]*\d{7,8}$/;
	var reFone4 = /^[(]\d{2}[)][ ]*\d{3,4}\-\d{4}$/;
	var reFone5 = /^\d{7,8}[ /]+?[\w„√·¡‚¬‡¿Í È…ÌÕı’Ù‘Û”˙⁄Á«\/= ]*$/;
	var reFone6 = /^\d{3,4}\-\d{4}[ /]+?[\w„√·¡‚¬‡¿Í È…ÌÕı’Ù‘Û”˙⁄Á«\/= ]*$/;
	var reFone7 = /^[(]\d{2}[)][ ]*\d{3,4}\d{4}[ /]+?[\w„√·¡‚¬‡¿Í È…ÌÕı’Ù‘Û”˙⁄Á«\/= ]*$/;
	var reFone8 = /^[(]\d{2}[)][ ]*\d{3,4}\-\d{4}[ /]+?[\w„√·¡‚¬‡¿Í È…ÌÕı’Ù‘Û”˙⁄Á«\/= ]*$/;
//	var reFone3 = /^[(]\d{2}[)][ ]*\d{3,4}\-\d{4}[ ]([\w!#$%&'*+\/=?^`{|}~-]+)*$/;

	var reFone = reFone1;
	
	eval("reFone = reFone" + pFmt);
	if (!reFone.test(pStr)) {
		if (pStr != null && pStr != "") {
			alert(pStr + " N√O È um n˙mero de telefone v·lido.");
		}
	}

}



function formataCpf(name){

	var cpf=document.getElementById(name);
	var size = cpf.value.length;
	var value = cpf.value;
	
	
	if (event.keyCode < 48 || event.keyCode > 57) {
		event.returnValue = false;
		return;
	}

	if (size==3 || size==7){
	   cpf.value = value + ".";
	}
	if (size==11){
	   cpf.value = value + "-";
	}

}//fim da funcao


function validaCpf(cpf) {
    cpf = document.getElementById(cpf).value;
    if(cpf.length > 0 ){
		// Aqui comeca a checagem do CPF
		var posicao;
		var digitoVerificador
		var digitoVerificadorInformado;
		var i,  z, soma;

		var digitos = new Array();

		// Retira os dois ˙ltimos digitos do n˙mero informado
		digitoVerificadorInformado = cpf.substr(cpf.length-2, 2);

		// Desmembra o numero do CPF no array DIGITOS
		z=0;
		for (i = 0; i < cpf.length; i++) {
			if (cpf.substr(i,1)==".") {
				continue;
			}
			if (cpf.substr(i,1)=="-") {
			  	break;
			}

			digitos[z] = cpf.substr(i, 1);
			z = z+1;
		}

		// Calcula o valor do 10 digito da verificaÁ„o
		posicao = 10;
		soma = 0;
		for (i = 0; i <= 8; i++) {
			soma = soma + digitos[i] * posicao;
			posicao--;
		}

		digitos[9] = soma % 11;
		if (digitos[9] < 2) {
			digitos[9] = 0;
		}
		else{
			digitos[9] = 11 - digitos[9];
		}

		// Calcula o valor do 11 digito da verificaÁ„o
		posicao = 11;
		soma = 0;
		for (i = 0; i <= 9; i++) {
			soma = soma + digitos[i] * posicao;
			posicao--;
		}
		digitos[10] = soma % 11;
		if ( digitos[10] < 2) {
			digitos[10] = 0;
		}
		else {
			digitos[10] = 11 - digitos[10];
		}

		// Verifica se os valores dos digitos verificadores conferem
		digitoVerificador = digitos[9] * 10 + digitos[10];
		if (digitoVerificador != digitoVerificadorInformado) {
			alert("CPF inv·lido.");
			cpf.focus();
			return false;
		}
		return true;
	}
}

