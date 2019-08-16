var importAchieve = function(options){
	var defaults = {
		data: ""
	};
	languageSettings={};
    const multilanguageSettings = {
    	"title": ["Import Publications","成果列表"],
    	"labelTips1": ["Please confirm the information. Publication in ","确认成果信息并导入，底色为"],
    	"labelTips2": [" may not be yours.","标识成果可能不属于本人。"],
    	"choosebox": ["No","序号"],
    	"infobox": ["Title / Authors / Source / Date Published","题目 / 作者 / 来源 / 年份"],
    	"indexbox": ["Indexed by","收录情况"],
    	"categorybox": ["Category","类别"],
    	"dupbox": ["Duplicated","重复记录"],
    	"update": ["Replace the existing record with this updated one.","将现有的成果更新为最新"],
    	"addnew": ["Still import this record as a new publication.","导入为一条新的成果"],
    	"skip": ["Keep the existing record and do nothing.","不做任何操作，仍保留原记录"],
    	"closebtn": ["Close","关闭"],
    	"importbtn": ["Import","导入"]
    };
    Object.keys(multilanguageSettings).forEach(function (x) {
    	if (window.locale === "en_US") {
			languageSettings[x] = multilanguageSettings[x][0];
		} else {
			languageSettings[x] = multilanguageSettings[x][1];
		}
    });
    var opts = Object.assign(defaults,options); 
    var newcontent = opts.data;
    if(document.getElementsByClassName("background-cover").length==0){
    	var coverbox = document.createElement("div");
        coverbox.className = "background-cover";
    	document.body.appendChild(coverbox);
        var element = '<div class="import_Achieve-header">'
	        +'<span class="import_Achieve-header_title">'+languageSettings.title+'</span>'
	        +'<i class="list-results_close import_Achieve-header_close import_Achieve-container_close"></i>'
	        +'</div> 	'
	        +'<div class="import_Achieve-neck_tip-box">'
	        +'<span class="import_Achieve-neck_tip-detaile">'+languageSettings.labelTips1+'</span>'
	        +'<div class="import_Achieve-neck_tip-icon"></div>'
	        +'<span class="import_Achieve-neck_tip-detaile">'+languageSettings.labelTips2+'</span>'
	        +'</div>'
	        +'<div class="import_Achieve-item-body">'
	        +'<div class="import_Achieve-item_header">'
	        +'<div class="import_Achieve-item_header-title"  style="width: 8%;">'+languageSettings.choosebox+'</div>'
	        +'<div class="import_Achieve-item_header-title"  style="width: 52%;">'+languageSettings.infobox+'</div>'
	        +'<div class="import_Achieve-item_header-title"  style="width: 12%;">'+languageSettings.indexbox+'</div>'
	        +'<div class="import_Achieve-item_header-title"  style="width: 14%;">'+languageSettings.categorybox+'</div>'
	        +'<div class="import_Achieve-item_header-title"  style="width: 24%; border-right: none;">'+languageSettings.dupbox+'</div>'
	        +'</div>'
	        +'<div class="import_Achieve-item-container" style="max-height: 327px;min-height: 0px; overflow-y: auto;  overflow-x: hidden;">'
			+'</div>'
			+'</div>'
			+'<div class="import_Achieve-footer">'
			+'<div class="import_Achieve-footer_loading" style="margin-right: 72px;"></div>'
			+'<div class="import_Achieve-footer_close import_Achieve-container_close">'+languageSettings.closebtn+'</div>'
			+'<div class="import_Achieve-footer_save" onclick="importPub(this);">'+languageSettings.importbtn+'</div>'
			+'</div>'
        	+'<iframe name="iframesaveAfter" style="display:none;"></iframe>';
        var parntbox = document.createElement("div");
        parntbox.className = "import_Achieve-container";
        parntbox.innerHTML = element;
        coverbox.appendChild(parntbox);
        var allwidth = coverbox.offsetWidth;
        var allheight = coverbox.offsetHeight;
        const selfwidth = parntbox.clientWidth;
        const selfheight = parntbox.clientHeight;
        var setwidth = (allwidth - selfwidth)/2 + "px"; 
	    var setheight =  (allheight - selfheight)/2 + "px";
	    parntbox.style.left = setwidth;
	    parntbox.style.bottom = setheight;
	    window.onresize = resize;
	    function resize(){
	    	var newwidth = coverbox.offsetWidth;
            var newheight = coverbox.offsetHeight;
	        var setwidth = (newwidth - selfwidth)/2 + "px"; 
	        var setheight =  (newheight - selfheight)/2 + "px";
	        parntbox.style.left = setwidth;
	        parntbox.style.bottom = setheight;
	    }
	    if(document.getElementsByClassName("import_Achieve-container_close")){
	    	var closelist = document.getElementsByClassName("import_Achieve-container_close");
	    	for(var i = 0; i < closelist.length; i++){
                  closelist[i].onclick = function(){
               	  parntbox.style.bottom= - selfheight + "px";
				  setTimeout(function(){
		        	coverbox.removeChild(parntbox);
		        	coverbox.style.display = "none";
		        	document.body.removeChild(coverbox);
				  } ,500);
               }
	    	}
	    }
	    document.onkeydown = function(event){
            if(event.keyCode == 27){
               event.stopPropagation();
               event.preventDefault();
               parntbox.style.bottom= - selfheight + "px";
               setTimeout(function(){
                   coverbox.removeChild(parntbox);
                   coverbox.style.display = "none";
                   document.body.removeChild(coverbox);
               } ,500);
            }
         }
	    
    }
}
var authorMatch = function(){
	$(".author_match").each(function(){	
		var id = $(this).attr("id");
		var mark = id.substr(0,id.indexOf('_'));  
		if(mark==0){
			$(this).addClass("bg01");
		}
	});
}
var showDupInfo = function(){
	$(".dup_value").each(function(){			
		if ($(this).val() !=""){
			var index=this.id.substr(7);
			showSingleDupInfo(index,$(this).val());
		}
	});
	//期刊重复，是否显示新增0不显示，1显示
	$(".is_inset").each(function(){	
		if ($(this).val()==1){
			var index=this.id.substr(10);	
			$('#isInsert_'+index).show();	
		}else{
			$('#isInsert_'+index).hide();	
		}
	});
}
/*var chooseSelect = function(){
	 $(".chooseSelect").each(function(i){    
           var index=i+1;
           if($("#dv_overwrite_"+index).css("display")!='none'){
               if($(this).find("option:selected")){
                    var idx=$(this).find("option:selected").val();
                   $("#dup_flag_"+index).val(idx);
               }
           }
       });
}*/
var selectTip = function(value,index){
    if($(value).find("option:selected")){
        var idx=$(value).find("option:selected").val();
       if(idx == '0'){
           $("#title_"+index).text(languageSettings.skip);
       }else if(idx == '2'){
           $("#title_"+index).text(languageSettings.addnew);
       }else{
           $("#title_"+index).text(languageSettings.update);
       }
   }
}
var checkDup = function(value,index){
	if(!getCheckedType(index)){
		hideSingleDupInfo(index);
		return;
	}		
	if("2"=="${publicationArticleType}")
		return;
	index=index-1;	
	var GetRandomn = 1;
	//获取随机范围内数值的函数
	function GetRandom(n){GetRandomn=Math.floor(Math.random()*n+1);};
	//开始调用，获得一个1-100的随机数
	var random=GetRandom("1000");
	$.ajax({
		url:'/pubweb/publication/import/getDupInfo',
		type:'post',
		data:{'seqNo':index,'pubType':value,'xmlId':$("#xmlId").val(),'random':random,'groupId':parent.$('#groupId').val(),'friendPsnId':$(parent.document).find("#friendPsnId").val()},
		dataType:'json',  
		timeout: 10000,
		success: function(dupJson){
			var dupValue=dupJson["dupValue"];
			var index=parseInt(dupJson["seqNo"])+1;
			var isJnlInsert = dupJson["isJnlInsert"];			
			if (dupValue !=""){
				try{
					if(isJnlInsert==1){
						$('#isInsert_'+index).show();	
					}	
				  showSingleDupInfo(index,dupValue);
				}catch(e){}
			}else{					
				hideSingleDupInfo(index);
			}},
			error : function(){
				jAlert('<s:text name="referencelist.type.find.error" />','<s:text name="referencelist.label.alert" />');
			}
		});
}

