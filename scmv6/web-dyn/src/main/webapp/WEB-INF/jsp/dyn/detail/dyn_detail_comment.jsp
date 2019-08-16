<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" id="locale" value="${locale}" />
<input type="hidden" id="totalCount" value="${totalCount}" />
<s:if test="dynReplayInfoList.size > 0">
  <s:iterator value="dynReplayInfoList" var="reply">
    <input type="hidden" name="dynReplySize" />
    <div class="dynamic_one">
      <div onclick="dynamic.openPsnDetail('${des3ReplyerId}',event)">
        <a href="javascript:;" class="dynamic_head"><img src="${replyerAvatar}"
          onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
        <p>
          <span class="fr">${rebuildDate}</span><em> <c:if test="${locale =='en_US' }">
	    ${replyerEnName}
	    </c:if> <c:if test="${locale =='zh_CN' }">
	    ${replyerName}
	    </c:if>
          </em>
        </p>
      </div>
      <p class="p2">${replyContent}
        <s:if test=" #reply.replyPubTitle !=null && #reply.replyPubTitle !='' ">
          <br />
          <a style="color: #2882d8 !important;" target="_blank"
            href="/pub/details/snsnonext?des3PubId=${des3ReplyPubId }">${replyPubTitle}</a>
        </s:if>
      </p>
    </div>
  </s:iterator>
</s:if>
