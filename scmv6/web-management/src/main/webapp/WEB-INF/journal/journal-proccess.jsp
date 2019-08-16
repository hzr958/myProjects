<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>处理期刊批量和手工导入的临时数据</title>
<script type="text/javascript" src="${resmod}/js/UIpublic.js"></script>
<script type="text/javascript" src="${resmod}/js/thickbox-compressed.js"></script>
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/journal/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<script src="${resmod}/js/journal/navMenu.js" type="text/javascript" ></script>
<link href="${resmod}/css/thickbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" >

$(document).ready(function() {
	var type='${temp}';
	if("handJournal"==type){
		var fromFlag='${haveDB}';
		if(fromFlag=='false'){
			  document.getElementById("insert").disabled='disabled';
	    }
	}
});
function checkedJnl(thiss){
	if(thiss.checked){
	   $("#jnlId").attr("value",thiss.value);
	}
}
//add by liuwei
function errorPissn(){
	var type = '${temp}';
	var pissn="";
	if("batch"==type){
		pissn = $.trim($("#batchPissn").val());
	}else if("manual"==type){
		pissn = $.trim($("#manualPissn").val());
	}else if("handJournal"==type){
		pissn = $.trim($("#handJournalIssn").val());
	}
	if(pissn==''){
		return true;
	}
	if("****_****"==pissn || "****-****"==pissn)
		return false;
	var reg = /^[0-9]{4}-[0-9A-Za-z]{4}$/;
	if(!reg.test(pissn)){
		return true;
	}
	return false;
	
}
function toCheckOriginally(){	
	if(confirm('确认保留原样？')){
	var data;
	if('${isBatchOrManual}'==0){
		data = {"jnlId":$("#jnlId").val(),"temp":$("#temp").val(),"batch.tempBatchId":$("input[name='batch.tempBatchId']").val(),"batch.pissn":$("#batchPissn").val(),"batch.eissn":$("#batchEissn").val(),"batch.titleXx":$("#batchTitleXx").val(),"batch.titleEn":$("#batchTitleEn").val(),"batch.dbId":$("input[name='batch.dbId']").val()};
	}else if('${isBatchOrManual}'==2){
		data = {"jnlId":$("#jnlId").val(),"temp":$("#temp").val(),"handJournal.id":$("input[name='handJournal.id']").val(),"handJournal.issn":$("#handJournalIssn").val(),"handJournal.zhName":$("#handJournalZhName").val(),"handJournal.enName":$("#handJournalEnName").val(),"serviceNodeId":$("#node").val()};
	}
	$.ajax({
		url : '/scmmanagement/journal/toCheck',
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			if(!data){
				alert("操作失败！");
			}else{
			  alert("操作成功！");
			  back();
			}
		}
	});
	}
} 

function toCheckInsertJnl(){
	//add by liuwei 增加pissn格式校验
	if(errorPissn()){
		alert("Pissn格式有错误。正确格式为：4个数字-4个数字或字母。如：1234-r6ty");
		return;
	}
	if(confirm("确定新增至期刊？")){
	var data;
	if('${isBatchOrManual}'==0){
		data = {"jnlId":$("#jnlId").val(),"temp":$("#temp").val(),"batch.tempBatchId":$("input[name='batch.tempBatchId']").val(),"batch.pissn":$("#batchPissn").val(),"batch.eissn":$("#batchEissn").val(),"batch.titleXx":$("#batchTitleXx").val(),"batch.titleEn":$("#batchTitleEn").val(),"batch.dbId":$("input[name='batch.dbId']").val()};
	}else if('${isBatchOrManual}'==2){
		data = {"jnlId":$("#jnlId").val(),"temp":$("#temp").val(),"handJournal.id":$("input[name='handJournal.id']").val(),"handJournal.issn":$("#handJournalIssn").val(),"handJournal.zhName":$("#handJournalZhName").val(),"handJournal.enName":$("#handJournalEnName").val(),"serviceNodeId":$("#node").val()};
	}
	//alert('issn:'+$("#handJournalIssn").val()+'  ENTITLE:'+$("#handJournalEnName").val()+'  CNTITLE:'+$("#handJournalZhName").val());
	$.ajax({
		url : "${ctx}/journal/toCheckAddJournal",
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			if(!data){
				alert("操作失败！");
			}else{
				alert("新增期刊成功！");
				back();
			}
			
		}
	});
	}
} 

