<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Collaborators confirm results</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="http://www.scholarmate.com"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" border="0" style="padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">
                <p style="margin:0; padding:0;">${copPsnName!''}，<a href="${frdUrl!''}"  target="_blank"  style="text-decoration:none;"><span style=" color:#55b1f5;">${psnName!''}</span></a> uploaded following publication, are you an co-author?</p>
            </td>
          </tr>
          <#if (pubDetails?exists)>
         <#list pubDetails as pubDetail>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
            <td>
			<table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td rowspan="4" valign="top"  width="13%"><span style="color: #333; font-weight: bold; padding-top: 40px; ">${pubDetail.pubTypeName!''}</span></td>
                    <td  width="87%">
                    <P style=" margin:0; padding:0; margin-left:24px; font-weight:bold;">
                      <a href="${pubDetail.pubIndexUrl!'#'}"  target="_blank"  style=" color:#333333; text-decoration:none;">${pubDetail.title!''}</a>
                    </P>
                    <p style="color:#999999; margin:0; padding:0; margin-left:24px;">${pubDetail.authorNames!''}</p>
                    <p style="color:#999999; margin:0; padding:0; margin-left:24px;">${pubDetail.briefDesc!''}</p>
                    <p style=" margin: 10px 0px 0px 26px; font-weight: bold;">
                    <a href="${pubAllUrl!''}"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;Confirm Authorship&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    <a href="${pubAllUrl!''}"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-left:27px; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display:inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;Not Me&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </p>
                    </td>
                  </tr>
                </table>
            </td>
             </tr>
              </#list>
            </#if>
         
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
            <td valign="bottom" align="center" style="width: 100%;  height:50px; font-size:14px; font-weight:bold;"><a href="${frdPubAllUrl!''}"   target="_blank" style=" width:100%; height:26px; text-align:center; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;View More&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
          </tr>
          <tr>
            <td valign="bottom" style="  height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">Confirm and share publications to increase citation </td>
          </tr>
        </table>
	</td>
  </tr>
</table>
<#include "/scm_base_foot_en_US.ftl" encoding= "UTF-8"> 
</body>
</html>
