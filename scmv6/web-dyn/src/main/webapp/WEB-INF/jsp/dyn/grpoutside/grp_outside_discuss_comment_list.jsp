<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function load(){
	$(".current_psn_avatar").find("img").attr("src",$("#publish_current_psn_id").find("img").attr("src")).removeAttr("onload");
}
</script>
<div class="dynamic-cmt">
  <div class="dynamic-cmt__list-box">
    <ul class="dynamic-cmt__list">
      <s:iterator value="groupDynCommentsShowInfoList" var="groupDynShowInfo">
        <li class="dynamic-cmt__item">
          <div class="dynamic-content__post">
            <div class="dynamic-post__avatar">
              <a href="/psnweb/outside/homepage?des3PsnId=${groupDynShowInfo.des3PersonId}" target="_Blank"> <img
                src="${groupDynShowInfo.avatars }" onerror="this.onerror=null;this.src='/avatars/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
            <div class="dynamic-post__main">
              <div class="dynamic-post__author">
                <a class="dynamic-post__author_name"
                  href="/psnweb/outside/homepage?des3PsnId=${groupDynShowInfo.des3PersonId}" target="_Blank"> <s:if
                    test="#groupDynShowInfo.name!=null&&#groupDynShowInfo.name!=''">${groupDynShowInfo.name }</s:if> <s:else>${groupDynShowInfo.firstName }${groupDynShowInfo.lastName }</s:else>
                </a>
              </div>
              <div>${groupDynShowInfo.commentContent }</div>
              <c:if
                test="${ groupDynShowInfo.commentResId!=null && not empty groupDynShowInfo.commentResId && groupDynShowInfo.commentResId!=0}">
                <div class="dynamic-main__box">
                  <div class="dynamic-divider"></div>
                  <div class="dynamic-main__att">
                    <div class="pub-idx_small">
                      <div class="pub-idx__base-info">
                        <div class="pub-idx__main_box">
                          <div class="pub-idx__main">
                            <div class="pub-idx__main_title">
                              <a
                                onclick='DiscussOpenDetail.openPubDetail("${ groupDynShowInfo.des3CommentResId}",event)'>
                                <s:if test="#locale=='zh_CN'">
                                  <s:if
                                    test="#groupDynShowInfo.commentResZhTitle==null || #groupDynShowInfo.commentResZhTitle==''">${groupDynShowInfo.commentResEnTitle }</s:if>
                                  <s:else>${groupDynShowInfo.commentResZhTitle}</s:else>
                                </s:if> <s:else>
                                  <s:if
                                    test="#groupDynShowInfo.commentResEnTitle==null || #groupDynShowInfo.commentResEnTitle==''">${groupDynShowInfo.commentResZhTitle }</s:if>
                                  <s:else>${groupDynShowInfo.commentResEnTitle }</s:else>
                                </s:else>
                              </a>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </c:if>
            </div>
            <div class="dynamic-post__time">${groupDynShowInfo.commentDateForShow }</div>
          </div>
        </li>
      </s:iterator>
    </ul>
  </div>
</div>