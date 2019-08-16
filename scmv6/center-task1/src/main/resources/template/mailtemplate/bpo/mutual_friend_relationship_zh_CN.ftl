<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>共同好友关系</title>
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
                      <td height="60" align="left" valign="middle" style="font-size:22px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">ScholarMate 科研之友：您科研创新的好帮手</td>
                    </tr>
                </table></td>
          </tr>
          <tr>
            <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                  <tr>
                    <td style="font-size:14px;"><span style="font-weight:bold;">${key1!''}</span>老师，以下是您感兴趣的人：</td>
                  </tr>
                  <tr>
                    <td bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" bgcolor="#f9f9f9" cellpadding="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
                      <tr>
                      	<#if (key2?exists)>
                        <td width="33%" align="left" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="60" height="50" rowspan="3" align="left">
                            	<a href="${key12!'#'}${proLogin1301!''}"><img src="${json1!'http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/default_avatar.gif'}" width="50" height="50" border="0" /></a>
							</td>
                            <td height="18" align="left">
								<a href="${key12!'#'}${proLogin1301!''}" style="font-weight:bold; font-size:12px; color:#005eac; text-decoration:none;">${key2}</a>
							</td>
                          </tr>
                          <tr>
                            <td height="14" align="left" style="color:#999;">${key3!'#'}</td>
                          </tr>
                          <tr>
                            <td height="18" align="left">
                            	<img src="http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/add_icon.gif" width="10" height="10" />&nbsp;
								<a href="${key4!'#'}${proLogin1301!''}" style="text-decoration:none; font-size:12px; color:#333;">加为好友</a>
							</td>
                          </tr>
                        </table>
						</td>
						</#if>
						<#if (key5?exists)>
                        <td align="left" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="60" height="50" rowspan="3" align="left">
                            	<a href="${key13!'#'}${proLogin1301!''}"><img src="${json2!'http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/default_avatar.gif'}" width="50" height="50" border="0" /></a>
							</td>
                            <td height="18" align="left">
								<a href="${key13!'#'}${proLogin1301!''}" style="font-weight:bold; font-size:12px; color:#005eac; text-decoration:none;">${key5}</a>
							</td>
                          </tr>
                          <tr>
                            <td height="14" align="left" style="color:#999;">${key6!'#'}</td>
                          </tr>
                          <tr>
                            <td height="18" align="left">
                            	<img src="http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/add_icon.gif" width="10" height="10" />&nbsp;
								<a href="${key7!'#'}${proLogin1301!''}" style="text-decoration:none; font-size:12px; color:#333;">加为好友</a>
							</td>
                          </tr>
                        </table>
						</td>
						</#if>
						<#if (key8?exists)>
                        <td width="33%" align="left" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="60" height="50" rowspan="3" align="left">
                            	<a href="${key14!'#'}${proLogin1301!''}"><img src="${json3!'http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/default_avatar.gif'}" width="50" height="50" border="0" /></a>
							</td>
                            <td height="18" align="left">
								<a href="${key14!'#'}${proLogin1301!''}" style="font-weight:bold; font-size:12px; color:#005eac; text-decoration:none;">${key8}</a>
							</td>
                          </tr>
                          <tr>
                            <td height="14" align="left" style="color:#999;">${key9!'#'}</td>
                          </tr>
                          <tr>
                            <td height="18" align="left">
                            	<img src="http://bpo.scholarmate.com/resscmwebbpo/images_v5/mail_template/add_icon.gif" width="10" height="10" />&nbsp;
								<a href="${key10!'#'}${proLogin1301!''}" style="text-decoration:none; font-size:12px; color:#333;">加为好友</a>
							</td>
                          </tr>
                        </table>
						</td>
						</#if>
                      </tr>
                    </table></td>
                  </tr>
                  <tr>
                    <td style="line-height:20px; font-size:14px;">与他们成为好友，及时了解他们的科研动态，互相赞与分享科研成果，提高论文引用率和科研影响力。</td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="middle"><table width="190" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                      <tr>
                        <td height="37" align="center" bgcolor="#d6e3f6" style=" border-top:1px solid #ebf1fb;">
							<a href="${key11!'#'}${proLogin1302!''}" style="font-size:14px; color:#3f68a8; text-align:center; line-height:37px; text-align:center; font-weight:bold; text-decoration:none;">查看更多</a>
						</td>
                      </tr>
                    </table></td>
                    <td width="250" rowspan="3" align="right">
                    	<img src="<#if (resourceUrl?exists)>${resourceUrl}/images_v5/mail_template/thesis_partner_relationship_zh_CN.gif<#else>#</#if>" width="207" height="116" />
					</td>
                  </tr>
                          <tr>
                            <td height="15">&nbsp;</td>
                          </tr>
                  <tr>
                    <td height="55" align="left" valign="bottom" style="line-height:20px;"><span style="font-weight:bold; font-size:14px;">科研之友帮助中心：</span><br />
                      <a href="https://www.scholarmate.com/help" style="color:#426cad;">https://www.scholarmate.com/help</a></td>
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
