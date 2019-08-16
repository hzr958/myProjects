<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>科研之友</title>
<meta charset="utf-8">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript">
function openChat(){
  //PC端地址，目前没有PC端页面，暂时不需要
  var pcChatUrl = "http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&cref=http://www.scholarmate.com/center-oauth/WEB-INF/jsp/sns/V_SNS_index.jsp&ref=&pt=scholarmate kefu&f=1&ty=1&ap=&as=&aty=&a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no";
  var mobileChatUrl = "http://wpa.qq.com/msgrd?v=3&uin=800018382&site=qq&menu=yes";
  var u = navigator.userAgent;
  //判断是移动端还是PC端
  if(/Android|webOS|iPhone|iPod|BlackBerry/i.test(u)) {
    window.open(mobileChatUrl);
  }else{
    window.open(pcChatUrl);
  }
  
}
</script>
</head>
<body>
  <div class="Prohibition-use_container">
    <div class="Prohibition-use_avator">
      <img src="${resmod}/smate-pc/img/new-Privacy_tip.png">
    </div>
    <div class="Prohibition-use_content">
      <div class="Prohibition-use_title">很遗憾，你的账号暂停使用</div>
      <div class="Prohibition-use_service">
        我们判断你的行为与操作不符合我们的<a target="_blank" href="/resscmwebsns/html/condition_zh_CN.html"> 服务协议与条款，</a>并已经暂停了你的账号使用。
      </div>
      <div class="Prohibition-use_complaint">
        如确定你的账号并未违规，请点击<a onclick="openChat();">此处投诉</a>
      </div>
    </div>
  </div>
</body>
</html>