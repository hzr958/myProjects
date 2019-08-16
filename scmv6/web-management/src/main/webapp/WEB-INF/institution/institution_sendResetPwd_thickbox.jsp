<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单位修改历史备注弹出框</title>
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${res}/js/plugins/scmtip/scmtip.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${res }/js/jquery.js"></script>
<script type="text/javascript" src="${res }/js/json2.js"></script>
<script type="text/javascript" src="${res }/js/plugins/scmtip/scmtip.js"></script>
<script type="text/javascript">
function sendResetPwdEmail() {
	var jsonArray = new Array();
	$(':checkbox[name="account"]').filter(':checked').each(function() {
		jsonArray.push({
			'des3PsnId' : $(this).val(),
		});
	});
	if (jsonArray.length <= 0) {
		show_msg_tips('warn', '你还没有选择发送邮件的账号');
		return;
	}
	$.ajax({
		method : "POST",
		url : "/scmmanagement/institution/manage/sendResetPwd",
		data : {
			"jsonParam" : JSON.stringify(jsonArray)
		},
		dataType : "json",
		success : function(data) {
			if (data.result == "success") {
				parent.tb_remove();
				show_msg_tips('success', data.msg);
			} else {
				if (data.result == "warn" || data.result == "error") {
					show_msg_tips(data.result, data.msg);
				} else {
					if(window.confirm('您还未登陆或登陆已超时，是否重新登陆？')) {
						 parent.window.location.reload();
					}
				}
			}
		},
		error : function() {
			show_msg_tips('error', '网络连接异常');
		}
	});
}

function selectAllCheckbox(obj, checkboxName) {
	var choose = obj.checked;
	$(":checkbox[name='" + checkboxName + "']").each(function() {
		$(this).attr("checked", choose);
	});
};

//在弹出框中显示提示信息
function show_msg_tips(type, msg){
	if(!type || !msg)
		return;
	var time=3000;
	if('success'==type || 'yes'==type)
		scmSuccess(msg,null,time);
	if('warn'==type || 'warning'==type)
		scmWarn(msg,null,time);
	if('error'==type || 'wrong'==type)
		scmError(msg,null,time);
}
</script>
<style type="text/css">
.search_table {
  border-collapse: collapse;
  color: #114473;
}

.search_table th, .search_table td {
  border: 1px solid #9BBDD5;
}

.search_table th {
  background-color: #E2EEFA;
  border-bottom: 0;
}
</style>
</head>
<body>
  <form id="mainForm" action="" method="post">
    <div style="padding: 5px 10px;">
      <s:if test="userList != null">
        <div style="margin: 10px 0 10px 0;">选择需要发送重置密码邮件的管理帐号:</div>
        <table class="search_table" border="0" cellpadding="0" cellspacing="0" width="100%">
          <tr>
            <th width="8%" height="25"><input type="checkbox" name="selectAll" id="selectAll"
              onclick="selectAllCheckbox(this, 'account');" /></th>
            <th width="50%">邮箱</th>
            <th>账号</th>
          </tr>
          <s:iterator value="userList" id="result" status="itStat">
            <tr style="${itStat.count%2==0?'background-color:#EFF6FC':''}">
              <td align="center" style="padding: 5px 0;"><input type="checkbox" name="account"
                value="<iris:des3 code="${id }"  />" /></td>
              <td align="left">${email }</td>
              <td>${loginName }</td>
            </tr>
          </s:iterator>
        </table>
        <div style="padding-top: 15px;">
          <input type="button" class="button" value="确认" style="height: 25px" onclick="sendResetPwdEmail();" /> <input
            type="button" class="button" value="取消" style="height: 25px" onclick="parent.tb_remove();" />
        </div>
      </s:if>
      <s:else>
        <div style="clear: left">没有符合条件的记录</div>
      </s:else>
    </div>
  </form>
</body>
</html>
