<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>群组上传群组课件</title>
</head>
<body>
	<table width="620" border="0" align="center" cellpadding="0" cellspacing="0"
		style="font-family: Arial, Helvetica, ' Microsoft YaHei'; font-size: 14px; color: #999999; background: #f7f7f4;">
		<tr>
			<td height="50" align="left" valign="middle" style="background: #666666;">
				<a href="${domainUrl!''}" target="_blank">
					<img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border: none; padding-left: 20px;">
				</a>
			</td>
		</tr>
		<tr>
			<td style="padding: 30px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family: Arial, Helvetica, ' Microsoft YaHei'; font -size: 14px; color: #333333;">
					<tr>
						<td style="font-size: 14px; font-weight: bold; line-height: 150%;">
							<p style="margin: 0; padding: 0;">
							<p style="margin: 0; padding: 0;">
								${receiver},
								<span>  <a href="${psnUrl!'#'}"   target="_blank" style=" color:#55b1f5; border:none; padding:0; margin:0; margin-right:0px; text-decoration: none;">${sender} </a> </span>
								<#if materialCount?number ==1>
								在群组“${groupName}”添加了1个群组课件。 <#elseif (materialCount?number >1)>
								在群组“${groupName}”添加了${materialCount}个群组课件。 </#if>
							</p>
							</p>
						</td>
					</tr>
					<tr>
						<td height="15">&nbsp;</td>
					</tr>
					<tr>
						<td>
							<table border="0" cellspacing="0" cellpadding="0"
								style="width: 100%; font-family: Arial, Helvetica, ' Microsoft YaHe i '; font-size: 14px; color: #333333; background: #fff; padding: 20px; border: 1px solid #eaeaea; line-height: 150%;">
								<tr>
									<td rowspan="4" valign="top" style="padding-left: 15px;">
										<p style="margin-bottom: 10px; padding: 0; font-weight: bold; margin-top: 4px;">${materialNames}</p>
										<p style="color: #999999; margin: 0; padding: 0; font-weight: bold;">${materialDesc}</p>
										<p style="margin: 15px 0 0 0px; font-weight: bold;">
											<a href="${shareMaterialUrl!''}" target="_blank"
												style="height: 26px; padding: 0 20px; margin: 0; margin-right: 10px; line-height: 26px; color: #fff; text-decoration: none; background: #55b1f5; border: 1px solid #49a0e0; display: inline-block;">查看课件</a>
											<a href="${materialUrl!''}" target="_blank"
												style="height: 26px; padding: 0 20px; margin: 0; margin-right: 10px; line-height: 26px; color: #666666; text-decoration: none; background: #ffffff; border: 1px solid #c9cdd1; display: inline-block;">全部课件</a>
										</p>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>