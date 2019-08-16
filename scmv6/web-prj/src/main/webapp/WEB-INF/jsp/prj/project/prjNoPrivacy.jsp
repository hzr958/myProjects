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
<link href="${resmod }/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
    function linkOthers(){
        window.location.href="/dynweb/main";
    }
    function login(){
        var service = window.location.href;
        link = "/oauth/index?service="+encodeURIComponent(service);
        window.location.href=link;
    }
    $(function(){
        document.getElementsByClassName("psn-check__tip")[0].style.minHeight = window.innerHeight - 208 + "px";
    })
</script>
</head>
<body>
  <div class="psn-check__tip" style="display: flex;">
    <div class="psn-check__icon"></div>
    <c:if test="${psnId > 0 }">
      <span><s:text name="project.details.no.autority.tip" /></span>
    </c:if>
    <c:if test="${psnId == null || psnId == 0 }">
      <span><s:text name="project.details.no.login.tip" /></span>
      <a style="cursor: pointer" onclick="login()"> <span class="psn-checktitle__onload"><s:text
            name="project.details.immediately.login" /></span>
      </a>
      <s:text name="project.details.search.project" />
    </c:if>
  </div>
</body>
</html>