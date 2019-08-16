<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css" />

<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/mobile.pub.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>

<title>科研之友</title>
<script type="text/javascript">

changCheckBox = function() {
    if (document.getElementsByClassName("pub-idx__main_add-tip")) {
        var addlist = document.getElementsByClassName("pub-idx__main_add-tip");
        for (var i = 0; i < addlist.length; i++) {
            addlist[i].onclick = function() {
                if (this.innerHTML != "check_box") {
                    this.innerHTML = "check_box";
                } else {
                    this.innerHTML = "check_box_outline_blank";
                }
            }
        }
    }
};

//移动端-打开成果详情
</script>
</head>
<body>
  <div class="header">
    <div class="header_toolbar">
      <div class="header_toolbar_tools" onclick="Msg.arrowBack('${pubListVO.fromPage}');"
        style="width: 10vw; padding: 0px !important">
        <i class="material-icons paper__func-header__tip" style="margin-top: 6px">keyboard_arrow_left</i>
      </div>
      <div class="header_toolbar_title"
        style="font-size: 19px; width: 80vw; display: flex; justify-content: center; align-items: center;">成果认领</div>
      <div style="width: 10vw;"></div>
    </div>
  </div>
  <div style="height: 56px;"></div>
  <c:if test="${not empty pubListVO.resultList}">
    <div class="body_content">
      <div class="body_content_container">
        <div class="list_container" id="listdiv">
          <div class="pub-idx__main_add" style="border-bottom: 1px solid #ddd;">
            <i class="material-icons pub-idx__main_add-tip" id="isCheck" onclick="changCheckBox()">check_box</i><span
              class="pub-idx__main_add-detail">邀请我的合作者成为联系人</span>
          </div>
          <c:if test="${pubListVO.hasLogin == 1}">
            <c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
              <div class="list_item_container" id="pub_${pub.pubId}" name="dataCount"
                des3PubId="<iris:des3 code='${pub.pubId }'/>" des3PsnId="<iris:des3 code='${psnId}'/>" style="border: none; padding: 0px;">
                <div class="list_item_section"></div>
                <div class="list_item_section" style="padding-left: 20px;">
                  <div class="pub_whole" style="padding: 20px 0px 20px 0px; border-bottom: 1px solid #ddd;">
                    <div class="pub_avatar" onclick="mobile.pub.downloadFTFile('${pub.fullTextDownloadUrl }')">
                      <c:if test="${not empty pub.fullTextFieId}">
                        <img src="${pub.fullTextImgUrl}"
                          onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" class="pub_avatar"
                          style="border: 1px solid #ccc;">
                      </c:if>
                      <c:if test="${empty pub.fullTextFieId}">
                        <img src="${resmod}/images_v5/images2016/file_img.jpg" class="pub_avatar"
                          style="border: 1px solid #ccc;">
                      </c:if>
                    </div>
                    <div class="pub_information">
                      <div class="title webkit-multipleline-ellipsis"
                        onclick="mobile.pub.pdwhDetails('<iris:des3 code='${pub.pubId }'/>');">
                        <c:out value="${pub.title}" escapeXml="false" />
                      </div>
                      <div class="author">
                        <c:out value="${pub.authorNames}" escapeXml="false" />
                      </div>
                      <div class="source" style="width: 90%;">
                        <c:out value="${pub.briefDesc}" escapeXml="false" />
                      </div>
                      <div class="operations" style="transform: translateX(-8px)">
                        <div onclick="Msg.AffirmConfirmPub(this,'${pub.pubId}');" class="btn_normal btn_bg_blue">这是我的成果</div>
                        <div onclick="Msg.IgnoreConfirmPub(this,'${pub.pubId}');"
                          class="btn_normal btn_bg_origin btn_normal-not_mine">不是我的成果</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="list_item_section"></div>
              </div>
            </c:forEach>
          </c:if>
        </div>
      </div>
    </div>
  </c:if>
  <c:if test="${empty pubListVO.resultList}">
    <div class=" noRecord" style="margin-top: -40px">
      <div class="content">
        <div class="no_effort">
          <h2 class="tc">对不起，未找到相关记录。</h2>
          <div class="no_effort_tip pl27">
            <span>如有需要请联系客服（电话：400-675-1236）</span>
          </div>
        </div>
      </div>
    </div>
  </c:if>
</body>
</html>
