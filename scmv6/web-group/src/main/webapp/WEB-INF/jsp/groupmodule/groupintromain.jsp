<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="group_box">
  <script type="text/javascript">
	$(document).ready(function(){
		Group.groupDynLoad();
	});
</script>
  <div id="group_dyn_main_load" class="group_l fl">
    <div id="group_dyn_main_preloader" style="height: 38px; margin: 0 auto; width: 792px;"></div>
  </div>
  <div class="group_r fr">
    <div class="rt_box">
      <div class="r_title">
        <s:text name="group.tab.abstract"></s:text>
      </div>
      <div class="group_paper">
        <p style="white-space: normal;">
          <c:out value="${ groupPsn.groupDescription}" escapeXml="fasle"></c:out>
        </p>
        <div class="group_keywords">
          <s:text name="group.tab.keywords"></s:text>
          ： ${ groupPsn.keyWords}
        </div>
      </div>
    </div>
    <div class="rt_box mt10">
      <div class="r_title">
        <s:text name="group.tab.member"></s:text>
      </div>
      <div class="group_member">
        <ul class="group_mber_lt">
          <c:forEach items="${groupInvitePsns }" var="groupInvitePsn">
            <li><a href="${snsctx }/resume/psnView?des3PsnId=${ groupInvitePsn.des3PsnId}"><img
                src="${ groupInvitePsn.avatars}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a></li>
          </c:forEach>
        </ul>
        <div class="r_title">
          <s:text name="group.tab.representPublication"></s:text>
        </div>
        <ul class="group_mber_eft">
          <s:iterator value="groupPubs" var="groupPub">
            <li><s:if test=' zhTitle == null || zhTitle =="" '>
                <a
                  href='${snsctx }/publication/view?des3Id=<s:property value="des3pubId"/>&groupId=&ownerId=&snsNodeId=1'>
                  <s:property value="enTitle" escapeHtml="false" />
                </a>
              </s:if> <s:else>
                <a
                  href='${snsctx }/publication/view?des3Id=<s:property value="des3pubId"/>&groupId=&ownerId=&snsNodeId=1'>
                  <s:property value="zhTitle" escapeHtml="false" />
                </a>
              </s:else>
              <p>
                <s:property value="authorNames" escapeHtml="false" />
              </p></li>
          </s:iterator>
        </ul>
      </div>
    </div>
  </div>
</div>