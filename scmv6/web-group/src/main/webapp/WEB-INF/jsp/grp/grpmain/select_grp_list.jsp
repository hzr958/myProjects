<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".select_grp_box").mouseover(function () {
        $(this).addClass("item_selected");
    });
    $(".select_grp_box").mouseleave(function () {
    	$(this).removeClass("item_selected");
    });
});
    
</script>
<s:iterator value="grpBaseInfoList" var="grp" status="st">
  <li o class="main-list__item select_grp_box" des3GrpId='<iris:des3 code="${grp.grpId }"/>'
    onclick='SmateShare.clickGrpName(this)'>${grp.grpName }</li>
</s:iterator>