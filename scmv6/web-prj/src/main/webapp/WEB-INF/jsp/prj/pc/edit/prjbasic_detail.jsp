<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="prjbasic_title_detail.jsp"%>
<%@ include file="prjbasic_abstract_detail.jsp"%>
<%@ include file="prjbasic_keywords_detail.jsp"%>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="projectEdit.label.schemezh" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <c:set var="scheme_id">
        <x:out select="$myoutput/data/project/@scheme_id" />
      </c:set>
      <c:set var="scheme_agency_id">
        <x:out select="$myoutput/data/project/@scheme_agency_id" />
      </c:set>
      <c:set var="scheme_name">
        <x:out select="$myoutput/data/project/@scheme_name" />
      </c:set>
      <c:set var="scheme_agency_name">
        <x:out select="$myoutput/data/project/@scheme_agency_name" />
      </c:set>
      <input type="hidden" id="_project_scheme_id" name="/project/@scheme_id" value="${scheme_id }" /> <input
        type="hidden" id="_project_scheme_agency_id" name="/project/@scheme_agency_id"
        value="<x:out select="$myoutput/data/project/@scheme_agency_id" />" /> <input type="hidden"
        id="_project_old_scheme_agency_name" name="/project/@old_scheme_agency_name" value="${scheme_agency_name }" />
      <input type="hidden" id="_project_old_scheme_name" name="/project/@old_scheme_name" value="${scheme_name }" /> <input
        type="text" maxlength="200" class="inp_text" isselect="true" style="width: 300px"
        id="_project_scheme_agency_name" name="/project/@scheme_agency_name" value="${scheme_agency_name }" />
      &nbsp;-&nbsp; <input type="text" maxlength="200" class="inp_text" isselect="true" style="width: 300px"
        id="_project_scheme_name" name="/project/@scheme_name" value="${scheme_name }" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="projectEdit.label.schemeen" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <c:set var="scheme_en_id">
        <x:out select="$myoutput/data/project/@scheme_en_id" />
      </c:set>
      <c:set var="scheme_agency_en_id">
        <x:out select="$myoutput/data/project/@scheme_agency_en_id" />
      </c:set>
      <c:set var="scheme_name_en">
        <x:out select="$myoutput/data/project/@scheme_name_en" />
      </c:set>
      <c:set var="scheme_agency_name_en">
        <x:out select="$myoutput/data/project/@scheme_agency_name_en" />
      </c:set>
      <input type="hidden" id="_project_scheme_en_id" name="/project/@scheme_en_id" value="${scheme_en_id }" /> <input
        type="hidden" id="_project_scheme_agency_en_id" name="/project/@scheme_agency_en_id"
        value="<x:out select="$myoutput/data/project/@scheme_agency_en_id" />" /> <input type="hidden"
        id="_project_old_scheme_agency_name_en" name="/project/@old_scheme_agency_name_en"
        value="${scheme_agency_name_en }" /> <input type="hidden" id="_project_old_scheme_name_en"
        name="/project/@old_scheme_name_en" value="${scheme_name_en }" /> <input type="text" maxlength="200"
        class="inp_text" isselect="true" style="width: 300px" id="_project_scheme_agency_name_en"
        name="/project/@scheme_agency_name_en" value="${scheme_agency_name_en}" /> &nbsp;-&nbsp; <input type="text"
        maxlength="200" class="inp_text" isselect="true" style="width: 300px" id="_project_scheme_name_en"
        name="/project/@scheme_name_en" value="${scheme_name_en}" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <label class="title" tab="1" for="_project_prj_internal_no"> <s:text
          name="projectEdit.label.prj_internal_no" />
      </label>
    </dd>
    <dd>
      <input type="text" maxlength="20" class="inp_text" style="width: 300px" id="_project_prj_internal_no"
        name="/project/@prj_internal_no" value="<x:out  select="$myoutput/data/project/@prj_internal_no" />" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <label class="title" tab="1" for="_project_prj_external_no"> <s:text
          name="projectEdit.label.prj_external_no" />
      </label>
    </dd>
    <dd>
      <input type="text" maxlength="20" class="inp_text" style="width: 300px" id="_project_prj_external_no"
        name="/project/@prj_external_no" value="<x:out  select="$myoutput/data/project/@prj_external_no" />" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <s:set var="disNameArr" value="disName.split('-')" />
    <dd class="info_name">
      <label class="title" tab="1" for="_project_discipline"> <s:text name="projectEdit.label.discipline" />
      </label>
    </dd>
    <dd>
      <div class="disc_input_class"></div>
      <input type="hidden" disc_code="<s:property value="#disNameArr[0]"/>"
        disc_name="<s:property value="#disNameArr[1]"/>" id="_project_discipline_id" name="/project/@discipline"
        value="<x:out select="$myoutput/data/project/@discipline" />" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <label class="title" tab="1" for="_project_ins_name"> <span class="red">*</span> <s:text
          name="projectEdit.label.prj_ins2" />
      </label>
    </dd>
    <dd>
      <input type="hidden" id="_project_ins_id" name="/project/@ins_id"
        value="<x:out select="$myoutput/data/project/@ins_id" />" /> <input type="hidden" id="_project_old_ins_name"
        name="/project/@old_ins_name" value="<x:out select="$myoutput/data/project/@ins_name" />" /> <input type="text"
        maxlength="200" isselect="true" class="inp_text" style="width: 300px" id="_project_ins_name"
        name="/project/@ins_name" value="<x:out select="$myoutput/data/project/@ins_name" />" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <label class="title" tab="1" for="_project_funding_year"> <s:text name="projectEdit.label.funding_year" />
      </label>
    </dd>
    <dd>
      <input type="text" maxlength="4" class="inp_text" style="width: 40px" id="_project_funding_year"
        name="/project/@funding_year" value="<x:out  select="$myoutput/data/project/@funding_year" />" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="projectEdit.label.project_type" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <c:set var="prj_type">
        <x:out select="$myoutput/data/project/@prj_type" />
      </c:set>
      <iris:constDictionary webContextType="${webContextType}" key="prj_type" code="${prj_type }"
        tagName="/project/@prj_type"></iris:constDictionary>
    </dd>
  </dl>
