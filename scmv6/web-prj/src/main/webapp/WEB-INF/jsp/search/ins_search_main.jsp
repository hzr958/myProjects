<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>机构页面</title>
<link rel="stylesheet" type="text/css"
	href="${resmod }/smate-pc/css/scm-newpagestyle.css">
<script type="text/javascript"
	src="${resmod}/js_v5/searchins/inspg.search.ins.js"></script>
<script type="text/javascript"
	src="${resmod }/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.share.js"></script>
<!-- js脚本方法 -->
<%@ include file="ins_search_script.jsp"%>
<style type="text/css">
* {
	word-break: break-word;
	margin: 0 0;
}

.filter_item_name{
    display: inline-block;
    width: 105px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
<script type="text/javascript">
$(function(){
    if(document.getElementsByClassName("header__nav")){
      if(document.getElementById("num1")){
        document.getElementById("num1").style.display="flex";      
      }
      if(document.getElementsByClassName("header-main__box") && document.getElementById("num2")){
        document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
      }
      if(document.getElementsByClassName("top-main_search-input").length>0){
          console.log("01");
          document.getElementsByClassName("top-main_search-input")[0].style.width = "400px";
          console.log("12");
      }
    }  
})
</script>
</head>
<body>
	<!-- 头部菜单 -->
	<%@ include file="search_menu.jsp"%>
	<div class="mechanism" style="border-left: 0px; border-right: 0px; border-top: 0px; padding: 0px; width: 1200px;">
		<input id="searchString" type="hidden" value="${searchString }" /> 
		<input type="hidden" name="insCharacter" id="selected_ins_Character" value="${insCharacter }" />
		<input type="hidden" name="insRegion" id="selected_ins_region" value="${insRegion }" /> 
		<input type="hidden" name="orderBy" id="orderBy" value="" />
		<input type="hidden" name="searchListComplete" id="searchListComplete" value="0"/>
		<input type="hidden" name="characterMenuComplete" id="characterMenuComplete" value="0"/>
		<input type="hidden" name="regionMenuComplete" id="regionMenuComplete" value="0"/>
		<input type="hidden" name="hasLogin" id="hasLogin" value="${hasLogin }"/>
		<!-- 左边检索 -->
		<div class="module-home__fixed-layer_filter" id="left_filter" style="width: 265px;">
			<%@ include file="ins_search_filter_new.jsp"%>
		</div>

		<!-- 右边列表 -->
		<div class="mechanism-right" style="margin-left: 0px; width: 880px; " id="right_list">
			<%-- <%@ include file="ins_list.jsp" %> --%>
		</div>
	</div>
	<jsp:include page="/common/smate.share.jsp" />
</body>
</html>