<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资助类别对比</title>
<script type="text/javascript">var resctx="${res}";var respath="${res}";var ctxpath="${ctx}";var loacle='${locale }'</script>
<link href="${res }/css_v5/rol_header.css" rel="stylesheet" type="text/css" />
<link href="${res }/css_v5/rol_common.css" rel="stylesheet" type="text/css" />
<link href="${res }/css_v5/rol_public.css" rel="stylesheet" type="text/css" />
<link href="${res }/css_v5/rol_footer.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"	href="${res}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen"	href="${res}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/pop.css" />
<link rel="stylesheet" type="text/css"	href="${res }/css_v5/plugin/jquery.thickbox.css" />
<link type="text/css" rel="stylesheet" href="${res }/css_v5/plugin/jquery.discipline.css"  />
<link type="text/css" rel="stylesheet" href="${res}/css_v5/plugin/jquery.autoword.css"/>
<link type="text/css" rel="stylesheet" href="${res}/css_v5/plugin/jquery.complete.css"/>
<script type="text/javascript" src="${res}/js_v5/common.ui.js"></script>
<script type="text/javascript" src="${res}/js_v5/jquery.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.proceedingwin.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${res}/js_v5/rol_common.js"></script>
<script type='text/javascript' src='${res}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.discipline.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.complete.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${res}/js_v5/link.status.js"></script>
<script type="text/javascript" src="${res}/js_v5/loadding_div.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${res}/js_v5/json2.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var fileJson = ${empty category.fileList?"[]":category.fileList};
    if(fileJson && fileJson.length>0){
    	for ( var i = 0; i < fileJson.length; i++) {
    		$("#viwe_files_table").append("<tr><td align=\"center\">"+(i+1)+"</td><td align=\"left\"><a href=\""+fileJson[i].filePath+"\" class=\"Blue file\">"+fileJson[i].fileName+"</a></td><td align=\"center\"></td></tr>");
    		$("#viwe_files_table").css("display","");
    	}
    };
    var parentFileJson = ${empty parentCategory.fileList?"[]":parentCategory.fileList};
    if(parentFileJson && parentFileJson.length>0){
    	for ( var i = 0; i < parentFileJson.length; i++) {
    		$("#parent_viwe_files_table").append("<tr><td align=\"center\">"+(i+1)+"</td><td align=\"left\"><a href=\""+parentFileJson[i].filePath+"\" class=\"Blue file\">"+parentFileJson[i].fileName+"</a></td><td align=\"center\"></td></tr>");
    		$("#parent_viwe_files_table").css("display","");
    	}
    };
});
   
