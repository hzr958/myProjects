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
<link rel="stylesheet" href="${resmod}/css/public.css" type="text/css">
</head>
<script type="text/javascript">
    $(function(){
        document.getElementsByClassName("new-design_infor-container")[0].style.minHeight = window.innerHeight - 508 + "px";
    })
</script>
<body style="height: 100%;  display: flex; justify-content: center; flex-direction: column; ">
  <div class="new-design_infor-container">
    <div class="new-design_infor" style="width:250px; min-height: 240px;">
      <h4>
        <spring:message code="pub.view.not.exists" />
      </h4>
    </div>
  </div>
</body>
</html>