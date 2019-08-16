<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>推荐合作者为好友</title>
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
    <td style="line-height:25px;"><span style="font-weight:bold;">${psnName!''}</span>，以下人员可能是您的合作者：</td>
  </tr>
  <tr>
    <td align="left" valign="top" bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#005eac;">
      <#if (coopPsnName0?exists)> 
      <tr>
        <td width="50%" align="left">
         <a href="${frdUrl0}" style="text-decoration:none; color:#005eac; font-size:12px;">
         ${coopPsnName0}</a>
         <span style="text-decoration:none; color:#999; font-size:12px;">
        &nbsp;&nbsp;${coopPsnTitolo0!''}</span>
         </td>
          <#if (coopPsnName1?exists)>
        <td align="left">
          <a href="${frdUrl1}" style="text-decoration:none; color:#005eac; font-size:12px;">
           ${coopPsnName1}</a>
           <span style="text-decoration:none; color:#999; font-size:12px;">
           &nbsp;&nbsp;${coopPsnTitolo1!''}</span>
          </td>
      		</#if>
      </tr>
      </#if>
      <#if (coopPsnName2?exists)> 
      <tr>
        <td align="left">
        <a href="${frdUrl2}" style="text-decoration:none; color:#005eac; font-size:12px;">
        ${coopPsnName2}</a>
        <span style="text-decoration:none; color:#999; font-size:12px;">
        &nbsp;&nbsp;${coopPsnTitolo2!''}</span>
        </td>
       		<#if (coopPsnName3?exists)>
        <td align="left">
         <a href="${frdUrl3}" style="text-decoration:none; color:#005eac; font-size:12px;">
         ${coopPsnName3}</a>
         <span style="text-decoration:none; color:#999; font-size:12px;">
         &nbsp;&nbsp;${coopPsnTitolo3!''}</span>
         </td>
      	</#if>
        </tr>
        </#if>
      <#if (coopPsnName4?exists)> 
      <tr>
        <td align="left">
        <a href="${frdUrl4}" style="text-decoration:none; color:#005eac; font-size:12px;">
        ${coopPsnName4}</a>
         <span style="text-decoration:none; color:#999; font-size:12px;">
        &nbsp;&nbsp;${coopPsnTitolo4!''}</span>
        </td>
       	<#if (coopPsnName5?exists)>
        <td align="left">
        <a href="${frdUrl5}" style="text-decoration:none; color:#005eac; font-size:12px;">
        ${coopPsnName5}</a>
        <span style="text-decoration:none; color:#999; font-size:12px;">
        &nbsp;&nbsp;${coopPsnTitolo5!''}</span>
        </td>
        	</#if>
        </tr>
       </#if>
        <#if (ltSix)?exists>
         <tr>
         <td align="left">
        	  <a href="${viewUrl!'#'}" style="text-decoration:none;color:#005eac;">……</a>
           </td>
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
                        <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${viewUrl!'#'}" style="font-size:14px; color:#3f68a8; text-align:center; line-height:30px; text-align:center; font-weight:bold; text-decoration:none;">加为好友</a></td>
                        </tr>
                    </table></td>
                    <td align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                      <tr>
                        <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${viewUrl!'#'}" style="font-size:14px; color:#3f68a8; text-align:center; line-height:30px; text-align:center; font-weight:bold; text-decoration:none;">查看更多</a></td>
                      </tr>
                    </table></td>
                    </tr>
                      <tr>
                    <td colspan="2" align="left" valign="middle">&nbsp;</td>
                    </tr>
                  </table></td>
                  
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">与合作者保持联系，分享信息和获取更多机会</td>
                    </tr>
                  </table>
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
