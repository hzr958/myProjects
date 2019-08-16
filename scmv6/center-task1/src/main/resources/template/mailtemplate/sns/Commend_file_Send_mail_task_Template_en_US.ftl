<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
	Scholarmate - ${send_psnName} has shared ${total} file(s) to you
</title>
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
									<td align="center" valign="top">
										<table width="100%" border="0" cellspacing="20"
											cellpadding="0" style="font-size: 14px;">
											<tr>
												<td align="left" valign="top"><table width="100%" border="0" cellpadding="6"
														cellspacing="0" style="font-size: 14px;">
														<tr>
															<td colspan="2" style="line-height:25px; font-size: 14px;">
																<#if zh_CN_psnname?exists >
																Dear 
																<span style="font-weight: bold;">
																	${zh_CN_psnname},
																</span>
																<#else>
																	Hi , 
																</#if><br /> 
																${send_psnName} shared ${total} file(s) that you may like:
															</td>
														</tr>
														<tr>
										                    <td colspan="2" valign="top" bgcolor="#f9f9f9">
										                    <#if (recommendReason?exists)>
										                    <table width="100%" border="0" cellspacing="0" cellpadding="5" style="line-height:16px;">
																<tr>
															  	  	<td align="left" style="font-size:12px;">
																		${recommendReason}
															  	  	</td>
															  	</tr>
										                    </table>
										                    </#if>
															</td>
										                  </tr>
										                  <tr>
										                  	<td style="font-size:14px;" colspan="2"></td>
										                  </tr>
														<tr>
										                  	<td style="height:6px" colspan="2">
															</td>
										                </tr>

														<tr>
															<td height="60" colspan="2">
																<table width="100%" border="0" cellspacing="0"
																	cellpadding="0">
																	<tr>
																		<td width="145" align="left" valign="middle">
													                        <table width="135" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
													                          <tr>
													                            <td height="37" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;">
																					<a href="${keycode_email_view_res_url}" style="font-size:14px; color:#3f68a8;text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">View More</a>
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

