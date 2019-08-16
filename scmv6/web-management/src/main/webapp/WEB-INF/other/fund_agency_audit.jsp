<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />

<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
	var resctx = "${res}";
	var ctxpath = "${ctx}";
	var respath="${res}";
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基金管理</title>
<link href="${res}/css_v5/header.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/common.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"	href="${res}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen"	href="${res}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css"	href="${res }/css_v5/plugin/jquery.thickbox.css" />
<link rel="stylesheet" type="text/css"	href="${res}/css_v5/plugin/jquery.complete.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/pop.css" />

<script type="text/javascript" src="${res}/js_v5/plugin/jquery.proceedingwin.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${res }/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.scmtips.js"></script>
<script type='text/javascript' src='${res }/js_v5/common.order.js'></script>
<script type="text/javascript" src="${res }/js_v5/rol_common.js"></script>
<script type='text/javascript' src='${res}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${res }/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${res }/js_v5/table.float.rol.js"></script>
<script type="text/javascript" src="${res}/js_v5/scm.page.js"></script>
<script type="text/javascript" src="${res}/js_v5/bpo_fund/agency.audit.js"></script>
<script type="text/javascript">
var search_tipcon = '请输入：机构名称，类别名称';
var lable_check_input="请输入检索条件";
var check_input_date = "开始日期必须在截止日期之前";
var insAgencyId = "${id}";
$(document).ready(function() {
	$("a.thickbox,input.thickbox").thickbox({ctxpath:"${ctx}",respath:"${res}"});
	agencyAudit.search(0);
});
function loadOtherPage(pageNo) {
	$("#pageNo").val(pageNo);
	agencyAudit.search(0);
}

function topage(pageNo) {
	var toPage = $.trim($("#toPage").val());
	if (!/^\d+$/g.test(toPage))
		toPage = 1;
	$("#pageNo").attr("value", toPage);
	agencyAudit.search(0);
}

function setTabUrl(url){
	location.href=url;
}
</script>
</head>
<body>
<input type="hidden" class="thickbox" id="thickbox_agency"/>
	<div id="rol_main">
		<div id="Tab">
			<div class="Menubox">
				<ul>
					<li id="one1" onClick="setTabUrl('${ctx}/fund/agency/maint')" >资助机构列表</li>
					<li id="one2" onClick="setTabUrl('${ctx}/fund/agency/audit')" class="hover">待确认资助机构列表</li>
				</ul>
			</div>
			<div class="Contentbox">
				<div id="con_one_2" class="hover">
						<div class="mdown10">
							<span class="Fleft"><span class="red">*</span>&nbsp;显示需要审核的资助机构数据</span>
							<span class="Fright">
								<a onclick="agencyAudit.audit();" class="uiButton">批准/拒绝</a> 
							</span>
							<div class="clear"></div>
						</div>
						<div id="loading_tip"><img src="${res}/images_v5/loading_img.gif" style="vertical-align: middle;"/>&nbsp;<s:text name="数据加载中..."/></div>
						<div id="searchResult"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>