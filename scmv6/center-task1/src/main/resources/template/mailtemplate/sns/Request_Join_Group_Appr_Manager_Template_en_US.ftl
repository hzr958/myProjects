<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>好友推荐-成功加入群组</title>
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
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;"> ScholarMate </td>
                    </tr>
                </table></td>
          </tr>
          <tr>
            <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
  <tr style="font-size:14px;">
    <td style="line-height:25px;">
    	<table cellspacing="0" width="100%" cellpadding="0" border="0">	
    		<tr>	
    			<td width="75">
					<p><img height="60px" width="60px" src="<#if (avatars?exists)>${avatars}</#if>"/></p>
				</td>
				<td align="left" valign="top"  style="font-size:12px;">
					<p><a href="<#if (viewPsnUrl?exists)>${viewPsnUrl}</#if>"><#if (viewName?exists)>${viewName}</#if></a></p>
					<p><span ><#if (viewTitle?exists)>${viewTitle}</#if></span></p>
				</td>
				<td style="font-size:18px;">
					&nbsp;joined the <a href="${viewGroupUrl}"  target="_blank" style="font-weight:bold; color:#426cad; text-decoration:none;">${groupName}</a>&nbsp;group.
				</td>
    		</tr>
    	</table>
    
    </td>
  </tr>
  <#if (groupDesc?exists) >
  <tr>
    <td align="left" valign="top" bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#333; line-height:20px;">
      <tr>
        <td align="left" valign="top"><a href="${viewGroupUrl}"  target="_blank" style="font-weight:bold; color:#426cad; font-size:12px; text-decoration:none;">${groupName}</a><br />群组描述：${groupDesc}</td>
        </tr>
      </table></td>
  </tr>
   </#if>
  </table></td>
              </tr>
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                      <tr>
                        <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;"><a href="${viewGroupUrl}" style="font-size:14px; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; font-weight:bold; text-decoration:none;"> View Details </a></td>
                        </tr>
                      </table></td>
                    <td width="250" rowspan="2" align="right">&nbsp;</td>
                  </tr>
                  </table></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">Collaborate, communicate and share with group members to access more resources and improve efficiency.
</td>
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