var checkPubTypeDup = function(value,index,pubId){
  if(!getCheckedType(index)){
    hideSingleDupInfo(index);
  } 
  $.ajax({
    url: "/pub/dup/checkpubtype",
    type: "post",
    data: {"pubType":value,"pubId":pubId},
    dataType: "json",
    timeout: 10000,
    success: function(data){
      if(data.result == "error"){
         // 查重失败
        scmpublictoast("查重异常", 1000);
      } else if(data.result == "no_dup"){
        // 没有查重到
        $("#dup_pubId_"+pubId).val("");
        $("#is_insert").val("");
        $("#is_insert").attr("alt",value);
        $("#dup_pubId_"+pubId).parent().css("display","none");
      } else{
        // 查重成功
        $("#dup_pubId_"+pubId).val(data.result);
        $("#is_insert").val(1);
        $("#is_insert").attr("alt",value);
        $("#dup_pubId_"+pubId).parent().css("display","flex");
      }
    },
    error: function(){scmpublictoast("查重异常", 1000);}
  });
}

var checkType = function(index,pubType){
	var resType = getType(index);
	if(pubType==1)
		checkDup(resType,index);
	else
		hideSingleDupInfo(index);
}
var getCheckedType = function(index){
	var flag = false;
	$(".oper_class").each(function(){	
		var name = $(this).attr('name');
		var idx = name.substr(10,name.length);   
		if(index==idx && $(this).attr('checked'))
			flag =true;
	});
	return flag;
}
var hideSingleDupInfo = function(index){  
	$("#dv_overwrite_"+index).hide();
	$("#abort_"+index).attr("checked",false);
	$("#nodup_"+index).attr("checked",true);
	$("#dup_id_"+index).val("0");
	 $("#dup_flag_"+index).val("2");

}
var showSingleDupInfo = function(index,dupValue){	
	$("#dv_overwrite_"+index).show();
	$("#nodup_"+index).attr("checked",false);
	$("#abort_"+index).attr("checked",true);
	$("#dup_id_"+index).val(dupValue);
}

