<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页邮件分享</title>
</head>

<body>
<!-- 正文内容 (样式内容)-->
<table width="728" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei' ; font-size:14px; color:#999999; background:#f7f7f4;">
<tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${sie_ins_domain!''}"  target="_blank"><img src="${sie_ins_domain!''}/ressie/images/sie_email_logo2.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
  
<tr>
    <td style="padding:24px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'  ; font-size:14px; color:#333333;">
          <tr>
          <td bgcolor="#FFFFFF">
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <!--替换内容begin-->
              <tr>
                <td valign="top" style="height:100px;padding: 20px">
                ${emailContent}
                  </td>
              </tr><!--替换内容end-->
            </table>
            </td>
        </tr>
      </table>
      </td>
  </tr>
</table>
</td>
  </tr>
</table>
<#include "/sie_contact_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>

</html>