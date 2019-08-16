<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>科研之友</title>
<meta name="description" content="${pubSEO.description}" charset="utf-8">
<c:if test="${not empty pubSEO.keywords}">
<meta name="keywords" content="${pubSEO.keywords}" charset="utf-8">
</c:if>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/new-confirmbox/confirm.css">
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/request-paper/addpaper.css">
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type="text/javascript" src="${resmod}/js/dialog.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/request-paper/addpaper.js"></script>
<style>
    .commnt_a{
      color: #288aed;
    }

  </style>
<script type="text/javascript">
var shareI18 = '<spring:message code="pub.details.opt.share"/>';
$(document).ready(function(){
	addFormElementsEvents();
	Pubdetails.getPdwhCommentList('${pubDetailVO.des3PubId}');
    Pubdetails.getPdwhCommentNumber('${pubDetailVO.des3PubId}');
    Pubdetails.findPdwhPubStatistics('${pubDetailVO.des3PubId}');
	//$("a.thickbox").thickbox();
	Pubdetails.initBuidName();
    if(document.getElementsByClassName("header__nav")){
        document.getElementById("num1").style.display="flex";
        document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
    }
    if(document.getElementById("search_some_one")){
        document.getElementById("search_some_one").onfocus = function(){
            this.closest(".searchbox__main").style.borderColor = "#2882d8";
        }
        document.getElementById("search_some_one").onblur = function(){
            this.closest(".searchbox__main").style.borderColor = "#ccc";
        }
    }
    $("#pubTotalComent").click();
    // SCM-23380 全站检索：站外点评论实现建议调整
    var type = "${pubDetailVO.type}";
    if(type == "comment"){
      $("#pubTotalComentOp").click();
    }
});
//监控textarea内容
function keypress(){
	var replyContent=$.trim($("#pubComment").find("textarea[name$='comments']").val());
	if(replyContent!=''){
		$("#pubCommnetBtn").removeAttr("disabled");
	}else{
		$("#pubCommnetBtn").attr("disabled", "disabled");
	}
}
//监控是否聚焦
function onfocusShow(){
  Pub.pdwhIsExist($("#des3PubId").val(),function(){
    $("#pubCommnetBtn").show();
    $("#pubCommnetCancle").show();
    var replyContent=$.trim($("#pubComment").find("textarea[name$='comments']").val());
    if(replyContent ==''){
      $("#pubCommnetBtn").attr("disabled", "disabled");
      }
    document.getElementById("input").focus();
  });
}
//离开聚焦
function onblurHidden(){
	var replyContent=$.trim($("#pubComment").find("textarea[name$='comments']").val());
	if(replyContent ==''){
		$("#pubComment").find("textarea[name$='comments']").val("");
	}
}

