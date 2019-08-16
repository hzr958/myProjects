<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type=text/javascript>
	$(function(){ 
	    var value=$("#searchStringInput").val(); //获取光标最后一个文字的后面获得焦点
		//$("#searchStringInput").val("").focus().val(value); 
	    $("#searchStringInput").val("").val(value); 
	    
	    if($("#searchStringInput").val()=="" && !$("#searchStringInput").is(":focus")){
	    	$("#showIcon1").show();
	    	$("#showIcon2").hide();
    	}else{
	    	$("#showIcon1").hide();
	    	$("#showIcon2").show();    		
    	}
	    $("#searchStringInput").focus(function(){
            if($("#searchStringInput").val()!=""){
              $("#showIcon1").hide();
              $("#showIcon2").show();
            }
	    });
	    $('#searchStringInput').bind('input propertychange', function() {  
	      if($("#searchStringInput").val()!=""){
	        $("#showIcon1").hide();
	        $("#showIcon2").show();
	      } else{
	        $("#showIcon1").show();
	        $("#showIcon2").hide();
	      } 
	    });  	    
	    $("#searchStringInput").blur(function(){
          setTimeout(function(){
              if($("#searchStringInput").val()==""){
                  $("#showIcon1").show();
                  $("#showIcon2").hide();
              }else{
                  $("#showIcon1").hide();
                  $("#showIcon2").show();         
              }     
            },100);
	    });
	});

	function clearSearchString(){
		$("#searchStringInput").val("");
	}
	function entersearch(){
		 var event = window.event || arguments.callee.caller.arguments[0];  
		 if (event.keyCode == 13){  
			 search();  
		 }  
	}
	function onclickSearch(){
		search();  
	}
	function goBack(){
      if(location.href.indexOf("/pub/search/main") != -1 && $("#fromPage").val() == "relationmain"){
        location.href="/psnweb/mobile/relationmain";
      }else{
         SmateCommon.goBack('/dynweb/mobile/dynshow');
      } 
   }
	
</script>
<%-- 		<a href="javascript:window.history.back()" class="fl"><i
			class="material-icons navigate_before">&#xe408;</i></a> 
			<input placeholder="输入关键词检索" class="top_input sl_box fl" maxlength="50" name="searchStringInput" onkeydown="entersearch()" onfocus="history.showHistory();" id="searchStringInput" value="<c:out value='${searchString}'/>"/>
			<a href="javascript:clearSearchString();" class="clear"><i class="material-icons clear">clear</i></a>
		<input value="检索" type="button" class="t-ensure-btn" onclick="search()"/> --%>
<div class="m-top m-top_top-background_color">
  <a onclick="goBack();" class="fl"><i class="material-icons navigate_before">&#xe408;</i></a> <span
    class='m-top_top-background_color-title'>全站检索</span>
</div>
<div class="paper__func-tool" style="background: #fff; height: 52px; top: 45px;">
  <div class="paper__func-box" style="align-items: center;">
    <a class="paper__func-search" id="showIcon1" style="margin-top: -10px; display: none; width: 10%;"></a>
    <form action="javascript:onclickSearch();" style="padding: 0px; margin: 0px; width: 90%;">
      <input type="search" class="paper__func-search__flag" name="searchStringInput" id="searchStringInput"
        onkeydown="entersearch()" onfocus="history.showHistory();" value="<c:out value="${searchString }"/>" placeholder="检索论文、专利、人员..."
        value="" style="height: 94%; padding-left: 3px; font-size: 12px; line-height: 24px; padding-top: 3px;" maxlength="100"/>
    </form>
    <a class="paper__func-search" id="showIcon2" style="margin-top: -10px; width: 8%;" onclick="onclickSearch();"></a>
  </div>
</div>
<div class="top_clear"></div>
