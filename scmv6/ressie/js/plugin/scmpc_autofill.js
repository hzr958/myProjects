var scmpcListfilling =function(options){
	var defaults = {
			targetheadersearch:"1",
	        targetleftUrl:"",
	        targetcntUrl:"",
	        formname:""
	    };
	if (typeof options != "undefined") {
		opts=$.extend({},defaults, options);
    }
    var shownum = 1;
    /* 置顶模块 */
    enterTop();
    /* 翻页模块 */
    function Localdisplay(num){
      for(var i = 0; i < num.length; i++){num[i].style.display = "none"; }
      for(var i = 0; i <= 4; i++){ num[i].style.display = "flex"; }
    }

    function Strictdisplay(num,sum){
      for(var i = 0; i < num.length; i++){num[i].style.display = "none";}
      for(var i = 5*(sum-1); i <= (5*sum-1); i++){if(num[i]){ num[i].style.display = "flex";}}// shownum
    } 
   
    if((document.getElementsByClassName("left-file__fill_container").length!=0)
    		&&(document.getElementsByClassName("left-filter_item").length!=0)
    		&&(document.getElementsByClassName("left-filter_list").length!=0)){
        if(opts.targetheadersearch=="1"){
        	document.getElementsByClassName("left-top_search-container")[0].style.display="flex";
        }else{
        	document.getElementsByClassName("left-top_search-container")[0].style.display="none";
        }
        var filltext ='<div class="left_container">' 
             +'<div class="left_container-item">'
              +'<div class="left_container-item_title">'
              +'<div class="left_container-item_title-content">资助机构</div>'
              +'<i class="material-icons left_container-item_title-tip">expand_less</i>'
              +'</div>'
              +'<div class="left_container-search_box">'
              +'<input placeholder="检索" class="left_container-search_input">'
              +'<i class="material-icons left_container-search_tip">search</i>'
              +'</div>'
              +'<div class="left_container-item_subbox">'
              +'<input type="hidden" class="leftPageNum" value="1" />'
              +'<div class="left_container-item_box">'

              +'</div>'
              +'<div class="left_container-item_footer">'
              +'<div class="left_container-item_footer-func left_container-item_footer-down" style="margin-right: 24px;">'
              +'<i class="material-icons">keyboard_arrow_left</i>'
              +'</div>'
              +'<div class="left_container-item_footer-func left_container-item_footer-up">'
              +'<i class="material-icons">keyboard_arrow_right</i>'
              +'</div>'
              +'</div>'
              +'</div>'
              +'</div>'
              +'</div>';
        var boxlist = document.getElementsByClassName("left-filter_list");
        for(var i = 0; i < boxlist.length; i++){
        	var firstinenr = 0;
        	var listitem = 0;
        	var searchtip  = boxlist[i].getAttribute("filter-search");  /* 获取是否显示搜索框的标志位 */
        	var seletortip = boxlist[i].getAttribute("filter-selector");
        	var seletoropen = boxlist[i].getAttribute("filter-open");
        	var seletortitle = boxlist[i].getAttribute("filter-title");
        	var firstinenr = boxlist[i].innerHTML;
        	boxlist[i].innerHTML = filltext;
            boxlist[i].querySelector(".left_container-item_box").innerHTML = firstinenr;
            boxlist[i].querySelector(".left_container-item_title-content").innerHTML = seletortitle;
            if(seletoropen == "close"){
            	boxlist[i].querySelector(".left_container-item_title-tip").innerHTML = "expand_more";
            	boxlist[i].querySelector(".left_container-item_box").style.display="none";
            	boxlist[i].querySelector(".left_container-item_subbox").style.display="none";
            }
            if(searchtip == "hidden"){ 
               boxlist[i].querySelector(".left_container-search_box").style.display="none";
            }
            listitem = boxlist[i].querySelectorAll(".left_container-item_list");
            /* 内容翻页功能模块 */ 
            if(listitem.length <= 5){
            	boxlist[i].querySelector(".left_container-item_footer").style.display="none";
            }else{
            	Localdisplay(listitem); }
            /* 确定选择方式 单选 多选 */
        }

        var listitem = document.getElementsByClassName("left_container-item_list");
        for(var j = 0;j < listitem.length; j++){
            listitem[j].onclick =function(){
                 if(seletortip=="single"){  /* 单选 */
			            if(this.classList.contains("left_container-item_list-back")){
					        this.classList.remove("left_container-item_list-back");
					        this.removeChild(this.querySelector(".material-icons"));
					        this.querySelector(".left_container-item_list-num").style.display="block";
					        var senddata =  getoptions();
					        ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
					        scmpcCntnum(senddata,opts.targetcntUrl);
				        }else{ 
				        	if($(this).closest(".left_container-item_subbox")[0].querySelectorAll(".left_container-item_list-back").length!=0){
				        		$(this).closest(".left_container-item_box")[0].querySelectorAll(".left_container-item_mark")[0].parentNode.removeChild($(this).closest(".left_container-item_box")[0].querySelector(".left_container-item_mark"));
				        		$(this).closest(".left_container-item_box")[0].querySelectorAll(".left_container-item_list-back")[0].querySelector(".left_container-item_list-num").style.display="block";
				        		$(this).closest(".left_container-item_box")[0].querySelectorAll(".left_container-item_list-back")[0].classList.remove("left_container-item_list-back");
				        	}
				            	var textclose = document.createElement("i");
				            	textclose.className = "material-icons left_container-item_mark";
				            	textclose.innerHTML = "close";
				            	textbox = this.querySelector(".left_container-item_list-num");
				            	this.classList.add("left_container-item_list-back");
				                this.querySelector(".left_container-item_list-num").style.display="none";
				                this.appendChild(textclose);
				                var senddata =  getoptions();
				              	ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
				              	var obj = this.children[1];
				              	 scmpcCntnum(senddata,opts.targetcntUrl,obj);
				              	 if(document.getElementsByClassName("examine_totla").length!=0&&(document.getElementById("totalCount")!=null)){
				       			  document.getElementsByClassName("examine_totla")[0].innerHTML = document.getElementById("totalCount").value;
				                 }
	                    }
                 }else{  /* 多选 */
                     if(this.classList.contains("left_container-item_list-back")){
		                 	this.classList.remove("left_container-item_list-back");
		                 	this.removeChild(this.querySelector(".material-icons"));
		                 	this.querySelector(".left_container-item_list-num").style.display="block";
		                 	var senddata =  getoptions();
				            ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
				             scmpcCntnum(senddata,opts.targetcntUrl);
		                }else{		                	
		                 	var textclose = document.createElement("i");
		                 	textclose.className = "material-icons";
		                 	textclose.innerHTML = "close";
		                 	textbox = this.querySelector(".left_container-item_list-num");
		                 	this.classList.add("left_container-item_list-back");
		                    this.querySelector(".left_container-item_list-num").style.display="none";
		                    $(this).append(textclose);
		                    var senddata =  getoptions();
			              	ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
			              	var obj = this.children[1];
			              	scmpcCntnum(senddata,opts.targetcntUrl,obj);
			              	 if(document.getElementsByClassName("examine_totla").length!=0&&(document.getElementById("totalCount")!=null)){
			       			  document.getElementsByClassName("examine_totla")[0].innerHTML = document.getElementById("totalCount").value;
			                 }
		                }
                 }
            }
         }
        scmpcCntnum(getoptions(),opts.targetcntUrl);    /* 对统计数为零的模块进行样式定制 */
        if(document.getElementsByClassName("left-top_search-container")){
        	if(document.getElementsByClassName("left-top_search-container")[0].querySelectorAll(".left_container-search_tip").length!=0){
        		 document.getElementsByClassName("left-top_search-container")[0].querySelector(".left_container-search_tip").onclick = function(){
           	 var senddata =  getoptions();
           	 var searchInput = document.getElementsByClassName("left-top_search-container")[0].querySelector("input").value;
           	 if(searchInput.trim() === "" && searchInput.length ==0){
           	   return;
           	 }
   			     ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
   			     scmpcCntnum(senddata,opts.targetcntUrl);
            }
        	}        	
        }
        if(document.getElementsByClassName("left_container-item_footer-down")){
        	var clickdownlist = document.getElementsByClassName("left_container-item_footer-down");
        	for(var i = 0;i < clickdownlist.length;i++){
        		clickdownlist[i].onclick = function(){
        		   var listitem = $(this).closest(".left_container-item_subbox")[0].querySelectorAll(".left_container-item_list");
        		   var strNum = $(this).closest(".left_container-item_subbox")[0].querySelector(".leftPageNum").value;
        		   var leftSubBoxPageNum = Number(strNum);
        		   var total = Math.ceil(listitem.length/5);
    		       if(leftSubBoxPageNum == 1){
    		    	   return ;
    		       }else{
    		    	   // shownum = shownum - 1;
    		    	   leftSubBoxPageNum = leftSubBoxPageNum -1;
    		    	   $(this).closest(".left_container-item_subbox")[0].querySelector(".leftPageNum").value = leftSubBoxPageNum;
    		    	   if((leftSubBoxPageNum<0)||(leftSubBoxPageNum == 0)){// (shownum<0)||(shownum==0)
    		    		   leftSubBoxPageNum=0;
    		    		   $(this).closest(".left_container-item_subbox")[0].querySelector(".leftPageNum").value = 0;
    		    		   Localdisplay(listitem);
    		    	   }else{
    		    		   Strictdisplay(listitem,leftSubBoxPageNum);// shownum
    		    	   }
    		       }
    		       setNextPageBtColor(leftSubBoxPageNum,total,this);
        		}
        	}
        };
        if(document.getElementsByClassName("left_container-search_input")){
        	var input = document.getElementsByClassName("left_container-search_input")[0];
        	document.onkeydown = function(event){
        		if(document.hasFocus()&&(event.keyCode == 13)&&(input.value!="")){
        			var senddata =  getoptions();
   			        ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
   			        scmpcCntnum(senddata,opts.targetcntUrl);
        		}
        	}
        }
        if(document.getElementsByClassName("left_container-item_footer-up")){
        	var clickuplist = document.getElementsByClassName("left_container-item_footer-up");
        	for(var i = 0;i < clickuplist.length;i++){
        		clickuplist[i].onclick = function(e){
        			e.stopPropagation();
        			e.preventDefault();
        			var listitem = $(this).closest(".left_container-item_subbox")[0].querySelectorAll(".left_container-item_list");
        			var strNum = $(this).closest(".left_container-item_subbox")[0].querySelector(".leftPageNum").value;
        			var leftSubBoxPageNum = Number(strNum);
        			var total = Math.ceil(listitem.length/5);
        			leftSubBoxPageNum = leftSubBoxPageNum+1;
        			$(this).closest(".left_container-item_subbox")[0].querySelector(".leftPageNum").value = leftSubBoxPageNum;
                    // shownum = shownum+1;
                    if((leftSubBoxPageNum>total)||(leftSubBoxPageNum == total)){// (shownum>total)||(shownum==total)
                      leftSubBoxPageNum=total;// shownum
                      $(this).closest(".left_container-item_subbox")[0].querySelector(".leftPageNum").value = leftSubBoxPageNum;
                      Strictdisplay(listitem,leftSubBoxPageNum);// shownum
                    }else{
                      Strictdisplay(listitem,leftSubBoxPageNum);// shownum
                    }
                    setNextPageBtColor(leftSubBoxPageNum,total,this);
        		}
        	}
        }
        /* 内容隐藏于显示模块 */
        if(document.getElementsByClassName("left_container-item_title")){
            var containerlist = document.getElementsByClassName("left_container-item_title");
            for(var i = 0; i < containerlist.length; i++){
            	containerlist[i].onclick = function(e){
            		e.stopPropagation();
        			e.preventDefault();
            		var $containerTip=this.querySelector(".left_container-item_title-tip");
            		if($containerTip){
            			if($containerTip.innerHTML === "expand_less"){
    	                	$containerTip.innerHTML = "expand_more";
    	                  $($containerTip).closest(".left_container-item")[0].querySelector(".left_container-item_box").style.display="none";
    	                  $($containerTip).closest(".left_container-item")[0].querySelector(".left_container-item_subbox").style.display="none";
    	                }else{
    	                	$containerTip.innerHTML = "expand_less";
    	                  $($containerTip).closest(".left_container-item")[0].querySelector(".left_container-item_box").style.display="block";
    	                  $($containerTip).closest(".left_container-item")[0].querySelector(".left_container-item_subbox").style.display="block";
    	                }
            		}
	            }
            }
        }
        autoSearch(this);
    }
}
var scmpcCntnum=function(sentdata,options,obj){
    var targetbox = document.getElementsByClassName("left_container-item_list-value");
    if((options!="")&&(targetbox.length!=0)){
         $.ajax({
            url:options,
          	dataType:"json",
          	beforeSend:function(){
          		// hideLeftContainerItemNum();
          		hideClickedLeftContainerItemNum(obj);
    		},
          	data:sentdata,
          	success: function(data){
          		if (data.ajaxSessionTimeOut == "yes" || data =="{\"ajaxSessionTimeOut\":\"yes\"}") {
          			ScmLoginBox.showLoginToast();
                } else {
                	var detailist = document.getElementsByClassName("left_container-item_list-detail");
              		for(var i = 0; i < detailist.length;i++){
              			detailist[i].innerHTML="0";
              		}
              		var ligotip = document.getElementsByClassName("left-filter_list");
              		for(var z = 0; z < ligotip.length; z++ ){
              			var ligoId = ligotip[z].getAttribute("filter-id");
                  		for(var key in data){
                  			var innerbox = ligotip[z].querySelectorAll(".left_container-item_list-value");
                  			if(key == ligoId){
                  				for(var tip in data[key]){
                  					for(var j = 0; j < innerbox.length;j++ ){	
                          				if(tip.trim() == innerbox[j].getAttribute("value").trim()){
                          					$(innerbox[j]).closest(".left_container-item_list")[0].querySelector(".left_container-item_list-detail").innerHTML= data[key][tip];
                          				}           
                          			}
                  					
                  				}
                  			}
                  		}
                  		// 显示内容
                  		var leftContainerItemNumList = ligotip[z].getElementsByClassName("left_container-item_list-num");
                  		for(var j = 0; j < leftContainerItemNumList.length; j++ ){
                  			if(leftContainerItemNumList[j] == obj){
                  				leftContainerItemNumList[j].style.display="none";
                  			}else{
                  				var $currentLeftContainerItemNum = $(leftContainerItemNumList[j]);
                  				if (!($currentLeftContainerItemNum.parent().hasClass("left_container-item_list-back"))) {
                  					leftContainerItemNumList[j].style.display="block";
                  				}
                  			}
                  		}
              		}
        			var listitem = document.getElementsByClassName("left_container-item_list");
    			        for(var i = 0; i < listitem.length;i++){
    			            if(listitem[i].querySelector(".left_container-item_list-detail").innerHTML=="0"){
    			                listitem[i].style.pointerEvents="none";
    			                listitem[i].querySelector(".left_container-item_list-content").style.color="rgba(0,0,0,0.24)";
    			                listitem[i].style.color="rgba(0,0,0,0.24)";
    			              }else{
    			            	listitem[i].style.pointerEvents="auto";
    			            	listitem[i].querySelector(".left_container-item_list-content").style.color="#333";
    			            	listitem[i].style.color="rgba(0,0,0,0.87)";
    			              }
    			        }
                }
          	},
        });
    }
}
var getoptions = function(){
    var sendfile={};
    if(document.getElementsByClassName("left_container-item_list-back").length!=0){
		var fillist = new Array();
        if(document.getElementsByClassName("left-filter_item").length!=0){
        	 var inlist =  document.getElementsByClassName("left-filter_item")[0].querySelectorAll(".left-filter_list");
             if(inlist.length>0){
        	      for( var j = 0; j < inlist.length; j++){
        	    	  fillist=[];
        	    	  var inkey = inlist[j].getAttribute("filter-id");
        	    	  var selectorlist = inlist[j].querySelectorAll(".left_container-item_list-back");
        	    	  if(selectorlist.length!=0){
            	    	  for(var i = 0; i < selectorlist.length; i++){
            	    		  var invalue =  selectorlist[i].querySelector(".left_container-item_list-value").getAttribute("value");
            	    		  fillist.push(invalue);
            	    	  }  
            	    	  var text = fillist.join(",").trim();
          	              sendfile[""+inkey+""]=text; 
        	    	  }
        	      }
        	     
             } 
	    }
	}
    if(document.getElementsByClassName("left-top_search-container")){    	
    	if(document.getElementsByClassName("cls_search_string").length!=0){
			sendfile["searchKey"] = document.getElementsByClassName("cls_search_string")[0].value;
		}else{
			sendfile["searchKey"]= document.getElementsByClassName("left-top_search-container")[0].querySelector("input").value;
		}
    	
    }
    return sendfile;
}

