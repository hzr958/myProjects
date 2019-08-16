<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单位注册审核</title>
<link href="${res}/css/thickbox.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/dialog.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/plugin/thickbox-compressed.js"></script>
<script type="text/javascript" src="${res}/js/dialog.js"></script>
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
<script type="text/javascript">

function sendResetPwd(insId) {
	$('#sendResetPwd_btn').attr('alt', '/scmmanagement/institution/manage/sendResetPwdMain?insId='
			+ insId + '&TB_iframe=true&height=400&width=600').click();
}

// 编辑单位信息
function editInstitution(insId) {
	window.location.href= '/scmmanagement/institution/manage/editMain?insId=' + insId;
}
</script>
</head>
<body id="main_body">
  <form id="mainForm" action="/scmmanagement/institution/manage/maint" method="post">
    <input type="hidden" id="sendResetPwd_btn" class="thickbox" title="发送重置密码邮件" />
    <div id="page_dept">
      <table cellpadding="0" cellspacing="0">
        <tr>
          <td style="padding-right: 10px;">单位名： <input type="text" class="textbox" id="insName"
            style="width: 130px" name="insName" value="${insName }" /></td>
          <td style="padding-right: 10px;">单位来源: <select id="status" name="insSource" style="width: 155px;">
              <option value="-1">全部</option>
              <option value="0" <s:if test="insSource == 0">selected</s:if>>在线注册</option>
              <option value="1" <s:if test="insSource == 1">selected</s:if>>邀请开通</option>
              <option value="2" <s:if test="insSource == 2">selected</s:if>>ISIS开通</option>
              <option value="3" <s:if test="insSource == 3">selected</s:if>>ISIS同步</option>
          </select>
          </td>
          <td height="40px" align="right"><input type="button" class="button" value="检索"
            onclick="$('#mainForm').submit();" /></td>
        </tr>
      </table>
    </div>
    <%@ include file="/common/tips-msg.jsp"%>
    <s:if test="page.pageCount>0">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="page_bottonbar">
        <tr>
          <td><span style="float: right"><%@ include file="/common/page-tages.jsp"%></span>
          </td>
        </tr>
      </table>
      <table class="search_table" border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <th width="6%" height="25">单位ID</th>
          <th width="25%">单位名称</th>
          <th width="28%">联系方式</th>
          <th width="8%">标记</th>
          <th width="7%">是否传真</th>
          <th width="13%">是否人工确认信息</th>
          <th>操作</th>
        </tr>
        <s:iterator value="page.result" id="result" status="itStat">
          <tr style="${itStat.count%2==0?'background-color:#EFF6FC':''}">
            <td height="20" align="center"><s:property value="id" /></td>
            <td><s:property value="insName" /></td>
            <td style="padding: 5px 0;">&nbsp;联系人：<s:property value="contactPerson" /> <br />
              &nbsp;电&nbsp;&nbsp;&nbsp;&nbsp;话：<s:property value="tel" /> <br /> &nbsp;Email：<s:property
                value="serverEmail" />
            </td>
            <td align="center"><s:if test="insSource == null || (insSource != null && insSource.onlineReg == 1)">在线注册<br />
              </s:if> <s:if test="insSource != null && insSource.inviteReg == 1">邀请<br />
              </s:if> <s:if test="insSource != null && insSource.isisLink == 1">ISIS开通<br />
              </s:if> <s:if test="insSource != null && insSource.isisSync == 1">ISIS同步</s:if></td>
            <td align="center">${empty faxAttachmentPath ? "否" : "是" }</td>
            <td align="center"><s:if test="status == 2 || status == 9">是</s:if> <s:else>否</s:else></td>
            <td align="center"><a href="javascript:void(0);" onclick="editInstitution('<s:property value="id"/>')">编辑</a>
              | <a href="javascript:void(0);" onclick="sendResetPwd('<s:property value="id"/>')">重置密码</a></td>
          </tr>
        </s:iterator>
      </table>
    </s:if>
    <s:else>
      <div style="clear: left">没有符合条件的记录</div>
    </s:else>
  </form>
</body>
</html>