function getJson(){
	var pubList=[];
	$(".import_Achieve-item-body_content").each(function(i){
		var $this = $(this);
		var pub = {};
		var $selectSeqNo = $this.find("input[name='pdwh_pubId']");
		pub.pubId = $selectSeqNo.attr("value");
		pub.dupPubId= $this.find("input[name='dup_pubId']").attr("value");
		pub.selected =  $this.find("[name='is_select']>i").hasClass("item_selected-tip") ? 1 : 0;
		pub.sitations=[];
		$this.find(".item_content-prj").find("i").each(function(){
			if($(this).hasClass("item_selected-tip")){
				pub.sitations.push($(this).siblings("input").attr("value"));
			}
		});
		pub.pubType = $this.find("select[name='pubtype']").val();
		
		pub.repeatSelect = $this.find(".repeatSelect").val() ? $this.find(".repeatSelect").val() : 2 ;
		pub.repeatSelect = pub.dupPubId ? pub.repeatSelect : 2;
		pubList.push(pub);
	});
	var importSaveVo = {};
	importSaveVo.changPubList = pubList;
	return importSaveVo;
};
var importPub = function(obj){
    //防重复点击
    var click = $(obj).attr("onclick");
    BaseUtils.unDisable(obj);
//    BaseUtils.doHitMore(obj,5000);
    BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
        //所有按钮置不可操作
        $(".import_Achieve-header_close").disabled;
        $(".import_Achieve-footer_close").disabled;
        $(".import_Achieve-footer_save").disabled;
        $(".import_Achieve-footer_loading").doLoadStateIco({status:1});
        $.ajax({
            url:"/pub/snspub/ajaxsaveimportlist",
            type:"post",
            data:{"saveJson":JSON.stringify(getJson())},
            dataType:"html",
            timeout:10000,
            success:function(data){
                var totle = window.parent.document.getElementsByClassName("background-cover")[0].offsetWidth;
                var aaa = (totle - 260)/2;
                if($(data).find(".new-success_save")){   
                    $(data).find(".new-success_save").css("left",aaa+"px");
                    var $div =  data;
                    var parentNode = window.parent.document.getElementsByClassName("background-cover")[0];
                    parentNode.innerHTML=$div;
                    var targetele = document.getElementsByClassName("new-success_save")[0];
                    var bottomsize = (window.innerHeight -targetele.offsetHeight)/2 + "px"; 
                    var leftsize = (window.innerWidth - targetele.offsetWidth)/2 + "px";
                    targetele.style.left = leftsize;
                    targetele.style.bottom = bottomsize;
                    window.onresize= function(){
                        targetele.style.left = leftsize;
                        targetele.style.bottom = bottomsize;
                    }
                }else{
                    $(data).find(".dialogs__box-fixed").css("left",aaa+"px");
                    var $div =  data;
                    var parentNode = window.parent.document.getElementsByClassName("import_Achieve-container")[0];
                    if(typeof parentNode == "undefined"){
                        return false;
                    }
                    parentNode.style.border='0';
                    parentNode.innerHTML=$div;  
                }
                BaseUtils.disable(obj, click);
            },
            error:function(e){
              
            }
        });        
    }, 0);
}
