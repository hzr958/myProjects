<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<c:if test="${ isBatchOrManual==0}">
  <title>审核批量导入基准数据</title>
</c:if>
<c:if test="${ isBatchOrManual==1}">
  <title>审核手工导入基准数据</title>
</c:if>
<script type="text/javascript" src="${resmod}/js/journal/UIpublic.js"></script>
<script type="text/javascript" src="${resmod}/js/journal/thickbox-compressed.js"></script>
<script type="text/javascript" src="${resmod}/js/journal/navMenu.js" ></script>
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/journal/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/journal/thickbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function findTempCheck(url) {
  if (!isLong($.trim($("#pissn").val()))) {
    alert("请输入纯数字！");
    return;
  }
  $("#mainForm").attr('action', url);
  $("#mainForm").submit();
}

function isLong(obj) {
  var reg = /^[0-9]{0,12}$/;
  if (reg.test(obj)) {
    return true;
  } else {
    return false;
  }
}

function checkedTemp(thiss) {
  if (thiss.checked) {
    $("#jnlId").attr("value", thiss.value);
  }
}
function checkboxAll() {
  var ids = "";
  $("input[name='checkbox']").each(function() {
    if ($(this).attr('checked')) {
      ids += $(this).val() + ",";
    }
  });
  $("#jnlId").attr("value", ids);
}

function manpass(temp) {
  if (!isCheckRadio()) {
    alert("请选择要操作的记录！");
    return;
  }
  checkboxAll();
  var jnlIds = $("#jnlId").val();
  if (jnlIds == '')
    return;
  disableAllButton(true);
  $("#wait").html("执行中，请稍后.....");
  $("#wait").css("color", "red");
  $.ajax({
    url : '${ctx}/journal/auditPassed',
    type : 'post',
    dataType : 'json',
    data : {
      'jnlIds' : jnlIds,
      'temp' : temp
    },
    success : function(data) {
      if (data) {
        alert("操作成功！");
        window.location.reload();
      } else {
        disableAllButton(false);
        $("#wait").html("");
        alert("操作失败！");
      }
    }
  });
}

function manrefuse(temp) {
  if (!isCheckRadio()) {
    alert("请选择要操作的记录！");
    return;
  }
  if (!confirm("确认要拒绝此记录吗？")) {
    return;
  }
  checkboxAll();
  var jnlIds = $("#jnlId").val();
  if (jnlIds == '')
    return;
  disableAllButton(true);
  $("#wait").html("执行中，请稍后.....");
  $("#wait").css("color", "red");
  $.ajax({
    url : '${ctx}/journal/auditRefuse',
    type : 'post',
    dataType : 'json',
    data : {
      'jnlIds' : jnlIds,
      'temp' : temp
    },
    success : function(data) {
      if (data) {
        alert("操作成功！");
        window.location.reload();
      } else {
        disableAllButton(false);
        $("#wait").html("");
        alert("操作失败！");
      }
    }
  });
}

function disableAllButton(status) {
  var inputObjs = document.getElementsByTagName("input");
  for (i = 0; i < inputObjs.length; i++) {
    if (inputObjs[i].type == "button") {
      inputObjs[i].disabled = status;
    }
  }
}

function isCheckRadio() {
  var allCheck = false;
  $('input[name*=checkbox]').each(function() {
    if ($(this).attr('checked')) {
      allCheck = true;
    }
  });
  return allCheck;
}

function changeTitleLang() {

  if ($("#titleType").val() == 'en')
    $("#titletext").attr("name", "check.titleEn");
  else
    $("#titletext").attr("name", "check.titleXx");
}

