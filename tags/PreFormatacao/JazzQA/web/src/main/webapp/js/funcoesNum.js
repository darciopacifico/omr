/*
#######################################################################################
# ARQUIVO DE FUNCOES PARA TRATAMENTO DE NUMEROS                                       #
# DESENVOLVIDO POR ANSELMO PARRECHIO E JOSE LUIS                                      #
# DATA 05/04/2005                                                                     #
#######################################################################################
*/

/*
Função que obriga o campo apenas numeros
*/
function onlyNum(){
	if(event.keyCode <48 || event.keyCode > 57){
		event.returnValue = false;
	}
}//fim da funcao

/*
Função que obriga o campo apenas numeros, pontos ou virgulas
*/
function onlyDec(){
	if(event.keyCode <44 || event.keyCode > 57 || event.keyCode==47){
		event.returnValue = false;
	}

}//fim da funcao

/*
Funcao que valida os numeros, numero de inteiros e de decimais
Parametros: valor, inteiros, decimais
*/
function validaDecimal(valor, inteiros, decimais){

	var reDecimalPt = /^[+-]?((\d+|\d{1,3}(\.\d{3})+)(\,\d*)?|\,\d+)$/;
	var reDecimalEn = /^[+-]?((\d+|\d{1,3}(\,\d{3})+)(\.\d*)?|\.\d+)$/;
	var reDecimal = reDecimalPt;

	var pLang = "Pt";
	charDec = ( pLang != "En"? ",": "." );
	eval("reDecimal = reDecimal" + pLang);
	
	if (reDecimal.test(valor)) {
		pos = valor.indexOf(charDec);
		decs = pos == -1? 0: valor.length - pos - 1;
		ints = pos == -1? valor.length: pos;
		if (ints>inteiros) {
			alert("Por favor, informe apenas "+inteiros+" casa(s) antes da virgula.");
		}
		if (decs>0 && decs>decimais) {
			alert("Por favor, informe apenas "+decimais+" casa(s) decimais.");
		}
	} else if (valor != null && valor != "") {
		alert("por favor, informe um número válido.");
	}

}//fim da funcao

/*
Funcao que prepara o numero para ser formatado em moeda, esta funcao deve ser chamada no onChange
Parametros: nome do objeto
*/
function formataNum(src){
  	var valor = document.getElementById(src).value;
	if (valor!=''){
		for (i=0;i<3;i++){
			valor = valor.replace('.','');
		}
		valor = valor.replace(',','.');
		valor = formatToPort(valor,2);
		document.getElementById(src).value = valor;
	}
}//fim da funcao

/*
Funcao que faz a formatação do campo para moeda, esta funcao 
não é chamada diretamente, chame apenas a funcao formata_num 
no onChange Parametros: valor, numero de decimais
*/
function formatToPort(expr, decplaces){
  var blNegative = false;
  if (parseFloat(expr)<0){
   blNegative=true;
  }
  var str = '' + Math.round (eval(expr) * Math.pow(10, decplaces));
  while (str.length<=decplaces){
   str =  '0' + str ;
  }
  var decpoint = str.length - decplaces;
  if (blNegative){
   var numStr = str.substring(1, decpoint);
  }
  else{
   var numStr = str.substring(0, decpoint);
  }

  var blChange=false;
  if (numStr.length==4){
   numStr = numStr.substring(0,1) + '.' + numStr.substring(1, numStr.length);
   blChange=true;
  }
  if (numStr.length==5 && !blChange){
   numStr = numStr.substring(0,2) + '.' + numStr.substring(2, numStr.length);
   blChange=true;
  }
  if (numStr.length==6 && !blChange){
   numStr = numStr.substring(0,3) + '.' + numStr.substring(3, numStr.length);
   blChange=true;
  }
  if (numStr.length==7 && !blChange){
   numStr = numStr.substring(0,1) + '.' + numStr.substring(1, 4) + '.' + numStr.substring(4, numStr.length);
   blChange=true;
  }
  if (numStr.length==8 && !blChange){
   numStr = numStr.substring(0,2) + '.' + numStr.substring(2, 5) + '.' + numStr.substring(5, numStr.length);
   blChange=true;
  }
  if (numStr.length==9 && !blChange){
   numStr = numStr.substring(0,3) + '.' + numStr.substring(3, 6) + '.' + numStr.substring(6, numStr.length);
   blChange=true;
  }
  if (blNegative){
    return '-' + numStr + ',' + str.substring(decpoint, str.length);
  }
  else{
   return numStr + ',' + str.substring(decpoint, str.length);
  }

}//fim da funcao

