<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript"
  src="${resscmsns}/js_v5/home/publication/patent/${pubListVO.run_env}_patent_publish_white_list.js?version=17.2.17"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript" charset="UTF-8"></script>
<script src="${resmod}/js_v5/scm.maint.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script src="${resmod}/smate-pc/new-importprj/newimportprj.js" type="text/javascript" charset="utf-8"></script>
<script src="${resmod}/smate-pc/new-importAchieve/new-import_Achieve.js" type="text/javascript"></script>
<script type="text/javascript">


var ctxpath = "${snsctx}";
var locale = "${locale}"; 
var ressns = "${ressns}";
var snsctx = "${snsctx}";
var domainrol = "${domainrol}";
var pageContext_request_serverName = '${pubListVO.snsDomain}';
var pubIdArray = new Array();
if(pageContext_request_serverName.indexOf("https://") != -1){
	pageContext_request_serverName = "http://"+pageContext_request_serverName.substring(8);
}
var shareI18 = '<spring:message code="publication.recommend.btn.share"/>';
$(function(){
	ScmMaint.searchSomeOneBind();
	addFormElementsEvents(document.getElementById("share_to_scm_box"));
	//成果列表
	Pub.pubList();
	//由于成果认领,全文认领模块随机显示会导致关闭成果认领弹框后,成果认领和全文认领都会显示,所以修改成先显示完成果认领,若成果认领没有数据,再显示全文认领
	//Pub.randomModule();
	Pub.checkPubHasList();
	//成果合作者
	Pub.cooperator();
    var changflag = document.getElementsByClassName("filter-section__header");
    for(var i = 0;i<changflag.length;i++){
        changflag[i].onclick = function(){
            if(this.querySelector(".filter-section__toggle").innerHTML == "expand_less"){
                this.querySelector(".filter-section__toggle").innerHTML="expand_more";
                this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
            }else{
                this.querySelector(".filter-section__toggle").innerHTML = "expand_less";
                 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
            }
        } 
    }
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
    	targetlist[i].querySelector("input").onfocus = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
    	}
    	targetlist[i].querySelector("input").onblur = function(){
    			this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";
    	}
    }
});

//初始化 分享 插件
function initSharePlugin(obj){
	if(SmateShare.timeOut && SmateShare.timeOut == true)
		return;
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
	var obj_lis = $("#share_to_scm_box").find("li");
	obj_lis.eq(1).click();
};
//==============================
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
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
            if (data.result == "success") {
              	  var times=data.shareTimes;
              	  if(times>=1000){
              	    $('.shareCount_'+pubId).html("(1K+)");
              	  }else{
              	    $('.shareCount_'+pubId).html("("+(data.shareTimes)+")");
              	  }
            }
        }
    });
}
function shareGrpCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
    shareCallback(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId);
}
//==============================
//分享回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
    var count = Number($('.shareCount_'+pubId).attr("shareCount").replace(/[\D]/ig,""))+1;
    if(count>=1000){
      $('.shareCount_'+pubId).text("(1K+)");
    }else{
      $('.shareCount_'+pubId).text("("+count+")");
    }
};

//更新引用
function updateCite(des3pubIds,obj) {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    if(!initOcx()){
      downloadOctopus("cited","install");
      return false;
    }
    showDialog("updateCite");
    setTimeout(function () { 
        doUpdateCited();
    },2000);
  }, 1);
};
function closeTip(){
	hideDialog("updateError");
};

window.onload = function(){
	var hiddenlist = document.getElementsByClassName("filter-section__toggle");
	for( var i = 0;i < hiddenlist.length; i++){
            hiddenlist[i].onclick = function(){
            	if(this.innerHTML == "expand_less"){
            		this.innerHTML = "expand_more";
            		this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
            	}else{
            		this.innerHTML = "expand_less";
            		this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
            	}
            }
	}
}
</script>
<div class="container__horiz">
  <div style="display: flex; width: 100%;">
    <input id="dev_pubconfirm_isall" type="hidden" value="one" /> <input id="dev_pubftconfirm_isall" type="hidden"
      value="one" /> <input id="dev_pubcooperator_isall" type="hidden" value="one" /> <input id="whiteListKey"
      type="hidden" value="${psnId}" /> <input id="pub_type" type="hidden" value="5" />
    <div style="width: 280px; height: auto; margin-right: 16px;">
      <!-- 检索条件  begin -->
      <jsp:include page="pub_search_conditions.jsp" />
      <!-- 检索条件  end -->
      <!-- 成果合作者  begin -->
      <jsp:include page="recommend_partners.jsp" />
      <!-- 成果合作者  end -->
    </div>
    <div style="width: 100%; max-width: 880px;">
      <c:if test="${pubListVO.self == 'yes'}">
        <!-- 成果、全文认领  begin -->
        <jsp:include page="confirm_area.jsp" />
        <!-- 成果、全文认领  end -->
      </c:if>
      <div class="container__card">
        <div class="main-list">
          <!-- 成果统计模块  begin -->
          <jsp:include page="pub_statistics.jsp" />
          <!-- 成果统计模块  end -->
          <!-- 成果录入提示框    begin-->
          <jsp:include page="pub_enter_tip_box.jsp" />
          <!-- 成果录入提示框    end-->
          <!-- 成果列表  begin -->
          <div class="main-list__list dev_pub_list" list-main="psnpub"></div>
          <div class="main-list__footer">
            <div class="pagination__box" list-pagination="psnpub">
              <!-- 翻页 -->
            </div>
          </div>
          <!-- 成果列表 end -->
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="/common/smate.share.mvc.jsp" />
<!-- 分享操作 -->