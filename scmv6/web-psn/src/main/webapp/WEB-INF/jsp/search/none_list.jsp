<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<title>未找到与检索条件相关结果</title>
</head>
<body>
  <div class="no_effort">
    <h2>
      <s:text name="psn.nolist.tip.title" />
    </h2>
    <div class="no_effort_tip">
      <span><s:text name="psn.nolist.tip.Suggestions" /></span>
      <p>
        <s:text name="psn.nolist.tip.content1" />
      </p>
      <p>
        <s:text name="psn.nolist.tip.content2" />
      </p>
      <p>
        <s:text name="psn.nolist.tip.content3" />
      </p>
    </div>
  </div>
</body>
</html>
