var project = project?project:{};

// 项目列表使用的分享
project.itemShare =function(prjIds,ev){
	var $this=$(ev.currentTarget);
	// 加载弹窗插件
	$this.dynSharePullMode({
 		'groupDynAddShareCount':"",
 		'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)' ,
 		'language':locale
 	});
  // 每次定位到分享至联系人
  $("li[onclick='SmateShare.shareToFriendUI()']").click();
  $("li[onclick='SmateShare.shareToFriendUI()']").addClass("nav__item-selected item_selected");
  $("li[onclick='SmateShare.shareToDynUI()']").removeClass("nav__item-selected item_selected");
  $("li[onclick='SmateShare.shareToGrpUI()']").removeClass("nav__item-selected item_selected");
	// 收集参数
	var des3ResId = prjIds;
	$("#share_to_scm_box").attr("des3ResId",des3ResId).attr("resType", "4");
	var content = $this.closest(".main-list__item").find(".pub-idx__main_title").text();
	var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
	$parent.html("");
	SmateShare.createFileDiv($parent, content, des3ResId);
	// 初始化页面
	/* $("#share_to_scm_box").find(".nav__list").find(".nav__item:first").click(); */
 	$("#id_bt_select_grp").hide();
 /* $("#id_bt_select_friend").hide(); */
	
}

// 项目主页
project.ajaxShow =function(des3CurrentId){
	project.changeUrl("prj");
	$.ajax({
		url:'/prjweb/project/ajaxshow',
		type : 'post',
		data: {'des3CurrentId' :des3CurrentId},
		dataType:'html',
		success : function(data) {
		    BaseUtils.ajaxTimeOut(data,function(){
				$(".dev_psnhome_back").remove();
				if($(".container__horiz").length>0){
					$(".container__horiz").replaceWith(data);	
				}else{
					$(".dev_select_tab").replaceWith(data);	// container__horiz带有样式，影响力用dev_select_tab这个
				}
			});
		}
	});
}

// 项目列表
project.ajaxPrjList = function(des3CurrentId){
	/* var url=history.back(-1); */
	/*
   * if(url.indexOf("/prjweb/prj/edit")){ $("div[list-main='prjlist']").attr("memlist","display"); }
   */
	
	if($("#frompage").val()=='prjEdit'){
		$("div[list-main='prjlist']").attr("memlist","display");
	}
	$prjlist=window.Mainlist({
		name:"prjlist",
		listurl: "/prjweb/show/ajaxprjlist",
		listdata: {'des3CurrentId':des3CurrentId},
		listcallback: function(){
			// 排序条件的header显示，是gzl另外实现的，所以在回调这实现即可。 获取选中的排序，更新到显示
			$("li.sort-container_item-list").each(function(x){
				if(this.classList.contains("option_selected")){
					var sortName = $(this).find(".sort-container_item_name").text();
					$("div.sort-container_header-title").text(sortName);
				}
			});
			$("div[filter-section=fundingYear] li").each(function(){
				/*
         * if(this.indexOf(".option_selected")=="-1"){
         * $(".filter-list__section[filter-section=fundingYear] .filter-value__list").hide(); }
         */
			    if(this.classList.contains("option_selected")){
			    	$(".filter-list__section[filter-section=fundingYear] .filter-value__list").show();
			    }
		      });
		},
		statsurl:"/prjweb/show/ajaxprjlistcallback",
		drawermethods:locale == "zh_CN"?{"删除项目" :function(a){
			if(a.length>0){
				var prjIds ="";
				for(var i=0;i<a.length;i++){
					prjIds+=a[i]+",";
				}
				project.delete(prjIds);
			}
		}
	   }:{"Delete" :function(a){
			if(a.length>0){
				var prjIds ="";
				for(var i=0;i<a.length;i++){
					prjIds+=a[i]+",";
				}
				project.delete(prjIds);
			}
		}}
	   
	})
	
}
// 未上传全文的项目列表
var noprjfulltext;
project.ajaxNoFulltextPrjList = function(des3CurrentId,noPrjFulltextList){
	showDialog("noprjfulltextlist");
	noprjfulltext = window.Mainlist({
		name:"noprjfulltextlist",
		listurl: "/prjweb/show/ajaxprjlist",
		listdata: {'des3CurrentId':des3CurrentId,"noPrjFulltextList":noPrjFulltextList},
		method: "scroll",
		listcallback: function(xhr){

			var  $prjnofull = $("div[class='dialogs__box'][dialog-id='noprjfulltextlist']").first();
			$prjnofull .find(".dialogs__childbox_adapted   .main-list__item").each(function(){
					      var $this=$(this);
					      var dataJson = {
					    		'allowType':'*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;',
					  			'jsessionId':jsessionId,
					  			'des3PrjId':$this.attr("id")
					  			};
					      var filetype = ".txt;.jpg;.jpeg;.gif;.pdf;.doc;.docx;.png;.html;.xhtml;.htm;.rar;.zip;".split(";");
					      var data ={
									"fileurl": "/scmwebsns/archiveFiles/ajaxSimpleUpload",
									"filedata":dataJson ,
									"filecallback":project.prjUpdownFulltext,
									"filetype":filetype
								};
					   
					      fileUploadModule.initialization(this, data);
					});
			
		},
	});
};
// 上传文件保存信息
project.prjUpdownFulltext =function(xhr){
	/* var des3prjId=$("#des3Ids").val(); */
	const data = eval('('+xhr.responseText+')');
	var des3prjId = data.des3PrjId;
	var fileName =  data.fileName;
	var fileExt = fileName.substring(fileName.lastIndexOf("."));
	var fileDate = data.createTime;
	var file_id = data.fileId;
	if(file_id == ""){
		return;
	}
	dataJson={'des3Id':des3prjId,'fileName':fileName,'fulltextExt':fileExt,'fileDate':fileDate,'fulltextField':file_id};
	$.ajax({
		 url:"/prjweb/project/ajaxdatefulltext", 
		 type:'post',
		 dataType:'json',
		 data:dataJson,
		 success : function(data) {
			 noprjfulltext.listdata.fulltextCount = document.querySelector('*[list-main="noprjfulltextlist"]').getElementsByClassName("fileupload__box upload_finished").length;
			 project.ajaxTimeOut(data,project.noFulltextRemind)  ;
		}
	});
}

