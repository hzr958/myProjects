<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
   人员合并通知邮件
</title>
</head>

<body>
<#include "/base_header_en_US.ftl" encoding= "UTF-8">
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF"><table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
        <tr>
          <td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td height="60" valign="top" bgcolor="#426cad"><table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">ScholarMate</td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                          <tr style="font-size:14px;">
                             <#if (emailList?exists)>
                            <td style="line-height:25px;"><span style="font-weight:bold;">${psnName!''}</span>, your accounts <#list emailList?eval as email><#if (email_index = 0)> <a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a> <#elseif (email_index = emailList?eval?size-1)> and <a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a><#else>, <a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a></#if></#list> have been merged to <a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a>. You can sign in using <a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a> now. All the historical data are merged and saved to your new account.</td>
                            </#if>
                          </tr>
                         
                        </table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                            <tr>
                              <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${viewUrl!''}" style="font-size:14px; color:#3f68a8; font-weight:bold; text-align:center; line-height:30px; text-align:center; text-decoration:none;">Sign In</a></td>
                            </tr>
                          </table></td>
                        </tr>
                      </table></td>
                    </tr>
                  </table>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">Use ScholarMate to find more opportunities.</td>
                    </tr>
                  </table></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
</table>
<#include "/base_foot_no_unsubscribe_en_US.ftl" encoding= "UTF-8"> 
</body>
</html>

