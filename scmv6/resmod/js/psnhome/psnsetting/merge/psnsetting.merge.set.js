/**
 * 
 */

 var PsnsettingMerge =  PsnsettingMerge || {};


 PsnsettingMerge.init=function (){
	 PsnsettingMerge.loadMergeInfo();
 };
 
 
 PsnsettingMerge.showAddEmailBox = function(){
	 //表示改账号，是否正在被合并，
	 var currentMergeStatus = $("#currentMergeStatus").val();
	 if(currentMergeStatus=="true"){
		 scmpublictoast(PsnsettingMerge.acountHadMerge , 2000);
		   //alert("This account is being merged,the operation is forbiden.");
		}else{
			 PsnsettingMerge.popUpAddEmailBox ();
		}
 };
 
 
 PsnsettingMerge.saveMergeCountConfirm = function(){
	 var   mergeCount  = $("#merge_email").val();
    var  mergeCount = $.trim(mergeCount);
     var   mergePwd  = $("#merge_password").val();
	 if(mergeCount === "" ){
		scmpublictoast(PsnsettingMerge.accountNotBlank , 2000);
		return ;
	 } ;
	 if( mergePwd === ""){
		 scmpublictoast(PsnsettingMerge.passwordNotBlank , 2000);
		 return ;
	 } ;
	 var screencallbackData={
	  };
	 var span1 = "<span class='new-email_merge-title'>"+PsnsettingMerge.confirm1+"</span>";
	 var mergeTip = "<div class='new-email_merge-content' title='"+mergeCount+"'>"+mergeCount+"</div>";
	 var span2 = "<span class='new-email_merge-target'>"+PsnsettingMerge.confirm2 + $("#currentLoginCount").val() +" "+PsnsettingMerge.confirm3+"</span>";
	 var   mergeConfirm = span1 + mergeTip + span2;
	    var option={
	            'screentxt':mergeConfirm ,
	            'screencallback':PsnsettingMerge.saveMergeCount,
	            'screencallbackData':screencallbackData
	   };
	    popconfirmbox(option)
 }
 
 
 
 
 
 
 /**
  * /psnweb/psnsetting/ajaxMergeCount
  * 合并账号
  */
 
 PsnsettingMerge.saveMergeCount = function(){
	var   mergeCount  = $("#merge_email").val();
	     mergeCount = $.trim(mergeCount);
	var   mergePwd  = $("#merge_password").val();
	var paramt = {
			mergeCount :mergeCount,
			mergePwd :mergePwd
	} ;
	$.ajax({
		url : "/psnweb/psnsetting/ajaxMergeCount",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				if(data.result == "success"){
					scmpublictoast(data.msg , 2000);
					//重新加载当前页
					//需要延时
					setTimeout(function(){
						location.reload();
					},2000);
				}else{
					scmpublictoast(data.msg , 2000);
				}
				
			});
		},
		error: function(){
		}
	});	
};
 



  //<%--  是不是要监听后台合并任务，并给还留在合并页面的用户 提示合并结果信息 tsz_2015.1.30 --%>
