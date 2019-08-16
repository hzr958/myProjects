<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){

});
</script>
<s:iterator value="grpFileMemberList" var="grpFileMember">
  <div class="friend-selection__item-2" onclick="Grp.selectMemberFile('${grpFileMember.des3MemberId}'  ,this)">
    <div class="psn-idx_small">
      <div class="psn-idx__base-info">
        <div class="psn-idx__avatar_box">
          <div class="psn-idx__avatar_img">
            <img src="${grpFileMember.memberAvator}" onerror="this.src='${resmod}/smate-pc/img/avatar.jpg'">
          </div>
        </div>
        <div class="psn-idx__main_box">
          <div class="psn-idx__main">
            <div class="psn-idx__main_name psn-idx__main_name-length_limit" title="${grpFileMember.memberName}">${grpFileMember.memberName}</div>
            <div class="psn-idx__main_title">
              <s:text name='groups.file.uploadNum' />
              ：${grpFileMember.memberFileNum}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>