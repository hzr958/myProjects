<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Fund sharing</title>
</head>
<body>
	<table width="620" border="0" align="center" cellpadding="0" cellspacing="0"
		style="font-family: Arial, Helvetica, 'Microsoft YaHei'; font-size: 14px; color: #999999; background: #f7f7f4;">
		<tr>
			<td height="50" align="left" valign="middle" style="background: #666666;">
				<a href="${domainUrl!''}" target="_blank">
					<img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border: 0; padding-left: 20px;">
				</a>
			</td>
		</tr>
		<tr>
			<td style="padding: 30px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family: Arial, Helvetica, 'Microsoft YaHei'; font-size: 14px; color: #333333;">
					<tr>
						<td style="font-size: 14px; font-weight: bold; line-height: 150%;">
							<p style="margin: 0; padding: 0;">
								<span><#if (recvName)??>${recvName}，<#else>Hello,</#if></span>
								<a href="${senderPersonUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">${psnName} </a> shared a fund with you.
							</p>
						</td>
					</tr>
					<tr>
						<td height="15">&nbsp;</td>
					</tr>
					<tr>
						<td>
							<table border="0" cellspacing="0" cellpadding="0"
								style="width: 100%; font-family: Arial, Helvetica, 'Microsoft YaHei'; font-size: 14px; color: #333333; background: #fff; padding: 20px; border: 1px solid #eaeaea; line-height: 150%;">
                                <tr align="left" valign="top">
				                    <td valign="top" align="left"  style="width: 15%; display:block; float: left;">
				                        <a href="${pubDetailUrl!'#'}"  target="_blank" style="padding:0; margin:0; margin-right:10px; width: 72px; height: 72px; border-radius:36px;">
				                            <img width="72" height="72" style="border-radius:36px; width: 72px; height: 72px;"  src="${fundLogoUrl!''}" onerror="src='https://test.scholarmate.com/resmod/images_v5/fund_logo/default_logo.jpg'"/>
				                        </a>
				                      </td>
				                      <td style="width: 85%;">
				                        <p style=" margin:0; padding:0; font-weight:bold; display: block; min-height:26px;">
				                        	<a href="${fundUrl!'#'}" target="_blank" style="color: #333333; text-decoration: none;">${fundtitle!''} </a>
				                        </p>
				                        <p style="color:#999999; margin:0; padding:0; display: block; min-height:26px;">${fundAgencyName!''}</p>
				                        <p style="color:#999999; margin:0; padding:0; display: block; min-height:26px;">deadline:${fundStartDate!''}~${fundEndDate!''}</p>
				                        <#if (recommendReason?exists&&recommendReason!="")>
                                            <p style="color: #999999; margin: 0; padding: 0; font-size: 12px;">${recommendReason}</p>
				                        </#if>
                                        <p  style="color:#999999; margin:0; padding:0; display: block; min-height:26px;"></p>
                                        <p style="color:#999999; margin:0; padding:0; display: block; min-height:26px; text-align: right;">
                                        	<a href="${viewUrl}" target="_blank"
											style="width: 30%; width: 140px; height: 26px; text-align: center; margin: 0; margin-right: 10px; line-height: 26px; color: #fff; text-decoration: none; background: #55b1f5; border: 1px solid #49a0e0; display: inline-block;">View all shares</a>

                                        </p>
				                    </td>
				                </tr>
							</table>
						</td>
					</tr>
					<tr>
						<td height="15">&nbsp;</td>
					</tr>
					<tr>
						<td valign="bottom" style="height: 30px; font-size: 14px; text-align: center; color: #999999; font-style: italic;">Share fund and increase scientific research cooperation</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<#include "/scm_base_foot_en_US.ftl" encoding= "UTF-8">
</body>
</html>