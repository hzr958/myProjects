<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

var showGrpOutsideDiscussMainOpnetype = "${openType}";
var showGrpOutsideDiscussMainFlag = "${flag}";
var showGrpOutsideDiscussMainGrpControl = "${GrpControl.isMemberShow}"
$(document).ready(function(){
    var flag = false;
    if (showGrpOutsideDiscussMainOpnetype == "O") {
      flag = true;
    }
    if (showGrpOutsideDiscussMainOpnetype == "H" && showGrpOutsideDiscussMainFlag == "1") {
      flag = true;
    }
    if (flag) {
    	GrpDiscuss.showGrpOutsideDiscussList();
    }
	GrpDiscuss.showGrpDesc("/groupweb/grpinfo/outside/ajaxgrpbrief");
	GrpDiscuss.showOtherInfo("/groupweb/grpinfo/outside/ajaxgrpotherinfo");
	if (showGrpOutsideDiscussMainGrpControl == "1") {
    	GrpDiscuss.showGrpFiveMember("/groupweb/grpinfo/outside/ajaxgrpdiscussmembers");
	}
	GrpDiscuss.showGrpFivePub("/groupweb/grpinfo/outside/ajaxgrpdiscusspubs");
});
</script>
<div class="container__horiz_left width-7" flyin-direction="bottom">
  <div class="main-list">
    <c:if test="${openType == 'O'}">
      <div class="main-list__list item_no-border" list-main="grp_discuss_list"></div>
    </c:if>
    <c:if test="${openType == 'H' && flag == '1'}">
      <div class="main-list__list item_no-border" list-main="grp_discuss_list"></div>
    </c:if>
    <c:if test="${openType == 'H' && flag == '0'}">
      <div class="main-list__list item_no-border">
        <div class="response_no-result">
          <s:text name='groups.base.loginOrJoinPrompt' />
        </div>
      </div>
    </c:if>
  </div>
</div>
<div class="container__horiz_right width-5">
  <div class="container__card" id="grp_discuss_desc"></div>
  <div class="container__card" id="grp_discuss_other"></div>
  <div class="container__card" id="grp_discuss_member"></div>
  <div class="container__card" id="grp_discuss_pub"></div>
</div>
