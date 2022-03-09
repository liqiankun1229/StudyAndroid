! function() {
	// i: h5 可调用方法列表
	// funName: 方法名称
	// param: 是否需要参数
	// reFun: 是否有回调
	// staticType 默认0
	var funcList = [{
		funName: "postRequest",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "chooseImage",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "closeWebview",
		param: !1,
		reFun: !1,
		staticType: 0
	}, {
		funName: "alert",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "confirmAlert",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "showActionSheet",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "showToast",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "showLoading",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "hideLoading",
		param: !1,
		reFun: !1,
		staticType: 0
	}, {
		funName: "getAuthcode",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "searchDevice",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "connectDevice",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "getUserInfo",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "payConform",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "clearDevice",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "wxPay",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "aliPay",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "setTitleBarLeftButton",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "openNewWebView",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "setShareButton",
		param: !1,
		reFun: !0,
		staticType: 0
	}, {
		funName: "appShare",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "isRefreshPage",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "refreshPage",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "pushBuriedPointToGTSDKFromKYH",
		param: !0,
		reFun: !0,
		staticType: 0
	}, {
		funName: "scanCode",
		param: !1,
		reFun: !0,
		staticType: 0
	}, {
		funName: "brightness",
		param: !0,
		reFun: !0,
		staticType: 0
	}];
	// yl 对象 相当于 h5->原生 的工具类
	window.yl = {
		loaded: 0,
		readyType: 0,
		readyList: {},
		ua: window.navigator.userAgent,
		getSystemInfo: function() {
			// 容器校验 版本/app版本/渠道/平台
			var e = this.ua,
				a = {
					h5ContainerVersion: void 0,
					appVersion: void 0,
					systemId: void 0,
					appPlaform: void 0
				};
			try {
				a.h5ContainerVersion = e.split("ylh5ContainerVersion:")[1].split("&yl;")[0]
			} catch (e) {
				console.warn("获取容器版本错误")
			}
			try {
				a.appVersion = e.split("ylAppVersion:")[1].split("&yl;")[0]
			} catch (e) {
				console.warn("获取APP版本错误")
			}
			try {
				a.systemId = e.split("ylsystemId:")[1].split("&yl;")[0]
			} catch (e) {
				console.warn("获取渠道错误")
			}
			try {
				var n = -1 < e.indexOf("Android") || -1 < e.indexOf("Adr"),
					t = !!e.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
				n && (a.appPlaform = "Android"), t && (a.appPlaform = "iOS")
			} catch (e) {
				console.warn("获取APP平台错误")
			}
			return a
		},
		hasReady: function() {
			console.log("event ready!");
			// 准备就绪, 被原生间接调用, 吧
			var e = window.yl.readyList;
			for (var a in e) window.jsbridge.add(e[a]), delete window.yl.readyList[a]
		},
		readydo: function() {},
		ready: function(e) {
			this.readydo = e
		},
		call: function(e, a, n) {
			// e: 方法名  a: 参数  n: 回调
			console.log("方法构造" + e + "==", a);
			var t = function(e) {
				for (var a = 0; a < funcList.length; a++) {
					var n = funcList[a];
					if (e === n.funName) return n
				}
				return !1
			}(e);
			if (!t) return console.log("非法调用"), !1;
			if (t.param && (t.param = a), t.reFun) {
				if (!n) return console.log("需要传入回调");
				t.reFun = n
			}
			if (window.jsbridge) {
				window.jsbridge.add(t);
			} else {
				console.log("不是APP环境")
				var r = "ListItem" + (new Date()).getTime();
				this.readyList[r] = t
			}
		}
	}, document.addEventListener("YLKJBrigeReady", function(e) {
		console.log("event ready!");
		var a = window.yl.readyList;
		// 添加方法
		for (var n in a) window.jsbridge.add(a[n]), delete window.yl.readyList[n]
	}, !1)
}();
