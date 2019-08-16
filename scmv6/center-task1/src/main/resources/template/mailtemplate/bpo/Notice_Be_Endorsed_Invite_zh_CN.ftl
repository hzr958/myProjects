<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>认同研究领域通知</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:0;padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td rowspan="4" valign="top"><a href="${frdUrl!'#'}"   target="_blank" style=" border:none; padding:0; margin:0; float:left; margin-right:20px;"><img src="${avatars}" width="66" height="66" style="border:1px solid #e2e2e2;" ></a>
                   
                     <p style="color:#999999; margin:0; padding:0; margin-left:84px;"><a href="${frdUrl!'#'}"   target="_blank"  style="font-weight:bold; text-decoration:none; color:#426cad;">${frdPsnName}</a>
                       <span style="color:#000000"> 认同了您的研究领域：
                      <#list frdRaList as frdRa>
                     <#list frdRa.raList as ra> 
                     <#if ra ? size gt 3>
                       ${ra[0]}; ${ra[1]}; ${ra[2]} ...
                       <#else>
                       ${ra} ;
                     </#list>
                     </#list>
                     </span>
                     </p>
                    <p style=" margin: 10px 0px 0px 84px; font-weight: bold;">
                    <a href="${psnUrl}%26src%3ddiscipline_box"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">查看研究领域</a>
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
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">认同研究领域，加强合作交流。</td>
          </tr>
        </table>
	</td>
  </tr>
  </table>
  <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>