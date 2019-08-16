<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>根据好友的论文推荐</title>
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
                          <tr style="font-size:14px;">
                            <td style="line-height:25px;"><span style="font-weight:bold;">${psnName!''}</span>，您可能对<a href="${frdUrl!''}" style="font-weight:bold; color:#426cad; text-decoration:none; ">${frdName!''}</a>的<a href="${frdUrl!''}%26src%3dpublication_box" style="font-weight:bold; color:#426cad; text-decoration:none; ">${pubCount}</a>篇论文感兴趣：</td>
                          </tr>
                          <tr>
                            <td align="left" valign="top" bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#005eac;">
                                <#if (pub0?exists)>
                                <tr>
                                  <td width="100%" align="left"><a href="${pubDetailUrl0!''}" style="text-decoration:none; color:#005eac; font-size:12px;">${pub0!''}</a></td>
                                </tr>
                                </#if>
                                <#if (pub0?exists)>
                                <tr>
                                  <td align="left"><a href="${pubDetailUrl1!''}" style="text-decoration:none; color:#005eac; font-size:12px;">${pub1!''}</a></td>
                                </tr>
                                </#if>
                                <#if (pub0?exists)>
                                <tr>
                                  <td align="left"><a href="${pubDetailUrl2!''}" style="text-decoration:none; color:#005eac; font-size:12px;">${pub2!''}</a></td>
                                </tr>
                                 </#if>
                                <tr>
                                <#assign count = pubCount?number/>
                                	 <#if (count>3)>
                                  <td align="left"><a href="${frdUrl!''}%26src%3dpublication_box" style="text-decoration:none;color:#005eac;">……</a></td>
                                  </#if>
                                  <td align="left">&nbsp;</td>
                                </tr>
                              </table></td>
                          </tr>
                        </table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="26%" align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                                <tr>
                                  <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${frdUrl!''}%26src%3dpublication_box" style="font-size:14px; font-weight:bold; color:#3f68a8; text-align:center; line-height:30px; text-align:center; text-decoration:none;">阅读收藏</a></td>
                                </tr>
                              </table></td>
                            <td align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                                <tr>
                                  <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${frdUrl!''}%26src%3dpublication_box" style="font-size:14px; font-weight:bold; color:#3f68a8; text-align:center; line-height:30px; text-align:center; text-decoration:none;">查看更多</a></td>
                                </tr>
                              </table></td>
                          </tr>
                        </table></td>
                    </tr>
                  </table>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">阅读好友论文，互赞互引，提高论文引用。</td>
                    </tr>
                  </table></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
</table>
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
