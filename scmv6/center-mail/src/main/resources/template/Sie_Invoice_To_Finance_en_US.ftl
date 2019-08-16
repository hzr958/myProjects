<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SIE Invoice email notification</title>
</head>

<body>
<!-- 正文内容 (样式内容)-->
<table width="728" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei' ; font-size:14px; color:#999999; background:#f7f7f4;">
<tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${sie_ins_domain!''}"  target="_blank"><img src="${sie_ins_domain!""}/ressie/images/sie_email_logo2.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
<tr>
    <td style="padding:24px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="style="font-family:Arial, Helvetica, 'Microsoft YaHei'  ; font-size:14px; color:#333333;">
          <tr><td style="padding-bottom: 20px;"><span style="font-weight: bolder;font-size: 14px;color: black;">
                Please issue the electronic invoice for scientific research verification to the customer:${psn_name}，${email}
          </span></td></tr>
          <tr>
          <tr><td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <!--替换内容begin-->
                <tr><td align="center" valign="top">
                <table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                  <tr>
                    <td align="left" valign="top">
                    <table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                      <tr style="font-size:14px;">
                        <td align="left" style="font-family:Microsoft JhengHei;font-size:11pt;line-height: 15px;color: black;" colspan="2">
                        <p>
				   		Invoice type：
				   		<#if invoice_type == 1>
						  Commercial invoice
						<#else>
						  Special VAT invoice
						</#if>
				   </p> 
                   <p>Invoice amount：${amt}￥</p> 
                   <p>Title：${title}</p> 
                   <p>Ein：${uniform_id}</p>
	                   <#if invoice_type == 2>
						   <p>Bank：${bank}</p> 
		                   <p>Bank account：${bank_no}</p> 
		                   <p>Registered address：${addr}</p> 
		                   <p>registered tel：${tel}</p> 
					   </#if> 
                        </td>
                      </tr>
                    </table>20
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
<#include "/sie_contact_base_foot_en_US.ftl" encoding= "UTF-8">
</body>

</html>