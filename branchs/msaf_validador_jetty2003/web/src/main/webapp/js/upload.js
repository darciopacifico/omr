function doUploadECotacao(){
  elementService = document.getElementById("dispatchMethod");
    if (elementService != null){
       elementService.value = "doUploadECotacao";
    }
  elementService.form.submit();
}
function doProcessamento(){
  elementService = document.getElementById("dispatchMethod");
    if (elementService != null){
       elementService.value = "doProcessamento";
    }
  elementService.form.submit();
}
