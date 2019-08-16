<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<!--全文请求列表  -->
<s:iterator value="msgShowInfoList" var="msil" status="st">
  <div class="message-page__body-container main-list__item msg-req-item" pub-id="<iris:des3 code='${msil.pubId}'/>"
    msg-id="<iris:des3 code='${msil.msgRelationId}'/>"   style="padding: 0 0px 0px 16px; border: none;">
    <div class="message-page__body-container__infor"
      style="display: flex; justify-content: flex-start; color: #666; font-size: 16px;">
      <c:if test="${locale == 'en_US' }">
			  	You have a full-text request from&nbsp;<a class="request__psn-name request__psn-name_link" style="color:#2196f3" href="${senderShortUrl }">${senderEnName}</a>
      </c:if>
      <c:if test="${locale == 'zh_CN' }">
				你有一篇来自<a class="request__psn-name request__psn-name_link" style="color:#2196f3" href="${senderShortUrl }">${senderZhName}</a>的全文请求
			 </c:if>
    </div>
    <div class="full-request__content" style="margin: 12px 0px 0px 0px; padding-bottom: 20px; border-bottom: 1px solid #ddd; position: relative;">
      <s:if test="hasPubFulltext =='false' ">
        <img class="full-request__avator" src="/resmod/images_v5/images2016/file_img.jpg">
      </s:if>
      <s:elseif test='pubFulltextImagePath =="" ||  pubFulltextImagePath==null '>
        <a href="javascript:midmsg.downloadFulltext('<iris:des3 code='${msil.pubId}'/>')"
          style="width: fit-content; height: fit-content;"> <img class="full-request__avator"
          src="/resmod/images_v5/images2016/file_img1.jpg"
          onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
        </a>
      </s:elseif>
      <s:else>
        <a href="javascript:midmsg.downloadFulltext('<iris:des3 code='${msil.pubId}'/>')"
          style="width: fit-content; height: fit-content;"> <img class="full-request__avator"
          src="${msil.pubFulltextImagePath}" onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
        </a>
      </s:else>
      <div class="full-request__content-infor">
        <a class="webkit-multipleline-ellipsis"
          href="javascript:midmsg.openPubDetail('<iris:des3 code='${msil.pubId}'/>')"> <c:if
            test="${locale == 'zh_CN' }">
            <s:property value="pubTitleZh" />
          </c:if> <c:if test="${locale == 'en_US' }">
            <s:property value="pubTitleEn" />
          </c:if>
        </a>
        <div class="full-request__content-author">
          <s:property value="pubAuthorName" escapeHtml="false" />
        </div>
        <div class="full-request__content-abstracts">
          <c:if test="${locale == 'zh_CN' }">
            <s:property value="pubBriefZh" />
          </c:if>
          <c:if test="${locale == 'en_US' }">
            <s:property value="pubBriefEn" />
          </c:if>
        </div>
        <div class="full-request__footer">
          <s:if test="hasPubFulltext =='true' ">
            <div class="full-request__footer-agree dev_footer-agree" style="width: 20vw;"
              onclick="midmsg.fullTextReqMsgclick(event)">
              <s:text name="dyn.msg.center.accept" />
            </div>
            <div class="full-request__footer-ignore" style="width: 20vw;"
              onclick="midmsg.fullTextReqMsgIgnore(event)">
              <s:text name="dyn.msg.center.ignore" />
            </div>
          </s:if>
          <s:else>
            <div class="full-request__footer-agree_topc ">
              <s:text name="dyn.msg.center.topc" />
            </div>
          </s:else>
        </div>
      </div>
      
      <div class="full-request__content-delete">删除</div>
    </div>
  </div>
</s:iterator>
