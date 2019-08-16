
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="groupInvitePsnList.size > 0">
  <div class="rs_title">
    <s:text name='group.pubs.memberPub' />
    <div style="flex-grow: 1; margin-right: -8px;">
      <div class="flipper" id="add_member_pub_flipper">
        <div class="x_axis_front" style="left: auto; right: 0; font-size: 14px;">
          <c:if test="${currentPsnGroupRoleStatus==2 || currentPsnGroupRoleStatus==3 || currentPsnGroupRoleStatus==4}">
            <div class="btn_normal btn_bg_blue elevation " id="add_member_pub"
              onclick="javascript:Group.addMemberPubUI('<s:property value="groupInvitePsnList[0].des3PsnId"/>',this);">
              <s:text name='group.pubs.importMemberPub' />
            </div>
          </c:if>
        </div>
        <div class="x_axis_back" style="left: auto; right: 0; font-size: 14px;">
          <div class="btn_normal btn_bg_red elevation" id="add_member_pub2"
            onclick="javascript:Group.backFormPsnOpenPubs(this);">
            <s:text name='group.pubs.backMemberPub' />
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="rs_container import_members_pub">
    <div class="rs_content">
      <s:iterator value="groupInvitePsnList" var="member" status="stat">
        <div class="person_dialog_namecard_whole" style="margin-bottom: 8px;" des3PsnId="${member.des3PsnId}"
          onclick="Group.detailsGroupPubMember('${member.des3PsnId}')">
          <div>
            <img class="avatar" src="${member.avatars}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
          </div>
          <div class="person_information">
            <div class="name">
              <c:if test="${locale eq 'zh_CN' }">${member.psnName}</c:if>
              <c:if test="${locale eq 'en_US' }">${member.psnFirstName}&nbsp;${member.psnLastName}</c:if>
            </div>
            <div class="institution">成果数：${openPubSum }</div>
          </div>
        </div>
      </s:iterator>
      <%-- <ul>
          	
				<s:iterator value="groupInvitePsnList" var="member" status="stat">
            		<li >
              			<a  href="javascript:;" des3PsnId="${member.des3PsnId}" class="group_mber fl" style="cursor:default;">
              			<img style="cursor:pointer;" src="${member.avatars}" alt="" onclick="Group.detailsGroupPubMember('${member.des3PsnId}')" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                		<h4>
                			<span  onclick="Group.detailsGroupPubMember('${member.des3PsnId}')"  style="cursor:pointer;">
                			<c:if test="${locale eq 'zh_CN' }">${member.psnName}</c:if>
                			<c:if test="${locale eq 'en_US' }">${member.psnFirstName}&nbsp;${member.psnLastName}</c:if>
                			</span>
                		</h4>
                		
                		<p style="cursor:default;">成果数：${openPubSum }</p>
                		</a>
            		</li>
            	</s:iterator>
          
          </ul> --%>
    </div>
  </div>
</s:if>