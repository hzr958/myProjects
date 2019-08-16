<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>处理导入的IsI影响因子数据</title>
<script type="text/javascript" src="${resmod}/js/UIpublic.js"></script>
<script type="text/javascript" src="${resmod}/js/thickbox-compressed.js"></script>
<link href="${resmod}/css/thickbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" >
function checkedJnl(thiss){
	if(thiss.checked){
	   $("#jnlId").attr("value",thiss.value);
	}
}
//add by liuwei
function errorPissn(){
	var pissn = $("#ifPissn").val();
	if($.trim(pissn)=='')
		return false;
	var reg = /^[0-9]{4}-[0-9A-Za-z]{4}$/;
	if(!reg.test(pissn)){
		return true;
	}
	return false;
	
}
function toCheckOriginally(){
	var data;
	data = {"jnlId":$("#jnlId").val(),"temp":"isiIf","isiIf.tempIsiIfId":$("input[name='isiIf.tempIsiIfId']").val(),"isiIf.pissn":$("#ifPissn").val(),"isiIf.titleXx":$("#ifTitleXx").val(),"isiIf.titleEn":$("#ifTitleEn").val(),"isiIf.dbId":$("input[name='isiIf.dbId']").val()};
	$.ajax({
		url : "toCheck.action",
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			if(!data){
				alert("操作失败！");
			}
			alert("操作成功！");
			setTimeout(function(){back();},1000);
		}
	});
} 

function toCheckUpdateJnl(){
	if(errorPissn()){
		alert("Pissn格式有错误。正确格式为：4个数字-4个数字或字母。如：1234-r6ty");
		return;
	}
	if($("#jnlId").val()==""){
		alert("请选择要更新的期刊！");
		return;
	}
	var data = {"jnlId":$("#jnlId").val(),"temp":"isIif","isiIf.tempIsiIfId":$("input[name='isiIf.tempIsiIfId']").val(),"isiIf.pissn":$("#ifPissn").val(),"isiIf.titleXx":$("#ifTitleXx").val(),"isiIf.titleEn":$("#ifTitleEn").val(),"isiIf.dbId":$("input[name='isiIf.dbId']").val()};
	$.ajax({
		url : "${ctx}/journal/toCheckUpJnl",
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			if(!data){
				alert("操作失败！");
			}
			alert("操作成功！");
			setTimeout(function(){back();},1000);
		}
	});
} 

function toCheckInsertJnl(){
	if(errorPissn()){
		alert("Pissn格式有错误。正确格式为：4个数字-4个数字或字母。如：1234-r6ty");
		return;
	}
	var data = {"jnlId":$("#jnlId").val(),"temp":"isiIf","isiIf.tempIsiIfId":$("input[name='isiIf.tempIsiIfId']").val(),"isiIf.pissn":$("#ifPissn").val(),"isiIf.titleXx":$("#ifTitleXx").val(),"isiIf.titleEn":$("#ifTitleEn").val(),"isiIf.dbId":$("input[name='isiIf.dbId']").val()};
	$.ajax({
		url : "${ctx}/journal/toCheckInJnl",
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			if(!data){
				alert("操作失败！");
			}
			back();
		}
	});
}

function back(){
	$("#mainFormChain").attr('action', "${ctx}/journal/fiIsIif"); 
	$("#mainFormChain").submit();
}
</script>
</head>
<body>
<div id="main">
<div class="errorMessage"></div>
<div class="successMessage"></div>
<div class="message"  style="display: none"><s:actionmessage/></div>
<s:form id="mainFormChain" name="mainFormChain" method="post"  action="" theme="simple">
	<s:hidden name="isiIf.titleXx"  value="%{#session.isiIf.titleXx}"/>
	<s:hidden  name="isiIf.pissn" value="%{#session.isiIf.pissn}" />
	<s:hidden  name="page.pageNo" value="%{#session.page.pageNo}" />
	<s:hidden  name="page.pageSize" value="%{#session.page.pageSize}"/>