// 项目全文下载
project.download =function(fdesId){
	
	location.href='/scmwebsns/archiveFiles/downLoadNoVer.action?fdesId='+fdesId+'&nodeId=1';
}

// 项目合作者列表
project.ajaxprjcooperation =function(des3CurrentId){
	$.ajax( {
		url : '/psnweb/ajaxprjcooperation',
		type : 'post',
		data:{'des3CurrentId':des3CurrentId},
		dataType:'html',
		success : function(data) {
			/*
       * p=document.getElementById('prjcooperation'); p.innerHTML=data;
       */
			/* $("#prjcooperation").prepend(data); */
			p=$("#prjcooperation");
			p.html(data);
		},
		error: function (){
		}
	}); 
	
}
// 可能认识的人
project.ajaxKnowFriend =function(){
	$.ajax( {
		url : '/psnweb/friend/ajaxknownew',
		type : 'post',
		dataType:'html',
		success : function(data) {
			/* $("#knownewfriend").append(data); */
			p=document.getElementById('knownewfriend');
			p.innerHTML=data;
		},
		error: function (){
		}
	}); 
	
}
project.recommendList =function (des3CurrentId){
	$.ajax({
		url: "/prjweb/show/ajaxagencyname",
		type: "post",
		data:{'des3CurrentId':des3CurrentId},
		dataType: "json",
		success: function(data){
			project.ajaxTimeOut(data, function(){
				   var agencyNamesHtml = "";
					if(data != null){
						for(var i=0; i<data.length; i++){
							agencyNamesHtml += '<li class=\"filter-value__item js_filtervalue\" filter-value=\"' +data[i].agencyName+ '\"><div class=\"input-custom-style\"> <input type=\"checkbox\"> <i class=\"material-icons custom-style\"></i> </div> <div class=\"filter-value__option js_filteroption\" title=\"'+data[i].agencyName+'\">' + data[i].agencyName +'</div>  <div class=\"filter-value__stats js_filterstats\">(0)</div> <i class=\"material-icons filter-value__cancel js_filtercancel\">close</i> </li>';
						}
					}
					agencyNamesHtml += '<li class=\"filter-value__item js_filtervalue\" filter-value=\"' +'other_agency_name'+ '\"><div class=\"input-custom-style\"> <input type=\"checkbox\"> <i class=\"material-icons custom-style\"></i> </div> <div class=\"filter-value__option js_filteroption\" title=\"'+homepage.otherAgency+'\">' + homepage.otherAgency +'</div>  <div class=\"filter-value__stats js_filterstats\">(0)</div> <i class=\"material-icons filter-value__cancel js_filtercancel\">close</i> </li>';
					$("#agencyNames").html(agencyNamesHtml);
					project.ajaxPrjList(des3CurrentId);
			});
			
		},
		error: function(){
			
		}
	});
}
/**
 * 项目进入群组 -- 已废弃，改为a标签href跳转。CQ：SCM-12923
 * 
 * @author houchuanjie
 * @date 2017-7-11
 */
