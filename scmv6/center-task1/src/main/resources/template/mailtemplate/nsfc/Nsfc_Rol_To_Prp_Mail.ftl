<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基金委申请/在研项目确认</title>
</head>

<body>
<!-- 页眉内容 (样式内容)-->
<table width="600" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333333;">
  <tr>
    <td height="25" align="center" valign="bottom" style="color:#333333; font-size:14px;">
		<!--
		如邮件不能正常显示，请打开下面链接查看<br />
		<#if (viewMailUrl?exists)> <a href="${viewMailUrl}" target="_blank" style="font-size:12px;">${viewMailUrl}</a><#else><a href="http://rol.scholarmate.com">http://rol.scholarmate.com</a></#if>
		-->
    </td>
  </tr>
  <tr>
    <td height="15"></td>
  </tr>
</table>
<!-- 正文内容 (样式内容)-->
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF">
    	<table width="600" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
        <tr>
          <td bgcolor="#FFFFFF">
          	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td height="60" valign="top" bgcolor="#426cad">
                <!-- 导航条(样式内容) -->
                <table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
					  <td height="60" align="left" valign="middle">
					  	<span style="font-size:16pt; font-family:'宋体';font-weight:bold; color:#FFF; ">科研在线 :</span>
					  	<span style="font-family:Microsoft JhengHei;font-weight:bold;font-size:14pt;color:#FFF;"><i>社会化机构知识库，让科研更轻松</i></span>
					  </td>
                      <td align="right" valign="middle" style="font-size:16px; font-weight:bold; color:#FFF;"></td>
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
	                    <td colspan="2" style="line-height:25px;">
	                    	<span style="font-weight:bold;">
		                    	${psnName}
	                    	</span>老师，
	                    </td>
	                  </tr>
	                  <tr>
	                    <td colspan="2" valign="top" bgcolor="#f9f9f9">
	                    <table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; line-height:16px;">
							<tr>
						  	  	<td align="left" style="font-family:'宋体',Microsoft JhengHei;font-size:11pt;">
									
									最新的基金委申请项目数据已上传至依托单位科研在线，请您进入依托单位科研在线，确认您主持或参与的基金委申请/在研项目数据。

						  	  	</td>
						  	</tr>
	                    </table>
						</td>
	                  </tr>
	                  
	                  <tr>
	                    <td height="60" colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	                      <tr>
	                        <td width="220" align="left" valign="middle">
	                        <table width="200" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
	                          <tr>
	                            <td height="37" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;">
									<a href ="${loginUrl}" style="font-size:14px; color:#3f68a8; text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">确认基金委申请/在研项目</a>
								</td>
	                          </tr>
	                        </table>
	                        </td>
	                        <td align="left" valign="middle">
	                        </td>
	                      </tr>
	                    </table></td>
	                  </tr>
	                  <tr><td height="5" colspan="2"></td></tr>
	                  <tr>
	                    <td height="5" colspan="2"><span style="font-weight:bold; font-size:14px;">${insName}</span></td>
	                  </tr>
	                  <tr>
	                    <td align="left" valign="middle" style="line-height:20px;">
							<#if (domain?exists)>
							<span style="font-weight:bold; font-size:14px;">科研在线：</span>
							<a href="${domain}" style="color:#426cad;">${domain}</a>
							</#if>
						</td>
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
<table width="600" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#666666;">
  <tr>
    <td height="15" ></td>
  </tr>
  <tr>
    <td height="40" align="center" style="color:#666666; line-height:150%;">
		此邮件由科研在线系统发出，请勿回复。如对我们的产品有疑问，请发送邮件至<a href="mailto:support@scholarmate.com">support@scholarmate.com</a><br />
		<!--如果您不想以后再收到此类邮件，点击<a href="${screenMailUrl!'#'}" target="_blank">此处</a>取消订阅。-->
	</td>
  </tr>
</table>
<#if (proLoad?exists)>
<#if (isView?exists)>
	<#if isView != "view">
	<iframe src="${proLoad}" style="display:none"></iframe>
	</#if>
	<#else>
	<iframe src="${proLoad}" style="display:none"></iframe>
</#if>
</#if>
</body>
</html>