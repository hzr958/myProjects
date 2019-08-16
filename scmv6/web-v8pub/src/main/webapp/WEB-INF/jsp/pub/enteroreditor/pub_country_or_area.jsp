<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="sel-dropdown__box" selector-data="1st_area" data-type="json" item-event="PubEdit.getArea()"
  data-src="request" request-url="/psnweb/common/ajaxRegion"></div>
<div class="sel-dropdown__box" selector-data="2nd_area" data-type="json" item-event="PubEdit.getSubArea()"
  data-src="request" request-url="/psnweb/common/ajaxRegion" request-data="PubEdit.getAreaJSON()"></div>
<div class="sel-dropdown__box" selector-data="3nd_area" data-type="json" item-event="PubEdit.getRegionId()"
  data-src="request" request-url="/psnweb/common/ajaxRegion" request-data="PubEdit.getSubAreaJSON()"></div>
<input class="json_countryId" type="hidden" name="countryId" id="countryId" value="${pubVo.countryId }" />
<div class="handin_import-content_container-right_state-sub_body"
  style="width: 30%; padding: 0px; flex-direction: column; align-items: flex-start; height: 32px;">
  <div style="width: 550px; height: 32px; display: flex; align-items: center; justify-content: space-between;">
    <div class=" handin_import-content_container-right_select handin_import-content_container-right_select-subcontainer sel__box"
      selector-id="1st_area" style="width: 35%;">
      <div class="handin_import-content_container-right_select-box">
        <input class="dev-detailinput_container full_width check_date sel__value_selected" type="text"
          placeholder='<spring:message code="pub.profile.note.first.region"/>' name="year" id="year" unselectable="on"
          onfocus="this.blur()" readonly="readonly" value='' />
      </div>
      <i class="material-icons">arrow_drop_down</i>
    </div>
    <div class="handin_import-content_container-right_select handin_import-content_container-right_select-subcontainer sel__box"
      selector-id="2nd_area" style="visibility: hidden; width: 35%; margin:0px 4px;">
      <div class="handin_import-content_container-right_select-box">
        <input class="dev-detailinput_container full_width check_date sel__value_selected" type="text"
          placeholder='<spring:message code="pub.profile.note.second.region"/>' name="month" id="month"
          unselectable="on" onfocus="this.blur()" readonly="readonly" value='' />
      </div>
      <i class="material-icons">arrow_drop_down</i>
    </div>
    <div class=" handin_import-content_container-right_select handin_import-content_container-right_select-subcontainer sel__box"
      selector-id="3nd_area" style="visibility: hidden; width: 35%;">
      <div class="handin_import-content_container-right_select-box">
        <input class="dev-detailinput_container full_width check_date sel__value_selected" type="text"
          placeholder='<spring:message code="pub.profile.note.third.region"/>' name="date" id="date"
          onfocus="this.blur()" unselectable="on" readonly="readonly" value='' />
      </div>
      <i class="material-icons">arrow_drop_down</i>
    </div>
  </div>
  <div class="json_countryId_msg" style="margin-top: 4px; display: none"></div>
</div>