project.enterGroup=function(des3GrpId){
	location.href='/groupweb/grpinfo/main?des3GrpId='+des3GrpId;
}
// 项目编辑
project.editor=function(des3PrjId){ 
  des3PrjId = encodeURIComponent(des3PrjId);
  var forwardUrl = '/prjweb/prj/edit?des3Id='+des3PrjId+'&backType=0';
  BaseUtils.forwardUrlRefer(true, forwardUrl);
}
// 全文提醒
project.noFulltextRemind= function(){
	$.ajax({
		url:'/prjweb/remind/ajaxfulltext',
		type:'post',
		dataType : "json",
		success : function(data) {
			if("en"==data[1]){
				$("#prjfulltext").text(data[0]+" of your projects don't have full-texts.");
			}else{
				$("#prjfulltext").text("你有"+data[0]+"个项目未上传全文");
			}
			if(data[0]!=0){
				$(".promo__box").show();
			}else{
				$(".promo__box").hide();
			}
	    }	
		
	})
}
// 项目数和项目金额数
project.prjNumberAmount= function(des3CurrentId){
	$.ajax({
		url:'/prjweb/remind/ajaxamount',
		type:'post',
		data:{'des3CurrentId':des3CurrentId},
		dataType : "json",
		success : function(data) {
			if(data!=null){
				var numbers=data.amounts;
				if(numbers!=0 && data.locale=='zh'){
					/* numbers = numbers.toFixed(2); */
					var  remainder=numbers%10000;
					remainder=remainder/10000;
					remainder=remainder.toString();
					remainder=remainder.substring(1);
					var   numbers =parseInt(numbers/10000);
					numbers = parseFloat(numbers);
				    numbers = numbers.toLocaleString()+remainder;
				}else if(numbers!=0 && data.locale=='en'){
					var  remainder=numbers%1000;
					remainder=remainder/1000;
					remainder=remainder.toString();
					remainder=remainder.substring(1);
					var   numbers =parseInt(numbers/1000);
					numbers = parseFloat(numbers);
				    numbers = numbers.toLocaleString()+remainder;
				}
				$("#prjNumbers").text(data.prj);
				$("#prjTotalAmounts").text(numbers +homepage.unit);
			}
	    }	
		
	})
}
// 项目删除
project.ajaxDelete = function(prjIds){
  if($("#othersSee").val()=='true'){
    scmpublictoast(prji18n.i18n_onPermissions,1000);
    return;
  }
  $.ajax({
    url:'/prjweb/project/ajaxDelete',
    type:'post',
    dataType : "json",
    data : {"prjIds" : prjIds},
    success : function(data) {
      project.ajaxTimeOut(data,function(){
        if(data.result=='success'){
          $prjlist.reloadCurrentPage();
          $prjlist.drawerRemoveSelected(data.delDes3PrjIds);
          project.noFulltextRemind();
          scmpublictoast(data.msg,1000);
        } 
        else{
          scmpublictoast(data.msg,1000);
        }
        var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
        if($(".new-searchplugin_container")){
            $(".background-cover").remove();
            $(".new-searchplugin_container").remove();
         }
      });    
      } 
  })
}
// 项目删除
project.delete =function(prjIds){
  smate.showTips._showNewTips(prji18n.i18n_delete_content, prji18n.i18n_delete_title, "project.ajaxDelete('"+prjIds+"')", "",
      prji18n.choose_confirm_btn, prji18n.choose_cancel_btn);
}
// 合作者查看全部
project.ajaxprjallcooperation =function(des3CurrentId){ 
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        showDialog("prjCooperation");
        window.Mainlist({
            name:"prjcooperationlist",
            listurl: "/psnweb/ajaxprjcooperation",
            listdata: {'firstPage':false,'des3CurrentId':des3CurrentId},
            listcallback: function(){
                /* $("#kownNewPsn").append(data); */
            },
            method: "scroll",
        });
    }, 1);
}
// 关闭 合作者查看全部的弹出框
project.hideAllcooperation =function(){ 
	hideDialog("prjCooperation");
	
}

