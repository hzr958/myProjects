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
    $(function(){
        document.getElementsByClassName("psn-check__tip")[0].style.minHeight = window.innerHeight - 208 + "px";
    })
    function linkOthers(){
    	var searchString =$("#titleString").val();
    	link = "/pub/search/pdwhpaper?searchString="+encodeURIComponent(searchString);
        window.location.href=link;
    }
    function login(){
        var service = window.location.href;
        link = "/oauth/index?service="+encodeURIComponent(service);
        window.location.href=link;
    }
</script>
</head>
<body>
  <input id="titleString" type="hidden" value="${pubDetailVO.title}">
  <div class="psn-check__tip" style="display: flex;">
    <div class="psn-check__icon"></div>
    <c:if test="${pubDetailVO.psnId > 0 }">
      <span><spring:message code="pub.details.no.autority.tip" /></span>
      <a style="cursor: pointer" onclick="linkOthers()"> <span class="psn-checktitle__onload"><spring:message
            code="pub.details.no.autority.other.projects" /></span>
      </a>
    </c:if>
    <c:if test="${pubDetailVO.psnId == null || pubDetailVO.psnId == 0 }">
      <span> <spring:message code="pub.details.no.autority.tip" />
      </span>
      <a style="cursor: pointer" onclick="login()"> <span class="psn-checktitle__onload"><spring:message
            code="pub.details.immediately.login" /></span>
      </a>
      <spring:message code="pub.details.search.project" />
    </c:if>
  </div>
</body>
</html>