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
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/msgbox/mobile.msg.js"></script>
<%-- <script type="text/javascript" src="${res}/js/publication/scholar.view.js"></script> --%>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>

<title>科研之友</title>
</head>
<script type="text/javascript">
var ctxpath = '/scmwebsns';
</script>
<body>
  <div class="header">
    <div class="header_toolbar">
      <div class="header_toolbar_tools"
        style="width: 10vw; display: flex; justify-content: center; align-items: center; padding: 0px !important">
        <i class="material-icons paper__func-header__tip" onclick="Msg.arrowBack('${pubOperateVO.toBack}');"
          style="margin-top: 6px;">keyboard_arrow_left</i>
      </div>
      <div class="header_toolbar_title"
        style="font-size: 19px; width: 80vw; display: flex; justify-content: center; align-items: center;">全文认领</div>
      <div style="width: 10vw; display: flex; justify-content: center; align-items: center;"></div>
    </div>
  </div>
  <c:if test="${totalCount >0 }">
    <input type="hidden" id="currentDes3PsnId" name="currentDes3PsnId" value="${des3PsnId}" />
    <div style="height: 56px;"></div>
    <div class="body_content">
      <div class="body_content_container">
        <div class="list_container" id="listdiv" style="padding: 0px 20px;">
          <c:forEach items="${pubRcmdftList}" var="page" varStatus="stat">
            <input class="rcmd_pub_fulltext_tr" id="rcmd_pub_fulltext_tr_${page.id}"
              name="rcmd_pub_fulltext_tr_${page.id }" type="hidden" value="${page.id }">
            <input id="page.pub.zhTitle" name="page.pub.zhTitle" type="hidden" value="${page.pub.title}">
            <div class="list_item_container" id="fulltext_confirmation_container_${page.id}" style="border: none;">
              <div class="list_item_section"></div>
              <div class="list_item_section" style="flex-direction: column; border: none; ">
                <div class="pub_whole" style="border-bottom: 1px solid #ddd; padding: 20px 20px 20px 0px;">
                  <div class="pub_avatar pub_avatar-normal_size" onclick="Msg.downloadFTFile('${page.downloadUrl }');">
                    <img src="${page.fullTextImagePath}" onerror="javascript:this.src='${resmod}/images_v5/images2016/file_img1.jpg'"
                      style="border: 1px solid #ccc;">
                  </div>
                  <div class="pub_information">
                    <div class="title webkit-multipleline-ellipsis"
                      onclick="Msg.openPubfulltextDetail('${stat.index + 1}','${pubOperateVO.toBack}')">${page.pub.title}</div>
                    <div class="author">${page.pub.authorNames}</div>
                    <div class="source" style="width:90%">${page.pub.briefDesc}</div>
                    <div style="width: 100%; display: flex; justify-content: flex-start;">
                      <div class="operations" style="transform: translateX(-8px); display: flex; width: 100%;">
                         <div onclick="Msg.doConfirmPubft(${page.id});" class="btn_normal btn_bg_blue dev_yes_botton">是成果的全文</div>
                         <div onclick="Msg.doRejectPubft(${page.id});" class="btn_normal btn_bg_origin  btn_normal-not_mine dev_not_botton">不是成果的全文</div>
                      </div>
                    </div>
                  </div>
                </div>
 
              </div>
              <div class="list_item_section"></div>
            </div>
          </c:forEach>
        </div>
      </div>
    </div>
  </c:if>
  <c:if test="${totalCount==0 }">
    <div class="no_msg" style="padding-top: 67px; text-align: center;">未找到全文认领记录</div>
  </c:if>
</body>
</html>
