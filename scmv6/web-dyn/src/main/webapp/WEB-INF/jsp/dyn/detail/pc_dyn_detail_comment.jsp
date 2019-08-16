<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="/resmod/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript">
	/* function comment_Change(comThis){
		//console.log("len=="+$(comThis).val().length) ;
		//dynamic-cmt__post 
		//&&$(comThis).css("color")!="rgb(204,204,204)"
		if($(comThis).val().length>0){
			$(comThis).closest(".dynamic-cmt__post").find(".button_primary").removeClass("button_dense");
			//$(comThis).prev().addClass("fb_btn");
			$(comThis).closest(".dynamic-cmt__post").find(".button_primary").attr("onclick","dynamic.pcDoComment(this);");
		}else{
			$(comThis).closest(".dynamic-cmt__post").find(".button_primary")("onclick");
			//$(comThis).prev().removeClass("fb_btn");
			$(comThis).closest(".dynamic-cmt__post").find(".button_primary").addClass("button_dense");
			
		}
	} */

</script>
<style type="text/css">
.phcolor {
  color: #999;
}
</style>
<div class="dynamic-cmt">
  <input type="hidden" name="dyn_size" value="${fn:length(dynReplayInfoList)}" /> <input type="hidden" name="nowDate"
    value="${nowDate }" />
  <div class="dynamic-cmt__list-box" id="scroll_${nowDate}">
    <s:if test="dynReplayInfoList.size > 0">
      <s:iterator value="dynReplayInfoList" var="reply">
        <input type="hidden" name="dynReplySize" />
        <ul class="dynamic-cmt__list">
          <li class="dynamic-cmt__item" style="padding: 10px 0px;">
            <div class="dynamic-content__post" style="padding: 16px 16px 6px 16px;">
              <div class="dynamic-post__avatar" onclick="dynamic.openPsnDetail('${des3ReplyerId}',event)">
                <img src="${replyerAvatar}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </div>
              <div class="dynamic-post__main">
                <div class="dynamic-post__author-info">
                  <a class="dynamic-post__author-name" onclick="dynamic.openPsnDetail('${des3ReplyerId}',event)"> <c:if test="${locale =='en_US' }">
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
                    <div class="dynamic-main__att" style="margin-bottom: 0px;">
                      <div class="pub-idx_small">
                        <div class="pub-idx__base-info">
                          <div class="pub-idx__main_box">
                            <div class="pub-idx__main">
                              <div class="pub-idx__main_title">
                                <c:if test="${not empty shortUrl }">
                                  <a target="_blank" href="${shortUrl }">${replyPubTitle}</a>
                                </c:if>
                                <c:if test="${empty shortUrl }">
                                  <a target="_blank"
                                    href="/pub/details?des3PubId=${des3ReplyPubId }&currentDomain=/pubweb&pubFlag=1">${replyPubTitle}</a>
                                </c:if>
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
  <div class="dynamic-cmt__post">
    <div class="dynamic-content__post" style="padding: 16px 16px 8px 16px;">
      <div class="dynamic-post__avatar" onclick="dynamic.openPsnDetail('${des3psnId}',event)">
        <img src="${psnAvatars }" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
      </div>
      <div class="dynamic-post__main">
        <div>
          <div class="input__area" style="padding: 4px 0px;">
            <textarea autofocus="autofocus" class="global_no-border dynamic-post__linelimit" name="comments"
              maxlength="300" cols="30" rows="10" placeholder="<s:text name='dyn.pc.dyndetail.addcomments'/>"></textarea>
            <div class="input-area__textarea-autoresize-div"></div>
          </div>
        </div>
        <div class="dynamic-main__box  dev_comment_share_pubinfo" style="display: none;">
          <div class="dynamic-divider aleady_select_pub" pubId=""></div>
          <div class="dynamic-main__att">
            <div class="pub-idx_small">
              <div class="pub-idx__base-info">
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <div class="pub-idx__main_title"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="att-delete-icon" onclick="MainBase.clearCommentSelectedPub();">
              <i class="material-icons">close</i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="dynamic-cmt__post_actions" style="padding: 0px 8px 8px 8px;">
      <button class="button_main button_grey button_icon" onclick="MainBase.showCommentPubUI();">
        <i class="material-icons">description</i>
      </button>
      <button class="button_main button_dense button_primary" name="comment_button" onclick="dynamic.pcDoComment(this);"
        disable>
        <s:text name="dyn.pc.dyndetail.comments.confirm" />
      </button>
    </div>
  </div>
</div>
<script style="">
/**
 * $(this).css("color")!="rgb(204, 204, 204)"
	 css("color","#ccc")
 */
$(function(){
	//dynamic.IEinitCommentFocus();
});

</script>