var kownNewPsnlist;
// 可能认识的人查看全部
project.ajaxAllKnowFriend =function(totalCount){
	showDialog("kownAllPerson");
	kownNewPsnlist = window.Mainlist({
		name:"kownNewPsnlist",
		listurl: "/psnweb/friend/ajaxknownew",
		listdata: {'firstPage':false},
		listcallback: function(){
			/* $("#kownNewPsn").append(data); */
			/* showDialog("kownAllPerson"); */
		},
		method: "scroll",
	})
	
} 
project.hideKownAllPsn =function(){
	$('.dev_may_count').val(0);
	hideDialog("kownAllPerson");
	$prjlist.reloadCurrentPage();
	$prjlist.initializeDrawer();
}
project.getjson = function(){
	var $valueBox=document.querySelector('.sel-dropdown__box[selector-data="1st_discipline"]');
	if (!$valueBox.querySelector('*[selected="selected"]')){
		return {};
	} else {
		return {"fCategoryId":$valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue")}
	}
}
project.addFriendReq =function(reqPsnId,psnId,know,obj){
	if(obj){
		BaseUtils.doHitMore(obj,1000);
	}
	$.ajax({
		url:'/psnweb/friend/ajaxaddfriend',
		type:'post',
		data:{'des3Id':reqPsnId},
		dataType:'json',
		timeout:10000,
		success:function(data){
			project.ajaxTimeOut(data,function(){
				if (data.result == "true") {
					scmpublictoast(homepage.addFriend,1000);
					    if(know==true){
// $("#know_"+psnId).remove();
					    	$("#know_"+psnId).hide();
					    	$('.dev_may_count').val(Number($('.dev_may_count').val())+1);
					    	kownNewPsnlist.listdata.mayKnowPsnCount = $('.dev_may_count').val();
					    }
					    else{
					    	$(".kownMore").remove();
					    	$.ajax( {
								url : '/psnweb/friend/ajaxknownew',
								type : 'post',
								dataType:'html',
								success : function(data) {
									$("#knownewfriend").append(data);
								},
								error: function (){
								}
							}); 
					    }
				}else if(data.result == "false"){
					scmpublictoast(data.msg,1000);
				}
			});
			    
		},
		error:function(){
			scmpublictoast(prji18n.i18n_error,1000);
		}
	});
}
// 项目合作者里面添加联系人
project.addFriendCooper=function(reqPsnId,psnId,Cooper,obj,first){
	if(obj){
		BaseUtils.doHitMore(obj,1000);
	}
	$.ajax({
		url:'/psnweb/friend/ajaxaddfriend',
		type:'post',
		data:{'des3Id':reqPsnId},
		dataType:'json',
		timeout:10000,
		success:function(data){
			project.ajaxTimeOut(data,function(){
				if (data.result == "true") {
					scmpublictoast(homepage.addFriend,1000);
					if(first==true){
						project.ajaxprjcooperation($("#des3CurrentId").val());
					}	
				} else if(data.result == "notaddself") {
					scmpublictoast(prji18n.i18n_addself,1000);
				} else{
					scmpublictoast(data.msg,1000);
				}
				
			});    
		},
		error:function(){
			scmpublictoast(prji18n.i18n_error,1000);
		}
	});
}
/*
 * //显示添加项目 project.addprjFile = function(){ alert("1");
 * showDialog("select_import_grp_file_method"); }
 */
/*
 * //关闭添加项目弹出框 project.closeSelectPrjFileMethod = function(){
 * hideDialog("select_import_grp_file_method"); }
 */
// 创建群组
// 确定创建群组操作
project.doGrpBaseCreate = function(){
	
	var obj_ui = $("#create_grp_ui");
	var grpName= obj_ui.find(".input_grpname_val").val();
	var prjId =$("#prjIds").val();
	/* $("#prjIds").val(""); */
	if($.trim(grpName)==""){
		scmpublictoast(homepage.notPrjTitle,1000);
		return 0;
	}
	var grpDescription= obj_ui.find(".input_grpdescription_val").val();
	if($.trim(grpDescription)==""){
		scmpublictoast(homepage.notPrjAbstract,1000);
		return 0;
	}
	var projectNo= obj_ui.find(".input_projectno_val").val();
	var openType= obj_ui.find("input[name='choose-authority']:checked").val();
	if($.trim(openType)==""){
		scmpublictoast(homepage.notPrjPrivacy,1000);
		return 0;
	}
	var keywords = project.getGrpCreateKeywords();
	var firstCategoryId = $("#1st_discipline").val();
	var secondCategoryId = $("#2nd_discipline").val(); 
	if($.trim(firstCategoryId)==""||$.trim(secondCategoryId)==""){
		scmpublictoast(homepage.notPrjResearchArea,1000);
		return 0;
	}
    var isIndexDiscussOpen = 1;
    var isIndexMemberOpen = 0;
    var isIndexPubOpen = 0;
    var isIndexFileOpen = 0;

    var isCurwareFileShow = 0;
    var isWorkFileShow = 0;
    var isPrjPubShow = 0;
    var isPrjRefShow = 0;
    var grpCategory = 11  ; //项目群组
    var select = 0;
    if(hitOpneTypeCommen == ""){
        hitOpneTypeCommen = "H" ; //默认值
    }
    if (hitOpneTypeCommen == "O") {
        $.each($("#openType").find("input[type='checkbox']:checked"), function(i, o) {
            name = $(o).attr("name");
            if ('isIndexMemberOpen1' == name) {
                isIndexMemberOpen = 1;
                select ++ ;
            } else if ('isIndexPubOpen1' == name) {
                isIndexPubOpen = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isIndexFileOpen1' == name) {
                isIndexFileOpen = 1;
                if(grpCategory == "11")select ++ ;
            } else if ('isCurwareFileShow1' == name) {
                isCurwareFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isWorkFileShow1' == name) {
                isWorkFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isPrjPubShow1' == name) {
                if(grpCategory == "11")select ++ ;
                isPrjPubShow = 1;
            } else if ('isPrjRefShow1' == name) {
                if(grpCategory == "11")select ++ ;
                isPrjRefShow = 1;
            }
        });
        if(select<2){
            scmpublictoast(groupBase.selectOpenModuleTip, 2000);
            return 0;
        }
    }

    if (hitOpneTypeCommen == "H") {
        $.each($("#halfType").find("input[type='checkbox']:checked"), function(i, o) {
            name = $(o).attr("name");
            if ('isIndexMemberOpen2' == name) {
                isIndexMemberOpen = 1;
                select ++ ;
            } else if ('isIndexPubOpen2' == name) {
                isIndexPubOpen = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isIndexFileOpen2' == name) {
                isIndexFileOpen = 1;
                if(grpCategory == "11")select ++ ;
            } else if ('isCurwareFileShow2' == name) {
                isCurwareFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isWorkFileShow2' == name) {
                isWorkFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isPrjPubShow2' == name) {
                isPrjPubShow = 1;
                if(grpCategory == "11")select ++ ;
            } else if ('isPrjRefShow2' == name) {
                if(grpCategory == "11")select ++ ;
                isPrjRefShow = 1;
            }
        });
        if(select<2){
            scmpublictoast(groupBase.selectOpenModuleTip, 2000);
            return 0;
        }
    }
	// var disciplines = "";
	$.ajax({
		url : '/groupweb/mygrp/ajaxsavecreategrp',
		type : 'post',
		dataType:'json',
		data:{
            "isIndexDiscussOpen" : isIndexDiscussOpen,// 主页是否显示群组动态 [1=是 ， 0=否 ]
            "isIndexMemberOpen" : isIndexMemberOpen,// 主页是否显示群组成员 [1=是 ， 0=否 ]
            "isIndexPubOpen" : isIndexPubOpen,// 主页是否显示群组成果 [1=是 ， 0=否 ]
            "isIndexFileOpen" : isIndexFileOpen,// 主页是否显示群组文件 [1=是 ， 0=否 ]
            "isCurwareFileShow" : isCurwareFileShow,// 群组外成员是否显示项目成果 [1=是 ， 0=否 ] 默认1
            "isWorkFileShow" : isWorkFileShow,// 群组外成员是否显示项目文献 [1=是 ， 0=否 ] 默认1
            "isPrjPubShow" : isPrjPubShow,// 群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1
            "isPrjRefShow" : isPrjRefShow,// 群组外成员是否显示作业 [1=是 ， 0=否 ] 默认1
			'grpName':grpName,
			'grpDescription':grpDescription,
			'projectNo':projectNo,
			'openType':openType,
			'keywords':keywords,
			'grpCategory':"11",
			'firstCategoryId':firstCategoryId,
			'secondCategoryId':secondCategoryId
		},
		success : function(data) {
		    BaseUtils.ajaxTimeOut(data, function(){
		        if(data.result=='success'){
		            project.savePrjGroRelation(prjId,data.grpId);
		        }else{
		            scmpublictoast(data.msg,1000);
		        }
		    });
		},
		error: function(){
			scmpublictoast(prji18n.i18n_error,1000);
		}
	});
}
// 项目和群组创建关联关系
project.savePrjGroRelation = function(prjId,grpId){
	$.ajax({
		 url:'/prjweb/ajaxPrjGrpRalation',
		 type:'post',
		 dataType:'json',
		 data:{
				'des3PrjId':prjId,
				'groupId':grpId,
			},
		success : function(data) {
			if(data.result!="error"){
				scmpublictoast(prji18n.i18n_optSuccess,1000);
				location.href="/groupweb/mygrp/main";
			}else{
				scmpublictoast(prji18n.i18n_optFail,1000);
			}
		},
	})
	
}
// 项目群组获取关键词
// 获取创建群组的关键词
project.getGrpCreateKeywords = function (){
	var arrKeywords=new Array();
	$.each($("#create_keywords").find(".chip__text"),function(i,o){
		arrKeywords.push($(o).text());
	});
	return arrKeywords.join(";"); 
}
// 关键词的自动填充
project.showDisciplinesAndKeywordsPrjList = function(keyword){
	/* $("div#disciplinesAndKeywords").remove(); */
	var strS="<div class=\"chip__box\" id=\"disciplinesAndKeywords\"><div class=\"chip__avatar\"></div><div class=\"chip__text\">";
	var strE="</div><div class=\"chip__icon icon_delete\"><i class=\"material-icons\">close</i></div>";
	var str="";  
		$.each(keyword.split(";"),function(i,word){
		str=strS+word+strE;
		$("#autokeywords").before(str);
		});
}
// 科技领域填充的自动填充
project.showDisciplines = function(obj){
    var firstCategory = $(obj).closest(".main-list__item").find("input[name='firstCategory']").val();
    var firstCategoryId = $(obj).closest(".main-list__item").find("input[name='firstCategoryId']").val();
    var secondCategory = $(obj).closest(".main-list__item").find("input[name='secondCategory']").val();
    var secondCategoryId = $(obj).closest(".main-list__item").find("input[name='secondCategoryId']").val();
    $("#1st_discipline").val(firstCategoryId);
    $("#2nd_discipline").val(secondCategoryId);
    $(".dev_sel__box[selector-id='1st_discipline']").find(".sel__value_selected").html(firstCategory);
    $(".dev_sel__box[selector-id='2nd_discipline']").css("visibility", "");
    $(".dev_sel__box[selector-id='2nd_discipline']").find(".sel__value_selected").html(secondCategory);
}
project.hideDisciplines = function(obj){
    $("#1st_discipline").val("");
    $("#2nd_discipline").val("");
    $(".dev_sel__box[selector-id='1st_discipline']").find(".sel__value_selected").html("选择一级领域");
    $(".dev_sel__box[selector-id='2nd_discipline']").css("visibility", "hidden");
    $(".dev_sel__box[selector-id='2nd_discipline']").find(".sel__value_selected").html("");
}
project.resetSecondDiscipline = function (){
	
	var $selectBox = document.querySelector('.sel__box[selector-id="2nd_discipline"]');
	if ($selectBox.style.visibility === "hidden") {
        $selectBox.style.visibility="visible";
	} else {
		$selectValue = $selectBox.querySelector("span");
		if("en_US" == locale){
			$selectValue.textContent = "Secondary Category";
		}else{
			$selectValue.textContent = "选择二级学科";
		}
		$selectValue.classList.add("sel__value_placeholder");
		$selectBox.setAttribute("sel-value","");
	}
	
}
/**
 * 项目标题进入详情页面 -- 已废弃，改用a标签href打开
 * 
 * @author houchuanjie
 * @date 2017-7-11
 */
project.enterDetail = function (des3Id){
	window.open('/prjweb/project/detailsshow?des3PrjId='+des3Id+'');
}
// 项目手工录入
project.manualEntry =function () {
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
        $prjlist.setCookieValues();
        window.location.href='/prjweb/prj/enter?backType=1&recordFrom=0';
    }, 1);
}
// 项目在线检索
project.onlineSearch =function (){
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
        window.location.href='/prjweb/import';
    }, 1);
}
// 项目文件导入
project.batchImport =function (){
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
        window.location.href='/prjweb/fileimport' ;
    }, 1);
}

