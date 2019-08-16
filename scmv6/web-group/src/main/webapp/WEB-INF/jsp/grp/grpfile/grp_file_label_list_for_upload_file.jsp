<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:iterator value="grpLabelShowInfoList" var="showInfo">
  <div class="set__result-label_item-list" onclick="Grp.addUploadFileLabel(event,this);"
    des3LabelId="${showInfo.des3LabelId}">${showInfo.labelName}</div>
</s:iterator>