/* 通信模块 */
function ajaxdata(senddata,dataurl,callback){
	$.ajax({
	    url:dataurl,
	    dataType:"html",	
	    data:senddata,
	    beforeSend:function(){
	    	$(".right-content_container").doLoadStateIco({
	             style:"height:28px; width:28px; margin:auto;margin-top:100px;",
	             status:1
	         });
		},
	    success: function(data){
	    	if (data =="{\"ajaxSessionTimeOut\":\"yes\"}") { // ROL-4871 ztg
	    		ScmLoginBox.showLoginToast();
			} else {
	    	  $(".right-content_container").html(data);
			  reloadcheck();		  
			  if(document.getElementsByClassName("examine_totla").length!=0&&(document.getElementById("totalCount")!=null)){
				  document.getElementsByClassName("examine_totla")[0].innerHTML = document.getElementById("totalCount").value;
	          } 
			  if(dataurl == '/psnweb/person/ajaxpsnlist'  || dataurl == '/psnweb/person/public/ajaxlist'){
				  changePsnPositionHeight();
			  }else{
				  changePositionHeight();
			  }
			  changeStatisticsNum();
	      }
	    },
	    error: function(){
		   callback;
	    }
     });
}
function reloadcheck(){
    if(document.getElementsByClassName("checklist")){
    	   var  selectlist = document.getElementsByClassName("checklist"); 
     	   for(var i = 0 ;i < selectlist.length;i++){
    		 selectlist[i].onclick = function(){
    			 if(this.classList.contains("cur")){
     				 this.classList.remove("cur");
     				 if(document.getElementById("selectAll").classList.contains("cur")){
     					document.getElementById("selectAll").classList.remove("cur");
     				 }
     				 
       				 $(this).closest(".check_fx")[0].querySelector("input").removeAttribute("checked");
     			 }else{
    				 this.classList.add("cur");
    				 $(this).closest(".check_fx")[0].querySelector("input").setAttribute("checked", "checked");
     			 }     
    			 }
     	 }
       }
	if(document.getElementById("selectAll")&&(document.getElementsByClassName("checklist"))){
		var selectlist = document.getElementsByClassName("checklist")
		document.getElementById("selectAll").onclick = function(){
			if(this.classList.contains("cur")){
				this.classList.remove("cur");
				for(var i = 0; i < selectlist.length;i++){
					if(selectlist[i].classList.contains("cur")){
						selectlist[i].classList.remove("cur");
						$(selectlist[i]).closest(".check_fx")[0].querySelector("input").removeAttribute("checked");
					}
				}
			}else{
				this.classList.add("cur");
				for(var i = 0; i < selectlist.length;i++){
					if(!selectlist[i].classList.contains("cur")){
						selectlist[i].classList.add("cur");
						$(selectlist[i]).closest(".check_fx")[0].querySelector("input").setAttribute("checked", "checked");
					}
				}
			}
		};
	}

    
}

