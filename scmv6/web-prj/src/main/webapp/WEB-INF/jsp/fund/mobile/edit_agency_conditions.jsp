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
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css">
<script type="text/javascript">
    window.onload = function(){
        var keylist = document.getElementsByClassName("new_edit-financial_Optional-item_title");
        var checklist = document.getElementsByClassName("new_edit-financial_Optional-list");
        var closelist = document.getElementsByClassName("new_edit-financial_attention-close");
        var closelist = document.getElementsByClassName("new_edit-financial_Optional-container");
        for(var i = 0;i < closelist.length; i++){
            if(closelist[i].innerHTML == ""){
                closelist[i].closest(".new_edit-financial_Optional-item").removeChild(closelist[i]);
            }
        }
        for(var i = 0; i < keylist.length; i++){
            keylist[i].onclick = function(){
            	var onkey = this.querySelector(".new_edit-financial_Optional-item_onkey");
                if(onkey.innerHTML == "expand_more"){
                	onkey.innerHTML = "expand_less";
                	if($(this).closest(".new_edit-financial_Optional-item").find(".new_edit-financial_Optional-container").length>0){
	                	this.closest(".new_edit-financial_Optional-item").querySelector(".new_edit-financial_Optional-container").style.display = "flex";
                	}
                }else{
                	onkey.innerHTML = "expand_more";
                    if($(this).closest(".new_edit-financial_Optional-item").find(".new_edit-financial_Optional-container").length>0){
                        this.closest(".new_edit-financial_Optional-item").querySelector(".new_edit-financial_Optional-container").style.display = "none";                
                    }
                }
            }
        }
        
        for(var i = 0; i < checklist.length; i++ ){
            checklist[i].onclick = function(){//选中和取消
                if(this.classList.contains("new_edit-financial_Optional-checked")){
                    var selfthis = this;
                    this.classList.remove("new_edit-financial_Optional-checked");
                    repeatcount(selfthis);
                    var agencyId = $(this).attr("code");
                    deleteAgency(agencyId);
                }else{
                	if(addFundAgence(this)){
	                    var selfthis = this;
	                    this.classList.add("new_edit-financial_Optional-checked");
	                    repeatcount(selfthis);               		
                	}
                }
            }
        }
    }

    function repeatcount(obj){
        var inner = obj.closest(".new_edit-financial_Optional-container").getElementsByClassName("new_edit-financial_Optional-checked").length;
        if(inner == "0"){
            obj.closest(".new_edit-financial_Optional-item").querySelector(".new_edit-financial_Optional-item_cont").innerHTML = "";
        }else{
            obj.closest(".new_edit-financial_Optional-item").querySelector(".new_edit-financial_Optional-item_cont").innerHTML = "(" +  inner +")";
        }
    }
    //返回
    function goback(){
        $("#defultArea").val("");
        $("#pub_search").submit();
    };
    function addFundAgence(obj){
    	if($(".dev_my_agency").length<10){
	        var name = $(obj).text();
	        var agencyId = $(obj).attr("code");
	        var item = "<div class='new_edit-financial_attention-item dev_my_agency' value='"+agencyId+"'>"+ "<span>"+name+"</span>"
	                   +"<i class='new_edit-financial_attention-close' onclick='deleteAgency("+agencyId+")'></i>"
	                   +"</div>"
	        $(item).appendTo($(".dev_my_agency_all"));	
	        return true;
    	}else{
    		 scmpublictoast("最多添加10个关注的资助机构",2000,3);
    		return false;
    	}
    };
    function deleteAgency(agencyId){
    	if(agencyId){
	        var selfthis = $(".dev_agency_item[code='"+agencyId+"']");
	        $(selfthis).removeClass("new_edit-financial_Optional-checked");
	        repeatcount($(selfthis)[0]);  
	        $(".dev_my_agency[value='"+agencyId+"']").remove();
    	}
    };
    function saveAgency(){
    	if($(".dev_my_agency").length==0){
            scmpublictoast("最少添加1个关注的资助机构",2000,3);
    		return;
    	}
    	if($(".dev_my_agency").length>10){
        scmpublictoast("最多添加10个关注的资助机构",2000,3);
        return;
        }
    	var code = [];
    	$(".dev_my_agency").each(function(){
    		code.push($(this).attr("value"));
    	});
    	var saveAgencyIds = code.join(",");
    	$.ajax({
            url: "/prjweb/wechat/ajaxsavepsnagency",
            data: {"saveAgencyIds" : saveAgencyIds},
            type: "post",
            dataType: "json",
            success: function(data){
                if(data.result == "success"){
                	if(saveAgencyIds.indexOf($("#searchAgencyId").val())<=-1){//选中的的被删掉
                		$("#searchAgencyId").val("");
                	}
                    goback();
                }else{
                      scmpublictoast("添加科技领域出错",2000,2);
                }
            },
            error: function(data){
                
            }
        });
    }
    </script>
