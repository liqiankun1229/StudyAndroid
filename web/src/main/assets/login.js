function onNative() {
	var param = {
		funName: "userInfo",
		params: {},
		back: {}
	};

	window.android.onNative(JSON.stringify(param));
}

function sendInfoToJava() {
	var name = document.getElementById("name_input").value;
	window.android.showInfoFormJs(name);
}

function showInfoFormJava(msg) {
	alert("android : " + msg);
	document.getElementById("name_input2").value = msg;
	loginSuccess()
}

function showAlert() {
	var inputValue = document.getElementById("name_input").value;
	document.getElementById("name_input2").value = inputValue;
	alert("测试 Alert ！" + inputValue);
}

function loginSuccess() {
	var name = document.getElementById("name_input2").value;
	window.android.toastFromJs(name)
}

function kotlinToast(s) {
	var name = document.getElementById("name_input3").value;
	window.android.toastJs(name)
	return "kot"
}
