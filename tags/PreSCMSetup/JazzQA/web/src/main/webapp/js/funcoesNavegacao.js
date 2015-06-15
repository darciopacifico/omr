function ocultaPesquisa(){
	if ( document.getElementById("statusAreaPesquisa").value=="none" ) {
		document.getElementById("statusAreaPesquisa").value="block";
		document.getElementById("areaPesquisa").style.display="block";
		document.getElementById("imgExpand").src="../assets/menosVermelho.gif";
		document.getElementById("imgExpand").alt="Ocultar area de pesquisa";
	}
	else {
		document.getElementById("statusAreaPesquisa").value="none";
		document.getElementById("areaPesquisa").style.display="none";
		document.getElementById("imgExpand").src="../assets/maisVermelho.gif";
		document.getElementById("imgExpand").alt="Expandir area de pesquisa";
	}
}
function travarBotao(button){
	button.disabled="true";

}

function invocaServico(servico){
	elementService = document.getElementById("paramService");
	elementService.value = servico;
	elementService.form.submit();
}

function atribuirValor(elementId,valor){
	elementService = document.getElementById(elementId);
	elementService.value=valor;
}

function marcarItem(image){
	var nomeHidden = image.name+".selecionado";

	var hidden = document.getElementById(nomeHidden);

	hidden.value="true";

}

function onKeyPress(keyCode){
	var divErro 	= document.getElementById("divErro");
	var divConteudo = document.getElementById("conteudo");
	
	if(keyCode==27){
		if(divErro.style.display== "block"){
			divErro.style.display = "none";
			divConteudo.style.display = "block";
		}else{
			divConteudo.style.display = "block";
			fechar();
		}
	}
}

function textCounter( field, countfield, maxlimit) {
	var label = " Caracteres restantes"

  if ( field.value.length > maxlimit ){
    field.value = field.value.substring( 0, maxlimit );
    return false;
    
  }else{
    countfield.innerText = " "+(maxlimit - field.value.length) + " " + label;
    
  }
}

/* FUNCOES PARA TRATAR POPUP DE MESG DE ERROS */
function visibilidade(link,divName){
		
	var me = eval(divName);
	var hrefx = eval(link);
		
	if (me.style.display=="none"){
		me.style.display="block";
		hrefx.innerHTML = '<img src="../assets/menosVermelho.gif" border="0">';
	}else{
		me.style.display="none";
		hrefx.innerHTML = '<img src="../assets/maisVermelho.gif" border="0">';
	}
}
	
function fechaDivErro(){
	document.getElementById("divErro").style.display = "none";
	document.getElementById("conteudo").style.display = "block";
	document.getElementById("imgErro").style.display = "block";
	document.getElementById("imgErro").src = "../assets/alert_erro.gif";
}
	
function tdOn(src){
	src.style.cursor = 'hand';
	src.className = 'tt3L';
}

function tdOff(src){
	src.className = 'tt2L';
}

function showErro(){
	document.getElementById("divErro").style.display = "block";
	document.getElementById("conteudo").style.display = "none";
}




function pesquisar(){
	elementService = document.getElementById("paramService");

	if (elementService != null)
	{
		elementService.value = "pesquisar";
	}
	elementService.form.submit();
}
	
function preparaIncluir(){
	elementService = document.getElementById("paramService");
	if (elementService != null)
	{
		elementService.value = "preparaIncluir";
	}
	elementService.form.submit();
}
function preparaAlterar(id){
	elementService = document.getElementById("paramService");
	pesquisaIdSelecionado = document.getElementById("pesquisaIdSelecionado");
	if (elementService != null)
	{
		elementService.value = "preparaAlterar";
		pesquisaIdSelecionado.value = id;
	}
	elementService.form.submit();
}

function preparaConsultar(id){
	elementService = document.getElementById("paramService");
	pesquisaIdSelecionado = document.getElementById("pesquisaIdSelecionado");
	if (elementService != null)
	{
		elementService.value = "preparaConsultar";
		pesquisaIdSelecionado.value = id;
	}
	elementService.form.submit();
}

function excluir(id){

	if (!window.confirm("Deseja realmente excluir este registro ?"))
		return;
		
	elementService = document.getElementById("paramService");
	
	pesquisaIdSelecionado = document.getElementById("pesquisaIdSelecionado");
	if (elementService != null)
	{
		elementService.value = "excluir";
		pesquisaIdSelecionado.value = id;
	}
	elementService.form.submit();
}

function finalizar(){
	elementService = document.getElementById("paramService");
	
	if (elementService != null)
	{
		elementService.value = "finalizar";
	}
	elementService.form.submit();
}

/* FUNCOES PARA EXIBIR MENSAGENS DE DI?LOGO */
var timeOutDialogMessage = 5000;
var strQuebraLinha = "<FIM_MSG>";

	
function possuiMensagens(){
 	var posQuebra = divMensagens.innerHTML.indexOf(strQuebraLinha)
	return (posQuebra>-1);
}
				
function agendarLimpeza(){
	setTimeout("limparUltimaMensagem()",timeOutDialogMessage);
}

function limparUltimaMensagem(){
	var htmlMessages = divMensagens.innerHTML;
	var posQuebra = htmlMessages.indexOf(strQuebraLinha)+strQuebraLinha.length;
	htmlMessages = htmlMessages.substr(posQuebra);
	divMensagens.innerHTML = htmlMessages;
					
	if(possuiMensagens()){
		//alert('reagendando limpeza');
		agendarLimpeza();
	}else{
		divMensagens.innerHTML = "&nbsp;";
	}
}

/*paginacao*/
	function vlhNextPage(){
		invocaServico('vlhNextPage');
	}
	
	function vlhPreviousPage(){
		invocaServico('vlhPreviousPage');
	}
	
	function vlhFirstPage(){
		invocaServico('vlhFirstPage');
	}
	
	function vlhLastPage(){
		invocaServico('vlhLastPage');
	}
	
	function vlhPageByIndex(index){
		var paginaVLH = document.getElementById("paginaVLH");	
		paginaVLH.value=index;
		invocaServico('vlhPageByIndex');
	}


/*ordenacao de registros*/
var icone_ordenacao_ 			= "../assets/ordenacao/ordenacao_.GIF";
var icone_ordenacao_asc 	= "../assets/ordenacao/ordenacao_asc.GIF";
var icone_ordenacao_desc 	= "../assets/ordenacao/ordenacao_desc.GIF";

var prefix_ordenacao = "icone_ordenacao_";

function ordenar(indexOf, img){
	var hiddenTipoOrdenacao = document.getElementById("IVOWrapper.webQueryOrder["+indexOf+"].tipoOrdenacao");
	var sufix_atual = hiddenTipoOrdenacao.value;
	
	try{
		
		if(sufix_atual==""){
			sufix_atual = "asc";
		}else if(sufix_atual=="asc"){
			sufix_atual = "desc";
		}else if(sufix_atual=="desc"){
			sufix_atual = "";
		}	
		
		var icone_ordenacao = eval(prefix_ordenacao+sufix_atual);
					
		img.src=icone_ordenacao;
		hiddenTipoOrdenacao.value = sufix_atual;
	
	}catch(e){
		alert(e.message);
	}
	//Para invocar a pesquisa logo ap?s a configura??o da ordena??o, descomente a linha abaixo
	pesquisar();
}

	
	