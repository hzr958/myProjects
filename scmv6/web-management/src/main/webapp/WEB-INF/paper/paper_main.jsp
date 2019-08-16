<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.thickbox.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/management/pub.management.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript">
   var domainscm="${domainscm}";
   $("document").ready(function() {
     simpleSearch();
   });

 </script>
<div style="width: 1280px; margin: 0 auto; display: flex; flex-direction: column;">
  <div class="search_box" style="margin-bottom: 12px; display: flex;">
    <div style="display: flex; flex-direction: column;">
      <div class="s_in Fleft" style="width: 430px; height: 30px; line-height: 30px; display: flex;">
        <span style="font-size: 14px; font-weight: bold; margin-right: 6px;">成果ID:</span> <input maxlength='200'
          id="pubId" type="text" style="height: 30px; width: 320px; border: 1px solid #ccc;" />
      </div>
    </div>
    <div style="display: flex; margin-left: -20px;">
      <span style="font-size: 14px; font-weight: bold; margin-right: 6px; margin-top: 6px;">成果标题:</span> <input
        maxlength='200' id="title" type="text" style="height: 30px; width: 320px; border: 1px solid #ccc;" />
      <div id="clearBtn" onclick="clearSearch()"
        style="cursor: pointer; width: 64px; height: 30px; text-align: center; font-size: 16px; line-height: 30px; color: #fff; margin-left: 108px; background-color: #288aed;">清空</div>
      <div id="simpleSearchBtn" onclick="simpleSearch()"
        style="cursor: pointer; width: 64px; height: 30px; text-align: center; font-size: 16px; line-height: 30px; color: #fff; margin-left: 20px; background-color: #288aed;">检索</div>
    </div>
  </div>
</div>
<div class="contant-maintitle mtop10"
  style="width: 1280px; margin: 0 auto; display: flex; justify-content: space-between; font-size: 15px; margin-bottom: 0px;">
  <div class="bargain" style="margin: 12px 0px 0px 32px;">
    <span class="bargain-icon" style="font-size: 15px;"></span>
  </div>
  <div class="btn-wrapright" style="margin-right: 192px; margin-top: 8px;">
    <a id="deletePub" onclick="deletePub()" class="uiButton">删除成果</a>
  </div>
</div>
<div id="table_header" style="width: 1280px; margin: 0 auto;">
  <div class="table_header">
    <table width="100%" border="0" cellspacing="0" cellpadding="1" class="t_css">
      <tr class="t_tr">
        <td width="8%" align="center">操作列</td>
        <td align="10%" align="center">成果Id</td>
        <td width="32%" align="center">标题</td>
        <td width="20%" align="center">来源</td>
        <td width="30%" align="center">作者</td>
      </tr>
    </table>
  </div>
  <div class="main-column"></div>
  <!-- 分页 -->
</div>
<!-- </div> -->
