<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name='homepage.fundmain.recommend' /></title>
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/smate-pc/new-fundRecommend/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/smate-pc/new-fundRecommend/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/smate-pc/new-fundRecommend/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/smate-pc/new-fundRecommend/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/smate-pc/new-fundRecommend/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/smate-pc/new-fundRecommend/footer2016.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/new-fundRecommend/pop_new.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/css/scm-newpagestyle.css">
  <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
  <link href="${resmod }/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
    <script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
    <script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
    <script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.qrcode.min.js"></script>
    <script type="text/javascript" src="${resscmsns}/js_v5/plugin/dialog.js"></script>
    <link href="${resmod}/css/resetpwd/resetpassword-temp.css" rel="stylesheet" type="text/css" />
    <!--重置密码框css  -->
    <script src="${resmod}/js/plugin/des/des.js" type="text/javascript"></script>
    <!--重置密码框-密码加密js  -->
    <script type="text/javascript" src="${resmod}/js/resetpwd/resetpassword_${locale}.js"></script>
    <!--重置密码框js  -->
    <script type="text/javascript" src="${resmod}/js/resetpwd/resetpassword.js"></script>
    <!--重置密码框js  -->
    <script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ressns }/js/fund/fund_recommend_${locale}.js"></script>
    <script type="text/javascript" src="${ressns }/js/fund/fund_recommend.js"></script>
    <script type="text/javascript" src="${ressns }/js/fund/fund_find.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
    <script type="text/javascript" src="${resmod }/js_v8/agency/scm_pc_agency.js"></script>
    <style type="text/css">
.nav__item {
  height: 46px !important;
  min-height: 41px !important;
}

