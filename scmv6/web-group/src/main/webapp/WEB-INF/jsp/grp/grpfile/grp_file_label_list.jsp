<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">


$(document).ready(function(){
	
});
</script>
<s:iterator value="grpLabelShowInfoList" var="showInfo">
  <div class="kw-chip_container">
    <div class="kw-chip_small" onclick="Grp.selectGrpLabelFileList(this);" style="display: flex;"
      des3GrpLabelid="${showInfo.des3LabelId}">
      <span class="kw-chip_small-detail" style="cursor: pointer;">${showInfo.labelName}</span>
      <s:if test="#showInfo.resCount==0">
        <span class="kw-chip_small-num"></span>
      </s:if>
      <s:else>
        <span class="kw-chip_small-num" style="cursor: pointer;">&nbsp;(${showInfo.resCount})</span>
      </s:else>
    </div>
    <s:if test="#showInfo.showDel==true">
      <i class="normal-global_icon normal-global_del-icon normal-global_del-icon_show"
        title='<s:text name="groups.file.del.label"/>' onclick="Grp.delGrpLabelConfirm(event,this)"></i>
    </s:if>
  </div>
</s:iterator>
