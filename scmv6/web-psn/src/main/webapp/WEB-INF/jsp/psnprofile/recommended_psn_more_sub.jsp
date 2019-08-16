<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
/* $(document).ready(function(){
    var buttonlist = document.getElementsByClassName("button_delefriend");
    clickDynamicreact(buttonlist);
}) */
</script>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${psnInfoList}" var="psnInfo">
  <div class="main-list__item">
    <div class="main-list__item_content"
      onclick="BaseUtils.goToPsnHomepage('<iris:des3 code="${psnInfo.psnId}"/>', '${psnInfo.psnShortUrl}');"
      style="cursor: pointer;">
      <div class="psn-idx_small">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <a href="/psnweb/homepage/show?des3PsnId=${psnInfo.des3PsnId}" target="_blank"><img
                src="${psnInfo.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name" title="${psnInfo.name}">${psnInfo.name}</div>
              <div class="psn-idx__main_title1">${psnInfo.insAndDep}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_primary"
        onclick="MainBase.addOneFriend('${psnInfo.des3PsnId}',this)">
        <s:text name="friend.main.list.addfriend" />
      </button>
    </div>
  </div>
</c:forEach>