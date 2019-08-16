<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报告成果认领</title>
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
            <td style="font-size:14px; font-weight:bold; line-height:150%;">
               <p style="margin:0; padding:0;"><span ><a href="${psnUrl!'#'}"   target="_blank" style="  border:none; padding:0; margin:0; float:left; margin-right:0px; text-decoration: none;">${psnName!''}</a></span>，我们为你的项目“${grpName!''}”找到了以下成果。</p>
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
                    <td rowspan="4" valign="top">
                    <a href="${pubDetail.pdwhPubIndexUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                    <img src="${pubDetail.fulltextImg!'http://www.scholarmate.com/resmod/images_v5/images2016/file_img.jpg'}" width="66" height="78" style="border:1px solid #e2e2e2;">
                    </a>
                    <p style=" margin:0; padding:0; margin-left:84px;font-weight:bold;"><a  href="${pubDetail.pdwhPubIndexUrl!'#'}" target="_blank" style="text-decoration:none;color:#333333;">${pubDetail.title!''}</a></p>
                    <p style="color:#999999; margin:0; padding:0; margin-left:84px;">${pubDetail.authorNames!''}</p>
                    <p style="color:#999999; margin:0; padding:0; margin-left:84px;">${pubDetail.briefDesc!''}</p>
                    <p style=" margin:15px 0 0 84px;">
                    <a href="${grpUrl!''}"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">添加到我的项目</a>
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
           <#if (pubSum?exists&&pubSum?number>0)>
         <tr>
            <td valign="bottom" style=" height:50px; font-size:14px; font-weight:bold;"><a href="${grpUrl!'#'}"   target="_blank" style=" width:100%; height:26px; text-align:center; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">发现更多可能是我的成果</a></td>
          </tr>
          </#if>
          <tr>
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">认领科研成果，让科研更成功</td>
          </tr>
        </table>
	</td>
  </tr>
  </table>
  <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>