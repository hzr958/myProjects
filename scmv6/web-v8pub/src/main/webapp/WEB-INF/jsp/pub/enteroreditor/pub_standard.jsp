
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>标准</title>
<%@ include file="pub_head_res.jsp"%>
<script type="text/javascript">
  $(function() {
    // 标准类型:改变标准类型，同时对界面进行微调
    var standardType = $("#standardType").val();
    if (standardType == "" ) {
      standardType = "INTERNATIONAL";
      $("#standardType").val(standardType);
    }
    PubEdit.changeStandardType(standardType);
    // 设置选中框中的显示信息
    PubEdit.setStandardShowName(standardType);
    //行业弹出框
    Multistagescienceselectoe("industryBox",'${locale}');
  });
</script>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--标准--%>
    <div class="handin_import-container">
      <!-- 成果头部信息  begin-->
      <%@ include file="pub_head_info.jsp"%>
      <!-- 成果头部信息  end-->
      <div class="handin_import-content_container">
        <!-- 成果类型  begin-->
        <%@ include file="pub_type.jsp"%>
        <!-- 成果类型  end-->
        <!-- 成果全文  begin-->
        <%@ include file="pub_fulltext.jsp"%>
        <!-- 成果全文  end-->
      </div>
      <!-- 成果标题begin-->
      <div class="handin_import-content_container-center">
        <div class="handin_import-content_container-left">
          <span class="handin_import-content_container-tip">*</span> <span><spring:message
              code="pub.enter.standard.title" />:</span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_input error_import-tip_border">
            <input type="text" class="dev-detailinput_container json_title" maxlength="2000" autocomplete="off"
              title_msg="<spring:message code="pub.enter.standard.title"/>" id="title" name="title"
              value="${pubVo.title}">
          </div>
          <div class="json_title_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 成果标题end-->
      <!-- 标准类型 begin -->
      <div class="handin_import-content_container-around">
        <div class="handin_import-content_container-left">
          <!-- <spring:message code="pub.enter.pulishDate" /> -->
          <span class="handin_import-content_container-tip">*</span>
          <spring:message code="pub.enter.standard.type" />
          :<span></span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_select">
            <div class="handin_import-content_container-right_select-box">
              <input type="text" class="dev-detailinput_container dev_standardtype_input" id="showStandardTypeName"
                readonly onfocus="this.blur()" unselectable="on" value="" />
            </div>
            <input type="hidden" id="standardType" name="standardType" value="${pubVo.pubTypeInfo.type}"
              class="json_standard_type"> <i
              class="material-icons handin_import-content_container-right_select-tip dev_standardtype"
              style="padding-right: 0px !important;">arrow_drop_down</i>
            <div class="handin_import-content_container-right_select-detail">
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="INTERNATIONAL"><spring:message
                    code="pub.enter.standard.type.international" /></span>
              </div>
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="NATION_FORCE"><spring:message
                    code="pub.enter.standard.type.nationForce" /></span>
              </div>
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="NATION_RECOMMENDED"><spring:message
                    code="pub.enter.standard.type.nationRecommended" /></span>
              </div>
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="INDUSTRIAL_FORCE"><spring:message
                    code="pub.enter.standard.type.industrualForce" /></span>
              </div>
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="INDUSTRIAL_RECOMMENDED"><spring:message
                    code="pub.enter.standard.type.industrualRecommended" /></span>
              </div>
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="LOCAL"><spring:message
                    code="pub.enter.standard.type.local" /></span>
              </div>
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="ENTERPRISE"><spring:message
                    code="pub.enter.standard.type.enterprise" /></span>
              </div>
              <div class="handin_import-content_container-right_select-item dev_standard_type">
                <span class="handin_import-content_container-right_select-item_detail" value="OTHER"><spring:message
                    code="pub.enter.standard.type.other" /></span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 标准类型 end -->
      <input type="hidden" value="${pubVo.pubTypeInfo.obsoleteDate}" class="json_standard_obsoleteDate">
      <input type="hidden" value="${pubVo.pubTypeInfo.implementDate}" class="json_standard_implementDate">
      <input type="hidden" value="${pubVo.pubTypeInfo.icsNo}" class="json_standard_icsNo">
      <input type="hidden" value="${pubVo.pubTypeInfo.domainNo}" class="json_standard_domainNo">
      <!-- 标准号 begin -->
      <div class="handin_import-content_container-around">
        <div class="handin_import-content_container-left">
          <span class="handin_import-content_container-tip">*</span>
          <spring:message code="pub.enter.standard.number" />
          :<span></span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_input error_import-tip_border">
            <input type="text" class="dev-detailinput_container json_standard_standardNo" maxlength="65"
              autocomplete="off" title_msg='<spring:message code="pub.enter.standard.number"/>' id="standard_standardNo"
              name="standard_standardNo" value="${pubVo.pubTypeInfo.standardNo}">
          </div>
          <div class="json_standard_standardNo_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 标准号 end -->
      <!-- 公布机构 begin -->
      <div class="handin_import-content_container-around" id="standard_publishAgency">
        <div class="handin_import-content_container-left">
          <span class="handin_import-content_container-tip">*</span>
          <spring:message code="pub.enter.standard.publishAgency" />
          :<span></span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_input error_import-tip_border">
            <input type="text" class="dev-detailinput_container json_standard_publishAgency" maxlength="95"
              autocomplete="off" title_msg='<spring:message code="pub.enter.standard.publishAgency"/>'
              name="standard_publishAgency" value="${pubVo.pubTypeInfo.publishAgency}">
          </div>
          <div class="json_standard_publishAgency_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 公布机构 end -->
      <!-- 归口单位 begin -->
      <div class="handin_import-content_container-around" id="standard_technicalCommittees">
        <div class="handin_import-content_container-left">
          <span class="handin_import-content_container-tip">*</span>
          <spring:message code="pub.enter.standard.technicalCommittees" />
          :<span></span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_input error_import-tip_border">
            <input type="text" class="dev-detailinput_container json_standard_technicalCommittees" maxlength="50"
              autocomplete="off" title_msg='<spring:message code="pub.enter.standard.technicalCommittees"/>'
              name="standard_technicalCommittees" value="${pubVo.pubTypeInfo.technicalCommittees}">
          </div>
          <div class="json_standard_technicalCommittees_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 归口单位 end -->
      <!-- 发布日期  begin-->
      <div class="handin_import-content_container-around" style="align-items: flex-start;">
        <div class="handin_import-content_container-left" style="margin-top: 6px;">
          <!-- <spring:message code="pub.enter.pulishDate" /> -->
          <span class="handin_import-content_container-tip">*</span>
          <spring:message code="pub.enter.standard.publishDate" />
          :<span></span>
        </div>
        <div class="handin_import-content_container-right_state"
          style="display: flex; flex-direction: column; align-items: flex-start; height: auto; border: none;">
          <div class="handin_import-content_container-right_area "
            style="width: 95%; margin-left: 12px; height: 30px; border: 1px solid #ddd; border-radius: 3px;">
            <div
              class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input error_import-tip_border"
              style="border-radius: 3px;">
              <input class="json_publishDate dev-detailinput_container" itemevent="callbackDate" type="text"
                name="publishDate" id="publishDate" unselectable="on" onfocus="this.blur()" readonly datepicker
                date-format="yyyy-mm-dd" value='<iris:dateFormat dateStr="${pubVo.publishDate }" splitChar="-"/>' />
            </div>
          </div>
          <div class="json_publishDate_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 发表日期 end-->
      <!-- doi  begin -->
      <div class="handin_import-content_container-center">
        <div class="handin_import-content_container-left">
          <span style="color: red;"></span><span>DOI:</span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_input">
            <input type="text" class="dev-detailinput_container json_doi" maxlength="100" name="doi"
              value="${pubVo.doi}">
          </div>
          <div class="json_doi_msg" style="display: none"></div>
        </div>
      </div>
      <!-- doi  end -->
      <!-- 剩下的摘要、作者等公共部分 begin -->
      <%@ include file="author_and_other_common.jsp"%>
      <!-- 剩下的摘要、作者等公共部分 end -->
    </div>
  </form>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0">
    <!-- 科技领域弹出框 -->
  </div>
</body>
</html>