function toCheckUpdateJnl(){
	//add by liuwei 增加pissn格式校验
  if(errorPissn()){
		alert("Pissn格式有错误。正确格式为：4个数字-4个数字或字母。如：1234-r6ty");
		return;
	}
	if($("#jnlId").val()==""){
		alert("请选择要更新的期刊！");
		return;
	}
	if(confirm("确定更新/匹配至选中期刊？")){
	var data;
	if('${isBatchOrManual}'==0){
		data = {"jnlId":$("#jnlId").val(),"temp":$("#temp").val(),"batch.tempBatchId":$("input[name='batch.tempBatchId']").val(),"batch.pissn":$("#batchPissn").val(),"batch.eissn":$("#batchEissn").val(),"batch.titleXx":$("#batchTitleXx").val(),"batch.titleEn":$("#batchTitleEn").val(),"batch.dbId":$("input[name='batch.dbId']").val()};
	}else if('${isBatchOrManual}'==2){
		data = {"jnlId":$("#jnlId").val(),"temp":$("#temp").val(),"handJournal.id":$("input[name='handJournal.id']").val(),"handJournal.issn":$("#handJournalIssn").val(),"handJournal.zhName":$("#handJournalZhName").val(),"handJournal.enName":$("#handJournalEnName").val(),"serviceNodeId":$("#node").val()};
	}
	$.ajax({
		url : "${ctx}/journal/toCheckUpdatejournal",
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			if(data.result=='error'){
				alert(data.mesg);
			}else{
			    alert("操作成功！");
			  	back();
			}
		}
	});
	}
} 

function back(){
	if('${isBatchOrManual}'==0){
		$("#mainFormChain").attr('action', "${ctx}/journal/getBatchImportCheckData"); 
	}else if('${isBatchOrManual}'==2){
		$("#mainFormChain").attr('action', "${ctx}/journal/handBatch");
	}
	$("#mainFormChain").submit();
}

</script>
</head>
<body id="main_body">
  <div id="container">
    <div id="nav">
      <ul id="main_nav">
        <li id="navItem2" class="highlight"><div class="nav_bluebtn_left"></div>
          <div class="nav_bluebtn_bg">
            <span class="menu_a_default">期刊管理</span>
            <ul>
              <li id="navItem2_SubNavItem0" class="highlight" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)"><a href="${ctx }/journal/getBatchImportCheckData"
                class="arrow_more_gray"> 期刊审核</a>
                <ul id="navItem2_SubNavItem0_SubNav" style="display: none">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getBatchImportCheckData" class="menu_a_default"> 批量导入</a></li>
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/auIsIif" class="menu_a_default"> ISI影响因子</a></li>
                </ul></li>
              <li><a href="${ctx }/journal/manager" class="menu_a_default"> 修改/合并</a></li>
              <li id="navItem2_SubNavItem2" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)" ><a href="${ctx }/journal/importBatch"
                class="arrow_more_gray"> 期刊导入</a>
                <ul id="navItem2_SubNavItem2_SubNav" style="visibility: hidden; z-index: 999; display: none;">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/importBatch" class="menu_a_default"> 批量导入</a></li>
                </ul></li>
            </ul>
          </div>
          <div class="nav_bluebtn_right"></div></li>
      </ul>
    </div>
    <div class="nav_bluebtn_right"></div>
    <div class="box_nav3tab">
      <div class="nav4_div_hover">
        <ul>
          <li><a href="${ctx }/journal/importBatch"><b class="left"></b><b class="middle">批量导入</b><b
              class="right"></b></a></li>
        </ul>
      </div>
    </div>
