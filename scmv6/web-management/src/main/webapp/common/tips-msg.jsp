<%@ page language="java" pageEncoding="UTF-8"%>
<%//显示操作消息%>
<div style="height: 30px; padding: 0 0 6px 0; display: none;" id="tip_msg_box" onclick="$(this).fadeOut();">
  <div class="tips" style="height: 26px;">
    <div class="tips_close">
      <a style="cursor: pointer;"> <img src="${res}/images/tips_close.gif" width="14" height="14" border="0"
        align="middle" /></a>
    </div>
    <div id="tips_msg"></div>
  </div>
</div>
<%
/**
一、css,js导入
	<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${res }/js/CommonUI.js"></script>

二、在需要显示操作消息的位置添加
	include file="/common/tips-msg.jsp"

三、调用显示正确操作信息JS方法
	方式1:show_msg_tips(type,msg)
	方式2:show_msg_tips(type,msg,width)
  参数说明
	type：成功[yes或success],警告[warn或warning],失败[error或wrong]
	msg：显示的消息
	width：显示条的宽度
备注：(方式1和2)显示时间默认为5秒
四、手动关闭显示的操作消息
    close_msg_tips()
*/
%>