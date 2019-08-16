<!doctype html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!--  科研之友 -->
<link href="${resmod}/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css_v5/css2016/scmjscollection.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js_v8/pub/language/search_import_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/jquery.js"></script>
<link href="${resmod}/css/plugin/searchplugindownload.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/collect/collect.global.js"></script>
<script type=text/javascript src="${resmod}/js/search/searchplugin/iris_Octopus.html"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/smate.scmtips.css" />
<script type=text/javascript src="${res}/js_v5/collect/collect.global_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/judge-browser/browser.match.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript">
var returnInt = '${returnInt}';
var searchType = "${importVo.searchType}";
$(document).ready(function() {
  if(!(client.browser.is360() || Sys.ie)){
    smate.showTips._showNewTips(globalTips.not_support_browser, globalTips.search_tips_title, "BaseUtils.goBackReferrer()", "",globalTips.sure);
    $("#alert_box_cancel_btn").hide();
    $("#alert_box_close_btn").hide();
  }
        loopCheckIsInstalled();
});
//循环判断当前计算机是否已经安装了成果检索工具
function loopCheckIsInstalled() {
    try {
      var  forwardUrl = "/pub/import/search";
      if("prj" == searchType){
        forwardUrl = "/prjweb/import";
      }
        var isInstall = isSearchPlusInstalled();
        if (isInstall) {
            if (Sys.chrome){
                if(returnInt!=3){
                    location.href = forwardUrl;
                }
                return;
            }else if(Sys.safari){
                location.href = forwardUrl + "?timestamp="+ Date.parse(new Date());
                return;
            }else{
                 window.history.back(); 
            return;
            }
        }
    } catch (e) {
        location.href = forwardUrl;
        return;
    }
    setTimeout("loopCheckIsInstalled()", 2000);
}
</script>
</head>
<body>
  <div class="spd_body_content">
    <div class="spd_title">
      <spring:message code="referencesearch.tip.title" />
      
    </div>
    <div class="spd_content">
      <div class="spd_section" onclick="dowOctopus('${importVo.systemType}','360');">
        <div class="spd_section_title">
          <spring:message code="referencesearch.tip.chrombrowsers" />
          
        </div>
        <div class="spd_section_logo_container">
          <div style="margin-right: 16px;">
            <img src="${resmod}/images/plugin/360_logo1.png" class="spd_section_logo">
          </div>
          <div>
            <img src="${resmod}/images/plugin/360_logo2.png" class="spd_section_logo">
          </div>
        </div>
        <div class="spd_section_operations">
          <div class="btn_normal btn_bg_origin raised">
            <spring:message code="referencesearch.tip.downplugs" />
          </div>
        </div>
      </div>
      <div class="spd_section" onclick="dowOctopus('${importVo.systemType}','IE');">
        <div class="spd_section_title">
          <spring:message code="referencesearch.tip.iebrowsers" />
        </div>
        <div class="spd_section_logo_container">
          <div>
            <img src="${resmod}/images/plugin/IE_logo.png" class="spd_section_logo">
          </div>
        </div>
        <div class="spd_section_operations">
          <div class="btn_normal btn_bg_origin raised">
            <spring:message code="referencesearch.tip.downplugs" />
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
