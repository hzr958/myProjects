<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>通知管理员已成功加入群组</title>
</head>
<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td rowspan="1" valign="top">
                    <p style=" margin:0; padding:0; margin-left:40px;font-weight:bold;">${adminPsnName}，<a href="${viewPsnUrl}" target="_blank"  style="font-weight:bold; color:#426cad; text-decoration:none;">${viewName}</a>已加入<a href="${viewGroupUrl}" target="_blank" style="font-weight:bold; color:#426cad; text-decoration:none;">${groupName}</a>群组。</p>
                    <p style=" margin:30px 0 0 40px;font-weight:bold;">
                    <a href="${viewGroupUrl!''}"  target="_blank"  style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #c9cdd1; display:inline-block;text-align:center">进入群组</a>
                    </p>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
          <tr>
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">与群组成员合作、交流、共享，获取更多资源，提高工作效率。</td>
          </tr>
        </table>
	</td>
  </tr>
</table>
 <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">              	
</body>
</html>
