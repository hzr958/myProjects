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
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/mobile_pub_recommend.js"></script>
<script type="text/javascript">

$(function(){
	conditionsSelect();
    document.getElementsByClassName("provision_container-body")[0].style.height = window.innerheight - 96 + "px"; 
});
//自动选择
function conditionsSelect(){
	var searchArea = $("#searchArea").val();
	var searchPsnKey = $("#searchPsnKey").val();
	var searchPubYear = $("#searchPubYear").val();
	var searchPubType = $("#searchPubType").val();
	
	$(".type_area[value='"+searchArea+"']").addClass("selector__list-target");
    if(searchPubYear==""){
         $(".type_time[value='0']").addClass("selector__list-target");
    }else{
		$(".type_time[value='"+searchPubYear+"']").addClass("selector__list-target");
    }
	
	var selectListPubType = searchPubType.split(',');
    for(var i in selectListPubType){
        var val = selectListPubType[i];
        $(".type_pub[value='"+val+"']").addClass("selector__list-target");
    }

	if(searchPsnKey==""){
		 $(".type_key[value='']").addClass("selector__list-target");
	}else{
		var selectListKey = new Array();
		selectListKey=eval(searchPsnKey);
		for(var i in selectListKey){
			var val = selectListKey[i];
	        $(".type_key[value='"+val+"']").addClass("selector__list-target");
		}	
	}
}
function pubRecommendSubmit(){
	getSelectCondition();
	$("#pub_search").attr("action","/pub/mobile/pubrecommendmain");
	$("#pub_search").submit();
}
/**
 * 获取选择的条件
 */
function getSelectCondition(){
    var area = "";
    var key=[];
    var time="";
    var type="";
    $(".type_area.selector__list-target").each(function(){
    	area = $(this).attr("value");
    });
    $(".type_key.selector__list-target").each(function(){
      if($(this).attr("value")!=""){
    	key.push($(this).attr("value"));     
      }
    });
    $(".type_time.selector__list-target").each(function(){
       time = $(this).attr("value");
    });
    $(".type_pub.selector__list-target").each(function(){
       type += $(this).attr("value")+",";
    });
    var reg = /,$/;
    $("#searchArea").attr("value",area);
    if(key.length>0){
	    $("#searchPsnKey").attr("value",JSON.stringify(key));   	
    }else{
        $("#searchPsnKey").attr("value","");         	
    }
    $("#searchPubYear").attr("value",time);
    $("#searchPubType").attr("value",type.replace(reg,""));
}
function editAreaSubmit(){
	$("#pub_search").attr("action","/pub/mobile/editcoditionsarea");
	$("#pub_search").submit();
}
function editKeySubmit(){
    $("#pub_search").attr("action","/pub/mobile/editcoditionskeyword");
    $("#pub_search").submit();
}
function gobacke(){
    $("#pub_search").attr("action","/pub/mobile/pubrecommendmain");
    $("#pub_search").submit();
}
</script>
</head>
<body>
  <form id="pub_search" method="post" action="/pub/mobile/pubrecommendmain">
    <input type="hidden" id="defultArea" name="defultArea" value="${pubVO.defultArea}" /> <input type="hidden"
      id="defultKeyJson" name="defultKeyJson" value='<c:out value="${pubVO.defultKeyJson}"/>' /> <input type="hidden"
      id="searchArea" name="searchArea" value="${pubVO.searchArea}" /> <input type="hidden" id="searchPsnKey"
      name="searchPsnKey" value='<c:out value="${pubVO.searchPsnKey}"/>' /> <input type="hidden" id="searchPubYear"
      name="searchPubYear" value="${pubVO.searchPubYear}" /> <input type="hidden" id="searchPubType"
      name="searchPubType" value="${pubVO.searchPubType}" />
  </form>
  <%-- <form id="edit_area_form" method="post" action="/pubweb/mobile/editcoditionsarea">
    <input type="hidden" id="defultArea" name="defultArea" value="${defultArea}"/>
</form> --%>
  <div class="provision_container-title">
    <!--  <a href="javascript:gobacke();"> -->
    <i class="material-icons" onclick="gobacke();">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置推荐条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>科技领域</h3></span> <span class="provision_container-body_title-edit  app_psn-main_page-body_item-icon"
        onclick="editAreaSubmit();"></span>
    </div>
    <div class="provision_container-body_item">
      <c:if test="${fn:length(pubVO.areaList)>0 }">
        <c:forEach items="${pubVO.areaList }" var="area">
          <c:if test="${locale == 'en_US' }">
            <div class="provision_container-body_item-list div_item type_area"
              onclick="PubRecommend.changCondition(this)" value="${area.scienceAreaId}">${area.enScienceArea}</div>
          </c:if>
          <c:if test="${locale == 'zh_CN' }">
            <div class="provision_container-body_item-list div_item type_area"
              onclick="PubRecommend.changCondition(this)" value="${area.scienceAreaId}">${area.scienceArea}</div>
          </c:if>
        </c:forEach>
      </c:if>
<!--       <div class="provision_container-body_item-list div_item type_area" onclick="PubRecommend.changCondition(this)"
        value="">不限</div> -->
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>关键词</h3></span> <span class="provision_container-body_title-edit app_psn-main_page-body_item-icon"
        onclick="editKeySubmit();"></span>
    </div>
    <div class="provision_container-body_item">
      <c:if test="${fn:length(pubVO.keyList)>0 }">
        <c:forEach items="${pubVO.keyList }" var="disciplineKey">
          <div class="provision_container-body_item-list div_item type_key" onclick="PubRecommend.changCondition(this)"
            value='${disciplineKey.keyWords}'>${disciplineKey.keyWords}</div>
        </c:forEach>
      </c:if>
<!--       <div class="provision_container-body_item-list div_item type_key" onclick="PubRecommend.changCondition(this)"
        value=''>不限</div> -->
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>出版年份</h3></span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list div_item type_time" onclick="PubRecommend.changCondition(this)"
        value="1">${pubVO.nowYear}年以来</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="PubRecommend.changCondition(this)"
        value="2">${pubVO.nowYear-2}年以来</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="PubRecommend.changCondition(this)"
        value="3">${pubVO.nowYear-4}年以来</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="PubRecommend.changCondition(this)"
        value="0">不限</div>
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>成果类型</h3></span>
    </div>
    <div class="provision_container-body_item">
        <div class="provision_container-body_item-list div_item type_pub" onclick="PubRecommend.changCondition(this)" value="4">期刊论文</div>
        <div class="provision_container-body_item-list div_item type_pub" onclick="PubRecommend.changCondition(this)" value="3">会议论文</div>
        <div class="provision_container-body_item-list div_item type_pub" onclick="PubRecommend.changCondition(this)" value="5">专利</div>
        <div class="provision_container-body_item-list div_item type_pub" onclick="PubRecommend.changCondition(this)" value="7">其他</div>
    </div>
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="pubRecommendSubmit();">确定</div>
</body>
</html>
