<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.thickbox.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/management/psn.management.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript">
   var domainbpo="${domainbpo}";
   var domainscm="${domainscm}";
   var nowDate = new Date();
   $("document").ready(function() {
	   $("#addMergeButton").thickbox();
	   $("#addMerge").click(function(){
			$("#addMergeButton").attr("alt","/scmmanagement/setting/enterAddMerge?TB_iframe=true&height=180&width=470");
			$("#addMergeButton").click();
		});
   });

 </script>
<div style="width: 1280px; margin: 0 auto; display: flex; flex-direction: column;">
  <div class="search_box" style="margin-bottom: 12px; display: flex;">
    <div style="display: flex; flex-direction: column;">
      <div class="s_in Fleft" style="width: 430px; height: 30px; line-height: 30px; display: flex;">
        <span style="font-size: 14px; font-weight: bold; margin-right: 6px;">人名:</span> <input maxlength='200'
          id="nameSearchContent" type="text" style="height: 30px; width: 320px; border: 1px solid #ccc;" />
      </div>
      <div style="width: 430px; margin-top: 16px; font-size: 14px; margin-left: 32px;">
        <input type="radio" name="search_type" value="1" checked />&nbsp;精确，完全匹配输入的内容 <input type="radio"
          name="search_type" value="0" />&nbsp;模糊，有一部分内容匹配上就行
      </div>
    </div>
    <div style="display: flex; margin-left: -20px;">
      <span style="font-size: 14px; font-weight: bold; margin-right: 6px; margin-top: 6px;">账号/邮箱:</span> <input
        maxlength='200' id="emailSearchContent" type="text" style="height: 30px; width: 320px; border: 1px solid #ccc;" />
      <div id="simpleSearchBtn" onclick="simpleSearch()"
        style="cursor: pointer; width: 64px; height: 30px; text-align: center; font-size: 16px; line-height: 30px; color: #fff; margin-left: 108px; background-color: #288aed;">检索</div>
    </div>
  </div>
</div>
<%-- 人员操作权限 --%>
<div class="contant-maintitle mtop10"
  style="width: 1280px; margin: 0 auto; display: flex; justify-content: space-between; font-size: 15px; margin-bottom: 0px;">
  <div class="bargain" style="margin: 12px 0px 0px 32px;">
    <span class="bargain-icon" style="font-size: 15px;"><i class="icon21 py-icon"></i> <s:text name="人员" /></span>
  </div>
  <div class="btn-wrapright" style="margin-right: 192px; margin-top: 8px;">
    <a id="addMerge" class="uiButton">合并账号</a> <input type="hidden" id="addMergeButton" class="thickbox" title="合并账号" />
  </div>
</div>
<div id="table_header" style="width: 1280px; margin: 0 auto;">
  <div class="table_header">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="t_css">
      <tr class="t_tr">
        <td width="10%" align="center">&nbsp;</td>
        <td align="left"><s:text name="基本信息" /></td>
        <td align="12%" align="center"><s:text name="人员Id" /></td>
        <td width="12%" align="center"><s:text name="账号/邮箱" /></td>
        <td width="12%" align="center"><s:text name="项目/成果/专利" /></td>
        <td width="12%" align="center"><s:text name="历史邮件" /></td>
        <td width="12%" align="center">&nbsp;&nbsp;</td>
        <td width="12%" align="center"><s:text name="注册/登录时间" /></td>
      </tr>
    </table>
  </div>
  <div class="main-column"></div>
  <!-- 分页 -->
</div>
<!-- </div> -->
