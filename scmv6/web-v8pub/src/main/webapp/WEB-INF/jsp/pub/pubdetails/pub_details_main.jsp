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
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/css2016/scmjscollection.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/css/scmpcframe.css">
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/request-paper/addpaper.css">
  <link href="${resmod}/css_v5/plugin/jquery.atwho.css" rel="stylesheet">
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/request-paper/addpaper.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
  <script src="${resmod}/js_v5/plugin/jquery.caret.js"></script>
  <script src="${resmod}/js_v5/plugin/jquery.atwho.js"></script>
  <style>
    .commnt_a{
      color: #288aed;
    }

  </style>
<script type="text/javascript">
var shareI18 = '<spring:message code="pub.details.opt.share"/>';
$(document).ready(function(){
	var des3PubId = $("#des3PubId").val();
	addFormElementsEvents();
	Pubdetails.getCommentList(des3PubId);
	Pubdetails.getCommentNumber(des3PubId);
    Pubdetails.commentInit();
	document.getElementsByClassName("nav__item-selected—pub")[0].closest(".nav_horiz").querySelector(".nav__underline").style.width = document.getElementsByClassName("nav__item-selected—pub")[0].offsetWidth + "px";
	//$("a.thickbox").thickbox();
	/* 大小自适应 */
     if((document.getElementsByClassName("header__nav").length>0)&&(document.getElementsByClassName("header__nav")[0].innerHTML!="")){
        if((document.getElementById("num2"))&&(document.getElementById("num1"))){
            document.getElementById("num2").style.display="flex";
            document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num1"));
        }
    }else{
            document.getElementById("num1").style.display="flex";
            document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
    }
	if($("#isOwner").val() == "false"){
		//添加阅读记录
		Pubdetails.addReadRecord();
		//添加访问记录
		SmateCommon.addVisitRecord($("#currentPsnId").val(),$("#des3PubId").val(),1);
		//Pubdetails.updatePsnEffect();
	}
	//有全文认领推荐出来
	Pubdetails.rcmFulltext();
    if(document.getElementsByClassName("header__nav")){
        if((document.getElementById("num1"))&&(document.getElementById("num2"))){
            document.getElementById("num1").style.display="flex";
            document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
        }
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


});
//初始化 分享 插件
function initSharePlugin(obj){
	if(SmateShare.timeOut && SmateShare.timeOut == true)
		return;
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
};
//==============================
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
	$.ajax({
        url : '/pub/opt/ajaxshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PubId':resId,
             'comment':shareContent,
             'sharePsnGroupIds':receiverGrpId,
             'platform':"2"
               },
        success : function(data) {
            if (data.result == "success") {
              	if(data.shareTimes>=1000){
              	  $('.dev_pub_share_'+pubId).text(shareI18+"(1K+)");
              	}else{
              	  $('.dev_pub_share_'+pubId).text(shareI18+"("+data.shareTimes+")");
              	}
            }
        }
    });
}
function shareGrpCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
	var shareSpan = $(".dev_pub_share_"+pubId);
    var count = Number(shareSpan.text().replace(/[\D]/ig,""))+1;
    if(count>=1000){
    	shareSpan.text(shareI18+"(1K+)");
  }else{
  	shareSpan.text(shareI18+"("+count+")");
  }
}
//==============================
//分享回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
	var shareSpan = $(".dev_pub_share_"+pubId);
    var count = Number(shareSpan.text().replace(/[\D]/ig,""))+1;
    if(count>=1000){
      	shareSpan.text(shareI18+"(1K+)");
    }else{
    	shareSpan.text(shareI18+"("+count+")");
    }
};
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
	$("#pubCommnetBtn").show();
	$("#pubCommnetCancle").show();
	var replyContent=$.trim($("#pubComment").find("textarea[name$='comments']").val());
	if(replyContent ==''){
		$("#pubCommnetBtn").attr("disabled", "disabled");
	}else{
	  $("#pubCommnetBtn").removeAttr("disabled");
	}
	document.getElementById("input").focus();
}
//离开聚焦
function onblurHidden(){
	var replyContent=$.trim($("#pubComment").find("textarea[name$='comments']").val());
	if(replyContent ==''){
		$("#pubComment").find("textarea[name$='comments']").val("");
	}
}
function closeRcmdFulltext(){
	$("#rcmdFulltext").remove();
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
function enterEdit(des3PubId){
    var forwardUrl = "/pub/enter?des3PubId="+encodeURIComponent(des3PubId);
    BaseUtils.forwardUrlRefer(true,forwardUrl);
}
</script>
</head>
<body style="overflow-x: hidden;">
  <input type="hidden" name="isLogin" value="${pubDetailVO.isLogin}" id="isLogin" />
  <input type="hidden" name="ownerPsnId" value="${pubDetailVO.des3PsnId}" id="ownerPsnId" />
  <input type="hidden" name="psnId" value="<iris:des3 code='${pubDetailVO.psnId}'/>" id="psnId" />
  <input type="hidden" name="pub_username" value="${username}" id="pub_username" />
  <input type="hidden" name="des3PubId" value="${pubDetailVO.des3PubId}" id="des3PubId" />
  <input type="hidden" name="currentPsnId" value="<iris:des3 code='${pubDetailVO.pubOwnerPsnId}'/>" id="currentPsnId" />
  <input type="hidden" name="isOwner" value="${pubDetailVO.isOwner }" id="isOwner" />
  <input type="hidden" name="psnAvatars" value="${pubDetailVO.psnAvatars}" id='psnAvatars'>
 <%@ include file="pubDetailViewCitation.jsp"%>
  <div style="display: none;" id='title'>${pubDetailVO.title}</div>
  <!-- ===================================================== -->
  <div class="neck__container-item">
    <div class="neck__container-content">
      <div class="neck__item-left">
        <a href="/psnweb/homepage/show?des3PsnId='${pubDetailVO.des3PsnId}'" target="_blank"> <img
          src="${pubDetailVO.pubOwnerAvatars}" onerror="this.src='${resmod}/smate-pc/img/logo_psndefault.png'"
          class="neck__item-left__avator">
        </a>
        <div class="neck__item-left__infor">
          <div class="neck__item-left__infor-box">
            <div class="neck__item-left__infor-name">
              <a href="/psnweb/homepage/show?des3PsnId='${pubDetailVO.des3PsnId}'" title="${pubDetailVO.pubOwnerName}"
                target="_blank">${pubDetailVO.pubOwnerName}</a>
            </div>
          </div>
          <div class="neck__item-left__infor-work" title="${pubDetailVO.pubOwnerTitle}">${pubDetailVO.pubOwnerTitle}</div>
          <c:if test="${pubDetailVO.pubOwnerhIndex > 0}">
            <div class="neck__item-left__infor-prj">
              <div>
                <spring:message code="pub.details.statistics.hindex" />
              </div>
              <div class="neck__infor-prj_num">${pubDetailVO.pubOwnerhIndex}</div>
            </div>
          </c:if>
        </div>
      </div>
      <c:if test="${!pubDetailVO.isOwner && !pubDetailVO.isFriend}">
        <div class="add__friend-container" onclick="Pub.addFriendReq('${pubDetailVO.des3PsnId}',this,false);">
          <i class="material-icons add__friend-tip" style="margin: 0px;">add</i> <span class="add__friend-content"><spring:message
              code="pub.details.add.friend" /></span>
        </div>
      </c:if>
    </div>
  </div>
  <!-- ===================================================== -->
  <div class="detail-header__box detail-header__box-top">
    <div class="detail-header__main">
      <div class="detail-header__title">
        <div class="detail-header__pub-stats">
          <span class="detail-header__pub-stats_number">${pubDetailVO.refCount }</span>
          <spring:message code="pub.details.info.citations" />
        </div>
        <div class="detail-header__pub-stats">
          <span class="detail-header__pub-stats_number">${pubDetailVO.readCount }</span>
          <spring:message code="pub.details.info.read" />
        </div>
      </div>
      <div class="detail-header__actions" style="display: flex; align-items: center;">
        <div class="multiple-button-container">
          <c:if test="${!pubDetailVO.isOwner}">
            <!-- 显示这是我的成果按钮 -->
            <c:if test="${!pubDetailVO.isShowButton}">
            <div class="multiple-button-container_func">
              <i class="material-icons">more_horiz</i>
              <div class="button__box button__model_rect  multiple-button-container_funcbox"
              onclick="Pubdetails.impMyPubConfirm(1,'${pubDetailVO.pubId}','${pubDetailVO.pubOwnerPsnId}');">
                <a>
                  <div class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue">
                    <span><spring:message code="pub.details.confirm.pub" /></span>
                  </div>
                </a>
              </div>
          </div> 
          </c:if>
          <c:if test="${pubDetailVO.isShowButton}">
            <div class="button__box button__model_rect"
              onclick="Pubdetails.impMyPubConfirm(1,'${pubDetailVO.pubId}','${pubDetailVO.pubOwnerPsnId}');">
              <a>
                <div
                  class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
                  <span><spring:message code="pub.details.confirm.pub" /></span>
                </div>
              </a>
            </div>
          </c:if>
          </c:if>
          <c:if test="${pubDetailVO.isOwner}">
            <!-- 如果是我的论文，则隐藏编辑按钮 -->
            <!-- 显示这是我的编辑按钮 -->
            <div class="button__box button__model_rect">
              <a href="javascript:enterEdit('${pubDetailVO.des3PubId}');">
                <div
                  class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
                  <span><spring:message code="pub.details.edit" /></span>
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
      <div class="detail-main__left-box detail-main__left-box__top" style="max-width: 1200px;">
        <div class="detail-pub__box" style="width: 1200px;">
          <div class="detail-pub__main" id="detail_pub_main_zh" style="width: 1200px;">
            <div class="detail-pub__title dev_pubdetails_title" style="width: 90%;">${pubDetailVO.title}</div>
            <div class="detail-pub__author dev_pubdetails_author" style="width: 90%;">${pubDetailVO.authorNames }</div>
            <div class="detail-pub__source dev_pubdetails_source" style="width: 90%;">${ !empty pubDetailVO.briefDesc ? pubDetailVO.briefDesc : "--"}</div>
            <c:if test="${!empty pubDetailVO.doi}">
              <div class="detail-pub__DOI" style="width: 90%;">
                <spring:message code="pub.details.info.doi" />
                <spring:message code="pub.details.info.colon" />
                <a href="${pubDetailVO.doiUrl }" target="_blank">${pubDetailVO.doi }</a>
              </div>
            </c:if>
            <div class="detail-pub__abstract">
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
            <div class="detail-pub__keyword">
              <div class="detail-pub__keyword_title">
                <spring:message code="pub.details.info.keywords" />
              </div>
              <div class="detail-pub__keyword_content">${!empty pubDetailVO.keywords ? pubDetailVO.keywords : "--"}</div>
              <div class="detail-pub__keyword_title" style="margin-top: 32px;">
                <spring:message code="pub.details.info.institution" />
              </div>
              <div class="detail-pub__keyword_content">${!empty pubDetailVO.insNames ? pubDetailVO.insNames : "--"}</div>
              <c:if test="${!empty pubDetailVO.accessorys}">
                <div class="detail-pub__keyword_title" style="margin-top: 32px;">
                  <spring:message code="pub.details.attachment" />
                </div>
                <div class="detail-pub__keyword_Enclosure">
                  <c:forEach items="${pubDetailVO.accessorys}" var="result" varStatus="stat">
                    <input type="hidden" name="fileType" value="${result.fileType}">
                    <c:choose>
                      <c:when test="${result.fileType =='.txt'}">
                        <div class="detail-pub__keyword_pic" title="${result.fileName}">
                          <a href="${result.fileUrl}"> <img src="${resmod}/smate-pc/img/fileicon_txt.png"
                            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                          </a>
                        </div>
                      </c:when>
                      <c:when test="${result.fileType =='.pdf'}">
                        <div class="detail-pub__keyword_pic" title="${result.fileName}">
                          <a href="${result.fileUrl}"> <img src="${resmod}/smate-pc/img/fileicon_pdf.png"
                            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                          </a>
                        </div>
                      </c:when>
                      <c:when test="${result.fileType =='.doc'||result.fileType=='.docx'}">
                        <div class="detail-pub__keyword_pic" title="${result.fileName}">
                          <a href="${result.fileUrl}"> <img src="${resmod}/smate-pc/img/fileicon_doc.png"
                            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                          </a>
                        </div>
                      </c:when>
                      <c:when test="${result.fileType=='.rar'||result.fileType=='.zip'}">
                        <div class="detail-pub__keyword_pic" title="${result.fileName}">
                          <a href="${result.fileUrl}"> <img src="${resmod}/smate-pc/img/fileicon_zip.png"
                            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                          </a>
                        </div>
                      </c:when>
                      <c:when test="${result.fileType=='.ppt'|| result.fileType =='.pptx'}">
                        <div class="detail-pub__keyword_pic" title="${result.fileName}">
                          <a href="${result.fileUrl}"> <img src="${resmod}/smate-pc/img/fileicon_ppt.png"
                            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                          </a>
                        </div>
                      </c:when>
                      <c:when test="${result.fileType=='.xls'||result.fileType=='.xlsx'}">
                        <div class="detail-pub__keyword_pic" title="${result.fileName}">
                          <a href="${result.fileUrl}"> <img src="${resmod}/smate-pc/img/fileicon_xls.png"
                            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                          </a>
                        </div>
                      </c:when>
                      <c:otherwise>
                        <div class="detail-pub__keyword_pic" title="${result.fileName}">
                          <a href="${result.fileUrl}"> <img src="${result.fileUrl}"
                            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                          </a>
                        </div>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </div>
              </c:if>
            </div>
          </div>
          <%-- <div class="detail-pub__action detail-pub__action-alignment detail-pub__action-footer__function" style="justify-content: flex-start!important;">
						<c:if test="${pubDetailVO.isAward == 0}">
							<div class="button__box button__model_rect" isAward="${pubDetailVO.isAward}" onclick="Pub.pubAward('${pubDetailVO.des3PubId}',this)"> 
							<a>
  								<div class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
   									<div class="button__inline-icon css-spirit__main">
      									<div class="css-spirit__icon spirit-icon__thumbup-outline dev_pubdetails_award_style"></div>
   									</div>
    								<span class="dev_pub_award css-spirit__icon-title"><spring:message code="pub.details.opt.award"/><c:if test="${pubDetailVO.awardCount > 0}">(${pubDetailVO.awardCount})</c:if></span>
    							</div>
 							</a>
							</div>
						</c:if>
						<c:if test="${pubDetailVO.isAward == 1}">
							<div class="button__box button__model_rect" isAward="${pubDetailVO.isAward}" onclick="Pub.pubAward('${pubDetailVO.des3PubId}',this)">
								<a>
  									<div class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
	   									<div class="button__inline-icon css-spirit__main">
	      									<div class="css-spirit__icon spirit-icon__thumbup dev_pubdetails_award_style"></div>
	   									</div>
	    								<span class="dev_pub_award dev_cancel_award css-spirit__icon-title"><spring:message code="pub.details.opt.unaward"/><c:if test="${pubDetailVO.awardCount > 0}">(${pubDetailVO.awardCount})</c:if></span>
    								</div>
 								</a>
 							</div>
						</c:if>
						<div class="button__box button__model_rect" onclick="onfocusShow()">
							<a>
  								<div class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
	   								<div class="button__inline-icon css-spirit__main">
	      								<div class="css-spirit__icon spirit-icon__comment"></div>
	   								</div>
	    							<span  class="css-spirit__icon-title" id = "pub_details_main_commentCount"><spring:message code="pub.details.opt.coment"/></span>
    							</div>
 							</a> 
 						</div>
						<div class="button__box button__model_rect" onclick="SmateShare.getPubDetailsSareParam(this); initSharePlugin(this);" resId="${pubDetailVO.des3PubId}" resType="1" pubId="${pubDetailVO.pubId}">
							 <a>
  							 	<div class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
	   								<div class="button__inline-icon css-spirit__main">
	      								<div class="css-spirit__icon spirit-icon__share"></div>
	   								</div>
	    							<span class="dev_pub_share_${pubDetailVO.pubId} css-spirit__icon-title"><spring:message code="pub.details.opt.share"/><c:if test="${pubDetailVO.shareCount > 0}">(${pubDetailVO.shareCount})</c:if></span>
    							</div>
 							</a>
 						</div>
				          <div class="button__box button__model_rect" collected="${pubDetailVO.isCollection}"  onclick="Pub.dealCollectedPub('${pubDetailVO.des3PubId}','SNS',this)">
				               <c:if test="${pubDetailVO.isCollection=='0'}">
				                       <div class="button__main button__style_flat button__size_dense button__color-plain color-display_grade">
				                         <div class="button__inline-icon css-spirit__main">
				                            <div class="css-spirit__icon spirit-icon__favorite-outline"></div>
				                         </div>
				                         <span><spring:message code="pub.details.opt.collect"/></span>
				                       </div>
				               </c:if>
				               <c:if test="${pubDetailVO.isCollection=='1'}">
				                       <div class="button__main button__style_flat button__size_dense button__color-plain color-display_grade">
				                         <div class="button__inline-icon css-spirit__main">
				                            <div class="css-spirit__icon icon-collect-cur"></div>
				                         </div>
				                         <span  class="css-spirit__icon-title"><spring:message code="pub.details.opt.unSave"/></span>
				                       </div>
				               </c:if>
				          </div>
						<div class="button__box button__model_rect"  onclick="Pub.showSnsQuote('/pub/ajaxpubquote','${pubDetailVO.des3PubId}',this)">
  								<div class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
	   								<div class="button__inline-icon css-spirit__main">
	      								<div class="css-spirit__icon spirit-icon__cite"></div>
	   								</div>
	    							<span class="css-spirit__icon-title"><spring:message code='common.cite'/></span>
    							</div>
 						</div>
					</div> --%>
          <div class="new-Standard_Function-bar">
            <c:if test="${pubDetailVO.isAward == 1}">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;"
                isAward="1" onclick="Pub.pubAward('${pubDetailVO.des3PubId}',this)">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"><spring:message code="pub.details.opt.unaward" />
                  <c:if test="${pubDetailVO.awardCount > 0&&pubDetailVO.awardCount>=1000}">
                   (1K+)
                  </c:if>
                  <c:if test="${pubDetailVO.awardCount > 0&&pubDetailVO.awardCount<1000}">
                   (${pubDetailVO.awardCount})
                  </c:if>
              </span>
              </div>
            </c:if>
            <c:if test="${pubDetailVO.isAward == 0}">
              <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0"
                onclick="Pub.pubAward('${pubDetailVO.des3PubId}',this)">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"><spring:message code="pub.details.opt.award" /> 
                   <c:if test="${pubDetailVO.awardCount > 0&&pubDetailVO.awardCount>=1000}">
                   (1K+)
                  </c:if>
                  <c:if test="${pubDetailVO.awardCount > 0&&pubDetailVO.awardCount<1000}">
                   (${pubDetailVO.awardCount})
                  </c:if>
                  </span>
              </div>
            </c:if>
            <div class="new-Standard_Function-bar_item" onclick="onfocusShow()">
              <i class="new-Standard_function-icon new-Standard_comment-icon"></i> <span
                class="new-Standard_Function-bar_item-title" id="pubTotalComentOp"><spring:message
                  code="pub.details.opt.coment" /> 
                  <c:if test="${pubDetailVO.commentCount > 0&&pubDetailVO.commentCount<1000}">
                        (${pubDetailVO.commentCount})
                  </c:if> 
                  <c:if test="${pubDetailVO.commentCount > 0&&pubDetailVO.commentCount>=1000}">
                        (1K+)
                  </c:if> 
                </span>
            </div>
            <div class="new-Standard_Function-bar_item" onclick="Pub.pubCite(this);" 
              pubMainListIsAnyUser="${pubDetailVO.permission}" type="detail" owner="${pubDetailVO.isOwner}" 
              resId="<iris:des3 code='${pubDetailVO.pubId}'/>" resType="1" pubId="${pubDetailVO.pubId}">
              <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                class="new-Standard_Function-bar_item-title dev_pub_share_${pubDetailVO.pubId}"
                resId="<iris:des3 code='${pubDetailVO.pubId}'/>"><spring:message code="pub.details.opt.share" />
                <c:if test="${pubDetailVO.shareCount>0&&pubDetailVO.shareCount>=1000 }">(1K+)</c:if>
                <c:if test="${pubDetailVO.shareCount>0&&pubDetailVO.shareCount<1000 }">(${pubDetailVO.shareCount })</c:if>
                 </span>
            </div>
            <div class="new-Standard_Function-bar_item"
              onclick="Pub.showSnsQuote('/pub/ajaxpubquote','${pubDetailVO.des3PubId}',this)">
              <i class="new-Standard_function-icon new-Standard_Quote-icon"></i> <span
                class="new-Standard_Function-bar_item-title"><spring:message code="common.cite" /> </span>
            </div>
            <div collected="${pubDetailVO.isCollection}"
              onclick="Pub.dealSnsCollectedPub('<iris:des3 code='${pubDetailVO.pubId}'/>','SNS',this)">
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
                  <li class="nav__item item_selected nav__item-selected—pub" style="padding: 0 10px;  border-bottom: 2px solid #288aed;" id="pubTotalComent"
                    onclick="Pubdetails.relatedComment('${pubDetailVO.des3PubId}')"><spring:message
                      code="pub.details.comment.related" /></li>
                </ul>
                <div class="nav__underline" style="width: 92px; min-width: 92px; display: none;"></div>
              </nav>
            </div>
            <div class="detail-comment__box">
              <div class="detail-post__box" style=" margin: 28px 0px 14px 0px;">
                <div class="detail-post__avatar">
                  <a href="/psnweb/homepage/show?des3PsnId='<iris:des3 code='${pubDetailVO.psnId}'/>'" target="_blank">
                    <img src="${pubDetailVO.psnAvatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                  </a>
                </div>
                <div class="detail-post__main">
                  <div class="detail-post__input">
                    <div class="form__sxn_row">
                      <div class="input__box">
                        <div class="input__area" id="pubComment">
                          <textarea maxlength="250" name="comments" id="input"  class="js_autocompletebox"
                             request-url="/psnweb/ac/ajaxgetComplete"  autocomplete="off"
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
                            class="button__main button__style_flat button__size_dense button__color-plain color-display_grey ripple-effect">
                            <span><spring:message code='pub.details.comment.cancel' /></span>
                          </div>
                        </a>
                      </div>
                      <div class="button__box button__model_rect" id="pubCommnetBtn" disabled style="display: none;"
                        onclick="Pubdetails.comment('${pubDetailVO.des3PubId}')">
                        <a>
                          <div
                            class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
                            <span><spring:message code='pub.details.comment.publish' /></span>
                          </div>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="detail-comment__list">
                <div class="main-list">
                  <div class="main-list__list item_no-border item_zero-space" list-main="pubCommentList"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="request-add__paper request-add__paper-newtop request-add__paper-tip_newposition">
        <!-- 个人成果没有全文附件 -->
        <c:if test="${empty pubDetailVO.fullTextDownloadUrl}">
          <!-- 不是我的论文 -->
          <!-- 当前用户是成果拥有者，则显示上传全文 -->
          <c:if test="${pubDetailVO.isOwner}">
            <div class="request-add__paper-avator" onclick="javascript:enterEdit('${pubDetailVO.des3PubId}');">
              <img class="request-add__paper-avator__img" src="${resmod}/images_v5/images2016/file_img.jpg"
                onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
              <div class="request-add__tip request-add__tip1"
                title="<spring:message code='pub.details.fulltext.upload_fulltext'/>">
                <img src="${resmod}/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                  src="${resmod}/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
              </div>
            </div>
          </c:if>
          <!-- 当前用户不是是成果拥有者 -->
          <c:if test="${!pubDetailVO.isOwner}">
            <!-- 如果基准库有全文附件，则显示弹窗，提示下载全文还是继续请求 -->
            <c:if test="${not empty pubDetailVO.pdwhFullTextDownloadUrl}">
              <div class="request-add__paper-avator" onclick="showDialog('requestFullTextDiv');">
                <img class="request-add__paper-avator__img" src="${resmod}/images_v5/images2016/file_img.jpg"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                <div class="request-add__tip request-add__tip1"
                  title="<spring:message code='pub.details.fulltext.req_fulltext'/>">
                  <img src="${resmod}/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                    src="${resmod}/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                </div>
              </div>
            </c:if>
            <!-- 如果基准库没有全文附件，则不显示弹窗，直接请求全文 -->
            <c:if test="${empty pubDetailVO.pdwhFullTextDownloadUrl}">
              <div class="request-add__paper-avator"
                onclick="Pubdetails.requestFullText(this,'${pubDetailVO.des3PsnId}','${pubDetailVO.des3PubId}')">
                <img class="request-add__paper-avator__img" src="${pubDetailVO.fulltextImageUrl}"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                <div class="request-add__tip request-add__tip1"
                  title="<spring:message code='pub.details.fulltext.req_fulltext'/>">
                  <img src="${resmod}/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                    src="${resmod}/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                </div>
              </div>
            </c:if>
          </c:if>
        </c:if>
        <!-- 个人成果有全文附件且有权限下载，则显示下载全文 -->
        <c:if test="${not empty pubDetailVO.fullTextDownloadUrl}">
          <div class="request-add__paper-avator" onclick="location.href='${pubDetailVO.fullTextDownloadUrl}';">
            <img class="request-add__paper-avator__img" src="${pubDetailVO.fulltextImageUrl}"
              onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
            <div class="request-add__tip request-add__tip1"
              title="<spring:message code='pub.details.fulltext.download_fulltext'/>">
              <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
            </div>
          </div>
        </c:if>
        <c:if test="${not empty pubDetailVO.snsSimilarCount && pubDetailVO.snsSimilarCount != 0}">
          <div class="requset-add__paper-func" onclick="Pubdetails.fulltextList('${pubDetailVO.des3PubId}');">
            <spring:message code='pub.details.fulltext.other' />
            <span>${pubDetailVO.snsSimilarCount}</span>
            <spring:message code='pub.details.fulltext.similar' />
          </div>
        </c:if>
      </div>
    </div>
  </div>
  <!-- 请求全文提示弹框，下载全文 还是 继续请求 -->
  <div class="dialogs__box" style="width: 344px;" id="requestFullTextDiv" dialog-id="requestFullTextDiv"
    cover-event="hide">
    <div class="dialogs__childbox_adapted">
      <div class="request-fulltext__box">
        <div class="request-alternative__header">
          <spring:message code='pub.details.fulltext.this' />
        </div>
        <div class="request-alternative__img_box">
          <div class="request-alternative__fulltext-img dev_pubdetails_img">
            <img src="/resmod/images_v5/images2016/file_img1.jpg" class="dev_fulltext_download dev_pub_img"
              onerror="this.src='/resmod/images_v5/images2016/file_img1.jpg'">
          </div>
        </div>
        <div class="request-alternative__actions">
          <div class="button__box button__model_rect"
            onclick="Pubdetails.requestFullText(this,'${pubDetailVO.des3PsnId}','${pubDetailVO.des3PubId}');">
            <a>
              <div
                class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
                <span><spring:message code='pub.details.fulltext.req_repeat' /></span>
              </div>
            </a>
          </div>
          <div class="button__box button__model_rect" onclick="location.href='${pdwhFullTextDownloadUrl}';">
            <a>
              <div
                class="button__main button__style_flat button__size_dense button__color-reverse color-display_grade ripple-effect">
                <span><spring:message code='pub.details.fulltext.download_fulltext' /></span>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <jsp:include page="/common/smate.share.mvc.jsp" />
  <!-- 分享操作 -->
  <div id="rcmdPubft"></div>

  <!-- 自动填词使用的 -->
  <div class="ac__box" id="search_suggest_ac__box"
       style="max-height: 388px; display: block; width: 400px; left: 675px; top: 10px; bottom: auto;">
    <div class="preloader_ind-linear" style="width: 100%;"></div>
    <div class="ac__list">
      <!-- 检索提示列表 -->
    </div>
  </div>
</body>
</html>
