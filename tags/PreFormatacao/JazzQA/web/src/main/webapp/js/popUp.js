function showErro(){
	document.getElementById("divErro").style.display = "block";
	document.getElementById("conteudo").style.display = "none";
}//fim da funcao


function invocaServico(servico){
	elementService = document.getElementById("paramService");
	elementService.value = servico;
	elementService.form.submit();
}

function avaliaTipoPopUp(checkedItem, multiplaEscolha){
// testa se e multipla escolha
	if(multiplaEscolha!=true){
		//nao e multipla escolha, por tanto faz o onlyOne() e submita
		onlyOne(checkedItem);
		invocaServico('confirmaPopUp');
	}
}
	
function onlyOne(checkedItem){
	try{

		nome = checkedItem.name;

		indexColchete1 = nome.indexOf("[")
		indexColchete2 = nome.indexOf("]")
	
		nomeParte1 = nome.substring(0,indexColchete1+1)
		nomeParte2 = nome.substring(indexColchete2)
	
		i=0;

		do{
		
			var checkDaVez = document.getElementById(nomeParte1+i+nomeParte2);

			if(checkDaVez!=null && checkDaVez != checkedItem){	
				checkDaVez.checked=false;
			}else if(checkDaVez!=null && checkDaVez == checkedItem){
				checkDaVez.checked=true;
				//event.returnValue = false;
			}

			i++;

		}while(checkDaVez!=null)
	}catch(e){	
		alert(e.message);
	}
}

/*paginacao popup*/
function paginarPara(servicoDePaginacao){
	var hidderPaginacao = document.getElementById("popUpController.paginarPara");
	hidderPaginacao.value=servicoDePaginacao;
	invocaServico('paginarPopUp')
}

function vlhNextPage(){
	paginarPara('VLHPopUpController.vlhNextPage');
}

function vlhPreviousPage(){
	paginarPara('VLHPopUpController.vlhPreviousPage');
}

function vlhFirstPage(){
	paginarPara('VLHPopUpController.vlhFirstPage');
}

function vlhLastPage(){
	paginarPara('VLHPopUpController.vlhLastPage');
}

function vlhPageByIndex(index){
	paginarPara(index);	
}