/**
 * 调整列表标题高度-项目、成果、专利
 * 
 * @returns
 */
function changePositionHeight(){
	var spanArray = document.getElementsByClassName("multipleline-ellipsis__content-box");
	var pArray = document.getElementsByClassName("sie_lsit_p3");
    for(var i=0; i<pArray.length; i++){
    	if(spanArray[i].clientHeight == 48){
    		continue;
    	}
        if(spanArray[i].clientHeight < 48){
            document.getElementsByClassName("sie_lsit_p3")[i].style.height = "24px";
        }
    }
}

/**
 * 调整人员列表的人名和职称的高度
 * 
 * @returns
 */
function changePsnPositionHeight(){
	var spanArray = document.getElementsByClassName("multipleline-ellipsis__content-box");
    var pArray = document.getElementsByClassName("sie_lsit_p2");
    for(var i=0; i<pArray.length; i++){
        if(spanArray[i].clientHeight < 49){
            document.getElementsByClassName("sie_lsit_p2")[i].style.cssText = "width:600px;";
        	// document.getElementsByClassName("sie_lsit_p2")[i].style.height = "24px";
        }
    }
}

/**
 * 换算列表右侧统计数
 * 
 * @returns
 */
function changeStatisticsNum(){
    var numDiv = document.getElementsByClassName("change_num");
    for(var i=0; i<numDiv.length; i++){
    	for(var j=0; j<numDiv[i].childElementCount; j++){// children.length
    		var statNum = numDiv[i].children[j].innerText;
    		if(statNum != null && statNum != "" && statNum != undefined && statNum.search("k") == -1){
				var changeStatNum = changeNumToThousandChar(statNum);
				numDiv[i].children[j].innerText = changeStatNum;
    		}
    	}
    }
}

