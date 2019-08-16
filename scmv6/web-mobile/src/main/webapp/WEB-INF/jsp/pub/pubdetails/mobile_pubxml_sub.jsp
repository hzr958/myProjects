<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript">
$(function(){	
    $.post(
        "/psnweb/outside/ajaxsignature",
        {"currentUrl": encodeURIComponent(window.location.href)},
        function(data){
            if(data.result == "success"){
                smatewechat.customWeiXinShare(
                        data.appId,
                        data.timeStamp, 
                        data.nonceStr,
                        data.signature,
                        $("#pubTitleShare").text(),
                        data.domain + "/pub/outside/details/snsnonext?des3PubId=${pubDetailVO.des3PubId}",
                        data.domain + "/resmod/mobile/images/scm_icon_share.png",
                        $("#pubAbs").text());
            }
        },"json");
    $("#des3PubId").val($("#des3PubId2").val());
	mobile.pub.ajaxLoadComments(1,10,2);
	autosize(document.querySelectorAll('textarea'));
	if($("#hasAwarded").val()==1){
		$("#pubAwardCount").prev(".material-icons").css({"background":"#2196f3","color":"#ffffff"});
	}
	if($("#isOwner").val() != "true"){
        //添加访问记录
        SmateCommon.addVisitRecord($("#des3OwnerId").val(),$("#des3PubId").val(),1);
    }
	/* document.getElementsByClassName("paper_detial")[0].style.minHeight = window.innerHeight - 46 - 55 - 130 + "px";
	document.getElementById("paperbox").style.height = window.innerHeight - 46 - 95 - 130 + "px"; */
	document.getElementById("paperbox").style.minHeight = window.innerHeight - 46 - 95 - 130 + "px";
});
	
/**
 * 生成赞成果的动态
 */
function produceAwardPubDyn(resType, resId){
	var jsonParam = "{\"operatorType\": \"2\",\"des3PubId\":\"" + $("#des3PubId").val()
	+ "\"}";
	$.ajax({
		url: "/dynweb/dynamic/ajaxrealtime",
		type: "post",
		data:{
			"resType": resType,
			"resId": resId,
			"dynType" : "B2TEMP",
			"paramJson" : jsonParam
		},
		dataType : "json",
		success: function(data){
			
		},
		error: function(e){
			
		}
		
	});
}

function loadMoreComments(){
	var PagNo = $("#PagNo").val();
	mobile.pub.ajaxLoadComments(parseInt(PagNo) + 1,10,1)  ;
	$("#PagNo").val(parseInt(PagNo) + 1);
}
function doPubComment(){
	var replyContent =$.trim($("#pubReplyContent").val()).replace(/\n/g,'<br>');
	if(replyContent.length<=0){
		return;
	}
	mobile.pub.submitMobileComment($("#des3PubId").val(),replyContent);
	$("#pubReplyContent").val("");
	$("#pubReplyContent").css("height","20px");
}

function sharePub(){
	if($("#hasLogin").val() == 0){
	    BaseUtils.buildLoginUrl(window.location.href);
    }else{
      //成果隐私判断全部统一调用smate.common.js-->SmateCommon.checkPubAnyUser入口
      SmateCommon.checkPubAnyUser($("#des3PubId").val(),"snsDetail",'');
      /* $.ajax({
        url : "/pub/details/getpubanyuser",
        type : "post",
        dataType : "json",
        data : {
          "des3PubId" : $("#des3PubId").val()
        },
        success : function(data) {
          if (data.isAnyUser == "7") {
          //成果详情分享也需要弹出层
            $('#dynamicShare').show();
          } else {
            if (data.isSelf == "true") {
              newMobileTip("请先将本成果隐私设置为公开", 2, 2000);
            } else {
              newMobileTip("成果隐私设置为仅作者本人可看", 2, 2000);
            }
          }
        },
        error : function() {
        }
      }); */
	}
}
function isMyMyPub(){
	var callbackData={
		'pubId':$("#pubId").val(),
		'ownerId':$("#ownerId").val()
	};
	mobile.pub.importSNSPub(callbackData);
}
function requestFullText(){
	Pubdetails.requestFullText($("#des3OwnerId").val(),$("#des3PubId").val());
}

