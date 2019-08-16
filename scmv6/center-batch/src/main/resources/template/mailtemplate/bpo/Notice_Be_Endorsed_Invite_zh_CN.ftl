<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通知被认同并邀请认同对方</title>
</head>

<body>
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
 <#assign count = psnCount?number/>
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
                            <td style="line-height:25px;"><span style="font-weight:bold;">${psnName}</span>，<a href="${frdUrl}" style="font-weight:bold; text-decoration:none; color:#426cad;">${frdPsnName}</a><#if (count>1)>等<a href="${psnUrl}" style="font-weight:bold;text-decoration:none; color:#426cad;">${count}</a>人</#if>认同了您的研究领域：</td>
                          </tr>
                          <tr>
                            <td align="left" valign="top" bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="10" style="font-size:12px; ">
                               <#list frdRaList as frdRa >
                                <tr>
                                  <td width="20%" align="left"><a href="${frdRa.frdUrl!'#'}" style="text-decoration:none; color:#005eac; font-size:12px;">${frdRa.frdName}</a></td>
                                  <td align="left"><#list frdRa.raList as ra><a href="${psnUrl!'#'}%26src%3ddiscipline_box" style="text-decoration:none; color:#005eac; font-size:12px;">${ra}</a>;</#list></td>
											 <td width="25%" align="left">
										      <table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#285510">
        										<tr>
            									<td height="22" align="center" bgcolor="#69a551"  style=" border-top:1px solid #8fbb7b;">
            									<a href="#" title="立即体验" style="font-size:12px; color:#fff;  text-align:center; line-height:20px; text-align:center; text-decoration:none;">认同好友</a></td>
        										</tr>
    										  </table>
										      </td>
										     </tr>
                                </#list>
                                <#if (psnCount>3)>
                                <tr>
                                  <td align="left"><a href="${psnUrl}%26src%3ddiscipline_box" style="text-decoration:none;color:#005eac;">……</a></td>
                                </tr>
                                </#if>
                              </table></td>
                          </tr>
                        </table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                            <tr>
                              <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${psnUrl}%26src%3ddiscipline_box" style="font-size:14px; font-weight:bold; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; text-decoration:none;">查看更多</a></td>
                            </tr>
                          </table></td>
                        </tr>
                      </table></td>
                    </tr>
                  </table>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">认同研究领域，加强合作交流。</td>
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