</script>
</head>
<body>
<div id="con_one_1" class="pro_fund_main">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="clear c_account">
	<tr>
	<!-- 现在修改的数据 -->
	<td>
	<table width="100%" border="2" cellspacing="0" cellpadding="0" class="clear c_account">
		 <tr >
	 	   <td width="80" align="center" colspan="2"><span class="b">用户修改的数据</span></td>
	     </tr>
		<tr <c:if test="${compareResult[0] eq 'false' }">class='red'</c:if>>
			<td width="170" align="right">资助机构：</td>
			<td align="left">
			<s:iterator value="agencyList" var="agency">
				<c:if test="${agency.id eq category.agencyId}">
				${agency.name}
				</c:if>
			</s:iterator>
			<s:iterator value="compareResult" var="obj">
				${obj }
			</s:iterator>
		  </td>
		</tr>
		<tr>
			<tr <c:if test="${compareResult[1] eq 'false' }">class='red'</c:if>>
				<td align="right">类别名称：</td>
				<td align="left">${fn:escapeXml(category.nameZh)}&nbsp;&nbsp;<span class="f666">（中文）</span></td>
			</tr>
			<tr <c:if test="${compareResult[2] eq 'false' }">class='red'</c:if>>
				<td align="right">&nbsp;</td>
				<td align="left">${fn:escapeXml(category.nameEn)}&nbsp;&nbsp;<span	class="f666">（英文）</span></td>
			</tr>
			<tr <c:if test="${compareResult[3] eq 'false' }">class='red'</c:if>>
				<td align="right">资助编号：</td>
				<td align="left">
					${fn:escapeXml(category.code)}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
			   </td>
			</tr>
			<tr <c:if test="${compareResult[4] eq 'false' }">class='red'</c:if>>
				<td align="right">资助类别简称：</td>
				<td align="left">
					${fn:escapeXml(category.abbr)}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
			   </td>
			</tr>
			<tr <c:if test="${compareResult[6] eq 'false' }">class='red'</c:if>>
				<td align="right">描述：</td>
				<td align="left"><label for="textarea"></label> ${category.description}</td>
			</tr>
			<tr <c:if test="${compareResult[9] eq 'false' }">class='red'</c:if>>
				<td align="right">学科：</td>
				<td align="left" valign="top">
				 	${category.disJson}
				   
				</td>
			</tr>
			<tr <c:if test="${compareResult[11] eq 'false' }">class='red'</c:if>>
				<td align="right" valign="middle">关键词：</td>
				<td align="left">
					${category.keywordList}
				</td>
			</tr>
			<tr>
				<td align="right" colspan="2"></td>
			</tr>
			<tr <c:if test="${compareResult[14] eq 'false' || compareResult[15] eq 'false' }">class='red'</c:if>>
				<td align="right" >申请日期：</td>
				<td align="left" valign="top" >
					<s:date name="category.startDate" format="yyyy/MM/dd"/> &nbsp;到&nbsp;
					<s:date name="category.endDate" format="yyyy/MM/dd"/> &nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			<tr <c:if test="${compareResult[13] eq 'false' }">class='red'</c:if>>
				<td align="right" >预计资助金额：</td>
				<td align="left" valign="top" >
					${category.strength}&nbsp;万元
				</td>
			</tr>
			<tr <c:if test="${compareResult[29] eq 'false' }">class='red'</c:if>>
				<td align="right" >是否配套：</td>
				<td align="left" valign="top" >
					${category.isMatch}&nbsp;
				</td>
			</tr>
			<tr <c:if test="${compareResult[30] eq 'false' }">class='red'</c:if>>
				<td align="right" >比例：</td>
				<td align="left" valign="top" >
					${category.percentage}&nbsp;
				</td>
			</tr>
			<tr <c:if test="${compareResult[7] eq 'false' }">class='red'</c:if>>
				<td align="right">网址：</td>
				<td align="left">${fn:escapeXml(category.guideUrl)}</td>
			</tr>
			<tr <c:if test="${compareResult[27] eq 'false' }">class='red'</c:if>>
				<td align="right">附件：</td>
				<td align="left">
					<table id="viwe_files_table"  border="0" cellspacing="0" cellpadding="0" >
							<tr>
								<td>序号</td>
								<td>文件名</td>
							</tr>
					</table>
				</td>
			</tr>
			
			<tr <c:if test="${compareResult[10] eq 'false' }">class='red'</c:if>>
				<td align="right" valign="middle">适合地区：</td>
				<td align="left">
						${category.regionList }
				</td>
			</tr>
			</tr>
			<tr <c:if test="${compareResult[16] eq 'false' }">class='red'</c:if>>
				<td align="right" >职称：</td>
				<td align="left" valign="top" >
					<s:iterator value="titleList" var="tmp">
						<c:if test="${tmp.id ne 340}">
							 ${fn:containsIgnoreCase(category.titleRequire1,tmp.id)?tmp.name:'' }&nbsp;&nbsp;
					 	</c:if>
					 </s:iterator>
				</td>
			</tr>
			<tr <c:if test="${compareResult[17] eq 'false' }">class='red'</c:if>>
				<td align="right" >学位：</td>
				<td align="left" valign="top" >
					 <c:if test="${fn:contains(category.degreeRequire1,'200')}">
					 	所有
					 </c:if>
					 <c:if test="${fn:contains(category.degreeRequire1,'220')}">
					 	本科或以上
					 </c:if>
					 <c:if test="${category.degreeRequire1=='230'}">
					 	博士或以上
					 </c:if>
				</td>
			</tr>
			<tr>
				<td height="30" align="right">出生年月：</td>
				<td align="left">
					 从&nbsp;
					 <span <c:if test="${compareResult[23] eq 'false' }">class='red'</c:if>>
					 <s:date name="category.birthLeast" format="yyyy/MM/dd"/>
					 </span>
					 &nbsp;到&nbsp;
					 <span <c:if test="${compareResult[24] eq 'false' }">class='red'</c:if>>
					 <s:date name="category.birthMax" format="yyyy/MM/dd"/>
					 </span>
				</td>
			</tr>
			<tr>
				<td align="right" colspan="2"></td>
			</tr>
	</table>
	</td>
	<!--原始bpo审核数据  -->
	<td>
	<table width="100%" border="2" cellspacing="0" cellpadding="0" class="clear c_account">
		<tr >
			<td width="80" align="center" colspan="2"><span class="b">关联的BPO库中的数据</span></td>
		</tr>
		<tr <c:if test="${compareResult[0] eq 'false' }">class='red'</c:if>>
			<td width="170" align="right">资助机构：</td>
			<td align="left">
			<s:iterator value="agencyList" var="agency">
				<c:if test="${agency.id eq parentCategory.agencyId}">
				${agency.name}
				</c:if>
			</s:iterator>
				<s:iterator value="compareResult" var="obj">
					${obj }
				</s:iterator>
		  </td>
		</tr>
		<tr>
			<tr <c:if test="${compareResult[1] eq 'false' }">class='red'</c:if>>
				<td align="right">类别名称：</td>
				<td align="left">${fn:escapeXml(parentCategory.nameZh)}&nbsp;&nbsp;<span class="f666">（中文）</span></td>
			</tr>
			<tr <c:if test="${compareResult[2] eq 'false' }">class='red'</c:if>>
				<td align="right">&nbsp;</td>
				<td align="left">${fn:escapeXml(parentCategory.nameEn)}&nbsp;&nbsp;<span	class="f666">（英文）</span></td>
			</tr>
			<tr <c:if test="${compareResult[3] eq 'false' }">class='red'</c:if>>
				<td align="right">资助编号：</td>
				<td align="left">
					${fn:escapeXml(parentCategory.code)}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
			   </td>
			</tr>
			<tr <c:if test="${compareResult[4] eq 'false' }">class='red'</c:if>>
				<td align="right">资助类别简称：</td>
				<td align="left">
					${fn:escapeXml(parentCategory.abbr)}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
			   </td>
			</tr>
			<tr <c:if test="${compareResult[6] eq 'false' }">class='red'</c:if>>
				<td align="right">描述：</td>
				<td align="left"><label for="textarea"></label> ${parentCategory.description}</td>
			</tr>
			<tr <c:if test="${compareResult[9] eq 'false' }">class='red'</c:if>>
				<td align="right">学科：</td>
				<td align="left" valign="top">
				 	${parentCategory.disJson}
				</td>
			</tr>
			<tr <c:if test="${compareResult[11] eq 'false' }">class='red'</c:if>>
				<td align="right" valign="middle">关键词：</td>
				<td align="left">
					${parentCategory.keywordList}
				</td>
			</tr>
			<tr>
				<td align="right" colspan="2"></td>
			</tr>
			<tr <c:if test="${compareResult[14] eq 'false' || compareResult[15] eq 'false' }">class='red'</c:if>>
				<td align="right" >申请日期：</td>
				<td align="left" valign="top" >
					<s:date name="parentCategory.startDate" format="yyyy/MM/dd"/> &nbsp;到&nbsp;
					<s:date name="parentCategory.endDate" format="yyyy/MM/dd"/>
				</td>
			</tr>
			<tr <c:if test="${compareResult[13] eq 'false' }">class='red'</c:if>>
				<td align="right" >预计资助金额：</td>
				<td align="left" valign="top" >
					${parentCategory.strength}&nbsp;万元
				</td>
			</tr>
			<tr <c:if test="${compareResult[29] eq 'false' }">class='red'</c:if>>
				<td align="right">是否配套：</td>
				<td align="left">${parentCategory.isMatch}</td>
			</tr>
			<tr <c:if test="${compareResult[30] eq 'false' }">class='red'</c:if>>
				<td align="right">比例：</td>
				<td align="left">${parentCategory.percentage}</td>
			</tr>
			<tr <c:if test="${compareResult[7] eq 'false' }">class='red'</c:if>>
				<td align="right">网址：</td>
				<td align="left">${fn:escapeXml(parentCategory.guideUrl)}</td>
			</tr>
			<tr <c:if test="${compareResult[27] eq 'false' }">class='red'</c:if>>
				<td align="right">附件：</td>
				<td align="left">
					<table id="parent_viwe_files_table"  border="0" cellspacing="0" cellpadding="0" >
							<tr>
								<td>序号</td>
								<td>文件名</td>
							</tr>
					</table>
				</td>
			</tr>
			
			<tr <c:if test="${compareResult[10] eq 'false' }">class='red'</c:if>>
				<td align="right" valign="middle">适合地区：</td>
				<td align="left">
						${parentCategory.regionList }
				</td>
			</tr>
			<tr <c:if test="${compareResult[16] eq 'false' }">class='red'</c:if>>
				<td align="right" valign="middle">职称：</td>
				<td align="left">
					<s:iterator value="titleList" var="tmp">
						<c:if test="${tmp.id ne 340}">
							 ${fn:containsIgnoreCase(parentCategory.titleRequire1,tmp.id)?tmp.name:'' }&nbsp;&nbsp;
					 	</c:if>
					 </s:iterator>
				</td>
			</tr>
			<tr <c:if test="${compareResult[17] eq 'false' }">class='red'</c:if>>
				<td align="right" valign="middle">学位：</td>
				<td align="left">
					 <c:if test="${fn:contains(parentCategory.degreeRequire1,'200')}">
					 	所有
					 </c:if>
					 <c:if test="${fn:contains(parentCategory.degreeRequire1,'220')}">
					 	本科或以上
					 </c:if>
					 <c:if test="${parentCategory.degreeRequire1=='230'}">
					 	博士或以上
					 </c:if>
				</td>
			</tr>
			<tr>
				<td height="30" align="right">出生年月：</td>
				<td align="left">
					 从&nbsp;
					 <span <c:if test="${compareResult[23] eq 'false' }">class='red'</c:if>>
					 <s:date name="parentCategory.birthLeast" format="yyyy/MM/dd"/>
					 </span>
					 &nbsp;到&nbsp;
					 <span <c:if test="${compareResult[24] eq 'false' }">class='red'</c:if>>
					 <s:date name="parentCategory.birthMax" format="yyyy/MM/dd"/>
					 </span>
				</td>
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