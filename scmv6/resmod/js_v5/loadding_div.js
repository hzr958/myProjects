var Loadding_div =Loadding_div?Loadding_div:{};
//覆盖在具体的层上面
Loadding_div.show_over = function(overId,name,content){
	var over = $("#"+overId) || $(overId);
	var	left = $(over).offset().left;
	var top = $(over).offset().top;
	var width = $(over).width();
	var padding_left = parseInt($(over).css("padding-left").substring(0,$(over).css("padding-left").length-2));
	var padding_right = parseInt($(over).css("padding-right").substring(0,$(over).css("padding-right").length-2));
	width = width+padding_left+padding_right;
	var height = $(over).height();
	var padding_top = parseInt($(over).css("padding-top").substring(0,$(over).css("padding-top").length-2));
	var padding_bottom = parseInt($(over).css("padding-bottom").substring(0,$(over).css("padding-bottom").length-2));
	height = height+padding_top+padding_bottom;
	Loadding_div.show_detail(left,top,width,height,name,content);
};
//自己指定位置覆盖
Loadding_div.show_detail = function(left,top,width,height,name,content){
	var box_id = name + "_box";
	var main_id = name + "_main";
	var box = $("<div class='loadding_div_box' id='"+box_id+"'></div>");
	var main =  $("<div class='loadding_div_main'  id='"+main_id+"'></div>");
	if(content){
		var size = content.length;
		var main_width = size * 5 + 20;
		main.css("padding-left",30);
		main.css("padding-top",5);
		main.css("font-size",11);
		main.width(main_width);
		main.html(content);
	}
	$(document.body).append(box);
	$(document.body).append(main);
	
	box.css("width",width);
	box.css("height",height);
	box.css("left",left);
	box.css("top",top);
	
	var main_width = $(main).width();
	var main_height = $(main).height();
	var mx = left + (width - main_width)/2;
	var my = top + (height - main_height)/2;
	main.css("left",mx);
	main.css("top",my);
	box.show();
	main.show();
};
//关闭进度条
Loadding_div.close = function(name){
	var box_id = name + "_box";
	var main_id = name + "_main";
	$("#"+box_id).remove();
	$("#"+main_id).remove();
};