function refreshHeadCollectAndDownload(pubId, des3PubId){
    //是否收藏
    $("#fullTextColl").remove();
    if($("#isMyPub").val() != "true" && $("#isCollection").val() == "0"){
		$("#pub_detail_head_li").before("<a id=\"fullTextColl\"  onclick=\"mobile.snspub.collect('"+des3PubId+"',0,this)\" collected=\"0\"  class=\"fr\"><i class=\"material-icons favorite_border\">&#xe87e;</i></a>");
    }
    //是否下载
    $("#fullTextFileId").remove();
    if($("#pubFullTextDownloadUrl").val() != "" && ($("#isMyPub").val() == "true" || $("#file_permission").val() == "0")){
        $("#pub_detail_head_li").before("<a  id=\"fullTextFileId\" onclick=\"Pub.getfulltextUrlDownload(this)\" resId=\""+pubId+"\" des3Id=\""+des3PubId+"\" class=\"fr\"><i class=\"material-icons file_download\">&#xe2c4;</i></a>")
    }
}
function commonResAward(obj){
  mobile.snspub.awardoptnew(obj);
}
function commonShowReplyBox(obj){
  BaseUtils.mobileCheckTimeoutByUrl("/pub/ajaxtimeout", function(){
    var text = $("#pubReplyContent").val().trim();
    if(text.length>0){
        $("#scm_send_comment_btn").removeClass("not_point");
    }else{
        $("#scm_send_comment_btn").addClass("not_point");
    }
    $("#pubReplyBox").slideToggle(function(){
        if($("#pubReplyBox").css("display")!="none" && $("#hasLogin").val() != 0){
            $("#pubReplyContent").focus();
        }
    });
  });
}
function commonShare(obj){
  sharePub();
}
function commonCollectnew(obj){
  mobile.snspub.collectnew(obj);
}

function callBackAward(obj,awardCount){
  if(awardCount>999){
    awardCount = "1k+";
  }
  $(obj).find(".new-Standard_Function-bar_item").toggleClass("new-Standard_Function-bar_selected");
  var isAward = $(obj).attr("isAward");
  if (isAward == 1) {// 取消赞
    $(obj).attr("isAward", 0);           
    if (awardCount == 0) {
      $(obj).find('span').text("赞");
    } else {
      $(obj).find('span').text("赞" + "(" + awardCount + ")");
    }
  } else {// 赞
    $(obj).attr("isAward", 1);
    $(obj).find('span').text("取消赞" + "(" + awardCount + ")");
  }
}

