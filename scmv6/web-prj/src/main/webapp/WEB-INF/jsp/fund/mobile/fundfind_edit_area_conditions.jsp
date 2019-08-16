<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<script type="text/javascript">
var selectAreas = [];
    $(function(){
    	selectDefultArea();//选中设置的科技领域
	    $(".dev_area").click(function(){//绑定点击事件
	    	var defultArea = $(".new_subject-field_item-detail_selected").length;
            $(this).toggleClass("new_subject-field_item-detail_selected");
	    });
    	var containerele = document.getElementsByClassName("new_subject-field_checked-container_detail")[0];
    	containerele.style.height =  window.innerHeight - 96 + "px";
    });
    //选中设置的科技领域
    function selectDefultArea(){
        var defultArea = $("#searchAreaCodes").val();
        if(defultArea==""){
        	return;
        }
        var psnArea = defultArea.split(",");
        for(var i=0;i<psnArea.length;i++){
          if(psnArea[i].split("|")[2] == "select"){
            var areacode = psnArea[i].split("|")[0];
            $(".dev_area[value="+areacode+"]").addClass("new_subject-field_item-detail_selected");
          }
        }
    };
    //返回
    function goback(){
    	$("#pub_search").submit();
    };
    
    function saveArea(){
      getSelectStatus();
      $("#searchAreaCodes").val(selectAreas.join(","));
      $("#pub_search").submit();
    };
    function getSelectStatus(){
    	$(".new_subject-field_item-detail_selected").each(function(){
    	  var subId = $(this).attr("value");
    	  var subAreaName = $(this).text();
    	  var selectArea = subId + "|" + subAreaName;
    		selectAreas.push(selectArea);
    	});
    };
    
    </script>
</head>
<body>
  <form id="pub_search" method="post" action="/prj/mobile/findfundcondition">
    <input type="hidden" id="searchAreaCodes" name="searchAreaCodes" value="${searchAreaCodes }" />
    <input type="hidden" id="searchKey" name="searchKey" value="${searchKey }" /> 
    <input type="hidden" id="regionCodesSelect" name="searchRegionCodes" value="${searchRegionCodes }" />
    <input type="hidden" id="scienceNamesSelect" name="searchseniority" value="${searchseniority }" /> 
  </form>
  <div class="new_subject-field">
    <div class="provision_container-title">
      <i class="material-icons" onclick="goback();">keyboard_arrow_left</i> <span>设置科技领域</span> <i></i>
    </div>
    <div class="new_subject-field_checked-container_body">
      <div class="new_subject-field_checked-container_detail" id="scroller">
        <s:iterator value="allScienceAreaList" var="itemMap">
          <div class="new_subject-field_item-title" id="${itemMap.first.categoryId}" value="${itemMap.first.categoryZh}">
            <span>${itemMap.first.categoryZh}</span>
          </div>
          <div class="new_subject-field_item-container">
            <s:iterator value="#itemMap.second" var="secondArea">
              <div class="new_subject-field_item-detail dev_area" value="${secondArea.categoryId}">${secondArea.categoryZh}</div>
            </s:iterator>
          </div>
        </s:iterator>
      </div>
    </div>
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="saveArea();">确定</div>
</body>
</html>