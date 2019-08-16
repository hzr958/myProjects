<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript">


//菜单选中事件
function selectRecommendPubMenu(type){
    //推荐菜单
    if("recommend" == type){
        $("#mobile_find_menu_icon").removeClass("new-mobilepage_footer-item_tip-discover_selected");
        $("#mobile_collected_menu_icon").removeClass("new-mobilepage_footer-item_tip-enshrine_selected");
        $("#mobile_recommend_menu_icon").addClass("new-mobilepage_footer-item_tip-Recommend_selected");
        $("#mobile_find_menu_word").css("color","#666");
        $("#mobile_collected_menu_word").css("color","#666");
        $("#mobile_recommend_menu_word").css("color", "#2196f3");
    }else if("find" == type){
    //发现菜单
        $("#mobile_find_menu_icon").addClass("new-mobilepage_footer-item_tip-discover_selected");
        $("#mobile_collected_menu_icon").removeClass("new-mobilepage_footer-item_tip-enshrine_selected");
        $("#mobile_recommend_menu_icon").removeClass("new-mobilepage_footer-item_tip-Recommend_selected");
        $("#mobile_find_menu_word").css("color", "#2196f3");
        $("#mobile_collected_menu_word").css("color","#666");
        $("#mobile_recommend_menu_word").css("color","#666");
    }else{
        //收藏菜单
        $("#mobile_find_menu_icon").removeClass("new-mobilepage_footer-item_tip-discover_selected");
        $("#mobile_collected_menu_icon").addClass("new-mobilepage_footer-item_tip-enshrine_selected");
        $("#mobile_recommend_menu_icon").removeClass("new-mobilepage_footer-item_tip-Recommend_selected");
        $("#mobile_find_menu_word").css("color","#666");
        $("#mobile_collected_menu_word").css("color", "#2196f3");
        $("#mobile_recommend_menu_word").css("color","#666");
    }
//    dealPubMenuClickEvent();
}
//论文推荐
function linkRecommed(){
	window.location.href = "/pub/mobile/pubrecommendmain";	
};
//发现
function linkPubFind(){
    window.location.href = "/pub/find/area";  
}
//论文收藏
function linkCollection(){
	window.location.href = "/pub/collect/main";  
}
</script>
<!-- <div style="width: 100vw; height: 9vh; overflow-x: hidden; position: fixed; bottom: 0px; left: 0px;"> -->
  <div class="new-mobilepage_footer-container mobile_menu" style="z-index: 99;">
    <div class="new-mobilepage_footer-item fc_blue500" id="mobile_recommend_menu" onclick="linkRecommed();">
      <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-Recommend" id="mobile_recommend_menu_icon"></i>
      <span class="new-mobilepage_footer-item_title" id="mobile_recommend_menu_word">推荐</span>
    </div>
    <div class="new-mobilepage_footer-item" style="position: relative;" id="mobile_find_menu" onclick="linkPubFind();">
      <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-discover" id="mobile_find_menu_icon"></i>
      <span class="new-mobilepage_footer-item_title" id="mobile_find_menu_word">发现</span>
    </div>
    <div class="new-mobilepage_footer-item" style="position: relative;" id="mobile_collected_menu"
      onclick="linkCollection();">
      <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-enshrine" id="mobile_collected_menu_icon"></i>
      <span class="new-mobilepage_footer-item_title" id="mobile_collected_menu_word">收藏</span>
    </div>
  </div>
<!-- </div> -->
