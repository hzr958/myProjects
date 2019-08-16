<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function refreshCode(){
	if("${needValidateCode}" == '1'){
	    document.getElementById("img_checkcode").src = "/oauth/validatecode.gif?date="+new Date().getTime();
    }
}
</script>
</head>
<body>
  权限系统登录页面 ${msg }
  <form action="${sysDomain }/oauth/login" id="loginForm" method="post">
    <input type="hidden" value="${token }" name="token"> <input type="text" value="${username}" id="userName"
      name="userName" class="l_input" /> <input type="password" value="${password }" id="password" name="password" />
    <br /> <input type="hidden" value="${needValidateCode}" id="needValidateCode" name="needValidateCode"
      class="l_input" />
    <c:if test="${needValidateCode eq '1'}">
      <input type="text" id="validateCode" name="validateCode" />
      <img id="img_checkcode" src="https://oauth.scholarmate.com/oauth/validatecode.gif" width="88" height="23"
        align="absmiddle" />
      <a onclick="refreshCode()" style="cursor: pointer" class="Blue u">刷新</a>
    </c:if>
    <br /> <input id="loginButton" type="submit" name="submit" value="提交" style="padding: 7px 30px; font-size: 14px;" />
  </form>
</body>
</html>