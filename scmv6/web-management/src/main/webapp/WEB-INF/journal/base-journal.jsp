<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name='rolPersonAdd.config' /></title>
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/journal/public.css" rel="stylesheet" type="text/css" />
<script src="${resmod}/js/journal/navMenu.js" type="text/javascript"></script>
<script type="text/javascript">
  function updateJnl() {
    //add by liuwei 增加pissn格式校验
    if (errorPissn()) {
      alert("Pissn格式有错误。正确格式为：4个数字-4个数字或字母。如：1234-r6ty");
      return;
    }
    $("#mainForm").attr('action', "${ctx}/journal/mergeUpdate");
    $.ajax({
      url:"/scmmanagement/journal/mergeUpdate",
      type:"post",
      data: $("#mainForm").serialize(),
      dataType:"json",
      success:function(data){
        alert("更新成功");
        window.location.reload();
      }
    });
  }

  //add by liuwei
  function errorPissn() {
    var jouPissn = $("#jouPissn").val();
    var jouTitPis = $("input[name='titlePssn']");
    var reg = /^[0-9]{4}-[0-9A-Za-z]{4}$/;
    if ("****_****" == jouPissn || "****-****" == jouPissn)
      return false;
    if (!reg.test(jouPissn)) {
      return true;
    }
    for (index = 0; index < jouTitPis.length; index++) {
      if (!reg.test(jouTitPis[index].value)) {
        return true;
      }
    }
    return false;
  }

  function delBaseJnlTitle(id) {
    if (!confirm("确认要删除此条基础期刊的title表中的数据吗？")) {
      return;
    }
    $.ajax({
       url: "/scmmanagement/journal/confirmDelJournalTitle",
       type: "post",
       data: {"id":id},
       dataType:"json",
       success:function(data){
         alert("删除成功");
         window.location.reload();
       }
    });
  }

  function back() {

      window.history.go(-1);
    
    //$("#mainFormChain").attr("action","${ctx}/journal/manager");
    //$("#mainFormChain").submit();

  }

</script>
</head>
<body id="main_body">
  <s:form id="mainFormChain" name="mainFormChain" method="post" action="" theme="simple">
    <div>
      <s:hidden name="journal.titleXx" value="%{#session.journal.titleXx}" />
      <s:hidden name="journal.pissn" value="%{#session.journal.pissn}" />
      <s:hidden name="journal.jnlId" value="%{#session.journal.jnlId}" />
      <s:hidden name="page.pageNo" value="%{#session.page.pageNo}" />
    </div>
  </s:form>
  <s:form id="mainForm" name="mainForm" action="ins-person-add!next.action" method="post" theme="simple">
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
        <div class="surebox">
          <div class="">
            <strong><font color="#0072E3">表名：BASE_JOURNAL</font></strong>
          </div>
          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="simple_table">
            <tr>
              <th>JNLID</th>
              <th>PISSN</th>
              <th>EISSN</th>
              <th>期刊英文名<font color=red>（公开显示的名称）</font></th>
              <th>期刊中文名<font color=red>（公开显示的名称）</font></th>
            </tr>
            <tr>
              <td align="center">${journal.jnlId}<input name="journal.jnlId" type="hidden"
                value="${journal.jnlId}" /></td>
              <td><input name="journal.pissn" maxlength="9" id="jouPissn" value="${journal.pissn}"
                style="width: 80px;" /></td>
              <td><input name="journal.eissn" maxlength="9" id="jouEissn" value="${journal.eissn}"
                style="width: 80px;" /></td>
              <td><s:textfield name="journal.titleEn" value="%{journal.titleEn}" style="width:310px;"
                  maxlength="250"></s:textfield></td>
              <td><s:textfield name="journal.titleXx" value="%{journal.titleXx}" style="width:310px;"
                  maxlength="250"></s:textfield></td>
            </tr>
          </table>
        </div>
        <br />
        <div class="surebox">
          <div class="">
            <strong><font color="#0072E3">表名：BASE_JOURNAL_TITLE</font></strong>
          </div>
          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="simple_table">
            <tr>
              <th width="30">序号</th>
              <th>JNLID</th>
              <th width="70">PISSN</th>
              <th width="70">EISSN</th>
              <th>期刊英文名</th>
              <th>期刊中文名</th>
              <th>来源</th>
              <th width="40">操作</th>
            </tr>
            <s:iterator value="baseJournalTitleList" id="tle" status="idx">
              <tr>
                <td>${idx.count}</td>
                <td>${tle.jnlId}</td>
                <td>${tle.pissn}</td>
                <td>${tle.eissn}</td>
                <td>${tle.titleEn}</td>
                <td>${tle.titleXx}</td>
                <td>${tle.dbCode}</td>
                <td><s:if test="baseJournalTitleList.size()>1">
                    <a href="javascript:void(0)" onclick="delBaseJnlTitle('${tle.jouTitleId}');">删除</a>
                  </s:if>
                  <s:else>&nbsp;</s:else></td>
              </tr>
            </s:iterator>
          </table>
        </div>
        <br />
        <div class="but_div">
          &nbsp;&nbsp;&nbsp;&nbsp; <input type="button" onclick="updateJnl();" value="更新" class="button" />&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="button" onclick="javaScript:window.location.reload();" value="保持原样" class="button" />&nbsp;&nbsp;&nbsp;&nbsp; <input
            type="button" onclick="javaScript:back();" value="返回" class="button" />&nbsp;
        </div>
      </div>
  </s:form>
</body>
</html>
