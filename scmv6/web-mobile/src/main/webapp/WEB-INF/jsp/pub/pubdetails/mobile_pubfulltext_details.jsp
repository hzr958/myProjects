<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css" />
<link href="${resmod}/mobile/css/common.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/fastclick.min.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>

<title>科研之友</title>
</head>
<script type="text/javascript">
var ctxpath = '/scmwebsns';
$(function(){
	   $("body").on("swipeleft",function(){
    	   var pageNo = parseInt($("#detailPageNo").val());
    	   var pageCount = parseInt($("#detailPageCount").val())-1;
    	   if (pageNo < pageCount) {
	       $("#detailPageNo").val(pageNo+1);
	        Msg.oneDetails($("#detailPageNo").val(),"psnpub");
    	   }
	    });
	    
	    $("body").on("swiperight",function(){
            var pageNo = parseInt($("#detailPageNo").val());
	        if (pageNo >0) {
	        $("#detailPageNo").val(pageNo-1);
	        Msg.oneDetails($("#detailPageNo").val(),"psnpub");
			}
	    });
	    //隐藏jquery.mobile.js自动加的div块
	    $(".ui-loader").remove();
});
//解决触摸屏300毫秒延时
window.addEventListener('load', function() {
      FastClick.attach(document.body);
}, false);
function downloadFulltext(url){
  if(!!url && url != ""){
    Smate.confirm("下载提示", "要下载全文附件吗？", function(){
            window.location.href = url;
    },["下载", "取消"]);
};
}
</script>
<body>
  <c:if test="${fn:length(fulltextList) > 0 }">
    <input id="detailPageNo" type="hidden" value="${detailPageNo}" />
    <input id="detailPageCount" type="hidden" value="${totalCount}" />
    <input type="hidden" id="currentDes3PsnId" value="<iris:des3 code='${model.psnId}'/>" name="currentDes3PsnId" />
    <div class="new-full_text-container">
      <div class="new-full_text-container_header">
        <i onclick="Msg.confimpubftrcmd('${model.toBack}','${model.whoFirst }');" style = "width:10vw;" class="material-icons new-full_text-container_header-left">keyboard_arrow_left</i>
        <span class="new-full_text-container_header-middle" style = "width:80vw;">全文认领</span>
        <a id="fullTextFileId" onclick="downloadFulltext('${fulltextList[0].downloadUrl}')" class="fr" style="color:white;padding-right: 5px;width:10vw;">
            <i class="material-icons file_download"></i>
        </a>
      </div>
      <div class="new-full_text-container_body dev_details_sub">
        <%@ include file="mobile_pubfulltext_details_sub.jsp"%>
      </div>
    </div>
  </c:if>
  <c:if test="${fn:length(fulltextList) == 0 }">
    <div class="no_msg"text-align:center;">此论文已被删除</div>
  </c:if>
</body>
</html>