<div id="main">
<div class="errorMessage"></div>
<div class="successMessage"></div>
<div class="message"  style="display: none"><s:actionmessage/></div>
<s:form id="mainFormChain" name="mainFormChain" method="post"  action="" theme="simple">
	<div>
		<c:if test="${ isBatchOrManual==0}">
			<s:hidden name="batch.titleXx" value="%{#session.batch.titleXx}"/>
			<s:hidden  name="batch.pissn" value="%{#session.batch.pissn}"  />
			<s:hidden  name="batch.throwsCause" value="%{#session.batch.throwsCause}" />
		</c:if>
		<s:hidden  name="page.pageNo" value="%{#session.page.pageNo}"/>
		<s:hidden  name="page.pageSize" value="%{#session.page.pageSize}"/>
	</div>
</s:form>
<s:form id="mainForm" name="mainForm" method="post"  action="" theme="simple">
<input name=jnlId id="jnlId" type="hidden" value=""/>
<input name=temp id="temp" type="hidden" value="${temp}"/>
	<div class="but_div">
	<input type="button"  value="保留原样" onclick="toCheckOriginally();" class="button" />&nbsp;
	<input type="button" id="insert"  value="新增期刊" onclick="toCheckInsertJnl();" class="button" />&nbsp;
	<c:if test="${isBatchOrManual==0}">
	<input type="button" value="更新至选中期刊" onclick="toCheckUpdateJnl();" class="button" />&nbsp;
	</c:if>
	<c:if test="${isBatchOrManual==2}">
	<input type="button" value="匹配至选中期刊" onclick="toCheckUpdateJnl();" class="button" />&nbsp;
	</c:if>
	<input type="button" value="返回" onclick="back()" class="button" />

	</div>
	<br /><br />
	<c:if test="${!empty check}">
		<div><strong>拒绝数据</strong></div>
		<table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
			<tr align="left">
				<th width="6">temp_check_id</th>
				<th width="110">PISSN</th>
				<th>期刊名</th>
				<th>来源</th>
			</tr>
			<tr align="left">
				<td>${check.tempCheckId}</td>
				<td>
					PISSN:${check.pissn}<br/>
					EISSN:${check.eissn}	
				</td>
				<td>
					期刊中文名：${check.titleXx}<br/>
					期刊英文名：${check.titleEn}
				</td>
				<td>${check.dbCode}</td>
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
		<s:if test="temp.equals('batch')">
		<tr align="left">
			<td>${batch.tempBatchId}</td>
			<td>
				<input name="batch.tempBatchId" type="hidden" value="<s:property value="batch.tempBatchId" />"/>
				PISSN：<input style="width:80px;" id="batchPissn" name="batch.pissn" maxlength="9" value="<s:property value="batch.pissn" />" /><br/>
				EISSN：<input style="width:80px;" id="batchEissn" name="batch.eissn" maxlength="9" value="<s:property value="batch.eissn" />" />
			</td>
			<td>
				期刊英文名：<input style="width:500px;" id="batchTitleEn" name="batch.titleEn" maxlength="250" value="<s:property value="batch.titleEn" />"/><br/>
				期刊中文名：<input style="width:500px;" id="batchTitleXx" name="batch.titleXx" maxlength="250" value="<s:property value="batch.titleXx" />" />
		    </td>
			<td><s:property value="batch.dbCode" /><input name="batch.dbId" type="hidden" value="<s:property value="batch.dbId" />"/></td>
		</tr>
		</s:if>
		</table>
	<br/><strong>基础期刊数据</strong>
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
				<td>${idx.count}&nbsp;
				<s:if test="temp.equals('handJournal')">
					<input type="radio" name="tempIds" onclick="checkedJnl(this)" value="<s:property value='#jnlTitle.jnlId' />"  />
				</s:if>
				<s:else>
					<input type="radio" name="tempIds" onclick="checkedJnl(this)" value="<s:property value='#jnlTitle.jouTitleId' />"  />
				</s:else>
				</td>
				<td><s:property value="#jnlTitle.jnlId" /></td>
				<td>
					PISSN：<s:property value="#jnlTitle.pissn" /><br/>
					EISSN：<s:property value="#jnlTitle.eissn" />
				</td>
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
