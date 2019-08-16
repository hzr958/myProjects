<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成果认领推荐</title>
</head>

<body>
<table width="700" border="0" align="center" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;margin-left:45px;">
  <tr>
    <td height="20" align="center" style="color:#656565; font-size:12px; font-family:Arial, Helvetica, '宋体';">如邮件不能正常显示，请打开下面链接查看。</td>
  </tr>
  <tr>
    <td height="25" align="center">
    <#if (viewMailUrl?exists)> <a href="${viewMailUrl}" target="_blank">${viewMailUrl}</a><#else><a href="#">http://www.xxxxxx.com</a></#if></td>
    </td>
  </tr>
</table>
<table width="700" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#4f81bc" style="margin-left:45px;">
  <tr>
    <td height="30" bgcolor="#4f81bc" style="color:#FFF; padding-left:20px;"><strong>成果认领推荐</strong></td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF" style="padding:20px; font-size:12px; font-family:Arial, Helvetica, '宋体'; color:#333; line-height:25px;">

尊敬的 <#if (userName?exists)> ${userName}<#else> AA</#if>：<br />

您可能是以下论文成果的作者/合作者：<br />
<div style="width:630px;line-height:120%;">
<#if (pubList?exists)>
<#list pubList as pubItem>
<p style="padding-left:50px">${pubItem.TITLE}</p>
</#list>
<#else>
<p style="padding-left:50px">Aaa</p>
<p style="padding-left:50px">Bbb</p>
</#if>
<div>
<span style="background-color:#92d050; color:#FFF;">
<div>
<#if (pubConfirmUrl?exists)>
<table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
      <tr>
          <td height="22" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${mailBaseUrl?replace("&service=target2",pubConfirmUrl)?replace("target3",sgparams16) }" target="_blank" title="点击认领成果" 
          style="font-size:12px; color:#3f68a8; font-family:Arial, Helvetica, sans-serif;  text-align:center; text-align:center; font-weight:bold; text-decoration:none;">  点击认领成果  </a></td>
      </tr>
 </table>
<#else>
<table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
      <tr>
          <td height="22" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><label style="font-size:12px; color:#3f68a8; font-family:Arial, Helvetica, sans-serif;  text-align:center; text-align:center; font-weight:bold; text-decoration:none;">点击认领成果</label></td>
      </tr>
 </table>
</#if>
</div>
</span><br /><br />

<span style="line-height:18px;">认领成果后，您可以方便的生成个人主页和图形化科研简历，同时利用专业化科研社区网络推广科研成果，提高论文引用。 </span><br />

<br /><br />

<strong>科研之友</strong>服务小组<br>
<strong>科研社区网络，成就创新梦想</strong><br>
<ADDRESS>Email：support@scholarmate.com</ADDRESS><br><hr style="margin:-12px;0"><br />
<div style="color:#A6A6A6;font-size:12px">
此邮件为科研之友系统发出，请勿回复。如果您不想以后再收到此类邮件，点击<#if (screenMailUrl?exists)><a style="text-decoration:none;" href="${screenMailUrl}" target="_blank">此处</a><#else><a href="#">此处</a></#if>取消订阅。<br><br>
科研之友 © 2014 爱瑞思 粤ICP备11010329号<br>
查看我们的<a href="https://www.scholarmate.com/resscmwebsns/html/policy_zh_CN.html">隐私政策</a>和<a href="https://www.scholarmate.com/resscmwebsns/html/condition_zh_CN.html">服务条款</a>。
</div>
</td>
</tr>
</table>
<#if (proLoad?exists)>
<#if (isView?exists)>
	<#if isView != "view">
	<iframe src="${proLoad}" style="display:none"></iframe>
	</#if>
	<#else>
	<iframe src="${proLoad}" style="display:none"></iframe>
</#if>
</#if>
</body>
</html>
