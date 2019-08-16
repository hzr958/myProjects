<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function setTab(obj){
    var hrefVal = $(obj).attr("targetHref");
    if(hrefVal.indexOf("?searchString=")<0){
    	hrefVal += "?searchString=" + $.trim(encodeURIComponent($("#searchString").val())) + "&fromPage="+$("#fromPage").val();	
    }
    window.location.replace(hrefVal);
}

function initTabStatus(obj){
    $(".tab_div .hover").removeClass("hover");
    $(obj).addClass("hover");
}
</script>
<div class="Menubox_1 tab_div dev_history_comment" style="top: 93px;">
  <ul style="display: flex;justify-content: space-around;align-items: center;">
    <a targetHref="/pub/paper/search" id="pdwhpapertaga" onclick="setTab(this)"><li id="one1" class="hover">论文</li></a>
    <a targetHref="/pub/patent/search" id="pdwhpatenttaga" onclick="setTab( this)"><li id="one2">专利</li></a>
    <a targetHref="/psnweb/mobile/search" id="topsn" onclick="setTab(this)"><li id="one3">人员</li></a>
  </ul>
</div>
