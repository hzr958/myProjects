<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jstl/xml_rt"%>
<c:set var="resprj" value="/ressns/js/prj" />
<html>
<%@ include file="edit_head.jsp"%>
<body>
  <script type="text/javascript">
        function back() {
            var backType = $("#backType").val();
            var url = "/prjweb/backurl";
            if(backType == 1){
                url = "/psnweb/homepage/show?module=prj&frompage=prjEdit";
            }else if(backType == 2){
                url = "/prjweb/project/detailsshow?des3PrjId=${des3Id}";
            }
            window.location.href = url;
        }
    </script>

  <div class="handin_import-container">
    <form name="mainform" id="mainform" action="/prjweb/prj/save" method="post">
      <x:parse xml="${prjXml}" var="myoutput" />
      <c:set var="prjId">
        <x:out select="$myoutput/data/prj_meta/@prj_id" />
      </c:set>
      <c:set var="fromPrjId">
        <x:out select="$myoutput/data/prj_meta/@from_prj_id" />
      </c:set>
      <input type="hidden" id="des3Id" name="des3Id" value="<iris:des3 code="${prjId }"/>" />
      <input type="hidden" id="des3PrjId" name="des3PrjId" value="" />
      <input type="hidden" id="_prj_meta_prj_id" name="/prj_meta/@prj_id" value="${prjId }" />
      <input type="hidden" type="hidden" name="from" id="from" value="${from}" />
      <input type="hidden" name="backType" id="backType" value="${backType}" />
      <input type="hidden" name="des3GrpId" id="des3GrpId" value="${des3GrpId}" />
      <div class="handin_import-container_title">
        <div class="handin_import-container_title-left">
          <span class="handin_import-container_title-left_content"><s:text name="project.edit.tip1"/> </span>
          (<span class="handin_import-container_title-left_tip"><s:text name="project.edit.tip2"/><span class="handin_import-content_container-tip">*</span><s:text name="project.edit.tip3"/></span>)
          <input type="hidden" id="_prj_authority"  readonly="readonly" name="/prj_meta/@authority"  value="${empty authority?7:authority}"/>
          <c:if test="${authority == 4}">
            <i class="selected-func_close " title="<s:text name="project.edit.onlyMe"/>"  authority="${authority }" onclick="ProjectEnter.setAuthority(this);"></i>
          </c:if>
          <c:if test="${authority != 4}">
            <i class="selected-func_close-open" title="<s:text name="project.edit.public"/>" authority="${authority }" onclick="ProjectEnter.setAuthority(this);"></i>
          </c:if>

        </div>
        <div class="handin_import-container_title-right">
          <c:if test="${!empty prjId}">
          <div style="font-size: 14px; color: #999;">
            <span><s:text name="projectEdit.label.serialno" /><s:text name="colon.all" /></span>
            <span>${prjId }</span>
          </div>
          </c:if>
        </div>
      </div>
      <div class="handin_import-content_container">
        <c:set var="zh_title">
          <x:out select="$myoutput/data/project/@zh_title" escapeXml="false" />
        </c:set>
        <c:set var="en_title">
          <x:out select="$myoutput/data/project/@en_title" escapeXml="false" />
        </c:set>
        <%--<div class="project-edit_item-center" style="margin: 32px 0px auto; display: flex; align-items: center; ">

          <div class="project-edit_item-title">
            <span class="handin_import-content_container-tip">*</span>
            <span><s:text name="project.authority.tip" />:</span>
          </div>

          <div class="arrow-selected_box" style="cursor: pointer;">
            <input type="hidden" id="_prj_authority"  readonly="readonly" name="/prj_meta/@authority"  value="${empty authority?7:authority}"/>
            <div class="arrow-selected_box-detail"><input type="text" readonly="readonly" UNSELECTABLE="on" style="cursor: pointer;" id="_prj_authority_show"/></div>
            <i class="material-icons arrow-selected_icon">arrow_drop_down</i>
            <div class="arrow-selected_container">
              <div class="arrow-selected_container-item" id="_prj_authority_1" authority="7" onclick="ProjectEnter.setAuthority(this);">公开</div>
              <div class="arrow-selected_container-item" id="_prj_authority_2" authority="4" onclick="ProjectEnter.setAuthority(this);">仅本人</div>
            </div>
          </div>
        </div>--%>
        <div class="project-edit_item-center">
          <div class="project-edit_item-title">
            <span class="handin_import-content_container-tip">*</span>
            <span><s:text name="project.edit.title.zh"/></span>
          </div>
          <div class="project-edit_item-displaybox_normal">
            <input type="text" value="<c:out value="${zh_title }" escapeXml="false"/> " name="/project/@zh_title" id="_project_zh_title" maxlength="250" />
          </div>
        </div>
        <div class="project-edit_item-center">
          <div class="project-edit_item-title">
            <span><s:text name="project.edit.title.en"/></span>
          </div>
          <div class="project-edit_item-displaybox_normal">
            <input type="text" value="<c:out value="${en_title }" escapeXml="false"/>" name="/project/@en_title" id="_project_en_title" maxlength="250" />
          </div>
        </div>

        <!-- 项目全文-->
        <%@ include file="prj_fulltext.jsp"%>

