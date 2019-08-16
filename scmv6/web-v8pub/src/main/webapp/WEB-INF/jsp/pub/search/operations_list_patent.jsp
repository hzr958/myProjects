
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/iristaglib.tld" prefix="iris"%>
<script type="text/javascript">
</script>
<div class="new-Standard_Function-bar">
  <a href="javascript:;" class="manage-one mr20" isAward="${pubInfo.isAward == 1}"
    onclick="Pub.pdwhAward('${pubInfo.des3PubId}',this)"> <c:if test="${pubInfo.isAward == 1}">
      <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;">
        <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
          class="new-Standard_Function-bar_item-title"><spring:message code="pub.search.unaward" /><iris:showCount count="${pubInfo.awardCount}" preFix="(" sufFix=")"/></span>
      </div>
    </c:if> <c:if test="${pubInfo.isAward == 0}">
      <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
        <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
          class="new-Standard_Function-bar_item-title"><spring:message code="pub.search.award" /><iris:showCount count="${pubInfo.awardCount}" preFix="(" sufFix=")"/></span>
      </div>
    </c:if>
  </a> <a href="${pubInfo.pubIndexUrl}" class="manage-one mr20" target="_blank">
    <div class="new-Standard_Function-bar_item">
      <i class="new-Standard_function-icon new-Standard_comment-icon"></i> <span
        class="new-Standard_Function-bar_item-title"> <spring:message code="pub.search.coment" /><iris:showCount count="${pubInfo.commentCount}" preFix="(" sufFix=")"/></span>
    </div>
  </a> <input type="hidden" class="share_title_input" value='${pubInfo.title}' /> <a
    class="manage-one mr20 dev_pdwhpub_share" onclick="initShare(this);"
    resId="${pubInfo.des3PubId}" resType="22" pdwhpubShare="true" databaseType="2" dbId="${pubInfo.dbid}">
    <div class="new-Standard_Function-bar_item">
      <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
        class="new-Standard_Function-bar_item-title"> <spring:message code="pub.search.share" /><iris:showCount count="${pubInfo.shareCount }" preFix="(" sufFix=")"/></span>
    </div>
  </a>
       <a class="manage-one mr20" onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${pubInfo.des3PubId}',this)">
       <div class="new-Standard_Function-bar_item">
          <i class="new-Standard_function-icon new-Standard_Quote-icon"></i> <span
            class="new-Standard_Function-bar_item-title"><spring:message code="common.cite" /> </span>
        </div>
       </a>
  <a href="javascript:;" class="manage-one mr20" collected="${pubInfo.isCollected}"
    onclick="Pub.dealCollectedPub('${pubInfo.des3PubId}','PDWH',this)"> <c:if test="${pubInfo.isCollected==0}">
      <div class="new-Standard_Function-bar_item">
        <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
          class="new-Standard_Function-bar_item-title"> <spring:message code="pub.search.collect" />
        </span>
      </div>
    </c:if> <c:if test="${pubInfo.isCollected==1}">
      <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
        <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
          class="new-Standard_Function-bar_item-title"> <spring:message code="pub.search.unsave" />
        </span>
      </div>
    </c:if>
  </a>
</div>
