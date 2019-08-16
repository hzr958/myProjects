<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
var userSys = "${importVo.systemType}";
    //重新定位安装插件提示框
    function showInstallOrUpdateTips(type){
      //提示不支持当前使用的浏览器
      if(!(client.browser.is360() || Sys.ie)){
        smate.showTips._showNewTips(globalTips.not_support_browser, referenceSearch_propmt, "BaseUtils.goBackReferrer()", "234",globalTips.sure);
      }else{
        if("update" == type){
            smate.showTips._showNewTips(referencesearch_msg_updatecab_alert, referenceSearch_propmt, "updateOrInstallPluginPage('update');", "");
        }else{
            var tipMessage = referencesearch_msg_downloadcab_alert;
            smate.showTips._showNewTips(tipMessage, referenceSearch_propmt, "updateOrInstallPluginPage('install');", "");
        }
      }
        $("#alert_box_cancel_btn").hide();
        $("#alert_box_close_btn").hide();
    }
    //跳转更新或安装插件页面
    function updateOrInstallPluginPage(showType){
      if(Sys.chrome){
        if(showType == "update"){
            window.open("https://ext.chrome.360.cn/webstore/search/IrisSearch");
            $("#alert_box_cancel_btn").click();
            setTimeout("navigator.plugins.refresh(true)", 4000);
        }else{
	        location.href = "/pub/import/plugin?downloadType="+showType;
        }
      }else if(Sys.ie){
        location.href = "/pub/import/plugin?downloadType="+showType;
      }else{
        $.smate.scmtips.show('error',globalTips.chooseVersion);
      }
      return false;
    }
    //关闭提示框
    function closeUpdateOrInstallTips(){
        $("#update_or_install-plugin-div").hide();
    }
</script>
