<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ressie }/css/unit.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert title here</title>
</head>
<body style="overflow-x: hidden;">
  <div class="conter_finished">
    <!--部门-->
    <div id="con_five_1" style="display: block">
      <ul class="finish version-tip">
        <li><img src="${ressie}/images/pass.png" alt=""></li>
        <li><h1 class="finish_catalog">${impRestut.totalResult}条项目导入成功</h1></li>
        <li><a href="/prjweb/project/importfile" class="martter-demo-step finish_catalog_step">继续</a> <a
          href="/prjweb/project/maint" class="martter-demo-browse">完成</a></li>
      </ul>
    </div>
    <div id="con_five_2" style="display: none"></div>
    <div id="con_five_3" style="display: none"></div>
    <div id="con_five_4" style="display: none"></div>
  </div>
</body>
</html>