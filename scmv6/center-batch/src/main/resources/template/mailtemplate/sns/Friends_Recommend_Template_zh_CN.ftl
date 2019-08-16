<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>好友推荐的邮件</title>
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
                    <td style="font-size:14px;"><span style="font-weight:bold;">${psnName}</span>，您可能认识以下人员：</td>
                  </tr>
                  <tr>
                    <td bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" bgcolor="#f9f9f9" cellpadding="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
                      <tr>
                        <td width="33%" align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                          <tr>
                          <#if (frdName1?exists)>
                            <td height="18" align="left"><a href="${viewFrdUrl1!'#'}" style="font-weight:bold; font-size:12px; color:#005eac; text-decoration:none;">${frdName1}</a></td>
                            </tr>
                          <tr>
                            <td height="14" align="left" style="color:#999;"><#if (frdIns1?exists)><a href="${viewFrdUrl1!'#'}" style="text-decoration:none; color:#999; font-size:12px;">${frdIns1}</a>&nbsp;&nbsp;</#if><#if (frdTitolo1?exists)><a href="${viewFrdUrl1}" style="text-decoration:none; color:#999; font-size:12px;">${frdTitolo1}</a></#if></td>
                            </tr>
                          <tr>
                            <td height="18" align="left"><img src="http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/add_icon.gif" width="10" height="10" align="absmiddle" />&nbsp;<a href="${addFrdUrl1}" style="text-decoration:none; font-size:12px; color:#333;">加为好友</a></td>
                            </tr>
                          </table></td>
                          	</#if>
                        <#if (frdName2?exists)>
                        <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                          <tr>
                            <td height="18" align="left"><a href="${viewFrdUrl2!'#'}" style="font-weight:bold; font-size:12px; color:#005eac; text-decoration:none;">${frdName2}</a></td>
                            </tr>
                          <tr>
                            <td height="14" align="left" style="color:#999;"><#if (frdIns1?exists)><a href="${viewFrdUrl2!'#'}" style="text-decoration:none; color:#999; font-size:12px;">${frdIns2}</a>&nbsp;&nbsp;</#if><#if (frdTitolo2?exists)><a href="${viewFrdUrl2}"  style="text-decoration:none; color:#999; font-size:12px;">${frdTitolo2}</a></#if></td>
                            </tr>
                          <tr>
                            <td height="18" align="left"><img src="http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/add_icon.gif" width="10" height="10" align="absmiddle" />&nbsp;<a href="${addFrdUrl2}" style="text-decoration:none; font-size:12px; color:#333;">加为好友</a></td>
                            </tr>
                          </table></td>
                          </#if>
                           <#if (frdName3?exists)>
                        <td width="33%" align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                          <tr>
                            <td height="18" align="left"><a href="${viewFrdUrl3!'#'}" style="font-weight:bold; font-size:12px; color:#005eac; text-decoration:none;">${frdName3}</a></td>
                            </tr>
                          <tr>
                            <td height="14" align="left" style="color:#999;"><#if (frdIns1?exists)><a href="${viewFrdUrl3!'#'}" style="text-decoration:none; color:#999; font-size:12px;">${frdIns3}</a>&nbsp;&nbsp;</#if><#if (frdTitolo3?exists)><a href="${viewFrdUrl3}"  style="text-decoration:none; color:#999; font-size:12px;">${frdTitolo3}</a></#if></td>
                            </tr>
                          <tr>
                            <td height="18" align="left"><img src="http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/add_icon.gif" width="10" height="10" align="absmiddle" />&nbsp;<a href="${addFrdUrl3}" style="text-decoration:none; font-size:12px; color:#333;">加为好友</a></td>
                            </tr>
                          </table></td>
                          </#if>
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
                        <td height="37" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${viewMore}" style="font-size:14px; color:#3f68a8;  text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">查看更多</a></td>
                        </tr>
                      </table></td>
                    <td width="250" rowspan="2" align="right">&nbsp;</td>
                  </tr>
                  </table></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">及时了解好友的科研动态，与他们互动，获取更多的科研机会。</td>
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
