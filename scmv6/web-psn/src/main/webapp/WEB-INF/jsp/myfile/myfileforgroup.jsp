<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="groupMyFile" class="group_r fr">
  <input type="hidden" id="myfilepageno" value="1" />
  <s:if test="sfList.size>0">
    <div class="rt_box">
      <div class="r_title1">
        <h2>我的文件库</h2>
      </div>
      <table class="rt_tab" id="myFileTable">
        <s:include value="myfileforgroupsub.jsp"></s:include>
      </table>
    </div>
  </s:if>
</div>
