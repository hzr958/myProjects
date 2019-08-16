<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<script type="text/javascript">
//定义环境变量
var appContextPath ="${resmod}";
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理基础期刊数据</title>
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/journal/public.css" rel="stylesheet" type="text/css" />
<script src="${resmod}/js/journal/navMenu.js" type="text/javascript" ></script>
<script type="text/javascript" src="${resmod}/js/journal/thickbox-compressed.js"></script>
<link href="${resmod}/css/journal/thickbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function findJournal(){
	$(".throwType").each(function(){
		$(this).attr("checked",false);
    });
	$("#mainForm").attr('action', "${ctx}/journal/manager"); 
	$("#mainForm").submit();
}

//注意：如果再增加新表须在以添加提示以免删除数据时发生异常
function delJnl(jnlId){
	//基础期刊相关属性表
	var temp1 = "\r\nBASE_JOURNAL\r\nBASE_JOURNAL_CATEGORY\r\nBASE_JOURNAL_CATEGORY_RANK\r\nBASE_JOURNAL_DB\r\nBASE_JOURNAL_ISI_IF\r\nBASE_JOURNAL_PUBLISHER\r\nBASE_JOURNAL_TITLE";
	//期刊相关临时表
	var temp2 = "\r\nBASE_JOURNAL_TEMP_BATCH\r\nBASE_JOURNAL_TEMP_ISI_IF\r\n";
	var temp3="\r\n清空JOURNAL表匹配的基础期刊Id字段";
	if(!confirm("确认要删除此条期刊记录和以下相关表数据吗？"+temp1+temp2+temp3)){
		return;
	}
	$("#jnlId").attr("value",jnlId);
	$("#mainForm").attr('action', "${ctx}/journal/manDel"); 
	$("#mainForm").submit();
}

function showTutti(jnlId){
	$("#jnlId").attr("value",jnlId);
	$("#showDiv").click();
}

function tuttiJnl(){
	var tuttiJnlId=$("#tuttiJnlId").val();
	if(tuttiJnlId==""){
		alert("合并期刊ID不能为空！");
	}else{
		$.ajax({
			url:"${ctx}/journal/mergeJournal",
			type:"post",
			data:{"tuId":tuttiJnlId,"jnlId":$("#jnlId").val()},
			timeout:10000,
			success:function(data){
				tb_remove();
				if(data.result==1){
					 alert("合并成功");
				   setTimeout(function(){
						location.href="${ctx}/journal/manager";
					},1000);
				}else if(data.result==3){
					alert("找不到期刊ID对应的期刊");
				}else{
				  alert("合并失败");
				}
			},error:function(){
				tb_remove();
				alert("合并失败");
			}
		});
	}
}