/**
 * 列表右侧统计数的显示数量
 * 
 * @param num
 * @returns
 */
function changeNumToThousandChar(num){
	if(num <= 999){
		return num;
	}else if(num > 999 && num <= 9999){
		var numArr = num.split('');
		// 百位
		var hunDigit = numArr[numArr.length - 3];
		// 千位
		var thouDigit = numArr[numArr.length - 4];
		var changeNum = "";
		// 判断百位是否为0
		if(hunDigit != 0){
			return changeNum = thouDigit + "." + hunDigit + "k";
		}else{
			return changeNum = thouDigit + "k";
		}
	}else if(num > 9999){
		return "9.9k";
	}
}

function timeOutReload (tag) {
	document.location.href = "/insweb/index";
}

function timeOutCancelcallback () {
	return;
}

/**
 * 定义事件移除方法和时间添加方法，避免在初始化时重复绑定
 * 
 * @method addSpecificEventListener 添加一个监听事件
 * @method removeSpecificEventListener 移除一个监听事件，在addSpecificEventListener前使用
 */
var $EventArray = []; // 定义一个事件集合数组

/**
 * 添加一个监听事件
 * 
 * @param {HTMLElemnt}
 *          o 所需要绑定事件的HTMLElement对象
 * @param {String}
 *          evt 事件类型名称
 * @param {String}
 *          fname 方法名称
 */
