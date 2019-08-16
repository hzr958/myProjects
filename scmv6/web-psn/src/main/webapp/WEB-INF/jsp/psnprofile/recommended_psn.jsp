<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$.ajax({
        url : '/psnweb/homepage/ajaxrecommendpsn',
        type : 'post',
        data:{"isAll":0,"des3PsnId": $("#des3PsnId").val()},
        dataType : 'html',
        success : function(data) {
            /*$('#recommend_fund_list').html(data);
            if($("#recommend_fund_list").find(".main-list__item").length==0){
                $("#recommend_fund_list").html("<div class='response_no-result'>"+homepage.noRecord+"</div>");
            }*/
        	$('#recommend_psn_list').html(data);
        },
        error: function(){
            
        }
    });
	
/*     document.onkeydown = function(event){
        if(event.keyCode == 27){
            event.stopPropagation();
            event.preventDefault();
            var target = document.getElementsByClassName("list-results_close");
            for(var i = 0; i < target.length; i++){
                Resume.hideRecommendPsnMore(target[i]);
            }
           
        }
    } */
});
</script>
<div id="recommendFundModuleDiv">
  <div class="container__card">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="homepage.profile.recommended" />
        </div>
        <button class="button_main button_link" onclick="Resume.showRecommendPsnMore(this)">
          <s:text name="homepage.profile.recommendedMore" />
        </button>
      </div>
      <div class="main-list">
        <div class="main-list__list item_no-border" id="recommend_psn_list">
          <!-- 可能认识的人列表 -->
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__box" style="width: 720px;" dialog-id="recommend_psn_more">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="homepage.profile.recommended" />
      </div>
      <i class="list-results_close" onclick="Resume.hideRecommendPsnMore(this);"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" list-main="recommendfriend"></div>
  </div>
</div>
