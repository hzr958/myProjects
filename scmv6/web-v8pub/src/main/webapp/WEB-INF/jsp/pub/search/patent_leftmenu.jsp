<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    var menuParent = $('.menu > .ListTitlePanel > div');//获取menu下的父层的DIV
    var menuList = $('.menuList');
    var name=document.getElementById("iconQh");
    $('.menu > .menuParent > .ListTitlePanel > .ListTitle').each(function(i) {//获取列表的大标题并遍历
      $(this).click(function(){
        if($(menuList[i]).css('display') == 'none'){
          $(menuList[i]).slideDown(300);
          $(this).find("i").html(" &#xE5CE;");
        }
        else{
          $(menuList[i]).slideUp(300);
           $(this).find("i").html("&#xE5CF;");
        }
      });
    });
    //初始化左菜单
    var language = $("#searchLanguages").val();
    if(language!=""){
		   $("#slanguage").find("em").each(function(){
		    	if($(this).text()=="(0)"){
		    		$(this).parent().parent().hide();
			    }
		    });
		   $("#slanguage").find("i[name='language']").css("visibility","visible").html("&#xe5cd;");
		   $("#slanguage").find("i[name='language']").css("display","block").html("&#xe5cd;");
	    	//添加点击类型返回事件
	    	$("#slanguage").find("a").addClass("menuList-item_selected");
			$("#slanguage").find("a").attr("onclick","page.backSearch('language')");
			//隐藏数据量
			$("#slanguage").find("em").html("");
 }
 /*    if(language!=""&&language=="ZH_CN"){
    	$("#slanguage").find("i[name='ZH_CN']").css("visibility","visible").html("&#xe5cd;");
    	$("#slanguage").find("a[name='EN']").parent().hide();
    	//添加点击语言返回事件
    	$("#slanguage").find("a").attr("onclick","page.backSearch('ZH')");
    }else if(language!=""&&language=="EN"){
    	$("#slanguage").find("i[name='EN']").css("visibility","visible").html("&#xe5cd;");
    	$("#slanguage").find("a[name='ZH']").parent().hide();
    	//添加点击语言返回事件
    	$("#slanguage").find("a").attr("onclick","page.backSearch('EN')");
    } */
	var searchType = $("#searchType").val();
	 if(searchType!=""){
		 $("#stype").find("em").each(function(){
		    	   if($(this).text()=="(0)"){
		    		$(this).parent().parent().hide();
			    }
		    });
	    	$("#stype").find("i[name='type']").css("visibility","visible").html("&#xe5cd;");
	    	$("#stype").find("i[name='type']").css("display","block").html("&#xe5cd;");
	    	//添加点击类型返回事件
	    	$("#stype").find("a").addClass("menuList-item_selected");
			$("#stype").find("a").attr("onclick","page.backSearch('type')");
			//隐藏数据量
			$("#stype").find("em").html("");
	    }
	var searchYear = $("#searchYear").val();
	 if(searchYear!=""){
	    	$("#years").find("i[name='year']").css("visibility","visible").html("&#xe5cd;");
	    	$("#years").find("i[name='year']").css("display","block").html("&#xe5cd;");
	    	//添加点击年份返回事件
	    	$("#years").find("a").addClass("menuList-item_selected");
			$("#years").find("a").attr("onclick","page.backSearch('year')");
			//隐藏年份翻页
			$("#years").find(".menuList_sc").hide();
			//隐藏数据量
			$("#years").find("em").html("");
	    }
	 var orderBy = $("#orderBy").val();
	 if(orderBy=="DEFAULT"){
	    	$("#ssort").find("input[value='DEFAULT']").attr("checked","checked");
	    	
	    }
});
</script>
<div class="l_menu_tit"  style="border-bottom: 1px solid #ddd; width: 95%;">
  <i class="paper_icon"></i>
  <spring:message code='psn.patent.leftmenu.pub' />
</div>
<div class="menu"  style="width: 265px;">
  <%-- <jsp:include page="/WEB-INF/jsp/pub/search/pdwh/sort_leftmenu.jsp"></jsp:include> --%>
  <jsp:include page="/WEB-INF/jsp/pub/search/pubyears_leftmenu.jsp"></jsp:include>
  <jsp:include page="/WEB-INF/jsp/pub/search/patentcategories_leftmenu.jsp"></jsp:include>
  <%-- <jsp:include page="/WEB-INF/jsp/pub/search/pdwh/languages_leftmenu.jsp"></jsp:include> --%>
</div>