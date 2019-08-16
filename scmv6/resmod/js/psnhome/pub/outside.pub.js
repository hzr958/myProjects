/**
 * 站外个人主页-成果模块 相关js
 */
var Pub = Pub ? Pub : {};


//改变url
Pub.changeUrl = function(targetModule) {
	var json = {};
	var oldUrl = window.location.href;
	var index = oldUrl.lastIndexOf("module");
	var newUrl = window.location.href;
	if (targetModule != undefined && targetModule != "") {
		if (index < 0) {
			if(oldUrl.lastIndexOf("?")>0){
				newUrl = oldUrl + "&module=" + targetModule;
			}else{
				newUrl = oldUrl + "?module=" + targetModule;
			}
		} else {
			newUrl = oldUrl.substring(0, index) + "module=" + targetModule;
		}
	}
	window.history.replaceState(json, "", newUrl);
}


//成果模块
Pub.main = function() {
	Pub.changeUrl("pub");
	$.ajax({
		url : '/pub/outside/ajaxmain',
		type : 'post',
		dataType : 'html',
		data : {
			"des3PsnId":$("#outsideDes3PsnId").val()
		},
		success : function(data) {
			if($(".container__horiz").length>0){
				$(".container__horiz").replaceWith(data);	
			}else{
				$(".dev_select_tab").replaceWith(data);	//container__horiz带有样式，影响力用dev_select_tab这个
			}
			$('.oldDiv').remove();
		}
	});
};

//成果列表
Pub.pubList = function() {
	$pubList = window.Mainlist({
		name: "psnpub",
		listurl: "/pub/outside/ajaxlist",
		listdata: {"des3SearchPsnId":$("#outsideDes3PsnId").val(),'isPsnPubs':'1'},
		listcallback: function(xhr){
		    pubaddborder();
		},
		statsurl: "/pub/query/ajaxpsnpubcount"
	});
	if($("#sortBy").val() == "citedTimes"){
		$("div[filter-section='orderBy']").attr("init","ignore");
		$("div[filter-section='orderBy'] li").each(function(){
			if($(this).attr("filter-value") == "citedTimes"){
				$("div.sort-container_header-title").text($(this).find(".sort-container_item_name").text());
				this.classList.add("option_selected");
			}else{
				this.classList.remove("option_selected");
			}
		});
	}
};

//
pubaddborder = function(){
    if(document.getElementsByClassName("fileupload__box-border")){
        var movelist = document.getElementsByClassName("fileupload__box-border");
        for(var i = 0; i < movelist.length; i++){
            movelist[i].onmouseenter = function(){
                if(this.querySelector(".fileupload__box") !=null && this.querySelector(".fileupload__box").classList != null){
                    this.querySelector(".fileupload__box").classList.add("upload_ready");
              }
                /* else{
                    console.log("this.querySelector(\".fileupload__box\").classList == null");
                }*/
            }
            movelist[i].onmouseleave = function(){
                if(this.querySelector(".fileupload__box") !=null && this.querySelector(".fileupload__box").classList != null) {
                    this.querySelector(".fileupload__box").classList.remove("upload_ready");
                }
            }
        }
    }
}
//成果合作者
Pub.cooperator = function() {
	$.ajax({
		url : '/psnweb/outside/ajaxpubcooperator',
		type : 'post',
		dataType : 'html',
		data : {"des3CurrentId":$("#outsideDes3PsnId").val()},
		success : function(data) {
			$('.dev_pub_cooperator').html("");
			$(".dev_pub_cooperator").append(data);
		}
	});
};
//查看全部成果合作者
Pub.outsideCooperatorAll = function() {
	$('#dev_pubcooperator_isall').val("all");
	showDialog("dev_lookall_pubcooperator_back");
	window.Mainlist({
		name : "pubcooperator",
		listurl : "/psnweb/outside/ajaxpubcooperatorAll",
		listdata : {
			"des3CurrentId" : $("#outsideDes3PsnId").val(),
			"isAll" : "1"
		},
		method : "scroll",
		listcallback : function(xhr) {
		}
	});
};
//成果合作者-关闭查看全部弹出层
Pub.closePubCooperatorBack = function() {
	$('#dev_pubcooperator_isall').val("one");
	hideDialog("dev_lookall_pubcooperator_back");
	//Pub.reloadCurrentPubList();
};
//添加联系人
Pub.addFriendReq = function(reqPsnId, obj, first) {
	if (obj) {
		BaseUtils.doHitMore(obj, 1000);
	}
	$.ajax({
		url : "/psnweb/friend/ajaxaddfriend",
		type : 'post',
		data : {
			'des3Id' : reqPsnId
		},
		dataType : 'json',
		timeout : 10000,
		success : function(data) {
			Pub.ajaxTimeOut(data, function() {
				if (data.result == "true") {
					if ($('#dev_pubcooperator_isall').val() == "one") {
						scmpublictoast(pubi18n.i18n_send_success, 1000);
					} else {
						scmpublictoast(pubi18n.i18n_send_success, 1000);
					}
					if (first == true) {
						Pub.cooperator();
					}
				} else if (data.result == "notaddself") {
					scmpublictoast(pubi18n.i18n_addself, 1000);
				} else {
					scmpublictoast(data.msg, 1000);
				}
			});
		},
		error : function() {
		}
	});
};
//查看他人个人主页
Pub.lookOtherPsnHome = function(des3PsnId) {
	window.open("/psnweb/outside/homepage?des3PsnId="+des3PsnId,'target','');
	//window.location.href = "/psnweb/outside/homepage?des3PsnId="+des3PsnId;
};

