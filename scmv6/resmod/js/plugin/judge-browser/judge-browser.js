/**
 * 浏览器识别插件
 * 提供的方法：
 * Client()		-- 使用var client = new Client(mode)的方式来获取一个客户端对象，
 * 				-- 该对象包含了browser、engine、system三个对象属性，每个对象属性都有name、version两个属性
 * 				-- browser： 包含了浏览器的名称和版本
 * 				-- engine：	包含了浏览器引擎的名称和版本
 * 				-- system：	包含了操作系统的名称和版本
 * 方法参数mode：可以传"compatiable" 或者 "real"，默认值是 "real"
 * 				"compatiable"表示优先获取兼容模式的版本信息
 * 				"real"标识优先获取浏览器真实的版本信息
 * 				此参数仅对ie有效。
 * 引用本插件后，会默认以mode="real"方式自动创建一个全局变量client,以供使用，如需覆盖，再次创建该变量即可。
 * @author houchuanjie
 */
;(function(window, document) {
	var Engine = function() {
		var name = "unknown";
		var version = "unknown";
	};

	var System = function() {
		var name = "unknown";
		var version = "unknown";
	};

	var Browser = function() {
		var name = "unknown"; //浏览器名称
		var version = "unknown"; //浏览器版本号
	};
	var Client = function() {
		this.browser = new Browser(),
		this.engine = new Engine(),
		this.system = new System(),
		this.mode = arguments[0] || "real";
		this.init();
		return {browser: this.browser, engine: this.engine, system: this.system};
	};
	Client.prototype = {
		init: function(){
			this.judgeBrowser();
			this.judgeSystem();
		},
		judgeBrowser: function() {
			var ua = navigator.userAgent.toString();
			if (/AppleWebKit\/(\S+)/.test(ua)) { //匹配Webkit内核浏览器(Chrome、Safari、新Opera、新Konqueror)
				this.engine.name = "WebKit";
				this.engine.version = RegExp["$1"];
				if (/OPR\/(\S+)/.test(ua)) { //确定是不是引用了Webkit内核的Opera
					this.browser.name = "Opera";
					this.browser.version = RegExp["$1"];
				} else if (/Edge\/(\S+)/.test(ua)) {
					this.browser.name = "Edge";
					this.browser.version = RegExp["$1"];
				} else if (/Chrome\/(\S+)/.test(ua)) { //确定是不是Chrome
					this.browser.name = "Chrome";
					this.browser.version = RegExp["$1"];
				} else if (/konqueror\/(\S+)/.test(ua)) { //
					this.browser.name = "Konqueror";
					this.browser.version = RegExp["$1"];
				} else if (/Safari\/(\S+)/.test(ua)) { 
					this.browser.name = "Safari";
					if (/Version\/(\S+)/.test(ua)) {	//确定是不是高版本（3+）的Safari
						this.browser.version = RegExp["$1"];
					} else { 						//近似地确定低版本Safafi版本号
						var SafariVersion = 1;
						var wk = parseFloat(engine.version);
						if (wk < 100) {
							SafariVersion = 1;
						} else if (wk < 312) {
							SafariVersion = 1.2;
						} else if (wk < 412) {
							SafariVersion = 1.3;
						} else {
							SafariVersion = 2;
						}
						this.browser.version = SafariVersion;
					}
				}
			} else if (window.opera) { //只匹配拥有Presto内核的老版本Opera 5+(12.15-)
				this.engine.name = "Presto";
				this.browser.name = "Opera";
				this.engine.version = this.browser.version = window.opera.version();
			} else if (/Opera[\/\s](\S+)/.test(ua)) { //匹配不支持window.opera的Opera 5-或伪装的Opera
				this.engine.name = "Presto";
				this.browser.name = "Opera";
				this.engine.version = this.browser.version = RegExp["$1"];
			} else if (/KHTML\/(\S+)/.test(ua)) { //KHTML内核
				this.browser.name = "Konqueror";
				this.engine.name = "KHTML";
				this.engine.version = this.browser.version = RegExp["$1"];
			} else if (/Konqueror\/([^;]+)/.test(ua)) { //Konqueror内核
				this.browser.name = "Konqueror";
				this.engine.name = "Konqueror";
				this.engine.version = this.browser.version = RegExp["$1"];
			} else if (/rv:([^\)]+)\) Gecko\/\d{8}/.test(ua)) { //判断是不是基于Gecko内核
				this.engine.name = "Gecko";
				this.engine.version = RegExp["$1"];
				if (/Firefox\/(\S+)/.test(ua)) { //确定是不是Firefox
					this.browser.name = "Firefox";
					this.browser.version = RegExp["$1"];
				}
			} else if (/Trident\/([\d\.]+)/.test(ua)) { //确定是否是Trident内核的浏览器（IE8+）
				this.engine.name = "Trident";
				this.engine.version = RegExp["$1"];
				this.browser.name = "IE";
				if(this.mode === "real"){
					this.browser.version = ({
						"7.0" : "11.0",
						"6.0" : "10.0",
						"5.0" : "9.0",
						"4.0" : "8.0"
					})[RegExp["$1"]];
				}else if(this.mode === "compatible"){
					if (/rv\:([\d\.]+)/.test(ua) || /MSIE ([^;]+)/.test(ua)) { //匹配IE8-11+
						this.browser.version = RegExp["$1"];
					}
				}
			} else if (/MSIE ([^;]+)/.test(ua)) { //匹配IE6、IE7
				this.browser.name = "IE";
				this.browser.version = RegExp["$1"];
				this.engine.name = "Trident";
				this.engine.version = this.browser.version - 4.0; //模拟IE6、IE7中的Trident值
			}
		},
		judgeSystem: function() {
			var ua = navigator.userAgent.toString();
			var p = navigator.platform; //判断操作系统
			this.system.name = p.indexOf("Win") == 0 ? "Windows" : this.system.name;
			this.system.name = p.indexOf("Mac") == 0 ? "Mac" : this.system.name;
			this.system.name = p.indexOf("Linux") > -1 ? "Linux" : p.indexOf("SunOS") > -1 ? "Solaris" : p.indexOf("FreeBSD") > -1 ? "FreeBSD" : p
				.indexOf("X11") > -1 ? "X11" : this.system.name;
			if (this.system.name == "Windows") {
				if (/Win(?:dows )?([^do]{2})\s?(\d+\.\d+)?/.test(ua)) {
					if (RegExp["$1"] == "NT") {
						this.system.version = ({
							"5.0": "2000",
							"5.01":"2000 SP1",
							"5.1": "XP",
							"5.2": "Server 2003",
							"6.0": "Vista",
							"6.1": "7",
							"6.2": "8",
							"6.3": "8.1",
							"10.0": "10"
						})[RegExp["$2"]] || "NT " + RegExp["$2"];
					} else if (RegExp["$1"] == "9x") {
						this.system.version = "ME";
					} else {
						this.system.version = RegExp["$1"];
					}
				}
			}
		}
	};
	var browserAnalyze = function(){
		var client = new Client();
		if((client.browser.name == "IE" && parseFloat(client.browser.version) < 11.0)
			|| (client.browser.name == "Chrome" && parseFloat(client.browser.version) < 38.0)
			|| (client.browser.name == "Firefox" && parseFloat(client.browser.version) < 40.0)){
			//window.location.href = "/scmwebsns/upgrade-browser";
		}
		/*console.log(client.browser.name + " " + client.browser.version);*/
	}
	window.Client = Client;
	window.client = new Client();
	var smate = smate || {};
	window.smate = smate;
	window.smate.browserAnalyze = browserAnalyze;
}(window, document))