function removeSpecificEventListener(o, evt, fname) {
	// 遍历数组，找到事件属性相同的数组元素，并移除相关方法
	$EventArray.forEach(function (x, idx) {
		if (x.node === o && x.eventType === evt && x.functionName === fname) {
			o.removeEventListener(evt, x.function);
			$EventArray.splice(idx, 1);
		}
	});
}

/**
 * 添加一个监听事件
 * 
 * @param {HTMLElemnt}
 *          o 所需要绑定事件的HTMLElement对象
 * @param {String}
 *          evt 事件类型名称
 * @param {String}
 *          fname 方法名称
 * @param {Function}
 *          f 定义的具体方法
 */

function addSpecificEventListener(o, evt, fname, f) {
	removeSpecificEventListener(o, evt, fname); // 添加事件之前也移除，避免重复绑定
	const $object = {};
	$object.node = o;
	$object.eventType = evt;
	$object.functionName = fname;
	$object.function = f;
	o.addEventListener(evt, f);
	$EventArray.push($object); // 每添加一个监听事件就在数组中添加这个事件的一些属性，方便之后移除
}
/**
 * 隐藏左侧菜单栏的数目，加载完以后再显示
 */
function hideLeftContainerItemNum(){
	var initLeftContainerItemNumList = document.getElementsByClassName("left_container-item_list-num");
	for(var ij = 0; ij < initLeftContainerItemNumList.length; ij++ ){
		initLeftContainerItemNumList[ij].style.display="none";
	}
}