.item_selected {
  border-bottom: 2px solid #288aed;
}
</style>
    <script type="text/javascript">
		var module = "${module}";
		var locale = '${locale}';
		$(document).ready(function() {
			if(module == "collected"){
				$("#collectedFundLi").click();
			}else if(module =="recommend"){
				$("#recommendFundLi").click();
			}else if(module =="findFund"){
			   $("#findFundLi").click();
			}else{
				$("#agencyFundLi").click();
			}
		    var headerlist = document.getElementsByClassName("nav_horiz-container");
            var total = document.getElementsByClassName("header__box")[0].offsetWidth;
            var parentleft = document.getElementsByClassName("header__nav")[0].offsetLeft;
            var subleft  = document.getElementsByClassName("header-nav__item-bottom")[0].offsetWidth;
            for(var i = 0 ; i < headerlist.length; i++){
                if(!!window.ActiveXObject || "ActiveXObject" in window){
                    if(locale == "en_US"){
                        headerlist[i].style.right = 167 + "px";
                    }else{
                        headerlist[i].style.right = 167 + "px";
                    }
                }else{
                    if(locale == "en_US"){
                        headerlist[i].style.right = 167 + "px";
                    }else{
                        headerlist[i].style.right = 167 + "px";
                    }
                }
            }
            var setheight = window.innerHeight - 180 - 95;
            var contentlist = document.getElementsByClassName("content-details_container");
            for(var i = 0; i < contentlist.length; i++){
                contentlist[i].style.minHeight = setheight + "px";
            }           
		});

		function changeModule(module, obj) {
		    $(".nav__item").removeClass("item_selected");
	        $(obj).addClass("item_selected"); 
			if (module == "collected") {
			    clearhtml();
			    document.getElementById("fundcollect").style.display = "flex";
                document.getElementById("fundcollect").style.left = (document.body.offsetWidth - 1200)/2 + "px";
                document.getElementById("content").style.marginTop = 50 + "px";
                window.onresize = function(){
                    document.getElementById("fundcollect").style.left = (document.body.offsetWidth - 1200)/2 + "px";
                }
				FundRecommend.showCollectionPage();
			} else if(module == "recommend"){
			    clearhtml();
			    document.getElementById("fundcollect").style.display = "none";
			    document.getElementById("content").style.marginTop = 50 + "px";
				FundRecommend.showRecommendPage();
			}else if(module == "findFund"){
			    clearhtml();
			    document.getElementById("fundcollect").style.display = "none";
			    document.getElementById("content").style.marginTop = 50 + "px";
			    FundRecommend.showFundFindPage();
            }else{
                clearhtml();
                document.getElementById("fundcollect").style.display = "none";
                document.getElementById("content").style.marginTop = 50 + "px";
				FundRecommend.showAgencyPage();
			}
		}
		
		
		function clearhtml(){
            if(document.getElementsByClassName("content-details_container").length > 0){
                var clearlist = document.getElementsByClassName("content-details_container");
                for(var i = 0; i < clearlist.length; i++){
                    clearlist[i].innerHTML　=　"";
                }
            }
        }
		//初始化 分享 插件
		function initSharePlugin(obj){
			if (locale == "en_US") {
				$(obj).dynSharePullMode({
				    'groupDynAddShareCount': PCAgency.updateShareOutsideCount,
					'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
					'language' : 'en_US'
				});
			} else {
				$(obj).dynSharePullMode({
				    'groupDynAddShareCount': PCAgency.updateShareOutsideCount,
					'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
				});
			}
		};
		//==============================
		function sharePsnCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
			$.ajax({
				url : '/prjweb/fund/ajaxsharecount',
				type : 'post',
				dataType : 'json',
				data : {
					'des3FundId':resId
				},
				success : function(data) {
					if (data.result == "success") {
			           var count = data.shareCount;
                       if(count>0 && count<1000){
                          count = "("+count+")";  
                       }else if(count>=1000){
                         count = "(1k+)";  
                       }
					   $('.shareCount_'+fundId).html(count);
					}
				}
			});
		}
		function shareGrpCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
			$.ajax({
				url : '/prjweb/fund/ajaxsharecount',
				type : 'post',
				dataType : 'json',
				data : {
					'des3FundId':resId
				},
				success : function(data) {
					if (data.result == "success") {
                        var count = data.shareCount;
                        if(count>0 && count<1000){
                           count = "("+count+")";  
                        }else if(count>=1000){
                          count = "(1k+)";  
                        }
                        $('.shareCount_'+fundId).html(count);						
					}
				}
			});
		}
		//==============================
		//分享回调
		function shareCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
			if(shareContent){
	            $.ajax({
	                url : '/prjweb/fund/ajaxsharecount',
	                type : 'post',
	                dataType : 'json',
	                data : {
	                    'des3FundId':resId
	                },
	                success : function(data) {
	                    if (data.result == "success") {
	                       var count = data.shareCount;
	                       if(count>0 && count<1000){
	                          count = "("+count+")";  
	                       }else if(count>=1000){
	                         count = "(1k+)";  
	                       }
	                       $('.shareCount_'+fundId).html(count);
	                    }
	                }
	            });
			}else{
				var count = $.trim($('.shareCount_'+fundId).text());
				if(count!='(1k+)'){
				   count = Number($('.shareCount_'+fundId).text().replace(/[\D]/ig,""))+1;
                   if(count>=1000){
                     count = "1k+";  
                   }
  				   $('.shareCount_'+fundId).html("("+count+")");			
				}
			}
		};
	</script>
