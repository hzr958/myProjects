<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		$("#id_grp_add_friend_names_list").find(".psn-idx_small").click(function(){
			if($(this).hasClass("psn_chosen")){
				$(this).removeClass("psn_chosen");
				SmateShare.clickChooiseFriend($(this),false);
			}else{
				$(this).addClass("psn_chosen");
				SmateShare.clickChooiseFriend($(this),true);
			}
		});
		//以下是记录查询的条数 start
		var count  =$("#id_grp_add_friend_names_list").find(".friend-selection__item-3").length ;
		var totalcount  =$("#id_grp_add_friend_names_list").attr("totalcount") ;
		var currentCount  =$("#id_grp_add_friend_names_list").attr("currentCount") ;
		//说明是第一次进来
		if(count >totalcount ){
			$("#id_grp_add_friend_names_list").attr("totalcount" , count)
		} 
		$("#id_grp_add_friend_names_list").attr("currentCount" ,count);
		//以下是记录查询的条数 end
	});
	</script>
<s:iterator value="psnInfoList" var="psnInfo" status="st">
  <div class="friend-selection__item-3">
    <div class="psn-idx_small ">
      <div class="psn-idx__base-info">
        <div class="psn-idx__avatar_box">
          <div class="psn-idx__avatar_img">
            <img src="<s:property value="#psnInfo.avatarUrl"/>">
          </div>
        </div>
        <div class="psn-idx__main_box">
          <div class="psn-idx__main">
            <div class="psn-idx__main_name" title="<s:property value='#psnInfo.name' escape='false'/>"
              code="<s:property value='#psnInfo.des3PsnId'/>">
              <s:property value="#psnInfo.name" escape="false" />
            </div>
            <div class="psn-idx__main_title">
              <s:property value='#psnInfo.insName' escape="false" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>
