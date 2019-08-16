<!doctype html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%
    response.setHeader("cache-control", "public");
%>
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<link href="${resmod }/css/wechat.iscroll.css?version=2" media="all" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/weixin/iscroll.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=2"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.search.scroll.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript">
	$(function() {
		if ($.trim($("#fromPage").val()) == "psn") {
			mobile_bottom_setTag("psn");
		}
		$("#publication").attr("class", "cur");
		if ("${wxOpenId}") {
			smatewechat.initWeiXinShare("${appId}", "${timestamp}",
					"${nonceStr}", "${signature}");
		}
		//图标高亮
		$("#findpubs").addClass("cur");
		//成果认领与全文认领提示
		mobile.pub.msgtips("${other}");
		//成果列表
		mobile.pub.ajaxuploadpubs();
		$(window).scroll(function() {
			var scrollTop = $(window).scrollTop();
			var scrollHeight = $(document).height();
			var windowHeight = $(window).height();
			if (scrollTop + windowHeight == scrollHeight) {
				if ($("#listdiv").html().indexOf("response_no-result") == -1) {
					mobile.pub.ajaxuploadpubs();
				}
			}
		});

		if (window.location.href.indexOf("pubLocale") != -1) {
			$(".mypub").attr("href", "javascript:history.go(-2);");
		}

		//模拟复选框
		$('.checkbox').on(
		'click',
		function() {
			if ($(this).siblings("input[type='checkbox']").prop(
					'checked')) {
				$(this).removeClass('cur');
				$(this).siblings("input[type='checkbox']").removeProp(
						'checked')
			} else {
				$(this).addClass('cur');
				$(this).siblings("input[type='checkbox']").prop(
						'checked', 'true')
			}
		});
		
		dealWithShowName();
	});

	var myScroll, pullDownEl, pullDownOffset, pullUpEl, pullUpOffset, generatedCount = 0, goalOffset = 10;

	function topInit() {//初始化过过滤条件
		$('span[name="typeSpan"]').each(function(index, item) {
			if ($(this).attr("value") == $("#orderBy").val()) {
				$(this).attr('class', "checked " + $(this).attr('name'));
				$(this).attr("checked", true);
			} else {
				$(this).attr("class", "unchecked");
				$(this).attr("checked", false);
			}
		});
		$('input[name="pubLocaleCheckBox"]').each(function(index, item) {
			var lang = $("#pubLocale").val();
			if (lang.indexOf($(this).val()) >= 0) {
				$(this).addClass('cur');
				$(this).prop('checked', true);
				$(this).siblings("label[class='checkbox']").addClass('cur');
			} else {
				$(this).siblings("label.checkbox").removeClass("cur");
				$(this).removeClass('cur');
				$(this).removeProp('checked');
			}
		});
		$('input[name="pubTypeCheckBox"]').each(function(index, item) {
			var temp = $("#pubType").val();
			if (temp.indexOf($(this).val()) >= 0) {
				$(this).addClass('cur');
				$(this).prop('checked', true);
				$(this).siblings("label[class='checkbox']").addClass('cur');
			} else {
				$(this).siblings("label.checkbox").removeClass("cur");
				$(this).removeClass('cur');
				$(this).removeProp('checked');
			}
		});
	}

/*   	function requestdata() {
		$("#pubsForm").submit();
	}  */ 

	function opendetail(des3PubId) {
		var des3SearchPsnId = $("#des3SearchPsnId").val();
		var pubFilterInfo = $("#select_filter_action").serializeFormToJson();
		sessionStorage.setItem("pub_filter_info", pubFilterInfo);
		window.location.href = "/pub/outside/details/list?des3PubId=" + encodeURIComponent(des3PubId) + "&des3SearchPsnId=" + encodeURIComponent(des3SearchPsnId) + 
		    "&orderBy=" + $.trim($("#orderBy").val()) + "&publishYear=" + $.trim($("#searchPubYear").val()) + "&searchPubType=" + $.trim($("#searchPubType").val()) +
		    "&includeType=" + $.trim($("#pubDBIds").val());
	}

	// 动态选择成果
	function dyn_select(despubid, obj) {
		$("#dyndes3pubId").val(despubid);
		$("#dynpubtitle").html($(obj).find(".pubTitle").html());
		$("#dynselect").show();
	}

	function cancel_dyn_select() {
		$("#dyndes3pubId").val("");
		$("#dynselect").hide();
	}

	function confirm_dyn_select(pubId) {
		$("#pubselectform").submit();
	}

	function select_filter() {
	    $("#select_filter_action").attr("action",
        "/pub/querylist/find/conditions");
		$("#select_filter_action").submit();
		event.stopPropagation();
	}

	function cancel_select() {
		$("#select").hide();
		topInit();
	}
	//模拟单选按钮
	var obj = {
		typeSpan : "",
		holderSpan : "",
	};
	function change(span) {
		$('span[name="' + $(span).attr('name') + '"]').each(function() {
			if (this != span) {
				this.className = "unchecked";
				this.checked = false;
			}
		});
		obj[$(span).attr('name')] = span.innerHTML;
		span.className = "checked " + $(span).attr('name');
		span.checked = true;
	}
