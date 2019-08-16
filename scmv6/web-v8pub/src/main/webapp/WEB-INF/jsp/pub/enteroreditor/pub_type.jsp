<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- <%@ include file="/common/taglibs.jsp"%> --%>
<div class="handin_import-content_container-center" style="margin: 32px 20px auto;">
  <div class="handin_import-content_container-left">
    <span class="handin_import-content_container-tip">*</span> <span><spring:message code="pub.enter.pub_type" /></span>
  </div>
  <div class="handin_import-content_container-right">
    <div class="handin_import-content_container-right_select">
      <div class="handin_import-content_container-right_select-box">
        <input type="text" class="dev-detailinput_container dev_select_input" id="showTypeName" readonly
          onfocus="this.blur()" unselectable="on" value="${pubVo.typeName}" />
      </div>
      <i class="material-icons handin_import-content_container-right_select-tip dev_pubtype"
        style="padding-right: 0px !important;">arrow_drop_down</i>
      <div class="handin_import-content_container-right_select-detail">
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="4"><spring:message
              code="pub.enter.pub_journal" /></span>
        </div>
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="5"><spring:message
              code="pub.enter.pub_patent" /></span>
        </div>
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="3"><spring:message
              code="pub.enter.pub_conference" /></span>
        </div>
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="1"><spring:message
              code="pub.enter.pub_award" /></span>
        </div>
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="2"><spring:message
              code="pub.enter.pub_book" /></span>
        </div>
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="10"><spring:message
              code="pub.enter.pub_chapter" /></span>
        </div>
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="8"><spring:message
              code="pub.enter.pub_thesis" /></span>
        </div>
        <!-- 标准 -->
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="12"><spring:message
              code="pub.enter.pub_standard" /></span>
        </div>
        <!-- 软件著作权 -->
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="13"><spring:message
              code="pub.enter.pub_softwarecopyright" /></span>
        </div>
        <div class="handin_import-content_container-right_select-item dev_pub_type">
          <span class="handin_import-content_container-right_select-item_detail" value="7"><spring:message
              code="pub.enter.pub_other" /></span>
        </div>
      </div>
    </div>
  </div>
</div>
<input type="hidden" id="pubType" name="pubType" value="${pubVo.pubType}" class="json_pubType" />
<input type="hidden" id="HCP" name="HCP" value="${pubVo.HCP}" class="json_HCP" />
<input type="hidden" id="HP" name="HP" value="${pubVo.HP}" class="json_HP" />
<input type="hidden" id="OA" name="OA" value="${pubVo.OA}" class="json_OA" />
