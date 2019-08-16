<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title><s:text name="referencesearch.edit.name"/></title>
<script type="text/javascript">var appContextPath="${resmod}";</script>
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" /> 
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/CommonUI.js"></script>
<script type="text/javascript">
	function closeDiv(){
		  parent.tb_remove();
		  parent.$("#mainForm").submit();
	}
	
	$(document).ready(function(){
		
	});
	
	function toBaseJnlInsert(){
		var zhName=$.trim($("#zhName").val());
		var enName = $.trim($("#enName").val());
		var issn = $.trim($("#issn").val());
		var dbcode = $.trim($("#dbcode").val());
		if(zhName=='' && enName==''){
			alert("期刊中英文标题不能同时为空！");
			return;
		}
		var reg = /^[0-9]{4}-[0-9A-Za-z]{4}$/;
		if(("****_****"!=issn && "****-****"!=issn)){
			if($.trim(issn)=='' || !reg.test(issn)){
				alert("issn格式有错误。正确格式为：4个数字-4个数字或字母。如：1234-r6ty");
				return;
			}	
		}
		
		if(dbcode==''){
			alert("请为新增的期刊选择来源！");
			return;
		}
		var data = {"temp":"handJournal","jnl.id":"${jnl.id}","jnl.zhName":zhName,"jnl.enName":enName,"jnl.issn":issn,'jnl.fromFlag':dbcode};
		$.ajax({
			url : "${ctx}/journal/toCheckInJnl",
			type : 'post',
			dataType:'json',
			data : data,
			success : function(data) {
				if(data.result=='dbidnull'){
					alert("新增失败，该条数据在jnl_ref_db表没有找到相应的来源信息。");
				}else if(!isNaN(data.result)){
					if(data.result==1){
						alert("该期刊在基础期刊中存在，系统已经自动关联！");
						parent.$("#mainForm").submit();
					}else{
						alert("新增数据已进入基础期刊导入流程，请继续处理...");
						window.open("${ctx}/journal/update?temp=batch&jnlId="+data.result);
						$("#subBut").attr("disabled","disabled");
					}
				}else{
					alert(data.result);
				}
			}
		});
	}
</script>
</head>
<body>
<s:form id="mainForm">
<br/>
<%@ include file="/common/tips-msg.jsp"%>
<input type="hidden" name="jnl.id" value="${jnl.id}" />
<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:14px;float: left">
	<tr>
		<td width="5%">&nbsp;</td>
		<td>中文名称：</td>
		<td><input type="text" id="zhName" name="jnl.zhName" maxlength="255"  style="width:420px;" value="${jnl.zhName}" /></td>
	</tr>
	<tr>
		<td style="margin-top: 5px">&nbsp;</td>
		<td style="margin-top: 5px">英文名称：</td>
		<td><input type="text" id="enName" name="jnl.enName" maxlength="250" style="width: 420px;margin-top: 5px" value="${jnl.enName}"/></td>
	</tr>
	<tr>
		<td style="margin-top: 5px">&nbsp;</td>
		<td style="margin-top: 5px"><b class="red">*</b>ISSN：</td>
		<td><input type="text" id="issn" name="jnl.issn" maxlength="40" style="width:100px;margin-top: 5px" value="${jnl.issn}"/></td>
	</tr>
	<tr>
		<td style="margin-top: 5px">&nbsp;</td>
		<td style="margin-top: 5px">来源：</td>
		<td>
			数据源：${jnl.fromFlag}	&nbsp;&nbsp;&nbsp;&nbsp;<b class="red">*</b>基础期刊来源：
			<select id="dbcode">
				<option value="">请选择</option>
				<s:iterator value="jnlRefDbList" var="item">
				<option value="${item.dbCode}"  ${jnl.fromFlag eq item.dbCode?'selected=selected':''}>${item.dbCode}</option>
				</s:iterator>
			</select>	
		</td>
	</tr>
</table>


<table width="96%" border="0" cellpadding="0" cellspacing="0">
  <tr>
  	<td width="5%">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
  	<td>&nbsp;</td>
    <td class="button_bar" style="patext-align: left;">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input id="subBut" type="button" onclick="toBaseJnlInsert()"  value="确定" class="button" /> 
    &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
    <input type="button" onclick="closeDiv()" value="关 闭" class="button" />
   </td>
  </tr>
</table>
</s:form>
</body>
</html>