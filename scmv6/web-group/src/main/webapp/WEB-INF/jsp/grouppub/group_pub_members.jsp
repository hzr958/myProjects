<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
  <li>
    <div>
      <div class="Fleft text-overflow2 groupMemberMenuTitle">
        <s:text name="group.leftMenu.item.groupPsnPub" />
      </div>
    </div>
  </li>
</ul>
<!-- <dl class="menu-shrink"> -->
<ul>
  <s:if test="groupInvitePsnList.size > 0">
    <s:iterator value="groupInvitePsnList" var="member" status="stat">
      <s:if test="#stat.index <= 4">
        <li class="group_psn_pub"
          onclick="Member.publication.loadPsnOpenPubs('${member.psnId}','${member.groupId}','1');"
          style="margin-left: 10px;">
      </s:if>
      <s:if test="#stat.index > 4">
        <li class="group_psn_pub"
          onclick="Member.publication.loadPsnOpenPubs('${member.psnId}','${member.groupId}','1');"
          style="display: none; margin-left: 10px;">
      </s:if>
      <a style="height: 60px;" onclick="selected(this);">
        <div class="Pleft_tupian" style="float: left;">
          <img src="${member.avatars}" width="60" height="60" border="0" />
        </div>
        <div class="Pright_name" style="float: left; margin-left: 10px;">
          <c:if test="${locale eq 'zh_CN' }">
            <p class="cuti" style="width: 80px; text-overflow: ellipsis; overflow: hidden;" title="${member.psnName}">${member.psnName}</p>
          </c:if>
          <c:if test="${locale eq 'en_US' }">
            <p class="cuti" style="width: 80px; text-overflow: ellipsis; overflow: hidden;"
              title="${member.psnFirstName}&nbsp;${member.psnLastName}">${member.psnFirstName}&nbsp;${member.psnLastName}</p>
          </c:if>
          <p title="${member.openPubSum}" style="text-overflow: ellipsis; overflow: hidden;">${member.openPubSum}</p>
          <c:if test="${member.isAccept ne '1' }">
            <p id="lab${member.psnId }" class="f8080" style="display: inline">
              <s:text name="group.psnpub.member.waitToJoin" />
            </p>
          </c:if>
        </div>
      </a>
      </li>
    </s:iterator>
  </s:if>
  <input type="hidden" class="groupPsnPubIndex" name="groupPsnPubIndex" value="1" />
  <s:if test="groupInvitePsnList.size > 5">
    <dd id="menu-dd-year-move" class="date_bottom_${locale}" style="padding-top: 10px;">
      <a onclick="Member.publication.groupPsnPubMove('left');" id="preFive"
        title="<s:text name='group.psnpub.memberList.preFive'/>"> <span> <s:text
            name='group.psnpub.memberList.preFive' />
      </span>
      </a> <a class="canclick" onclick="Member.publication.groupPsnPubMove('right');" id="nextFive"
        title="<s:text name='group.psnpub.memberList.nextFive'/>" canClick="true"> <span> <s:text
            name='group.psnpub.memberList.nextFive' />
      </span>
      </a>
    </dd>
  </s:if>
</ul>
<c:if test="${groupInvitePsn.groupRole == 1 || groupInvitePsn.groupRole == 2 }">
  <div id="inviteFriend" onclick="javascript:inviteFriendToGroup('${des3GroupId }');" style="padding-top: 20px;">
    <a style="text-decoration: none;"> <span class="addfriends" style="background-position: 3px -60px;"></span> <span><s:text
          name="group.psnpub.member.invitepsn" /></span>
    </a> <input type="hidden" name="isInvite" id="isInvite" value="0" />
  </div>
</c:if>
