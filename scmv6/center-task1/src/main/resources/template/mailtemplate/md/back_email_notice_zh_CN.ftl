<html>
<head>
  <title>退回邮件分析通知邮件</title>
</head>
<body>
您好！<br/>
    在分析邮箱中退回邮件时，发送用户<${useremail}>使用了忘记密码功能，但发出的邮件因各种原因没有成功递送到用户邮箱，请处理！<br/>
	<br/><br/>
   如浏览器不能正常显示，请打开下面链接<br/>
<a href="${viewMailPath!''}${viewMailUrl!'#'}" target="_blank" style="font-size:12px;">${viewMailPath!''}${viewMailUrl!'###'}</a>
<br/><br/>
</body>
</html>