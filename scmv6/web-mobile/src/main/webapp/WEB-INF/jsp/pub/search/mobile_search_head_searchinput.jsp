<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type=text/javascript>
	$(function(){
	    var value=$("#searchStringInput").val(); //获取光标最后一个文字的后面获得焦点
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
			if($.trim($("#searchStringInput").val()) == ""){
				return;
			}
			//点击检索按钮，弹出框中的过滤条件要清空为默认的
            $("#searchString").val($.trim($("#searchStringInput").val()));
            $("#orderBy").val("DEFAULT");
            $("#searchPubYear").val("");
            $("#searchPubType").val("");
            $("#pageNo").val("1");
            $("#pubDBIds").val("");
            $("#language").val("ALL");
			//保存到历史记录
    		var his = new History($("#desPsnId").val());
			var keyword=$.trim($("#searchStringInput").val()); 
			var fromPage = $("#fromPage").val();
			var searchType = $("#searchStringInput").attr("searchType");
			var searchUrl = "/pub/paper/search";
			if(searchType == "patent"){
			    searchType = "/pub/patent/search";
			    PdwhSearch.ajaxLoadPatent();
	        }else{
	        	PdwhSearch.ajaxLoadPaper();
	        }
	        his.add(keyword, searchUrl + "?searchString="+keyword+"&fromPage="+fromPage, "");
		 }  
	}
	
	
	function doSearch(){
		if($.trim($("#searchStringInput").val()) == ""){
			return;
		}
		//点击检索按钮，弹出框中的过滤条件要清空为默认的
        //$("#searchString").val(encodeURIComponent($.trim($("#searchStringInput").val())));
        $("#orderBy").val("DEFAULT");
        $("#searchPubYear").val("");
        $("#searchPubType").val("");
        $("#pageNo").val("1");
        $("#pubDBIds").val("");
        $("#language").val("ALL");
		//保存到历史记录
    	var his = new History($("#desPsnId").val());
		var keyword=$.trim($("#searchStringInput").val());
		var fromPage = $("#fromPage").val();
		var searchType = $("#searchStringInput").attr("searchType");
        var searchUrl = "/pub/paper/search";
        if(searchType == "patent"){
            searchUrl = "/pub/patent/search";
            PdwhSearch.ajaxLoadPatent();
        }else{
        	PdwhSearch.ajaxLoadPaper();
        }
        his.add(keyword, searchUrl + "?searchString="+keyword+"&fromPage="+fromPage, "");
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
<div>
 <!--  <div class="paper__func-header">
    <a onclick="javascript:SmateCommon.goBack('/dynweb/mobile/dynshow');"><i
      class="material-icons paper__func-header__tip">keyboard_arrow_left</i></a> <span>全站检索</span> <i
      class="material-icons paper__func-header__tip"></i>
  </div> -->
<div class="m-top m-top_top-background_color">
  <a onclick="goBack();" class="fl">
  <i class="material-icons navigate_before"></i>
  </a> 
  <span class="m-top_top-background_color-title">全站检索</span>
</div>
  <div class="paper__func-tool" style="justify-content: space-between;">
    <div class="paper__func-tool">
      <div class="paper__func-box">
        <form action="javascript:return true">
          <input type="search" placeholder="" class="paper__func-box__inputarea" searchType="paper"
            name="searchStringInput" id="searchStringInput" onkeydown="entersearch();" onfocus="history.showHistory();"
            value="<c:out value="${model.searchString }"/>" style="width: 73vw;" maxlength="100"/>
        </form>
        <i class="paper__func-search" onclick="doSearch();" id="search_icon_i" style="margin-bottom: 8px;"></i>
      </div>
      <div class="message-page__fuctool-right_tip" id="search_filter_icon" style="margin-right: 8px; margin-left: 16px;">
        <i class="material-icons" onclick="select_filter();">filter_list</i>
      </div>
    </div>
  </div>
</div>
<div class="top_clear"></div>
