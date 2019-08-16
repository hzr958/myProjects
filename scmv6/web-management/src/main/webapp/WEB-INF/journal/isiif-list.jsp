<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期刊ISI影响因子</title>
<script type="text/javascript" src="${resmod}/js/journal/UIpublic.js"></script>
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/journal/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<script src="${resmod}/js/journal/navMenu.js" type="text/javascript"></script>
<script type="text/javascript">
function processing(temp,id){
	$("#mainForm").attr('action', "${ctx}/journal/toProcessing?temp="+temp+"&jnlId="+id); 
	$("#mainForm").submit();
}

function findTemp(url){
	$("#mainForm").attr('action', "${ctx}/journal/"+url); 
	$("#mainForm").submit();
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
              <li id="navItem2_SubNavItem0" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)" class="highlight"><a href="${ctx }/journal/getBatchImportCheckData"
                class="arrow_more_gray"> 期刊审核</a>
                <ul id="navItem2_SubNavItem0_SubNav" style="display: none">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getBatchImportCheckData" class="menu_a_default"> 批量导入</a></li>
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getIsIBatchImportCheckData" class="menu_a_default"> ISI影响因子</a></li>
                </ul></li>
              <li><a href="${ctx }/journal/manager" class="menu_a_default"> 修改/合并</a></li>
              <li id="navItem2_SubNavItem2" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)" ><a href="${ctx }/journal/importBatch"
                class="arrow_more_gray"> 期刊导入</a>
                <ul id="navItem2_SubNavItem2_SubNav" style="visibility: hidden; z-index: 999; display: none;">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getIsIBatchImportCheckData" class="menu_a_default"> ISI影响因子</a></li>
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
      <s:form id="mainForm" name="mainForm" method="post" action="" theme="simple">
        <!-- add by liuwei 将page记录数也传过去！ -->
        <input name="page.totalCount" type="hidden" value="${page.totalCount }"></input>
        <input name="psnId" id="psnId" type="hidden" value="" />
        <input name="addfind" id="addfind" type="hidden" value="" />
        <div class="functionTitle">
          <img src="${resmod}/images/arrow.gif" /> 期刊ISI影响因子查询
        </div>
        <div class="search_div">
          <table border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>期刊标题：<s:textfield name="isiIf.titleXx" maxlength="100" cssStyle="width:450px;" /></td>
              <td>PISSN：<s:textfield name="isiIf.pissn" maxlength="50" /></td>
              <td><input id="find" type="button" onclick="findTemp('getIsIBatchImportCheckData');" class="search_button" value="查询" /></td>
            </tr>
          </table>
        </div>
        <br />
        <br />
        <s:if test="page.pageCount>0">
          <%@ include file="/common/page-tages.jsp"%>
          <table cellpadding="0" cellspacing="0" width="100%" class="simple_table">
            <tr>
              <th width="30">序号</th>
              <th width="80">PISSN</th>
              <th>期刊名</th>
              <th width="60">来源</th>
              <th width="120">导入时间</th>
              <th width="60">操作</th>
            </tr>
            <s:iterator value="page.result" id="isiif" status="idx">
              <tr align="left">
                <td>${(page.pageNo-1)*page.pageSize+idx.count }</td>
                <td><s:property value="#isiif.pissn" /></td>
                <td>期刊英文名：<s:property value="#isiif.titleEn" /><br /> 期刊中文名：<s:property value="#isiif.titleXx" />
                </td>
                <td><s:property value="#isiif.dbCode" /></td>
                <td><s:date name="#isiif.createDate" format="yyyy-MM-dd HH:mm:ss" /></td>
                <td align="center"><s:if test="#isiif.status==1 || #isiif.status==0">
                    <a style="color:blue" onclick="processing('isIif',${isiif.tempIsiIfId})">待处理</a>
                  </s:if> <s:if test="#isiif.status==2">
				    		待审核
				    	</s:if> <s:if test="#isiif.status==3">
                    <a onclick="processing('isIif',${isiif.tempIsiIfId})">已拒绝</a>
                  </s:if></td>
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
      </s:form>
    </div>
</body>
</html>
