<%@ page language="java" pageEncoding="UTF-8"%>
<ul>
  <!-- 简介 -->
  <li id="groupIntro" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','intro');"><a><s:text
        name="group.tip.course.description" /></a></li>
  <s:if test="groupInvitePsn!=null || groupPsn.openType==\"O\"">
    <!-- 动态 -->
    <li id="groupDyn" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','dyn');"><a><s:text
          name="group.course.tab.activity" /></a></li>
    <!-- 成员 -->
    <li id="groupMember" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','member')"><a><s:text
          name="group.tab.member" /></a></li>
    <%-- 是否支持教学课件 --%>
    <s:if test="groupPsn.isMaterialView==1">
      <li id="groupCourses" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','course')"><a><s:text
            name="group.tip.course.document" /></a></li>
    </s:if>
    <s:if test="groupInvitePsn!=null">
      <%-- 是否支持作业,群组成员才能查看 --%>
      <s:if test="groupPsn.isWorkView==1">
        <li id="groupWorks" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','work')"><a><s:text
              name="group.tip.assignment" /></a></li>
      </s:if>
    </s:if>
    <%-- 是否支持文献 --%>
    <s:if test="groupPsn.isRefView==1">
      <li id="groupRefs" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','ref')"><a><s:text
            name="group.tab.reference" /></a></li>
    </s:if>
    <%-- 是否支持群组文件 --%>
    <s:if test="groupPsn.isShareFile==1">
      <li id="groupFiles" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','file')"><a><s:text
            name="group.tab.file" /></a></li>
    </s:if>
    <%-- 是否支持文献 --%>
    <%-- <s:if test="groupPsn.isRefView==1">
		<li id="groupRefs" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','ref')"><a ><s:text name="group.tab.reference" /></a></li>
		</s:if> --%>
    <%-- <!-- 成员 -->
		<li id="groupMember" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','member')"><a ><s:text name="group.tab.member" /></a></li>
 --%>
  </s:if>
  <s:if test="groupInvitePsn!=null">
    <!-- 群组主页 -->
    <s:if test="groupInvitePsn.groupRole==1 || groupInvitePsn.groupRole==2">
      <li id="groupWebPage" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','page');"><a><s:text
            name="group.tab.webpage" /></a></li>
    </s:if>
    <s:if test="groupInvitePsn.groupRole > 0">
      <li id="settings" style="position: absolute; margin-right: 10px; right: 0; border-left: 1px solid #dbdbdb;">
        <a><span class="icon-setup"></span> <s:text name="group.tab.settings" /></a>
      </li>
    </s:if>
  </s:if>
</ul>