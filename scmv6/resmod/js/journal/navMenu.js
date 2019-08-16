
var navCurrent=null;
var navDefault=null;
var navTid=null;
var navSelectedL3Item=""; 

	$(document).ready(function(){	

        $('#main_nav>li').hover(			
			function(){
				/*
				if($(".default")){
					$('#main_nav').children(".highlight").addClass("default");
				}
				$(this).addClass("highlight").siblings().removeClass("highlight");*/
				clearTimeout(navTid);
				navCurrent = $(this).attr("id");
				
				if(document.getElementById(navSelectedL3Item))	
				{
					if(navSelectedL3Item.indexOf(navCurrent+"_")<0)
					{
						document.getElementById(navSelectedL3Item).style.display="none";
						document.getElementById(navSelectedL3Item).style.visibility="hidden";
					}
				}
			},
			function(){	
				navCurrent = $(this).attr("id");
				navDefault = $(".default").attr("id");
				navTid = setTimeout('navCheckForMouseOut()', 200);
			}
		);		
        /*
        $('#main_nav li').click(function(){
        	$(this).addClass("highlight").siblings().removeClass();
			$(this).addClass("default");
        });	*/

})  

function navCheckForMouseOut()
{
		/*
	if(document.getElementById(navCurrent))
		document.getElementById(navCurrent).className="";

	if(document.getElementById(navDefault))	
		document.getElementById(navDefault).className="highlight";
		 */
	if(document.getElementById(navSelectedL3Item))	
	{
		document.getElementById(navSelectedL3Item).style.display="none";
		document.getElementById(navSelectedL3Item).style.visibility="hidden";
	}
}
 
function navShowLevel3Section(sObjectName)
{	navHideLevel3Section(navSelectedL3Item);
	if(document.getElementById(sObjectName+"_SubNav"))
	{
		var cuItem = $("#"+sObjectName);
		var ol3Item=document.getElementById(sObjectName+"_SubNav");
		ol3Item.style.display="";
		ol3Item.style.visibility="visible";
		ol3Item.style.zIndex="999";
		navSelectedL3Item=sObjectName+"_SubNav";
		if(cuItem.size() > 0 && cuItem.width() > 130){
			$(ol3Item).width(cuItem.width());
			$(ol3Item).find(">li").each(function(){
				$(this).width(cuItem.width() - 10);
			});
		}
	}
}

function navHideLevel3Section(sObjectName)
{
	if(sObjectName!=""){
		var ol3Item=document.getElementById(sObjectName);
		ol3Item.style.display="none";
		navSelectedL3Item="";
	}
}

function navClearLevel3Nav(sObjectName)
{
	if(navSelectedL3Item!=sObjectName+"_SubNav")
	{
		navHideLevel3Section(navSelectedL3Item);
	}
}

function navL3ItemHover(o)
{
	o.className="navL3ItemHover";
}

function navL3ItemOut(o)
{
	o.className="navL3ItemNormal";
}
