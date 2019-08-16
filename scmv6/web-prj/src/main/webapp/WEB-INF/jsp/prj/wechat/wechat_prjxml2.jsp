<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/new-confirmbox/confirm.css">
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/request-paper/addpaper.css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript">

$(function(){
	if("${wxOpenId}"){
		smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
	}
	if($("#isMyPrj").val() != "true"){
        //添加访问记录
        SmateCommon.addVisitRecord($("#ownerPsnId").val(),$("#des3PrjId").val(),4);
    }
	$("#title").html($("#title").text());
	
});
</script>
</head>
<body style="background-color: white;">
  <input type="hidden" name="ownerPsnId" id="ownerPsnId" value="${des3OwnerPsnId }" />
  <input type="hidden" name="des3PrjId" id="des3PrjId" value="${des3PrjId}" />
  <input type="hidden" name="isMyPrj" id="isMyPrj" value="${isMyPrj}" />
  <div id="header" class="fund__page-header">
    <span class="fund__page-icon"><i class="material-icons"
      onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i></span> <span class="fund__page-title">项目详情</span>
  </div>
  <div class="detail" style="padding-top: 65px">
    <s:if test="prjXml==null">
      <dl>
        <dd>未找到对应项目详情</dd>
      </dl>
    </s:if>
    <s:else>
      <x:parse xml="${prjXml}" var="myoutprj" />
      <dl>
        <h2 id="title">
          <!-- 标题 -->
          <c:set var="zhtitle">
            <x:out select="$myoutprj/data/project/@zh_title" escapeXml="false" />
          </c:set>
          <c:set var="entitle">
            <x:out select="$myoutprj/data/project/@en_title" escapeXml="false" />
          </c:set>
          <c:choose>
            <c:when test="${locale eq 'zh_CN' }">
              <c:if test="${!empty zhtitle}">
					${zhtitle}
				</c:if>
              <c:if test="${!empty entitle && empty zhtitle}">
					${entitle}
				</c:if>
            </c:when>
            <c:otherwise>
              <c:if test="${!empty entitle}">
					${entitle}
				</c:if>
              <c:if test="${!empty zhtitle && empty entitle}">
					${zhtitle}
				</c:if>
            </c:otherwise>
          </c:choose>
        </h2>
        <dt>
          <!-- 作者-->
          <c:set var="author_names">
            <x:out select="$myoutprj/data/project/@author_names" escapeXml="false" />
          </c:set>
          <c:if test="${!empty author_names}">${author_names}</c:if>
        </dt>
        <!-- 依托单位 -->
        <c:set var="ins_name">
          <x:out select="$myoutprj/data/project/@ins_name" />
        </c:set>
        <c:if test="${!empty ins_name}">
          <h5 style="line-height: 30px;">
            <%-- <s:text name="projectEdit.label.prj_ins" /> --%>
            依托单位:&nbsp;${ins_name}
          </h5>
        </c:if>
        <!--资助机构，类别  -->
        <c:set var="scheme_name">
          <x:out select="$myoutprj/data/project/@scheme_name" escapeXml="false" />
        </c:set>
        <c:set var="scheme_agency_name">
          <x:out select="$myoutprj/data/project/@scheme_agency_name" escapeXml="false" />
        </c:set>
        <c:if test="${!empty scheme_name || !empty scheme_agency_name}">
          <c:set var="zh_scheme">1</c:set>
        </c:if>
        <c:set var="scheme_name_en">
          <x:out select="$myoutprj/data/project/@scheme_name_en" escapeXml="false" />
        </c:set>
        <c:set var="scheme_agency_name_en">
          <x:out select="$myoutprj/data/project/@scheme_agency_name_en" escapeXml="false" />
        </c:set>
        <c:if test="${!empty scheme_name_en || !empty scheme_agency_name_en}">
          <c:set var="en_scheme">1</c:set>
        </c:if>
        <c:choose>
          <c:when test="${locale eq 'zh_CN' }">
            <c:if test="${!empty zh_scheme}">
              <h5 style="line-height: 30px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
                资助机构 - 类别:&nbsp;
                <c:out value="${scheme_agency_name}" />
                <c:if test="${!empty scheme_agency_name && !empty scheme_name}">-</c:if>
                <c:out value="${scheme_name }" />
              </h5>
            </c:if>
            <c:if test="${!empty en_scheme && empty zh_scheme}">
              <h5 style="line-height: 30px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
                资助机构 - 类别:&nbsp;
                <c:out value="${scheme_agency_name_en}" />
                <c:if test="${!empty scheme_agency_name_en && !empty scheme_name_en}">-</c:if>
                <c:out value="${scheme_name_en}" />
              </h5>
            </c:if>
          </c:when>
          <c:otherwise>
            <c:if test="${!empty en_scheme}">
              <h5 style="line-height: 30px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
                资助机构 - 类别:&nbsp;
                <c:out value="${scheme_agency_name_en}" />
                <c:if test="${!empty scheme_agency_name_en && !empty scheme_name_en}">-</c:if>
                <c:out value="${scheme_name_en}" />
              </h5>
            </c:if>
            <c:if test="${!empty zh_scheme && empty en_scheme}">
              <h5 style="line-height: 30px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
                资助机构 - 类别:&nbsp;
                <c:out value="${scheme_agency_name}" />
                <c:if test="${!empty scheme_agency_name && !empty scheme_name}">-</c:if>
                <c:out value="${scheme_name }" />
              </h5>
            </c:if>
          </c:otherwise>
        </c:choose>
        <!--  -->
        <c:set var="prj_internal_no">
          <x:out select="$myoutprj/data/project/@prj_internal_no" />
        </c:set>
        <c:if test="${!empty prj_internal_no}">
          <h5 style="line-height: 30px;">项目批准号（本机构）:&nbsp;${prj_internal_no}</h5>
        </c:if>
        <c:set var="prj_external_no">
          <x:out select="$myoutprj/data/project/@prj_external_no" />
        </c:set>
        <c:if test="${!empty prj_external_no}">
          <h5 style="line-height: 30px;">项目批准号（资助机构）:&nbsp;${prj_external_no}</h5>
        </c:if>
        <!-- 资金总数  -->
        <c:set var="amount">
          <x:out select="$myoutprj/data/project/@amount" />
        </c:set>
        <c:set var="amount_unit">
          <x:out select="$myoutprj/data/project/@amount_unit" />
        </c:set>
        <c:if test="${!empty prj_external_no}">
          <h5 style="line-height: 30px;">资金总数:&nbsp;${amount}&nbsp;${amount_unit}</h5>
        </c:if>
        <!-- 项目日期 -->
        <c:set var="start_year">
          <x:out select="$myoutprj/data/project/@start_year" />
        </c:set>
        <c:set var="start_month">
          <x:out select="$myoutprj/data/project/@start_month" />
        </c:set>
        <c:set var="start_day">
          <x:out select="$myoutprj/data/project/@start_day" />
        </c:set>
        <c:set var="end_year">
          <x:out select="$myoutprj/data/project/@end_year" />
        </c:set>
        <c:set var="end_month">
          <x:out select="$myoutprj/data/project/@end_month" />
        </c:set>
        <c:set var="end_day">
          <x:out select="$myoutprj/data/project/@end_day" />
        </c:set>
        <c:if test="${!empty start_year}">
          <h5 style="line-height: 30px;">
            项目日期:&nbsp;
            <c:if test="${!empty end_year}">
              <c:if test="${!empty start_month && !empty end_month}">${start_year}/${start_month }/${start_day}&nbsp;-&nbsp;${end_year}/${end_month}/${end_day}
            </c:if>
              <c:if test="${empty start_month && !empty end_month}">
                 ${start_year}&nbsp;-&nbsp;${end_year}/${end_month}/${end_day}
            </c:if>
              <c:if test="${!empty start_month && empty end_month}">
                ${start_year}/${start_month }/${start_day}&nbsp;-&nbsp;${end_year}
            </c:if>
              <c:if test="${empty start_month && empty end_month}">
                 ${start_year}&nbsp;-&nbsp;${end_year}
            </c:if>
            </c:if>
            <c:if test="${empty end_year}">
              <c:if test="${!empty start_month}">
                <c:if test="${!empty start_day}">
                    ${start_year}/${start_month }/${start_day}
                </c:if>
                <c:if test="${empty start_day}">
                    ${start_year}/${start_month }
                </c:if>
              </c:if>
              <c:if test="${empty start_month}">
                ${start_year}
            </c:if>
            </c:if>
          </h5>
        </c:if>
        <!-- 摘要 -->
        <p>
          <c:set var="zh_abstract">
            <x:out select="$myoutprj/data/project/@zh_abstract" escapeXml="false" />
          </c:set>
          <c:set var="en_abstract">
            <x:out select="$myoutprj/data/project/@en_abstract" escapeXml="false" />
          </c:set>
          <c:choose>
            <c:when test="${locale eq 'zh_CN' }">
              <c:if test="${!empty zh_abstract }">
							摘要:&nbsp;${zh_abstract }
					</c:if>
              <c:if test="${!empty en_abstract && empty zh_abstract}">
							摘要:&nbsp;${en_abstract }
					</c:if>
            </c:when>
            <c:otherwise>
              <c:if test="${!empty en_abstract}">
							摘要:&nbsp;${en_abstract }
					</c:if>
              <c:if test="${!empty zh_abstract && empty en_abstract }">
							摘要:&nbsp;${zh_abstract }
					</c:if>
            </c:otherwise>
          </c:choose>
        </p>
        <h3></h3>
        <!-- 关键字 -->
        <p>
          <c:set var="zh_keywords">
            <x:out select="$myoutprj/data/project/@zh_keywords" escapeXml="false" />
          </c:set>
          <c:set var="zh_keywords">${fn:replace(zh_keywords,' ','')}</c:set>
          <c:set var="zh_keywords">${fn:replace(zh_keywords,';','；')}</c:set>
          <c:set var="en_keywords">
            <x:out select="$myoutprj/data/project/@en_keywords" escapeXml="false" />
          </c:set>
          <c:choose>
            <c:when test="${locale eq 'zh_CN' }">
              <c:if test="${!empty zh_keywords}">
						关键词:&nbsp;${zh_keywords }&nbsp;
				</c:if>
              <c:if test="${!empty en_keywords && empty zh_keywords}">
						关键词:&nbsp;${en_keywords }
				</c:if>
            </c:when>
            <c:otherwise>
              <c:if test="${!empty en_keywords}">
						关键词:&nbsp;${en_keywords }
				</c:if>
              <c:if test="${!empty zh_keywords && empty en_keywords}">
						关键词:&nbsp;${zh_keywords }
				</c:if>
            </c:otherwise>
          </c:choose>
        </p>
    </s:else>
    <s:if test="hasLogin == 0">
      <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
    </s:if>
  </div>
</body>
</html>