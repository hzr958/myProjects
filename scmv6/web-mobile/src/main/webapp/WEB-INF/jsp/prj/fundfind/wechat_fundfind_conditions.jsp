<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend_${locale}.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_find.js"></script>
<script type="text/javascript">

$(function(){
    initArea();
	conditionsSelect("searchseniority","type_sen");
    document.getElementsByClassName("provision_container-body")[0].style.height = window.innerheight - 96 + "px"; 
});
//初始化科技领域
function initArea(){
  var searchAreaCodes = $("#searchAreaCodes").val();
  if(searchAreaCodes == '' || searchAreaCodes == null){
    return;
  }
  var areaContent = "";
  var AreaCodes = searchAreaCodes.split(",");
  var scienceCodesSelect = $("#scienceCodesSelect").val().split(",");
  //初始化远中的科技领域
  for(var i=0;i<AreaCodes.length;i++){
    var areaStr = AreaCodes[i].split("|");
    if(scienceCodesSelect != null && scienceCodesSelect != "" && scienceCodesSelect.length > 0){
      areaContent += "<div class='provision_container-body_item-list div_item type_area";
      var selectStatus = "' onclick=FundFind.addCondition(this,'type_area')";
      $.each(scienceCodesSelect,function(index,value){
        if(areaStr[0] == value){
          selectStatus = " selector__list-target'"+" onclick=FundFind.delCondition(this,'type_area')";
          return false;
        }
      })
      areaContent += selectStatus;
    areaContent += " id="+areaStr[0]+" value="+areaStr[1]+">"+areaStr[1]+"</div>";
    }else{
      if(areaStr[2] == 'none'){
        areaContent += "<div class='provision_container-body_item-list div_item type_area'" + " onclick=FundFind.addCondition(this,'type_area')" 
        +" id="+areaStr[0]+" value="+areaStr[1]+">"+areaStr[1]+"</div>";
      }else{
        areaContent += "<div class='provision_container-body_item-list div_item type_area selector__list-target'" + " onclick=FundFind.delCondition(this,'type_area')" 
        +" id="+areaStr[0]+" value="+areaStr[1]+">"+areaStr[1]+"</div>";
      }
    }
     
  }
  $(".arealist").html(areaContent);
}
//初始化单位性质
function conditionsSelect(searchName,selectName){
	var selectStr = $("#"+searchName).val();
	var selectList = new Array();;
	var selectList = selectStr.split(',');
	$("."+selectName).each(function(){
		for(var item in selectList){
		      if($(this).attr("value") == selectList[item]){
		    		$(this).addClass("selector__list-target");
		    		$(this).attr('onclick','FundFind.delCondition(this,"'+selectName+'")');
		      }
		}
	});
}
function fundFindSubmit(){
	getSelectCondition();
	$("#pub_search").attr("action","/prj/mobile/fundfindmain");
	$("#pub_search").submit();
}

function fundFindgoback(){
$("#pub_search").attr("action","/prj/mobile/fundfindmain");
$("#pub_search").submit();
}

/**
 * 获取选择的条件
 */
function getSelectCondition(){
    var areas = [];
    var seniority= "";
    $(".type_area.selector__list-target").each(function(){
      areas.push($(this).attr("id"));
    });
    seniority = $(".type_sen.selector__list-target").attr("value");
    $("#scienceCodesSelect").attr("value",areas.join(","));
    $("#searchseniority").attr("value",seniority == "" ? 0 : seniority);
}
function editAreaSubmit(){
	$("#pub_search").attr("action","/prjweb/wechat/fundfind/editsarea");
    var area = "";
	 $(".type_area").each(function(){
	   area += $(this).attr("id")+"|"+$(this).attr("value");
	   var flag = "|none";
	   if($(this).hasClass("selector__list-target")){
	     var flag = "|select";
	   }
	   area += flag + ","
     });
	var reg = /,$/;
	$("#searchAreaCodes").attr("value",area.replace(reg,""));
	$("#pub_search").submit();
}
</script>
</head>
<body>
  <form id="pub_search" action="/prj/mobile/fundfindmain" method="post" id="select_filter_action">
    <input id="searchAreaCodes" type="hidden" name="searchAreaCodes" value="${searchAreaCodes }" /> 
    <input id="searchKey" type="hidden" name="searchKey" value="${searchKey }" /> 
    <input id="regionCodesSelect" type="hidden" name="SearchRegionCodes" value="${regionCodesSelect }" />
    <input id="searchseniority" type="hidden" name="searchseniority" value="${seniorityCodeSelect }" />
    <input id="scienceCodesSelect" type="hidden" name="scienceCodesSelect" value="${scienceCodesSelect }" />
    <input type="hidden" id="flag" name="flag" value="0">
  </form>
  <div class="provision_container-title">
    <i class="material-icons" onclick="fundFindgoback();">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置发现条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">科研领域</span> 
        <span class="provision_container-body_title-edit  app_psn-main_page-body_item-icon" onclick="editAreaSubmit();"></span>
    </div>
    <div class="provision_container-body_item arealist">
         
    </div>
     <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">单位类型</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list div_item type_sen"
        onclick="FundFind.addCondition(this,'type_sen')" value="1">科技企业</div>
      <div class="provision_container-body_item-list div_item type_sen"
        onclick="FundFind.addCondition(this,'type_sen')" value="2">科研机构</div>
      <div class="provision_container-body_item-list div_item type_sen"
        onclick="FundFind.addCondition(this,'type_sen')" value="0">不限</div>
    </div>
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="fundFindSubmit();">确定</div>
</body>
</html>
