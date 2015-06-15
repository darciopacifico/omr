function salvar(){
	elementService = document.getElementById("paramService");
	if (elementService != null){
		elementService.value = "salvar";
	}
	elementService.form.submit();
}

function fechar(){
	elementService = document.getElementById("paramService");
	if (elementService != null){
		elementService.value = "fechar";
	}
	elementService.form.submit();
}

