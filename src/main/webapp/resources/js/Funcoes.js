PrimeFaces.locales['pt'] = {
	closeText : 'Fechar',
	prevText : 'Anterior',
	nextText : 'Pr�ximo',
	currentText : 'Hoje',
	monthNames : [ "Janeiro", "Fevereiro", "Mar\u00e7o", "Abril", "Maio",
			"Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro",
			"Dezembro" ],
	monthNamesShort : [ "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago",
			"Set", "Out", "Nov", "Dez" ],
	dayNames : [ "Domingo", "Segunda-feira", "Ter\u00e7a-feira",
			"Quarta-feira", "Quinta-feira", "Sexta-feira", "S\u00e1bado" ],
	dayNamesShort : [ "Dom", "Seg", "Ter", "Qua", "Qui", "Sexta", "S\u00e1b" ],
	dayNamesMin : [ 'D', 'S', 'T', 'Q', 'Q', 'S', 'S' ],
	weekHeader : 'Semana',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'S� Horas',
	timeText : 'Tempo',
	hourText : 'Hora',
	minuteText : 'Minuto',
	secondText : 'Segundo',
	ampm : false,
	month : "m\u00eas",
	week : 'semana',
	day : 'dia',
	allDayText : "todos os dias"
};

function MascaraDeMoeda(objTextBox, e) {
	return MascararDouble(objTextBox, '.', ',', 2, false, e);
}

function MascaraDeQuantidade(objTextBox, e) {
	return MascararDouble(objTextBox, '.', ',', 3, true, e);
}

function MascararDouble(objTextBox, SeparadorMilesimo, SeparadorDecimal,
		CasasDecimais, PermiteNegativo, e) {

	var i = j = 0;
	var len = len2 = 0;
	var strCheck = (PermiteNegativo ? '-' : '') + '0123456789';
	var aux = aux2 = '';
	var whichCode = (window.event) ? event.keyCode : e.which;
	var key = String.fromCharCode(whichCode);
	var ApertouNegativo = '-'.indexOf(key) != -1;
	var JaPossuiNegativo = objTextBox.value.indexOf('-') != -1;

	if (whichCode == 13 || whichCode == 8 || whichCode == 0 || whichCode == 8)
		return true;

	if (strCheck.indexOf(key) == -1)
		return false; // Chave inv�lida

	if (ApertouNegativo && JaPossuiNegativo)
		return false; // j� existe sinal de negativo na string

	len = objTextBox.value.length;

	if (JaPossuiNegativo) {
		// tira o negativo, vai colocar l� no final
		objTextBox.value = objTextBox.value.replace('-', '');
	}

	for (i = 0; i < len; i++) {
		if ((objTextBox.value.charAt(i) != '0')
				&& (objTextBox.value.charAt(i) != SeparadorDecimal))
			break;
	}

	for (; i < len; i++) {
		if (strCheck.indexOf(objTextBox.value.charAt(i)) != -1)
			aux += objTextBox.value.charAt(i);
	}

	for (; i < len; i++) {
		if (strCheck.indexOf(objTextBox.value.charAt(i)) != -1)
			aux += objTextBox.value.charAt(i);
	}

	aux += ApertouNegativo ? '' : key;// SE APERTOU NEGATIVO N�O ADICIONA
	// NADA, VAI ADICIONAR L� NO FINAL

	len = aux.length;

	if (len == 0)
		objTextBox.value = '';

	if (len < CasasDecimais) {
		objTextBox.value = '0' + SeparadorDecimal
				+ repetirString('0', CasasDecimais - len) + aux;
	} else if (len == CasasDecimais) {
		objTextBox.value = '0' + SeparadorDecimal + aux;
	} else if (len >= CasasDecimais) {

		aux2 = '';

		for (j = 0, i = len - (CasasDecimais + 1); i >= 0; i--) {
			if (j == 3) {
				aux2 += SeparadorMilesimo;
				j = 0;
			}
			aux2 += aux.charAt(i);
			j++;
		}

		objTextBox.value = '';
		len2 = aux2.length;

		for (i = len2 - 1; i >= 0; i--)
			objTextBox.value += aux2.charAt(i);

		objTextBox.value += SeparadorDecimal
				+ aux.substr(len - CasasDecimais, len);
	}

	if (ApertouNegativo || JaPossuiNegativo) {
		objTextBox.value = '-' + objTextBox.value;
	}

	return false;
}

