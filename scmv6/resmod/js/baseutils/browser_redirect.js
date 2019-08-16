/**
 * 判断当前浏览器和环境跳转对应页面-------
 * @author zzx
 * 使用列子：
 * ---------------------
 * <script type="text/javascript">
 * window.BrowserRedirect({
 * 		"pc_uri":"",
 * 		"mid_uri":"",
 * 		"prompt_uri":"",
 * });
 * </script>
 * -----------------------
 * 静若处子 动若疯兔。
 */
(function (window, document) {
	//Main 方法
	var BrowserRedirect = function(options){
		var self_obj = this;
		var defaults = {
				"pc_uri":"",//有跳转的pc页面
				"mid_uri":"",//有跳转的mid页面
				"prompt_uri":"" //如果没有对应的跳转页面，则跳转统一的提示页面uri
		};
		if (typeof options != "undefined") {
			defaults=$.extend({},defaults, options);
	    }
		self_obj.checkBrowser(defaults);
	};
	//判断当前系统
	BrowserRedirect.prototype.checkBrowser = function(defaults){
		var sUserAgent = navigator.userAgent.toLowerCase();
		// TODO 有新的移动端系统可以接着往这添加
		var reg=/ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i;
		// 获取域名-判断当前环境-判断当前请求是pc还是mid
		var host = window.location.host;
		var environment = host.substr(0,host.indexOf("."));
		//判断当前域名是否是移动端的
		var isMid = environment.indexOf("m")==-1?false:true;
		//请求是移动端，系统是pc端，则跳转pc页面
		if (isMid&&!(reg.test(sUserAgent))){
			if(defaults.pc_uri!=""){
				environment=environment.replace("m","");
				window.location.href="http://"+(environment==""?"www":environment)+".scholarmate.com"+defaults.pc_uri;
				return;
			}
		} 
		//请求是pc端，系统是移动端，则跳转移动页面
		if(!isMid&&reg.test(sUserAgent)) {
			if(defaults.mid_uri!=""){
				window.location.href="http://"+environment.replace("www","")+"m.scholarmate.com"+defaults.mid_uri;
			}
		}
	};
	//主入口
	window.BrowserRedirect = function (options) {
		return new BrowserRedirect(options);
	};
})(window, document);