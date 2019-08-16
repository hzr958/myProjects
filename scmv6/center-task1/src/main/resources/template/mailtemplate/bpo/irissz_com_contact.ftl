<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户咨询</title>
</head>

<body style="padding-top:50px;">
<table width="700" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#bcbcbc" style="font-family:Arial, Helvetica, '宋体'; font-size:14px; color:#333333;">
  <tr>
    <td width="13%" align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>姓名：</strong></td>
    <td width="36%" align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${name!''}</td>
    <td width="13%" align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>联系电话：</strong></td>
    <td align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${tel!''}</td>
  </tr>
  <tr>
    <td align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>电邮：</strong></td>
    <td align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${email!''}</td>
    <td align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>单位规模：</strong></td>
    <td align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${insScale!''}</td>
  </tr>
  <tr>
    <td align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>单位：</strong></td>
    <td align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${insName!''}</td>
    <td align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>职务：</strong></td>
    <td align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${position!''}</td>
  </tr>
  <tr>
    <td align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>城市：</strong></td>
    <td align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${city!''}</td>
    <td align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>国家：</strong></td>
    <td align="left" bgcolor="#FFFFFF" style="padding:10px 3px;">${country!''}</td>
  </tr>
  <tr>
    <td align="right" bgcolor="#f5f5f5" style="padding:10px 3px;"><strong>地址：</strong></td>
    <td align="left" colspan="3" bgcolor="#FFFFFF" style="padding:10px 3px;">${address!''}</td>
  </tr>
  <tr>
    <td colspan="4" align="left" bgcolor="#FFFFFF" style="padding:15px; line-height:22px;">
    		<strong>您对我们有什么疑问？</strong><br />
            ${remark!''} 
    </td>
  </tr>
</table>

</body>
</html>