function MascararTelefone(objTextBox) {

	mascarar = function(valor) {
		valor = valor.replace(/\D/g, ''); // Remove tudo o que não é dígito
		valor = valor.replace(/^(\d{2})(\d)/g, '($1) $2'); // Coloca parênteses
		// em volta dos dois
		// primeiros dígitos
		// valor = valor.replace(/(\d)(\d{4})$/, '$1-$2'); //Coloca hífen entre
		// o quarto e o quinto dígitos
		valor = valor.replace(/(\d{4})(\d)/, "$1-$2")
		return valor;
	}

	objTextBox.value = mascarar(objTextBox.value)
}

function MascararCNPJ(objTextBox) {

	mascarar = function(valor) {
		valor = valor.replace(/\D/g, ''); // Remove tudo o que não é dígito
		valor = valor.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/g,
				"\$1.\$2.\$3\/\$4\-\$5");
		return valor;
	}

	objTextBox.value = mascarar(objTextBox.value)
}

function MascararCPF(objTextBox) {

	mascarar = function(valor) {
		valor = valor.replace(/\D/g, ''); // Remove tudo o que não é dígito
		valor = valor.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/g,
				"\$1.\$2.\$3\-\$4");
		return valor;
	}

	objTextBox.value = mascarar(objTextBox.value)
}

function repetirString(texto, vezes) {
	if (vezes < 1)
		return '';
	var result = '';
	while (vezes > 0) {
		if (vezes & 1)
			result += texto;
		vezes >>= 1, texto += texto;
	}
	return result;
}

function onlyNumero(e) {
	var tecla = (window.event) ? event.keyCode : e.which;

	if ((tecla > 47 && tecla < 58))
		return true;
	else if (tecla != 8 && tecla != 0)
		return false;
	else
		return true;
}

function bloquearTecla(codigoTecla) {
	var tecla = window.event.keyCode;
	if (tecla == codigoTecla) {
		event.keyCode = 0;
		event.returnValue = false;
	}
}

function bloquearCtrlJ() { // Verifica��o das Teclas
	var tecla = window.event.keyCode; // Para controle da tecla pressionada
	var ctrl = window.event.ctrlKey; // Para controle da Tecla CTRL

	if (ctrl && tecla == 74) { // Evita teclar ctrl + j
		event.keyCode = 0;
		event.returnValue = false;
	}
}

function formEmEstadoBlank(form) {
	form.target = '_blank';
}

function formEmEstadoSelf(form) {
	form.target = '_self';
}

function getRamdonID(idLength) {
	var text = "";
	var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	for (var i = 0; i < idLength; i++)
		text += possible.charAt(Math.floor(Math.random() * possible.length));

	return text;
}

function changeUrlLogotipo(graphicImage, pathImagens) {
	var hostname = location.hostname;
	var img = "";

	if (hostname == 'cliquepetshop.vouclicar.com')
		img = 'logoVouClicar.png';
	else
		img = 'logo.png';

	graphicImage.src = pathImagens + img;
}

function getScreenWidth() {
	return screen.width;
}

function getScreenHeight() {
	return screen.height;
}

function getWindowWidth() {
	return $(window).width();
}

function getWindowHeight() {
	return $(window).height();
}

function getCumprimentoAoUsuario() {
	var t = new Date;
	if (t.getHours() > 5 && t.getHours() < 12)
		return "Bom dia";
	else if (t.getHours() >= 12 && t.getHours() <= 18) {
		return "Boa tarde";
	} else {
		return "Boa noite";
	}
}

function isMobileSafari() {
	return navigator.userAgent.match(/(iPod|iPhone|iPad)/)
			&& navigator.userAgent.match(/AppleWebKit/);
}

function changeFavicon(src) {
	var link = document.createElement('link'), oldLink = document
			.getElementById('dynamic-favicon');
	link.id = 'dynamic-favicon';
	link.rel = 'shortcut icon';
	link.href = src;
	if (oldLink) {
		document.head.removeChild(oldLink);
	}
	document.head.appendChild(link);
}

// function disable(obj, disabled) {
// $(obj).attr('disabled', disabled);
// }
