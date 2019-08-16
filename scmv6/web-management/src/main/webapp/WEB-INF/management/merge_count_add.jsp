<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="https://www.w3.org/1999/xhtml">
<script type="text/javascript">
var respath = '${res}';
</script>
<link rel="stylesheet" type="text/css" href="${resmod }/css/public.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/css/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.scmtips.css" />
<script type="text/javascript" src="${res}/js_v5/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.scmtips.js"></script>
<script type='text/javascript' src='${resmod}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${resmod}/js/management/attention_settings_util_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.proceeding.js">
</script>
<script type="text/javascript" src="${resmod}/js/management/merge_count_set.js"></script>
<body id="dialog_content">
  <form action="" id="mainForm" class="cmxform" method="post">
    <div class="dialog_content"
      style="height: 100px; overflow-y: auto; display: flex; justify-content: center; align-items: center; margin-top: 24px;">
      <div
        style="font-family: arial, Tahoma, Verdana, simsun, sans-serif; width: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
        <div style="font-size: 12px; margin-right: 320px;">请在下面输入帐号</div>
        <div>
          <div style="display: flex; margin: 8px 0;">
            <div style="margin-right: 7px;">保留账号邮箱：</div>
            <input type="text" name="mergeCount" id="mergeCount" class="inp_text inp_bg1" style="width: 200px;" />
          </div>
          <div style="display: flex;">
            <div style="margin-right: 7px;">删除账号邮箱：</div>
            <input name="deleteCount" id="deleteCount" class="inp_text inp_bg1" style="width: 200px;" />
          </div>
        </div>
      </div>
      <%-- 	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="edt-Per-information">
		<tr>
			<td align="left" colspan="2"><s:text name="请在下面输入账号" /></td>
		</tr>
		<tr>
			<td width="28%" align="right">
				<s:text name="保留账号邮箱" /><s:text name="colon.all" />
			</td>
			<td align="left">
				<label for="textfield"></label>
				<input type="text" name="mergeCount" id="mergeCount" class="inp_text inp_bg1" style="width: 200px;" />
			</td>
		</tr>
		<tr>
			<td align="right"><s:text name="删除账号邮箱" /><s:text name="colon.all" /></td>
			<td align="left">
			<!-- 合并帐号，建议密码输入框参照登录密码输入框修改，不要把密码显示出来。_TSZ_20140626_SCM-5374 -->
				<input  name="deleteCount" id="deleteCount"class="inp_text inp_bg1" style="width: 200px;" />
			</td>
		</tr>
	</table> --%>
    </div>
    <div style="display: flex; justify-content: flex-end; margin: 12px 100px 0px 0px;">
      <a class="uiButton" onclick="mergeCount.addMerge();" id="submitBtn">合并</a>
    </div>
  </form>
</body>
</html>