//站外分享(只是一个标志)
Pub.outsideShare = function() {
	$.ajax({
		url : "/psnweb/timeout/ajaxtest",
		type : "post",
		dataType : "json",
		success : function(data) {
			Pub.ajaxTimeOut(data,function(){});
		}
	});
}
Pub.pubAward = function(){
	$.ajax({
		url : "/psnweb/timeout/ajaxtest",
		type : "post",
		dataType : "json",
		success : function(data) {
			Pub.ajaxTimeOut(data,function(){});
		}
	});
}
//站外成果详情
Pub.pubDetail = function(des3Id) {
	window.open("/pubweb/outside/details?des3Id="+encodeURIComponent(des3Id)+"&currentDomain=/pubweb&pubFlag=1");
};

//超时处理
Pub.ajaxTimeOut = function(data,myfunction){
	var toConfirm=false;
	if('{"ajaxSessionTimeOut":"yes"}'==data){
		toConfirm = true;
	}
	if(!toConfirm&&data!=null){
		toConfirm=data.ajaxSessionTimeOut;
	}
	if(toConfirm){
		jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
			if (r) {
				var url = window.location.href;//  /psnweb/homepage/show
				if (url.indexOf("/psnweb/outside/homepage") > 0) {
					document.location.href = url.replace("/psnweb/outside/homepage","/psnweb/homepage/show");
					return 0;
				} 
			}
		});
	}else{
		if(typeof myfunction == "function"){
			myfunction();
		}
	}
}
Pub.outgetfulltextUrlDownload = function(downLoadUrl){
	/*var downLoadUrl = $("#fullTextDownloadUrl").val();*/
	if(downLoadUrl != null && downLoadUrl != "" && typeof(downLoadUrl) != "undefined"){
		window.location.href=downLoadUrl;
	}
};

//检查基准库成果是否存在
Pub.pdwhIsExist = function(des3PubId,func){
  $.ajax({
    url:"/pub/opt/ajaxpdwhisexists",
    type : 'post',
    dataType:"json",
    async : false,
    data:{"des3PubId":des3PubId},
    timeout : 10000,
    success: function(data){
      Pub.ajaxTimeOut(data,function(){
        if(data.result == 'success'){
          func();
        } else{
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        }
      });
    },
    error: function(data){
      scmpublictoast("系统出错", 1000);
    }
  });
}

//检查基准库成果是否存在
Pub.pdwhIsExist2 = function(des3PubId,func){
  $.ajax({
    url:"/pub/outside/ajaxpdwhisexists",
    type : 'post',
    dataType:"json",
    async : false,
    data:{"des3PubId":des3PubId},
    timeout : 10000,
    success: function(data){
        if(data.result == 'success'){
          func();
        } else{
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        }
    },
    error: function(data){
      scmpublictoast("系统出错", 1000);
    }
  });
}