/**
 * 发送统计请求前，隐藏用户点击的统计条目
 * 
 * @param obj
 * @returns
 */
function hideClickedLeftContainerItemNum(obj){
	var ligotip = document.getElementsByClassName("left-filter_list");
	for(var z = 0; z < ligotip.length; z++ ){
		var leftContainerItemNumList = ligotip[z].getElementsByClassName("left_container-item_list-num");
		for(var j = 0; j < leftContainerItemNumList.length; j++ ){
			if(leftContainerItemNumList[j] == obj){
				leftContainerItemNumList[j].style.display="none";
			}else{
				var $currentLeftContainerItemNum = $(leftContainerItemNumList[j]);
  				if (!($currentLeftContainerItemNum.parent().hasClass("left_container-item_list-back"))) {
  					leftContainerItemNumList[j].style.display="block";
  				}
			}
		}
	}
}

/**
 * 自动搜索 参考SNS-Mainlist.prototype.bindSearchEvents
 * 
 * @param even
 * @returns
 */
function autoSearch(even){
	const $self = this;
	const search_box = (document.getElementsByClassName("left_container-search_box left-top_search-container").length==1)?
			document.getElementsByClassName("left_container-search_box left-top_search-container")
			:document.getElementsByClassName("search")
			;
	if (search_box.length > 1) {
		throw "One list can only have one search field.";
	}
	if (search_box && search_box[0]!=undefined) {
		const $searchInput = search_box[0].getElementsByTagName("input")[0];
		// 为输入中文定义一个开关变量
		var $inputLock = false;
		const scanChart = function (tempstr) {
			if(document.getElementsByClassName("cls_search_string").length!=0){
				$(".cls_search_string")[0].setAttribute("value",tempstr);
				document.getElementsByClassName("cls_search_string")[0].value=tempstr;
			}
		}
		const startEvent = function () {
			$inputLock = true;
		};
		const endEvent = function () {
			$inputLock = false;
			scanChart($searchInput.value.trim());
			$searchInput.setAttribute("search-string", $searchInput.value.trim());
			// 如果字符串已经没有检索到信息，那么包含这个字符串的其他字符串也不会收到信息，减少请求，下同
			if (!($searchInput.value.trim().indexOf($searchInput.getAttribute("search-string")) >= 0 
					&& parseInt($searchInput.getAttribute("total-count")) === 0)) {
				var senddata =  getoptions();
		        ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
		        scmpcCntnum(senddata,opts.targetcntUrl);
			}
		};
		const inputEvent = function (e) {
			// 为空就不要在查询了 解决IE 浏览器问题 SCM-13951
			if($searchInput.value.trim() ===""
				&&  (  $searchInput.getAttribute("search-string") ===""  || $searchInput.getAttribute("total-count")===null )
				&& $searchInput.value.length ==0
			){
				return ;
			}
			if (!$inputLock) {
				scanChart($searchInput.value.trim());
				$searchInput.setAttribute("search-string", $searchInput.value.trim());
				if (!($searchInput.value.trim().indexOf($searchInput.getAttribute("search-string")) >= 0 && parseInt($searchInput.getAttribute("total-count")) === 0)) {
					var senddata =  getoptions();
					ajaxdata(senddata,opts.targetleftUrl,opts.targetcllback);
			        scmpcCntnum(senddata,opts.targetcntUrl);
				}
			}
		};
		addSpecificEventListener($searchInput, "compositionstart", "startEvent", startEvent);
		addSpecificEventListener($searchInput, "compositionend", "endEvent", endEvent);
		addSpecificEventListener($searchInput, "input", "inputEvent", inputEvent);
	}
}