// 超时处理
project.ajaxTimeOut = function(data,myfunction){
	var toConfirm=false;
	if('{"ajaxSessionTimeOut":"yes"}'==data){
		toConfirm = true;
	}
	if(!toConfirm&&data!=null){
		toConfirm=data.ajaxSessionTimeOut;
	}
	if(toConfirm){
		jConfirm(prji18n.i18n_timeout, prji18n.i18n_tipTitle, function(r) {
			if (r) {
				document.location.href=window.location.href;
				return 0;
			}
		});
	}else{
		if(typeof myfunction == "function"){
			myfunction();
		}
	}
}
// 改变url
project.changeUrl = function(targetModule) {
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


// 项目详情界面 - 点赞
project.award = function(resType, resNode, des3PrjId ,obj){
	var awardUrl = "";
	BaseUtils.doHitMore(obj,1000);
	if($(obj).find("span").html().substring(0,3) == "取消赞" || $(obj).find("span").html().substring(0,6) == "Unlike"){
		awardUrl = "/prjweb/project/ajaxprjcancelaward";
	}
	if($(obj).find("span").html().substring(0,1) == "赞" || $(obj).find("span").html().substring(0,4) == "Like"){
		awardUrl = "/prjweb/project/ajaxprjaddaward";
	}
	$.ajax( {
		url : awardUrl,
		type : "post",
		data : {
			"resType" : Number(resType),
			"resNode" : Number(resNode),
			"des3Id" : des3PrjId,
		},
		dataType : "json",
		success : function(data) {
			if (data.result == "success") {
					// 说明点赞成功
					if(data.awardStatus == 1){
						// 1 赞 显示取消赞 ；
						if(Number(data.awardCount) > 0){
							$(obj).find("span").html(prji18n.i18n_unlike+"("+data.awardCount+")");
						}else{
							$(obj).find("span").html(prji18n.i18n_unlike);
						}
						
						$(obj).addClass("new-program_body-container_func-checked");
					}
					if(data.awardStatus == 0){
						// 显示赞
						if(Number(data.awardCount) > 0){
							$(obj).find("span").html(prji18n.i18n_like+"("+data.awardCount+")");
						}else{
							$(obj).find("span").html(prji18n.i18n_like);
						}
						$(obj).removeClass("new-program_body-container_func-checked");
					}
			}
		},
		error : function(date) {
		
		}
		});
}

var prjListMainList;
// 加载项目评论数据
project.ajaxPrjComments = function(des3PrjId){
	prjListMainList =window.Mainlist({
		name:"prj_comment_list",
		listurl: "/prjweb/project/ajaxcommentshow",
		listdata: {"des3PrjId":des3PrjId},
		method:"scroll",
		listcallback: function(){
			
		}
	});
};
// 项目详情界面 - 评论数
project.comments = function(){
	document.getElementById("input").focus();
}

// 项目详情界面 - 添加评论
project.comment = function(des3PrjId,obj){
	des3PrjId = encodeURIComponent(des3PrjId);
	// 防重复点击、加载中的样式
	BaseUtils.doHitMore(obj,1000);
	var comment = $.trim($("#prjComment").find("textarea[name$='comments']").val()).replace(/\n/g,
					'<br>');
	if(comment==""){
		scmpublictoast(prji18n.i18n_comment_content,1000);
		return;
	}
	$.ajax( {
		url :"/prjweb/project/ajaxaddcomment" ,
		type : "post",
		dataType : "json",
		data : {
			"comment":comment,
			"des3PrjId":des3PrjId
		},
		success : function(data) {
			SmateShare.ajaxTimeOut(data,function(){
				if(data.result == "success"){
					$("#prjComment").find("textarea[name$='comments']").val(""); 
					// 刷新评论列表
					prjListMainList.reloadCurrentPage();
					// 设置界面评论数
					$("#dev_prj_comment").find("span").html(prji18n.i18n_comment+"("+data.commentCount+")");
					// 设置相关评论的评论数
					$("#dev_correlation_comment").find("span").html(prji18n.i18n_fantastic_comment+"("+data.commentCount+")");
					$("#dev_prj_comment").addClass("new-program_body-container_func-checked");
					addFormElementsEvents(document.getElementById("pubComment"));
					$("#prjCommnetBtn").attr("disabled", "disabled");
					scmpublictoast(prji18n.i18n_comment_success,1000);
				}else{
					scmpublictoast(prji18n.i18n_comment_failure,1000);
				}
				
			});
		}
	});
}
// 项目详情-分享
project.share = function(des3ResId,ev){
	des3ResId = encodeURIComponent(des3ResId);
	var $this=$(ev.currentTarget);
	BaseUtils.doHitMore($this,1000);
	// 加载弹窗插件
	$this.dynSharePullMode({
 		'groupDynAddShareCount':"",
 		'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)' ,
 	});
	// 收集参数
	$("#share_to_scm_box").attr("des3ResId",des3ResId).attr("resType", "4");
	var content = $("#id_prj_title").text();
	var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
	$parent.html("");
	SmateShare.createFileDiv($parent, content, des3ResId);
	// 初始化页面
	// $("#share_to_scm_box").find(".nav__list").find(".nav__item:first").click();
 	$("#id_bt_select_grp").hide();
 	// $("#id_bt_select_friend").hide();
}

project.outsideTimeOut = function(){
	$.ajax({
		url : "/psnweb/timeout/ajaxtest",
		type : "post",
		dataType : "json",
		success : function(data) {
			project.ajaxTimeOut(data,function(){
				document.location.href =window.location.href;
			});
		}
	});
}

// 超时处理
project.ajaxTimeOut = function(data,myfunction){
	var toConfirm=false;
	if(data.ajaxSessionTimeOut == 'yes'){
		toConfirm = true;
	}
	if(!toConfirm&&data!=null){
		toConfirm=data.ajaxSessionTimeOut;
	}
	if(toConfirm){
		jConfirm(prji18n.i18n_timeout, prji18n.i18n_tipTitle, function(r) {
			if (r) {
				var url = window.location.href;
// if (url.indexOf("/prjweb/outside/project/detailsshow") > 0) {
// document.location.href =
// url.replace("/prjweb/outside/project/detailsshow","/prjweb/project/detailsshow");
// return 0;
// }
				document.location.href = "/oauth/index?sys=SNS&service=" + encodeURIComponent(url);
			}
		});
	}else{
		if(typeof myfunction == "function"){
			myfunction();
		}
	}
};
// 取消评论
project.cancelComment =function(){
	$("#prjCommnetBtn").hide();
	$("#prjCommnetCancle").hide();
	$("#prjComment").find("textarea[name$='comments']").val("");
};

