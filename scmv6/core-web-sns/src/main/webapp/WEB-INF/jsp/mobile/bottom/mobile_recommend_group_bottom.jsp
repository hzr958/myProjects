<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript">


//菜单选中事件
function selectRecommendPubMenu(type){
    //推荐菜单
    if("recommend" == type){
      $("#mobile_find_menu_icon").addClass("new-mobilepage_footer-item_tip-discover_selected");
      $("#mobile_collected_menu_icon").removeClass("new-mobilepage_footer-item_tip-enshrine_selected");
      $("#mobile_find_menu_word").css("color", "#2196f3");
      $("#mobile_recommend_menu_word").css("color","#666");
    }else{
      //收藏菜单
      $("#mobile_find_menu_icon").removeClass("new-mobilepage_footer-item_tip-discover_selected");
      $("#mobile_collected_menu_icon").addClass("new-mobilepage_footer-item_tip-enshrine_selected");
      $("#mobile_find_menu_word").css("color","#666");
      $("#mobile_collected_menu_word").css("color", "#2196f3");
    }
//    dealPubMenuClickEvent();
}
//论文推荐
function linkRecommed(){
  window.history.replaceState({}, "", "https://" + document.domain + "/grp/mobile/findgroupmain");
  window.location.reload();
};

//论文收藏
function linkMyGroup(){
  window.history.replaceState({}, "", "https://" + document.domain + "/grp/mobile/mygroupmain");
  window.location.reload();
}
</script>
<!-- <div style="width: 100vw; height: 9vh; overflow-x: hidden; position: fixed; bottom: 0px; left: 0px;"> -->
<div class="new-mobilepage_footer-container" style="justify-content: space-around;">
    <div class="new-mobilepage_footer-item" style="position: relative;" onclick="linkRecommed();">
        <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-discover" id="mobile_find_menu_icon"></i>
        <span class="new-mobilepage_footer-item_title" id="mobile_find_menu_word" style="color: rgb(102, 102, 102);">群组发现</span>
    </div>
    <div class="new-mobilepage_footer-item" style="position: relative;" onclick="linkMyGroup();">
        <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-enshrine" id="mobile_collected_menu_icon"></i>
        <span class="new-mobilepage_footer-item_title" id="mobile_collected_menu_word" style="color: rgb(102, 102, 102);">我的群组</span>
    </div>
</div>
<!-- </div> -->
