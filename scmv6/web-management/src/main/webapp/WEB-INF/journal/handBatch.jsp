<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期刊匹配</title>
<script type="text/javascript" src="${resmod}/js/loadding_div2.js"></script>
<script type="text/javascript" src="${resmod}/js/thickbox-compressed.js"></script>
<link href="${resmod}/css/thickbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" >
var jid="";
var issn="";
var zhName="";
var enName="";
var isChecked=true;
var isInit=true;
	
$(document).ready(function(){
	jid= "${jnl.id}";
	issn = "${jnl.issn}";
	zhName = "${jnl.zhName}";
	enName = "${jnl.enName}";
	//loadMatchBjnlData();	
});

function toCheckOriginally(){	
	var jids = "";
	$("input[name='checkbox']").each(function() {
		if ($(this).attr("checked")) {
			jids+=$(this).val()+',';
		}
	});
	if(jids.length<=0){
		alert("请选择需要操作的数据");
		return;
	}	
	if(confirm('确认保留原样？')){
		jids = jids.substr(0,jids.lastIndexOf(','));
		if(jids.length<=0){
			alert("请选择需要操作的数据");
			return;
		}		
		var data = {'jids':jids,'temp':'handJournal'};
		$.ajax({
			url : "toCheck.action",
			type : 'post',
			dataType:'json',
			data : data,
			success:function(data){
				$("#mainForm").submit();
			}
		});
	};
}

function checkJnl(thiss){
	isInit=false;
	if ($(thiss).attr("checked")) {
		 jid= $(thiss).val();
		 issn = $(thiss).attr("issn");
		 zhName = $(thiss).attr("zhName");
		 enName = $(thiss).attr("enName");
		 isChecked=true;
		 loadMatchBjnlData();
	}else if(!checkboxAll()){
		jid= "";
		issn = "";
		zhName = "";
		enName = "";
		loadMatchBjnlData();
	}else{
		 jid= $(thiss).val();
		 issn = $(thiss).attr("issn");
		 zhName = $(thiss).attr("zhName");
		 enName = $(thiss).attr("enName");
		 isChecked=false;
		 loadMatchBjnlData();
	}
}

function checkboxAll(){
	var flag=false;
	isInit=false;
	$("input[name='checkbox']").each(function() {
		if ($(this).attr("checked")) {
			flag=true;
		}
	});
	return flag;
}

function selectAll(){
	isInit=false;
	 isChecked=true;
	$("input[name='checkbox']").each(function() {
		$(this).attr("checked",true);
	});
	$("input[name='checkbox']").each(function() {
		if($(this).attr("checked")){
		 jid= $(this).val();
		 issn = $(this).attr("issn");
		 zhName = $(this).attr("zhName");
		 enName = $(this).attr("enName");
		 loadMatchBjnlData();
		 }
	});	
}

function loadMatchBjnlData(){
	Loadding_div.show_over("bjnlList","loadbody_right");
	var data={'jnl.id':jid,'jnl.issn':issn,'jnl.zhName':zhName,'jnl.enName':enName,'isChecked':isChecked,'isInit':isInit};
	$.ajax({
		url : "${ctx}/journal/ajaxMatchBjnl",
		type : 'post',
		dataType:'html',
		data : data,
		success:function(data){
			$("#bjnlList").html(data);
			Loadding_div.close("loadbody_right");
		},
		error:function(){
			Loadding_div.close("loadbody_right");
		}
	});
}
function checkedBaseJnl(thiss){
	if(thiss.checked){
	   $("#baseJid").val($(thiss).val());
	}
}

function toCheckUpdateJnl(){
	var jids = "";
	$("input[name='checkbox']").each(function() {
		if ($(this).attr("checked")) {
			jids+=$(this).val()+',';
		}
	});
	jids = jids.substr(0,jids.lastIndexOf(','));
	var baseJid = $("#baseJid").val();
	if(jids.length<=0){
		alert("请选择需要操作的数据");
		return;
	}
	if(baseJid==''){
		alert("请选择匹配的基础期刊");
		return;
	}
	if(confirm("确定更新至匹配至选中期刊？")){
		if(jids.length<=0 || baseJid==''){
			alert("请选择需要操作的数据");
			return;
		}		
		var data = {"jids":jids,"temp":'handJournal','baseJid':baseJid};
		$.ajax({
			url : "${ctx}/journal/toCheckUpJnl",
			type : 'post',
			dataType:'json',
			data : data,
			success : function(data) {
				$("#mainForm").submit();
			}
		});
	}
}

