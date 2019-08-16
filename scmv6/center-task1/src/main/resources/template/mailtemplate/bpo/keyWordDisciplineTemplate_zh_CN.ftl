<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科研关键词投票模版</title>
</head>

<body>
<table width="700" border="0" align="center" cellpadding="0" cellspacing="0" id="mailId">
  <tr>
    <td height="50" align="center" style="font-size:12px;font-family: Arial, Helvetica, sans-serif; line-height:18px; color:#666;">如邮件不能正常显示，请打开下面链接查看<br />
      <#if (viewMailUrl?exists)> <a href="${viewMailUrl}" target="_blank">${viewMailUrl}</a><#else><a href="#">http://www.xxxxxx.com</a></#if></td>
  </tr>
</table>
<table width="700" border="0" align="center" cellpadding="0" cellspacing="0" style="border:#4F81BD 1px solid;font-family: Arial, Helvetica, sans-serif;background:#fff;">
  <tr>
    <td height="45" align="center" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" align="left" bgcolor="#4F81BD" style=" font-size:16px;font-weight:bold;text-indent:5px;color:#fff; padding-left:20px;">科研关键词投票</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><table width="650" border="0" align="center" cellpadding="0" cellspacing="0" style="margin:10px 0; font-size:12px;">
      <tr>
        <td width="680" height="25" colspan="2" align="left">尊敬的<#if (userName?exists)> ${userName}<#else> AA</#if>：</td>
      </tr>
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>
      <tr>
        <td height="25" colspan="2" align="left" style="line-height:20px;"><p>规范学科领域关键词一直是科学基金规范管理中最具挑战的难题之一。我们获国家自然科学基金委员会项目（项目编号：J1124003
）的资助，开发了社会化学科关键词投票系统，并诚意邀请您参与“规范学科关键词”的投票活动。 </p>
          <p>您只需花上几分钟时间，点击以下链接，在熟悉的学科领域中选出最有用和最相关的关键词。</p>
          <p>
            <!--<a href="https://www.scholarmate.com" target="_blank">https://www.scholarmate.com</a></p>-->
         <#if (mailBaseUrl?exists)><a target="_blank" style="text-decoration:none;width:93px;height:32px;line-height:32px;background-color:#3A76C0;color:#fff;text-align:center;font-size:14px;display:block;" href="${mailBaseUrl?replace("target0=",voteUrlParams)?replace("target2",keyWordUrl)?replace("target3",keyWordParams) }"><#else><a href="#"></#if>点击进入投票</a></p>
          <p>规范后的学科关键词将可用于精细化科研管理。您还可以利用规范后的关键词分析最新科研动态，寻找合适的科研合作者。<br />
          </p></td>
      </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>

        <td align="left"><strong>蔺振江</strong>&nbsp;&nbsp; 博士<br />
            <#if (mailBaseUrl?exists)><a href="${mailBaseUrl?replace("target1","yes")?replace("target2",scholarMate)?replace("target3",scholarMatelParams)}" target="_blank"><#else><a href="https://www.scholarmate.com" target="_blank"></#if>科研之友</a>|&nbsp;&nbsp; <#if (mailBaseUrl?exists)><a href="${mailBaseUrl?replace("target1","yes")?replace("target2",resumeUrl)?replace("target3",keyUrlParamsForZhen) }" target="_blank"><#else><a href="www.scholarmate/scmwebsns/in/zhenjiang_lin" target="_blank"></#if>我的科研简历</a><br />
社会化学科关键词项目小组成员<br />
<ADDRESS>Email：support@scholarmate.com</ADDRESS><br><hr style="margin:-10px;0"><br />
<div style="color:#A6A6A6;font-size:12px">
此邮件为科研之友系统发出，请勿回复。如果您不想以后再收到此类邮件，点击<#if (screenMailUrl?exists)><a style="text-decoration:none;" href="${screenMailUrl}" target="_blank">此处</a><#else><a href="#">此处</a></#if>取消订阅。<br><br>
科研之友 © 2014 爱瑞思 粤ICP备11010329号<br>
查看我们的<a href="https://www.scholarmate.com/resscmwebsns/html/policy_zh_CN.html">隐私政策</a>和<a href="https://www.scholarmate.com/resscmwebsns/html/condition_zh_CN.html">服务条款</a>。
</div>
		</td>
        </tr>
      <tr>
        <td height="25" colspan="2" align="left" style="line-height:18px;">&nbsp;</td>
      </tr>
    </table></td>
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