//全文请求提示
function fullTextUpTips(){
  if (document.getElementsByClassName("bckground-cover").length == 0) {
    var innerele = '<div class="background-cover__content">请使用电脑端上传全文</div>';
    var newele = document.createElement("div");
    newele.className = "bckground-cover";
    newele.innerHTML = innerele;
    document.getElementsByTagName("body")[0].appendChild(newele);
    var windheight = newele.offsetHeight;
    var windwidth = newele.offsetWidth;
    var windbottom = (windheight - 48) / 2 + "px";
    var windleft = (windwidth - 240) / 2 + "px";
    document.getElementsByClassName("background-cover__content")[0].style.left = windleft;
    document.getElementsByClassName("background-cover__content")[0].style.bottom = windbottom;
    setTimeout(function() {
      document.getElementsByClassName("background-cover__content")[0].style.bottom = "-64px";
      setTimeout(function() {
        document.getElementsByTagName("body")[0].removeChild(newele);
      }, 500);
    }, 1500);
  }
}

	function goPsnHomePage(){
	  var targetUrl = "/psnweb/mobile/outhome?des3ViewPsnId=" + $("#des3SearchPsnId").val();
	  window.location.replace(targetUrl);
	}
	
	
	function commonResAward(obj){
	  mobile.snspub.awardoptnew(obj);
	}
	function commonShowReplyBox(obj){
	   var des3PubId = $(obj).attr("resId");
	   var currentPage = $(obj).attr("currentPage");
	   var currentIndex = $(obj).attr("currentIndex");
	   opendetail(des3PubId,currentIndex,currentPage);
	}
	function commonShare(obj){
	  var des3PubId = $(obj).attr("resId");
	  mobile.pub.sharePubBox(des3PubId);
	}


	function callBackAward(obj,awardCount){
	  if(awardCount>999){
	    awardCount = "1k+";
	  }
	  $(obj).find(".new-Standard_Function-bar_item").toggleClass("new-Standard_Function-bar_selected");
	  var isAward = $(obj).attr("isAward");
	  if (isAward == 1) {// 取消赞
	    $(obj).attr("isAward", 0);           
	    if (awardCount == 0) {
	      $(obj).find('span').text("赞");
	    } else {
	      $(obj).find('span').text("赞" + "(" + awardCount + ")");
	    }
	  } else {// 赞
	    $(obj).attr("isAward", 1);
	    $(obj).find('span').text("取消赞" + "(" + awardCount + ")");
	  }
	}
	
	  $.fn.serializeFormToJson = function(){    
     var o = {};    
     var a = this.serializeArray();    
     $.each(a, function() {    
         if (o[this.name]) {    
             if (!o[this.name].push) {    
                 o[this.name] = [o[this.name]];    
             }    
             o[this.name].push(this.value || '');    
         } else {    
             o[this.name] = this.value || '';    
         }    
     });    
     return o;    
  }
	  
  //获取人员姓名
  function dealWithShowName(){
    var des3PsnId = "${pubQueryModel.des3SearchPsnId }";
    if(des3PsnId != null && des3PsnId != ""){
      $.post("/psn/mobile/ajaxnames", {"des3PsnId": des3PsnId}, function(data, status, xhr){
        if(status == "success" && data.result == "success"){
          var showName = BaseUtils.htmlRestore(data.data.name);
          if (showName.length >= 10) {
            showName = showName.substring(0, 9) + "...";
          }
          $("#showName_span").text(showName);
        }
      }, "json");
    }
  }

