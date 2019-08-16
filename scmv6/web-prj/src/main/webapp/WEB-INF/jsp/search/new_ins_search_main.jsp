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
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js_v5/searchins/inspg.search.ins_${locale}.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.qrcode.min.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
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

.pd0{
padding-left:0px;
}

</style>
<script type="text/javascript">
window.onload = function() {
    page.submit(1);
    resetSelectedMenu();
    $("#search_some_one_form").attr("action","/prjweb/outside/agency/searchins");
}

var page = page ? page : {};

page.submit = function(p){
    var orderBy = $("#orderBy").val();
    var insCharacter = $("#selected_ins_Character").val();
    var insRegion = $("#selected_ins_region").val();
    var pageSize =$("#pageSize").val();
    if(!p || !/\d+/g.test(p))
        p = 1;
    var searchString = $.trim($("#search_some_one").val());
    var data = {"searchString":searchString ,
                "insCharacter": insCharacter,
                "insRegion": insRegion,
                "page.pageSize":pageSize,
                "orderBy":orderBy,
                "page.pageNo":p     
               };
    
    SearchIns.ajaxSearchNewIns(data);
};

page.topage = function(){
    var  toPage = $.trim($("#toPage").val()) ;
    var pageSize =$("#pageSize").val();
    if(!/^\d+$/g.test(toPage))
        toPage = 1;
    
    toPage   =Number(toPage) ;
    var totalPages = Number( $("#totalPages").val()  );
    
    if(toPage > totalPages){
        toPage = totalPages ;
    }else if(toPage<1){
        toPage = 1 ;
    }
    var searchString = $.trim($("#search_some_one").val());
    var orderBy = $("#orderBy").val();
    var insCharacter = $("#selected_ins_Character").val();
    var insRegion = $("#selected_ins_region").val();
    var data = {
            "searchString":searchString ,
            "insCharacter": insCharacter,
            "insRegion": insRegion,
            "orderBy":orderBy,
            "page.pageSize":pageSize,
            "page.pageNo":toPage     
           };
    
    SearchIns.ajaxSearchNewIns(data);
};

//设置机构菜单为选中状态
function resetSelectedMenu(){
    $(".result_class_wrap li").removeClass("cur");
    $("#search_ins").addClass("cur");
}

//点击地区查询
page.searchByRegion = function(insRegion,obj){
    $("#selected_ins_region").val(insRegion);
    var searchString = $.trim($("#search_some_one").val());
    var insCharacter = $("#selected_ins_Character").val();
    var orderBy = $("#orderBy").val();
    var pageSize = $("#pageSize").val();
    if (pageSize == null || typeof (pageSize) == "undefined") {
      pageSize = 10;
    }
    var data = {
        "searchString":searchString ,
        "insCharacter": insCharacter,
        "insRegion": insRegion,
        "orderBy":orderBy,
        "page.pageSize":pageSize,
        "page.pageNo":1     
       };

    SearchIns.ajaxSearchNewIns(data);
};

//点击类型查询
page.searchByCharacter = function(insCharacter,obj){
    $("#selected_ins_Character").val(insCharacter);
    var searchString = $.trim($("#search_some_one").val());
    var insRegion = $("#selected_ins_region").val();
    var orderBy = $("#orderBy").val();
    var pageSize = $("#pageSize").val();
    if (pageSize == null || typeof (pageSize) == "undefined") {
      pageSize = 10;
    }
    var data = {
        "searchString":searchString ,
        "insCharacter": insCharacter,
        "insRegion": insRegion,
        "orderBy":orderBy,
        "page.pageSize":pageSize,
        "page.pageNo":1     
       };

    SearchIns.ajaxSearchNewIns(data);
};

//自己输入地区
page.inputRegionNameBySelf = function() {
  var $regionId = $("#insRegionName").attr("code");
  var showName = $("#insRegionName").val();
  if ($regionId != "") {
    $("#ins_region_id_by_self").val($regionId);
    $("#ins_region_name_by_self").val(showName);
    page.searchByRegion($regionId,null);
  }
}

//再次点击返回
page.backSearch = function(str){
    var searchString = $.trim($("#searchString").val());
    if(str=="region"){
        $("#selected_ins_region").val("");
    }else if(str=="character"){
        $("#selected_ins_Character").val("");
    }
    var insRegion = $("#selected_ins_region").val();
    var insCharacter = $("#selected_ins_Character").val();
    var orderBy = $("#orderBy").val();
    var pageSize = $("#pageSize").val();
    if (pageSize == null || typeof (pageSize) == "undefined") {
      pageSize = 10;
    }
    var data = {
        "searchString":searchString ,
        "insCharacter": insCharacter,
        "insRegion": insRegion,
        "orderBy":orderBy,
        "page.pageSize":pageSize,
        "page.pageNo":1    
       };

    SearchIns.ajaxSearchNewIns(data);
}

//初始化 分享 插件
function initInsShare(obj){
    if(SmateShare.timeOut && SmateShare.timeOut == true)
        return;
    if (locale == "en_US") {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
            'language' : 'en_US'
        });
    } else {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
        });
    }
    var obj_lis = $("#share_to_scm_box").find("li");
    obj_lis.eq(0).hide();
    obj_lis.eq(1).click();
    obj_lis.eq(2).hide();
};

$(function(){
    if(document.getElementsByClassName("header__nav")){
      if(document.getElementById("num1")){
        document.getElementById("num1").style.display="flex";      
      }
      if(document.getElementsByClassName("header-main__box") && document.getElementById("num2")){
        document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
      }
      if(document.getElementsByClassName("top-main_search-input").length>0){
          document.getElementsByClassName("top-main_search-input")[0].style.width = "400px";
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
        <input type="hidden" name="insRegionIdBySelf" id="ins_region_id_by_self" value="" /> 
        <input type="hidden" name="insRegionNameBySelf" id="ins_region_name_by_self" value="" />
		<input type="hidden" name="orderBy" id="orderBy" value="" />
		<input type="hidden" name="searchListComplete" id="searchListComplete" value="0"/>
		<input type="hidden" name="characterMenuComplete" id="characterMenuComplete" value="0"/>
		<input type="hidden" name="regionMenuComplete" id="regionMenuComplete" value="0"/>
		<input type="hidden" name="hasLogin" id="hasLogin" value="${hasLogin }"/>
		
        <div id="inscontent" style="display: flex; justify-content: center; width: 1200px;"></div>
	</div>
	<jsp:include page="/common/smate.share.jsp" />
</body>
</html>