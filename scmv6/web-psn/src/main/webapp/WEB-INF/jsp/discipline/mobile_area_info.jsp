<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var totalSelected = parseInt($("#totalSelected").val());
$(function(){
  scienceAreaOnclick();
  //默认点击第一个一级领域
  $(".new-edit_technology-items:first").click();
 initSelectedArea();

});

//点击事件
function scienceAreaOnclick(){
    //一级领域点击事件
    $(".new-edit_technology-items").click(function(){
        var $this = $(this);
        $(".new-edit_technology-items.new-edit_technology-items_select").removeClass("new-edit_technology-items_select");
        $this.addClass("new-edit_technology-items_select");
        var selector = ".new-edit_technology-subitems[parentcategoryid='"+$this.attr("id")+"']";
        $(".new-edit_technology-subitems").hide();
        $(selector).show();
        $("#selected_parent_area").html($this.attr("areaName"));
    });
    //二级领域点击事件
    $(".new-edit_technology-subitems").click(function(){
        var $this = $(this);
        var parentAreaId = $this.attr("parentCategoryId");
        var $parentArea = $("#"+parentAreaId);
        var $parentChildCount = $parentArea.find("span.selected_count");
        var sumSelector = "#select_sum_" + parentAreaId;
        var totalSelected = parseInt($("#totalSelected").val());
        var currentSelectedSum = parseInt($(sumSelector).val());
        if($this.hasClass("new-edit_technology-items_subselect")){
            //去掉选中标志
            $this.removeClass("new-edit_technology-items_subselect");
            //重新计数
            currentSelectedSum--;
            if(currentSelectedSum > 0){
                $parentChildCount.html("(" + currentSelectedSum + ")");
            }else{
                $parentChildCount.html("");
            }
            --totalSelected;
        }else{
            if(totalSelected == 5){
              newMobileTip("最多选择5个科技领域");
            }else{
                //添加选中标志
                $this.addClass("new-edit_technology-items_subselect");
                currentSelectedSum++
                //重新计数
                $parentChildCount.html("(" + currentSelectedSum + ")");
                ++totalSelected;
            }
        }
        $(sumSelector).val(currentSelectedSum);
        $("#totalSelected").val(totalSelected);
    });
}

function initSelectedArea(){
  $(".new-edit_technology-items_subselect").each(function(){
      var $this = $(this);
      var parentAreaId = parseInt($this.attr("parentCategoryId"));
      var parentAreaInp = $("#select_sum_"+parentAreaId);
      var currentSelectedSum = parseInt(parentAreaInp.val()) + 1;
      var parentArea = $("#"+parentAreaId);
      var parentCountShow = parentArea.find("span.selected_count");
      parentAreaInp.val(currentSelectedSum);
      parentCountShow.text("(" + currentSelectedSum + ")");
      ++totalSelected;
  });
  $("#totalSelected").val(totalSelected);
}

</script>
<div class="mobile_header">
  <s:if test="isHomepageEdit == 1">
    <i class="material-icons" onclick="window.history.go(-1);" style="font-size: 30px;">keyboard_arrow_left</i>
  </s:if>
  <s:else>
    <i class="material-icons" style="font-size: 30px;"></i>
  </s:else>
  <span class="mobile_header_title">编辑科技领域</span> <i class="material-icons"></i>
</div>
<input type="hidden" id="scmDomain" value="${domain }">
<input type="hidden" id="totalSelected" value="0">

<div class="new-edit_technology-container">
        <div class="new-edit_technology-title">
            <span class="new-edit_technology-container_detail">
                完善科技领域：请选择关注的科技领域
            </span>
        </div>
        
<div class="box new-edit_technology-itembox">
    <s:iterator value="firstLevel" id="first" status="Stat">
      <div class="new-edit_technology-items" showdata-id="edit_technology1" id="${categoryId }" areaName="${showCategory }">
      <span>${showCategory }</span>
        <span class="new-edit_technology-items_count selected_count"></span>
        <input type="hidden" class="hidden_data" id="select_sum_${categoryId}" value="0">
      </div>
    </s:iterator>
</div>


<div class="new-edit_technology-subcontainer" showdata-subid="edit_technology1">
            <div class="new-edit_technology-title new-edit_technology-container_subtitle">
                <span class="new-edit_technology-container_detail new-edit_technology-container_subdetail" id="selected_parent_area"></span>
            </div>
    <div class="new-edit_technology-itembox">
      <s:iterator value="secondLevel" id="second" status="Stat">
        <s:if test="added">
            <div class="new-edit_technology-subitems new-edit_technology-items_subselect" id="${categoryId }" parentCategoryId="${superCategoryId }" style="display: none;">${showCategory }</div>
        </s:if> <s:else>
          <div class="new-edit_technology-subitems" id="${categoryId }" parentCategoryId="${superCategoryId }" style="display: none;">${showCategory }</div>
        </s:else></li>
      </s:iterator> 
    </div>
</div>