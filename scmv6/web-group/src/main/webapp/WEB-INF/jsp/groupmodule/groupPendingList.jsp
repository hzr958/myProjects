<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="pendingList">
  <script type="text/javascript">
	$(function(){
		//分页主入口
		TagesMain.main(loadPending);
	});
	function loadPending(){
		Group.groupPendingList(Group.getGroupPendingParam());
	}
</script>
  <input id="PendingCount" type="hidden" value="${page.totalCount}" />
  <table border="0" cellpadding="0" cellspacing="0" class="tab_lt">
    <s:if test="psnInfoList.size > 0">
      <s:iterator value="psnInfoList" status="st">
        <tr id="pending_${st.index}">
          <td width="58"><a href="${snsctx }/resume/psnView?des3PsnId=<iris:des3 code="${psnId}"></iris:des3>"><img
              src="${avatarUrl}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'" alt=""
              class="member_header"></a></td>
          <td>
            <div class="menber_infro">
              <p>
                <strong>${name}</strong>&nbsp;&nbsp;<span class="f999"><s:if test="groupRole==1">(Admin)</s:if> <s:elseif
                    test="groupRole==2">(Owner)</s:elseif></span>
              </p>
              <p class="f666">
                <s:if test="primaryIns.INSNAME !=null">${primaryIns.INSNAME}</s:if>
                <s:if test="primaryIns.INSDPT !=null">,&nbsp;${primaryIns.INSDPT}</s:if>
                <s:if test="primaryIns.INSPOS !=null">,&nbsp;${primaryIns.INSPOS}</s:if>
              </p>
              <p class="p1">
                <span class="f999"><s:text name="group.member.prjNum"></s:text>&nbsp;${prjSum}</span><span class="f999"><s:text
                    name="group.member.pubNum"></s:text>&nbsp;${pubSum}</span>
              </p>
            </div>
          </td>
          <td align="center"><a id="accept_a_${st.index}" href="javascript:;"
            onclick="Group.acceptMember('<iris:des3 code="${invitePsnId}"></iris:des3>',${st.index})" class="apply_pass"><s:text
                name="group.pending.accept"></s:text></a><a href="javascript:;"
            onclick="Group.ignoreMember('<iris:des3 code="${invitePsnId}"></iris:des3>',${st.index})"
            class="apply_ignore ml20"><s:text name="group.pending.ignore"></s:text></a></td>
        </tr>
      </s:iterator>
    </s:if>
    <s:else>
      <div class="both-center" style="width: 100%; height: 240px;">未找到相关记录</div>
    </s:else>
  </table>
  <s:if test="psnInfoList.size >0">
    <div id="pending_page">
      <s:include value="/common/group-page-tages.jsp"></s:include>
    </div>
  </s:if>
</div>