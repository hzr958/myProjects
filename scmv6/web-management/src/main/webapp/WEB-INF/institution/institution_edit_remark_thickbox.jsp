<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单位修改历史备注弹出框</title>
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${res }/js/jquery.js"></script>
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
  <form id="mainForm" action="${ctx}/institution/manage/openInsEditRemarkPage" method="post">
    <input type="hidden" name="insId" value="${insId }" />
    <div style="padding: 5px 10px;">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="page_bottonbar">
        <tr>
          <td><span style="float: right"><%@ include file="/common/page-tages.jsp"%></span>
          </td>
        </tr>
      </table>
      <table class="search_table" border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <th width="15%" height="25">操作人</th>
          <th width="15%">修改日期</th>
          <th>备注</th>
        </tr>
        <s:iterator value="page.result" id="result" status="itStat">
          <tr style="${itStat.count%2==0?'background-color:#EFF6FC':''}">
            <td align="center"><s:property value="psnName" /></td>
            <td align="center"><s:date name="createDate" format="yyyy-MM-dd HH:mm:ss" /></td>
            <td style="padding: 5px 0;"><s:property value="remark" /></td>
          </tr>
        </s:iterator>
      </table>
      <s:if test="page.pageCount>0">
      </s:if>
      <s:else>
        <div style="clear: left">没有符合条件的记录</div>
      </s:else>
    </div>
  </form>
</body>
</html>