</head>
<body style="overflow: hidden;">
  <div class="result-class01" style="position: fixed; background: #fff; opacity: 1; z-index: 15; top: 47px;">
    <div class="result-class__wrap result-class__wrap-normalheight">
      <c:if test="${locale=='zh_CN'}">
        <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px;">
        <ul class="nav__list">
          <li class="nav__item" style="max-width: 67px;" onclick="javascript:changeModule('recommend', this);"
            id="recommendFundLi"><s:text name='homepage.fundmain.recommend' /></li>
          <li class="nav__item" style="max-width: 67px;" onclick="javascript:changeModule('collected', this);"
            id="collectedFundLi"><s:text name='homepage.fundmain.myfund' /></li>
          <li class="nav__item" style="max-width: 67px;" onclick="javascript:changeModule('findFund', this);"
            id="findFundLi"><s:text name='homepage.fundmain.findfund' /></li>
          <li class="nav__item" style="max-width: 67px;" onclick="javascript:changeModule('fundAgency', this);"
            id="agencyFundLi"><s:text name='homepage.fundmain.agencyName' /></li>
        </ul>
        <!-- <div class="nav__underline"></div> --> </nav>
      </c:if>
      <c:if test="${locale=='en_US'}">
        <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px;">
        <ul class="nav__list">
          <li class="nav__item" onclick="javascript:changeModule('recommend', this);" id="recommendFundLi"><s:text
              name='homepage.fundmain.recommend' /></li>
          <li class="nav__item" onclick="javascript:changeModule('collected', this);" id="collectedFundLi"><s:text
              name='homepage.fundmain.myfund' /></li>
          <li class="nav__item" onclick="javascript:changeModule('findFund', this);" id="findFundLi"><s:text
              name='homepage.fundmain.findfund' /></li>
          <li class="nav__item" onclick="javascript:changeModule('fundAgency', this);" id="agencyFundLi"><s:text
              name='homepage.fundmain.agencyName' /></li>
        </ul>
        <!--  <div class="nav__underline"></div> --> </nav>
      </c:if>
    </div>
  </div>
  <div class="clear_h20"></div>
   <div id="fundcollect" class="new-fundcollect_search">
      <div class="new-fundcollect_search-box">
         <input class="new-fundcollect_search-input" type="text" placeholder="请输入你要检索的基金名称">
         <i class="searchbox__icon"></i>
      </div>
  </div> 
  <div id="content" style="margin-top: 50px; ">
    <div id="recommend_list_container">
    <div style="width: 1200px; display: flex; align-items: flex-start; justify-content: space-between;">
      <div id="div_preloader"></div>
      <div style="width: 939px; padding-left: 20px; float: right; border-left: 1px solid #dddddd; display: none;" id="div_preloader_re"></div>
      <!-- 左边栏和推荐条件弹出框 -->
      <div class="cont_l" id="conditions_div" style=" overflow-x: hidden; width: 240px; border-right: 1px solid #ddd;">
        <%@include file="fund_recommend_conditions.jsp"%> 
        
      </div>
      <input type="hidden" name="des3FundAgencyIds" value="" id="des3FundAgencyIds" />
      <!-- 推荐的基金列表 -->
      <div class=""  style="right: 0px; width: 939px; border: none; padding-left: 20px;">
          <div class="main-list__list  main-list__list-onscroll" list-main="recommendFundList" style="display: block;"></div>
          <div class="main-list__footer">
              <div class="pagination__box" list-pagination="recommendFundList">
                <!-- 翻页 -->
              </div>
          </div>
      </div>
    </div>
    
   <%--  <div id="recommend_list_footer"><jsp:include page="/skins_v6/footer_infor.jsp" /></div> --%>
    
    </div>
    <!-- 收藏的基金列表 -->
    <div class="content" id="collectioned_list_container">
      <div class="content-details_container" id="collectioned_list_div"></div>
      <%-- <div id="collectioned_list_footer"><jsp:include page="/skins_v6/footer_infor.jsp" /></div> --%>
    </div>
     <!-- 发现的基金列表 -->
    <div class="content" id="fund_find_list_container">
      <div class="content-details_container" id="fund_find_list_div"></div>
      <%--  --%>
    </div>
    
    <!-- 资助机构列表 -->
    <div id="agency_list_container">
      <div id="agency_list_div"></div>
    </div>
    <!-- 资助机构详情列表 -->
    <div id="agency_detail_list_container">
      <div id="agency_detail_list_div"></div>
    </div>
    <div class="clear"></div>
    <jsp:include page="/common/smate.share.jsp" />
  </div>
  <div><jsp:include page="/skins_v6/footer_infor.jsp" /></div>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box  new-Popup_containerlimit-width" dialog-id="dialogsBox" style="width: 720px;" cover-event="" id="dialogsBox" process-time="0"></div>
  <!-- 科技领域弹出框 -->
</body>
</html>
