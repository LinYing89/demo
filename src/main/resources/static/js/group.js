var a1_img;
var a1_txt;
var a2_img;
var a2_txt;
var a3_img;
var a3_txt;

var d1_img;
var d1_txt;
var d2_img;
var d2_txt;
var d3_img;
var d3_txt;

var c1;
var c2;

var d1_btn;
var d2_btn;
var d3_btn;
//A相电流
var li_axA;
//B相电流
var li_bxA;
//C相电流
var li_cxA;
//A相电压
var li_axV;
//B相电压
var li_bxV;
//C相电压
var li_cxV;
//功率因数
var li_yinshu;
var li_axyg;
var li_axwg;
var li_bxyg;
var li_bxwg;
var li_cxyg;
var li_cxwg;

var tem_great_value;
var tem_less_value;

var websocket = null;
var heart;

var stompClient = null;

window.onload = prepare;

function prepare() {

	a1_img = document.getElementById("a1_img");
	a1_txt = document.getElementById("a1_txt");
	a2_img = document.getElementById("a2_img");
	a2_txt = document.getElementById("a2_txt");
	a3_img = document.getElementById("a3_img");
	a3_txt = document.getElementById("a3_txt");
	d1_img = document.getElementById("d1_img");
	d1_txt = document.getElementById("d1_txt");
	d2_img = document.getElementById("d2_img");
	d2_txt = document.getElementById("d2_txt");
	d3_img = document.getElementById("d3_img");
	d3_txt = document.getElementById("d3_txt");
	c1 = document.getElementById("c1");
	c2 = document.getElementById("c2");

	d1_btn = document.getElementById("d1_btn");
	d2_btn = document.getElementById("d2_btn");
	d3_btn = document.getElementById("d3_btn");

	li_axA = document.getElementById("axA");
	//alert(li_axA);
	//li_axA.innerText="3";
	//li_axA.innerText=123;
	li_bxA = document.getElementById("bxA");
	li_cxA = document.getElementById("cxA");
	li_axV = document.getElementById("axV");
	li_bxV = document.getElementById("bxV");
	li_cxV = document.getElementById("cxV");
	li_yinshu = document.getElementById("yinshu");
	li_axyg = document.getElementById("axyg");
	li_axwg = document.getElementById("axwg");
	li_bxyg = document.getElementById("bxyg");
	li_bxwg = document.getElementById("bxwg");
	li_cxyg = document.getElementById("cxyg");
	li_cxwg = document.getElementById("cxwg");

	tem_great_value = document.getElementById("tem_great_value");
	tem_less_value = document.getElementById("tem_less_value");

	var btnSubmit = document.getElementById("btn_submit");

	d1_btn.onclick = function() {
		ctrlClick(0);
	};
	d2_btn.onclick = function() {
		ctrlClick(1);
	};
	d3_btn.onclick = function() {
		ctrlClick(2);
	};
	btnSubmit.onclick = function() {
		var json = new Object();
		json.great = tem_great_value.value;
		json.less = tem_less_value.value;
		send(JSON.stringify(json));
	};

	initWebSocket();

	//alert("test1");
	//test1();
}

function ctrlClick(which) {

	switch (which) {
	case 0:
		send("d1");
		break;
	case 1:
		send("d2");
		break;
	case 2:
		send("d3");
		break;
	}
}

// 0报警,1不报警
function alarm(coding, img, txt, value) {
	if (value == 0) {
		img.setAttribute("class", "card-img-top bg-danger");
	} else {
		img.setAttribute("class", "card-img-top bg-info");
	}
}

// 0开,1关
function device(img, txt, value, btn) {
	if (value == 0) {
		img.setAttribute("class", "card-img-top bg-success");
		txt.innerText = "开";
		btn.setAttribute("class", "col m-1 btn btn-success");
	} else {
		img.setAttribute("class", "card-img-top bg-info");
		txt.innerText = "关";
		btn.setAttribute("class", "col m-1 btn btn-info");
	}
}

function initWebSocket() {
	// 变量ser在index.jsp文件中初始化,读取request的参数需要在jsp文件中
	console.info(serverIp);

	var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.send("/app/refresh", {}, JSON.stringify({'message': 'hello'}));
            //开启定时心跳
            heart = self.setInterval("sendHeart()",5000);

            stompClient.subscribe('/topic/eleInfo', handlerEleInfo);
            stompClient.subscribe('/topic/valueChanged', handlerValueChanged);
        });
}

//处理电力数据
function handlerEleInfo(message){
    var eleInfo = JSON.parse(message.body);
    li_axA.innerText = eleInfo.axA;
    li_bxA.innerText = eleInfo.bxA;
    li_cxA.innerText = eleInfo.cxA;
    li_axV.innerText = eleInfo.axV;
    li_bxV.innerText = eleInfo.bxV;
    li_cxV.innerText = eleInfo.cxV;
    li_yinshu.innerText = eleInfo.yinshu;
    li_axyg.innerText = eleInfo.axyg;
    li_axwg.innerText = eleInfo.axwg;
    li_bxyg.innerText = eleInfo.bxyg;
    li_bxwg.innerText = eleInfo.bxwg;
    li_cxyg.innerText = eleInfo.cxyg;
    li_cxwg.innerText = eleInfo.cxwg;
}

//处理设备值数据
function handlerValueChanged(message){
    var valueData = JSON.parse(message.body);
    console.log('handlerValueChanged : ' + valueData.coding + valueData.value);
    var value = valueData.value;
    switch (valueData.coding) {
		case "a1":
			alarm("a1", a1_img, a1_txt, value);
			break;
		case "a2":
			alarm("a2", a2_img, a2_txt, value);
			break;
		case "a3":
			alarm("a3", a3_img, a3_txt, value);
			break;
		case "d1":
			device(d1_img, d1_txt, value, d1_btn);
			break;
		case "d2":
			device(d2_img, d2_txt, value, d2_btn);
			break;
		case "d3":
			device(d3_img, d3_txt, value, d3_btn);
			break;
		case "c1":
			c1.innerText = value;
			break;
		case "c2":
			c2.innerText = value;
			break;
    }
}

function sendHeart(){
	stompClient.send("/app/eleInfo", {}, JSON.stringify({'message': 'eleInfo'}));
}

function clearTime() {
	disconnect();
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function send(message) {
	if (null != websocket) {
		websocket.send(message);
	}
}

//window.onunload = clearTime;