</script>
</head>
<body>
  <form action="/pub/querylist/find/conditions" method="get" id="select_filter_action">
    <!-- DEFAULT与排序条件无关,是由于在改动全站检索进入筛选条件默认选择条件不正确那个问题影响,所以在此将DEFAULT排除 -->
    <input type="hidden" id="orderBy" name="orderBy" value = 
        <c:choose>
            <c:when test="${pubQueryModel.orderBy eq 'DEFAULT'}">
                "pubLishDate"
            </c:when>
            <c:otherwise>
                "${pubQueryModel.orderBy }"
            </c:otherwise>
        </c:choose>
    />
    <input type="hidden"
      id="scienceAreaIds" name="des3AreaId" value="${pubQueryModel.des3AreaId}" /> <input type="hidden"
      id="searchPubYear" name="publishYear" value="${pubQueryModel.publishYear}" /> <input type="hidden"
      id="searchPubType" name="searchPubType" value="${pubQueryModel.searchPubType}" /> <input type="hidden"
      id="searchString" name="searchString" value="${pubQueryModel.searchString }" /> <input type="hidden"
      id="pubDBIds" name="includeType" value="${pubQueryModel.includeType }" /> <input id="fromPage" name="fromPage"
      type="hidden" value="${pubListVO.fromPage}" /> <input id="des3SearchPsnId" name="des3SearchPsnId" type="hidden"
      value="${pubQueryModel.des3SearchPsnId }" /> <input id="psnName" name="showName" type="hidden"
      value="${pubListVO.psnName}" /> <input type="hidden" id="searchPdwh" name="searchPdwh" value="0" /> <input
      type="hidden" id="pubSum" name="pubSum" value="${pubQueryModel.pubSum }"> <input id="des3PubId"
      name="des3PubId" type="hidden" value="" /> <input id="pubIndex" name="pubIndex" type="hidden"
      value="${pubQueryModel.pubIndex } " /> <input id="detailPageNo" name="detailPageNo" type="hidden" value="" /> <input
      id="detailCurrSize" name="detailCurrSize" type="hidden" value="" /> <input id="pubSumToDetail" name="pubSum"
      type="hidden" value="" />
  </form>
  <input class="dev_publist_share" type="hidden" value="">
  <input class="psn_has_private_pub" type="hidden" value="${pubListVO.hasPrivatePub}" id="psnHasPrivatePub">
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" onclick="mobile.pub.quickShareDyn(1,'dev_publist_share');">
        <h2>
          <a href="javascript:;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <div class="black_top" style="display: none" id="dynselect">
    <div class="screening_box">
      <div class="screening">
        <h2>分享成果</h2>
        <div class="screening_tx" id="dynpubtitle"></div>
        <p>
          <input type="button" value="确&nbsp;&nbsp;定" onclick="confirm_dyn_select()" class="determine_btn"><input
            type="button" onclick="cancel_dyn_select()" value="取&nbsp;&nbsp;消" class="cancel_btn">
        </p>
      </div>
      <form id="pubselectform" action="/dynweb/dynamic/publish" method="post">
        <input id="dynText" name="dynText" type="hidden" value="${dynText}" /> <input id="dyndes3pubId"
          name="des3PubId" type="hidden" value="" />
      </form>
    </div>
  </div>
  <div class="m-top new_page-header_backcover new_page-header_func-tool">
    <c:choose>
      <c:when test="${pubListVO.fromPage=='dyn'}">
        <a class="fl"> <i class="material-icons close" onclick="confirm_dyn_select()" style="color: #fff;">close</i>
        </a>
      </c:when>
    </c:choose>
    <div style="display: flex; align-items: center; justify-content: space-between; width: 100vw;">
      <a class="fl mypub"
      <c:if test="${pubListVO.fromPage=='psn'}">
        href="/psnweb/mobile/homepage"
       
      </c:if>
      <c:if test="${pubListVO.fromPage!='psn'}">
       href="javascript:SmateCommon.goBack('/dynweb/mobile/dynshow');"
      
      </c:if>
      ><i class="material-icons "
        style="color: #fff;">keyboard_arrow_left</i></a>
      <c:choose>
        <c:when test="${pubListVO.fromPage=='psn'}">
          <span style="color: #fff !important;">我的成果</span>
        </c:when>
        <c:when test="${pubListVO.fromPage=='dyn'}">
          <span style="color: #fff; flex-grow: 1;">分享成果</span>
        </c:when>
        <c:when test="${pubListVO.fromPage=='otherpsn'}">
          <span><span id="showName_span"
            style="flex-wrap: nowrap; flex-grow: 1; text-overflow: ellipsis; white-space: nowrap; overflow: hidden;"></span>的成果</span>
        </c:when>
      </c:choose>
      <a class="fr"><i class="material-icons filter_list" onclick="select_filter(event)" style="color: #fff;">filter_list</i></a>
    </div>
  </div>
  <div class="top_clear"></div>
  <div class="content">
    <c:if test="${pubListVO.fromPage=='psn'}">
      <div class="paper-neck_tip-container dev_confirmpub_tip" style="display: none;">
        <span class="paper-neck_tip-container_detail">你有<span id="confirmPubNum">7</span>条成果需要认领
        </span> <i class="material-icons paper-neck_tip-container_icon">keyboard_arrow_right</i>
      </div>
      <div class="paper-neck_tip-container dev_confirmfulltext_tip" style="display: none;">
        <span class="paper-neck_tip-container_detail">你有<span id="confirmfullTextNum">7</span>条全文需要认领
        </span> <i class="material-icons paper-neck_tip-container_icon">keyboard_arrow_right</i>
      </div>
    </c:if>
    <div class="effort_list">
      <form id="pubsForm" action="/pub/querylist/psn" method="post">
        <input name="dynText" value="${dynText}" type="hidden"> <input id="pubType" name="pubType" type="hidden"
          value="${pubQueryModel.pubType}" /> <input id="pubLocale" name="pubLocale" type="hidden"
          value="${pubListVO.pubLocale}" /> <input id="nextId" name="nextId" type="hidden"
          value=" ${pubQueryModel.nextId}" /> <input id="des3NextId" name="des3NextId" type="hidden"
          value="${pubQueryModel.des3NextId }" /> <input id="count" name="count" type="hidden"
          value="${pubQueryModel.count} " /> <input id="psnId" name="psnId" type="hidden" value="${pubListVO.psnId}" />
        <input id=des3SearchPsnId name="des3SearchPsnId" type="hidden" value="${pubQueryModel.des3SearchPsnId}" /> <input
          id="hasLogin" name="hasLogin" type="hidden" value="${pubListVO.hasLogin}" /> <input id="fromPage"
          name="fromPage" type="hidden" value="${pubListVO.fromPage}" /> <input id="other" name="other" type="hidden"
          value="${pubListVO.other}" /> <input id="referer" name="referer" type="hidden" value="${referer}" /> <input
          id="pubCount" name="pubCount" type="hidden" value="${pubListVO.page.totalCount}" /> <input id="prePageSize"
          name="prePageSize" type="hidden" value="${pubListVO.page.pageCount}" /> <input id="showName" name="showName"
          type="hidden" value="${pubListVO.psnName}" /> <input type="hidden" id="publishYear" name="publishYear"
          value="${pubQueryModel.publishYear}" /> <input type="hidden" id="includeType" name="includeType"
          value="${pubQueryModel.includeType }" /> <input type="hidden" id="pubSum" name="pubSum"
          value="${pubQueryModel.pubSum }" /> <input id="des3PubId" name="des3PubId" type="hidden" value="" /> <input
          id="pubIndex" name="pubIndex" type="hidden" value="${pubQueryModel.pubIndex } " /> <input id="detailPageNo"
          name="detailPageNo" type="hidden" value="" /> <input id="detailCurrSize" name="detailCurrSize" type="hidden"
          value="" /> <input id="orderBy" name="orderBy" type="hidden" value="${pubQueryModel.orderBy}" />
        <div class="wrap_com1" id="listdiv">
          <div id="addload" style="width: 100%; height: 120px;"></div>
        </div>
      </form>
    </div>
  </div>
  <c:if test="${pubListVO.fromPage=='psn'}">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_bottom.jsp"></jsp:include>
  </c:if>
  <c:if test="${pubListVO.hasLogin == 0 }">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </c:if>
</body>
</html>