function toCheckInsertJnl(){
	var jids="";
	var count=0;
	$("input[name='checkbox']").each(function() {
		if ($(this).attr("checked")) {
			jids+=$(this).val();
			count++;
		}
	});
	if(jids.length<=0){
		alert("请选择需要操作的数据");
		return;
	}	
	if(count>1){
		alert("只能选择一条期刊进行新增！");
		return;
	}
	$("#new_addjnl_but").attr("alt","${ctx}/journal/iframeJnl?jnl.id="+jids+"&TB_iframe=true&height=200&width=550");
	$("#new_addjnl_but").click();
}

function toCanlAll(){
	$("input[name='checkbox']").each(function() {
		$(this).attr("checked",false);		
	});
	$("#bjnlList").html("");
}
</script>
</head>
<body>
<div id="main">
<div class="errorMessage"></div>
<div class="successMessage"></div>
<div class="message"  style="display: none"><s:actionmessage/></div>
<s:form id="mainForm" name="mainForm" method="post"  action="" theme="simple">
<input name="page.totalCount" type="hidden" value="${page.totalCount }"></input>
<input name="psnId" id="psnId" type="hidden" value=""/>
<input name="baseJid" id="baseJid" type="hidden" value=""/>
<input name="addfind" id="addfind" type="hidden" value=""/>
<input name="resultMsg" id="resultMsg" type="hidden" />
<input type="hidden" id="new_addjnl_but" class="thickbox" title="新增期刊" />
	<br/><img src="${resmod}/images/arrow.gif" />&nbsp;<strong>选中期刊对应的基础期刊类似数据</strong>
	<div id="bjnlList"></div>
	<br />
	<div class="but_div">
	<input type="button"  value="保留原样" onclick="toCheckOriginally();" class="button" />&nbsp;
	<input type="button" value="匹配至选中期刊" onclick="toCheckUpdateJnl();" class="button" />&nbsp;
	<input type="button" id="insert"  value="新增期刊" onclick="toCheckInsertJnl();" class="button" />&nbsp;
	<input type="button" id="insert"  value="清空选项" onclick="toCanlAll();" class="button" />
	
	</div>
	<br/><img src="${resmod}/images/arrow.gif" />&nbsp;<strong>待处理期刊列表</strong>
	<s:if test="page.pageCount>0">
	<div style="float: right;">
	<%@ include file="/common/page-tages.jsp"%>
	</div>
	<table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
		<tr>
			<th width="50"></th>
			<th >JID</th>
			<th width="80">PISSN</th>
			<th>期刊名</th>
			<th>来源</th>
		</tr>
			 <s:iterator value="page.result" id="journal" status="idx">
				<tr align="left">
					<td><input type="checkbox" onclick="checkJnl(this);" name="checkbox" value="${journal.id}" issn="${journal.issn}" zhName="${journal.zhName}" enName="${journal.enName}" />${(page.pageNo-1)*page.pageSize+idx.count }</td>
					<td>${journal.id}</td>
					<td><s:property value="#journal.issn" /></td>
					<td>
						<c:if test="${!empty journal.enName }">
						期刊英文名：<s:property value="#journal.enName" /><br/>
						</c:if>
						<c:if test="${!empty journal.zhName }">
						期刊中文名：<s:property value="#journal.zhName" />
						</c:if>
					</td>
					<td><s:property value="#journal.fromFlag" /></td>
				</tr>
			</s:iterator>
	</table>
	</s:if><s:else>
	   <div class="curved">
		<b class="b1"></b><b class="b2"></b><b class="b3"></b><b class="b4"></b>
		<div class="roundTable">
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td >
		           <s:text name="没有找到相应的记录！">
		           </s:text>
				</td>
			  </tr>
			</table>
		</div>
		<b class="b4"></b><b class="b3"></b><b class="b2"></b><b class="b1"></b>
	   </div>
	</s:else>
</s:form>
</div>
</body>
</html>