</s:form>
<s:form id="mainForm" name="mainForm" method="post"  action="" theme="simple">
<input name=jnlId id="jnlId" type="hidden" value=""/>
<input name=temp id="temp" type="hidden" value="IsIif"/>
	<div class="but_div">
	<input type="button"  value="保留原样" onclick="toCheckOriginally();" class="button" />&nbsp;
	<!--<input type="button"  value="新增期刊" onclick="toCheckInsertJnl();" class="button" />&nbsp;-->
	<input type="button" value="更新至选中期刊" onclick="toCheckUpdateJnl();" class="button" />&nbsp;
	<input type="button" value="返回" onclick="back()" class="button" />
	</div>
	<br /><br />
	<c:if test="${!empty check}">
		<div><strong>拒绝数据</strong></div>
		<table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
			<tr align="left">
				<th>temp_check_id</th>
				<th>PISSN</th>
				<th>期刊名</th>
				<th>来源</th>
			</tr>
			<tr align="left">
				<td>${check.tempCheckId}</td>
				<td><s:property value="check.pissn" /></td>
				<td>
					期刊英文名：<s:property value="check.titleEn" /><br/>
					期刊中文名：<s:property value="check.titleXx" />
				</td>
				<td><s:property value="check.dbCode" /></td>
			</tr>
		</table>
		<br/>
	</c:if>
	<div><strong>处理数据</strong></div>
	<table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
		<tr align="left">
			<th>临时表Id</th>
			<th>PISSN</th>
			<th>期刊名</th>
			<th>来源</th>
		</tr>
		<tr align="left">
			<td>${isiIf.tempIsiIfId}</td>
			<td>
			<input name="isiIf.tempIsiIfId" type="hidden" value="<s:property value="isiIf.tempIsiIfId" />"/>
			<input style="width:80px;" id="ifPissn"  name="isiIf.pissn" maxlength="9" value="<s:property value="isiIf.pissn" />" /></td>
			<td>
				期刊英文名：<input style="width:350px;" id="ifTitleEn" name="isiIf.titleEn" maxlength="250" value="<s:property value="isiIf.titleEn" />"/><br/>
				期刊中文名：<input style="width:350px;" id="ifTitleXx" name="isiIf.titleXx" maxlength="250" value="<s:property value="isiIf.titleXx" />" />
			</td>
			<td><s:property value="isiIf.dbCode" /><input name="isiIf.dbId" type="hidden" value="<s:property value="isiIf.dbId" />"/></td>
		</tr>
	</table>
	<br/><strong>基础期刊数据</strong>
	<table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
		<s:if test="baseJournalTitleList.size()>0">
			<tr align="left">
			<th width="40">序号</th>
			<th width="70">基础期刊ID</th>
			<th width="70">PISSN</th>
			<th>期刊名</th>
			<th width="60">来源</th>
			</tr>
		<s:iterator value="baseJournalTitleList" id="jnlTitle" status="idx">
			<tr align="left">
				<td>${idx.count}&nbsp;<input type="radio" name="tempIds" value="${jnlTitle.jouTitleId}" onclick="checkedJnl(this)"/></td>
				<td><s:property value="#jnlTitle.jnlId" /></td>
				<td><s:property value="#jnlTitle.pissn" /></td>
				<td>
					期刊英文名：<s:property value="#jnlTitle.titleEn" /><br/>
					期刊中文名：<s:property value="#jnlTitle.titleXx" />
				</td>
				<td><s:property value="#jnlTitle.dbCode" /></td>
			</tr>
		</s:iterator>
		</s:if><s:else>
		<div class="curved">
		<b class="b1"></b><b class="b2"></b><b class="b3"></b><b class="b4"></b>
		<div class="roundTable">
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td><br/><font color="#FF0000" ><b>没有在基础期刊中找到类似记录！</b></font></td>
			  </tr>
			</table>
		</div>
		<b class="b4"></b><b class="b3"></b><b class="b2"></b><b class="b1"></b>
	   </div>
		</s:else>
	</table>
</s:form>
</div>
</body>
</html>
