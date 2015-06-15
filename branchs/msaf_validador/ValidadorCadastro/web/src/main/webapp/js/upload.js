function doUploadECotacao() {
	elementService = document.getElementById("dispatchMethod");
	if (elementService != null) {
		elementService.value = "doUploadECotacao";
	}
	elementService.form.submit();
}

function doProcessamento() {
	elementService = document.getElementById("dispatchMethod");
	if (elementService != null) {
		elementService.value = "doProcessamento";
	}
	elementService.form.submit();
}

function doAction(actionName) {
	elementService = document.getElementById("dispatchMethod");
	if (elementService != null) {
		elementService.value = actionName;
	}
	elementService.form.submit();
}

function aternarVisualizacao(origem, destino) {

	desabilitaTodosInputDaDiv(origem, true);
	desabilitaTodosInputDaDiv(destino, false);
	
}

function desabilitaTodosInputDaDiv(idObjeto, valorDisable) {
	
	var objeto = document.getElementById(idObjeto);
	
	var inputs = objeto.getElementsByTagName("input");
	
	for (i = 0; i < inputs.length; i++) {
		inputs[i].disabled = valorDisable;
	}	
	
}


function controlaCheckBoxTipoConsulta() {
	
	var theForm = document.forms[0];
	
	var checkbox = theForm.checkBoxTipoPesquisa;
	
	var selecionado = false;
		
	for(i=0; i < checkbox.length; i++) {
		
		// Completa
		if((checkbox[i].value == "899") && (checkbox[i].checked)) {
			
			checkbox[++i].checked = false;
			checkbox[i].disabled = true;
			
			checkbox[++i].checked = false;
			checkbox[i].disabled = true;

			selecionado = true;

			break;
			
		} else if (checkbox[i].checked) {
			
			checkbox[0].checked = false;
			checkbox[0].disabled = true;
			
			selecionado = true;
			
			break;
		}
		
	}

	if(!selecionado) {
		
		for (i = 0; i < checkbox.length; i++) {
			
			checkbox[i].disabled = false;
			checkbox[i].checked = false;
		}
	}	
	
	
}