</li>
<li>
  <dl>
    <div class="prjStateError pub-error" style="padding-left: 18%; display: none;">
      <label for="_project_prj_state1" class="error"> <s:text name="project.state.error" />
      </label>
    </div>
    <dd class="info_name">
      <label class="title" for="_project_prj_state1" tab="1"> <s:text name="projectEdit.label.prj_state" />
      </label>
    </dd>
    <dd>
      <c:set var="prj_state">
        <x:out select="$myoutput/data/project/@prj_state" />
      </c:set>
      <iris:constDictionary webContextType="${webContextType}" key="prj_state" code="${prj_state }"
        tagName="/project/@prj_state"></iris:constDictionary>
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <label class="title" tab="1" for="_project_amount"> <s:text name="projectEdit.label.amount" />
      </label>
    </dd>
    <dd>
      <input type="text" class="inp_text" style="width: 45px" id="_project_amount" name="/project/@amount"
        value="<x:out  select="$myoutput/data/project/@amount" />" />
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
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <label class="title" tab="1" for="_project_start_year" style="display: none"> <s:text
          name="projectEdit.label.project_startdate" />
      </label>
      <s:text name="projectEdit.label.project_date" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <label class="title" tab="1" for="_project_end_year" style="display: none"> <s:text
          name="projectEdit.label.project_enddate" />
      </label>
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
      <c:set var="dateInputTagId" value="_project_"></c:set>
      <c:set var="dateInputTagName" value="/project/@"></c:set>
      <%@ include file="prjEnter_datePeriodInput_tag.jsp"%>
    </dd>
  </dl>
</li>
<%@ include file="prjbasic_fulltext_remark.jsp"%>
