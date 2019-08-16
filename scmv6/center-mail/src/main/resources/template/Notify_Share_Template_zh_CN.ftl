<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科研之友-${psnName} 分享了 ${total}条你可能感兴趣的资源</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:0;padding-left:20px;"></a></td>
  </tr>
  <tr>
	<td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
	     <tr>
	       <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;color:#333333;">
	     <tr style="font-size:14px;">
	        <td colspan="2" style="line-height:25px;font-size:14px;">
	          <span style="font-weight:bold;">
	                    		<#if (recvName)??>${recvName}，<#else>您好，</#if>
	                    	</span>
	                    </td>
	                  </tr>
	                  <tr>
	                    <td colspan="2" style="line-height:150%;font-size:14px;">
						<!-- 正文显示内容mailContext。 -->
		                <#if (mailContext?exists)><a href="${senderPersonUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">${psnName}</a>分享了<#else>科研之友 – <a href="${senderPersonUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">${psnName}</a>分享了</#if><#if total?number == 1><a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${total}</a><#switch type><#case '2'>条您可能感兴趣的文献<#break><#case '3'>个您可能感兴趣的文件<#break><#case '4'>个您可能感兴趣的项目<#break><#case '1'>条您可能感兴趣的成果<#break><#case '6'>条您可能感兴趣的资助机构<#break><#default>个您可能感兴趣的资源</#switch>：
		                  <#switch type>
			                   <#case '2'> <a href="${pubDetail!'#'}" style="color:#55b1f5;text-decoration:none;">${minZhShareTitle!''}</a><#break>
			                   <#case '1'> <a href="${pubDetail!'#'}" style="color:#55b1f5;text-decoration:none;">${minZhShareTitle!''}</a><#break>
			                   <#case '4'> <a href="${prjDetail!'#'}" style="color:#55b1f5;text-decoration:none;">${minZhShareTitle!''}</a><#break>
			                   <#case '6'><a href="${fundUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${minZhShareTitle!''}</a><#break>
			                   <#default> <a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${minZhShareTitle!''}</a>
		                   </#switch>
		                <#elseif (total?number > 1)><a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${minZhShareTitle!''}</a> 等<a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${total}</a><#switch type><#case '2'>条您可能感兴趣的文献<#break><#case '3'>个您可能感兴趣的文件<#break><#case '4'>个您可能感兴趣的项目<#break><#case '1'>条您可能感兴趣的文献<#break><#case '6'>条您可能感兴趣的资助机构<#break><#default>个您可能感兴趣的资源</#switch>。</#if>
						</td>
	                  </tr>
	                  <#if (recommendReason?exists&&recommendReason!="")>
	                  <tr>
	                     <td colspan="2" valign="top" bgcolor="#f9f9f9" style="padding-top:10px; padding-bottom:10px;word-break: break-all;">
	                    <table width="100%" border="0" cellspacing="0" cellpadding="5" style="line-height:16px;">
							<tr>
						  	  	<td align="left" style="font-size:12px;">
									${recommendReason}
						  	  	</td>
						  	</tr>
	                    </table>
	                    </td>
	                  </tr>
	                  </#if>
	                  <tr>
	                  	<td style="line-height:16px; font-size:14px;" colspan="2"></td>
	                  </tr>
	                  <tr>
	                  	<td style="height:6px" colspan="2">
						</td>
	                </tr>
	                  
	                  <tr style="font-size:14px;">
	                    <td height="60" colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	                      <tr>
	                        <td width="145" align="left" valign="middle">
	                        <table width="135" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
	                          <tr>
								<td height="30" align="center" bgcolor="#55b1f5"  style=" border:1px solid #55b1f5;">
									<a href="${viewUrl}" style="font-size:14px; color:#fff;text-align:center; line-height:30px; text-align:center; text-decoration:none;">查看更多</a>
								</td>
	                          </tr>
	                        </table>
	                        </td>
	                      </tr>
	                      <tr>
						  </tr>
	                    </table></td>
	                  </tr>
	                </table>
	              </td>
              </tr><!--替换内容end-->
            </table>
            </td>
        </tr>
</table>
<#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>