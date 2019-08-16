<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

//菜单选中事件
function selectRecommendFundMenu(type){
    //推荐菜单
    if("recommend" == type){
        $("#mobile_agency_menu_icon").removeClass("new-mobilepage_footer-item_tip-financial_selected");
        $("#mobile_collected_menu_icon").removeClass("new-mobilepage_footer-item_tip-enshrine_selected");
        $("#mobile_find_menu_word").removeClass("new-mobilepage_footer-item_tip-discover_selected");
        $("#mobile_recommend_menu_icon").addClass("new-mobilepage_footer-item_tip-Recommend_selected");
        $("#mobile_agency_menu_word").css("color","#666");
        $("#mobile_collected_menu_word").css("color","#666");
        $("#mobile_find_menu_word").css("color","#666");
        $("#mobile_recommend_menu_word").css("color", "#2196f3");
    }else if("agency" == type){
    //资助机构
        $("#mobile_collected_menu_icon").removeClass("new-mobilepage_footer-item_tip-enshrine_selected");
        $("#mobile_recommend_menu_icon").removeClass("new-mobilepage_footer-item_tip-Recommend_selected");
        $("#mobile_find_menu_word").removeClass("new-mobilepage_footer-item_tip-discover_selected");
        $("#mobile_agency_menu_icon").addClass("new-mobilepage_footer-item_tip-financial_selected");
        $("#mobile_collected_menu_icon").css("color", "#666");
        $("#mobile_find_menu_word").css("color", "#666");
        $("#mobile_recommend_menu_word").css("color","#666");
        $("#mobile_agency_menu_word").css("color", "#2196f3");
    }else if("fundFind" == type){
      //基金发现
      $("#mobile_collected_menu_icon").removeClass("new-mobilepage_footer-item_tip-enshrine_selected");
      $("#mobile_recommend_menu_icon").removeClass("new-mobilepage_footer-item_tip-Recommend_selected");
      $("#mobile_agency_menu_icon").removeClass("new-mobilepage_footer-item_tip-financial_selected");
      $("#mobile_find_menu_icon").addClass("new-mobilepage_footer-item_tip-discover_selected");
      $("#mobile_agency_menu_icon").css("color","#666");
      $("#mobile_collected_menu_word").css("color","#666");
      $("#mobile_recommend_menu_word").css("color","#666");
      $("#mobile_find_menu_word").css("color", "#2196f3");
    }else{
      //基金收藏
        $("#mobile_agency_menu_icon").removeClass("new-mobilepage_footer-item_tip-financial_selected");
        $("#mobile_find_menu_word").removeClass("new-mobilepage_footer-item_tip-discover_selected");
        $("#mobile_recommend_menu_icon").removeClass("new-mobilepage_footer-item_tip-Recommend_selected");
        $("#mobile_collected_menu_icon").addClass("new-mobilepage_footer-item_tip-enshrine_selected");
        $("#mobile_agency_menu_word").css("color", "#666");
        $("#mobile_find_menu_word").css("color", "#666");
        $("#mobile_recommend_menu_word").css("color","#666");
        $("#mobile_collected_menu_word").css("color","#2196f3");
    }
}
//基金推荐
function linkRecommed(){
	window.location.href = "/prjweb/wechat/findfunds";	
};
//收藏的基金
function linkCollection(){
	window.location.href = "/prjweb/wechat/myfunds";  
}

//基金发现
function linkFind(){
  window.location.href = "/prj/mobile/fundfindregion";
}

//资助机构
function linkfundagnecy(){
	window.location.href = "/prj/mobile/fundagency";  
}
</script>
<!-- <div style="width: 100vw; height: 9vh; overflow-x: hidden; position: fixed; bottom: 0px; left: 0px;"> -->
  <div class="new-mobilepage_footer-container mobile_menu" style="z-index: 99;">
    <div class="new-mobilefund_footer-item" onclick="linkRecommed()" style=" width: 25vw;">
      <i id="mobile_recommend_menu_icon"
        class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-Recommend new-mobilepage_footer-item_tip-Recommend_selected"></i>
      <div class="new-mobilepage_footer-item_title" id="mobile_recommend_menu_word" style="color: #666;">推荐</div>
    </div>
    
    <div class="new-mobilepage_footer-item" style="position: relative; width: 25vw;" onclick="linkCollection();">
      <i id="mobile_collected_menu_icon" class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-enshrine"></i>
      <span class="new-mobilepage_footer-item_title" id="mobile_collected_menu_word">收藏</span>
    </div>
    
    <div class="new-mobilepage_footer-item" style="position: relative;  width: 25vw;" onclick="linkFind();">
      <i id="mobile_find_menu_icon" class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-discover"></i>
      <span class="new-mobilepage_footer-item_title" id="mobile_find_menu_word">发现</span>
    </div>
    
    <div class="new-mobilefund_footer-item" style="position: relative;  width: 25vw;" onclick="linkfundagnecy();">
      <i id="mobile_agency_menu_icon" class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-financial"></i>
      <div class="new-mobilepage_footer-item_title" id="mobile_agency_menu_word">资助机构</div>
    </div>
  </div>
<!-- </div> -->
