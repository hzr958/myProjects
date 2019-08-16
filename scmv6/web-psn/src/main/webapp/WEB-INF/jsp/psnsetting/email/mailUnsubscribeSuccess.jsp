<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>邮件退订</title>
</head>
<body>
  <div id="content">
    <div class="system-point">
      <div class="ico_note"></div>
      <div class="system-content">
        <!--  判断退订结果，根据结果显示不同的信息  tsz -->
        <c:if test="${cancleMark==0 }">
          <p class="cu14">
            <s:text name='sub.sys.tip' />
            <s:text name='sub.sys.tip.success' />
          </p>
        </c:if>
        <c:if test="${ cancleMark==1 }">
          <p class="cu14">
            <s:text name='sub.sys.tip' />
            <s:text name='sub.sys.tip.success' />
          </p>
        </c:if>
        <p class="f666 mtop5">
          <s:text name='sub.address' />
          <span class=" red">${mail}</span>&nbsp;
          <c:if test="${ cancleMark==1 }">
            <s:text name='sub.success' />
          </c:if>
          <c:if test="${ cancleMark==0 }">
            <s:text name='sub.success' />
          </c:if>
        </p>
      </div>
    </div>
  </div>
</body>
</html>