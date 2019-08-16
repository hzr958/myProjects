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
 <!-- 英文新页脚 --> 
  <table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#999999; background:#f7f7f4; padding:15px 0; margin-top:15px;"> 
   <tbody>
    <tr> 
     <td align="center" valign="bottom" style="color:#999999; font-size:12px; line-height:150%;"> Like us @ <a href="http://www.facebook.com/scholarmate1" target="_blank" style="color:#55b1f5; text-decoration:none;">facebook.com/scholarmate1</a>; follow us @ <a href="http://twitter.com/ScholarMate1" target="_blank" style="color:#55b1f5; text-decoration:none;">twitter.com/ScholarMate1</a> and <a href="http://plus.google.com/+Scholarmate" target="_blank" style="color:#55b1f5; text-decoration:none;">plus.google.com/+Scholarmate</a>.<br /> This is a system generated email, please do not reply. For enquiry, please write to <a href="mailto:support@scholarmate.com" target="_blank" style="color:#55b1f5; text-decoration:none;">support@scholarmate.com</a>.<br /> If you cannot view this email properly, please<a href="${viewMailPath!''}${viewMailUrl}" target="_blank" style="color:#55b1f5; text-decoration:none;"> click here.</a> </td> 
    </tr> 
   </tbody>
  </table> 
</body>
</html>