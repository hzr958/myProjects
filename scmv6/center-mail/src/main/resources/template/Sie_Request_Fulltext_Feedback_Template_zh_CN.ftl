<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>您请求的全文已被上传</title>
</head>

<body>
<!-- 正文内容 (样式内容)-->
<table width="728" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei' ; font-size:14px; color:#999999; background:#f7f7f4;">
<tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${sie_ins_domain!''}"  target="_blank"><img src="${sie_ins_domain!""}/ressie/images/sie_email_logo2.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
<tr>
    <td style="padding:24px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'  ; font-size:14px; color:#333333;">
          <tr><td style="padding-bottom: 20px;"><span style="font-weight: bolder;font-size: 12px;color: black;">
                ${zh_CN_psnname}，您好：
          </span></td></tr>
          <tr>
          <tr><td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <!--替换内容begin-->
                <tr><td align="center" valign="top">
                <table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                  <tr>
                    <td align="left" valign="top">
                    <table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                      <tr style="font-size:14px;">
                        <td colspan="2" style="line-height:25px;">
                            <span >
                            您 ${zh_CN_requestTime} 请求的“${zh_CN_requestPubName}”全文已被上传，请前往科研之友机构版查看<#if zh_CN_requestReason?exists&&zh_CN_requestReason?trim?length gt 1>，您当时的请求理由是： ${zh_CN_requestReason}<#else></#if>。<br/>
                            </span><span >
                            查看地址： <a href="${view_url!''}"  target="_blank">${view_url!''}</a>
                            </span>
                        </td>
                      </tr>
                    </table>
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

