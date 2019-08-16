<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>论文推荐</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod}/smate-pc/new-mypaper/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link href="/resmod/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod }/js/search/pub_search_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/search/pub_search.js"></script>
<script type="text/javascript" src="${resmod}/js/search/PdwhPubSearch.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/homepage/psn.homepage.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/pub_recommend_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/pub_recommend.js"></script>
<style type="">
   body{
       overflow: auto;
   }
</style>
<script type="text/javascript">
$(function(){
    /* document.getElementsByClassName("main-list__container")[0].style.height= document.body.offsetHeight - 98 + "px"; */
 	var targetlist = document.getElementsByClassName("click-target_btn");
    var searchlist = document.getElementsByClassName("search-box");
    var titlelist = document.getElementsByClassName("item_list-align_item");
/* 	if(document.getElementsByClassName("main-list__container")){
	    var heightlist = document.getElementsByClassName("main-list__container");
		for(var i = 0; i < heightlist.length; i++){
			heightlist[i].style.height = window.innerHeight - 98 +"px";
		}
	} */
    for(var i = 0; i < targetlist.length;i++){
        targetlist[i].onclick = function(){
          if(this.innerHTML=="expand_less"){
            this.innerHTML="expand_more";
            this.closest(".setting-parent").querySelector(".setting-list").style.display="none";
          }else{
            this.innerHTML="expand_less";
            this.closest(".setting-parent").querySelector(".setting-list").style.display="block";
          }
        }
    }
    for(var j = 0; j< searchlist.length; j++ ){
        searchlist[j].onfocus=function(){
          this.closest(".search-box_container").style.borderColor="#288aed";
        }
        searchlist[j].onblur=function(){
          this.closest(".search-box_container").style.borderColor="#ccc";
        }
    }
    for(var z = 0; z < titlelist.length; z++){
      titlelist[z].onmouseover = function(){
        this.title=this.innerHTML; 
      }
    } 
    PubRecommend.ajaxLeftshow();

});
//初始化 分享 插件
function initSharePlugin(obj){
	if ("${locale}" == "en_US") {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
			'language' : 'en_US'
		});
	} else {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
		});
	}
 	addFormElementsEvents(); 
 	$("div[selector-id='list_sharetype']").find(".nav__item").eq(1).click();
};

function initShare(des3PubId,obj){
  Pub.pdwhIsExist2(des3PubId,function(){
    PubRecommend.getPubDetailsSareParam(obj); 
    initSharePlugin(obj);
  });
}

//分享到联系人回调
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId){
	$.ajax({
        url : '/pub/opt/ajaxpdwhshare',
        type : 'post',
        dataType : 'json',
        data : {
                  'des3PdwhPubId':resId,
                  'comment':shareContent,
                  'sharePsnGroupIds':receiverGrpId,
                  'platform':"2"
               },
        success : function(data) {
            var shareCount = data.shareTimes;
            if(shareCount>999){
              shareCount = "1k+";
            }
            var shareNote = $(".dev_pdwhpub_share[resid='"+resId+"']");
            shareNote.html('<i class="icon-share"></i> '+Pubsearch.share+" ("+shareCount+")");
        }
    }); 
}
//分享到群组回调
function shareGrpCallback (des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, dyntype, resType){
        $.ajax({
            url : '/dynweb/dynamic/ajaxsharecount',
            type : 'post',
            dataType : 'json',
            data : {'des3ResId':resId,
                'des3DynId':des3DynId,
                'resTypeStr':resType,
                 'dynType':dyntype    
                   },
            success : function(data) {
            	changeShareNum(resId);
            }
        });     
}
//分享到动态回调(只需改变页面分享统计数即可)
function shareCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId,resType,dbId){
	changeShareNum(resId); 
};
function changeShareNum(resId){
    var shareNote = $(".dev_pdwhpub_share[resid='"+resId+"']");
    var count = Number(shareNote.text().replace(/[\D]/ig,""))+1;
    if(count>999){
      count = "1k+";
    }
    shareNote.html('<i class="icon-share"></i> '+Pubsearch.share+" ("+count+")");
}
//收藏和取消收藏成果回调
function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
    	var html = "<div class='new-Standard_Function-bar_item new-Standard_Function-bar_selected'>"
    		   +"<i class='new-Standard_function-icon new-Standard_Save-icon'></i>"
    		   +"<span class='new-Standard_Function-bar_item-title'>"+Pubsearch.unsave+"</span></div>";
        $(obj).html(html);  
    }else{
    	var html = "<div class='new-Standard_Function-bar_item'>"
            +"<i class='new-Standard_function-icon new-Standard_Save-icon'></i>"
            +"<span class='new-Standard_Function-bar_item-title'>"+Pubsearch.save+"</span></div>";
        $(obj).html(html);
    }
}


function buildExtralParams(){
  var ids = "";
  $("#grp_friends").find(".chip__box").each(function() {
    var des3PsnId = $(this).attr("code");
    if (des3PsnId != "") {
      ids += "," + des3PsnId;
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
    return {"des3PsnId":ids};
 }
</script>
</head>
<body>
<div style="display: flex; flex-direction:column;">
  <div style="display: flex; align-items: flex-start;">
    <div class="cont_l" id="left_condition" style=" width: 241px; border-right: 1px solid #ddd;"></div>
    <div class="cont_r" style="width: 960px; padding-left: 0px;  border: none;">
      <div class="main-list__detail" style="width: 980px; padding-left: 20px; overflow: visible; height: auto;">
        <div class="main-list__container  main-list__container-scroll" style="height: auto;">
          <div class="main-list__list item_no-padding dev_pub-list content-details_container" list-main="pub_list" style="min-height: 1200px;"></div>
          <div class="main-list__footer" style="width: 960px;">
                <div class="pagination__box" list-pagination="pub_list">
                  <!-- 翻页 -->
                </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <%-- <div><%@ include file="/skins_mvc/footer_infor.jsp"%></div> --%>
</div>
</body>
</html>
