/**
 * 分页.
 * 
 * @author zzx
 * @since 2016-09-26
 */
var  TagesMain = TagesMain ? TagesMain : {};
// 主入口,myFunction为需要执行的方法
TagesMain.main= function (myFunction){
	if($("#divselect").hasClass("hasload")){
		return;
	}
	// 加载翻页模块（必须放在最前）
	TagesMain.loadPageModule();
	// 点击每页显示条数
	TagesMain.divselectClick("#divselect","#divselectul","#pageSize",myFunction);
	// 点击第几页
	TagesMain.divselectClick("#divselect01","#divselect01ul","#pageNo",myFunction);
	// 翻页列表事件
	TagesMain.clickPageList(myFunction);
	// 点击上一页和下一页
	TagesMain.findLastAndNextPage(myFunction);
	$("#divselect").addClass("hasload");
}
// 加载翻页模块（必须放在最前）
TagesMain.loadPageModule = function (){
	var totalPages = $("#totalPages").val();
	var pageNo = $("#pageNo").val();
	var totalCount = $("#totalCount").val();
	// 选择页数下拉框加载
	for(var i=totalPages;i>0;i--){
		$("#divselect01ul").prepend("<li><a href='javascript:;' selectid='"+i+"'>"+i+"</a></li>");
		}
	// 选择页数行加载
	$("#lastPage").after("<a href='javascript:;' id='currentPage'class='hover page_a'>"+pageNo+"</a>");
	if(totalPages<7){
		for(var i = pageNo-1;i>0;i--){
			$("#lastPage").after("<a href='javascript:;'class='page_a'>"+i+"</a>");
		}
		for(var i =totalPages-pageNo;i>0;i--){
			$("#currentPage").after("<a href='javascript:;' class='page_a'>"+(Number(pageNo)+Number(i))+"</a>");
		}
	}else{
	if(pageNo<5){
		for(var i = pageNo-1;i>0;i--){
			$("#lastPage").after("<a href='javascript:;' class='page_a'>"+i+"</a>");
		}
		var tem1 =totalPages>6?5-pageNo:totalPages-pageNo;
		for(;tem1>0;tem1--){
			$("#currentPage").after("<a href='javascript:;' class='page_a'>"+(Number(pageNo)+Number(tem1))+"</a>");
		}
		if(totalPages>6){
			$("#nextPage").before("<span id='pagespan'>…</span>");
			$("#nextPage").before("<a href='javascript:;' class='page_a'>"+totalPages+"</a>");
		}
	}else{
		$("#currentPage").before("<a href='javascript:;' class='page_a'>1</a>");
		$("#currentPage").before("<span id='pagespan'>…</span>");
		for(var i =pageNo-2;i<pageNo;i++){
			$("#currentPage").before("<a href='javascript:;' class='page_a'>"+i+"</a>");
		}
		var tem2 =totalPages-pageNo>3?3:totalPages-pageNo;
		for(;tem2>0;tem2--){
			$("#currentPage").after("<a href='javascript:;' class='page_a'>"+(Number(pageNo)+Number(tem2))+"</a>");
		}
		if(totalPages-pageNo<3&&totalPages>3){
			var tem3 = totalPages-pageNo;
			for(var i =0;i<2-tem3;i++){
				$("#pagespan").after("<a href='javascript:;' class='page_a'>"+(Number(pageNo)-3-Number(i))+"</a>");
			}
		}
		if(totalPages-pageNo>3){
			if (!(Number(totalPages) - Number(pageNo) == 4)) {
				$("#nextPage").before("<span id='pagespan' class='page_a'>…</span>");
			}
			$("#nextPage").before("<a href='javascript:;' class='page_a'>"+totalPages+"</a>");
		}
	}
	}
	// 选择每页数量下拉框加载
	var i =totalCount%10>0?Math.floor(totalCount/10)+1:Math.floor(totalCount/10);
	if(i>5){
		i=5;
	}
	for(;i>0;i--){
		$("#divselectul").prepend("<li><a href='javascript:;' selectid='"+i*10+"'>"+i*10+"</a></li>");
		}		
}
// 翻页列表事件
TagesMain.clickPageList = function (myFunction){
	$("#pageModule").find(".page_a").click(function(){
		$("#pageNo").val($(this).html());
		myFunction();
	});
}
// 点击上一页和下一页
TagesMain.findLastAndNextPage = function (myFunction){
	$("#lastPage,#nextPage").removeClass("page_grey");
	if($("#pageNo").val()==1){
		$("#lastPage").addClass("page_grey");
	}
	if($("#pageNo").val()==$("#totalPages").val()){
		$("#nextPage").addClass("page_grey");
	}
	$("#lastPage").click(function(){
		if(!$("#lastPage").is(".page_grey")){
		$("#pageNo").val((Number($("#pageNo").val())-1));
		myFunction();
		}
	});
	$("#nextPage").click(function(){
		if(!$("#nextPage").is(".page_grey")){
		$("#pageNo").val((Number($("#pageNo").val())+1));
		myFunction();
		}
	});
}
// 点击翻页模块下拉框（总页数、页数量公用）
TagesMain.divselectClick = function(divselect,divselectul,inputParam,myFunction){
	$(divselect).click(function(e){
		var _obj = $(divselectul);
		// 显示隐藏
		if (_obj.is(":hidden")) {
			_obj.show();
		} else {
			_obj.hide();
			return;
		}
		// 点击其他地方关闭
	    if (e && e.stopPropagation) {// 非IE
	        e.stopPropagation();  
	    }  
	    else {// IE
	        window.event.cancelBubble = true;  
	    } 
	    $(document).click(function(){_obj.hide();});
	});
	$(divselect).find("li").click(function(){
		$(divselect).find("cite").html($(this).html());
		$(inputParam).val($(this).text());
		myFunction();
	});
}

