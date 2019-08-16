<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基金管理</title>
<script type="text/javascript">
  var resctx = "${resmod}";
  var respath = "${resmod}";
  var ctxpath = "${ctx}";
  var loacle = '${locale }'
</script>
<link href="${resmod}/css_v5/rol_header.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/sie_pop.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${resmod}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/pop.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.thickbox.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css_v5/plugin/jquery.discipline.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css_v5/plugin/jquery.autoword.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css_v5/plugin/jquery.complete.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/smate-pc/css/new-jquerythick.css" />
<script type="text/javascript" src="${resmod}/js_v5/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.proceedingwin.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/rol_common.js"></script>
<script type='text/javascript' src='${resmod}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.discipline.bpo.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.complete.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/link.status.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/loadding_div.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/bpo_fund/agency.maint.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/bpo_fund/category.thickbox.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/json2.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/newstructuresubject.js"></script>
</head>
<body style="overflow: hidden;">
  <div id="content" style="height: 560px; overflow-y: hidden; overflow-y: auto;">
    <div id="con_one_1" class="pro_fund_main">
      <div class="Contentbox">
        <div id="con_two_1" class="hover">
          <p class=""
            style="margin-bottom: 10px; border-bottom: 1px #CCC dashed; line-height: 220%; padding-left: 10px;">
            <span style="font-size: 14px; font-weight: bold;">基本信息</span>
          </p>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td align="right" width="30%" style="font-weight: bold">基金Id：</td>
              <td align="left">${thirdSourcesFund.id }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">基金机会名称-中文：</td>
              <td align="left">${thirdSourcesFund.fundTitleCn }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">基金机会名称-英文：</td>
              <td align="left">${thirdSourcesFund.fundTitleEn }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">基金机会简称：</td>
              <td align="left">${thirdSourcesFund.fundTitleAbbr }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">基金机会编号：</td>
              <td align="left">${thirdSourcesFund.fundNumber }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">基金机会描述：</td>
              <td align="left">${thirdSourcesFund.fundDesc }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">基金类型：</td>
              <td align="left">${thirdSourcesFund.fundType }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">分类标准：</td>
              <td align="left">${thirdSourcesFund.disciplineClassificationType }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">适用分类：</td>
              <td align="left">${thirdSourcesFund.disciplineLimit }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">关键词：</td>
              <td align="left">${thirdSourcesFund.fundKeywords }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">基金年度：</td>
              <td align="left">${thirdSourcesFund.fundYear }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">申请开始日期-申请结束日期：</td>
              <td align="left"><c:if test="${!empty thirdSourcesFund.applyDateStart}">
                  <fmt:formatDate value="${thirdSourcesFund.applyDateStart }" pattern="yyyy/MM/dd" />
                </c:if> <c:if test="${!empty thirdSourcesFund.applyDateStart && !empty thirdSourcesFund.applyDateEnd}">
               - 
              </c:if> <c:if test="${!empty thirdSourcesFund.applyDateEnd}">
                  <fmt:formatDate value="${thirdSourcesFund.applyDateEnd }" pattern="yyyy/MM/dd" />
                </c:if></td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">资助机构：</td>
              <td align="left"><c:if test="${empty thirdSourcesFund.agencyViewName }">${thirdSourcesFund.fundingAgency }</c:if>
                <c:if test="${not empty thirdSourcesFund.agencyViewName }">${thirdSourcesFund.agencyViewName }</c:if></td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">申报指南网址：</td>
              <td align="left">${thirdSourcesFund.declareGuideUrl }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">申报网址：</td>
              <td align="left">${thirdSourcesFund.declareUrl }</td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">附件地址：</td>
              <td align="left">
                <c:if test="${not empty thirdSourcesFund.accessoryUrlView }">
                  <c:forEach var="urlView" items="${thirdSourcesFund.accessoryUrlView}">
                                                  文件名:${urlView.name } &nbsp;地址:${urlView.url }<br />
                  </c:forEach>
                </c:if>
              </td>
            </tr>
            <%-- <tr>
              <td align="right" style="font-weight: bold">基金机会状态：</td>
              <td align="left"><s:if test="#thirdSourcesFund.status != 0 ">删除</s:if> <s:else>正常</s:else></td>
            </tr> --%>
            <tr>
              <td align="right" style="font-weight: bold">审核状态：</td>
              <td align="left"><s:if test="#thirdSourcesFund.auditStatus == 1 ">审核通过</s:if>
                <s:if test="#thirdSourcesFund.auditStatus == 2 ">审核拒绝</s:if> <s:else>未审核</s:else></td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">创建时间：</td>
              <td align="left"><fmt:formatDate value="${thirdSourcesFund.createTime }"
                  pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
              <td align="right" style="font-weight: bold">更新时间：</td>
              <td align="left"><fmt:formatDate value="${thirdSourcesFund.updateTime }"
                  pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
  <div class="pop_buttom">
    <a onclick="parent.$.Thickbox.closeWin();" class="uiButton text14 mright20">关闭</a>
  </div>
</body>
</html>