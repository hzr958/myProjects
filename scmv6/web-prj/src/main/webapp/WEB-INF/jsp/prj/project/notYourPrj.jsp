<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<link href="${res }/css/public.css" rel="stylesheet" type="text/css" />
<meta charset="utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<title><s:text name="project.enter.project" /></title>
</head>
<body>
  <div style="padding-top: 40px; padding-left: 40px;">
    <div class="tips" style="width: 600px">
      <div class="tips_close"></div>
      <div class="tips_info">
        <ul>
          <li class="tips_wrong"><s:text name="projectType.msg.sorry" />，<s:text
              name="projectEdit.label.notYourPrj"></s:text></li>
        </ul>
      </div>
    </div>
  </div>
</body>
</html>