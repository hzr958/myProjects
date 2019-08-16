<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>软件著作权</title>
<%@ include file="pub_head_res.jsp"%>
<script type="text/javascript">
  $(function() {

    // 权利获得方式
    var acquisitionType = $(".json_softwarecopyright_acquisitionType").val();
    if (acquisitionType == "" || acquisitionType == "NULL") {
      acquisitionType = "ORIGINAL";
      $(".json_softwarecopyright_acquisitionType").val(acquisitionType);
    }
    $(".dev_sc_acquisitionType[value='" + acquisitionType + "']").removeClass("selected-oneself").addClass(
        "selected-oneself_confirm");

    // 权利范围
    var scopeType = $(".json_softwarecopyright_scopeType").val();
    if (scopeType == "" || scopeType == "NULL") {
      scopeType = "ALL";
      $(".json_softwarecopyright_scopeType").val(scopeType);
    }
    $(".dev_sc_scopeType[value='" + scopeType + "']").removeClass("selected-oneself").addClass(
        "selected-oneself_confirm");
    // 行业弹出框
    Multistagescienceselectoe("industryBox",'${locale}');
  });
</script>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--软件著作权--%>
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
              code="pub.enter.softwarecopyright.title" />:</span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_input error_import-tip_border">
            <input type="text" class="dev-detailinput_container json_title" maxlength="2000" autocomplete="off"
              title_msg="<spring:message code="pub.enter.softwarecopyright.title"/>" id="title" name="title"
              value="${pubVo.title}">
          </div>
          <div class="json_title_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 成果标题end-->
      <!-- 登记号 begin-->
      <div class="handin_import-content_container-center">
        <div class="handin_import-content_container-left">
          <span class="handin_import-content_container-tip">*</span> <span><spring:message
              code="pub.enter.register.number" />:</span>
        </div>
        <div class="handin_import-content_container-right">
          <div class="handin_import-content_container-right_input error_import-tip_border">
            <input type="text" class="dev-detailinput_container json_softwarecopyright_registerNo" maxlength="100"
              autocomplete="off" title_msg="<spring:message code="pub.enter.register.number"/>" id="registerNo"
              name="registerNo" value="${pubVo.pubTypeInfo.registerNo}">
          </div>
          <div class="json_softwarecopyright_registerNo_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 登记号end-->
      <input type="hidden" value="${pubVo.pubTypeInfo.categoryNo}" class="json_softwarecopyright_categoryNo"> <input
        type="hidden" value="${pubVo.pubTypeInfo.publicityDate}" class="json_softwarecopyright_publicityDate"> <input
        type="hidden" value="${pubVo.pubTypeInfo.firstPublishDate}" class="json_softwarecopyright_firstPublishDate">
      <!-- 权利获得方式 begin -->
      <div class="handin_import-content_container-around">
        <div class="handin_import-content_container-left">
          <span class="handin_import-content_container-tip">*</span> <span><spring:message
              code="pub.enter.acquisitionType" />:</span>
        </div>
        <div class="handin_import-content_container-right_collect "
          style="margin-right: 12px; justify-content: flex-start;">
          <div class="handin_import-content_container-right_collect-item dev_acquisitionType_ORIGINAL"
            style="margin: 0px 20px 0px 0px;">
            <i class="selected-oneself dev_sc_acquisitionType" value="ORIGINAL"></i> <span
              class="selected-author_confirm-detaile"><spring:message code="pub.enter.acquisitionType.Original" /></span>
          </div>
          <div class="handin_import-content_container-right_collect-item dev_acquisitionType_DERIVATIVE"
            style="margin: 0px 20px;">
            <i class="selected-oneself dev_sc_acquisitionType" value="DERIVATIVE"></i> <span
              class="selected-author_confirm-detaile"><spring:message code="pub.enter.acquisitionType.Derivative" /></span>
          </div>
        </div>
        <input type="hidden" class="json_softwarecopyright_acquisitionType" name="acquisitionType"
          value="${pubVo.pubTypeInfo.acquisitionType }" />
      </div>
      <!-- 权利获得方式 end -->
      <!-- 权利范围 begin -->
      <div class="handin_import-content_container-around">
        <div class="handin_import-content_container-left">
          <span class="handin_import-content_container-tip">*</span> <span><spring:message
              code="pub.enter.scopeType" />:</span>
        </div>
        <div class="handin_import-content_container-right_collect "
          style="margin-right: 12px; justify-content: flex-start;">
          <div class="handin_import-content_container-right_collect-item dev_scopetype_ALL"
            style="margin: 0px 20px 0px 0px;">
            <i class="selected-oneself dev_sc_scopeType" value="ALL"></i> <span class="selected-author_confirm-detaile"><spring:message
                code="pub.enter.scopeType.all" /></span>
          </div>
          <div class="handin_import-content_container-right_collect-item dev_scopetype_PART" style="margin: 0px 20px;">
            <i class="selected-oneself dev_sc_scopeType" value="PART"></i> <span class="selected-author_confirm-detaile"><spring:message
                code="pub.enter.scopeType.part" /></span>
          </div>
        </div>
        <input type="hidden" class="json_softwarecopyright_scopeType" name="scopeType"
          value="${pubVo.pubTypeInfo.scopeType }" />
      </div>
      <!-- 权利范围 end -->
      <!-- 发表日期  begin-->
      <div class="handin_import-content_container-around" style="align-items: flex-start;">
        <div class="handin_import-content_container-left" style="margin-top: 6px;">
          <!--  -->
          <span class="handin_import-content_container-tip">*</span> <span><spring:message
              code="pub.enter.sc.pulishDate" />:</span>
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
      <!-- 登记日期  begin-->
      <div class="handin_import-content_container-around" style="align-items: flex-start;">
        <div class="handin_import-content_container-left" style="margin-top: 6px;">
          <span class="handin_import-content_container-tip">*</span>
          <spring:message code="pub.enter.sc.registerDate" />
          :<span></span>
        </div>
        <div class="handin_import-content_container-right_state"
          style="display: flex; flex-direction: column; align-items: flex-start; height: auto; border: none;">
          <div class="handin_import-content_container-right_area "
            style="width: 95%; margin-left: 12px; height: 30px; border: 1px solid #ddd; border-radius: 3px;">
            <div
              class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input error_import-tip_border"
              style="border-radius: 3px;">
              <input class="json_softwarecopyright_registerDate dev-detailinput_container" itemevent="callbackDate"
                type="text" unselectable="on" onfocus="this.blur()" readonly datepicker date-format="yyyy-mm-dd"
                value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.registerDate }" splitChar="-"/>' />
            </div>
          </div>
          <div class="json_softwarecopyright_registerDate_msg" style="display: none"></div>
        </div>
      </div>
      <!-- 登记日期 end-->
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