<!-- 项目资助机构 start-->
        <div class="project-edit_item-center">
          <c:set var="scheme_id">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_id" />
            </c:if>
          </c:set>
          <c:set var="scheme_agency_id">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_agency_id" />
            </c:if>
          </c:set>
          <c:set var="scheme_name">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_name" escapeXml="false" />
            </c:if>
          </c:set>
          <c:set var="scheme_agency_name">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_agency_name" escapeXml="false" />
            </c:if>
          </c:set>
          <input type="hidden" id="_project_scheme_id" name="/project/@scheme_id" value="${scheme_id }" />
          <input type="hidden" id="_project_scheme_agency_id" name="/project/@scheme_agency_id"
                <c:if test="${!empty prjId }">
                  value="<x:out select="$myoutput/data/project/@scheme_agency_id"/>"
                </c:if> />
          <input type="hidden" id="_project_old_scheme_agency_name" name="/project/@old_scheme_agency_name"
                 value="<c:out value="${scheme_agency_name}" escapeXml="false" />" />
          <input type="hidden" id="_project_old_scheme_name" name="/project/@old_scheme_name" value="<c:out value="${scheme_name}" escapeXml="false" />" />




          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.schemezh"/></span>
          </div>
          <div class="project-edit_item-displaybox_normal" style="border: none; display: flex; align-items: center; justify-content: space-between;" >
            <div class="project-edit_item-displaybox_fund-category" >

              <input type="text" class="dev-detailinput_container full_width json_member_insNames js_autocompletebox" none-request="true"
                     autocomplete="off" request-data="ProjectEnter.SchemeAgencyParamZH();" id="_project_scheme_agency_name"
                     name="/project/@scheme_agency_name" other-event="ProjectEnter.SchemeAgencyCallZH"
                     value="<c:out value="${scheme_agency_name}" escapeXml="false" />" request-url="/prjweb/prj/ajax-scheme-agencies" maxlength="150" code="">
            </div>
            <span >-</span>
            <div class="project-edit_item-displaybox_fund-category" >
              <input type="text" class="dev-detailinput_container full_width json_member_insNames js_autocompletebox" none-request="true"
                     autocomplete="off" request-data="ProjectEnter.SchemeAgencyParamZH();" id="_project_scheme_name"
                     name="/project/@scheme_name" other-event="ProjectEnter.SchemeCallZH"
                     value="<c:out value="${scheme_name}" escapeXml="false" />" request-url="/prjweb/prj/ajax-schemes" maxlength="100" code="">

            </div>
          </div>
        </div>



        <div class="project-edit_item-center">

          <c:set var="scheme_en_id">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_en_id" />
            </c:if>
          </c:set>
          <c:set var="scheme_agency_en_id">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_agency_en_id" />
            </c:if>
          </c:set>
          <c:set var="scheme_name_en">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_name_en" escapeXml="false"/>
            </c:if>
          </c:set>
          <c:set var="scheme_agency_name_en">
            <c:if test="${!empty prjId }">
              <x:out select="$myoutput/data/project/@scheme_agency_name_en"  escapeXml="false"/>
            </c:if>
          </c:set>
          <input type="hidden" id="_project_scheme_en_id" name="/project/@scheme_en_id" value="${scheme_en_id }" />
          <input type="hidden" id="_project_scheme_agency_en_id" name="/project/@scheme_agency_en_id"
                <c:if test="${!empty prjId }">
                  value="<x:out select="$myoutput/data/project/@scheme_agency_en_id" />"
                </c:if> />
          <input type="hidden" id="_project_old_scheme_agency_name_en" name="/project/@old_scheme_agency_name_en" value="<c:out value="${scheme_agency_name_en}" escapeXml="false" />" />
          <input type="hidden" id="_project_old_scheme_name_en" name="/project/@old_scheme_name_en" value="<c:out value="${scheme_name_en}" escapeXml="false" />" />


          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.schemeen"/></span>
          </div>
          <div class="project-edit_item-displaybox_normal" style="border: none; display: flex; align-items: center; justify-content: space-between;" >
            <div class="project-edit_item-displaybox_fund-category" >
              <input type="text" class="dev-detailinput_container full_width json_member_insNames js_autocompletebox"  none-request="true"
                     autocomplete="off" request-data="ProjectEnter.SchemeAgencyParamEN();" id="_project_scheme_agency_name_en"
                     name="/project/@scheme_agency_name_en" other-event="ProjectEnter.SchemeAgencyCallEN"
                     value="<c:out value="${scheme_agency_name_en}" escapeXml="false" />" request-url="/prjweb/prj/ajax-scheme-agencies" maxlength="150" code="">

            </div>
            <span>-</span>
            <div class="project-edit_item-displaybox_fund-category" >
              <input type="text" class="dev-detailinput_container full_width json_member_insNames js_autocompletebox"  none-request="true"
                     autocomplete="off" request-data="ProjectEnter.SchemeAgencyParamEN();" id="_project_scheme_name_en"
                     name="/project/@scheme_name_en" other-event="ProjectEnter.SchemeCallEN"
                     value="<c:out value="${scheme_name_en}" escapeXml="false" />" request-url="/prjweb/prj/ajax-schemes" maxlength="150" code="">

            </div>
          </div>
        </div>
        <!-- 项目资助机构 end-->
        
        <!-- 项目批准号 begin-->
        <div class="project-edit_item-center">
            <c:set var="prj_external_no">
              <x:out select="$myoutput/data/project/@prj_external_no" escapeXml="false" />
            </c:set>
            <div class="project-edit_item-title">
                <span><s:text name="projectEdit.label.project_no"/></span>
            </div>
            <div class="project-edit_item-displaybox_normal">
                <input type="text" value="<c:out value="${prj_external_no }" escapeXml="false"/> " name="/project/@prj_external_no" id="_prj_external_no" maxlength="20" />
            </div>
        </div>
        <!-- 项目批准号 end-->
        
        <div class="project-edit_item-center" id="ins_name_div">
          <div class="project-edit_item-title">
            <span class="handin_import-content_container-tip">*</span>
            <span><s:text name="projectEdit.label.prj_ins"/></span>
          </div>
          <div class="project-edit_item-displaybox_normal"  >
            <input type="text" class="dev-detailinput_container full_width json_member_insNames js_autocompletebox" autocomplete="off" none-request="true"
                   request-data="ProjectEnter.getInsNameJson();" id="_project_ins_name" name="/project/@ins_name"  other-event="ProjectEnter.InsNameCallBack"
                   value="<x:out select="$myoutput/data/project/@ins_name"  escapeXml="false"/>"
                   request-url="/prjweb/prj/ajax-psnIns"  maxlength="100"   code="">
            <input type="hidden" id="_project_ins_id" name="/project/@ins_id" value="<x:out select="$myoutput/data/project/@ins_id" />" />
            <input type="hidden" id="_project_old_ins_name" name="/project/@old_ins_name" value="<x:out select="$myoutput/data/project/@ins_name" escapeXml="false" />" />
          </div>
        </div>
        <!-- 项目年度 -->
        <div class="project-edit_item-center">
          <div class="project-edit_item-title">
            <span><s:text name="project.edit.annual"/></span>
          </div>
          <div class="project-edit_item-displaybox_small">
            <input type="text"  maxlength="4" id="_project_funding_year" name="/project/@funding_year"
                   value="<x:out  select="$myoutput/data/project/@funding_year" escapeXml="false" />" />
          </div>
        </div>

        <!-- 项目类型，项目状态 -->
        <div class="project-edit_item-center">
          <c:set var="prj_type">
            <x:out select="$myoutput/data/project/@prj_type" />
          </c:set>
          <div class="project-edit_item-title"> <%-- <s:text name="colon.all" /> --%>
            <span><s:text name="projectEdit.label.project_type" />:</span>
          </div>
          <div class="item_attribute-container" id="_project_prj_type" type="${prj_type}" style="justify-content: flex-start;">
            <input type="hidden" id="_project_prj_type_h" name="/project/@prj_type" value="${prj_type}">
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px 0px 0px;">
              <i class="selected-oneself  check-item_attribute" type="1" id="_project_prj_type1" onclick="ProjectEnter.setPrjType(this);"></i>
              <span class="selected-author_confirm-detaile"><s:text name="projectEdit.label.project_type_1"/></span>
            </div>

            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px;">
              <i class="selected-oneself  check-item_attribute"  type="0" id="_project_prj_type2" onclick="ProjectEnter.setPrjType(this);"></i>
              <span class="selected-author_confirm-detaile" ><s:text name="projectEdit.label.project_type_0"/></span>
            </div>
          </div>
        </div>
        <div class="project-edit_item-center">
          <c:set var="prj_state">
            <x:out select="$myoutput/data/project/@prj_state" />
          </c:set>
          <div class="project-edit_item-title">
          
            <span><s:text name="projectEdit.label.prj_state"/></span>
          </div>
          <div class="item_attribute-container" id="_project_prj_state" state="${prj_state}" style="justify-content: flex-start;">
            <input type="hidden" id="_project_prj_stat_h" name="/project/@prj_state" value="${prj_state}">
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px 0px 0px;">
              <i class="selected-oneself check-item_attribute" onclick="ProjectEnter.setPrjState(this);" id="_project_prj_state1" state="01"></i>
              <span class="selected-author_confirm-detaile"><s:text name="project.edit.status1"/></span>
            </div>

            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px;">
              <i class="selected-oneself check-item_attribute" onclick="ProjectEnter.setPrjState(this);" id="_project_prj_state2" state="02"></i>
              <span class="selected-author_confirm-detaile"><s:text name="project.edit.status2"/></span>
            </div>

            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px;">
              <i class="selected-oneself check-item_attribute" onclick="ProjectEnter.setPrjState(this);" id="_project_prj_state3" state="03"></i>
              <span class="selected-author_confirm-detaile"><s:text name="project.edit.status3"/></span>
            </div>

          </div>
        </div>
        <!-- 项目类型，项目状态 -->

        <div class="project-edit_item-center">
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.amount" /></span>
          </div>
          <div class="project-edit_item-displaybox_small">
            <input type="text"  id="_project_amount" name="/project/@amount"  maxlength="9"
                   value="<x:out  select="$myoutput/data/project/@amount" />" />
          </div>
          &nbsp;&nbsp;
            <c:set var="amountUnit">
              <x:out select="$myoutput/data/project/@amount_unit" />
            </c:set>
            <select id="_project_amount_unit" class="inp_text" name="/project/@amount_unit">
              <s:iterator value="prjMoneyType" var="key">
                <c:choose>
                  <c:when test="${!empty amountUnit}">
                    <option value='${key.code}' <c:if test="${key.code eq amountUnit}">selected</c:if>>${enUsName}</option>
                  </c:when>
                  <c:otherwise>
                    <c:choose>
                      <c:when test="${locale eq 'zh_CN' }">
                        <option value='${key.code}' <c:if test="${key.code eq 'RMB'}">selected</c:if>>${enUsName}</option>
                      </c:when>
                      <c:otherwise>
                        <option value='${key.code}' <c:if test="${key.code eq 'HKD' }">selected</c:if>>${enUsName}</option>
                      </c:otherwise>
                    </c:choose>
                  </c:otherwise>
                </c:choose>
              </s:iterator>
            </select>
        </div>
        
        <div class="project-edit_item-center">
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.project_date"/></span>
            <c:set var="start_year" scope="page">
              <x:out select="$myoutput/data/project/@start_year" />
            </c:set>
            <c:set var="start_month" scope="page">
              <x:out select="$myoutput/data/project/@start_month" />
            </c:set>
            <c:set var="start_day" scope="page">
              <x:out select="$myoutput/data/project/@start_day" />
            </c:set>
            <c:set var="end_year" scope="page">
              <x:out select="$myoutput/data/project/@end_year" />
            </c:set>
            <c:set var="end_month" scope="page">
              <x:out select="$myoutput/data/project/@end_month" />
            </c:set>
            <c:set var="end_day" scope="page">
              <x:out select="$myoutput/data/project/@end_day" />
            </c:set>
            <input type="hidden" value="${start_year}" id="_project_start_year" name="/project/@start_year">
            <input type="hidden" value="${start_month}" id="_project_start_month" name="/project/@start_month">
            <input type="hidden" value="${start_day}" name="/project/@start_day" id="_project_start_day">
            <input type="hidden" value="${end_year}" id="_project_end_year" name="/project/@end_year">
            <input type="hidden" value="${end_month}" id="_project_end_month" name="/project/@end_month">
            <input type="hidden" value="${end_day}"    id="_project_end_day" name="/project/@end_day">
          </div>
          <div class="project-edit_item-displaybox_normal input__box"   style="border: none; display: flex; align-items: center; justify-content: flex-start;" >
            <div class="project-edit_item-displaybox_normal-itemdata" >
              <input class=""  type="text" name="projectStartDate"   confirmEvent="ProjectEnter.listenerPrjDate();"
                     id="projectStartDate" unselectable="on"  readonly="" datepicker="" focusdata=""
                     date-format="yyyy-mm-dd" value="" date-year="${start_year}" date-month="${start_month}" date-date="${start_day}" aria-invalid="false">
            </div>
            <span >&nbsp;-&nbsp;</span>
            <div class="project-edit_item-displaybox_normal-itemdata"  >
              <input class=""  type="text" name="projectEndDate" confirmEvent="ProjectEnter.listenerPrjDate();"
                     id="projectEndDate" unselectable="on"  readonly="" datepicker="" focusdata=""
                     date-format="yyyy-mm-dd" value="" date-year="${end_year}" date-month="${end_month}" date-date="${end_day}" aria-invalid="false">
            </div>
          </div>
        </div>

        <div class="project-edit_item-start">
          <div class="project-edit_item-title">
            <span><s:text name="project.edit.abstract.zh"/></span>
          </div>
          <div class="project-edit_item-displaybox_normal" style="height: auto;">
            <textarea maxlength="20000" name="/project/@zh_abstract" id="_project_zh_abstract">
              <x:out select="$myoutput/data/project/@zh_abstract" escapeXml="false" />
            </textarea>
          </div>
        </div>

        <div class="project-edit_item-start">
          <div class="project-edit_item-title">
            <span><s:text name="project.edit.abstract.en"/></span>
          </div>
          <div class="project-edit_item-displaybox_normal" style="height: auto;">
            <textarea maxlength="20000" name="/project/@en_abstract" id="_project_en_abstract"><x:out select="$myoutput/data/project/@en_abstract" escapeXml="false" /></textarea>
          </div>
        </div>
        
        <%-- <div class="project-edit_item-center">
          <s:set var="disNameArr" value="disName.split('-')" />
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.discipline"/></span>
          </div>
          <div class="project-edit_item-displaybox_small">
            <input type="hidden" disc_code="<s:property value="#disNameArr[0]"/>"
                   disc_name="<s:property value="#disNameArr[1]"/>" id="_project_discipline_id" name="/project/@discipline"
                   value="<x:out select="$myoutput/data/project/@discipline" />" />
            <input type="text" value="<s:property value="#disNameArr[1]"/>" id="_project_discipline_id_1"  readonly="readonly" unselectable="on" />
            <i class="selected-func_field" onclick="ProjectEnter.showDisciplineBox();"></i>
          </div>
        </div> --%>
        <div class="project-edit_item-center">
          <s:set var="disNameArr" value="disName.split('-')" />
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.discipline"/></span>
          </div>
          <div class="project-edit_item-displaybox_small">
            <input type="hidden" disc_code="<s:property value="#disNameArr[0]"/>"
                   disc_name="<s:property value="#disNameArr[1]"/>" id="_project_discipline_id" name="/project/@areaId"
                   value="<x:out select="$myoutput/data/project/@areaId" />" />
            <input type="text" value="<s:property value="#disNameArr[1]"/>" id="_project_discipline_id_1"  readonly="readonly" unselectable="on" />
            <i class="selected-func_field" onclick="ProjectEnter.showAreaBox();"></i>
          </div>
        </div>

        <!-- 项目编辑关键词-->
        <div class="project-edit_item-center">
          <input name="/project/@zh_keywords" id="_project_zh_keywords" type="text"
                 value="<x:out select="$myoutput/data/project/@zh_keywords" />" style="display: none; " />
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.ckeywords"/></span>
          </div>
          <div class="new-importantkey_container project-edit_item-displaybox_normal" id="prj_keyword_list">
            <div class="handin_import-content_container-right_input" style="justify-content: center; height: 28px; border: none; width: 100%;">
              <div class="new-importantkey_container" style="width: 98%; margin-left: -12px; min-height: 28px; height: auto;">
                <input type="text" id="addKeyInput" maxlength="80" autocomplete="off" class="new-importantkey_container-input  dev-detailinput_container valid" style="height: 28px; padding: 0px; width: 20% !important;"
                       placeholder="<s:text name="project.edit.srkword"/>" onfouce="" aria-invalid="false">
              </div>
            </div>
          </div>
        </div>

        <div class="project-edit_item-center">
          <input name="/project/@en_keywords" id="_project_en_keywords" type="text"
                 value="<x:out select="$myoutput/data/project/@en_keywords" />" style="display: none; " />
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.ekeywords"/></span>
          </div>
          <div class="new-importantkey_container project-edit_item-displaybox_normal" id="prj_keyword_list_en">
            <div class="handin_import-content_container-right_input" style="justify-content: center; height: 28px; border: none; width: 100%;">
              <div class="new-importantkey_container" style="width: 98%; margin-left: -12px; min-height: 28px; height: auto;">
                <input type="text" id="addKeyInputEn" maxlength="80" autocomplete="off" class="new-importantkey_container-input  dev-detailinput_container valid" style="height: 28px; padding: 0px; width: 20% !important;"
                       placeholder="<s:text name="project.edit.srkword"/>" onfouce="" aria-invalid="false">
              </div>
            </div>
          </div>
        </div>


        <!-- 项目编辑关键词 end-->

        <div class="project-edit_item-center">
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.remark" /></span>
          </div>
          <div class="project-edit_item-displaybox_normal" >
            <input type="text" id="_project_remark" maxlength="400"
                   name="/project/@remark" value="<x:out  select="$myoutput/data/project/@remark" escapeXml="false" />" />
          </div>
        </div>

        <!--  附件-->
        <div class="project-edit_item-center" style="align-items: flex-start;">
          <div class="project-edit_item-title">
            <span><s:text name="projectEdit.label.otherFile.filename"/> </span>
          </div>
          <!--  模板-->
          <!-- template-->
          <div class="handin_import-content_container-right_upload-box fileupload__box dev_fileupload__box" style="display: none" maxClass="prj_attachment_dev" maxlength="10" onclick="ProjectAttachment.fileuploadBoxOpenInputClick(event);" title="<s:text name="project.edit.upfileMsg"/>">
            <div class="fileupload__core initial_shown">
              <div class="fileupload__initial">
                <div class="pubv8-enter_add_file1_avator"></div>
                <div class="pubv8-enter_add_file2_avator"></div>
                <div class="fileupload__hint-text" style="left: -27px; bottom: -40px; width: 120px;">
                  <s:text name="project.edit.selectFile"/></div>
                <input type="file" class="fileupload__input"  style="display: none;">
              </div>
              <div class="fileupload__progress">
                <canvas width="56" height="56"></canvas>
                <div class="fileupload__progress_text"></div>
              </div>
              <div class="fileupload__saving">
                <div class="preloader"></div>
                <div class="fileupload__saving-text"></div>
              </div>
              <div class="fileupload__finish"></div>
            </div>
          </div>
          <!--  模板-->
          <div  class="project-edit_item-footer_body-right" id="projectAttachmentList">
            <div class="handin_import-content_container-right"  style="justify-content: flex-start; align-items: flex-start; display: flex;">
              <div class="upfile" filetype="fulltext">
                <div class="handin_import-content_container-right_upload-box fileupload__box"   maxClass="prj_attachment_dev" maxlength="10" onclick="ProjectAttachment.fileuploadBoxOpenInputClick(event);" title="<s:text name="project.edit.upfileMsg"/>">
                  <div class="fileupload__core initial_shown">
                    <div class="fileupload__initial">
                      <div class="pubv8-enter_add_file1_avator"></div>
                      <div class="pubv8-enter_add_file2_avator"></div>
                      <div class="fileupload__hint-text" style="left: -27px; bottom: -40px; width: 120px;">
                        <s:text name="project.edit.selectFile"/></div>
                      <input type="file" class="fileupload__input"  style="display: none;">
                    </div>
                    <div class="fileupload__progress">
                      <canvas width="56" height="56"></canvas>
                      <div class="fileupload__progress_text"></div>
                    </div>
                    <div class="fileupload__saving">
                      <div class="preloader"></div>
                      <div class="fileupload__saving-text"></div>
                    </div>
                    <div class="fileupload__finish"></div>
                  </div>
                </div>
              </div>
              <!--  模板 end-->
              <div class="handin_import-content_container-right_upload-item  prj_attachment_template" style="width:45%;display: none">
                <input type="hidden" id="_prj_attachments_prj_attachment_0_file_id"
                       name="/prj_attachments/prj_attachment[0]/@file_id" value="" />
                <input type="hidden" id="_prj_attachments_prj_attachment_0_seq_no"
                       name="/prj_attachments/prj_attachment[0]/@seq_no"  value="" />
                <input type="hidden" id="_prj_attachments_prj_attachment_0_file_ext"
                       name="/prj_attachments/prj_attachment[0]/@file_ext" value="" />
                <input type="hidden" style="width: 85% !important;" maxlength="61" autocomplete="off" class="dev-detailinput_container full_width json_member_name "
                       name="/prj_attachments/prj_attachment[0]/@file_name"
                       id="_prj_attachments_prj_attachment_0_file_name"
                       value=""  />
                <input type="hidden" name="/prj_attachments/prj_attachment[0]/@permission"
                       id="_prj_attachments_prj_attachment_0_permission" value="2">
                <span class="handin_import-content_container-right_upload-item_detaile dev_prj_name_show"><a title=""></a></span>
                <i class="material-icons handin_import-content_upload-item_icon" onclick="ProjectAttachment.delPrjAttachment(this);">close</i>
                <%--<i class="selected-func_close" title="仅自己可以查看" onclick="ProjectAttachment.setPermission(this)"></i>--%>
              </div>
              <!--  模板-->
              <div class="project-edit_item-uploadfile_list"  id="prj_attachment_list" style="width: 100%;">
                <c:set value="0" var="index" scope="page" />
                <x:forEach select="$myoutput/data/prj_attachments/prj_attachment" var="attach">
                  <c:set value="${index+1}" var="index" scope="page" />
                  <c:choose>
                    <c:when test="${index<10}">
                      <c:set value="0" var="flag" scope="page" />
                    </c:when>
                    <c:otherwise>
                      <c:set value="" var="flag" scope="page" />
                    </c:otherwise>
                  </c:choose>


                  <c:set var="fileCode">
                    <x:out select="$attach/@file_id" />
                  </c:set>
                  <c:set var="file_name">
                    <x:out select="$attach/@file_name" escapeXml="false" />
                  </c:set>
                  <c:set var="fileExt">
                    <x:out select="$attach/@file_ext" escapeXml="false" />
                  </c:set>

                  <c:set var="permission">
                    <x:out select="$attach/@permission" />
                  </c:set>

                  <div class="handin_import-content_container-right_upload-item prj_attachment_dev" style="width:45%;">
                    <input type="hidden" id="_prj_attachments_prj_attachment_${flag}${index}_file_id"
                           name="/prj_attachments/prj_attachment[${flag}${index}]/@file_id" value="${fileCode}" />
                    <input type="hidden" id="_prj_attachments_prj_attachment_${flag}${index}_seq_no"
                           name="/prj_attachments/prj_attachment[${flag}${index}]/@seq_no"  value="${index}" />
                    <input type="hidden" id="_prj_attachments_prj_attachment_${flag}${index}_file_ext"
                           name="/prj_attachments/prj_attachment[${flag}${index}]/@file_ext" value="${fileExt}" />
                    <input type="hidden" style="width: 85% !important;" maxlength="61" autocomplete="off" class="dev-detailinput_container full_width json_member_name "
                           name="/prj_attachments/prj_attachment[${flag}${index}]/@file_name"
                           id="_prj_attachments_prj_attachment_${flag}${index}_file_name"
                           value="${file_name}"  />
                    <span class="handin_import-content_container-right_upload-item_detaile dev_prj_name_show" onclick="ProjectEnter.link('${prjAttachDownload[fileCode]}',this,event);"><a title="${file_name}">${file_name}</a></span>
                    <i class="material-icons handin_import-content_upload-item_icon" onclick="ProjectAttachment.delPrjAttachment(this);">close</i>
                      <%--<i class="<c:if test="${permission != 2}">selected-func_close-open</c:if> <c:if test="${permission == 2}">selected-func_close</c:if>" title="<c:if test="${permission == 2}">仅自己可以查看</c:if><c:if test="${permission != 2}">所有人都可以查看</c:if>" onclick="ProjectAttachment.setPermission(this)">
                      </i>--%>
                    <input type="hidden" name="/prj_attachments/prj_attachment[${flag}${index}]/@permission"
                           id="_prj_attachments_prj_attachment_${flag}${index}_permission" value="2">
                  </div>

                </x:forEach>
              </div>
            </div>
          </div>
        </div>

        <div class="project-edit_item-center">
          <div class="project-edit_item-title">
            <span><s:text name="project.edit.fulltextlink"/></span>
          </div>
          <c:set var="fulltext_url">
            <x:out  select="$myoutput/data/prj_fulltext/@fulltext_url" escapeXml="false"/>
          </c:set>
          <c:if test="${fulltext_url == ''}">
            <c:set var="fulltext_url" value="http://"/>
          </c:if>
          <div class="project-edit_item-displaybox_normal" >
            <input type="text" id="_prj_fulltext_fulltext_url" maxlength="400"
                   name="/prj_fulltext/@fulltext_url" value="${fulltext_url}" />
          </div>
        </div>
      </div>

      <div class="project-edit_item-footer">
        <!-- 项目成员-->
        <%@ include file="prj_member.jsp"%>
      </div>


      <!-- 项目附件
      <div class="project-edit_item-footer">
      </div>
      -->

      <div class="handin_import-content_container-center" style="margin: 32px 0px;">
      <div class="handin_import-content_container-left" style="width: 14%;">
        <span></span>
      </div>
      <div class="handin_import-content_container-right" style="display: flex; align-items: center; margin-right: -24px;">
        <div class="handin_import-content_container-save" onclick="ProjectEnter.save(this);">
          <s:text name="projectEdit.button.save"/></div>
        <div class="handin_import-content_container-cancle" onclick="ProjectEnter.back(this);">
          <s:text name="projectEdit.button.back"/></div>
      </div>
    </div>
    </form>
  </div>


  <!-- 学科弹出框 -->
  <div class="dialogs__box" dialog-id="disciplineBox" style="width: 720px;" cover-event="" id="disciplineBox"
       process-time="0"></div>
  <!-- 学科弹出框 -->

  <!--  提示框-->
  <div class="background-cover" id="resultMsg" style="display: none;">
    <div class="new-success_save" id="new-success_save">
      <div class="new-success_save-body">
        <div class="new-success_save-body_avator">
          <img id="show_img" src="${resmod}/smate-pc/img/pass.png">
          <input type="hidden" id="successImg" value="${resmod}/smate-pc/img/pass.png" />
          <input type="hidden" id="errorImg" value="${resmod}/smate-pc/img/fail.png" />
        </div>
        <div class="new-success_save-body_tip">
          <span id="show_msg"></span> <input type="hidden" id="save_error_msg" value="保存成果失败！" />
          <input type="hidden" id="save_success_msg" value="保存成功" />
          <input type="hidden" id="change_msg" value="切换类型可能会丢失您填写的部分内容，您确定要切换类型吗？" />
        </div>
        <div class="new-success_save-body_footer" id="saveBotten">
          <div id="returnBottom" class="new-success_save-body_footer-complete" isEdit="true"
               onclick="PubEdit.viewPubs(this);"><s:text name="project.edit.backlist"/></div>
          <div id="continueBottom" class="new-success_save-body_footer-continue" onclick="PubEdit.continueEdit();"><s:text name="project.edit.continue"/></div>
        </div>
        <input type="hidden" id="changTypeName" value="" />
      </div>
    </div>
  </div>
</body>
</html>
