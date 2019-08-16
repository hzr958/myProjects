<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	window.onload = function() {
		SearchIns.refreshRegionFilter();
		SearchIns.refreshCharacterFilter();
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
		
		SearchIns.ajaxSearchIns(data);
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
		
		SearchIns.ajaxSearchIns(data);
	};
	
	//设置机构菜单为选中状态
	function resetSelectedMenu(){
		$(".result_class_wrap li").removeClass("cur");
		$("#search_ins").addClass("cur");
	}
	
</script>
<style>
.pd0{
	padding-left:0px;
}
</style>
