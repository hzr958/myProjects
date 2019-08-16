var page = {};

page.submit = function(p){
	if(!p || !/\d+/g.test(p))
  	  	p = 1;
	$("#pageNo").attr("value",p);
		if(typeof submitToPage!='undefined'&&submitToPage instanceof Function){    
			submitToPage();  
			}else{
				if(typeof(waterMark)!= 'undefined' && waterMark != null){
				
				var searchValue = $.trim($("#searchStr").val());
				if(searchValue == null ||searchValue == waterMark){
					searchValue = "";
				}
				
				$("#searchStr").val(searchValue);
				}
				if(typeof publist!='undefined'&&publist=="true"){
					$("#mainForm").attr("action","/pubweb/publication/show")
				}
				
				$("#mainForm").submit();	
			}
};

page.topage = function(){
	var toPage =  $.trim($("#toPage").val());
	if(!/^\d+$/g.test(toPage))
		toPage = 1;
	$("#pageNo").attr("value",toPage); 
	if(typeof submitToPage!='undefined'&&submitToPage instanceof Function){    
		submitToPage();  
		}else{
				if(typeof(waterMark)!= 'undefined' && waterMark != null){
				
				var searchValue = $.trim($("#searchStr").val());
				if(searchValue == null ||searchValue == waterMark){
					searchValue = "";
				}
				
				$("#searchStr").val(searchValue);
				}
				if(typeof publist!='undefined'&&publist=="true"){
					$("#mainForm").attr("action","/pubweb/publication/show")
				}
			    $("#mainForm").submit();	
		}
};
