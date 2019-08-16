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
	      }else{
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
			if($.trim($("#searchStringInput").val()) == ""){
				return;
			}
			//保存到历史记录
    		var his = new History($("#desPsnId").val());
			var keyword=$.trim($("#searchStringInput").val());       
	        his.add(keyword, "/pub/paper/search?searchString="+keyword+"&fromPage="+$("#fromPage").val(), "");
			//点击检索按钮，弹出框中的过滤条件要清空为默认的
			$("#searchString").val(keyword);
			$("#orderBy").val("DEFAULT");
			$("#patYear").val("");
			$("#patTypeId").val("");
			$("#pageNo").val("");
			$("#language").val("ALL");
			$("#firstLoad").val("0");
			$("#pubsForm").submit();
		 }  
	}
	function doSearch(){
		if($.trim($("#searchStringInput").val()) == ""){
			return;
		}
		//保存到历史记录
    	var his = new History($("#desPsnId").val());
		var keyword=$.trim($("#searchStringInput").val()); 
		var fromPage = $("#fromPage").val();
        his.add(keyword, "/pub/paper/search?searchString="+keyword+"&fromPage="+fromPage, "");
        
		//点击检索按钮，弹出框中的过滤条件要清空为默认的
		$("#searchString").val(keyword);
		$("#orderBy").val("DEFAULT");
		$("#patYear").val("");
		$("#patTypeId").val("");
		$("#pageNo").val("");
		$("#language").val("ALL");
		$("#firstLoad").val("0");
		$("#pubsForm").submit();
	}
	function goBack(){
      if(location.href.indexOf("/pub/search/main") != -1 && $("#fromPage").val() == "relationmain"){
        location.href="/psnweb/mobile/relationmain";
      }else{
         SmateCommon.goBack('/dynweb/mobile/dynshow');
      } 
    }
</script>
<input id="desPsnId" type="hidden" value="${desPsnId }" />
<div class="m-top m-top_top-background_color">
  <a onclick="goBack();" class="fl" style="width: 46px;"><i
    class="material-icons navigate_before">&#xe408;</i></a> <span class="m-top_top-background_color-title"> 全站检索 </span>
</div>
<div class="paper__func-tool" style="background: #fff; height: 52px; top: 45px;">
  <div class="paper__func-box" style="align-items: center;">
    <a class="paper__func-search" id="showIcon1" style="height: 32px; display: none; width: 10%;" onclick="doSearch();"></a>
    <form action="javascript:doSearch();" style="padding: 0px; margin: 0px; width: 90%;">
      <input type="search" class="paper__func-search__flag" name="searchStringInput" id="searchStringInput"
        onkeydown="entersearch();" onfocus="history.showHistory();" value="${searchString }" placeholder="检索论文、专利、人员..."
        value="" style="line-height: 24px; padding-left: 3px; font-size: 12px; padding-top: 3px; width: 100%;" />
    </form>
    <a class="paper__func-search" id="showIcon2" style="height: 32px; width: 10%;" onclick="doSearch();"></a>
  </div>
</div>
<div class="top_clear"></div>
