<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var shareI18 = '<spring:message code="publication.recommend.btn.share"/>';
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
            	  $('.shareCount_'+pubId).html("("+(data.shareTimes)+")");
            }
        }
    });
}
function getOutSideType (obj){
    var $obj = $("#share_to_scm_box");
    var des3ResId = $obj.attr("des3ResId");
    var pubId = $obj.attr("pubId");
    var outsideType = $(obj).attr("type");
    var ShareCount = Number($('.shareCount_'+pubId).text().replace(/[\D]/ig,""))+1;
    $('.shareCount_'+pubId).html("("+(ShareCount)+")");
    
    switch (outsideType) {
    case "WeChat":
        type=4;
        break;
    case "sina":
        type=5;
        break;
    case "Facebook":
        type=6;
        break;
    case "LinkedIn":
        type=7;
        break;
    case "Link":
        type=8;
        break;
    default:
        type=1;
        break;
    }
}

var requestUrl = {
        'sina' : 'http://v.t.sina.com.cn/share/share.php?',
        'tencent' : 'http://share.v.t.qq.com/index.php?c=share&a=index&',
        'LinkedIn' : 'http://www.linkedin.com/shareArticle?',
        'Facebook' : 'https://www.facebook.com/sharer/sharer.php?',
        'WeChat' : '',
        'Link' : ''
            
};
var tips = {
        "prompt_zh_CN" : "提示",
        "prompt_en_US" : "Reminder",
        "sessionTimeout_zh_CN" : "系统超时或未登录，你要登录吗？",
        "sessionTimeout_en_US" : "You are not logged in or session time out, please log in again."
};

function shareGrpCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
    shareCallback(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId);
}
//==============================
//分享回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
    var count = Number($('.shareCount_'+pubId).text().replace(/[\D]/ig,""))+1;
    $('.shareCount_'+pubId).text("("+count+")");
};
</script>
<c:if test="${not empty pubVO.pubInfoList || pubVO.isMySelf== 'true'}">
  <div class="container__card representPubDiv">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <spring:message code='homepage.title.represent.pub' />
        </div>
        <c:if test="${pubVO.isMySelf== 'true'}">
          <button class="button_main button_icon button_light-grey operationBtn" onclick="showPsnRepresentPubBox();">
            <i class="material-icons">edit</i>
          </button>
        </c:if>
      </div>
      <c:if test="${!empty pubVO.pubInfoList}">
        <div class="main-list__list item_no-border">
          <c:forEach items="${pubVO.pubInfoList }" var="representPub" varStatus="status">
            <input id="pubFullTextDownloadUrl" type="hidden" value="${representPub.fullTextDownloadUrl }" />
            <input id="pubFulltextImagePath" type="hidden" value="${representPub.fullTextImgUrl }" />
            <div class="main-list__item dev_pub_del_${representPub.pubId} dev_pub_list_div">
              <div class="main-list__item_content">
                <div class="pub-idx_medium">
                  <div class="pub-idx__base-info">
                    <div class="pub-idx__full-text_box">
                      <c:if
                        test="${representPub.fullTextFieId != null && representPub.fullTextFieId !='' && not empty representPub.fullTextDownloadUrl 
                        && (pubVO.isMySelf== 'true' || representPub.fullTextPermission == 0)}">
                        <div class="pub-idx__full-text_img"
                          style="cursor: pointer; position: relative; display: flex; justify-content: center; align-items: center;"
                          onclick="window.location.href='${representPub.fullTextDownloadUrl}';"
                          resId="${representPub.pubId }" des3id="${representPub.des3PubId}" restype="1" resnode="1">
                          <img src="${representPub.fullTextImgUrl }"
                            onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'"
                            style="width: 70px; height: 92px;"> <a class="new-tip_container-content"
                            title="<spring:message code='homepage.profile.pub.download'/>"> <img
                            src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                            src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                          </a>
                        </div>
                      </c:if>
                      <c:if test="${ pubVO.isMySelf== 'true'}">
                        <c:if test="${representPub.fullTextFieId == null || representPub.fullTextFieId ==''}">
                          <div class="pub-idx__full-text_img" style="border: none;">
                            <div class="pub-idx__full-text_img" style="position: relative; margin: 0px;">
                              <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img"
                                onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                              <!-- 上传全文 start -->
                              <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                                des3Id="${representPub.pubId}">
                                <div class="fileupload__box" onclick="fileuploadBoxOpenInputClick(event);"
                                  title="<spring:message code='homepage.profile.pub.upload'/>">
                                  <div class="fileupload__core initial_shown">
                                    <div class="fileupload__initial">
                                      <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                                      <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                                      <input type="file" class="fileupload__input" style='display: none;'>
                                    </div>
                                    <div class="fileupload__progress">
                                      <canvas width="56" height="56"></canvas>
                                      <div class="fileupload__progress_text"></div>
                                    </div>
                                    <div class="fileupload__saving">
                                      <div class="preloader"></div>
                                      <div class="fileupload__saving-text"></div>
                                    </div>
                                    <div class="fileupload__finish"></div>
                                    <div class="fileupload__hint-text" style="display: none">添加全文</div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </c:if>
                      </c:if>
                      <c:if test="${pubVO.isMySelf== 'false'}">
                        <c:if test="${empty representPub.fullTextDownloadUrl || representPub.fullTextPermission != 0}">
                          <div class="pub-idx__full-text_img" style="position: relative">
                          <c:if test="${empty representPub.fullTextFieId || empty representPub.fullTextDownloadUrl}">
                            <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img"
                              onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                            </c:if>
                            <c:if test="${representPub.fullTextFieId != null && representPub.fullTextFieId !='' && not empty representPub.fullTextDownloadUrl}">
                              <img src="${representPub.fullTextImgUrl }" class="dev_pub_img"
                              onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
                            </c:if>
                            <!-- 请求全文 start -->
                            <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                              des3Id="${representPub.des3PubId}">
                              <div class="fileupload__box"
                                onclick="Pubdetails.requestFullText(this,'<iris:des3 code='${representPub.ownerPsnId}'/>','${representPub.des3PubId}');"
                                title="<spring:message code='homepage.pub.details.fulltext.req_fulltext'/>">
                                <div class="fileupload__core initial_shown">
                                  <div class="fileupload__initial">
                                    <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                                    <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                                    <!-- 这个 input 框要加上,要不上传全文插件scm-pc_filedragbox.js报错-->
                                    <input type="file" class="fileupload__input" style='display: none;'>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                          <!-- 请求全文 end -->
                        </c:if>
                      </c:if>
                    </div>
                    <div class="pub-idx__main_box">
                      <div class="pub-idx__main dev_pub_list_div">
                        <div class="pub-idx__main_title  pub-idx__main_title-multipleline" style="height: 40px; overflow: hidden;" >
                          <a class="dev_pub_title multipleline-ellipsis__content-box" href="${representPub.pubIndexUrl}" target="_blank">${representPub.title}</a>
                        </div>
                        <div class="pub-idx__main_author">${representPub.authorNames }</div>
                        <div class="pub-idx__main_src">${representPub.briefDesc }</div>
                      </div>
                      <div class="new-Standard_Function-bar">
                        <c:if test="${representPub.isAward == 0}">
                          <div class="new-Standard_Function-bar_item" style="margin-left: 0px;"
                            isAward="${representPub.isAward}" onclick="Pub.pubAward('${representPub.des3PubId}',this)">
                            <i class="new-Standard_function-icon new-Standard_Praise-icon"></i><span
                              class="new-Standard_Function-bar_item-title"> <spring:message code="pub.like" /> <c:if
                                test="${representPub.awardCount > 0}">(${representPub.awardCount})</c:if></span>
                          </div>
                        </c:if>
                        <c:if test="${representPub.isAward == 1}">
                          <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected"
                            style="margin-left: 0px;" isAward="${representPub.isAward}"
                            onclick="Pub.pubAward('${representPub.des3PubId}',this)">
                            <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                              class="new-Standard_Function-bar_item-title"> <spring:message code="pub.unlike" />
                              <c:if test="${representPub.awardCount > 0}">(${representPub.awardCount})</c:if></span>
                          </div>
                        </c:if>
                        <div class="new-Standard_Function-bar_item idx-social__item dev_pub_share_${representPub.pubId}"
                          onclick="SmateShare.getPsnPubListSareParam(this); initSharePlugin(this);"
                          resId="${representPub.des3PubId}" resType="1" pubId="${representPub.pubId}">
                          <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                            class="new-Standard_Function-bar_item-title"> <spring:message
                              code="publication.recommend.btn.share" /> <span class="shareCount_${representPub.pubId}"><c:if
                                test="${representPub.shareCount > 0}">(${representPub.shareCount})</c:if> </span></span>
                        </div>
                        <div class="new-Standard_Function-bar_item"
                          onclick="Pub.showSnsQuote('/pub/ajaxpubquote','${representPub.des3PubId}',this)">
                          <i class="new-Standard_function-icon new-Standard_Quote-icon"></i> <span
                            class="new-Standard_Function-bar_item-title"> <spring:message code="common.cite" />
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </div>
</c:if>
<c:if test="${pubVO.isMySelf== 'true'}">
  <div class="dialogs__box representPubDiv" dialog-id="representPubBox" cover-event="hide"
    style="width: 720px; height: 600px;" id="representPubBox">
    <%@ include file="./psn_representpub_edit.jsp"%>
  </div>
</c:if>