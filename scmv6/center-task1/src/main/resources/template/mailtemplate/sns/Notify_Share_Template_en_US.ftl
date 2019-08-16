<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
	Scholarmate - ${psnName} has shared ${total} resource(s) to you
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
															<td colspan="2" style="line-height:25px;font-size:14px;">
																<#if (recvName)??>
																Dear 
																<span style="font-weight: bold;">
																	${recvName},
																</span>
																<#else>
																	Hi ,
																</#if>
																<br /> 
															</td>
														</tr>
														<tr>
															<td colspan="2" style="line-height:150%;font-size:14px;">
																<!-- 正文显示内容 -->
																<!--mailContext-->
																<#if (mailContext?exists)>${psnName} shared <#else>ScholarMate - ${psnName} shared </#if><#if total?number == 1><a href="${viewUrl!'#'}" style="text-decoration:none;">${total}</a><#switch type><#case '2'> reference(s) that you may like <#break><#case '3'> file(s) that you may like <#break><#case '4'> project(s) that you may like <#break><#case '1'> reference(s) that you may like <#break><#default> resource(s) that you may like </#switch><#if (recommendReason?exists&&recommendReason!="null")>：</#if> <a href="${viewUrl!'#'}" style="font-weight:bold; text-decoration:none;">${minEnShareTitle}</a><#elseif (total?number > 1)><a href="${viewUrl!'#'}" style="text-decoration:none;">${minEnShareTitle}</a> and <a href="${viewUrl!'#'}" style="text-decoration:none;">${total?eval - 1}</a> more<#switch type><#case '2'> reference(s) that you may like <#break><#case '3'> file(s) that you may like <#break><#case '4'> project(s) that you may like <#break><#case '1'> reference(s) that you may like <#break><#default> resource(s) that you may like </#switch><#if (recommendReason?exists&&recommendReason!="null")>：</#if></#if>
															</td>
														</tr>
                                                        <tr>
										                    <#if (recommendReason?exists&&recommendReason!="null")>
										                    <td colspan="2" valign="top" bgcolor="#f9f9f9" style="padding-top:10px; padding-bottom:10px;">
										                    <table width="100%" border="0" cellspacing="0" cellpadding="5" style="line-height:16px;">
																<tr>
															  	  	<td align="left" style="font-size:12px;">
																		${recommendReason}
															  	  	</td>
															  	</tr>
										                    </table>
										                    	</td>
										                    </#if>
										                  </tr>
										                  <tr>
										                  	<td style="line-height:16px; font-size:14px;" colspan="2"></td>
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
																		<td align="left" valign="middle">
													                         <table width="190" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
													                          <tr>
													                            <td height="37" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;">
																					<a href="${viewUrl}" style="font-size:14px; color:#3f68a8;text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">View More</a>
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

