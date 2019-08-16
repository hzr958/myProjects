<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="dynReplayInfoList.size > 0">
  <script type="text/javascript" src="/resmod/js/plugin/smate.custom.valuechange.js"></script>
  <div class="dynamic-cmt">
    <input type="hidden" name="dyn_size" value="${fn:length(dynReplayInfoList)}" /> <input type="hidden" name="nowDate"
      value="${nowDate }" />
    <div class="dynamic-cmt__list-box" id="scroll_${nowDate}">
      <s:if test="dynReplayInfoList.size > 0">
        <s:iterator value="dynReplayInfoList" var="reply">
          <input type="hidden" name="dynReplySize" />
          <ul class="dynamic-cmt__list">
            <li class="dynamic-cmt__item">
              <div class="dynamic-content__post">
                <div class="dynamic-post__avatar" onclick="dynamic.openPsnDetail('${des3ReplyerId}',event)">
                  <img src="${replyerAvatar}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                </div>
                <div class="dynamic-post__main">
                  <div class="dynamic-post__author-info">
                    <a class="dynamic-post__author-name" onclick="dynamic.openPsnDetail('${des3ReplyerId}',event)">
                      <c:if test="${locale =='en_US' }">
										    	${replyerEnName}
										    </c:if> <c:if test="${locale =='zh_CN' }">
										   		${replyerName}
										    </c:if>
                    </a>
                  </div>
                  <div>${replyContent}</div>
                  <s:if test=" #reply.replyPubTitle !=null && #reply.replyPubTitle !='' ">
                    <div class="dynamic-main__box">
                      <div class="dynamic-divider"></div>
                      <div class="dynamic-main__att">
                        <div class="pub-idx_small">
                          <div class="pub-idx__base-info">
                            <div class="pub-idx__main_box">
                              <div class="pub-idx__main">
                                <div class="pub-idx__main_title">
                                  <a target="_blank"
                                    href="/pub/details?des3PubId=${des3ReplyPubId }&currentDomain=/pubweb&pubFlag=1">${replyPubTitle}</a>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </s:if>
                </div>
                <div class="dynamic-post__time">${rebuildDate}</div>
              </div>
            </li>
          </ul>
        </s:iterator>
      </s:if>
    </div>
  </div>
</s:if>
