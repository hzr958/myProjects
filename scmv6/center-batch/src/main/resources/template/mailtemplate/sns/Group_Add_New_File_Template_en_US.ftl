<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
	Scholarmate - Group File Template
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
														<tr style="font-size: 14px;">
															<td colspan="2" style="line-height: 25px;">
																Dear 
																<span style="font-weight: bold;">
																	${receiver},
																</span><br /> 
																<#if fileCount?number == 1>
																	<label>${sender}</label> uploaded a file "${fileNames}" to "${groupName}" group. 
																	<#elseif (fileCount?number > 1)>
																	<label>${sender}</label> uploaded ${fileCount} files named "${fileNames}" etc. to "${groupName}" group. 
																</#if>
															</td>
														</tr>
														 <#if (fileDesc?exists&&fileDesc!="null"&&fileDesc!="")>
														 <tr>
								                     <td colspan="2" valign="top" bgcolor="#f9f9f9" style="padding-top:10px; padding-bottom:10px;">
								                    <table width="100%" border="0" cellspacing="0" cellpadding="5" style="line-height:16px;">
														<tr>
													  	  	<td align="left" style="font-size:12px;">
																${fileDesc}
													  	  	</td>
													  	</tr>
								                    </table>
								                    </td>
								                  </tr>
								                  </#if>
														<tr>
										                  	<td style="height:6px" colspan="2">
															</td>
										                </tr>

														<tr>
															<td height="60" colspan="2">
																<table width="100%" border="0" cellspacing="0"
																	cellpadding="0">
																	<tr>
																		<td align="left" valign="middle">
													                        <table width="190" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
													                          <tr>
													                            <td height="37" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;">
																					<a href="${fileUrl}" style="font-size:14px; color:#3f68a8;text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">View More</a>
																				</td>
													                          </tr>
													                        </table>
													                    </td>
																	</tr>
																	  <tr>
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

