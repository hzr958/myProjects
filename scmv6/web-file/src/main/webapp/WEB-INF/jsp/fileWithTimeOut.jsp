<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<title><s:text name="fileDownLoad.label.fileWithTimeOutTitle"></s:text></title>
<link href="${res }/css/public.css" rel="stylesheet" type="text/css" />
<link href="/resmod/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<%@ include file="/common/meta.jsp"%>
</head>
<body>
  <div class="no_effort">
    <h2>
      <s:text name="fileDownLoad.label.fileWithTimeOut"></s:text>
      <a href="javascript:window.history.back(-1);"><s:text name="fileDownLoad.label.fileWithBack"></s:text></a>
    </h2>
    <div class="no_effort_tip"></div>
  </div>
  <!--   <div id="main" style="padding-left:15px;padding-right:15px;">
    <form name="mainform" id="mainform" action="" method="post">
	<div class="errorMessage" style="display:block;margin-top:50px; text-align:center;">
		<s:text name="fileDownLoad.label.fileWithTimeOut"></s:text>
		<a href="javascript :history.back(-1)"><s:text name="fileDownLoad.label.fileWithBack"></s:text></a>
	 </div>
	
  </form>
  </div> -->
</body>
</html>