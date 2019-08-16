<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<title></title>
<link href="${resmod }/css/public.css" rel="stylesheet" type="text/css" />
<%@ include file="/common/meta.jsp"%>
</head>
<body>
  <div style="padding-left: 350px; min-height: 800px;">
    <div class="tips" style="width: 600px">
      <div class="tips_close"></div>
      <div class="tips_info">
        <ul>
          <c:choose>
            <c:when test="threesys==1&&groupHave!=1">
              <li class="tips_wrong"><spring:message code="puburlview.msg.tip.sorry" /> <spring:message
                  code="publicationEdit.label.pubNotOwner" /></li>
            </c:when>
            <c:otherwise>
              <li class="tips_wrong"><spring:message code="puburlview.msg.tip.sorry" /> <spring:message
                  code="publicationEdit.label.pubNotExit" /></li>
            </c:otherwise>
          </c:choose>
        </ul>
      </div>
    </div>
  </div>
</body>
</html>