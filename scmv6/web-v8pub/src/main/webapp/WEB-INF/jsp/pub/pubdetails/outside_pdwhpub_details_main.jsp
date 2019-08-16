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
<%@ include file="pubDetailViewCitation.jsp"%>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/request-paper/addpaper.css">
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/request-paper/addpaper.js"></script>
<style>
    .commnt_a{
      color: #288aed;
    }

  </style>
<script type="text/javascript">
  $(document).ready(function() {
    addFormElementsEvents();
    Pubdetails.getPdwhCommentList('${pubDetailVO.des3PubId}');
    Pubdetails.getPdwhCommentNumber('${pubDetailVO.des3PubId}');
    Pubdetails.findPdwhPubStatistics('${pubDetailVO.des3PubId}');
    if ($("#isLogin").val() == "false") {
      $("#detail_head").css("top", "48px");
    }
    Pubdetails.initBuidName();
    if (document.getElementsByClassName("header__nav")) {
      document.getElementById("num1").style.display = "flex";
      document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
    }
    if (document.getElementById("search_some_one")) {
      document.getElementById("search_some_one").onfocus = function() {
        this.closest(".searchbox__main").style.borderColor = "#2882d8";
      }
      document.getElementById("search_some_one").onblur = function() {
        this.closest(".searchbox__main").style.borderColor = "#ccc";
      }
    }
    // SCM-23380 全站检索：站外点评论实现建议调整
    var type = "${pubDetailVO.type}";
    if(type == "comment"){
      $("#commentDiv").click();
    }
  });
  //监控textarea内容
  function keypress() {
    var replyContent = $.trim($("#pubComment").find("textarea[name$='comments']").val());
    if (replyContent != '') {
      $("#pubCommnetBtn").removeAttr("disabled");
    } else {
      $("#pubCommnetBtn").attr("disabled", "disabled");
    }
  }
  //监控是否聚焦
  function onfocusShow() {
    Pub.pdwhIsExist($("#des3PubId").val(),function(){
      $("#pubCommnetBtn").show();
      $("#pubCommnetCancle").show();
      var replyContent = $.trim($("#pubComment").find("textarea[name$='comments']").val());
      if (replyContent == '') {
        $("#pubCommnetBtn").attr("disabled", "disabled");
      }
    });
  }
  //离开聚焦
  function onblurHidden() {
    var replyContent = $.trim($("#pubComment").find("textarea[name$='comments']").val());
    if (replyContent == '') {
      $("#pubCommnetBtn").hide();
      $("#pubCommnetCancle").hide();
      $("#pubComment").find("textarea[name$='comments']").val("");
    }
  }
  //初始化 分享 插件
  function initSharePlugin(obj) {
      if (locale == "en_US") {
        $(obj).dynSharePullMode({
          'shareToSmateMethod' : 'SmateShare.showOutsideShareToScmBox(event)',
          'language' : 'en_US'
        });
      } else {
        $(obj).dynSharePullMode({
          'shareToSmateMethod' : 'SmateShare.showOutsideShareToScmBox(event)'
        });
      }
  };
  function initShare(des3PubId,obj){
    Pub.pdwhIsExist2(des3PubId,function(){
      SmateShare.getPubDetailsSareParam(obj); 
      initSharePlugin(obj);
    });
  }
