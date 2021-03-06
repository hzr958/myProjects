<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>推荐论文</title>
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
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">科研之友</td>
                    </tr>
              </table></td>
          </tr>
          <tr>
            <td align="center" valign="top">
              <table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
              <tr>
                <td align="left" valign="top">
                <table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
						  <tr style="font-size:14px;">
						    <td style="line-height:25px;"><span style="font-weight:bold;">${psnName!''}</span>老师，向您推荐<a style="font-size:14px; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; text-decoration:none; font-weight:bold;" href="${InterestPubUrl!'#'}">${pubSize!''}</a>篇最新论文：</td>
						  </tr>
						   <#list pubList as pub >
							  <tr>
								<td align="left" valign="top" bgcolor="#f9f9f9">
								  <table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#005eac;">
										<tr>
										 <td width="75" align="left"><img src="${pub.fullTextUrl!'https://www.scholarmate.com/resscmwebsns/images_v5/fulltextImg/nofulltext_img.jpg'}" width="72" height="82" /></td>
										 <td valign="top" align="left" style="font-size:14px; line-height:22px;">
									  		  <a style="text-decoration:none;" href="${pub.pubDetailUrl!'#'}"><span style="font-size:16px; color:#005eac; font-weight:bold; margin-bottom:10px;font-family:Arial, Helvetica, '宋体';">${pub.pubTitle}</span></a><br>
									        <span style="font-size:12px; color:#000;">${pub.authors!''}&nbsp;<#if (author?exists && author?length > 0)><#if (dbId = 0)>等<#else>et al.</#if></#if></span><br>
									        <span style="font-size:12px; color:#000;">${pub.pubBrief!''}</span>
									    </td>
										</tr>
								  </table>
							 </td>
	  						 </tr>
  						 </#list>
    				 </table>
    			   </td>
              </tr>
              <tr>
                <td align="left" valign="top">
                 <table width="100%" border="0" cellspacing="0" cellpadding="0">
                 <tr>
                     <td valign="top" align="left">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                          <tbody><tr>
	                            <td width="26%" valign="middle" align="left"><table width="130" border="0" bgcolor="#6a8bbf" cellspacing="1" cellpadding="0">
	                                <tbody><tr>
	                                  <td height="30" bgcolor="#d6e3f6" align="center" style=" border-top:1px solid #ebf1fb;"><a style="font-size:14px; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; text-decoration:none; font-weight:bold;" href="${InterestPubUrl!'#'}">阅读收藏</a></td>
	                                </tr>
	                              </tbody></table></td>
	                            <td valign="middle" align="left"><table width="130" border="0" bgcolor="#6a8bbf" cellspacing="1" cellpadding="0">
	                                <tbody><tr>
	                                  <td height="30" bgcolor="#d6e3f6" align="center" style=" border-top:1px solid #ebf1fb;"><a style="font-size:14px; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; text-decoration:none; font-weight:bold;" href="${InterestPubUrl!'#'}">查看更多</a></td>
	                                </tr>
	                              </tbody></table></td>
	                             </tr>
                        	</tbody>
                         </table>
                       </td>
                   </tr>
                  <tr>
                    <td colspan="2" align="left" valign="middle">&nbsp;</td>
                    </tr>
                  </table></td>
                   <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">通过科研之友，了解最新论文动态。</td>
                    </tr>
                  </table>
              </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
