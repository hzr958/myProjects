<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Scholarmate - ${psnName} has shared ${total} resource(s) to you</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:0;padding-left:20px;"></a></td>
  </tr>
  <tr>
	<td align="center" valign="top">
		<table width="100%" border="0" cellspacing="20"cellpadding="0" style="font-size: 14px;">
			<tr>
				<td align="left" valign="top">
                    <table width="100%" border="0" cellpadding="6"cellspacing="0" style="font-size: 14px;color:#333333;">
						<tr style="font-size: 14px;">
							<td colspan="2" style="line-height:25px;font-size:14px;">
							    <#if (recvName)??>Dear <span style="font-weight: bold;">${recvName},</span>
							    <#else>Hi ,</#if><br /> 
							</td>
						</tr>
				        <tr>
							<td colspan="2" style="line-height:150%;font-size:14px;">
							<!-- 正文显示内容 -->
							<!--mailContext-->
								<#if (mailContext?exists)><a href="${senderPersonUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">${psnName}</a> shared <#else>ScholarMate - <a href="${senderPersonUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">${psnName}</a> shared </#if><#if total?number == 1><a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${total}</a><#switch type><#case '2'> reference(s) that you may like <#break><#case '3'> file(s) that you may like <#break><#case '4'> project(s) that you may like <#break><#case '1'> publication(s) that you may like <#break> <#case '6'> funding agency(s) that you may like <#break><#default> resource(s) that you may like </#switch>:
									<#switch type>
			               				<#case '2'> <a href="${pubDetail!'#'}" style="color:#55b1f5;text-decoration:none;">${minEnShareTitle!''}</a><#break>
			               				<#case '1'> <a href="${pubDetail!'#'}" style="color:#55b1f5;text-decoration:none;">${minEnShareTitle!''}</a><#break>
			               				<#case '4'> <a href="${prjDetail!'#'}" style="color:#55b1f5;text-decoration:none;">${minEnShareTitle!''}</a><#break>
			               				 <#case '6'><a href="${fundUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${minEnShareTitle!''}</a><#break>
			                  			<#default> <a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${minEnShareTitle!''}</a>
		                 			</#switch>
								<#elseif (total?number > 1)><a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${minEnShareTitle!''}</a> and <a href="${viewUrl!'#'}" style="color:#55b1f5;text-decoration:none;">${total?eval - 1}</a> more<#switch type><#case '2'> reference(s) that you may like <#break><#case '3'> file(s) that you may like <#break><#case '4'> project(s) that you may like <#break><#case '1'> reference(s) that you may like <#break> <#case '6'> funding agency(s) that you may like <#break> <#default> resource(s) that you may like </#switch>.</#if>
							</td>
						</tr>
						<#if (recommendReason?exists&&recommendReason!="")>
                        <tr>
							<td colspan="2" valign="top" bgcolor="#f9f9f9" style="padding-top:10px; padding-bottom:10px;word-break: break-all;">
							    <table width="100%" border="0" cellspacing="0" cellpadding="5" style="line-height:16px;">
									<tr>
										<td align="left" style="font-size:12px;">${recommendReason}</td>
									</tr>
								</table>
							</td>
						</tr>
						</#if>
						<tr><td style="line-height:16px; font-size:14px;" colspan="2"></td></tr>
						<tr><td style="height:6px" colspan="2"></td></tr>
						<tr>
							<td height="60" colspan="2">
								<table width="100%" border="0" cellspacing="0"cellpadding="0">
									<tr>
										<td align="left" valign="middle">
											<table width="190" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
												<tr>
													<td height="30" align="center" bgcolor="#55b1f5"  style=" border:1px solid #55b1f5;">
													    <a href="${viewUrl}" style="font-size:14px; color:#fff;text-align:center; line-height:30px; text-align:center; text-decoration:none;">View More</a>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!--替换内容end-->							
		</table>
	</td>
  </tr>
</table>
<#include "/scm_base_foot_en_US.ftl" encoding= "UTF-8">
</body>
</html>