<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:iterator value="psnInfoList" status="st">
  <s:if test="#st.index < 5">
    <li id="invite_join_${st.index}" class="invite_jion_list"><a href="javascript:;" class="group_mber fl"
      style="text-decoration: none; cursor: default;"> <img style="cursor: pointer;" src="${avatarUrl}"
        onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" alt=""
        onclick="Group.detailsGroupPubMember('${des3PsnId}')">
        <h4 id="invite_name_${st.index}">
          <span style="cursor: pointer;" onclick="Group.detailsGroupPubMember('${des3PsnId}')">${name}</span>
        </h4>
        <p style="cursor: default;">${person.insName}</p>
    </a> <a href="javascript:;" onclick="Group.inviteMember('${des3PsnId}',${st.index})"
      class="waves-effect waves-teal button02 w62 invite_btn"><i class="material-icons add_icon">&#xe145;</i> <s:text
          name="group.member.invite" /></a></li>
  </s:if>
  <s:if test="#st.index >= 5">
    <li id="invite_join_${st.index}" style="display: none;" class="invite_jion_list psn_hide"><a
      href="javascript:;" class="group_mber fl" style="text-decoration: none; cursor: default;"> <img
        style="cursor: pointer;" src="${avatarUrl}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" alt=""
        onclick="Group.detailsGroupPubMember('${des3PsnId}')">
        <h4 id="invite_name_${st.index}">
          <span style="cursor: pointer;" onclick="Group.detailsGroupPubMember('${des3PsnId}')">${name}</span>
        </h4>
        <p style="cursor: default;">${person.insName}</p>
    </a> <a href="javascript:;" onclick="Group.inviteMember('${des3PsnId}',${st.index})"
      class="waves-effect waves-teal button02 w62 invite_btn"><i class="material-icons add_icon">&#xe145;</i> <s:text
          name="group.member.invite" /></a></li>
  </s:if>
</s:iterator>
