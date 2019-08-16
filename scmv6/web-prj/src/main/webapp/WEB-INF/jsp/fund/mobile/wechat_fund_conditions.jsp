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
<script type="text/javascript">

$(function(){
	conditionsSelect("searchAreaCodes","type_area");
	conditionsSelect("searchAgencyId","type_agency");
	conditionsSelect("searchTimeCodes","type_time");
	conditionsSelect("searchseniority","type_sen");
    document.getElementsByClassName("provision_container-body")[0].style.height = window.innerheight - 96 + "px"; 
});
//自动选择
function conditionsSelect(searchName,selectName){
	var selectStr = $("#"+searchName).val()=="" ? "0" : $("#"+searchName).val();
	var selectList = new Array();;
	var selectList = selectStr.split(',');
	$("."+selectName).each(function(){
		for(var item in selectList){
		      if($(this).attr("value") == selectList[item]){
		    		$(this).addClass("selector__list-target");
		    		$(this).attr('onclick','FundRecommend.delCondition(this,"'+selectName+'")');
		      }
		}
	});
}
function fundRecommendSubmit(){
    getSelectCondition();
    $.ajax({
        url:"/prjweb/fundconditions/ajaxsaveseniority",
        type:"post",
        data:{"seniorityCode" : $("#searchseniority").val()},
        dataType:"json",
        success:function(data){
          $("#pub_search").attr("action","/prjweb/wechat/findfunds");
          $("#pub_search").submit();
        },
        error:function(){
        }
    });
}
/**
 * 获取选择的条件
 */
function getSelectCondition(){
    var area = "";
    var agencyId="";
    var time="";
    var seniority="";
    $(".type_area.selector__list-target").each(function(){
    	area = $(this).attr("value")+",";
    });
    $(".type_agency.selector__list-target").each(function(){
    	agencyId += $(this).attr("value");
    });
    $(".type_time.selector__list-target").each(function(){
        time += $(this).attr("value")+",";
    });
    $(".type_sen.selector__list-target").each(function(){
        seniority = $(this).attr("value");
    });
    var reg = /,$/;
    $("#searchAreaCodes").attr("value",area.replace(reg,""));
    $("#searchAgencyId").attr("value",agencyId == "" ? 0 : agencyId);
    $("#searchTimeCodes").attr("value",time.replace(reg,""));
    $("#searchseniority").attr("value",seniority == "" ? 0 : seniority);
}
function editAreaSubmit(){
	$("#pub_search").attr("action","/prjweb/wechat/editcoditionsarea");
	getSelectCondition();
	$("#pub_search").submit();
}
function editAgencySubmit(){
	$("#pub_search").attr("action","/prjweb/wechat/editagencyinterest");
	getSelectCondition();
	$("#pub_search").submit();
}
</script>
</head>
<body>
  <form id="pub_search" method="get" action="/prjweb/wechat/findfunds">
    <input type="hidden" id="searchAreaCodes" name="searchAreaCodes" value="${searchAreaCodes}" /> <input type="hidden"
      id="searchAgencyId" name="searchAgencyId" value="${searchAgencyId}" /> <input type="hidden" id="searchTimeCodes"
      name="searchTimeCodes" value="${searchTimeCodes}" /> <input type="hidden" id="searchseniority"
      name="searchseniority" value="${searchseniority}" /> <input type="hidden" id="defultArea" name="defultArea"
      value="${defultArea}" /> <input type="hidden" id="defaultAgencyId" name="defaultAgencyId"
      value="${defaultAgencyId}" />
  </form>
  <%-- <form id="edit_area_form" method="post" action="/pubweb/mobile/editcoditionsarea">
    <input type="hidden" id="defultArea" name="defultArea" value="${defultArea}"/>
</form> --%>
  <div class="provision_container-title">
    <!-- <a href="javascript:fundRecommendSubmit();"> -->
    <i class="material-icons" onclick="fundRecommendSubmit();">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置推荐条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">资助机构</span> <span class="provision_container-body_title-edit  app_psn-main_page-body_item-icon"
        onclick="editAgencySubmit();"></span>
    </div>
    <div class="provision_container-body_item">
      <s:if test="fundAgencyInterestList.size()>0">
        <s:iterator value="fundAgencyInterestList" var="item">
          <div class="provision_container-body_item-list div_item type_agency"
            onclick="FundRecommend.addCondition(this,'type_agency')" value='${item.agencyId}'>
            <c:out value="${item.showName}" />
          </div>
        </s:iterator>
      </s:if>
<!--       <div class="provision_container-body_item-list div_item type_agency"
        onclick="FundRecommend.addCondition(this,'type_agency')" value='0'>全部推荐</div> -->
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">单位类型</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list div_item type_sen"
        onclick="FundRecommend.addCondition(this,'type_sen')" value="1">科技企业</div>
      <div class="provision_container-body_item-list div_item type_sen"
        onclick="FundRecommend.addCondition(this,'type_sen')" value="2">科研机构</div>
      <div class="provision_container-body_item-list div_item type_sen"
        onclick="FundRecommend.addCondition(this,'type_sen')" value="0">不限</div>
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">科技领域</span> <span class="provision_container-body_title-edit app_psn-main_page-body_item-icon"
        onclick="editAreaSubmit();"></span>
    </div>
    <div class="provision_container-body_item">
      <s:if test="fundAreaList.size()>0">
        <s:iterator value="fundAreaList" var="area">
          <div class="provision_container-body_item-list div_item type_area"
            onclick="FundRecommend.addCondition(this,'type_area')" value="${area.scienceAreaId}">${area.showName}</div>
        </s:iterator>
      </s:if>
      <!--                     <div class="provision_container-body_item-list div_item type_area" onclick="FundRecommend.addCondition(this,'type_area')" value="0">不限</div>   -->
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">截止日期</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list div_item type_time"
        onclick="FundRecommend.addCondition(this,'type_time')" value="1">一周以内</div>
      <div class="provision_container-body_item-list div_item type_time"
        onclick="FundRecommend.addCondition(this,'type_time')" value="2">一个月以内</div>
      <div class="provision_container-body_item-list div_item type_time"
        onclick="FundRecommend.addCondition(this,'type_time')" value="3">三个月以内</div>
      <div class="provision_container-body_item-list div_item type_time"
        onclick="FundRecommend.addCondition(this,'type_time')" value="0">不限</div>
    </div>
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="fundRecommendSubmit();">确定</div>
</body>
</html>
