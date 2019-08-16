<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>科研之友</title>
<link href="${resmod }/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod }/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/scmjscollection.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">

var locale='${locale}';
var searchTypeTip="<s:text name='page.main.search.tip' />";
var searchType="<s:text name='page.main.search' />";
var searchType2="<s:text name='page.main.search2'/>";
var searchType3="<s:text name='page.main.search3'/>";
var searchType4="<s:text name='page.main.search4'/>";

</script>
</head>
<body>
  <div class="header"
    style="box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.2), 0 4px 5px 0 rgba(0, 0, 0, 0.14), 0 1px 10px 0 rgba(0, 0, 0, 0.12);">
    <div class="hd_wrap" style="width: 1280px;">
      <div style="display: flex; align-items: center; justify-content: space-around;">
        <a href="/" class="logo" style="margin-right: 40px;"><img
          src="${resmod }/smate-pc/img/smatelogo_transparent.png"></a>
        <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get">
          <div class="main-page__search" style="margin-right: 360px; margin-top: 27px; width: 537px; margin-left: 0px;">
            <div class="main-page__searchbox">
              <input id="search_some_one"
                style="height: 100%; width: 100%; outline: none; border: none; margin-left: 6px; color: rgb(153, 153, 153);"
                maxlength="50" name="searchString" title="<s:text name='page.main.search.tip' />"
                placeholder="<s:text name='page.main.search.tip' />" autocomplete="off" value="">
              <div class="searchbox__icon" style="cursor: pointer;" onclick="ScmMaint.searchSomeOne()"></div>
            </div>
          </div>
        </form>
        <div class="logining">
          <div class="logining_icon"
            onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=http://test.scholarmate.com/center-oauth/WEB-INF/jsp/sns/V_SNS_index.jsp&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">
            <i class="question_icon" style="cursor: pointer;" title='<s:text name="common.sharefile.online"/>'></i>
          </div>
          <div class="version_en" style="margin-left: 8px !important;">
            <c:if test="${locale == 'zh_CN'}">
              <a onclick="BaseUtils.change_local_language('en_US');">&nbsp;English&nbsp;</a>
            </c:if>
            <c:if test="${locale != 'zh_CN'}">
              <a onclick="BaseUtils.change_local_language('zh_CN');">&nbsp;中文&nbsp;</a>
            </c:if>
          </div>
        </div>
      </div>
      <div class="clear"></div>
    </div>
  </div>
  <div class="check__share-container">
    <!-- 未登录 -->
    <s:if test=" status == 0 ">
      <div class="check__share-header">
        <div class="check__share-load__box">
          <div class="check__share-load__title">
            <s:text name="application.station.page.tip8" />
          </div>
          <div class="check__share-load__content">
            <div>
              <s:text name="application.station.page.tip7" />
            </div>
            <div class="check__share-load__footer">
              <div class="check__share-load__button" onclick="window.location.href='${loginUrl}' ">
                <s:text name="application.station.page.tip5" />
              </div>
              <div class="check__share-load__tip">
                <s:text name="application.station.page.tip2" />
                <span class="check__share-load__tip-content" style="cursor: pointer;"
                  onclick="window.location.href='${registerUrl}' "><s:text name="application.station.page.tip6" />
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </s:if>
    <div>
      <div class="check__share-item__title">
        <div class="check__share-item__file">
          <s:text name='common.sharefile.filename' />
        </div>
        <div class="check__share-item__type">
          <s:text name='common.sharefile.type' />
        </div>
        <div class="check__share-item__size">
          <s:text name='common.sharefile.size' />
        </div>
        <div class="check__share-item__time">
          <s:text name='common.sharefile.rcmddate' />
        </div>
      </div>
      <div class="check__share-item__container">
        <s:if test=" status == -1">
          <div class="check__share-item__list">
            <s:text name="commend.shareIsDel.tip" />
          </div>
        </s:if>
        <c:forEach items="${page.result }" var="form">
          <div class="check__share-item__list">
            <div class="check__share-item__list-file" style="cursor: pointer;"
              onclick="window.location.href='${form.downloadUrl}' ";  >
              <img class="check__share-item__list-avator" src="${form.imgThumbUrl }"
                onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
              <div class="check__share-item__list-name">${form.fileName }</div>
            </div>
            <div class="check__share-item__type">${form.fileViewType }</div>
            <div class="check__share-item__size">
              <fmt:formatNumber value="${form.fileSize/1024}" minFractionDigits="2"></fmt:formatNumber>
              KB
            </div>
            <div class="check__share-item__time">
              <fmt:formatDate value="${form.shareDate }" type="both" pattern="yyyy/MM/dd HH:mm:ss"></fmt:formatDate>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</body>
</html>