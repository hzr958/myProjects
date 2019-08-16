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
<link href="${resmod }/css/wechat.iscroll.css?version=2" media="all" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/weixin/iscroll.js?version=1"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.search.scroll.js"></script>
<script type="text/javascript">
	$(function() {
		$("#publication").attr("class", "cur");
		if ("${wxOpenId}") {
			smatewechat.initWeiXinShare("${appId}", "${timestamp}",
					"${nonceStr}", "${signature}");
		}
		//图标高亮
		$("#findpubs").addClass("cur");
		topInit();
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

	function requestdata() {
		$("#pubsForm").submit();
	}

	function opendetail(obj, index, nextId) {
		$("#pubsForm").attr("action",
				"/pub/details/sns?useoldform=true");
		$("#des3PubId").val(obj);
		$("#pubIndex").val(index);
		var size=$(".paper").length;
		$("#detailCurrSize").val($(".paper").length);
		if (nextId == "undefined" || nextId == "") {//SCM-9669_start
			nextId = 0;
		}
		var deatailPageNo=parseInt(nextId) * 10 + index;
		$("#detailPageNo").val(parseInt(nextId) * 10 + index);//SCM-9669_end
		confirm_select();
	}

	// 动态选择成果
	function dyn_select(despubid, obj) {
	    SmateCommon.checkPubAnyUser(despubid,"publishDyn",obj);
	}

	function cancel_dyn_select() {
		$("#dyndes3pubId").val("");
		$("#dynselect").hide();
	}

	function confirm_dyn_select(pubId) {
		$("#pubselectform").submit();
	}

	function select_filter() {
		 $("#select_filter_action").submit();
	}

	function cancel_select() {
		$("#select").hide();
		topInit();
	}

	function confirm_select() {
		//获取数据
		$("#orderBy").val($(".typeSpan").attr("value"));
		var lang = ""
		var langLen = $('input[name="pubLocaleCheckBox"]:checked').length;
		$('input[name="pubLocaleCheckBox"]:checked').each(
				function(index, item) {
					if (index == langLen - 1) {
						lang += $(this).val();
					} else {
						lang += $(this).val() + ",";
					}
				});
		var chkBox = "";
		var len = $('input[name="pubTypeCheckBox"]:checked').length;
		$('input[name="pubTypeCheckBox"]:checked').each(function(index, item) {
			if (index == len - 1) {
				chkBox += $(this).val();
			} else {
				chkBox += $(this).val() + ",";
			}
		});
		$("#pubType").val(chkBox);
		$("#pubLocale").val(lang);
		//http://jira.oa.irissz.com/browse/SCM-9433
		$("#nextId").val("");
		$("#pubsForm").submit();
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
</script>
</head>
<body>
  <form action="/pub/querylist/find/conditions" method="get" id="select_filter_action">
    <input type="hidden" id="orderBy" name="orderBy" value="${pubQueryModel.orderBy}" /> <input type="hidden"
      id="scienceAreaIds" name="des3AreaId" value="${pubQueryModel.des3AreaId}" /> <input type="hidden"
      id="publishYear" name="publishYear" value="${pubQueryModel.publishYear}" /> <input type="hidden"
      id="searchPubType" name="searchPubType" value="${pubQueryModel.searchPubType}" /> <input type="hidden"
      id="searchString" name="searchString" value="${pubQueryModel.searchString }" /> <input type="hidden"
      id="includeType" name="includeType" value="${pubQueryModel.includeType }" /> <input id="fromPage" name="fromPage"
      type="hidden" value="${pubQueryModel.fromPage}" /> <input type="hidden" id="searchPdwh" name="searchPdwh"
      value="0" />
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
        <input id="dynText" name="dynText" type="hidden" value="${pubQueryModel.dynText}" /> <input id="dyndes3pubId"
          name="des3PubId" type="hidden" value="" />
      </form>
    </div>
  </div>
  <div class="m-top new_page-header_backcover new_page-header_func-tool">
    <a class="fl"> <i class="material-icons close" onclick="confirm_dyn_select()" style="color: #fff;">close</i>
    </a>
    <div style="display: flex; align-items: center; justify-content: space-between; width: 100vw;">
      <span style="color: #fff; flex-grow: 1;">分享成果</span> <a href="javascript:;" class="fr"><i
        class="material-icons filter_list" onclick="select_filter()" style="color: #fff;">filter_list</i></a>
    </div>
  </div>
  <div class="top_clear"></div>
  <div class="content">
    <div class="effort_list">
      <form id="pubsForm" action="/pub/querylist/psn" method="post">
        <input id="orderBy" name="orderBy" type="hidden" value="${pubQueryModel.orderBy}" /> <input name="dynText"
          value="${pubQueryModel.dynText}" type="hidden"> <input id="pubType" name="pubType" type="hidden"
          value="${pubQueryModel.pubType}" /> <input id="pubLocale" name="pubLocale" type="hidden"
          value="${pubListVO.pubLocale}" /> <input id="articleType" name="articleType" type="hidden"
          value="${pubListVO.articleType}" /> <input id="nextId" name="nextId" type="hidden"
          value=" ${pubQueryModel.nextId}" /> <input id="des3NextId" name="des3NextId" type="hidden"
          value="${pubQueryModel.des3NextId }" /> <input id="pubIndex" name="pubIndex" type="hidden"
          value="${pubQueryModel.pubIndex } " /> <input id="des3PubId" name="des3PubId" type="hidden" value="" /> <input
          id="detailCurrSize" name="detailCurrSize" type="hidden" value="" /> <input id="detailPageNo"
          name="detailPageNo" type="hidden" value="" /> <input id="count" name="count" type="hidden"
          value="${pubQueryModel.count} " /> <input id="psnId" name="psnId" type="hidden" value="${pubListVO.psnId}" />
        <input id=des3SearchPsnId name="des3SearchPsnId" type="hidden" value="${pubQueryModel.des3SearchPsnId}" /> <input
          id="hasLogin" name="hasLogin" type="hidden" value="${pubListVO.hasLogin}" /> <input id="fromPage"
          name="fromPage" type="hidden" value="${pubListVO.fromPage}" /> <input id="other" name="other" type="hidden"
          value="${pubListVO.other}" /> <input id="referer" name="referer" type="hidden" value="${referer}" /> <input
          id="pubCount" name="pubCount" type="hidden" value="${pubListVO.page.totalCount}" /> <input id="prePageSize"
          name="prePageSize" type="hidden" value="${pubListVO.page.pageCount}" />
        <div class="wrap_com1" id="listdiv">
          <div id="addload" style="width: 100%; height: 120px;"></div>
        </div>
      </form>
    </div>
  </div>
  <c:if test="${pubListVO.hasLogin == 0 }">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </c:if>
</body>
</html>