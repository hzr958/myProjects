<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>成果编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="pub_head_res.jsp"%>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--其他--%>
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
        <!-- 成果标题begin-->
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.title" /></span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container json_title" maxlength="2000" autocomplete="off"
                title_msg="<spring:message code="pub.enter.title"/>" id="title" name="title" value="${pubVo.title}">
            </div>
            <div class="json_title_msg" style="display: none"></div>
          </div>
          <!-- 成果标题end-->
        </div>
        <div class="handin_import-content_container-around" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.pulishDate" /></span>
          </div>
          <div class="handin_import-content_container-right_state"
            style="display: flex; flex-direction: column; align-items: flex-start; height: auto; border: none;">
            <!-- 发表日期  begin-->
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
            <!-- 发表日期 end-->
            <div class="json_publishDate_msg" style="display: none"></div>
          </div>
        </div>
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.countryArea" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <!-- 国家或地区  begin-->
            <%@ include file="pub_country_or_area.jsp"%>
            <!-- 国家或地区  end-->
          </div>
        </div>
        <!-- 成果资助标注  begin-->
        <%@ include file="pub_funding_annotation.jsp"%>
        <!-- 成果资助标注  end-->
        <!-- 引用数  begin-->
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span><spring:message code="pub.enter.citations" /></span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container full_width json_citations" maxlength="5"
                name="downloadTimes" value="${pubVo.citations}">
            </div>
            <div class="json_citations_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 引用数  end-->
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
        <!-- 剩下的摘要、作者等公共部分 begin -->
        <%@ include file="author_and_other_common.jsp"%>
        <!-- 剩下的摘要、作者等公共部分 end -->
      </div>
    </div>
  </form>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0">
    <!-- 科技领域弹出框 -->
  </div>
</body>
</html>