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
    $(function(){
    	selectDefultArea();//选中设置的科技领域
	    $(".dev_area").click(function(){//绑定点击事件
	    	var defultArea = $(".new_subject-field_item-detail_selected").length;
	    	if(defultArea>=3 && !$(this).hasClass("new_subject-field_item-detail_selected")){
	    		scmpublictoast('最多选3个科技领域',1000,3);
	    	}else{
                $(this).toggleClass("new_subject-field_item-detail_selected");
	    	}
	    });
    	var containerele = document.getElementsByClassName("new_subject-field_checked-container_detail")[0];
    	containerele.style.height =  window.innerHeight - 96 + "px";
    });
    //选中设置的科技领域
    function selectDefultArea(){
        var defultArea = $("#defultArea").val();
        if(defultArea==""){
        	return;
        }
        var psnArea = defultArea.split(",");
        for(var i=0;i<psnArea.length;i++){
            var areacode = psnArea[i];
            $(".dev_area[value="+areacode+"]").addClass("new_subject-field_item-detail_selected");
        }
    };
    //返回
    function goback(){
    	$("#defultArea").val("");
    	$("#pub_search").submit();
    };
    
    function saveArea(){
        var addArea = getSelectArea();
        if(addArea.length<=0){
          scmpublictoast("至少选择一个科技领域",2500,3);
        } 
    	$.ajax({
    		url: "/pub/mobile/ajaxsavepsnareas",
    		data: {"defultArea" : addArea.join(",")},
    		type: "post",
    		dataType: "json",
    		success: function(data){
    			if(data.result == "success"){
    				goback();
    			}else if(data.result == "error"){
    			  scmpublictoast(data.msg,2500,3);
    			}else{
    			  scmpublictoast("添加科技领域出错",2000,2);
    			}
    		},
    		error: function(data){
    			
    		}
    	});
    };
    function getSelectArea(){
    	var selectArea = [];
    	$(".new_subject-field_item-detail_selected").each(function(){
    		selectArea.push($(this).attr("value"));
    		
    	});
    	return selectArea;
    };
    
    </script>
</head>
<body>
  <form id="pub_search" method="post" action="/pub/mobile/ajaxconditions">
    <input type="hidden" id="defultArea" name="defultArea" value="${pubVO.defultArea}" /> <input type="hidden"
      id="defultKeyJson" name="defultKeyJson" value='<c:out value="${pubVO.defultKeyJson}"/>' /> <input type="hidden"
      id="searchArea" name="searchArea" value="${pubVO.searchArea}" /> <input type="hidden" id="searchPsnKey"
      name="searchPsnKey" value='<c:out value="${pubVO.searchPsnKey}"/>' /> <input type="hidden" id="searchPubYear"
      name="searchPubYear" value="${pubVO.searchPubYear}" /> <input type="hidden" id="searchPubType"
      name="searchPubType" value="${pubVO.searchPubType}" />
  </form>
  <div class="new_subject-field">
    <div class="provision_container-title">
      <i class="material-icons" onclick="goback();">keyboard_arrow_left</i> <span>设置科技领域</span> <i></i>
    </div>
    <div class="new_subject-field_checked-container_body">
      <div class="new_subject-field_checked-container_detail" id="scroller">
        <c:forEach items="${pubVO.allScienceAreaList }" var="itemMap">
          <div class="new_subject-field_item-title" value="${itemMap.first.categoryId}">
            <span>${itemMap.first.categoryZh}</span>
          </div>
          <div class="new_subject-field_item-container">
            <c:forEach items="${itemMap.second }" var="secondArea">
              <div class="new_subject-field_item-detail dev_area" value="${secondArea.categoryId}">${secondArea.categoryZh}</div>
            </c:forEach>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="saveArea();">确定</div>
</body>
</html>