</head>
<body>
  <form id="pub_search" method="post" action="/prjweb/wechat/findfundcondition">
    <input type="hidden" id="searchAreaCodes" name="searchAreaCodes" value="${searchAreaCodes}" /> <input type="hidden"
      id="searchAgencyId" name="searchAgencyId" value="${searchAgencyId}" /> <input type="hidden" id="searchTimeCodes"
      name="searchTimeCodes" value="${searchTimeCodes}" /> <input type="hidden" id="searchseniority"
      name="searchseniority" value="${searchseniority}" /> <input type="hidden" id="defultArea" name="defultArea"
      value="${defultArea}" />
  </form>
  <div class="message-page__header"
    style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between;">
    <i onclick="goback();" class="material-icons" style="margin-left: 15px; width: 10vw;">keyboard_arrow_left</i> <span
      style="width: 80vw; display: flex; justify-content: center; align-items: center;">编辑资助机构</span> <i
      style="width: 10vw; margin-right: 15px"></i>
  </div>
  <div class="new_edit-financial_body" style="margin: 56px 0px;">
    <div class="new_edit-financial_attention">
      <div class="new_edit-financial_attention-title">我关注的资助机构</div>
      <div class="new_edit-financial_attention-box dev_my_agency_all">
        <s:if test="fundAgencyInterestList.size()>0">
          <s:iterator value="fundAgencyInterestList" var="item">
            <div class="new_edit-financial_attention-item dev_my_agency" value='${item.agencyId}'>
              <span><c:out value="${item.showName}" /></span> <i class="new_edit-financial_attention-close"
                onclick="deleteAgency('${item.agencyId}')"></i>
            </div>
          </s:iterator>
        </s:if>
      </div>
    </div>
    <div class="new_edit-financial_Optional">
      <div class="new_edit-financial_Optional-title">
        <div class="new_edit-financial_Optional-title_content">待关注的资助机构</div>
        <span class="new_edit-financial_Optional-title_func">(点击添加)</span>
      </div>
      <c:forEach items="${fundAgencyMapList }" var="firstLevel" varStatus="firstStatus">
        <c:set var="subKey" value="${firstLevel.regionId}"></c:set>
        <div class="new_edit-financial_Optional-item">
          <div class="new_edit-financial_Optional-item_title" code="${firstLevel.regionId }">
            <div class="new_edit-financial_Optional-item_name">
              <span>${firstLevel.regionName }</span> <span class="new_edit-financial_Optional-item_cont"> <c:if
                  test="${firstLevel.selectCount>0 }">(${firstLevel.selectCount})</c:if>
              </span>
            </div>
            <i class="material-icons new_edit-financial_Optional-item_onkey">expand_more</i>
          </div>
          <c:if test="${fn:length(firstLevel.agencyList) > 0}">
            <div class="new_edit-financial_Optional-container">
              <c:forEach items="${firstLevel.agencyList }" var="subLevel">
                <div
                  class="new_edit-financial_Optional-list dev_agency_item <c:if test="${subLevel.selectStatus==1 }">new_edit-financial_Optional-checked</c:if>"
                  code="${subLevel.id }">${subLevel.nameView }</div>
              </c:forEach>
            </div>
          </c:if>
        </div>
      </c:forEach>
       <div class="new_edit-financial_Optional-item"></div>
    </div>
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="saveAgency()">确定</div>
</body>
</html>