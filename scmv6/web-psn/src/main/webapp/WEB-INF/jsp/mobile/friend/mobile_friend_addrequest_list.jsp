<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	//slideDom("add_friend_item", 35, true, leftMoveItemFrdCallBack, rightMoveItemFrdCallBack, clickItemFrdCallBack);
	//slideDom("accept_psn", 0, false, leftMoveBtnFrdCallBack, rightMoveBtnFrdCallBack, clickBtnFrdCallBack);
});
//点击进入个人主页
function goTofriendlPage(des3PsnId){
	window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId=" + des3PsnId;
}
function leftMoveItemFrdCallBack(obj, event){
	Msg.refusetHandler($(obj).attr("des3PsnId"), $(obj).attr("psnId"));
}

function rightMoveItemFrdCallBack(obj, event){
	$(obj).css('transform', 'translateX(0px)');
	$(obj).css('transition', '0.3s');
	setTimeout(function(){
		$(obj).css('transition', '0s')
	},10);//回复原位
}

function clickItemFrdCallBack(obj, event){
	$(obj).css('transform', 'translateX(0px)');
	$(obj).css('transition', '0.3s');
	setTimeout(function(){
		$(obj).css('transition', '0s')
	},10);//回复原位
}

function leftMoveBtnFrdCallBack(obj, event){
	$(obj).parents("div.add_friend_item").css('transform', 'translateX(0px)');
	$(obj).parents("div.add_friend_item").css('transition', '0.3s');
	setTimeout(function(){
		$(obj).parents("div.add_friend_item").css('transition', '0s')
	},10);
	event.preventDefault();
	event.stopPropagation();
}

function rightMoveBtnFrdCallBack(obj, event){
	$(obj).parents("div.add_friend_item").css('transform', 'translateX(0px)');
	$(obj).parents("div.add_friend_item").css('transition', '0.3s');
	setTimeout(function(){
		$(obj).parents("div.add_friend_item").css('transition', '0s')
	},10);
	event.preventDefault();
	event.stopPropagation();
}

function clickBtnFrdCallBack(obj, event){
	var des3PsnId = $(obj).attr("des3PsnId");
	var psnId = $(obj).attr("psnIds");
	Msg.requestHandler(des3PsnId, psnId);
	event.preventDefault();
	event.stopPropagation();
}
</script>
<c:forEach items="${psnInfoList}" var="inbox" varStatus="stat">
  <div class="list_item_container add_friend_item" id="list_item_container_${inbox.psnId}"
    des3PsnId="${inbox.des3PsnId}" psnId="${inbox.psnId}">
    <input id="des3PsnId" name="des3PsnId" type="hidden" value='${inbox.des3PsnId}'> <input id="personId"
      name="personId" type="hidden" value="${inbox.psnId}">
    <div class="list_item_section left" des3PsnId="${inbox.des3PsnId}"></div>
    <div class="list_item_section psn_info " des3PsnId="${inbox.des3PsnId}">
      <div class="person_namecard_whole hasBg" >
        <div class="avatar" onclick="goTofriendlPage('${inbox.des3PsnId}');">
          <img class="avatar" src="${inbox.avatarUrl}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
        </div>
        <div class="person_information" onclick="goTofriendlPage('${inbox.des3PsnId}');">
          <div class="name">${inbox.name}</div>
          <div class="institution">
            <c:if test="${not empty inbox.posAndTitolo && not empty inbox.insAndDep}">${inbox.insAndDep},&nbsp;${inbox.posAndTitolo}</c:if>
            <c:if test="${empty inbox.posAndTitolo && not empty inbox.insAndDep}">${inbox.insAndDep}</c:if>
            <c:if test="${not empty inbox.posAndTitolo && empty inbox.insAndDep}">${inbox.posAndTitolo}</c:if>
          </div>
        </div>
        <div class="operations" onclick="Msg.requestHandler('${inbox.des3PsnId}', '${inbox.psnId}', event);">
          <div class="btn_normal btn_bg_blue accept_psn" style="margin-right: 0px;" des3PsnId='' psnIds="${inbox.psnId}">接受</div>
          <input type="hidden" name="lastDes3PsnId" value='' />
        </div>
        
        <div class="add-frind_ignore" onclick="Msg.refusetHandler('${inbox.des3PsnId}', '${inbox.psnId}')">忽略 </div>
        
      </div>
    </div>
    <!-- <div class="list_item_section del_btn">
      <div class="list_item_right_commands bg_red refuse_psnman">
        <i class="material-icons icon"></i>
        <div>拒 绝</div>
      </div>
    </div> -->
  </div>
</c:forEach>