<html>
<head>
  <title></title>
</head>
<body>
<#if (psnname?exists) >  
<br/>${psnname}<br/><br/>
</#if>

您好:<br/><br/>

您已开通了${insName}科研在线，请通过以下方式登录：<br/><br/>

网址：http://${insDomain} <br/>
帐号：${account} <br/>
<#if (password?exists) >  
密码: ${password}<br/>
</#if>
<br/><br/>


通过使用科研之友机构版，您可以：<br/><br/>

1) 帮助单位进行申请与在研项目检索统计。<br/>
2) 通过成果检索工具快速创建本单位成果库。<br/>
3) 更好地支持基金委成果的收集和提交，如：结题报告成果、申请书成果等。<br/><br/>

科研之友机构版常见问题解答：<a href="http://${insDomain}/faq">http://${insDomain}/faq</a>  <br/>
科研之友机构版使用帮助中心：<a href="http://${insDomain}/help">http://${insDomain}/help</a> <br/><br/>

如果有任何问题，请发邮件至 support@scholarmate.com 或致电科研在线客户服务部。<br/>
 联系电话：0755-26712950 崔小姐 <br/><br/><br/>


科研之友机构版：让管理更简单！<br/>
<a href="http://sie.scholarmate.com">http://sie.scholarmate.com</a> <br/><br/>

</div>
</body>
</html>