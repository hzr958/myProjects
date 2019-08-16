<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	
	</script>
<s:iterator value="psnInfoList" var="psnInfo" status="st">
  <div class="chip__box white-style" code="<s:property value='#psnInfo.des3PsnId'/>" style="cursor: pointer;"
    onclick="SmateShare.clickMaybeShareFriendName(this);">
    <div class="chip__avatar">
      <img src="<s:property value='#psnInfo.avatarUrl'/>">
    </div>
    <div class="chip__text" title="<s:property value='#psnInfo.name' escape='false'/>">
      <s:property value="#psnInfo.name" escape="false" />
    </div>
    <div class="chip__icon icon_add">
      <i class="material-icons">add</i>
    </div>
  </div>
</s:iterator>