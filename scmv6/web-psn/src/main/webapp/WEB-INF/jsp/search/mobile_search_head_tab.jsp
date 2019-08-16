<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function setTab(obj){
    var hrefVal = $(obj).attr("targetHref");
    if(hrefVal.indexOf("?searchString=")<0){
        hrefVal += "?searchString=" + $.trim($("#searchString").val())+ "&fromPage="+$("#fromPage").val(); 
    }
    window.location.replace(hrefVal);
}

function initTabStatus(obj){
    $(".tab_div .hover").removeClass("hover");
    $(obj).addClass("hover");
}
</script>
<div class="Menubox_1" style="top: 93px;">
  <ul style="display: flex;justify-content: space-around;align-items: center;">
    <a targetHref="/pub/paper/search" onclick="setTab(this)"><li id="one1">论文</li></a>
    <a targetHref="/pub/patent/search" onclick="setTab(this)"><li id="one2">专利</li></a>
    <a targetHref="/psnweb/mobile/search" id="topsn" onclick="setTab(this)"><li id="one3" class="hover">人员</li></a>
  </ul>
</div>
