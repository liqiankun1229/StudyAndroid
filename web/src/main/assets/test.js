/* eslint-disable no-unused-vars */
/* 类微信小程序调用对象 */
/* 执行一段匿名函数 */
!function () {
    /* 匿名函数 执行 */
    const isDebug = 1;
    /* 方法列表 map 主要保存需要回调的函数对象 */
    const funcList = {};
    /* 调用 */
    let isReady = false;
    /* 相关字段定义 */
    const keyParams = {
        funcName: "funcName",
        params: "params",
        callback: "callback",
        callbackName: "callbackName",
        errCode: "errCode",
        errMsg: "errMsg"
    };
    /* 声明一个 hfiJSBridge 用于两端交互 */
    window.hfiJSBridge = {
        cacheFuncList: function () {
            /* 回去缓存的 回调 map */
            return funcList;
        },
        isAndroid: function () {
            /* Android 环境 */
            return -1 < navigator.userAgent.indexOf("Android") || -1 < navigator.userAgent.indexOf("Adr");
        },
        isIOS: function () {
            /* iOS 环境 */
            return !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        },
        isHfi: function () {
            /* 在 集成 hfiBridgeSDK 的APP 中 -> ua 中加入关键字 */
            return true;
        },
        check: function () {
            /* 校验信息 */

        },
        version: function () {
            /* 版本解析 */
            const ua = window.navigator.userAgent;
            const versionInfo = {};
            if (isDebug) {
                console.log(ua);
                console.log(JSON.stringify(versionInfo));
            }
        },
        strUUID: function () {
            /* 唯一标识 */
            function formatDateTime() {
                const date = new Date();
                const y = date.getFullYear();
                let m = date.getMonth() + 1;
                m = m < 10 ? ('0' + m) : m;
                let d = date.getDate();
                d = d < 10 ? ('0' + d) : d;
                const h = date.getHours();
                const minute = date.getMinutes();
                const second = date.getSeconds();
                return y + m + d + h + minute + second;
            }

            return formatDateTime() + Math.random().toString(36).substr(2);
        },
        call: function (funName, params, callback) {
            /* web 调用原生的中间类 判断是否有权限调用原生方法 */
            if (isDebug) {
                console.log(funName);
                console.log(JSON.stringify(params));
                console.log(JSON.stringify(callback));
            }
            /* 调用原生方法 */
            const hfiParams = {};
            /* 校验方法 是否合规 构造需要传递参数 */
            /* funName string 对象 */
            if (funName === "") {
                isDebug && console.log("----------funName 为空----------");
                return 0;
            }
            /* params map 对象 需要参数的时候验证/或者不验证-直接传递入参到原生 */

            /* callback object 对象 回调后 */
            if (typeof callback === "object") {
                if (typeof callback.onSucceed === "function" && typeof callback.onFailed === "function") {
                    /* 满足调用 */
                    /* 判断环境 */
                    if (this.isHfi()) {
                        /* 保存 当前的回调 */
                        /* 生成key funName + this.strUUID()*/
                        const key = funName + this.strUUID();
                        /* 保存到 funcList 中 */
                        funcList[key] = callback;
                        /* 构造传递对象 */
                        hfiParams[keyParams.funcName] = funName;
                        hfiParams[keyParams.params] = params;
                        hfiParams[keyParams.callbackName] = key;

                        /* 根据环境调用对应方法 */
                        /* ua 加载后才能判断环境- ua加载的时机非常早,main.js过程就可以访问 */
                        console.log(navigator.userAgent);
                        console.log("iOS:" + this.isIOS());
                        console.log("Android:" + this.isAndroid());
                        if (this.isAndroid) {
                            /* Android 环境 hifCore 是 原生 WebView 挂载到 window上的对象 onNative 是原生定义可调用的方法*/
                            window.hfiCore.onNative(JSON.stringify(hfiParams));
                        } else if (this.isIOS()) {
                            /* iOS 环境 */
                            window.webkit.messageHandlers.core.postMessage(JSON.stringify(hfiParams));
                        } else {
                            /* 不是app 中,只打印入参 log */
                            console.log(JSON.stringify(hfiParams));
                        }
                    } else {
                        isDebug && console.log("----------不是hzfi 环境----------");
                        return 0
                    }
                } else {
                    isDebug && console.log("----------callback 验证未通过----------");
                    return 0
                }
            } else if (null === callback) {
                isDebug && console.log("----------callback 不是object----------");
                return 0;
            }
        },
        onWeb: function (e) {
            /* 原生传参 web解析 */
            /* 格式 json 字符串 参数 errCode, errMsg, callbackName */
            try {
                const params = JSON.parse(e);
                console.log(JSON.stringify(e));
                const callback = funcList[params[keyParams.callbackName]];
                if (typeof callback === "object") {
                    if (params.errCode.toString() === "0") {
                        /* 调用成功 */
                        callback.onSucceed(params.params);
                    } else {
                        /* 调用失败 */
                        callback.onFailed(params.errCode, params.errMsg);
                    }
                    /* todo 判断是否删除缓存回调的方法 */

                } else {
                    return "非法调用";
                }
            } catch (err) {
                console.log(JSON.stringify(err));
            }
        },
        onReady: function () {
            /* 原生调用 isReady 后修改状态,表示 bridge 已经注入完毕 会给原生返回状态 */
            isReady = true;
            return isReady;
        },
        /* 方法区 */
        openWebView: function () {
            /* 打开网页 - 新窗口 */
            this.call("openWebView", {}, {
                onSucceed: function (e) {

                },
                onFailed: function (code, msg) {

                }
            });
        },
        closeWebView: function () {
            /* 关闭网页 - 当前窗口 多个窗口情况下逐一关闭(调用一次关闭一个) */
            this.call("closeWebView", {}, {
                onSucceed: function (e) {

                },
                onFailed: function (code, msg) {

                }
            });
        },
        getNetWorkInfo: async function (callback) {
            /* 网络状态 */
            this.call("getNetWorkInfo", {}, callback)
        },
        getDeviceInfo: function (callback) {
            /* 设备详细信息 */
            this.call("getDeviceInfo", {}, callback)
        },
        getVersionInfo: function (callback) {
            /* 获取版本信息 */
            this.call("getDeviceInfo", {}, callback)
        },
        brightness: function (params, callback) {
            /* 亮度调节 - 参数校验 */
            this.call("brightness", params, callback)
        },
        snapshot: function (callback) {
            /* 保存截图 */
            this.call("snapshot", {}, callback)
        },
        openLight: function (params, callback) {
            /* 打开手电筒(闪光灯) - 参数校验 */
            this.call("openLight", params, callback)
        },
        volume: function (params, callback) {
            /* 音量调节 */
            this.call("volume", params, callback)
        },
        scanCode: function (callback) {
            /* 扫码 */
            this.call("scanCode", {}, callback);
        },
        openThirdApp: function () {
            /* 打开三方应用 */
        },
        saveDesktop: function () {
            /* 保存到桌面快捷方式 */
        },
        chooseImage: function (callback) {
            /* 拍照/相册选择图片 - (先弹出选择对话框) */
            this.call("chooseImage", {}, callback)
        },
        openPage: function (params, callback) {
            /* 跳转特定界面 - 路径必须满足 aRouter 规范 */
            this.call("openPage", params, callback)
        },
        alert: function () {
            /* 弹框 - 是否需要使用统一弹框 */
        },
        confirm: function () {
            /* 确认框 - 支持取消 - 是否需要使用统一弹框 */
        },
        toast: function () {
            /* toast - 没有按钮, 定时消失 - 是否需要使用统一弹框 */
        },
        sheetList: function (params, callback) {
            /* 选择列表 - 是否需要使用统一弹框 */
            /* 传入 list 返回选择的条目 */

        },
        setWebTitle: function (params, callback) {
            /* 设置标题 */
            this.call("setWebTitle", params, callback)
        },
        setNavBackgroundColor: function (params, callback) {
            /* 设置导航栏背景颜色 */
            this.call("setNavBackgroundColor", params, callback)
        },
        setOptionMenu: function (params, callback) {
            /* 设置菜单 */
            this.call("setOptionMenu", params, callback)
        },
        showLoading: function () {
            /* 显示加载框 -> 原生加载框 */
            this.call("showLoading", {})
        },
        hideLoading: function () {
            /* 隐藏加载框 -> 原生加载框  */
        },
        appShare: function (params, callback) {
            /* 分享 */
            this.call("appShare", params, callback)
        },
        login: function (callback) {
            /* 统一用户 - 授权登录 */
            this.call("login", {}, callback)
        },
        userInfo: function (callback) {
            /* 用户信息 */
            this.call("userInfo", {}, callback)
        },
        actionFuncByName: function (name, params, callback) {
            /* 扩展函数 通过 name 调用到原生的 对应方法 */
            this.call(name, params, callback)
        },
        closeCORS:function(callback){
            this.call("closeCORS", {}, callback)
        }

    }
}();
