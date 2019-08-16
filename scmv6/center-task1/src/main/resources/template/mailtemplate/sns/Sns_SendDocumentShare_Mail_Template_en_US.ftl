<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Scholarmate - ${keycode_sharePsnName} shared documents</title>
</head>

<body>
	<!-- 页眉内容 (样式内容)-->
<#include "/base_header_en_US.ftl" encoding= "UTF-8">
	<!-- 正文内容 (样式内容)-->
	<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10"
		style="font-family: Arial, Helvetica, '宋体'; font-size: 12px; color: #333;">
		<tr>
			<td bgcolor="#FFFFFF">
				<table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
								<tr>
									<td height="60" valign="top" bgcolor="#426cad">
										<!-- 导航条(样式内容) -->
										<table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
											<tr>
												<td height="60" align="left" valign="middle"
													style="font-size: 18px; font-family: Helvetica, Arial, '宋体'; color: #FFF;">
													<span style="font-weight: bold; font-size: 22px;" size="4"> ScholarMate</span> 
													</td>
											</tr>
										</table>
									</td>
								</tr>
								<!--替换内容begin-->
								<tr>
									<td align="center" valign="top"><table width="100%" border="0" cellspacing="20"
											cellpadding="0" style="font-size: 14px;">
											<tr>
												<td align="left" valign="top"><table width="100%" border="0" cellpadding="6"
														cellspacing="0" style="font-size: 14px;">
														<tr style="font-size: 14px;">
															<td colspan="2" style="line-height: 25px;">
															<span style="font-weight: bold;">Hi ,</span>
																	<br /> 
																Please click the button and reset
																your password in 24 hours.
															</td>
														</tr>
														<tr>
															<td colspan="2" valign="top" bgcolor="#f9f9f9">
																<table width="100%" border="0" cellspacing="0" cellpadding="5"
																	style="font-size: 12px; line-height: 16px;">
																	<tr>
																		<td align="left">
																			<div class="mailContent">
																				共享人： ${keycode_sharePsnName}<br /> 标题： ${keycode_sharePsnName}共享给您了
																				${keycode_shareDocCount}个文档 <br /> 内容：
																				${keycode_sharePsnName}共享给您了${keycode_shareDocCount}个文档 ，请点击以下链接查看详细的文档信息：<br />
																				${keycode_shareURL}
																				<div>
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
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<!-- 页脚内容(样式内容) -->
<#include "/base_foot_en_US.ftl" encoding= "UTF-8"> 
</body>
</html>

