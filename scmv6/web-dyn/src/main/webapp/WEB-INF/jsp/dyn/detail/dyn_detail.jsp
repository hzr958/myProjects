<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/smate.alerts.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">

<script src="${resmod }/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js" charset="UTF-8"></script>
<script type="text/javascript" src="/resmod/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dialogs.js"></script>
<script type='text/javascript' src='${resmod }/js/smate.alerts_${locale}.js'></script>
<script type='text/javascript' src='${resmod }/js/smate.alerts.js'></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil_${locale}.js"></script>
<script src="${resdyn}/dynamic.common.js" type="text/javascript"></script>
<script src="${resdyn}/dynamic.common_${locale}.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>

</script>
<script src="${resdyn}/dynamic.mobile.detail.js?version=20181013" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
	  var des3DynId=$("input[name='dynId']").attr("des3dynid");
      var $obj = $($("div.dynamic-social__item.dynamic-social__item-left.dynamic-social__item_mobile")[2]);
		//$("#div_dynamicContent").html(content);
	    autosize(document.querySelectorAll('textarea'));
	    $("#comment_content").focus();
	    initStatisCount();
	    //dynamic.initHasAward();
	    initImge_src();
	    initAward_HasAward_Comment();
	    initCollectedPub();
		dynamic.ajaxLoadComments($("#pageNumber").val());
		if("${wxOpenId}"){
			smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
		}
		$(".comments").css("display","none");
		$(".look_all").css("display","none");
		$("#comment_content").on('valuechange', function (e, previous) {
			 if($.trim($(this).val()).length>0){
	           $("#scm_send_comment_btn").attr("onclick","dynamic.doComment();");
	           $("#scm_send_comment_btn").removeClass("not_point");
	           
			 }else{
				 if($.trim($(this).val()).length==0){
					 $("#scm_send_comment_btn").removeAttr("onclick");
					 $("#scm_send_comment_btn").addClass("not_point");
				 }
			 }
	    })
        transDetailUrl();
		//在页面数据加载完之后,校验全文是否删除,删除应该替换成默认图片
		var hasFullText = "${dynMap['hasFullText']}";
		var hasThumbnailPath = "${dynMap['hasThumbnailPath']}";
		if (hasFullText == "false") {
		  //全文已删除
		  $(".dynamic-content__main img").attr("src","/resmod/images_v5/images2016/file_img.jpg");
		} else if (hasFullText == "true" && hasThumbnailPath == "false") {
		  //缩略图被删除
		  $(".dynamic-content__main img").attr("src","/resmod/images_v5/images2016/file_img1.jpg");
		} else {
		  //全文未改动不做操作
		}
	});
	
	function formatCount(count){
	  var nowCount = parseInt(count);
	  var showCount;
	  if(nowCount == 0){
	    showCount = "";
	  }else if(nowCount > 999){
	    showCount = "(1k+)";
	  }else{
	    showCount = "("+nowCount+")";
	  }
	  return showCount;
	}
	
	/**
	 * 页面刷新时，展示正确的赞数 赞状态 以及评论数
	 */
	 function initAward_HasAward_Comment(){
		
		var item = $(".dynamic__box .dynamic-social__list").find(".dynamic-social__item").find("a");
		// 展示正确的赞数
		var awardCount = ${dynMap['dyn-statis-award']};
		// 展示准确的赞状态
		var hasAward = ${dynMap['dyn-statis-hasaward']};
		//基金收藏状态 1=已收藏
		var hascollected = ${dynMap['dyn-statis-collected']};
		// hasAward 为true代表点赞   false为未点赞
		if(hasAward == true){
    		awardCount = awardCount > 999 ? '1k+' : awardCount;
			// 已经点赞，应该显示取消赞(赞数)
			item.eq(0).html(dynCommon.unlike+"("+awardCount+")");
		}else {
            if(awardCount > 0){
              awardCount = awardCount > 999 ? '1k+' : awardCount;
              // 未点赞，应该显示赞(赞数)
              item.eq(0).html(dynCommon.like+"("+awardCount+")");
            }else{
              item.eq(0).html(dynCommon.like);
            }   
		}
		// 展示正确的评论数
		var commentCount = ${dynMap['dyn-statis-reply']};
		if(commentCount > 0){
          commentCount = commentCount > 999 ? '1k+' : commentCount;
          item.eq(1).html(dynCommon.comment+"("+commentCount+")");
		}else{
		  item.eq(1).html(dynCommon.comment);
		}		
        var $obj = $($("div.dynamic-social__item.dynamic-social__item-left.dynamic-social__item_mobile")[2]);
        // 展示正确的分享数
        var shareCount = ${dynMap['dyn-statis-share']};
        if(shareCount > 0){
          shareCount = shareCount > 999 ? '1k+' : shareCount;
          item.eq(2).html(dynCommon.share+'('+shareCount+')');
        }else{
          item.eq(2).html(dynCommon.share);
        }
        
		if($obj.attr("restype") != 25){
		  if(hascollected!=null&&hascollected=="1"){
        item.eq(3).html(dynCommon.unCollected);
        }else{
            item.eq(3).html(dynCommon.collected);
            }
		}else{
		  
		}
	 }
	
	function  initImge_src(){
    var $obj = $($("div.dynamic-social__item.dynamic-social__item-left.dynamic-social__item_mobile")[2]);
     if($obj.attr("restype") != 25){
		var url = document.location.toString();
		var arrObj = url.split("?");
	    var arrPara = arrObj[1].split("&");
	    for (var i = 0; i < arrPara.length; i++) {
	    	   var arr = arrPara[i].split("=");
         if (arr != null && arr[0] == 'des3DynId') {
		     var des3DynId = arr[1];
		     dynamic.dyndetailstatistics(des3DynId);
		    return;
		 }
	  }
     }
	}
	
	/**
	 * 发表的动态中含有网址的话展示时显示网页链接
	 */
	function transDetailUrl(){
		var $div = $(".dyn_content");
		if(!$div.attr('class') || $div.hasClass('dyn_content')){
			if(!$div.attr('transurl')){
				var c = $div.html().trim();
				var matchArray = dynamic.matchUrl(c);
				var newstr = "";
				for(var i=0; i <= matchArray.length; i++){
					var beginIndex = i==0 ? 0 : matchArray[i-1].lastIndex;
					var endIndex = i == matchArray.length ? c.length : matchArray[i].index;
					var stri = c.substring(beginIndex, endIndex);
					var urli = "";
					if(i < matchArray.length){
						urli = " <a href=\"" + matchArray[i].str + "\" style=\"color: #005eac !important;\" target=\"_blank\">" + matchArray[i].str + "</a> ";
					}
					newstr += stri + urli;
				}
				$div.html(newstr);
				$div.attr('transurl', 'true');
			}
		}
	}
	//查看更多评论
	function getMoreComment(){
		var pageNumber = $("#pageNumber").val();
		pageNumber =parseInt(pageNumber) + 1;
		$("#pageNumber").attr("value",pageNumber);
		dynamic.ajaxLoadComments(pageNumber);
	};
	 $("#pageNumber").val(1);
	//初始化 分享 插件
	 function initSharePlugin(obj){
	 	var dyntype = $("#share_to_scm_box").attr("dyntype") ;
	 	$(obj).dynSharePullMode({
	 		'groupDynAddShareCount':"",
	 		'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)' ,
	 	});
	 	if("ATEMP" == dyntype ||"B1TEMP" == dyntype ||"B2TEMP" == dyntype ||"B3TEMP" == dyntype){
	 		$("#share_to_scm_box").find(".nav__list .nav__item").eq(0).click() ;
	 		//框必须最后弹出来
	 		$("#share_site_div_id").find(".inside").click();
	 		var obj_lis = $("#share_to_scm_box").find("li");
	 		obj_lis.eq(1).hide();
	 		obj_lis.eq(2).hide();
	 	}
	 }
	function initStatisCount(){
		var des3DynId=$("input[name='dynId']").attr("des3dynid");
		if(des3DynId != null && des3DynId != ""){
		  //是否赞过
            dynamic.hasAward(des3DynId);
		}
	}
	function initCollectedPub(){
        var $obj = $($("div.dynamic-social__item.dynamic-social__item-left.dynamic-social__item_mobile")[2]);
        var des3DynId=$("input[name='dynId']").attr("des3dynid");
		if($obj.attr("restype") != 25){
        if(des3DynId != null && des3DynId != ""){
            //是否收藏
            dynamic.hasCollect(des3DynId);
            }
        }
	}