Pub.showPdwhQuote = function(url, des3PubId, obj){
  $.ajax({
    url:"/pub/outside/ajaxpdwhisexists",
    type : 'post',
    dataType:"json",
    async : false,
    data:{"des3PubId":des3PubId},
    timeout : 10000,
    success: function(data){
        if(data.result == 'success'){
          Pub.showPubQuote(url, des3PubId, obj);
        } else{
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        }
    },
    error: function(data){
      scmpublictoast("系统出错", 1000);
    }
  });
}

Pub.showSnsQuote = function(url, des3PubId, obj){
  $.ajax({
    url:"/pub/outside/ajaxsnsisexists",
    type : 'post',
    dataType:"json",
    async : false,
    data:{"des3PubId":des3PubId},
    timeout : 10000,
    success: function(data){
        if(data.result == 'success'){
          Pub.showPubQuote(url, des3PubId, obj);
        } else{
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        }
    },
    error: function(data){
      scmpublictoast("系统出错", 1000);
    }
  });
}

//成果引用展示
Pub.showPubQuote=function(url,des3PubId,obj){
	var post_data = {"des3PubId":des3PubId};
	//if((document.getElementsByClassName("background-cover").length==0)&&(document.getElementsByClassName("new-quote_container").length==0)){
        var content = '<div class="new-quote_container-header">'
        +'<span class="new-quote_container-header_title">引用</span>'
        +'<i class=" new-quote_container-closetip list-results_close"></i>'
        +'</div>'
        +'<div class="new-quote_container-body">'
        +'</div>'
        +'<div class="new-quote_container-footer">' 
        +'<div class="new-quote_container-footer_close">关闭</div>'
        +'</div>';            
        var container = document.createElement("div");
        container.className = "new-quote_container";
        container.innerHTML = content;
        var box = document.createElement("div");
        box.className  = "background-cover";
        box.appendChild(container);
        document.body.appendChild(box);
        var closeele = document.getElementsByClassName("new-quote_container-closetip")[0];
        var boxele = document.getElementsByClassName("new-quote_container")[0];
        boxele.style.left = (window.innerWidth - boxele.offsetWidth)/2 + "px";
        boxele.style.bottom = (window.innerHeight - boxele.offsetHeight)/2 + "px";
        window.onresize = function(){
          boxele.style.left = (window.innerWidth - boxele.offsetWidth)/2 + "px";
          boxele.style.bottom = (window.innerHeight - boxele.offsetHeight)/2 + "px";
        }
        $.ajax({
          url:url,
          type : 'post',
          dataType:"html",  
          data:post_data,
          success: function(data){
        	  Pub.ajaxTimeOut(data,function(){
        		  $(".new-quote_container-body").append(data);
        		 /* document.getElementsByClassName("new-quote_container-body")[0].appendChild(data);*/
				});
          },
        });   
        
        var closebtn = document.getElementsByClassName("new-quote_container-footer_close")[0];
        closebtn.onclick = function(){
        	container.style.bottom = -600 + "px";
            setTimeout(function(){
                document.body.removeChild(box);
            },200);
        }
        closeele.onclick = function(){
          this.closest(".new-quote_container").style.bottom = -600 + "px";
          setTimeout(function(){
              document.body.removeChild(box);
          },200);
        }
    //} 
};

/**
 * 个人成果请求全文
 * 
 * @author houchuanjie
 */
Pub.requestFullText = function(des3RecvPsnId, des3PubId) {
  if (des3RecvPsnId && des3PubId) {
    $.ajax({
      url : '/pub/fulltext/ajaxreqadd',
      type : 'post',
      data : {
        "des3RecvPsnId" : des3RecvPsnId,
        "des3PubId" : des3PubId,
        'pubType' : 'sns'
      },
      dataType : "json",
      success : function(data) {
        Pub.ajaxTimeOut(data, function() {
          if (data.status == "success") { // 全文请求保存成功
            scmpublictoast(pubi18n.i18n_req_success, 2000);
          } else {
            scmpublictoast(pubi18n.i18n_req_error, 2000);
          }
        });
      },
      error : function() {
        scmpublictoast(pubi18n.i18n_req_error, 2000);
      }
    });
  }
};