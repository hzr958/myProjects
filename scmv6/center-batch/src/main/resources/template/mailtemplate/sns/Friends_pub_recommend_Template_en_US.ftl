<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>好友论文推荐的邮件</title>
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
    <td style="line-height:25px;"><span style="font-weight:bold;">${psnName}</span>,<a href="${frdUrl}" style="font-weight:bold; color:#426cad;text-decoration:none;">${friendName}</a> has <a href="${viewMore}" style="font-weight:bold; color:#426cad;text-decoration:none;">${total}</a> new publication(s):</td>
  </tr>
  <tr>
    <td align="left" valign="top" bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#005eac;">
      <tr>
        <td align="left">
          <a href="${pubDetailUrl}" style="font-weight:bold; color:#426cad;text-decoration:none;"> ${pubTtitle!''}，${pubAuthor!''}
          	<#if (fullTextFlag?number=1)>
          	  <img src="http://bpo.scholarmate.com/resscmwebbpo/images_v5/full_text_US_en.gif" width="33" height="16" align="absmiddle" />
          	</#if>
        	</a>
        </td>
        </tr>
      </table></td>
  </tr>
  </table></td>
              </tr>
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="middle"><table width="190" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                      <tr>
                        <td height="37" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${viewMore}" style="font-size:14px; color:#3f68a8;  text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">View More</a></td>
                        </tr>
                      </table></td>
                    <td width="250" rowspan="2" align="right">&nbsp;</td>
                  </tr>
                  </table></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="6">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">View and congratulate on, like and share your friend’s publications to help him/her improve citations and impact.</td>
                    </tr>
                  </table>
            </td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<#include "/base_foot_en_US.ftl" encoding= "UTF-8"> 
</body>
</html>