function callBackCollect(obj){
  $(obj).toggleClass("new-Standard_Function-bar_selected");
  var isCollect = $(obj).attr("collect");
  if(isCollect==0){
    $(obj).find("span").text("取消收藏");
    $(obj).attr("collect",1);
  }else{
    $(obj).find("span").text("收藏");
    $(obj).attr("collect",0);    
  }
}
</script>
<input id="isOwner" name="isOwner" type="hidden" value="${pubDetailVO.isOwner }" />
<input id="des3OwnerId" name="des3OwnerId" type="hidden" value="<iris:des3 code='${pubDetailVO.pubOwnerPsnId}'/>" />
<input id="des3PubId2" name="des3PubId" type="hidden" value="${pubDetailVO.des3PubId}" />
<input id="referrer" name="referrer" type="hidden" value="${pubOperateVO.referrer }" />
<input id="nextPageNo" name="nextPageNo" type="hidden" value="${pubOperateVO.detailPageNo }" />
<input id="allPubSum" name="allPubSum" type="hidden" value="${pubOperateVO.pubSum }" />
<c:choose>
  <c:when test="${pubDetailVO.psnId==0 && pubDetailVO.fromPage!='search'} ">
    <dl class="tc">
      <dd>会话已经超时</dd>
    </dl>
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${pubDetailVO.pubId==0}">
        <dl class="tc">
          <dd>已浏览完毕</dd>
        </dl>
      </c:when>
      <c:when test="${empty pubDetailVO }">
        <div class="response_no-result">未找到对应成果详情</div>
      </c:when>
      <c:otherwise>
        <div class="paper_detial pub_detail_info" style="padding: 0px 6px;">
          <input id="pubId" name="pubId" type="hidden" value="${pubDetailVO.pubId}" />
          <input id="articleType" name="articleType" type="hidden" value=""> 
          <input id="ownerId" name="ownerId" type="hidden" value="${pubDetailVO.pubOwnerPsnId}" /> 
          <input id="awardCount_${pubId}" name="pubAwardCount" type="hidden" value="${pubDetailVO.awardCount}" /> 
          <input id="hasAwarded" name="hasAwarded" type="hidden" value="${isAward}" /> 
          <input id="des3FullTextId" name="des3FullTextId" type="hidden" value="${pubDetailVO.fullText.fileid}" /> 
          <input id="file_permission" type="hidden" value="${pubDetailVO.fullText.permission}" /> 
          <input id="fullTextDownloadUrl" type="hidden" value="${pubDetailVO.srcFulltextUrl}" /> 
          <input id="pubFullTextDownloadUrl" type="hidden" value="${pubDetailVO.fullTextDownloadUrl }" /> 
          <input type="hidden" id="isCollection" value="${pubDetailVO.isCollection }" />
          <div id="paperbox" style="display: flex; flex-direction: column;justify-content: space-between; padding: 0px 8px;"> 
           <div>
           <dl>
            <h2>
              <c:if test="${!empty pubDetailVO.title}">
                <span id="pubTitleShare">${pubDetailVO.title}</span>
              </c:if>
            </h2>
            <dt>
              <c:if test="${!empty pubDetailVO.authorNames}">
					${pubDetailVO.authorNames}
				</c:if>
            </dt>
            <dd>
              <c:if test="${!empty pubDetailVO.briefDesc}">
							${pubDetailVO.briefDesc }
						</c:if>
            </dd>
          </dl>
          <c:if test="${not empty pubDetailVO.summary}">
            <h3>摘要:</h3>
            <p>
              <c:if test="${not empty pubDetailVO.summary}">
                <span id="pubAbs">${pubDetailVO.summary }</span>
              </c:if>
            </p>
          </c:if>
          <c:if test="${not empty pubDetailVO.keywords}">
            <h3>关键词:</h3>
            <p style="word-wrap: break-word;">
              <c:if test="${not empty pubDetailVO.keywords}">${pubDetailVO.keywords}&nbsp;</c:if>
            </p>
          </c:if>
          <!-- DOI的显示要放到关键词的下面 -->
          <c:if test="${!empty pubDetailVO.doi}">
            <h3>
              DOI：
              <c:if test="${locale eq 'en_US' }">&nbsp;</c:if>
              <a href="${pubDetailVO.doiUrl }">${pubDetailVO.doi}</a>
            </h3>
          </c:if>
          </div>
          <div class="project_infor-footer">
            <div id="animatePubId"></div>
            <c:if test="${not empty pubDetailVO.fullText.fileId || not empty pubDetailVO.fullText.des3fileId}">
              <c:if test="${pubDetailVO.isOwner == true}">
                <!-- 全文下载 -->
                <a onclick="Pub.getfulltextUrlDownload(this)" resId="${pubDetailVO.pubId}"
                  des3Id="<iris:des3 code='${pubDetailVO.pubId}'/>" class="project_infor-footer__request" style="width:100%">下载全文</a>
              </c:if>
              <c:if test="${pubDetailVO.isOwner == false}">
                <c:if test="${pubDetailVO.fullText.permission == 0}">
                  <!-- 全文下载 -->
                  <a onclick="Pub.getfulltextUrlDownload(this)" resId="${pubDetailVO.pubId}"
                    des3Id="<iris:des3 code='${pubDetailVO.pubId}'/>" class="project_infor-footer__request">下载全文</a>
                </c:if>
                <c:if test="${pubDetailVO.fullText.permission != 0}">
                  <a id="requestPubFullTextTab" 
                    onclick="mobile.pub.requestPubFullText('${pubDetailVO.des3PubId}', 'sns', '<iris:des3 code="${pubDetailVO.pubOwnerPsnId}"/>');"
                    class="project_infor-footer__request">请求全文</a>
                </c:if>
              </c:if>
            </c:if>
            <c:if test="${pubDetailVO.isOwner == false }">
              <c:if test="${empty pubDetailVO.fullText.fileId && empty pubDetailVO.fullText.des3fileId}">
                <a id="requestPubFullTextTab" 
                   onclick="mobile.pub.requestPubFullText('${pubDetailVO.des3PubId}', 'sns', '<iris:des3 code="${pubDetailVO.pubOwnerPsnId}"/>');"
                  class="project_infor-footer__request">请求全文</a>
              </c:if>
              <a onclick="isMyMyPub()" class="project_infor-footer__mine">这是我的成果</a>
            </c:if>
          </div>
        
          </div>
 <%--社交操作start --%>
        <c:set var="isAward" value="${pubDetailVO.isAward}"></c:set>
        <c:set var="resDes3Id" value="${pubDetailVO.des3PubId}"></c:set>
        <c:set var="awardCount" value="${pubDetailVO.awardCount}"></c:set>
        <c:set var="commentCount" value="${pubDetailVO.commentCount}"></c:set>
        <c:set var="shareCount" value="${pubDetailVO.shareCount}"></c:set>
        <c:set var="isCollection" value="${pubDetailVO.isCollection}"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>
      
        </div>
        <div class="background__line" style="width: 100%; height: 12px; background: #eee;"></div>
        <h2 class="wdful_comments wdful__comments-title">
                             精彩评论
        </h2>
        <div id="replyDiv" style="background: #fff; padding: 1px 8px;">
          <jsp:include page="/WEB-INF/jsp/pub/pubdetails/mobile_pub_reply.jsp"></jsp:include>
        </div>
        <div id="showMore"   style="width: 100%; height: 30px;padding:10px; background: #eee;display: none;text-align: center">
          <a href="javascript:;"  onclick="loadMoreComments()" style="text-align: center">查看更多评论</a>
    </div>
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>
