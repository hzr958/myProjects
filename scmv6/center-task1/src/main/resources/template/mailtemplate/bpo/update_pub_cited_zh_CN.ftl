<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>更新成果引用</title>
</head>

<body>
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF"><table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
      <tr>
        <td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="60" valign="top" bgcolor="#426cad"><table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">科研之友</td>
                    </tr>
                </table></td>
          </tr>
          <tr>
          <td valign="top" align="center"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
              <tbody><tr>
                <td valign="top" align="left"><table width="100%" border="0" cellspacing="0" cellpadding="6" style="font-size:14px;">
                  <tbody><tr style="font-size:14px;">
                    <td style="line-height:25px;"><span style="font-weight:bold;">${psnName!''}</span>，除了论文数量外，越来越多的科研单位和政府资助机构将<a style="color:#005eac; text-decoration:none;" target="_blank" href="http://www.nsfc.gov.cn/publish/portal0/tab38/info40275.htm" >SCI论文引用次数</a>和H指数作为科研质量的评价指标。                       
                    </td>
                  </tr>
                  <tr>
                    <td valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="6" style="font-size:12px; line-height:16px;">
                      <tbody><tr>
                        <td bgcolor="#e3e3e3" align="left" style="font-weight:bold;">指标</td>
                        <td width="50%" bgcolor="#e3e3e3" align="center" style="font-weight:bold; text-decoration:none;">您的最新统计数据</td>
                        </tr>
                      <tr>
                        <td bgcolor="#f3f3f3" align="left">论文数</td>
                        <td bgcolor="#f3f3f3" align="center"><a style="color:#005eac; text-decoration:none;" target="_blank" href="${updateCitedUrl!'#'}">${pubSum!'0'}</a></td>
                        </tr>
                      <tr>
                        <td bgcolor="#f3f3f3" align="left">SCI引用数</td>
                        <td bgcolor="#f3f3f3" align="center"><a style="color:#005eac; text-decoration:none;" target="_blank" href="${updateCitedUrl!'#'}">${citedSum!'0'}</a></td>
                        </tr>
                      <tr>
                        <td bgcolor="#f3f3f3" align="left">H指数</td>
                        <td bgcolor="#f3f3f3" align="center"><a style="color:#005eac; text-decoration:none;" target="_blank" href="${updateCitedUrl!'#'}">${Hindex!'0'}</a></td>
                      </tr>
                      </tbody></table></td>
                  </tr>
                  </tbody></table></td>
              </tr>
              <tr>
                <td valign="top" align="left"><table width="150" border="0" bgcolor="#6a8bbf" cellspacing="1" cellpadding="0" style="margin-left:6px;">
                  <tbody><tr>
                    <td height="30" bgcolor="#d6e3f6" align="center" style=" border-top:1px solid #ebf1fb;"><a style="font-size:14px; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; text-decoration:none; font-weight:bold;" target="_blank" href="${updateCitedUrl!'#'}">查看详情</a></td>
                  </tr>
                </tbody></table></td>
              </tr>
            </tbody></table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tbody><tr>
                      <td height="40" bgcolor="#f3f3f3" align="center" style="text-align:center; font-size:14px; color:#999999;"><a style="color:#005eac; text-decoration:none;" target="_blank" href="${addPubUrl!'#'}">你可以在科研之友上添加论文成果，更新论文引用，获取最新的引用数据。</a></td>
                    </tr>
                  </tbody></table>
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
