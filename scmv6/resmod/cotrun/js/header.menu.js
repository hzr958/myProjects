var HeaderMenu = HeaderMenu ? HeaderMenu : {};

HeaderMenu.menuNavMouseHandle = function(){
	$(".headerNav a").mouseover(function(){
		$(this).parent().addClass("header_nav_on");
		var dataItem = $(this).attr("dataItem");
		var flag = true;
		$(".pull_downnav").each(function(){
			if($(this).hasClass("active")){
				$(this).hide();
			}
			if($(this).attr("dataItem")==dataItem){
				$(this).show();
				flag=false;
			}
		});
		if(flag){
			$(".pull_downnav").each(function(){
				if($(this).hasClass("active")){
					$(this).show();
				}
			})
		}
	}).mouseout(function(){
		$(".headerNav a").each(function(){
			if($(this).hasClass("active"))
				return;
			$(this).parent().removeClass("header_nav_on");
		})
		$(".pull_downnav").each(function(){
			if($(this).hasClass("active")){
				$(this).show();
			}else{
				$(this).hide();
			}
		})
	});
	
	$(".pull_downnav").mouseover(function(){
		if(!$(this).hasClass("active")){		
			$(this).parent().find(".pull_downnav.active").hide();			
		}		
		$(this).show();
		var dataItem = $(this).attr("dataItem");
		$(".headerNav a").each(function(){
			if($(this).attr("dataItem")==dataItem){
				$(this).parent().addClass("header_nav_on");
			}
		});
	}).mouseout(function(){
		$(".pull_downnav").each(function(){
			if($(this).hasClass("active")){
				$(this).show();
			}else{
				$(this).hide();
			}
		});
		$(".headerNav a").each(function(){
			if($(this).hasClass("active"))
				return;
			$(this).parent().removeClass("header_nav_on");
		});
	});
};

$(document).ready(function(){
	HeaderMenu.menuNavMouseHandle();
});