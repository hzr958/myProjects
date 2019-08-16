<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="memberList">
  <script type="text/javascript">
	$(document).ready(function() {
		//当前人群组角色为组员时,隐藏移除此人操作
		if ($("#currentPsnGroupRole").val() == 3) {
			$(".del_psn_li").each(function(){
				$(this).hide();
			});
			$(".to_admin_li").each(function(){
				$(this).hide();
			});
			$(".to_member_li").each(function(){
				$(this).hide();
			});
		}else if ($("#currentPsnGroupRole").val() == 2) {
			$(".to_member_li").each(function(){
				$(this).hide();
			});
		}
		//分页主入口
		TagesMain.main(loadMember);
	});
	function loadMember(){
		Group.groupMemberList(Group.getGroupMemberParam());
	}
</script>
  <table border="0" cellpadding="0" cellspacing="0" class="tab_lt">
    <s:if test="psnInfoList.size > 0">
      <s:iterator value="psnInfoList" status="st">
        <tr id="memberList_${st.index}">
          <td width="58"><a href="javascript:Group.detailsGroupPubMember('<iris:des3 code="${psnId}"/>');"><img
              src="${avatarUrl}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'" alt=""
              class="member_header"></a></td>
          <td>
            <div class="menber_infro">
              <p id="psn_${st.index}">
                <strong style="cursor: pointer;" onclick="Group.detailsGroupPubMember('<iris:des3 code="${psnId}"/>')">${name}</strong>&nbsp;&nbsp;<span
                  class="f999"><s:if test="groupRole==1">(创建人)</s:if> <s:elseif test="groupRole==2">(管理员)</s:elseif></span>
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
          <c:if test="${ currentPsnGroupRoleStatus==3 || currentPsnGroupRoleStatus==4}">
            <c:if test="${currentPsnId != psnId }">
              <td width="200" align="center">
                <div class="member_pop">
                  <a href="#" class="mber_mn_icon"></a>
                  <div class="member_pop_cont">
                    <div class="arrow1">
                      <em></em><span></span>
                    </div>
                    <ul>
                      <c:if test="${groupRole ==1}">
                      </c:if>
                      <c:if test="${groupRole ==2}">
                        <li class="to_member_li" onclick="Group.toGroupMember('<iris:des3 code="${invitePsnId}" />')">
                          <a href="javascript:;"> 设置为普通成员 </a>
                        </li>
                      </c:if>
                      <c:if test="${groupRole ==3  && currentPsnGroupRoleStatus == 4}">
                        <li class="to_admin_li" onclick="Group.toGroupAdmin('<iris:des3 code="${invitePsnId}" />')">
                          <a href="javascript:;"> 设置为管理员 </a>
                        </li>
                      </c:if>
                      <c:if test="${currentPsnId != psnId }">
                        <li id="psn_li_${st.index}" class="del_psn_li"
                          onclick="Group.deleteGroupMember('<iris:des3 code="${psnId}"></iris:des3>',${st.index},${groupRole})"><a
                          href="javascript:;"><s:text name="group.member.delete.member" /></a></li>
                      </c:if>
                      <%-- <li onclick="Group.addMemberResume('<iris:des3 code="${psnId}"></iris:des3>',${st.index})"><a href="javascript:;"><s:text name="group.member.produce.resume"/></a></li> --%>
                    </ul>
                  </div>
                </div>
              </td>
            </c:if>
          </c:if>
        </tr>
      </s:iterator>
    </s:if>
    <!-- 没有记录 -->
    <s:else>
      <div class="both-center" style="width: 100%; height: 240px;">未找到相关记录</div>
    </s:else>
  </table>
  <s:if test="psnInfoList.size >0">
    <div id="member_page">
      <s:include value="/common/group-page-tages.jsp"></s:include>
    </div>
  </s:if>
</div>