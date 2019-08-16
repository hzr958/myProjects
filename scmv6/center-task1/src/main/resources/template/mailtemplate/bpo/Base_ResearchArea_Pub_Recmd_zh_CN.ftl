<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>根据研究领域的论文推荐</title>
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
                            <td style="line-height:25px;"><span style="font-weight:bold;">${receivePsnName}</span>，在您的研究领域内，您可能对以下<a href="${viewUrl}" style="text-decoration:none;font-weight:bold; color:#426cad;">${counts}</a>篇论文感兴趣：</td>
                          </tr>
                          <tr>
                            <td align="left" valign="top" bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#005eac;">
                                <#list pubList as pub>
                                <tr>
                                  <td width="60%" align="left"><a href="${pub.pubUrl}" style="text-decoration:none; color:#005eac; font-size:12px;">${pub.pubName!''}</a></td>
                                  <td align="left" style="color:#000;">相关性：<#assign sufCo=5-pub.recommendation/>
                                     <#list 1..pub.recommendation as  co >★</#list><#if (sufCo?number != 0)><#list 1..sufCo as  co2 >☆</#list></#if>
                                  <!--<a href="${viewUrl}" style="text-decoration:none; color:#005eac; font-size:12px;">${pub.recommendation!''}</a>-->
                                  </td>
                                </tr>
                                </#list>
                                <#if (counts?number > 3)>
                                <tr>
                                  <td align="left"><a href="${viewUrl}" style="text-decoration:none;color:#005eac;">……</a></td>
                                  <td align="left">&nbsp;</td>
                                </tr>
                                </#if>
                              </table></td>
                          </tr>
                        </table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="26%" align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                                <tr>
                                  <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px #ebf1fb;"><a href="${viewUrl}" style="font-size:14px; font-weight:bold; color:#3f68a8; text-align:center; line-height:30px; text-align:center; text-decoration:none;">阅读收藏</a></td>
                                </tr>
                              </table></td>
                            <td align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                                <tr>
                                  <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px #ebf1fb;"><a href="${viewUrl}" style="font-size:14px; font-weight:bold; color:#3f68a8; text-align:center; line-height:30px; text-align:center; text-decoration:none;">查看更多</a></td>
                                </tr>
                              </table></td>
                          </tr>
                        </table></td>
                    </tr>
                  </table>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">阅读最新论文，掌握科研动态，发掘合作机会。</td>
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
