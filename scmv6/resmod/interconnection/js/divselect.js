jQuery.divselect = function(divselectid,inputselectid) {
	var inputselect = $(inputselectid);
	$(divselectid+" cite").click(function(){
		var ul = $(divselectid+" ul");
		if(ul.css("display")=="none"){
			ul.slideDown("fast");
		}else{
			ul.slideUp("fast");
		}
	});
	$(divselectid+" ul li a").click(function(){
		var txt = $(this).text();
		$(divselectid+" cite").html(txt);
		var value = $(this).attr("selectid");
		inputselect.val(value);
		$(divselectid+" ul").hide();
		
	});
	$(document).click(function(){
		$(divselectid+" ul").hide();
	});
};
/*
jQuery.divselect1 = function(divselectid1,inputselectid1) {
	var inputselect1 = $(inputselectid1);
	$(divselectid1+" span").click(function(){
		var ul = $(divselectid1+" ul");
		if(ul.css("display")=="none"){
			ul.slideDown("fast");
		}else{
			ul.slideUp("fast");
		}
	});
	$(divselectid1+" ul li a").click(function(){
		var txt = $(this).text();
		$(divselectid1+" span").html(txt);
		var value = $(this).attr("selectid1");
		inputselect1.val(value);
		$(divselectid1+" ul").hide();

	});
	$(document).click(function(){
		$(divselectid1+" ul").hide();
	});
};*/
