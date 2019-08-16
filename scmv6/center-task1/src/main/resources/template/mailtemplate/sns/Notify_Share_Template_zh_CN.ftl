<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
	科研之友-分享资源模板(包括成果，文献，项目)
</title>
</head>

<body>
<!-- 页眉内容 (样式内容)-->
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
<!-- 正文内容 (样式内容)-->
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
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
					  <td height="60" align="left" valign="middle" style="font-size:20px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">
					  	科研之友
					  </td>
                    </tr>
                </table>
				</td>
              </tr>
              <!--替换内容begin-->
              <tr>
	            <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
	              <tr>
	                <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
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
		                <#if (mailContext?exists)>${psnName}分享了<#else>科研之友 – ${psnName}分享了</#if><#if total?number == 1><a href="${viewUrl!'#'}" style="text-decoration:none;">${total}</a><#switch type><#case '2'>条您可能感兴趣的文献<#break><#case '3'>个您可能感兴趣的文件<#break><#case '4'>个您可能感兴趣的项目<#break><#case '1'>条您可能感兴趣的文献<#break><#default>个您可能感兴趣的资源</#switch><#if (recommendReason?exists&&recommendReason!="null")>：</#if><a href="${viewUrl!'#'}" style="text-decoration:none;">${minZhShareTitle}</a><#elseif (total?number > 1)><a href="${viewUrl!'#'}" style="text-decoration:none;">${minZhShareTitle}</a> 等<a href="${viewUrl!'#'}" style="text-decoration:none;">${total}</a><#switch type><#case '2'>条您可能感兴趣的文献<#break><#case '3'>个您可能感兴趣的文件<#break><#case '4'>个您可能感兴趣的项目<#break><#case '1'>条您可能感兴趣的文献<#break><#default>个您可能感兴趣的资源</#switch><#if (recommendReason?exists&&recommendReason!="null")>：</#if></#if>
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
	                  
	                  <tr style="font-size:14px;">
	                    <td height="60" colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	                      <tr>
	                        <td width="145" align="left" valign="middle">
	                        <table width="135" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
	                          <tr>
								<td height="37" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;">
									<a href="${viewUrl}" style="font-size:14px; color:#3f68a8;text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">查看更多</a>
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
      </td>
  </tr>
</table>
</td>
</tr>
</table>
<!-- 页脚内容(样式内容) -->
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>