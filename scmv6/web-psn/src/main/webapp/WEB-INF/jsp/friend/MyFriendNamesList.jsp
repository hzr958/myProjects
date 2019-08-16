<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		$("#id_group_add_friend_names_list").find(".person_namecard_whole_tiny").click(function(){
			if($(this).hasClass("chosen")){
				$(this).removeClass("chosen");
			}else{
				$(this).addClass("chosen");
			}
		});
	});
	</script>
<s:iterator value="psnInfoList" var="psnInfo" status="st">
  <div class="select_person_container">
    <div class="person_namecard_whole_tiny">
      <div>
        <img class="avatar" src="<s:property value="#psnInfo.avatarUrl"/>">
      </div>
      <div class="person_information">
        <div class="name" title="<s:property value='#psnInfo.name'/>" code="<s:property value='#psnInfo.des3PsnId'/>">
          <s:property value="#psnInfo.name" />
        </div>
        <div class="institution"></div>
      </div>
    </div>
  </div>
</s:iterator>