//初始化 分享 插件
function initSharePlugin(obj){
  Pub.pdwhIsExist($("#des3PubId").val(),function(){
    if (locale == "en_US") {
      $(obj).dynSharePullMode({
        'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
        'language' : 'en_US'
      });
    } else {
      $(obj).dynSharePullMode({
        'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
      });
    }
  });
};
//==============================
//分享到好友回调
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId){
	$.ajax({
        url : '/pub/opt/ajaxpdwhshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PdwhPubId':resId,
                  'comment':shareContent,
                  'sharePsnGroupIds':receiverGrpId,
                  'platform':"2"
               },
        success : function(data) {
            var shareSpan = $(".dev_pub_share_"+pubId);
            shareSpan.text(shareI18+"("+data.shareTimes+")");
        }
    }); 
}
//分享到群组回调
function shareGrpCallback (des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, dyntype, resType){
	var shareSpan = $(".dev_pub_share_"+pubId);
    var count = Number(shareSpan.text().replace(/[\D]/ig,""))+1;
    shareSpan.text(shareI18+"("+count+")");
}
//分享到动态回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId,resType,dbId){
    var shareSpan = $(".dev_pub_share_"+pubId);
    var count = Number(shareSpan.text().replace(/[\D]/ig,""))+1;
    shareSpan.text(shareI18+"("+count+")");
}
//收藏和取消收藏成果回调
function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
        $(obj).find(".new-Standard_Function-bar_item").addClass("new-Standard_Function-bar_selected");
        $(obj).find("span").text(pubdetails.unsave);
    }else{
        $(obj).find(".new-Standard_Function-bar_item").removeClass("new-Standard_Function-bar_selected");
        $(obj).find("span").text(pubdetails.save);
    }
}
</script>
</head>
<body style="overflow-x: hidden;">
  <input type="hidden" class="dev_isAward" value="${pubDetailVO.isAward}" />
  <input type="hidden" name="des3PubId" value="${pubDetailVO.des3PubId}" id="des3PubId" />
  <input type="hidden" class="dev_imp_pdwh" value="" />
  <%@ include file="pubDetailViewCitation.jsp"%>
  <div class="detail-header__box" style="top: 48px;">
    <div class="detail-header__main">
      <div class="detail-header__title">
        <div class="detail-header__pub-stats">
          <span class="detail-header__pub-stats_number" id="cited_count_span">0</span>
          <spring:message code="pub.details.info.citations" />
        </div>
        <div class="detail-header__pub-stats">
          <span class="detail-header__pub-stats_number" id="read_count_span">0</span>
          <spring:message code="pub.details.info.read" />
        </div>
      </div>
      <div class="detail-header__actions" style="display: flex; align-items: center;">
        <div class="multiple-button-container">
          <!-- 显示这是我的成果按钮 -->
          <c:if test="${!pubDetailVO.isShowButton}">
            <div class="multiple-button-container_func">
              <i class="material-icons"  style="width: 24px; height: 24px; position: relative;">more_horiz
              <div class="button__box button__model_rect multiple-button-container_funcbox"
                onclick="Pubdetails.importMyPubPdwhConfirm('${pubDetailVO.des3PubId}')">
                <a>
                  <div class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue">
                    <span><spring:message code="pub.details.confirm.pub" /></span>
                  </div>
                </a>
              </div>
              </i>
          </div>
          </c:if>
           <c:if test="${pubDetailVO.isShowButton}">
          <div class="button__box button__model_rect"
            onclick="Pubdetails.importMyPubPdwhConfirm('${pubDetailVO.des3PubId}')">
            <a>
              <div class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue">
                <span><spring:message code="pub.details.confirm.pub" /></span>
              </div>
            </a>
          </div>
          </c:if>
        </div>
      </div>
    </div>
  </div>
  <div class="detail-main__box" style="display: flex; justify-content: center; margin: auto 0;">
    <div class="detail-main__main" style="flex-direction: row; position: relative; margin: 0px;">
      <div class="detail-main__left-box" style="max-width: 1200px;">
        <div class="detail-pub__box" style="width: 1200px;">
          <div class="detail-pub__main" id="detail_pub_main_zh" style="display: block; width: 90%;">
            <div class="detail-pub__title dev_pubdetails_title" style="width: 90%; margin-bottom: 19px;">${pubDetailVO.title }</div>
            <%@ include file="/WEB-INF/jsp/pub/pubdetails/pdwh_author_name.jsp"%>
            <div class="detail-pub__source dev_pubdetails_source" style="width: 90%;">${!empty pubDetailVO.briefDesc ? pubDetailVO.briefDesc : "--"}</div>
            <c:if test="${!empty pubDetailVO.doi}">
              <div class="detail-pub__DOI" style="width: 90%;">
                <spring:message code="pub.details.info.doi" />
                <spring:message code="pub.details.info.colon" />
                <a href="${pubDetailVO.doiUrl }" target="_blank"> ${pubDetailVO.doi}</a>
              </div>
            </c:if>
            <div class="detail-pub__abstract" style="margin-top: 19px;">
              <div class="detail-pub__abstract_title">
                <c:choose>
                  <c:when test="${pubDetailVO.pubType=='1'}">
                    <spring:message code="pub.details.info.cabstract_1" />
                  </c:when>
                  <c:when test="${pubDetailVO.pubType=='5'}">
                    <spring:message code="pub.details.info.cabstract_5" />
                  </c:when>
                  <c:when test="${pubDetailVO.pubType=='8'}">
                    <spring:message code="pub.details.info.cabstract_8" />
                  </c:when>
                  <c:when test="${pubDetailVO.pubType=='7'}">
                    <spring:message code="pub.details.info.cabstract_7" />
                  </c:when>
                  <c:otherwise>
                    <spring:message code="pub.details.info.abstract" />
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="detail-pub__abstract_content">${!empty pubDetailVO.summary ? pubDetailVO.summary : "--"}</div>
            </div>
            <div class="detail-pub__keyword" style="margin-top: 19px;">
              <div class="detail-pub__keyword_title">
                <spring:message code="pub.details.info.keywords" />
              </div>
              <div class="detail-pub__keyword_content">${!empty pubDetailVO.keywords ? pubDetailVO.keywords : "--"}</div>
            </div>
            <div class="detail-pub__keyword" style="margin-top: 19px;">
              <div class="detail-pub__keyword_title">
                <spring:message code="pub.details.info.institution" />
              </div>
              <div class="detail-pub__keyword_content">${!empty pubDetailVO.insNames ? pubDetailVO.insNames : "--"}</div>
            </div>
          </div>
          <div class="new-Standard_Function-bar">
            <c:if test="${pubDetailVO.isAward == 1}">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;"
                isAward="1" onclick="Pub.pdwhAward('${pubDetailVO.des3PubId}',this)">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title">
                  <spring:message code="pub.details.opt.unaward" /><iris:showCount count="${pubDetailVO.awardCount}" preFix="(" sufFix=")"/></span>
              </div>
            </c:if>
            <c:if test="${pubDetailVO.isAward == 0}">
              <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0"
                onclick="Pub.pdwhAward('${pubDetailVO.des3PubId}',this)">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title">
                  <spring:message code="pub.details.opt.award" /><iris:showCount count="${pubDetailVO.awardCount}" preFix="(" sufFix=")"/></span>
              </div>
            </c:if>
            <div class="new-Standard_Function-bar_item" onclick="onfocusShow()">
              <i class="new-Standard_function-icon new-Standard_comment-icon"></i> <span
                class="new-Standard_Function-bar_item-title" id="pubTotalComentOp">
                <spring:message code="pub.details.opt.coment" /><iris:showCount count="${pubDetailVO.commentCount}" preFix="(" sufFix=")"/></span>
            </div>
            <div class="new-Standard_Function-bar_item"
              onclick="SmateShare.getPubDetailsSareParam(this); initSharePlugin(this);"
              resId="<iris:des3 code='${pubDetailVO.pubId}'/>" articleType="${articleType}" resType="22"
              pdwhpubShare="true" pubId="${pubDetailVO.pubId}" databaseType="${databaseType}" dbId>
              <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                class="new-Standard_Function-bar_item-title dev_pub_share_${pubDetailVO.pubId}"
                resId="<iris:des3 code='${pubDetailVO.pubId}'/>">
                <spring:message code="pub.details.opt.share" /><iris:showCount count="${pubDetailVO.shareCount }" preFix="(" sufFix=")"/></span>
            </div>
            <div class="new-Standard_Function-bar_item"
              onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${pubDetailVO.des3PubId}',this)">
              <i class="new-Standard_function-icon new-Standard_Quote-icon"></i> <span
                class="new-Standard_Function-bar_item-title"><spring:message code="common.cite" /> </span>
            </div>
            <div collected="${pubDetailVO.isCollection}"
              onclick="Pub.dealCollectedPub('<iris:des3 code='${pubDetailVO.pubId}'/>','PDWH',this)">
              <c:if test="${pubDetailVO.isCollection=='0'}">
                <div class="new-Standard_Function-bar_item">
                  <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                    class="new-Standard_Function-bar_item-title"><spring:message code="pub.details.opt.collect" /></span>
                </div>
              </c:if>
              <c:if test="${pubDetailVO.isCollection=='1'}">
                <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
                  <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                    class="new-Standard_Function-bar_item-title"><spring:message code="pub.details.opt.unSave" /></span>
                </div>
              </c:if>
            </div>
          </div>
          <div class="detail-pub__tabs" style="margin-top: 12px;">
            <div class="detail-pub__tabs_header">
              <nav class="nav_horiz">
                <ul class="nav__list">
                  <li class="nav__item item_selected" id="pubTotalComent"
                    onclick="Pubdetails.relatedPdwhComment('${pubDetailVO.des3PubId}')"><spring:message
                      code="pub.details.comment.related" /></li>
                </ul>
                <div class="nav__underline"></div>
              </nav>
            </div>
            <div class="detail-comment__box">
              <div class="detail-post__box">
                <div class="detail-post__avatar">
                  <a href="/psnweb/homepage/show?des3PsnId='<iris:des3 code='${pubDetailVO.psnId}'/>'" target="_blank"><img
                    src="${pubDetailVO.psnAvatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
                </div>
                <div class="detail-post__main">
                  <div class="detail-post__input">
                    <div class="form__sxn_row">
                      <div class="input__box">
                        <div class="input__area" id="pubComment">
                          <textarea maxlength="250" name="comments" id="input"
                            placeholder="<spring:message code='pub.details.comment.marked'/>" onKeyUp="keypress()"
                            onblur="onblurHidden()" onfocus="onfocusShow()"></textarea>
                          <div class="textarea-autoresize-div"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="detail-post__actions">
                    <div class="multiple-button-container">
                      <div class="button__box button__model_rect" id="pubCommnetCancle" style="display: none;"
                        onclick="Pubdetails.cancelComment('${pubDetailVO.des3PubId}')">
                        <a>
                          <div
                            class="button__main button__style_flat button__size_dense button__color-plain color-display_grey">
                            <span><spring:message code="pub.details.comment.cancel" /> </span>
                          </div>
                        </a>
                      </div>
                      <div class="button__box button__model_rect" id="pubCommnetBtn" disabled style="display: none;"
                        onclick="Pubdetails.pdwhComment('${pubDetailVO.des3PubId}')">
                        <a>
                          <div
                            class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue">
                            <span><spring:message code="pub.details.comment.publish" /> </span>
                          </div>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="detail-comment__list">
                <div class="main-list__list item_no-border item_no-padding" list-main="pubPdwhCommentList"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="request-add__paper request-add__paper-tip_newposition">
        <!-- 如果基准库有全文附件，下载全文 -->
        <c:if test="${not empty pubDetailVO.pdwhFullTextDownloadUrl}">
          <div class="request-add__paper-avator" onclick="location.href='${pubDetailVO.pdwhFullTextDownloadUrl}';">
            <img class="request-add__paper-avator__img" src="${pubDetailVO.fulltextImageUrl}"
              onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
            <div class="request-add__tip request-add__tip1"
              title="<spring:message code='pub.details.fulltext.download_fulltext'/>">
              <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
            </div>
          </div>
        </c:if>
        <!-- 如果基准库没有全文附件，请求全文 -->
        <c:if test="${empty pubDetailVO.pdwhFullTextDownloadUrl}">
          <div class="request-add__paper-avator" onclick="Pubdetails.requestPdwhFullText('${pubDetailVO.des3PubId}');">
            <img class="request-add__paper-avator__img" src="${resmod}/images_v5/images2016/file_img.jpg"
              onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
            <div class="request-add__tip request-add__tip1"
              title="<spring:message code='pub.details.fulltext.req_fulltext'/>">
              <img src="${resmod}/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                src="${resmod}/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
            </div>
          </div>
        </c:if>
        <c:if test="${not empty pubDetailVO.pdwhSimilarCount && pubDetailVO.pdwhSimilarCount != 0}">
          <div class="requset-add__paper-func" onclick="Pubdetails.pdwhFulltextList('${pubDetailVO.des3PubId}');">
            <spring:message code="pub.details.fulltext.other" />
            <span>${pubDetailVO.pdwhSimilarCount}</span>
            <spring:message code="pub.details.fulltext.similar" />
          </div>
        </c:if>
      </div>
    </div>
  </div>
  <jsp:include page="/common/smate.share.mvc.jsp" />
  <!-- 分享操作 -->
</body>
</html>
