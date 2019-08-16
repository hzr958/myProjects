<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="utf-8">
<title>科研之友</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
});
function openShareFile(url){
	location.href=url;
}
//文件下载
function fileDown (fileUrl,obj,e){
	if(obj){
		doHitMore(obj,1000);
	}
	if(e){
		stopNextEvent(e);
	}
	if(fileUrl!=null&&fileUrl!=""){
		window.location.href = fileUrl;
	}
}
/**
 * ----冒泡事件处理-----
 */
 function stopNextEvent(evt){
	if(evt.currentTarget){
		if(evt.stopPropagation){
			evt.stopPropagation();
		}else{
			evt.cancelBubble=true;
		}
	}
};
/**
 * ---防止重复点击--
 */
function doHitMore (obj,time){
	$(obj).attr("disabled",true);
	var click = $(obj).attr("onclick");
	if(click!=null&&click!=""){
		$(obj).attr("onclick","");
	}
	setTimeout(function(){
		$(obj).removeAttr("disabled");
		if(click!=null&&click!=""){
			$(obj).attr("onclick",click);
		}
	},time);
};
</script>
</head>
<body style="background: #fff; margin: 0px; overflow-x: hidden;">
  <div class="page-header page-file__header" style="width: 100%; position: fixed; top: 0px;">
    <div class="page-file__title">文件</div>
  </div>
  <input type="hidden" name="url" value="${status}">
  <div class="page-folder__list-assemblage">
    <s:iterator value="psnFileInfoList" var="sf">
      <div class="page-folder__list" onclick="fileDown('${sf.downloadUrl }',this,event)">
        <div style="display: flex;">
          <s:if test='#sf.fileType=="txt"  '>
            <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_txt.png"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
          </s:if>
          <s:elseif test='#sf.fileType=="ppt" || sf.fileType=="pptx" '>
            <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_ppt.png"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
          </s:elseif>
          <s:elseif test='#sf.fileType=="doc" || sf.fileType=="docx" '>
            <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_doc.png"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
          </s:elseif>
          <s:elseif test='#sf.fileType=="rar" || sf.fileType=="zip" '>
            <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_zip.png"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
          </s:elseif>
          <s:elseif test='#sf.fileType=="xls" || sf.fileType=="xlsx" '>
            <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_xls.png"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
          </s:elseif>
          <s:elseif test='#sf.fileType=="pdf" '>
            <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_pdf.png"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
          </s:elseif>
          <s:elseif test='#sf.fileType=="imgIc"  || sf.fileType=="jpg" || sf.fileType=="png" '>
            <img class="page-folder__list-avator" src="${sf.downloadUrl }"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
          </s:elseif>
          <s:else>
            <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_default.png"
              onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
          </s:else>
          <div class="page-folder__left">
            <div class="page-header__work">${sf.fileName}</div>
            <div class="page-folder__footer">${sf.fileDesc}</div>
          </div>
        </div>
        <div class="page-folder__right">
          <div class="page-header__work">
            <fmt:formatDate value="${sf.uploadDate}" pattern="yyyy-MM-dd" />
          </div>
          <div class="page-folder__footer">${sf.fileSize}</div>
        </div>
      </div>
    </s:iterator>
  </div>
  <c:if test='${status==0}'>
    <div
      style="height: 48px; background-color: #288aed; width: 100%; text-align: center; line-height: 48px; color: #fff; font-size: 18px; position: fixed; over-flow: hidden; bottom: 0px;"
      onclick="openShareFile('${url}')">登录科研之友</div>
  </c:if>
  <c:if test='${status ==1}'>
    <jsp:include page="../../mobile/bottom/mobile_bottom.jsp"></jsp:include>
  </c:if>
  <c:if test="${empty psnFileInfoList}">
    <div style="width: 100%; text-align: center; font-size: 16px; color: #999;">此分享已被取消</div>
  </c:if>
</body>
</html>