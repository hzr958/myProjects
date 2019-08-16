<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新加好友邮件</title>
</head>

<body>
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF"><table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
      <tr>
        <td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="60" valign="top" bgcolor="#426cad"><table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">科研之友</td>
                    </tr>
                </table></td>
          </tr>
          <tr>
            <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                  <tr>
                    <td style="font-size:14px;"><span style="font-weight:bold;">${psnName}</span>，<a href="${friendurl!'#'}" style="font-weight:bold; color:#426cad; text-decoration:none;">${friendName}</a>新加了好友：</td>
                  </tr>
                  <tr>
                    <td bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#333;">
                      <tr>
                        <td width="35%" align="left" valign="top"><a href="${newFriendUrl!'#'}" style="font-weight:bold; color:#426cad; font-size:12px; text-decoration:none;">${newFrdname}</a><#if (newInsName?exists)>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${workUrl!'#'}" style="font-weight:bold; color:#426cad; font-size:12px; text-decoration:none;">${newInsName}</a></#if><#if (newTitlo?exists)>&nbsp;&nbsp;&nbsp;&nbsp;<a  href="${workUrl!'#'}" style="font-weight:bold; color:#426cad; font-size:12px; text-decoration:none;">${newTitlo}</a></#if></td>
                        </tr>
                      </table></td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                      <tr>
                      	<td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${addFrdUrl!'#'}" style="font-size:14px; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; font-weight:bold; text-decoration:none;">加为好友</a></td>
                        </tr>
                      </table></td>
                    <td width="250" rowspan="2" align="right">&nbsp;</td>
                  </tr>
                  </table></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">扩大社交网络，获取更多信息，提高影响力。</td>
                    </tr>
                  </table>
            </td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
