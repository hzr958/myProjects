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
<script type="text/javascript">

$(function(){
	conditionsSelect();
    document.getElementsByClassName("provision_container-body")[0].style.height = window.innerheight - 96 + "px"; 
    bindSelect();//绑定事件
});
//自动选择
function conditionsSelect(){
	var orderBy = $("#orderBy").val();
	var fundingYear = $("#fundingYear").val();
	var agencyNames = $("#agencyNames").val();
	if(orderBy != ""){
		$(".orderBy[value='"+orderBy+"']").addClass("selector__list-target");
	}
    if(fundingYear != ""){
	    $(".fundingYear[value='"+fundingYear+"']").addClass("selector__list-target");
    }
    if(agencyNames != ""){
        var agencies = agencyNames.split(",");
        for(var i=0;i<agencies.length;i++){
            $(".agencyNames[value='"+agencies[i]+"']").addClass("selector__list-target");
        }
    }
};
function bindSelect(){
	$(".provision_container-body_item-list").click(function(){
		if(!$(this).hasClass("selector__list-target") && $(this).hasClass("orderBy")){
			$(".orderBy").removeClass("selector__list-target");
		}
        if(!$(this).hasClass("selector__list-target") && $(this).hasClass("fundingYear")){
            $(".fundingYear").removeClass("selector__list-target");
        }
        $(this).toggleClass("selector__list-target");

	});
};
function prjSubmit(){
	getSelectCondition();
	$("#pub_search").submit();
};
/**
 * 获取选择的条件
 */
function getSelectCondition(){
    var orderBy = $(".orderBy[class*='selector__list-target']").attr("value");
    var fundingYear = $(".fundingYear[class*='selector__list-target']").attr("value");
    var agencyNames = getValue($(".agencyNames[class*='selector__list-target']"));
    $("#orderBy").val(orderBy);
    $("#fundingYear").val(fundingYear);
    $("#agencyNames").val(agencyNames);
};
function getValue(obj){
	var value=[];
	if(obj){
		obj.each(function(){
			value.push($(this).attr("value"));
		});
	}
	return value.join();
}
</script>
</head>
<body>
  <form id="pub_search" <s:if test="hasLogin == 0">action="/prjweb/outside/mobileotherprjs"</s:if>
    <s:else>action="/prjweb/wechat/prjmain"</s:else> method="get">
    <input id="agencyNames" name="agencyNames" type="hidden" value="${agencyNames }" /> 
    <input id="fundingYear" name="fundingYear" type="hidden" value="${fundingYear }" /> 
    <%-- <input id="searchKey" name="searchKey" type="hidden" value="${ searchKey}" />  --%>
    <input id="orderBy" name="orderBy" type="hidden" value="${orderBy }" /> 
    <input id="hasLogin" name="hasLogin" type="hidden" value="${hasLogin }" /> 
    <input id="des3PsnId" name="des3PsnId" type="hidden" value="${des3PsnId }" /> 
  </form>

  <div class="provision_container-title">
    <i class="material-icons" onclick="window.history.back();">keyboard_arrow_left</i>
    <span>设置条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">排序</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list div_item orderBy" value="updateDate">最新修改</div>
      <div class="provision_container-body_item-list div_item orderBy" value="publishYear">项目年度</div>
    </div>

    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">资助机构</span>
    </div>
    <div class="provision_container-body_item">
      <c:forEach items="${agencyNameList}" var="agency">
            <div class="provision_container-body_item-list div_item agencyNames" value="${agency.agencyName }">${agency.agencyName }</div>      
      </c:forEach>
      <div class="provision_container-body_item-list div_item agencyNames" value="其他">其他</div>
    </div>
    
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">项目年度</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list fundingYear" value='${currentYear }'>近一年</div>
      <div class="provision_container-body_item-list fundingYear" value='${currentYear-2 }'>近三年</div>
      <div class="provision_container-body_item-list fundingYear" value='${currentYear-4 }'>近五年</div>
      <div class="provision_container-body_item-list fundingYear" value='0'>不限</div>     
    </div>
    
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="prjSubmit();">确定</div>
</body>
</html>