/**
 * 设置翻页按钮颜色
 * 
 * @param thePageNo
 *          当前页
 * @param totlePageNo
 *          总页数
 * @param theBt
 *          当前对象
 */
function setNextPageBtColor(thePageNo,totlePageNo,theBt){
	var leftBt=$($(theBt).closest(".left_container-item_subbox")[0]).find(".left_container-item_footer-func")[0];
	var rightBt=$($(theBt).closest(".left_container-item_subbox")[0]).find(".left_container-item_footer-func")[1];
	if(thePageNo==1&&totlePageNo==1){
		// 只有一页时,无法翻页
		removeScollPageClass(leftBt,rightBt);
		$(leftBt).addClass("left_container-item_footer-down");
		$(rightBt).addClass("left_container-item_footer-down");
	}else if(thePageNo==1&&totlePageNo>1){
		// 第一页时
		removeScollPageClass(leftBt,rightBt);
		$(leftBt).addClass("left_container-item_footer-down");
		$(rightBt).addClass("left_container-item_footer-up");
	}else if(thePageNo>1&&totlePageNo>1&&thePageNo<totlePageNo){
		// 即不是第一页，也不是最后一页时
		removeScollPageClass(leftBt,rightBt);
		$(leftBt).addClass("left_container-item_footer-up");
		$(rightBt).addClass("left_container-item_footer-up");
	}else if(thePageNo!=1&&totlePageNo>1&&thePageNo==totlePageNo){
		// 最后一页时
		removeScollPageClass(leftBt,rightBt);
		$(leftBt).addClass("left_container-item_footer-up");
		$(rightBt).addClass("left_container-item_footer-down");
	}
	
}
function removeScollPageClass(leftBt,rightBt){
	$(leftBt).removeClass("left_container-item_footer-down");
	$(leftBt).removeClass("left_container-item_footer-up");
	$(rightBt).removeClass("left_container-item_footer-down");
	$(rightBt).removeClass("left_container-item_footer-up");
}
// 返回顶部的按钮和事件
function enterTop(){
	$(window).scroll(function() {
		if ($(window).scrollTop() >= 600) {
			if($(".back_top").length>0){
				$(".back_top").show(100);
			}else if($(".back_top").length==0){
				$("body").append("<a href=\"#\" class=\"back_top\" style=\"display: none;\"></a>");
			}
		}
		else{
			$(".back_top").hide(100);
		}
	});
}