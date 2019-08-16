<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="${resmod}/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
</head>
<body>
  <!-- ===================================================== -->
  <div class="m-top" style="text-align: center; margin-right: 46px;">
    <a href="javascript:;" onclick="window.history.go(-1);" class="fl"><i class="material-icons ">keyboard_arrow_left</i></a>
    返回
  </div>
  <!-- ===================================================== -->
  <div class=" noRecord" style="margin-top: -40px">
    <div class="content">
      <div class="no_effort">
        <h2 class="tc">
          <spring:message code="pub.view.not.exists" />
        </h2>
<%--         <div class="no_effort_tip pl27">
          <span><s:text name="pub.view.contact" /> </span>
        </div> --%>
      </div>
    </div>
  </div>
  <%-- 	<div style="padding-top: 66px;">
		<div class="tips" style="width: 270px; margin-left:auto; margin-right:auto;">
			<div class="tips_close"></div>
			<div class="tips_info">
				<ul>
					<li class="tips_wrong">
						<s:text name="pub.view.not.exists"></s:text></li>
				</ul>
			</div>
		</div>
	</div> --%>
</body>
</html>