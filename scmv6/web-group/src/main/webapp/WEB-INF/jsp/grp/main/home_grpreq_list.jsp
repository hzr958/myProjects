<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${grpShowInfoList}" var="grp" varStatus="stat">
  <div class="main-list__item">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div>
              <a href="${grp.grpInviterUrl }" target="_Blank"> <img class="main-list__list-item__container-avator"
                src="${grp.grpInviterAvatars}">
              </a>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title" style="display: flex; align-items: center;">
                <a href="${grp.grpInviterUrl }" target="_Blank" class="pub-idx__main_title-limit" title="<c:out value="${grp.grpInviterName }" escapeXml="false" />"> 
                   <b class="blue" title="${grp.grpInviterName }">
                    <c:out value="${grp.grpInviterName }" escapeXml="false" />
                </b>
                </a> <span style="cursor: default">&nbsp;<s:text name="groups.label.reqJoin" />&nbsp;</span> 
                <a class="pub-idx__main_title-limit" title="${grp.grpName }" href="${grp.grpIndexUrl }" target="_Blank"><b class="blue"> ${grp.grpName }</b></a>
              </div>
              <div class="pub-idx__main_author">
                <c:out value=" ${grp.grpInviterInsName }" escapeXml="false" />
              </div>
              <div class="pub-idx__main_src">
                <c:out value=" ${grp.grpInviterDepartment }" escapeXml="false" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey"
        onclick="Rm.disposegrpApplication(0,'${grp.grpInviterDes3psnId}','${grp.des3GrpId}',this)">
        <s:text name="groups.list.btn.ignore" />
      </button>
      <button class="button_main button_dense button_primary-changestyle"
        onclick="Rm.disposegrpApplication(1,'${grp.grpInviterDes3psnId}','${grp.des3GrpId}',this)">
        <s:text name="groups.list.btn.agree" />
      </button>
    </div>
  </div>
</c:forEach>