</script>
</head>
<body>
  <input type="hidden" id="pageNumber" value="${pageNumber}" />
  <input type="hidden" id="userId" value="${userId}">
  <input type="hidden" id="resOwnerDes3Id" value="${resOwnerDes3Id}">
  <div class="m-top m-top_top-background_color">
    <a href="/dynweb/mobile/dynshow" class="fl" style="width: 46px;"><i class="material-icons navigate_before">&#xe408;</i></a>
    <span class="m-top_top-background_color-title"> 动态详情 </span>
  </div>
  <div class="top_clear"></div>
  <div class="black_top" id="dynamicShare" style="display: none" onclick="javascirpt:	$('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;"
      onclick="javascirpt:$('#dynamicShare').show();">
      <div id="quickShareOpt" class="screening" style="max-width: 400px">
        <h2>
          <a href="javascript:;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <input type='hidden' id="dyn-statis-award" name="dyn-statis-award" value="${dynMap['dyn-statis-award']}">
  <input type='hidden' id="dyn-statis-reply" name="dyn-statis-reply" value="${dynMap['dyn-statis-reply']}">
  <input type='hidden' id="dyn-statis-share" name="dyn-statis-share" value="${dynMap['dyn-statis-share']}">
  <div class="content dynamic__box" id="div_dynamicContent" style="margin-top: 0px; background: #fff;">
    <!-- <script type="text/javascript">
alert("${dynMap['dynamicContent']}")
</script> 
    <s:property value="dynMap['dynamicContent']" escape="false"/>-->
    ${dynMap['dynamicContent']}
    <div style="padding: 0px 16px">
      <!-- <div class="pa16" style="margin-top: -16px;">  -->
      <h2 style="display: none;" class="wdful_comments">
        <a id="moreComment" style="display: none;" href="javascript:;" onclick="getMoreComment();">查看更多评论</a>精彩评论
      </h2>
      <div id="pubview_discuss_list"></div>
    </div>
  </div>
  <div class="bottom_clear"></div>
  <div class="m-bottom">
    <div class="m-bottom_wrap" style="display: flex; align-items: center;">
     
      <!--后当没有输入不可点状态-->
      <!--<input type="button" value="确定" class="not_point">-->
      <div class="input_box" style="width:85%; margin: 0px;">
        <textarea id="comment_content" rows="1" style="white-space: pre-wrap;" placeholder="添加评论" maxlength="300"></textarea>
      </div>
       <input type="button" id="scm_send_comment_btn" value="发布" class="not_point" style="width: 15%;">
    </div>
  </div>
  <!-- 分享插件 -->
  <jsp:include page="/common/mobile_smate_share.jsp" />
</body>
</html>
