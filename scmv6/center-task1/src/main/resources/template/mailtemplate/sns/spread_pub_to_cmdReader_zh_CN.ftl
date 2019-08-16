<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
	科研之友 - 读者推荐  - 成果推广邮件
</title>
</head>

<body>
<!-- 页眉内容 (样式内容)-->
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF">
    	<table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
        <tr>
          <td bgcolor="#FFFFFF">
          		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td height="60" valign="top" bgcolor="#426cad">
                    <table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                          <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">科研之友</td>
                        </tr>
                      </table>
                  </td>
              </tr>
              <tr>
                <td align="center" valign="top">
                            <table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                        <tr>
                          <td align="left" valign="top">
                                    <table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                                  <tr style="font-size:14px;">
                                    <td style="line-height:25px;"><span style="font-weight:bold;">${readerName!''}</span>老师：<br/>您好，以下论文与您的研究领域相近，欢迎点击阅读并下载全文：</td>
                                  </tr>
                                  <tr>
                                    <td align="left" valign="top" bgcolor="#f9f9f9">
                                           <table width="100%" border="0" cellspacing="0" cellpadding="5">
                                                    <tr>
                                                      <td width="75" align="left" valign="top"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/fulltextImg/nofulltext_img.jpg" width="72" height="82" /></td>
                                                      <td align="left" valign="top" style="font-size:14px; line-height:22px;"><a style="text-decoration:none;" href="${pubDetailUrl!'#'}"><span style="font-size:16px; color:#005eac; font-weight:bold; line-height:18px;font-family:Arial, Helvetica, '宋体';">${title!''}</span></a><br />
                                                        <span style="font-size:12px; color:#000;">${pubFirstAuthor!''}<#if (dbId?number = 0)>等<#else> et al.</#if></span><br />
                                                        <span style="font-size:12px; color:#000;">${briefDesc!''}</span></td>
                                                    </tr>
                                          </table>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td align="left" valign="top" bgcolor="#FFFFFF">请多多指教。</td>
                                  </tr>
                                </table>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td align="right" valign="middle">&nbsp;</td>
                                <td width="40%" align="left" valign="middle" style="line-height:22px;">${authorName!''}<br />
                                  ${authorEmail!''}<br />
                                 ${data!''}</td>
                              </tr>
                            </table></td>
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
</table>
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>