</script>
</head>
<body style="overflow-x: hidden;">
  <input type="hidden" name="isLogin" value="${pubDetailVO.isLogin}" id="isLogin" />
  <input type="hidden" value="${pubDetailVO.pubType }" />
  <input type="hidden" name="des3PubId" value="${pubDetailVO.des3PubId}" id="des3PubId" />
  <div class="detail-header__box" id="detail_head">
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
      <div class="detail-header__actions">
        <div class="multiple-button-container"></div>
      </div>
    </div>
  </div>
  <div class="detail-main__box" style="display: flex; justify-content: center; margin: auto 0;">
    <div class="detail-main__main" style="flex-direction: row; position: relative; margin: 0px;">
      <div class="detail-main__left-box" style="max-width: 1200px;">
        <div class="detail-pub__box" style="width: 1200px;">
          <div class="detail-pub__main" id="detail_pub_main_zh" style="display: block; width: 90%;">
            <div class="detail-pub__title" style="width: 90%;">${pubDetailVO.title }</div>
            <!--   newaddstart -->
            <%@ include file="/WEB-INF/jsp/pub/pubdetails/pdwh_author_name.jsp"%>
            <!--   newaddend -->
            <div class="detail-pub__source" style="width: 90%;">${ !empty pubDetailVO.briefDesc ? pubDetailVO.briefDesc : "--"}</div>
            <c:if test="${!empty pubDetailVO.doi }">
              <div class="detail-pub__DOI" style="width: 90%;">
                <spring:message code="pub.details.info.doi" />
                <spring:message code="pub.details.info.colon" />
                <a href="${pubDetailVO.doiUrl }" target="_blank"> ${pubDetailVO.doi }</a>
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
            </div>
            <div class="detail-pub__keyword">
              <div class="detail-pub__keyword_title">
                <spring:message code="pub.details.info.institution" />
              </div>
              <div class="detail-pub__keyword_content">${!empty pubDetailVO.insNames ? pubDetailVO.insNames : "--"}</div>
            </div>
          </div>
          <div class="detail-pub__action detail-pub__action-alignment detail-pub__action-footer__function"
            style="justify-content: flex-start !important;">
            <div class="button__box button__model_rect" onclick="Pubdetails.outsideTip('${pubDetailVO.des3PubId}');">
              <a>
                <div
                  class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
                  <div class="button__inline-icon css-spirit__main">
                    <div class="css-spirit__icon spirit-icon__thumbup-outline"></div>
                  </div>
                  <span><spring:message code="pub.details.opt.award" /><iris:showCount count="${pubDetailVO.awardCount}" preFix="(" sufFix=")"/></span>
                </div>
              </a>
            </div>
            <div class="button__box button__model_rect" id="commentDiv" onclick="Pubdetails.outsideTip('${pubDetailVO.des3PubId}');">
              <a>
                <div
                  class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
                  <div class="button__inline-icon css-spirit__main">
                    <div class="css-spirit__icon spirit-icon__comment"></div>
                  </div>
                  <span><spring:message code="pub.details.opt.coment" /><iris:showCount count="${pubDetailVO.commentCount}" preFix="(" sufFix=")"/></span>
                </div>
              </a>
            </div>
            <div class="button__box button__model_rect"
              onclick="initShare('${pubDetailVO.des3PubId}',this);" resId="${pubDetailVO.des3PubId}"
              resType="22" pubId="${pubDetailVO.pubId}" databaseType="${databaseType}" dbId="${dbid}">
              <a>
                <div
                  class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
                  <div class="button__inline-icon css-spirit__main">
                    <div class="css-spirit__icon spirit-icon__share"></div>
                  </div>
                  <span><spring:message code="pub.details.opt.share" /><iris:showCount count="${pubDetailVO.shareCount }" preFix="(" sufFix=")"/></span>
                </div>
              </a>
            </div>
            <div class="button__box button__model_rect"
              onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${pubDetailVO.des3PubId}',this)">
              <div
                class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
                <div class="button__inline-icon css-spirit__main">
                  <div class="css-spirit__icon spirit-icon__cite"></div>
                </div>
                <span><spring:message code="common.cite" /></span>
              </div>
            </div>
            <div class="button__box button__model_rect" onclick="Pubdetails.outsideTip('${pubDetailVO.des3PubId}');">
              <a>
                <div
                  class="button__main button__style_flat button__size_dense button__color-plain color-display_grade ripple-effect">
                  <div class="button__inline-icon css-spirit__main">
                    <div class="css-spirit__icon spirit-icon__favorite-outline"></div>
                  </div>
                  <span><spring:message code="pub.details.opt.collect" /></span>
                </div>
              </a>
            </div>
          </div>
          <div class="detail-pub__tabs">
            <div class="detail-pub__tabs_header">
              <nav class="nav_horiz">
                <ul class="nav__list">
                  <li class="nav__item item_selected"><spring:message code="pub.details.comment.related" /></li>
                </ul>
                <div class="nav__underline"></div>
              </nav>
            </div>
            <div class="detail-comment__box">
              <div class="detail-post__box" style="display: none;">
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
                    <button class="button_main button_dense button_primary-reverse" id="pubCommnetBtn"
                      disabled="disabled" style="display: none;"
                      onclick="Pubdetails.pdwhComment('${pubDetailVO.des3PubId}')">
                      <spring:message code="pub.details.comment.publish" />
                    </button>
                    <button class="button_main button_dense button_grey" id="pubCommnetCancle" style="display: none;"
                      onclick="Pubdetails.cancelComment('${pubDetailVO.des3PubId}')">
                      <spring:message code="pub.details.comment.cancel" />
                    </button>
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
              title="<spring:message code="pub.details.fulltext.download_fulltext"/>">
              <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
            </div>
          </div>
        </c:if>
        <!-- 如果基准库没有全文附件，请求全文 -->
        <c:if test="${empty pubDetailVO.pdwhFullTextDownloadUrl}">
          <div class="request-add__paper-avator" onclick="Pubdetails.outsideTip('${pubDetailVO.des3PubId}');">
            <img class="request-add__paper-avator__img" src="${resmod}/images_v5/images2016/file_img.jpg"
              onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
            <div class="request-add__tip request-add__tip1"
              title="<spring:message code="pub.details.fulltext.req_fulltext"/>">
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
