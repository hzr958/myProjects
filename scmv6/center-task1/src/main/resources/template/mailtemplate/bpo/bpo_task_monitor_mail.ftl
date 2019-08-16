<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>任务监控邮件</title>
<style type="text/css">
.page_listtitle {
	height: 24px;
	background: #DCEEF6;
	border-top: #B9D8F6 1px solid;
}

.page_listtitle td {
	height: 24px;
	line-height: 24px;
	color: #003366;
	text-align: center;
}

.page_app_info {
	padding: 2px;
}

.page_app_info td {
	line-height: 20px;
	padding: 3px;
}
</style>
</head>
<body>
	<p align="center" style="font-weight: bold; font-size: 16px;">最近两周任务运行情况<#if (run_env?exists) && run_env!='run'>(${run_env})</#if></p>
	<table width="1200" border="0" cellpadding="0" cellspacing="0" class="page_app_info">
		<tr class="page_listtitle">
			<td width="50" style="border-right: #B9D8F6 1px solid">编号</td>
			<td width="280" style="border-right: #B9D8F6 1px solid">名称</td>
			<td width="60" style="border-right: #B9D8F6 1px solid">系统节点</td>
			<td width="100" style="border-right: #B9D8F6 1px solid">最后开始时间</td>
			<td width="100" style="border-right: #B9D8F6 1px solid">最后出错时间</td>
			<td width="100" style="border-right: #B9D8F6 1px solid">最后结束时间</td>
			<td width="200" style="border-right: #B9D8F6 1px solid">负责人</td>
			<td width="50" style="border-right: #B9D8F6 1px solid">启动</td>
			<td width="50" style="border-right: #B9D8F6 1px solid">成功</td>
			<td width="50" style="border-right: #B9D8F6 1px solid">异常</td>
		</tr>
		<#if (datas?exists)>
			<#list datas as map>
				<tr align="center" bgcolor="<#if (map_index%2==0)>#EFF6FC</#if>">
					<td width="50">${map["task_id"]}</td>
					<td width="280" align="left">${map["task_name"]}</td>
					<td width="60">${map["sys_node"]}</td>
					<td width="100">${map["last_start_at"]!''}</td>
					<td width="100">${map["last_error_at"]!''}</td>
					<td width="100">${map["last_end_at"]!''}</td>
					<td width="200">${map["email"]!''}</td>
					<td width="50">${map["run_num"]!'0'}</td>
					<td width="50">${map["success_num"]!'0'}</td>
					<td width="50">${map["error_num"]!'0'}</td>
				</tr>
			</#list>
		</#if>
	</table>
</body>
</html>