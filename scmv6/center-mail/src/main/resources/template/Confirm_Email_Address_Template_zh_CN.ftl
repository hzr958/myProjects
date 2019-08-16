<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>确认邮件地址</title>
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
			<td align="center" valign="top">
				<table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size: 14px;color: #333333;">
					<tr>
						<td align="left" valign="top">
							<table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size: 14px;">
								<tr style="font-size: 14px;">
									<td colspan="2" style="line-height: 25px;">
										<span style="line-height: 25px;"> ${psnName}， </span>
									</td>
								</tr>
								<tr>
									<td colspan="2" valign="top" style="line-height: 25px;">
										<!-- 正文显示内容 -->
									 	<span style="line-height: 25px;">欢迎使用科研之友，请在科研之友中输入以下验证码：${confirmCode} 确认邮件地址或直接打开以下链接：<a href="${confirmUrl}"  target="_blank" style=" color:#55b1f5; line-height: 25px;">${confirmUrl}</a> 确认。</span>
									</td>
								</tr>
								<tr>
									<td style="height: 6px" colspan="2"></td>
								</tr>
							</table>
						</td>
					</tr>
					<!--替换内容end-->
				</table>
			</td>
		</tr>
	</table>
	<!-- 中文没有退订新页脚 -->
  <#include "/base_foot_no_unsubscribe_zh_CN.ftl" encoding= "UTF-8">
</body>