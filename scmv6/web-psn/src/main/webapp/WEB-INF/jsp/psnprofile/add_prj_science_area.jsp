<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
  $(document).ready(function() {
    //绑定研究领域列表展开事件
    addFormElementsEvents(document.getElementById("research-area-list"));
    var selectAreaIds = $("#selectAreaId").val();
    if (selectAreaIds) {
      var areas = selectAreaIds.split(",");
      for(i in areas){
        $("#checked_area_"+areas[i]).closest(".nav-cascade__toggle-list").prev(".nav-cascade__item").removeClass("list_toggle-off");
      }
    }
  });
</script>
<input type="hidden" id="selectAreaId" value="${scienceAreaIds}" />
<div class="dialogs__childbox_fixed">
  <div class="dialogs__header">
    <div class="dialogs__header_title">
      <s:text name='homepage.profile.box.title.select.sciencearea' />
    </div>
  </div>
</div>
<div class="dialogs__childbox_adapted">
  <div class="dialogs__content global__padding_24">
    <div class="sugg-picker">
      <div class="sugg-picker__header">
        <s:text name='homepage.profile.box.title.select.one.sciencearea' />
      </div>
      <div class="sugg-picker__panel">
        <div class="sugg-panel left-panel">
          <div class="sugg-panel__content">
            <div class="nav-cascade" id="research-area-list">
              <c:forEach items="${categoryMap['CategoryMap_first'] }" var="firstLevel" varStatus="firstStatus">
                <c:set var="subKey" value="CategoryMap_sub${firstLevel.categryId}"></c:set>
                <input type="hidden" value="${subKey }" />
                <div class="nav-cascade__section">
                  <div class="nav-cascade__item list_toggle-off">
                    <div class="nav-cascade__title">${firstLevel.showCategory }</div>
                    <i class="nav-cascade__icon js_togglelist material-icons">expand_less</i>
                  </div>
                  <div class="nav-cascade__toggle-list">
                    <c:forEach items="${categoryMap[subKey] }" var="subLevel">
                      <c:if test="${!subLevel.added }">
                        <div class="nav-cascade__item" onclick="ProjectEnter.addDiscipline('${subLevel.categryId}')"
                          id="unchecked_area_${subLevel.categryId }">
                          <div class="nav-cascade__title" id="${subLevel.categryId}_category_title">${subLevel.showCategory }</div>
                          <c:if test="${!subLevel.added }">
                            <i class="nav-cascade__icon material-icons" id="${subLevel.categryId}_status">add</i>
                          </c:if>
                        </div>
                      </c:if>
                      <c:if test="${subLevel.added }">
                        <div class="nav-cascade__item" id="checked_area_${subLevel.categryId }">
                          <div class="nav-cascade__title" id="${subLevel.categryId}_category_title">${subLevel.showCategory }</div>
                          <c:if test="${subLevel.added }">
                            <i class="nav-cascade__icon material-icons" style="color: forestgreen"
                              id="${subLevel.categryId}_status">check</i>
                          </c:if>
                        </div>
                      </c:if>
                    </c:forEach>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
        <div class="sugg-panel right-panel">
          <div class="sugg-panel__title">
            <s:text name='homepage.profile.box.title.hasselected.sciencearea' />
          </div>
          <div class="sugg-panel__content">
            <div class="main-list__list global_no-border" id="choosed_area_list">
              <c:forEach items="${categoryList }" var="area" varStatus="status">
                <div class="main-list__item" areaid="${area.categryId }" style="padding: 0px 16px !important;">
                  <div class="main-list__item_content">${area.showCategory }</div>
                  <div class="main-list__item_actions">
                    <a onclick="ProjectEnter.delDiscipline('${area.categryId }');"><i class="material-icons">close</i></a>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__childbox_fixed">
  <div class="dialogs__footer">
    <button class="button_main button_primary-reverse" id="homepage_area_save_btn" onclick="ProjectEnter.saveDiscipline();">
      <s:text name='homepage.profile.btn.save' />
    </button>
    <button class="button_main button_primary-cancle" onclick="ProjectEnter.hideDiscipline(this);">
      <s:text name='homepage.profile.btn.cancel' />
    </button>
  </div>
</div>
