<html>
<head>
  <title>邮件监控报警</title>
</head>
<body>
您好！<br/>
系统已检测到有异常邮件，具体消息如下<br/>
	节点：${nodeId}<br/>
	模板名称：${templateName}<br/>
	主题：${subject!'无'}<br/>
	报警时间：${createDate}<br/> 
请点击一下地址进行操作：<br/>
<a href="${url}">${url}</a><br/>
如果以上链接无法点击，请将上面的地址复制到您的浏览器的地址栏（浏览器上方输入网址的框）。

<br/><br/>
被监控的邮件预览<br/><br/>
${preview!'无'}
</body>
</html>