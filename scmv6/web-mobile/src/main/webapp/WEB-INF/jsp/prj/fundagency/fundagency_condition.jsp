<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%><html>
<head>
<title>科研之友</title>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
<script type="text/javascript" src="/resmod/js/jquery.js"></script>
<script type="text/javascript">
       $(function(){
    	   dealWithConditionsStatus();
    	   document.getElementsByClassName("new-financial_body-container")[0].style.height = window.innerHeight - 100 + "px";
       })
       //选中属性
       var className = "new-financial_body-items_check";
       //初始化选中条件
       function dealWithConditionsStatus(){
    	   var regionAgency = $("#regionAgency").val();
    	   if(regionAgency != null && regionAgency != ""){
    		   var regionIds = regionAgency.split(",");
    		   for(var i=0; i<regionIds.length; i++){
    			   var selector = "#region_"+regionIds[i];
    			   var obj = $(selector);
    	            dealSelectStatus(obj);
    	        }
    	    }   
       }
        
      //选中地区情况 
        function selectCondition(obj){
            var regionIds = new Array();
            $(".new-financial_body-items_check").each(function(){
            	regionIds.push($.trim($(this).attr("value")));
            });
            $("#regionAgency").val(regionIds.join(","));
            //将此值保存至前端数据库localStorage,用于分完之后跳转,跳转前移除该值
            //放之前先移除,防止重复
            /* localStorage.removeItem("regionAgency");
            localStorage.setItem("regionAgency",regionIds.join(",")); */
            
        }
       
      //处理选中效果
        function dealSelectStatus(obj){
            var $this = $(obj);
            if($this.hasClass(className)){
                $this.removeClass(className);
            }else{
            	var array = $(".new-financial_body-items_check");
            	if(array.length == 10){
            		//显示提示
            		return;
            	}else{
            		$this.addClass(className);
            	}
            }
            selectCondition(obj);
        }
      
      //确定按钮事件
        function findAgency(){
            $("#select_search").submit();
        }
    </script>
<body>
  <form id="select_search" method="get" action="/prj/mobile/fundagency">
    <input type="hidden" id="searchKey" name="searchKey" value="${searchKey}"> <input type="hidden"
      id="regionAgency" name="regionAgency" value="${regionAgency}"> <input type="hidden" id="flag" name="flag"
      value="condition">
  </form>
  <div class="message-page__header"
    style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between;">
    <i class="material-icons" style="margin-left: 15px; width: 10vw;" onclick="window.history.back();">keyboard_arrow_left</i>
    <span style="width: 80vw; display: flex; justify-content: center; align-items: center;">条件设置</span> <i
      style="width: 10vw; margin-right: 15px"></i>
  </div>
  <div class="new-financial_body-container" style="padding: 0px; margin: 0px; position: fixed; top: 50px; left: 0px; overflow-y: scroll;">
    <c:forEach items="${regionList}" var="region" varStatus="stat">
      <div class="new-financial_body-items" onclick="dealSelectStatus(this)" id="region_${region.id}"
        value="${region.id}">${region.zhName}</div>
    </c:forEach>
  </div>
  <div class="new_subject-field_checked-container_footer" onclick="findAgency();">确定</div>
</body>
</html>