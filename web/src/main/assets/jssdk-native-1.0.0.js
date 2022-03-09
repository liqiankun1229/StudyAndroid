! function() {
	var n = "callback",
		a = "1",
		c = 1,
		r = {};
	window.jsbridge = {
		add: function(e) {
			/* 判断环境调用原生 */
			console.log(JSON.stringify(e));
			if (!e.funName) return !1;
			var o = void 0;
			null !== e.reFun &&
				"object" == typeof e.reFun &&
				e.reFun.onSuccess &&
				(c && console.log("004------------------\\x3e"),
					c && console.log(e.reFun),
					o = 0 === e.staticType ? n + e.funName + (new Date).getTime() : n + e.funName,
					e.staticType = 0 === e.staticType && e.staticType, r[o] = e.reFun),
				function(e) {
					c && console.log("001" + JSON.stringify(e));
					var o = navigator.userAgent,
						n = -1 < o.indexOf("Android") || -1 < o.indexOf("Adr"),
						r = !!o.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
						s = -1 < o.indexOf(a) || !0;
					n && s && (c && console.log("002"), window.hfiCore.onNative(JSON.stringify(e))),
						r && s && (c && console.log("003"),window.webkit.messageHandlers.core.postMessage(JSON.stringify(e)))
				}({
					funName: e.funName,
					param: e.param ? e.param : null,
					reFun: o || null
				})
		},
		use: function(e) {
			console.log(JSON.stringify(e));
			try {
				e = function(e, o, n) {
				    console.log(e);
                    console.log(o);
                    console.log(n);
					try {
						"string" == typeof e && (e = JSON.parse(e))
					} catch (e) {
						c && console.warn("005")
					}
					var r = e.funMsg;
					try {
						"string" == typeof r && (r = JSON.parse(r))
					} catch (e) {
						c && console.warn("006")
					}
					if (c && console.log("007----------------------\\x3e"),
						c && console.log(JSON.stringify(r)),
						"string" == typeof r.reFun && -1 !== r.reFun.indexOf(o)
					) {
						if (0 === parseInt(e.errorCode)) {
							c && console.log("008--------------\\x3e"), c && console.log(JSON.stringify(n));
							try {
								n[r.reFun].onSuccess({
									errorCode: e.errorCode,
									errorMsg: e.errorMsg,
									param: e.callBackParam
								})
							} catch (e) {
								c && console.error("009")
							} - 1 < r.reFun.indexOf(o + "config") && window.yl.readydo()
						} else try {
							c && console.log("010--------------\\x3e"),
								n[r.reFun].onFail({
									errorCode: e.errorCode,
									errorMsg: e.errorMsg
								})
						} catch (e) {
							c && console.error("011")
						}
						0 === n[r.reFun].staticType && delete n[e.reFun]
					}
					return e
				}(e, n, r)
			} catch (e) {
				c && console.warn("012")
			}
		},
		has: function() {
			window.hfiCore.onNative("1")
		},
		ready: function() {
			window.yl && (c && console.log("013"), window.yl.hasReady())
		}
	}
}();
