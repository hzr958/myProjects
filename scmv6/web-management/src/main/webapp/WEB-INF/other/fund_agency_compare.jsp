<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资助机构对比</title>
<script type="text/javascript">var resctx="${res}";var respath="${res}"; var ctxpath="${ctx}";</script>
<link href="${res }/css_v5/rol_header.css" rel="stylesheet" type="text/css" />
<link href="${res }/css_v5/rol_common.css" rel="stylesheet" type="text/css" />
<link href="${res }/css_v5/rol_public.css" rel="stylesheet" type="text/css" />
<link href="${res }/css_v5/rol_footer.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"	href="${res}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen"	href="${res}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/pop.css" />
<script type="text/javascript" src="${res}/js_v5/common.ui.js"></script>
<script type="text/javascript" src="${res }/js_v5/jquery.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.proceedingwin.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.scmtips.js"></script>
<script type='text/javascript' src='${res}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${res }/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${res}/js_v5/link.status.js"></script>
<script type="text/javascript" src="${res}/js_v5/loadding_div.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript">
var jsessionId = "<%=session.getId() %>";
</script>
</head>
<body style="line-height:100%">
<div id="pro_fund_main" class="pro_fund_main">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="clear c_account">
	<tr>
		<td>
		<!-- 现在修改的数据 -->
			<table width="100%" border="2px" cellspacing="0" cellpadding="0" class="clear c_account">
				  <tr >
				    <td width="80" align="center" colspan="2"><span class="b">用户修改的数据</span></td>
				  </tr>
				  <tr  <c:if test="${compareResult[8] eq 'false' }">class='red'</c:if> >
				    <td width="100"  align="right">资助机构Logo：</td> 
				    <td align="left">
				    	<c:choose>
				       		<c:when test="${empty agency.logoUrl }"><img id="agency_logo" src="${res}/images_v5/fund_logo/default_logo.jpg" width="50" height="50" /></c:when>
				       		<c:when test="${fn:contains(agency.logoUrl,'http')}"><img id="agency_logo" src="${agency.logoUrl}" width="50" height="50" /></c:when>
				       	</c:choose>
				    </td>
				  </tr>
				  <tr <c:if test="${compareResult[0] eq 'false' }">class='red'</c:if>  >
				    <td width="80" align="right">&nbsp;资助机构：</td>
				    <td align="left">${agency.nameZh}</td>
				  </tr>
				  <tr  <c:if test="${compareResult[1] eq 'false' }">class='red'</c:if> >
				    <td align="right">资助机构（英文）</td>
				    <td align="left">${agency.nameEn}</td>
				  </tr>
				  <tr  <c:if test="${compareResult[4] eq 'false' }">class='red'</c:if> >
				    <td align="right">机构缩写：</td>
				    <td align="left">
				         ${agency.abbr}</td>
				  </tr>
				  <tr  <c:if test="${compareResult[2] eq 'false' }">class='red'</c:if> >
				  	 <td align="right">机构类型：</td>
				    <td align="left" valign="top" colspan="2">
				    	 ${agency.typeView}
				      
				    </td>
				  </tr>
				  <tr  <c:if test="${compareResult[5] eq 'false' || compareResult[9] eq 'false' || compareResult[10] eq 'false' || compareResult[11] eq 'false'}">class='red'</c:if> >
				    <td align="right">机构地址：</td>
				    <td align="left">${agency.addrCounName}${agency.addrPrvName}${agency.addrCityName}${agency.address}</td>
				  </tr>
				  <tr  <c:if test="${compareResult[6] eq 'false' }">class='red'</c:if> >
				    <td align="right">URL网址：</td>
				    <td align="left">${agency.url}</td>
				  </tr>
				  <tr>
					<td align="right" colspan="2"></td>
				</tr>
				</table>
		
		</td>
		<td>
		<!-- 原始bpo审核数据 -->
			<table width="100%" border="2" cellspacing="0" cellpadding="0" class="clear c_account">
				  <tr>
				    <td width="80" align="center" colspan="2"><span class="b">关联的BPO库中的数据</span></td>
				  </tr>
				  <tr>
				    <td width="100"  align="right">资助机构Logo：</td> 
				    <td align="left">
				    	<c:choose>
				       		<c:when test="${empty parentAgency.logoUrl }"><img id="agency_logo" src="${res}/images_v5/fund_logo/default_logo.jpg" width="50" height="50" /></c:when>
				       		<c:when test="${fn:contains(parentAgency.logoUrl,'http')}"><img id="agency_logo" src="${parentAgency.logoUrl}" width="50" height="50" /></c:when>
				       	</c:choose>
				    </td>
				  </tr>
				  <tr>
				    <td width="80" align="right">&nbsp;资助机构：</td>
				    <td align="left">${parentAgency.nameZh}</td>
				  </tr>
				  <tr>
				    <td align="right">资助机构（英文）</td>
				    <td align="left">${parentAgency.nameEn}</td>
				  </tr>
				  <tr>
				    <td align="right">机构缩写：</td>
				    <td align="left">
				         ${parentAgency.abbr}</td>
				  </tr>
				  <tr>
				  	 <td align="right">机构类型：</td>
				    <td align="left" valign="top" colspan="2">
				    	 ${parentAgency.typeView}
				      
				    </td>
				  </tr>
				  <tr>
				    <td align="right">机构地址：</td>
				    <td align="left">${parentAgency.addrCounName}${parentAgency.addrPrvName}${parentAgency.addrCityName}${parentAgency.address}</td>
				  </tr>
				  <tr>
				    <td align="right">URL网址：</td>
				    <td align="left">${parentAgency.url}</td>
				  </tr>
				  <tr>
					<td align="right" colspan="2"></td>
				</tr>
				</table>
		</td>
	</tr>
</table>
<br /><br />
说明:
不同项:红色字体或者所在行是红色框.
</div>
</body>
</html>