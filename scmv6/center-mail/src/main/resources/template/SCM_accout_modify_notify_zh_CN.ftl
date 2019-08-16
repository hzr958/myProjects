<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科研之友修改登录帐号通知</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:0;padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:30px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td rowspan="4" valign="top">
                     <p style="margin:0; padding:0; margin-left:4px;">
                     ${username}，你的登录帐号<span style="color:#1e5494">${oldAccount}</span>
                     于${modifyDate}更改为<span style="color:#1e5494">${newAccount}</span>
                     </p>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
           <tr>
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;"></td>
          </tr>
        </table>
    </td>
  </tr>
  </table>
 <!-- 页脚内容(样式内容) -->
 <table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style=" font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:12px; color:#999999; padding:10px 0; margin-top:5px;">
  <tr align="left" valign="center" style="color:#999999; font-size:12px; line-height:150%;">
  	<td align="left" valign="center" style="line-height:20px;"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_code.png" width="74" height="74" style=" float:left; padding-bottom:15px;padding-right:10px;" /><span style="margin-top:20px; float:left; color:#666666; font-size:14px;">扫描二维码关注科研之友公众号，及时获取基金、论文等最新科研信息。<br>或者关注我们官方微博：<a href="http://www.weibo.com/scholarmate"  target="_blank" style="color:#666666; font-size:14px; text-decoration:none;">weibo.com/scholarmate</a></span></td>
  </tr>
  <tr>
    <td align="left" valign="bottom" style="color:#999999; font-size:12px; border-top:1px solid #cccccc; line-height:150%; padding-top:10px;">
此邮件为系统发出，请勿回复。如对我们的产品有疑问，请发送邮件至<a href="mailto:support@scholarmate.com"  target="_blank" style="color:#55b1f5; text-decoration:none;">support@scholarmate.com</a>。
   <#if (viewMailUrl?exists)>如邮件不能正常显示，<a href="${viewMailPath!''}${viewMailUrl}" target="_blank" style="color:#55b1f5; width:420px;text-decoration:none;">请点击此处</a><#else>如邮件不能正常显示<a href="http://www.scholarmate.com"  target="_blank" style="color:#55b1f5; width:420px; overflow:hidden; ">请点击此处</a></#if>
	</td>
  </tr>
     <tr>
    <td style="color:#999999; font-size:12px; line-height:150%; ">
    </td>
  </tr>
</table>    
</body>
</html>