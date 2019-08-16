function scrollul(s){
	var $ul = $(s+" ul");
 	var liHeight = $ul.find("li:last").height();
 	$ul.animate({marginTop : liHeight+20 +"px"},1000,function(){
		$ul.find("li:last").prependTo($ul)
		$ul.find("li:first").hide();
		$ul.css({marginTop:0});
		$ul.find("li:first").fadeIn(1000);
	});
}
function setTab(name,cursel,n){
	for(i=1;i<=n;i++){
		var menu=document.getElementById(name+i);
		var con=document.getElementById("con_"+name+"_"+i);
		if(typeof menu != "undefined" && menu!=null){
			menu.className=i==cursel?"hover":"";
		}
		if(typeof con != "undefined" && con!=null){
			con.style.display=i==cursel?"block":"none";
		}
		
	}
}