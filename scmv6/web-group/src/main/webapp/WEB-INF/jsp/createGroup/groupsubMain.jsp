<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function() {  
	
	//移到学科上样式改变
	$(".selector_dropdown_option").mouseover(function(){
		$(this).addClass("hover");
	});
	
	//移除学科后样式改变
	$(".selector_dropdown_collections .selector_dropdown_option").mouseout(function(){
		$(this).removeClass("hover");
	});
	
	//点击学科进行选中操作
	$(".selector_dropdown_option").click(function(){
		var categoryText=$(this).text();
		$("#secondCategoryShown").text(categoryText);
		$(this).parent().removeClass("shown");
		$(this).parent().css("display","none");
		var categoryValue=$(this).attr("value");
		$("input[name='disciplines']").val(categoryValue);
		
	}); 
});

</script>
<div class="selector_dropdown_value">
  <c:forEach var="secondDiscipline" items="${secondDisciplineList}">
    <div class="selector_dropdown_option" value="${secondDiscipline.secondCategoryId}">${secondDiscipline.secondCategoryZhName }</div>
  </c:forEach>
</div>
<div class="selector_dropdown_collections shown" style="display: none;">
  <c:forEach var="secondDiscipline" items="${secondDisciplineList}">
    <div class="selector_dropdown_option" value="${secondDiscipline.secondCategoryId}">${secondDiscipline.secondCategoryZhName }</div>
  </c:forEach>
</div>
