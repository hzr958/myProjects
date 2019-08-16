<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>研究领域推荐</title>
</head>

<body>
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF"><table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
        <tr>
          <td bgcolor="#f3f3f3"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                        <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:0;padding-left:20px;"></a></td>
              </tr>
              <tr>
                <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                          <tr style="font-size:14px;">
                            <td style="line-height:25px;"><span style="font-weight:bold;">${receivePsnName}</span>，${email_subject}</td>
                          </tr>
                          <tr>
                            <td align="left" valign="top" bgcolor="#fff">
                              <table width="100%" border="0" cellspacing="0" cellpadding="10" style="font-size:12px; color:#005eac;">
                                  <tr>
                                    <td style="line-height: 36px;">
                                       <#list kwList as kw>
                                            <a href="${operatUrl}" style=" width: 128px; font-size:14px;  line-height:18px; text-align:center; text-decoration:none; border: 1px solid #ccc; padding: 6px; color: #666;">
                                                ${kw}
                                                <a href="${operatUrl}" style=" font-size: 16px; font-weight: bold; text-align:center; text-decoration:none; border: 1px solid #ccc; border-left: none; text-align:center; padding: 4px 14px; background-image: url(select.png); background-repeat: no-repeat;background-position: 5px 5px ; "></a>
                                            </a>
                                            <#if kw_index == 2>
                                                </br>
                                            </#if>
                                       </#list>                      
                                    </td>
                                  </tr>
                              </table>

                            </td>
                          </tr>
                        </table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="100%" align="left" valign="middle"><table width="540" border="0" cellpadding="0" cellspacing="1">
                                <tr>
                                     <td width="250" style="float: left;">
                                <a href="${operatUrl}" style="font-size:14px;  line-height:16px; text-align:center; text-decoration:none; color: #fff; background-color: #288aed; padding: 8px 85px;">确认或修改</a>
                            </td>
                            <td width="280" style="float: right; ">
                                <a href="${operatUrl}" style="font-size:14px; border: 1px solid #ccc; color: #666; background-color: #fff; line-height:16px;text-decoration:none; text-align:center; padding: 8px 97px;">编辑我的主页</a>
                            </td>
                                </tr>
                              </table></td>
                          </tr>
                        </table></td>
                    </tr>
                  </table>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">更新研究领域，获取更多机会推荐。</td>
                    </tr>
                  </table></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
</table>
<#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
