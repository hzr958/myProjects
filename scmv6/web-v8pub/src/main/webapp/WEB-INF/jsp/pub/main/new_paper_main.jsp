<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link href="${resmod}/smate-pc/new-mypaper/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/footer2016.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<style type="text/css">
.filter-value__option-style {
  width: 144px;
}
.filter-value__item {
  margin: 0px !important;
}
</style>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/js_v5/scm.maint.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript">
var shareI18 = '<spring:message code="publication.recommend.btn.share"/>';
$(function(){
	ScmMaint.searchSomeOneBind();
	addFormElementsEvents(document.getElementById("share_to_scm_box"));
	/*  document.getElementsByClassName("main-list__detail")[0].style.height= window.innerHeight - 98 + "px";
	 document.getElementsByClassName("main-list__container")[0].style.height= window.innerHeight - 98 + "px";
   document.getElementsByClassName("main-list__list")[0].style.height= window.innerHeight - 98 + "px"; */
	//加载成果列表
	Pub.loadCollectedPubs();
});
//初始化 分享 插件
function initSharePlugin(obj){
	if (locale == "en_US") {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
			'language' : 'en_US'
		});
	} else {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
		});
	}
};
//==============================
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType){
	if(resType == "1" || resType == "2"){
        addPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType);
    }else if(resType == "22" || resType == "24"){
        addPdwhPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType);
    }
}
function shareGrpCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
	shareCallback(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId);
}
//分享回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId,resType,dbId){
	var count = Number($('.dev_pub_share_'+pubId).text().replace(/[\D]/ig,""))+1;
    if(count>999){
      count = "1k+";
    }
    $('.dev_pub_share_'+pubId).html('<i class="icon-share"></i> '+shareI18+"("+count+")");
};
function addPdwhPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType){
    $.ajax({
        url : '/pub/opt/ajaxpdwhshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PdwhPubId':resId,
                  'comment':shareContent,
                  'sharePsnGroupIds':receiverGrpId,
                  'platform':"2"
               },
        success : function(data) {
          var count = data.shareTimes;
          if(count>999){
            count = "1k+";
          }
            $('.dev_pub_share_'+pubId).html('<i class="icon-share"></i> '+shareI18+"("+count+")");
        }
    }); 
}
function addPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType){
    $.ajax({
        url : '/pub/opt/ajaxshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PubId':resId,
             'comment':shareContent,
             'sharePsnGroupIds':receiverGrpId,
             'platform':"2"
               },
        success : function(data) {
          var count = data.shareTimes;
          if(count>999){
            count = "1k+";
          }
        	 $('.dev_pub_share_'+pubId).html('<i class="icon-share"></i> '+shareI18+"("+count+")");
        }
    });
}
window.onload=function(){    
    if(document.getElementById("search_some_one")){
        document.getElementById("search_some_one").onfocus = function(){
            this.closest(".searchbox__main").style.borderColor = "#2882d8";
        }
        document.getElementById("search_some_one").onblur = function(){
            this.closest(".searchbox__main").style.borderColor = "#ccc";
        }
    }
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
       targetlist[i].querySelector("input").onfocus = function(){
           this.closest(".searchbox__main").style.borderColor = "#3faffa;";
       }
       targetlist[i].querySelector("input").onblur = function(){
            this.closest(".searchbox__main").style.borderColor = "#ccc;";
       }
    }
    var targetlist = document.getElementsByClassName("click-target_btn");
    var titlelist = document.getElementsByClassName("item_list-align_item");
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
    for(var z = 0; z < titlelist.length; z++){
      titlelist[z].onmouseover = function(){
        this.title=this.innerHTML;}
    }
  }
  function fileuploadBoxOpenInputClick(ev){
      var $this = $(ev.currentTarget);
      $this.find('input.fileupload__input').click();
  };
</script>
<!-- 检索条件 -->
<jsp:include page="/common/smate.share.mvc.jsp" />
<jsp:include page="paper_search_conditions.jsp" />
<div class="main-list__detail" style="height: 100% !important; width: 960px; overflow: visible; height: auto;">
    <div class="main-list__container" id="listcontainer" style="border-left: 1px solid #ddd;display: flex; flex-direction: column; height: auto;">
       <div class="main-list__list cont_r" id='paper_list' list-main='paperlist' style="width: 960px;min-height: 1200px; border-left: 0px solid #ddd; flex-shrink: 0;">
       <!-- 论文列表 -->
       </div>
       <div class="main-list__footer">
            <div class="pagination__box" list-pagination="paperlist">
              <!-- 翻页 -->
            </div>
        </div>
    </div>
    <%-- <div><%@ include file="/skins_mvc/footer_infor.jsp"%></div>  --%>
</div>
