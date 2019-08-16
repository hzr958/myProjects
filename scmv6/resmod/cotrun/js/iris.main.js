var IrisMain = IrisMain ? IrisMain : {};

IrisMain.toggleNavItem = function(item){
	$("#"+item+" .icon_hover_noneimg a").click(function(){
		$("#"+item+" .icon_hover_noneimg").each(function(){
			$(this).removeClass("icon_hover");
		});
		$(this).parent().addClass("icon_hover");
		
		var dataItem = $(this).attr("dataItem");
		$("#"+item+" .product_img").each(function(){
			if($(this).attr("dataItem")==dataItem){
				if($(this).hasClass("active_noneimg")){
					$(this).fadeIn(1000);
					$(this).removeClass("active_noneimg");
				}
			}else{
				if(!$(this).hasClass("active_noneimg")){
					$(this).hide();
					$(this).addClass("active_noneimg");
				}
			}
		});
		
		$("#"+item+" .round_img a").each(function(){
			if($(this).attr("dataItem")==dataItem){
				if(!$(this).hasClass("round_hover")){
					$(this).addClass("round_hover");
				}
			}else{
				if($(this).hasClass("round_hover")){
					$(this).removeClass("round_hover");
				}
			}
		});
		
	});
};

IrisMain.toggleNavItemRound = function(item){
	$("#"+item+" .round_img a").click(function(){
		$("#"+item+" .round_img a").each(function(){
			$(this).removeClass("round_hover");
		});
		$(this).addClass("round_hover");
		
		var dataItem = $(this).attr("dataItem");
		$("#"+item+" .product_img").each(function(){
			if($(this).attr("dataItem")==dataItem){
				if($(this).hasClass("active_noneimg")){
					$(this).fadeIn(1000);
					$(this).removeClass("active_noneimg");
				}
			}else{
				if(!$(this).hasClass("active_noneimg")){
					$(this).hide();
					$(this).addClass("active_noneimg");
				}
			}
		});
		
		$("#"+item+" .icon_hover_noneimg").each(function(){
			if($(this).children().attr("dataItem")==dataItem){
				if(!$(this).hasClass("icon_hover")){
					$(this).addClass("icon_hover");
				}
			}else{
				if($(this).hasClass("icon_hover")){
					$(this).removeClass("icon_hover");
				}
			}
		});
		
	});
};

//IrisMain.mouseHandle = function(item){
//	$("#"+item+" .icon_hover_noneimg a").mouseover(function(){
//		$(this).parent().animate({marginTop:"0px"});
//	});
//	$("#"+item+" .icon_hover_noneimg a").mouseout(function(){
//		$(this).parent().animate({marginTop:"4px"});
//	});
//};

IrisMain.mouseHandle = function(item){
	$("#"+item+" .icon_hover_noneimg a").hover(function(){
		$(this).parent().stop().animate({marginTop:"0px"},"fast");
	},function(){
		$(this).parent().stop().animate({marginTop:"4px"},"fast");
	});
};

$(document).ready(function(){
	// 项目申请
	IrisMain.toggleNavItem("function_icon01");
	IrisMain.toggleNavItemRound("function_icon01");
	IrisMain.mouseHandle("function_icon01");
	
	// 项目评审
	IrisMain.toggleNavItem("function_icon02");
	IrisMain.toggleNavItemRound("function_icon02");
	IrisMain.mouseHandle("function_icon02");
	
	// 项目管理
	IrisMain.toggleNavItem("function_icon03");
	IrisMain.toggleNavItemRound("function_icon03");
	IrisMain.mouseHandle("function_icon03");
	
	// 资金管理
	IrisMain.toggleNavItem("function_icon04");
	IrisMain.toggleNavItemRound("function_icon04");
	IrisMain.mouseHandle("function_icon04");

	// 成果管理
	IrisMain.toggleNavItem("function_icon05");
	IrisMain.toggleNavItemRound("function_icon05");
	IrisMain.mouseHandle("function_icon05");
	
	// 单位管理
	IrisMain.toggleNavItem("function_icon06");
	IrisMain.toggleNavItemRound("function_icon06");
	IrisMain.mouseHandle("function_icon06");
	
	// 成果推广
	IrisMain.toggleNavItem("function_icon07");
	IrisMain.toggleNavItemRound("function_icon07");
	IrisMain.mouseHandle("function_icon07");

});