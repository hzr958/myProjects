function newstructuresubject(dataurl,obj){
   var getboxid = $(obj).attr("id");
   setselectecid = $(obj).attr("data-showid");
   subsetselectecid = $(obj).attr("data-subshowid");
   if(!document.getElementsByClassName("new-Discipline_establishment").length > 0){
        if(document.getElementsByClassName("background-promt").length > 0){
        	document.getElementsByClassName("background-promt")[0].style.display = "block"; 
        }else{
        	var back = document.createElement("div");
        	back.className = "background-promt"; 
        	document.body.appendChild(back);
        }
        var conrtent = '<div class="new-Discipline_establishment-header">'
            +'<span class="new-Discipline_establishment-header_title">选择学科代码</span>'
            +'<i class="material-icons new-Discipline_establishment-close">close</i>'
            +'</div>'
            +'<div class="new-Discipline_establishment-body">'
  	        +'<div class="new-Discipline_establishment-body_select">'
  		      +'<div class="new-Discipline_establishment-body_select-item">'
      			+'<span class="new-Discipline_establishment-body_select-title">一级学科</span>'
      			+'<div class="new-Discipline_establishment-body_select-box new-Discipline_establishment-body_select-mainbox">'
  	        +'</div>'
            +'</div>'
            +'<div class="new-Discipline_establishment-body_select-item">'
	          +'<span class="new-Discipline_establishment-body_select-title">二级学科</span>'
	          +'<div class="new-Discipline_establishment-body_select-box new-Discipline_establishment-body_select-subbox">'
	          +'</div>'
            +'</div>'
            +'</div>'
            +'<div class="new-Discipline_establishment-body_show">'
            +'<div></div>'
            +'<div class="new-Discipline_establishment-body_show-detail">请从上面的选择框中由左到右选择您的学科</div>'
            +'</div>'
    	      +'</div>'
    	      +'<div class="new-Discipline_establishment-footer">'
    		    +'<div class="new-Discipline_establishment-footer_confirm new-Discipline_establishment-close  new-Discipline_establishment-save">确认</div>'
    		    +'<div class="new-Discipline_establishment-footer_cancle new-Discipline_establishment-close">取消</div>'
    	      +'</div>';
		    var contentbox = document.createElement("div");	    	    
        contentbox.className = "new-Discipline_establishment";
        contentbox.innerHTML = conrtent;
        document.getElementsByClassName("background-promt")[0].appendChild(contentbox);
        contentbox.style.bottom = (document.body.offsetHeight - 400)/2 + "px";
        contentbox.style.left =  (document.body.offsetWidth - contentbox.offsetWidth)/2 + "px";
        window.onresize = function(){
        	  contentbox.style.top =  (document.body.offsetHeight - 400)/2 + "px";
            contentbox.style.left =  (document.body.offsetWidth - contentbox.offsetWidth)/2 + "px";
        }
        getstablishment(dataurl,"new-Discipline_establishment-body_select-mainbox");
    }
    var  closelist = document.getElementsByClassName("new-Discipline_establishment-close");    
    for(var i = 0; i < closelist.length; i++ ){
        closelist[i].onclick = function(){
            if(this.classList.contains("new-Discipline_establishment-save")){
                if(document.getElementsByClassName("new-Discipline_establishment-body_select-mainbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected").length>0){
                  var settext = document.getElementsByClassName("new-Discipline_establishment-body_select-mainbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].querySelector(".new-Discipline_establishment-body_item-detail").innerHTML.trim();
                 
                  var gettext = '<a href="javascript:void(0);" onclick="categoryThickbox.addSelectKeyword(this);" title="' + settext +'" flag="'+  getboxid +'">'
                        +'<i class="addfriends"></i>' + settext + '</a>';
                  var textbox = document.createElement("div");
                  textbox.className = "hot_01";
                  textbox.setAttribute("name", getboxid);
                  textbox.setAttribute("valtext",settext);
                  textbox.innerHTML = gettext;
                   
                  var flag = true;
                  var comparelist = document.getElementById("hotKeyWord").getElementsByClassName("hot_01");
                  for(var i = 0; i < comparelist.length; i++){
                      if(comparelist[i].querySelector("a").innerText == settext){
                        flag = false;
                      }
                  }
                  if(flag){
                      document.getElementById("hotKeyWord").getElementsByClassName("hot_key_list")[0].appendChild(textbox);
                  }
                  
                  if(document.getElementsByClassName("new-Discipline_establishment-body_select-subbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected").length>0){
                    var subtext =  document.getElementsByClassName("new-Discipline_establishment-body_select-subbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].querySelector(".new-Discipline_establishment-body_item-detail").innerHTML.trim();
                  }else{
                    var subtext =  document.getElementsByClassName("new-Discipline_establishment-body_select-mainbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].querySelector(".new-Discipline_establishment-body_item-detail").innerHTML.trim();
                  }
                  
                  $(obj).find("option").html(subtext);
                  var setcode = document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].getAttribute("datacode");
                  if(!setcode){//如果点击一级学科  
                    setcode=document.getElementsByClassName("new-Discipline_establishment-body_select-mainbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].querySelector(".new-Discipline_establishment-body_item-detail").getAttribute("datacode");
                  }
                  $(obj).find("option").attr("value",setcode);
                  var mainid = document.getElementsByClassName("new-Discipline_establishment-body_select-mainbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim();
                  $(obj).attr("data-showid",mainid);
                  if(document.getElementsByClassName("new-Discipline_establishment-body_select-subbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected").length > 0){
                    var subid = document.getElementsByClassName("new-Discipline_establishment-body_select-subbox")[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim();
                    $(obj).attr("data-subshowid",subid);
                  }
                  if(flag){//如果已经选择了  才显示关键词弹框
                    document.getElementById("hotKeyWord").style.cssText = "display: contents";
                    document.getElementById("hotKeyWord").style.display = "contents";
                  }
                }   
            }
            document.getElementsByClassName("new-Discipline_establishment")[0].style.bottom = "-700px";
            setTimeout(function(){
                document.getElementsByClassName("background-promt")[0].removeChild(document.getElementsByClassName("new-Discipline_establishment")[0]);
                document.body.removeChild( document.getElementsByClassName("background-promt")[0]);
               },500);
            }
        }
}
function getstablishment(xurl,target){
        if ((xurl != "")&&(target!="")) {
	        $.ajax({
	            url : "/scmmanagement/const/ajaxDiscipline",
	            dataType : "json",
	            type:"post",
	            success: function (data){
                    if(document.getElementsByClassName(target).length > 0){
                        for(var i = 0; i < data.discipline_code_1.length ; i++ ){
                            var box = document.createElement("div");
                            box.className = "new-Discipline_establishment-body_item";
                            if (window.locale === "en_US") {
                               var detail = '<span class="new-Discipline_establishment-body_item-count">' + data.discipline_code_1[i].id + '</span>'
				                         +'<div class="new-Discipline_establishment-body_item-detail">'+ data.discipline_code_1[i].en_US_name + '</div>'
				                         +'<i class="material-icons">close</i>';
                            }else{
                            	var detail = '<span class="new-Discipline_establishment-body_item-count">' + data.discipline_code_1[i].id + '</span>'
				                         +'<div class="new-Discipline_establishment-body_item-detail">'+ data.discipline_code_1[i].zh_CN_name + '</div>'
				                         +'<i class="material-icons">close</i>';
                            }
                            box.innerHTML=detail;
                            document.getElementsByClassName(target)[0].appendChild(box); 
    
                           }
                        if((setselectecid !=  undefined )||( subsetselectecid !=  undefined)){
                          var mainshow  =  document.getElementsByClassName("new-Discipline_establishment-body_select-mainbox")[0].getElementsByClassName("new-Discipline_establishment-body_item");
                          for(let i = 0; i < mainshow.length; i++){
                            if(setselectecid == mainshow[i].querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim()){
                               mainshow[i].classList.add("new-Discipline_establishment-body_item-selected");
                                                    
                               getstablishmentsub("new-Discipline_establishment-body_select-subbox",setselectecid);
                            }
                          }
                          
                       }
                            var mainlist =  document.getElementsByClassName(target)[0].getElementsByClassName("new-Discipline_establishment-body_item");
                            for(var i = 0; i < mainlist.length; i++){
                              mainlist[i].onclick = function(){
                                if(document.getElementsByClassName(target)[0].getElementsByClassName("new-Discipline_establishment-body_item-selected").length>0){
                                  if(!this.classList.contains("new-Discipline_establishment-body_item-selected")){
                                     document.getElementsByClassName(target)[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].classList.remove("new-Discipline_establishment-body_item-selected");
                                     this.classList.add("new-Discipline_establishment-body_item-selected");
                                     var getid = this.querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim(); 
                                     document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].innerHTML ="您已经选中" + this.querySelector(".new-Discipline_establishment-body_item-detail").innerHTML;
                                     document.getElementsByClassName("new-Discipline_establishment-body_select-subbox")[0].innerHTML = "";
                                     getstablishmentsub("new-Discipline_establishment-body_select-subbox",getid);
                                     document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].setAttribute("datacode",getid);
                                  }
                                }else{
                                     this.classList.add("new-Discipline_establishment-body_item-selected");
                                     var getid = this.querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim(); 
                                     document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].innerHTML ="您已经选中" + this.querySelector(".new-Discipline_establishment-body_item-detail").innerHTML;
                                     document.getElementsByClassName("new-Discipline_establishment-body_select-subbox")[0].innerHTML = "";
                                     getstablishmentsub("new-Discipline_establishment-body_select-subbox",getid);
                                     document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].setAttribute("datacode",getid);

                                }
                              }
                          } 
                    }
	             }
	       });
    }
};
function getstablishmentsub(target , dataid){
  if ((dataid != "")&&(target!="")) {
    $.ajax({
        url : "/scmmanagement/const/ajaxDiscipline",
        dataType : "json",
        type:"post",
        data: {
          discCode:dataid
        },
        success: function (data){
              if(document.getElementsByClassName(target).length > 0){
                  for(var i = 0; i < data.discipline_code_2.length; i++){
                      var box = document.createElement("div");
                      box.className = "new-Discipline_establishment-body_item";
                      if (window.locale === "en_US") {
                         var detail = '<span class="new-Discipline_establishment-body_item-count">' + data.discipline_code_2[i].id + '</span>'
                           +'<div class="new-Discipline_establishment-body_item-detail">'+ data.discipline_code_2[i].en_US_name + '</div>'
                           +'<i class="material-icons">close</i>';
                      }else{
                        var detail = '<span class="new-Discipline_establishment-body_item-count">' + data.discipline_code_2[i].id + '</span>'
                           +'<div class="new-Discipline_establishment-body_item-detail">'+ data.discipline_code_2[i].zh_CN_name + '</div>'
                           +'<i class="material-icons">close</i>';
                      }
                      box.innerHTML=detail;
                      document.getElementsByClassName(target)[0].appendChild(box);     
                  }
                  
                  if((setselectecid !=  undefined )||( subsetselectecid !=  undefined)){
                    var subshow  =  document.getElementsByClassName("new-Discipline_establishment-body_select-subbox")[0].getElementsByClassName("new-Discipline_establishment-body_item");
                    for(let i = 0; i < subshow.length; i++){
                      if(subshow[i].querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim() == subsetselectecid){
                        subshow[i].classList.add("new-Discipline_establishment-body_item-selected");
                      }
                    }
                  }
                  
                  var sublist = document.getElementsByClassName(target)[0].getElementsByClassName("new-Discipline_establishment-body_item");
                  for(var i = 0; i <sublist.length ; i++){
                    sublist[i].onclick = function(){
                      if(document.getElementsByClassName(target)[0].getElementsByClassName("new-Discipline_establishment-body_item-selected").length>0){
                        document.getElementsByClassName(target)[0].getElementsByClassName("new-Discipline_establishment-body_item-selected")[0].classList.remove("new-Discipline_establishment-body_item-selected");
                        this.classList.add("new-Discipline_establishment-body_item-selected");
                        var getid = this.querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim();
                        document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].innerHTML ="您已经选中" + this.querySelector(".new-Discipline_establishment-body_item-detail").innerHTML;
                        document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].setAttribute("datacode",getid);
                      }else{
                        this.classList.add("new-Discipline_establishment-body_item-selected");
                        var getid = this.querySelector(".new-Discipline_establishment-body_item-count").innerHTML.trim(); 
                        document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].innerHTML ="您已经选中" + this.querySelector(".new-Discipline_establishment-body_item-detail").innerHTML;
                        document.getElementsByClassName("new-Discipline_establishment-body_show-detail")[0].setAttribute("datacode",getid);
                      }
                    }
                 }
              }
          }
      });
  }
}


