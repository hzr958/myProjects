<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function load(){
	$(".current_psn_avatar").find("img").attr("src",$("#publish_current_psn_id").find("img").attr("src")).removeAttr("onload");
}
</script>
<div class="dynamic-cmt__list-box">
  <ul class="dynamic-cmt__list">
    <s:iterator value="groupDynCommentsShowInfoList" var="groupDynShowInfo">
      <li class="dynamic-cmt__item">
        <div class="dynamic-content__post">
          <div class="dynamic-post__avatar">
            <a href="/psnweb/homepage/show?des3PsnId=${groupDynShowInfo.des3PersonId}" target="_Blank"><img
              src="${groupDynShowInfo.avatars }" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
          </div>
          <div class="dynamic-post__main">
            <div class="dynamic-post__author">
              <a class="dynamic-post__author_name"
                href="/psnweb/homepage/show?des3PsnId=${groupDynShowInfo.des3PersonId}" target="_Blank"> <s:if
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
                            <a onclick='DiscussOpenDetail.openPubDetail("${ groupDynShowInfo.des3CommentResId}",event)'>
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
<div class="dynamic-cmt__post">
  <div class="dynamic-content__post">
    <div class="dynamic-post__avatar current_psn_avatar">
      <a href="/psnweb/homepage/show?des3PsnId=${des3CurrentPsnId }" target="_Blank"> <img onload="load()"
        src="${userData.avatars }" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
      </a>
    </div>
    <div class="dynamic-post__main">
      <div class="form__sxn_row">
        <div class="input__box">
          <div class="input__area">
            <textarea class="global_no-border publish_comment_text" maxlength="300"
              placeholder="<s:text name='groups.base.dynamic.commentTips' />"></textarea>
            <div class="textarea-autoresize-div"></div>
          </div>
        </div>
      </div>
      <div class="dynamic-main__box comment_res_box" style="display: none">
        <div class="dynamic-divider"></div>
        <div class="dynamic-main__att">
          <div class="pub-idx_small">
            <div class="pub-idx__base-info">
              <div class="pub-idx__main_box">
                <div class="pub-idx__main aleady_select_pub" pubId="">
                  <div class="pub-idx__main_title">
                    <a class="comment_res_title"></a>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="att-delete-icon" onclick="GrpSelectPub.clearSelectedPub(this);">
            <i class="material-icons">close</i>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="dynamic-cmt__post_actions">
    <button class="button_main button_grey button_icon" onclick='GrpDiscussComment.showSelectPubBox(this);'>
      <i class="material-icons">description</i>
    </button>
    <button class="button_main button_dense button_primary" onclick="GrpDiscussComment.commentDiscuss(this);">
      <s:text name='groups.base.dynamic.btnPost' />
    </button>
  </div>
</div>