PsnsettingMerge.loadMergeInfo  =  function loadMergeInfo(){
	timerId = window.setInterval(function() {
		//<%--  每5S查看数据库合并状态  如果是成果或失败 给出提示  --%>
		//<!--  获取参数   -->
		var psnIds = "";
		$(":input[name='mergedPsnId']").each(function() {
				if (psnIds == "") {
					psnIds = $(this).val();
				} else {
					psnIds = psnIds + "," + $(this).val();
				}
		});
		if(psnIds==""){
			return ;
		}
		$.ajax( {
			url : '/psnweb/psnsetting/ajaxgetmergestatus',
			type : 'post',
			dataType:'json',
			data : {"psnIds":psnIds},
			success : function(data) {
				if(data.result=='scusess'){
					 
					var showinfo="";
					if(data.showScusess=="true"){
						showinfo=showinfo+ PsnsettingMerge.account_1 +data.mergeScusess+  PsnsettingMerge.mergeSuccess;
					}
					if(data.showError=="true"){
						showinfo=showinfo+ PsnsettingMerge.account_1 +data.mergeError+ PsnsettingMerge.mergeFail;
					}
					if(showinfo!=""){
						scmpublictoast(showinfo , 3000);
						//需要延时
						setTimeout(function(){
							location.reload();
						},5000);
					}
				}
			},
			error:function(data){
			}
		});
	},
	5000);
}
 
 
/**
 * 弹出框
 */
 PsnsettingMerge.popUpAddEmailBox = function(options){
     var defaults = {
         screencallback: "" ,
         screencallbackData: ""
     };
     var opts = Object.assign(defaults,options);
     var screencallback = opts.screencallback || function () {};
     var screencallbackData = opts.screencallbackData || {};
     if(document.getElementsByClassName("confirm-container").length==0){
     	var coverbox = document.createElement("div");
         coverbox.className = "confirm-container";
     	document.body.appendChild(coverbox);
     	var element = '<div class="add__pass-header">'
     	+'<div class="add__pass-header__title">'+PsnsettingMerge.mergeAccount+'</div>'
 			    +'<i class="material-icons add__pass-close">close</i>'
 			    +'</div>'
 			    +'<div class="add__pass-content">'
 			    +'<div class="add__pass-content__tip">'+PsnsettingMerge.mergeAccountTip+'</div>'
 			    +'<div class="add__pass-content__list">'
 			    +'<span class="add__pass-content__list-title">'+PsnsettingMerge.account+PsnsettingMerge.colon+'</span>'
 			    +'<input class="add__pass-content__list-input" type="text"  id="merge_email" placeholder="'+PsnsettingMerge.account+'">'
 			    +'</div>'
 			    +'<div  class="add__pass-content__list">'
 			    +'<span class="add__pass-content__list-title" >'+PsnsettingMerge.password+ PsnsettingMerge.colon+'</span>'
 			    +'<input class="add__pass-content__list-input" type="password"  id="merge_password" placeholder="'+PsnsettingMerge.password+'">'
 			    +'</div>'
 			    +'</div>'
 			    +'<div class="add__pass-footer">'
 			    +'<div class="add__pass-cancle">'+PsnsettingMerge.cancel+'</div>'
 			    +'<div class="add__pass-comfir" onclick=" PsnsettingMerge.saveMergeCountConfirm();"  >'+PsnsettingMerge.confirm+'</div>'
 			    +'</div>';

 	    var parntbox = document.createElement("div");
 	    parntbox.className = "add__pass-container";
 	    parntbox.innerHTML = element;
 	    coverbox.appendChild(parntbox);
 	    const canclele = document.getElementsByClassName("add__pass-close")[0];
 	    const confirmele = document.getElementsByClassName("add__pass-cancle")[0];
 	    var inputlist = document.getElementsByClassName("add__pass-content__list-input");
 	    const allwidth = coverbox.clientWidth;
 	    const allheight = coverbox.clientHeight;
 	    var setwidth = (allwidth - 382)/2 + "px"; 
 	    var setheight =  (allheight - 242)/2 + "px";
 	    parntbox.style.left = setwidth;
 	    parntbox.style.bottom = setheight;
    
       
 	    function resize(){
             var allheight = document.getElementsByClassName("confirm-container")[0].clientHeight;
             var allwidth = document.getElementsByClassName("confirm-container")[0].clientWidth;
 	        var setwidth = (allwidth - 382)/2 + "px"; 
 	        var setheight =  (allheight - 242)/2 + "px";
 	        document.getElementsByClassName("add__pass-container")[0].style.left = setwidth;
 	        document.getElementsByClassName("add__pass-container")[0].style.bottom = setheight;
 	    }

 	   window.onresize = resize;
 	    canclele.onclick = function(){
 	        parntbox.style.bottom="-260px";
 	        setTimeout(function(){
 	        	coverbox.removeChild(parntbox);
 	       	    coverbox.style.display = "none";
 	       	    document.body.removeChild(coverbox);
 	        } ,400);
 	    };
 	   
 	    confirmele.onclick = function(){
 	    	screencallback(screencallbackData);
             parntbox.style.bottom="-260px";
 	        setTimeout(function(){
 	        	coverbox.removeChild(parntbox);
 	        	coverbox.style.display = "none";
 	        	document.body.removeChild(coverbox);
 	        } ,400);
 	    }; 

 	    for(var i = 0; i < inputlist.length; i++){
             inputlist[i].onfocus = function(){
             	this.style.borderColor = "#288aed";
             }
             inputlist[i].onblur = function(){
             	this.style.borderColor = "#ddd";
             }
 	    }


     }
 } 
