<%@ page language="java" pageEncoding="UTF-8"%>
<input type="hidden" id="queryString" name="queryString" value="${queryString}" />
<script type="text/javascript">
var contextLang="${lang}";
function chromeLoginOut(){
    logoutSys('${domaincas}','${locale}','${logoutindex}');
}
</script>
<!-- IE浏览器中引入插件 begin -->
<div style='display: none; visibility: hidden;'>
  <object classid='clsid:4FA0F169-4CF7-4CE7-A2A1-FF9FC5C7356C' id='IrisOctopus'></object>
</div>
<script for="IrisOctopus" event="OnJobCompleted(param1,param2,param3,param4)">
if(Sys.ie.indexOf("11.0")!=-1){
	OnUpdate_ie(param1,param2,param3,param4);	
}
</script>
<!-- IE浏览器中引入插件 end -->
<!-- 其他浏览器中引入插件 begin -->
<script type="text/javascript">
if(Sys.firefox)
document.write('<embed id="IrisEmbedOctopus" type="application/Octopus-plugin" width="0" height="0"></embed>');
if(Sys.chrome)
document.write('<embed id="IrisChromeOctopus" type="application/Chrome-plugin" width="0" height="0"></embed>');
if (Sys.safari)
document.write('<embed id="IrisSafariOctopus" type="application/safari-Splugin" width="0" height="0"></embed>');
</script>
<!-- 其他浏览器中引入插件 end -->
<input type="button" value="取消检索" class="button close_div" id="closeDiv" style="display: none" />
<input type="hidden" value="${locale}" id="lang" />
<div id="CmdEventDiv" style="display: none"></div>
<div id="CookieDiv" style="display: none">
  <span id="cookie_name"></span> <span id="cookie_value"></span> <span id="cookie_domain"></span>
</div>
<div id="SetcookieDiv" style="display: none">
  <span id="cookie_url"></span> <span id="cookie_str"></span>
</div>
<!-- 插件那边有个js方法会往下面的span中写入插件的版本号 -->
<div id="CmdVerDiv" style="display: none">
  <span id="chrome_ver"></span>
</div>
<div id="Chrome_DelExtDiv"></div>
<div id="Chrome_DelOldExtDiv"></div>
<script type="text/javascript">
$(document).ready(function() {
    //TODO 提示页面迁移
	if(!detectBrowsers() && !Sys.safari){
		jAlert(referencesearch_not_support_browser,referencesearch_msg_alert,function(){
			location.href="${snsctx}/publication/msgTipCAB";
		});
		return;
	}
	if(Sys.ie){
		detectOctopus("search");
	}else if(Sys.chrome){
        if(client.browser.is360()){
          checkBrowserIsHideStyle();
        }
		//原先有个判别chrome和360极速浏览器的操作，现在chrome浏览器不支持了，所以去掉了
		detectChromePlugin("search");
		//添加myCookieEvent事件监听，插件那边会触发这个事件
		addChromeCookieEventListen();
	}else{
	    showInstallOrUpdateTips("install");
	}
});
</script>