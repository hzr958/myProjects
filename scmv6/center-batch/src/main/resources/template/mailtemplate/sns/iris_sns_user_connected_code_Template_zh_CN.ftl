<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
	科研之友 - 站内信
</title>
<script type="text/javascript">
   var spanContent = document.getElementById("spanTemplateContent").innerHTML;
   spanContent = spanContent.replace(/href/g,"");
   spanContent = spanContent.replace(/HREF/g,"");
   document.getElementById("spanTemplateContent").innerHTML = spanContent; 
</script>
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
	            			<td align="center" valign="top">
	            				<table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
	              					<tr>
	                					<td align="left" valign="top">
	                						<table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
	                							<tr style="font-size:14px;">
			                    					<td colspan="2" style="line-height:25px;">
			                    						<span style="font-weight:bold;">${psnName}，</span>您科研之友帐号关联的验证码为：
			                    					</td>
			                  				</tr>
	                  						<tr>
	                    							<td colspan="2" valign="top" bgcolor="#f9f9f9">
															<!-- 正文显示内容 -->
	                    								<table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; line-height:16px;">
																<tr style="font-size:14px; font-weight:bold;">
						  	  										<td align="left" style="padding:10px;">
																		${code}
						  	  										</td>
						  										</tr>
	                    								</table>
														</td>
	                  						</tr>
                  							<tr>
	                  							<td style="height:6px" colspan="2"></td>
	                							</tr>
	                  						<tr style="font-size:14px;">
	                    							<td height="60" colspan="2"></td>
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
<!-- 页脚内容(样式内容) -->
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>