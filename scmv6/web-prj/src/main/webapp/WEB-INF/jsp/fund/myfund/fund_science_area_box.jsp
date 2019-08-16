<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="scienceAreaIds" id="scienceAreaIds" value="${scienceAreaIds }" />
<input type="hidden" name="scienceAreaSum" id="scienceAreaSum" value="${fundScienceArea.size() }" />
<script type="text/javascript">
	$(document).ready(function(){
	    //绑定研究领域列表展开事件
		addFormElementsEvents(document.getElementById("research-area-list"));
	});
 </script>
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
        <s:text name='homepage.profile.box.title.select.five.sciencearea' />
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
                        <div class="nav-cascade__item" onclick="FundRecommend.addScienceArea('${subLevel.categryId}')"
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
              <c:forEach items="${fundScienceArea }" var="area" varStatus="status">
                <div class="main-list__item" id="choosed_${area.scienceAreaId }">
                  <div class="main-list__item_content">${area.showTitle }</div>
                  <div class="main-list__item_actions main-list__item_actions-opacity">
                    <a onclick="FundRecommend.delScienceArea('${area.scienceAreaId }');"><i class="material-icons">close</i></a>
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
    <button class="button_main button_primary-reverse" onclick="FundRecommend.savePsnFundConditions(this);"
      id="nextToKeywords">
      <s:text name='psn.improve.info.complete' />
    </button>
    <button class="button_main " onclick="FundRecommend.cancelPsnFundConditions(this);">
      <s:text name='homepage.profile.btn.cancel' />
    </button>
  </div>
</div>
