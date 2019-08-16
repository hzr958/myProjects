<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基金机会推荐</title>
</head>

<body>
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF"><table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
      <tr>
        <td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="60" valign="top" bgcolor="#426cad">
            	<table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">ScholarMate 科研之友：您科研创新的好帮手</td>
                    </tr>
                </table>
			</td>
          </tr>
          <tr>
            <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                  <tr style="font-size:14px;">
                    <td style="line-height:25px;">
						<span style="font-weight:bold;">${key1!''}</span>老师，<br />一年一度的自科基金申请又开始了，我们为您推荐以下申请机会：                        
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2" valign="top" bgcolor="#f9f9f9">
                    <table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; line-height:16px;">
                      <tr>
                        <td align="left">
                        	<a href="${key5!'#'}${proLogin1601!''}" style="font-weight:bold; font-size:14px; color:#005eac; text-decoration:none;font-weight:bold;">${key2!'#'}</a>
							<#if (key3?exists)>、
                        		<a href="${key5!'#'}${proLogin1601!''}" style="font-weight:bold; font-size:14px; color:#005eac; text-decoration:none;font-weight:bold;">${key3!'#'}</a>
							</#if>
							<#if (key4?exists)>、
                        		<a href="${key5!'#'}${proLogin1601!''}" style="font-weight:bold; font-size:14px; color:#005eac; text-decoration:none;font-weight:bold;">${key4!'#'}</a>
							</#if>
						</td>
                      </tr>
                     </table>
					 </td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="middle"><table width="190" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                      <tr>
                        <td height="37" align="center" bgcolor="#d6e3f6" style="border-top:1px solid #ebf1fb;">
							<a href="${key5!'#'}${proLogin1601!''}" style="font-size:14px; color:#3f68a8;text-align:center;line-height:37px;text-align:center;font-weight:bold; text-decoration:none;">查看更多机会及申报指南</a>
						</td>
                      </tr>
                    </table></td>
                    <td width="250" rowspan="3" align="right">&nbsp;</td>
                  </tr>
                  <tr>
                    <td height="40" align="left">扩大科研网络，获取更多基金申请机会</td>
                  </tr>
                  <tr>
                    <td height="55" align="left" valign="bottom" style="line-height:20px;">
						<span style="font-weight:bold; font-size:14px;">科研之友帮助中心：</span><br />
                    	<a href="https://www.scholarmate.com/help" style="color:#426cad;">https://www.scholarmate.com/help</a>
					</td>
                  </tr>
                </table></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">基金申请、论文投稿、提高引用、找导师/学生/合作者，请使用科研之友。</td>
                </tr>
            </table>
            </td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