function findJnl(jnlId){
	$("#jnlId").attr("value",jnlId);
	//$("#mainForm").attr('action', "${ctx}/journal/findOrUpdate"); 
	//$("#mainForm").submit();
	location.href="/scmmanagement/journal/findOrUpdate?jnlId="+jnlId;
}
//add by liuwei
$(document).ready(function(){
	var temp = '${temp}';
	if(temp!="" && temp!=null){
		alert(temp);
	}	
	$("#mainForm").submit(function(){ 
		$("#jnlId").val($.trim($("#jnlId").val()));
		if($("#throwsType").val()==''){
			$(".throwType").each(function(){
				if($(this).attr("checked")){
					$("#throwsType").val($(this).val());
				}
		    });
		}
    });
	$("#throwType1").click(function(){
		if($(this).attr("checked")){
			$("#throwsType").val($(this).val());
			$("#mainForm").submit();
		}
		$("#throwType2").attr("checked",false);
    });
	$("#throwType2").click(function(){
		if($(this).attr("checked")){
			$("#throwsType").val($(this).val());
			$("#mainForm").submit();
		}
		$("#throwType1").attr("checked",false);
    });
    $("input[name=throwType][value='${journal.throwsType}']").attr("checked","checked");	
});
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
              <li id="navItem2_SubNavItem0" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)"><a href="${ctx }/journal/getBatchImportCheckData"
                class="arrow_more_gray"> 期刊审核</a>
                <ul id="navItem2_SubNavItem0_SubNav" style="display: none">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getBatchImportCheckData" class="menu_a_default"> 批量导入</a></li>
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getIsIBatchImportCheckData" class="menu_a_default"> ISI影响因子</a></li>
                </ul></li>
              <li class="highlight"><a href="${ctx }/journal/manager" class="menu_a_default"> 修改/合并</a></li>
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
      <div class="message" style="display: none">
        <s:actionmessage />
      </div>
      <form id="mainForm" name="mainForm" method="get" action="${ctx}/journal/manager" theme="simple">
        <!-- add by liuwei 将page记录数也传过去！ -->
        <input name="page.totalCount" type="hidden" value="${page.totalCount }" /> <input name=jnlId id="jnlId"
          type="hidden" value="" /> <input name="journal.throwsType" id="throwsType" type="hidden" />
        <div class="functionTitle">
          <img src="${resmod}/images/arrow.gif" /> 管理基础期刊数据
        </div>
        <div class="search_div">
          <table border="0" cellspacing="5px" cellpadding="0">
            <tr>
              <td>期刊标题：<s:textfield name="journal.titleXx" id="jouTitle" maxlength="100" cssStyle="width:400px;" /></td>
              <td>PISSN：<s:textfield id="pissn" name="journal.pissn" maxlength="50" /></td>
              <td>期刊ID：<s:textfield id="jnlId" name="journal.jnlId" maxlength="50" /></td>
              <td><input id="find" type="button" onclick="findJournal();" class="search_button" value="查询" /></td>
            </tr>
          </table>
        </div>
        <br /> 注意：&nbsp;&nbsp;<input type="checkbox" name="throwType" class="throwType" id="throwType1" value="1" /><img
          src="${resmod}/images/journal/ico_focus.gif" />表示：该期刊有多个title，注意设置需要公开的title。&nbsp; <input type="checkbox"
          name="throwType" id="throwType2" class="throwType" value="2" /><img
          src="${resmod}/images/journal/group_open_prj.gif"></img>表示：该期刊与title表中pissn不一至，注意需要修正。<br />
        <br />
        <s:if test="page.pageCount>0">
          <%@ include file="/common/page-tages.jsp"%>
          <table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
            <tr>
              <th width="50">序号</th>
              <th width="100">期刊ID</th>
              <th>期刊名</th>
              <th width="80">PISSN</th>
              <th width="80">EISSN</th>
              <th width="140">操作</th>
            </tr>
            <s:iterator value="page.result" id="jou" status="idx">
              <tr align="left">
                <td>${(page.pageNo-1)*page.pageSize+idx.count }&nbsp;<s:if test="#jou.isNewTitle==true">
                    <img src="${resmod}/images/journal/ico_focus.gif" title="该条基础期刊title有未处理的title"></img>
                  </s:if><br /> <s:if test="#jou.isNewPissn==true">&nbsp;&nbsp;&nbsp;&nbsp;<img
                      src="${resmod}/images/journal/group_open_prj.gif" title="该条基础期刊PISSN和title表不一至"></img>
                  </s:if>
                </td>
                <td><s:property value="#jou.jnlId" /></td>
                <td>期刊英文名：<s:property value="#jou.titleEn" /><br /> 期刊中文名：<s:property value="#jou.titleXx" />
                </td>
                <td><s:property value="#jou.pissn" /></td>
                <td><s:property value="#jou.eissn" /></td>
                <td align="center"><a style="color:blue" onclick="showTutti(${jou.jnlId});">合并</a>&nbsp;&nbsp;&nbsp;<a
                style="color:blue"  onclick="findJnl(${jou.jnlId})">查看/更新</a>&nbsp;&nbsp;&nbsp;<a style="color:blue" onclick="delJnl(${jou.jnlId })">删除</a></td>
              </tr>
            </s:iterator>
          </table>
        </s:if>
        <s:else>
          <div class="curved">
            <b class="b1"></b><b class="b2"></b><b class="b3"></b><b class="b4"></b>
            <div class="roundTable">
              <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><s:text name="没有找到相应的记录！">
                    </s:text></td>
                </tr>
              </table>
            </div>
            <b class="b4"></b><b class="b3"></b><b class="b2"></b><b class="b1"></b>
          </div>
        </s:else>
        <input type="button" id="showDiv" style="display: none;" value="期刊合并" class="button thickbox"
          alt="#TB_inline?height=128&width=350&inlineId=myOnPageContent" title="期刊合并" />
        <div id="myOnPageContent" style="display: none;" style="width:100%;overflow-x:auto;overflow-y:hidden;">
          <table width="100%" border="0" cellspacing="12" cellpadding="8">
            <tr align="center">
              <td><b class="red">*</b>合并至期刊ID：<input id="tuttiJnlId" name="tuttiJnlId" maxlength="10" type="text"
                onkeyup="this.value=this.value.replace(/\D/g,'')" /></td>
            </tr>
            <tr align="center">
              <td valign="top"><input type="button" onclick="tuttiJnl();" value="合并" class="button" />
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="button" value="取消" class="button close_div" /></td>
            </tr>
          </table>
        </div>
      </form>
    </div>
</body>
</html>
