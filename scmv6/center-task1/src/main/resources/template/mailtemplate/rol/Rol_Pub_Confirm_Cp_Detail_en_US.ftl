<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>合作者确认成果</title>
</head>

<body>
<!-- 英文通用页首 -->
<table width="600" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333333;">
  <tr>
   <td height="25" align="center" valign="bottom" style="color:#333333; font-size:12px;">
			If you can not read this email, please click the link below to read the details<br />
			<#if (viewMailUrl?exists)> <a href="${viewMailPath!''}${viewMailUrl!''}" target="_blank" style="font-size:14px;white-space:nowrap;">${viewMailPath!''}${viewMailUrl!''}</a><#else><a href="https://www.scholarmate.com"  target="_blank">https://www.scholarmate.com</a></#if>
	    </td>
  </tr>
  <tr>
    <td height="15"></td>
  </tr>
</table>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="https://www.scholarmate.com"  target="_blank"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" border="0" style="padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">
                <p style="margin:0; padding:0;">${copPsnName!''}，<a href="${frdUrl!''}${proLogin1!''}"  target="_blank"  style="text-decoration:none;"><span style=" color:#55b1f5;">${psnName!''}</span></a> uploaded following publication, are you an co-author?</p>
            </td>
          </tr>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
           <#if (pubInfo0?exists)>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td rowspan="4" valign="top"><a href="${pubUrl0}${proLogin20!''}"   target="_blank" style=" border:none; padding:0; margin:0; float:left; margin-right:20px;"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/fulltextImg/scm_pdf.jpg" width="66" height="78" style="border:1px solid #e2e2e2;"></a>
                     <p style="  margin:0; padding:0; margin-left:84px; font-weight:bold;"><a href="${pubUrl0!''}${proLogin20!''}"  target="_blank" style=" color:#333333; text-decoration:none;">${rolPubitle!''}</a></p>
                    <p style="color:#999999; margin:0; padding:0; margin-left:84px;">${authorNames!''}</p>
                    <p style="color:#999999; margin:0; padding:0; margin-left:84px;">${rolPubBriefDesc!''} </p>
                    <p style=" margin:10px 0 0 84px; font-weight:bold;">
                    <a href="${pubAllUrl!''}${proLogin9!''}"  target="_blank" style="  height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">Confirm Authorship</a>
                    <a href="${pubAllUrl!''}${proLogin9!''}"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display:inline-block;">Not Me</a>
                    <a href="${frdPubAllUrl!''}${proLogin16!''}"   target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display:inline-block;">View</a>
                    </p>
                    </td>
                  </tr>
                </table>
            </td>
            </#if>
          </tr>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
            <td valign="bottom" style=" height:50px; font-size:14px; font-weight:bold;"><a href="${frdPubAllUrl!''}${proLogin16!''}"   target="_blank" style=" width:100%; height:26px; text-align:center; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">View More</a></td>
          </tr>
          <tr>
            <td valign="bottom" style="  height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">Confirm and share  full texts to increase citation </td>
          </tr>
        </table>
	</td>
  </tr>
</table>
<#include "/scm_base_foot_en_US.ftl" encoding= "UTF-8"> 
</body>
</html>
