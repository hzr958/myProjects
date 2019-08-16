<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>机构主页邮件确认</title>
</head>
<body>
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, &#39;宋体&#39;; font-size:12px; color:#333;">
  <tbody><tr>
    <td bgcolor="#FFFFFF"><table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
        <tbody><tr>
          <td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tbody><tr>
                <td height="60" valign="top" bgcolor="#426cad"><table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tbody><tr>
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, &#39;宋体&#39;; font-weight:bold; color:#FFF;">科研之友</td>
                    </tr>
                  </tbody></table></td>
              </tr>
              <tr>
                <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                    <tbody><tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                          <tbody>
                          <tr style="font-size:14px;">
                            <td style="line-height:25px;"><span style="font-weight:bold;">${psnName!''}</span>，</td>
                          </tr>
                          <tr>
                            <td align="left" valign="top" style="text-indent:2em; line-height:24px;">你在科研之友上创建了“${insName!''}”主页，请点击以下“确认”按钮完成创建操作。</td>
                          </tr>
                        </tbody></table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tbody><tr>
                            <td width="26%" align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                                <tbody><tr>
                                  <td height="30" align="center" bgcolor="#d6e3f6" style=" border-top:1px solid #ebf1fb;"><a href="${verifyUrl!''}" style="font-size:14px; color:#3f68a8;  text-align:center; line-height:30px; text-align:center; text-decoration:none; font-weight:bold; margin:0 auto; display:inline-block;">确认</a></td>
                                </tr>
                              </tbody></table></td>
                          </tr>
                        </tbody></table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                          <tbody>
                          <tr>
                            <td align="left" valign="top" style=" color:#888888;">如链接点击无效，你可以将链接直接复制到浏览器地址栏内访问。</td>
                          </tr>
                          <tr>
                            <td align="left" valign="top"><a href="${verifyUrl!''}" style="color:#426cad;">${verifyUrl!''}</a></td>
                          </tr>
                        </tbody></table></td>
                    </tr>
                  </tbody></table></td>
              </tr>
            </tbody></table></td>
        </tr>
      </tbody></table></td>
  </tr>
</tbody></table>
</body></html>