function changeIssnType() {

  if ($("#issnType").val() == 'pissn')
    $("#issntext").attr("name", "check.pissn");
  else
    $("#issntext").attr("name", "check.eissn");
}
/* $(document).ready(function() {
$("#mainForm").submit(function() {
  $("#pissn").val($.trim($("#pissn").val()));
});
}); */
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
                onmouseout="navClearLevel3Nav(this.id)" class="highlight"><a href="${ctx }/journal/getAuditData" class="arrow_more_gray">
                  期刊审核</a>
                <ul id="navItem2_SubNavItem0_SubNav" style="display: none">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getAuditData" class="menu_a_default"> 批量导入</a></li>
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/auIsIif" class="menu_a_default"> ISI影响因子</a></li>
                </ul></li>
              <li><a href="${ctx }/journal/manager" class="menu_a_default"> 修改/合并</a></li>
              <li id="navItem2_SubNavItem2" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)"><a
                href="${ctx }/journal/importBatch" class="arrow_more_gray"> 期刊导入</a>
                <ul id="navItem2_SubNavItem2_SubNav" style="visibility: hidden; z-index: 999; display: none;">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/impBatch" class="menu_a_default"> 批量导入</a></li>
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
          <li><a href="${ctx }/journal/auBatch"><b class="left"></b><b
              class="middle">批量导入</b><b class="right"></b></a></li>
        </ul>
      </div>
      <div class="nav4_div_on">
        <ul>
          <li><a href="${ctx }/journal/auIsIif"><b class="left"></b><b
              class="middle">ISI影响因子</b><b class="right"></b></a></li>
        </ul>
      </div>
    </div>
    <div id="main">
      <div id="wait"></div>
      <div class="errorMessage"></div>
      <div class="successMessage"></div>
      <div class="message" style="display: none">
        <s:actionmessage />
      </div>
      <s:form id="mainForm" name="mainForm" method="post" action="" theme="simple">
        <input name=jnlId id="jnlId" type="hidden" value="" />
        <div class="functionTitle">
          <img src="${resmod}/images/arrow.gif" />
          <c:if test="${ isBatchOrManual==0}">
			审核批量导入基准数据
		</c:if>
          <c:if test="${ isBatchOrManual==1}">
			审核手工导入基准数据
		</c:if>
        </div>
        <div class="search_div">
          <table border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>期刊临时表ID： <c:if test="${ isBatchOrManual==0}">
                  <s:textfield id="pissn" name="check.tempBatchId" maxlength="12" />
                </c:if> <c:if test="${ isBatchOrManual==1}">
                  <s:textfield id="pissn" name="check.tempManualId" maxlength="12" />
                </c:if>
              </td>
              <td>状态：<s:select list="#{0:'未审核',1:'已通过',2:'已拒绝'}" name="check.status" headerKey="0" listKey="key"
                  listValue="value" /></td>
              <td>&nbsp;&nbsp;标题：<s:textfield id="titletext" style="width:250px;" name="check.titleXx" /></td>
              <td>&nbsp;&nbsp;pissn：<s:textfield id="issntext" name="check.pissn" /></td>
              <c:if test="${ isBatchOrManual==0}">
                <td><input type="button" onclick="findTempCheck('getAuditData');" class="search_button" value="查询" /></td>
              </c:if>
              <c:if test="${ isBatchOrManual==1}">
                <td><input type="button" onclick="findTempCheck('auManual');" class="search_button" value="查询" /></td>
              </c:if>
            </tr>
          </table>
        </div>
        <br />
        <br />
        <s:if test="page.pageCount>0">
          <%@ include file="/common/page-tages.jsp"%>
          <table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
            <tr>
              <th width="50">序号</th>
              <th width="60">审核描述</th>
              <th width="12%">ISSN</th>
              <th>期刊名</th>
              <th width="80">来源</th>
              <th width="70">修改状态</th>
              <th width="50">审核状态</th>
            </tr>
            <s:iterator value="page.result" id="check" status="idx">
              <tr align="left"
                <c:if test="${((page.pageNo-1)*page.pageSize+idx.count)%2==0}">style="background-color:#eee;"</c:if>>
                <td rowspan="3">${(page.pageNo-1)*page.pageSize+idx.count }&nbsp;<s:if test="#check.status==0">
                    <input type="checkbox" name="checkbox" value="<s:property value="#check.tempCheckId"/>" />
                  </s:if>
                </td>
                <!-- 
				<td>
					<s:if test="#check.tempBatchId!=null"><s:property value="#check.tempBatchId" /></s:if>
					<s:if test="#check.tempManualId!=null"><s:property value="#check.tempManualId" /></s:if>
				</td>
				 -->
                <td>最终</td>
                <td><s:if test="#check.pissn != null">
				  		Pissn：<s:property value="#check.pissn" />
                    <br />
                  </s:if> <s:if test="#check.eissn != null">
				  		Eissn：<s:property value="#check.eissn" />
                    <br />
                  </s:if></td>
                <td><s:if test="#check.titleEn != null">
						期刊英文名：<s:property value="#check.titleEn" />
                    <br />
                  </s:if> <s:if test="#check.titleXx != null">
						期刊中文名：<s:property value="#check.titleXx" />
                  </s:if></td>
                <td><s:property value="#check.dbCode" /></td>
                <td rowspan="3"><s:if test="#check.handleMethod==1">
		    		更新至期刊<br />title表ID(<s:property value="#check.tuttiJouId" />)
		    	</s:if> <s:if test="#check.handleMethod==2">
		    		新增期刊
		    	</s:if> <s:if test="#check.handleMethod==3">
		    		保留原样
		    	</s:if></td>
                <td rowspan="3">
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
              <tr align="left"
                <c:if test="${((page.pageNo-1)*page.pageSize+idx.count)%2==0}">style="background-color:#eee;"</c:if>>
                <s:iterator value="baseJournalTempBatchList" var="batchList">
                  <s:if test="#batchList.tempBatchId  == #check.tempBatchId">
                    <td>原始</td>
                    <td><s:if test="#batchList.pissn != null">
						  		Pissn：<s:property value="#batchList.pissn" />
                        <br />
                      </s:if> <s:if test="#batchList.eissn != null">
						  		Eissn：<s:property value="#batchList.eissn" />
                        <br />
                      </s:if></td>
                    <td><s:if test="#batchList.titleEn != null">
						期刊英文名：<s:property value="#batchList.titleEn" />
                        <br />
                      </s:if> <s:if test="#batchList.titleXx != null">
						期刊中文名：<s:property value="#batchList.titleXx" />
                      </s:if></td>
                    <td><s:property value="#batchList.dbCode" /></td>
                  </s:if>
                </s:iterator>
              </tr>
              <tr align="left"
                <c:if test="${((page.pageNo-1)*page.pageSize+idx.count)%2==0}">style="background-color:#eee;"</c:if>>
                <s:iterator value="baseJournalTitleList" var="titleList">
                  <s:if test="#titleList.jouTitleId  == #check.tuttiJouId">
                    <td>更新到</td>
                    <td><s:if test="#titleList.pissn != null">
						  		Pissn：<s:property value="#titleList.pissn" />
                        <br />
                      </s:if> <s:if test="#titleList.eissn != null">
						  		Eissn：<s:property value="#titleList.eissn" />
                        <br />
                      </s:if></td>
                    <td><s:if test="#titleList.titleEn != null">
								期刊英文名：<s:property value="#titleList.titleEn" />
                        <br />
                      </s:if> <s:if test="#titleList.titleXx != null">
								期刊中文名：<s:property value="#titleList.titleXx" />
                      </s:if></td>
                    <td><s:property value="#titleList.dbCode" /></td>
                  </s:if>
                </s:iterator>
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
        <br />
        <div class="but_div">
          <c:if test="${ isBatchOrManual==0}">
            <input type="button" value="通过" onclick="manpass('batch');" class="button" />&nbsp;
		<input type="button" value="拒绝" onclick="manrefuse('batch');" class="button" />
          </c:if>
          <c:if test="${ isBatchOrManual==1}">
            <input type="button" value="通过" onclick="manpass('manual');" class="button" />&nbsp;
		<input type="button" value="拒绝" onclick="manrefuse('manual');" class="button" />
          </c:if>
        </div>
      </s:form>
    </div>
  </div>
</body>
</html>
