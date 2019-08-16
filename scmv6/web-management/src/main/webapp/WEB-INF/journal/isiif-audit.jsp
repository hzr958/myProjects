<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审核期刊ISI影响因子</title>
<script type="text/javascript" src="${resmod}/js/UIpublic.js"></script>
<script type="text/javascript" src="${resmod}/js/thickbox-compressed.js"></script>
<link href="${resmod}/css/thickbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" >
function findTempCheck(url){
	if(!isLong($.trim($("#pissn").val()))){
		alert("请输入纯数字！");
		return;
	}
	$("#mainForm").attr('action', "${ctx}/journal/"+url); 
	$("#mainForm").submit();
}

function isLong(obj){
	var reg = /^[0-9]{0,12}$/;
	if(reg.test(obj)){
		return true;
	}else{
		return false;
	}
}

function checkedTemp(thiss){
	if(thiss.checked){
		var ids = $("#jnlId").val();
		ids +=","+thiss.value;
	   $("#jnlId").attr("value",ids);
	}
}

function manpass(temp){
	if(!isCheckRadio()){
		alert("请选择要操作的记录！");
		return;
	}
	disableAllButton(true);
	$("#wait").html("执行中，请稍后.....");
	$("#wait").css("color","red");
	var jnlIds = $("#jnlId").val();
	jnlIds = jnlIds.substr(1);
	$.ajax( {
		url : '${ctx}/journal/pass',
		type : 'post',
		dataType:'json',
		data : {'jnlIds':jnlIds,'temp':temp},
		success : function(data) {
			if(data){
				//$("#wait").html("");
				//disableAllButton(false);
				alert("操作成功！");
				window.location.reload(); 
			}
			else{
				$("#wait").html("");
				disableAllButton(false);
				alert("操作失败！");
			}
		}
	});
}

function manrefuse(temp){
	if(!isCheckRadio()){
		alert("请选择要操作的记录！");
		return;
	}
	if(!confirm("确认要拒绝此记录吗？")){
		return;
	}
	var checkId = $("#jnlId").val();
	 disableAllButton(true);
	 $("#wait").html("执行中，请稍后.....");
	 $("#wait").css("color","red");
	$.ajax( {
		url : '${ctx}/journal/refuse',
		type : 'post',
		dataType:'json',
		data : {'checkId':checkId,'temp':temp},
		success : function(data) {
			if(data){
			    //$("#wait").html("");
				//disableAllButton(false);
				alert("操作成功！");
				window.location.reload(); 
			}
			else{
				$("#wait").html("");
				disableAllButton(false);
				alert("操作失败！"); 
			}
		}
	});
}

function disableAllButton(status){   
    var   inputObjs=document.getElementsByTagName("input");   
    for(i=0;i<inputObjs.length;i++)   {   
		if(inputObjs[i].type == "button" )   {   
			inputObjs[i].disabled=status;   
		}   
	}   
}   
function isCheckRadio(){
    var allCheck=false;
    $('input[name*=checkId]').each(function (){
	    if($(this).attr('checked')){
	      allCheck=true;
	    }
    });
    return allCheck;
}

$(document).ready(function(){
	$("#mainForm").submit(function(){
		$("#pissn").val($.trim($("#pissn").val()));
    });
});
</script>
</head>
<body>
<div id="main">
<div id="wait"></div>
<div class="errorMessage"></div>
<div class="successMessage"></div>
<div class="message"  style="display: none"><s:actionmessage/></div>
<s:form id="mainForm" name="mainForm" method="post"  action="" theme="simple">
<input name=jnlId id="jnlId" type="hidden" value=""/>
	<div class="functionTitle"><img src="${resmod}/images/arrow.gif" />
			审核期刊ISI影响因子
	</div>
	<div class="search_div">
		<table border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td>期刊临时表ID：
				<s:textfield id="pissn" name="check.tempIsiIfId" maxlength="12"  />
			</td>
			<td>状态：<s:select list="#{0:'未审核',1:'已通过',2:'已拒绝'}" name="check.status" headerKey="0" listKey="key" listValue="value" /></td>
			<td><input type="button" onclick="findTempCheck('auIsIif');" class="search_button" value="查询"  /></td>
		  </tr>
		</table>
	</div>
	<br /><br />
	<s:if test="page.pageCount>0">
	<%@ include file="/common/page-tages.jsp"%>
	<table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
		<tr>
			<th width="50">序号</th>
			<th>TEMP_ISI_IF_ID</th>
			<th>期刊名</th>
			<th>PISSN</th>
			<th>来源</th>
			<th>修改状态</th>
			<th>审核状态</th>
		</tr>
		<s:iterator value="page.result" var="check" status="idx">
			<tr align="left">
				<td>${(page.pageNo-1)*page.pageSize+idx.count}&nbsp;
				<s:if test="#check.status==0">
					<input type="checkbox" name="checkId" value="<s:property value="#check.tempCheckId" />" onclick="checkedTemp(this)"/> 
				</s:if><s:else>
					<input type="checkbox" disabled="disabled" name="checkId" value="<s:property value="#check.tempCheckId" />"/> 
				</s:else>
				
				</td>
				<td><s:property value="#check.tempIsiIfId" /></td>
				<td>
					期刊英文名：<s:property value="#check.titleEn" /><br/>
					期刊中文名：<s:property value="#check.titleXx" />
				</td>
				<td width="60px"><s:property value="#check.pissn" /></td>
				<td><s:property value="#check.dbCode" /></td>
		    	<td>
		    	<s:if test="#check.handleMethod==1">
		    		更新至期刊<br/>title表ID(<s:property value="#check.tuttiJouId" />)
		    	</s:if>
		    	<s:if test="#check.handleMethod==3">
		    		保留原样
		    	</s:if>
		    	</td> 
		    	<td>
		    	 <div id="auditStstus${check.tempCheckId}">
			    	<s:if test="#check.status==0">
			    	     未审核
			    	</s:if>
			    	<s:if test="#check.status==1">
			    	     已通过
			    	</s:if>
			    	<s:if test="#check.status==2">
			    	     已拒绝
			    	</s:if>
			    </div>
		    	</td>
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
	<br/>
	<div class="but_div">
		<input type="button"  value="通过" onclick="manpass('isiIf');" class="button" />&nbsp;
		<input type="button" value="拒绝" onclick="manrefuse('isiIf');" class="button" />
	</div>
</s:form>
</div>
</body>
</html>
