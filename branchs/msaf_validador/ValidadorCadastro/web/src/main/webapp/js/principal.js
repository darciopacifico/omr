//Identificador de sele��o do tr
var trSelecionado = false;
var trObjetoSelecionado = null;

// Fun��es globais
function mudaCor(trElemento, ligarCor) {
	if (trSelecionado)
		return;
}



function mudarCorTR(modificaElemento, ligarCor) {

	// Somente efetua a modifica��o se o elemento n�o estiver selecionado
	if (trSelecionado)
		return;

	// Variaveis
	var cor = "";

	// Verifica qual a cor a selecionar
	if (ligarCor)
		cor = "#E5E9EC";
	else
		cor = "";

	// Monta a cor do elemento conforme a solicita��o
	for (x = 0; x < modificaElemento.cells.length; x++)
		modificaElemento.cells(x).style.backgroundColor = cor;
}

function sincronizarScrollTB(dvOrigemTB, tbScrollResize) {
	tbScrollResize.style.width = parseInt(dvOrigemTB.style.width) - 15;
}

function MM_preloadImages() { // v3.0
	var d = document;
	if (d.images) {
		if (!d.MM_p)
			d.MM_p = new Array();
		var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
		for (i = 0; i < a.length; i++)
			if (a[i].indexOf("#") != 0) {
				d.MM_p[j] = new Image;
				d.MM_p[j++].src = a[i];
			}
	}
}
// abrir links em janela popup

function MM_openBrWindow(theURL, winName, features) { // v2.0
	window.open(theURL, winName, features);
}

// utilizado para limpar o input text quando ganha foco

function limparPadrao(campo) {
	if (campo.value == campo.defaultValue) {
		campo.value = "";
	}
}

function escreverPadrao(campo) {
	if (campo.value == "") {
		campo.value = campo.defaultValue;
	}
}

function MM_swapImgRestore() { // v3.0
	var i, x, a = document.MM_sr;
	for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
		x.src = x.oSrc;
}
function MM_findObj(n, d) { // v4.01
	var p, i, x;
	if (!d)
		d = document;
	if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
		d = parent.frames[n.substring(p + 1)].document;
		n = n.substring(0, p);
	}
	if (!(x = d[n]) && d.all)
		x = d.all[n];
	for (i = 0; !x && i < d.forms.length; i++)
		x = d.forms[i][n];
	for (i = 0; !x && d.layers && i < d.layers.length; i++)
		x = MM_findObj(n, d.layers[i].document);
	if (!x && d.getElementById)
		x = d.getElementById(n);
	return x;
}

function MM_swapImage() { // v3.0
	var i, j = 0, x, a = MM_swapImage.arguments;
	document.MM_sr = new Array;
	for (i = 0; i < (a.length - 2); i += 3)
		if ((x = MM_findObj(a[i])) != null) {
			document.MM_sr[j++] = x;
			if (!x.oSrc)
				x.oSrc = x.src;
			x.src = a[i + 2];
		}
}
function MM_showHideLayers() { // v9.0
	
	var i, p, v, obj, args = MM_showHideLayers.arguments;
	for (i = 0; i < (args.length - 2); i += 3)
		with (document)
			if (getElementById && ((obj = getElementById(args[i])) != null)) {
				v = args[i + 2];
				if (obj.style) {
					obj = obj.style;
					v = (v == 'show') ? 'visible' : (v == 'hide') ? 'hidden'
							: v;
				}
				obj.visibility = v;
			}
}
