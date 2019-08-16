<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期刊匹配</title>
<script type="text/javascript">
</script>
</head>
<body>
	<table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
		<s:if test="baseJournalTitleList.size()>0">
			<tr align="left">
				<th width="40">序号</th>
				<th width="70">基础期刊ID</th>
				<th>PISSN</th>
				<th>期刊名</th>
				<th width="60">来源</th>
			</tr>
			<s:iterator value="baseJournalTitleList" id="jnlTitle" status="idx">
				<tr align="left">
					<td>${idx.count}&nbsp;<input type="radio" name="tempIds" onclick="parent.checkedBaseJnl(this)" value="<s:property value='#jnlTitle.jnlId' />" /></td>
					<td><s:property value="#jnlTitle.jnlId" />
					</td>
					<td><s:property value="#jnlTitle.pissn" />
					</td>
					<td>期刊英文名：<s:property value="#jnlTitle.titleEn" /><br /> 期刊中文名：<s:property value="#jnlTitle.titleXx" /></td>
					<td><s:property value="#jnlTitle.dbCode" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><br />
					<font color="#FF0000"><b>没有在基础期刊中找到类似记录！</b>
					</font>
					</td>
				</tr>
			</table>
		</s:else>
	</table>
</body>
</html>
