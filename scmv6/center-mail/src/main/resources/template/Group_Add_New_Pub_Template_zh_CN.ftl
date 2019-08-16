<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>在群组中新增了成果</title>
</head>

<body>
	<table width="620" border="0" align="center" cellpadding="0"
		cellspacing="0"
		style="font-family: Arial, Helvetica, ' Microsoft YaHei'; font-size: 14px; color: #999999; background: #f7f7f4;">
		<tr>
			<td height="50" align="left" valign="middle"
				style="background: #666666;"><a href="${domainUrl!''}"
				target="_blank"><img
					src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png"
					style="border: none; padding-left: 20px;"></a></td>
		</tr>
		<tr>
			<td style="padding: 30px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="font-family: Arial, Helvetica, ' Microsoft YaHei'; font -size: 14px; color: #333333;">
					<tr>
						<td style="font-size: 14px; font-weight: bold; line-height: 150%;">
							<p style="margin: 0; padding: 0;">
									${receiverName}，<span><a href="${senderPersonUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">${senderName}</a></span><#if addpubCount?number ==1>
								 在群组“${groupName}”添加了${addpubCount}条成果。<#elseif (addpubCount?number >1)>
								 在群组“${groupName}”添加了${addpubCount}条成果。 </#if>
							</p>
						</td>
					</tr>
					<tr>
						<td height="15">&nbsp;</td>
					</tr>
					<tr>
						<td>
							<table border="0" cellspacing="0" cellpadding="0"
								style="width: 100%; font-family: Arial, Helvetica,' Microsoft YaHe i '; font-size: 14px; color: #333333; background: #fff; padding: 20px; border: 1px solid #eaeaea; line-height: 150%;">
								<tr align="left">
 
                                    <td align="left" valign="top" style="width: 20%; float: left;">
                                        <a href="${groupPubUrl!'#'}" target="_blank"
										style="border: 1px solid #e2e2e2; padding: 0; margin: 0; float: left; margin-right: 20px;  width:66px; height:78px">
										    <img  src="${groupPubFullTextImg!'http://www.scholarmate.com/resscmwebsns/images_v5/fulltextImg/nofulltext_img.jpg'}" width="66" height="78" style=" float: left;">
									    </a>
                                    </td>
                                    <td align="left" style=" width:80%;">
                                    	<p style="margin: 0; padding: 0; margin-left: 0px; font-weight: bold;">
											<a href="${groupPubUrl!'#'}" target="_blank" style="text-decoration: none; color: #333333;">${groupPubName}</a>
										</p>
										<p style="color: #999999; margin: 0; padding: 0; margin-left: 0px;">${groupPubAuthorNames!''}</p>
										<p style="color: #999999; margin: 0; padding: 0; margin-left: 0px;">${groupPubDesc!''}</p>
										<p style="margin: 15px 0 0 0px; font-weight: bold;">
										   <a href="${groupPubUrl!'#'}"    target="_blank"  style="height: 26px; padding: 0 20px; margin: 0; margin-right: 10px; line-height: 26px; color: #fff; text-decoration: none; background: #55b1f5; border: 1px solid #49a0e0; display: inline-block;">查看成果</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<a href="${groupAllPubUrl!'#'}" target="_blank"   	style="height: 26px; padding: 0 20px; margin: 0; line-height: 26px; color: #666666; text-decoration: none; background: #ffffff; border: 1px solid #c9cdd1; display: inline-block;">全部成果</a>
										</p>
                                    </td>




									<!-- <td rowspan="4" valign="top">
										<a href="${groupPubUrl!'#'}" target="_blank"
										style="border: none; padding: 0; margin: 0; float: left; margin-right: 20px;">
										    <img  src="${groupPubFullTextImg!'http://www.scholarmate.com/resscmwebsns/images_v5/fulltextImg/nofulltext_img.jpg'}" width="66" height="78" style="border: 1px solid #e2e2e2; float: left;">
									    </a>
										<p style="margin: 0; padding: 0; margin-left: 84px; font-weight: bold;">
											<a href="${groupPubUrl!'#'}" target="_blank" style="text-decoration: none; color: #333333;">${groupPubName}</a>
										</p>
										<p style="color: #999999; margin: 0; padding: 0; margin-left: 84px;">${groupPubAuthorNames!''}</p>
										<p style="color: #999999; margin: 0; padding: 0; margin-left: 84px;">${groupPubDesc!''}</p>
										<p style="margin: 15px 0 0 84px; font-weight: bold;">
										   <a href="${groupPubUrl!'#'}"    target="_blank"  style="height: 26px; padding: 0 20px; margin: 0; margin-right: 10px; line-height: 26px; color: #fff; text-decoration: none; background: #55b1f5; border: 1px solid #49a0e0; display: inline-block;">查看成果</a>
											<a href="${groupAllPubUrl!'#'}" target="_blank"   	style="height: 26px; padding: 0 20px; margin: 0; margin-right: 10px; line-height: 26px; color: #666666; text-decoration: none; background: #ffffff; border: 1px solid #c9cdd1; display: inline-block;">全部成果</a>
										</p>
									</td> -->
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td valign="bottom"
							style="height: 30px; font-size: 14px; text-align: center; color: #999999; font-style: italic;">上传论文成果，提高科研影响力</td>
					</tr>
						</table>
			</td>
		</tr>
	</table>
	 <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">   
</body>