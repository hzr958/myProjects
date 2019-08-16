<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		 <c:if test="${groupDynShowInfoList.size() > 0 }">
			//显示动态时间
			groupDynamic.showDynsTime();
			//加载动态附带 的评论
			groupDynamic.getCommentFordyn();
		 </c:if>
		$(".class_current_avatars").attr("src",$("#currentAvatars").val());
	});
</script>
<s:iterator value="groupDynShowInfoList" var="groupDynShowInfo" status="gds">
  <div class="dynamic_container">
    <input type="hidden" class="dynTime" value="${dynDateForShow }" /> ${groupDynShowInfo.dynContent }
    <div class="dynamic_operations_container">
      <div class="single_action" style="border: none;" id="group_dyn_id_${groupDynShowInfo.dynId }"
        onclick="groupDynamic.award('${groupDynShowInfo.dynId }');">
        <!--  0  表示已经赞了 -->
        <c:if test="${awardstatus ==0 }">
          <s:text name="group.discuss.canclePraise"></s:text>
          <s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
        </c:if>
        <c:if test="${awardstatus !=0 }">
          <s:text name="group.discuss.praise"></s:text>
          <s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
        </c:if>
      </div>
      <div class="single_action" onclick="groupDynamic.clickComments('${groupDynShowInfo.dynId }',this)">
        <s:text name="group.discuss.comment"></s:text>
        <s:if test="#groupDynShowInfo.commentCount!=null&&#groupDynShowInfo.commentCount!=0">(${groupDynShowInfo.commentCount })</s:if>
      </div>
      <c:if test="${resType =='pub' }">
        <div class="single_action" style="position: relative;">
          <a onclick="groupDynamic.showPubInfoToShareUINew(this); initSharePlugin(this);"
            class="share_sites_show share_tile " resId="" des3ResId="" resType="1" dbid="" style="color: #2196f3;">
            <s:text name="group.discuss.share"></s:text> <s:if
              test="#groupDynShowInfo.shareCount!=null&&#groupDynShowInfo.shareCount!=0">(${groupDynShowInfo.shareCount })</s:if>
          </a>
        </div>
      </c:if>
    </div>
    <%----------附带的评论位置------------------------------------------------------------------------- --%>
    <div class="dynamic_comment_list" style="display: none;"></div>
    <div class="dynamic_comment_action" style="display: none;">
      <div>
        <img src='${ currentAvatars}' class="avatar class_current_avatars">
      </div>
      <textarea class="comment_content textarea_autoresize" placeholder="发表评论..."
        id="comment_content_${groupDynShowInfo.dynId }"></textarea>
      <div>
        <div class="btn_normal btn_bg_origin fc_blue500"
          onclick="groupDynamic.publishCommnet(this,'${groupDynShowInfo.dynId }')">发布</div>
      </div>
    </div>
  </div>
</s:iterator>