var page = page ? page : {};
page.submit = function(p){
	if(!p || !/\d+/g.test(p))
  	  	p = 1;
	$("#pageNo").attr("value",p);
		if(typeof projectToPage!='undefined'&&projectToPage instanceof Function){    
			projectToPage();  
			}else if($("#isGroupPsnPubList").val() == "1" && typeof groupPsnPubToPage!='undefined' && groupPsnPubToPage instanceof Function){
				groupPsnPubToPage();
			}else{
				$("#mainForm").submit();	
			}
};

page.topage = function(){
	var toPage =  $.trim($("#toPage").val());
	if(!/^\d+$/g.test(toPage))
		toPage = 1;
	$("#pageNo").attr("value",toPage); 
	if(typeof projectToPage!='undefined'&&projectToPage instanceof Function){    
		projectToPage();  
		}else if($("#isGroupPsnPubList").val() == "1" && typeof groupPsnPubToPage!='undefined' && groupPsnPubToPage instanceof Function){
			groupPsnPubToPage();
		}else{
			$("#mainForm").submit();	
		}
};
