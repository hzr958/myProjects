<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="pub_head_res.jsp"%>
<title>科研之友</title>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--奖励--%>
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
        <!-- 成果标题  begin-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.awardName" />:</span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container json_title" autocomplete="off"
                title_msg="<spring:message code="pub.enter.awardName"/>" maxlength="2000" name="title"
                value="${pubVo.title }" />
            </div>
            <div class="json_title_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 成果标题  end-->
        
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> 
            <span><spring:message code="pub.enter.countryArea" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <!-- 国家或地区  begin-->
            <%@ include file="pub_country_or_area.jsp"%>
            <!-- 国家或地区  end-->
          </div>
        </div>
        
        
        <div class="handin_import-content_container-center"  style="align-items: flex-start;">
        <!-- 授奖机构  begin-->
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span>
              <span><spring:message code="pub.enter.issuingAuthority" /></span>
          </div>
          <div class="handin_import-content_container-right">
             <div class="handin_import-content_container-right_input error_import-tip_border">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border"
                style="border: 1px solid #ddd; border-radius: 3px; height: 28px; width: 100% !important;">
                <input type="text"
                  class="dev-detailinput_container full_width json_award_issuingAuthority"
                  maxlength="75" name="issuingAuthority" value="${pubVo.pubTypeInfo.issuingAuthority }" />
              </div>
              <div class="json_award_issuingAuthority_msg" style="display: none"></div>
            </div> 
          </div>
          <!-- 授奖机构  end-->
        </div>
        
        
        
        <div class="handin_import-content_container-around">
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span><spring:message code="pub.enter.certificateNo" /></span>
          </div>
          <div class="handin_import-content_container-right_state" style="padding-left: 12px;">
            <!-- 证书编号  begin-->
            <div class="handin_import-content_container-right_area"
              style="width: 329px; border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border"
                style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 329px !important;">
                <input type="text" class="dev-detailinput_container full_width json_award_certificateNo" maxlength="50"
                  name="certificateNo" value="${pubVo.pubTypeInfo.certificateNo }" />
              </div>
              <div class="json_award_certificateNo_msg" style="display: none"></div>
            </div>
            <!-- 证书编号  end-->
            <div class="handin_import-content_container-right_state-sub"
              style="margin: 20px 41px; justify-content: flex-end;">
              <!-- 授奖日期begin-->
              <div class="handin_import-content_container-right_sub-area" style="width: 141px;">
                <span class="handin_import-content_container-tip">*</span> <span><spring:message
                    code="pub.enter.awardDate" /></span>
              </div>
              <div class="handin_import-content_container-right_area"
                style="width: 315px; border: none; flex-direction: column; align-items: flex-start; height: auto;">
                <div
                  class="handin_import-content_container-right_area-content input__box  error_import-tip_border  dev-detailinput_container-input"
                  style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 100% !important;">
                  <input class="json_award_awardDate dev-detailinput_container" itemevent="callbackDate" type="text"
                    name="awardDate" id="awardDate" readonly onfocus="this.blur()" unselectable="on" datepicker
                    date-format="yyyy-mm-dd"
                    value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.awardDate }" splitChar="-"/>' />
                </div>
                <div class="json_award_awardDate_msg" style="display: none"></div>
              </div>
              <!-- 授奖日期end-->
            </div>
          </div>
        </div>
        <div class="handin_import-content_container-center">
          <!-- 奖励等级begin-->
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span><span><spring:message
                code="pub.enter.grade" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border"
                style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 329px !important;">
                <input type="text" class="dev-detailinput_container full_width json_award_grade js_autocompletebox"
                  none-request="true" itemevent="callbackGrade"
                  request-data="PubEdit.getAutoCompleteJson('awardGrade');" request-url="/psnweb/ac/ajaxgetComplete"
                  maxlength="50" name="grade" value="${pubVo.pubTypeInfo.grade }" />
              </div>
              <div class="json_award_grade_msg" style="display: none"></div>
            </div>
            <!-- 奖励等级end-->
            <!-- 奖励种类begin-->
            <div class="handin_import-content_container-right_sub-area">
              <span class="handin_import-content_container-tip">*</span> <span><spring:message
                  code="pub.enter.awardcategory" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border"
                style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 100% !important;">
                <input type="text" class="dev-detailinput_container full_width json_award_category js_autocompletebox"
                  none-request="true" itemevent="callbackCategory"
                  request-data="PubEdit.getAutoCompleteJson('awardCategory');" request-url="/psnweb/ac/ajaxgetComplete"
                  maxlength="50" name="category" value="${pubVo.pubTypeInfo.category }" />
              </div>
              <div class="json_award_category_msg" style="display: none"></div>
            </div>
            <!-- 奖励种类end-->
          </div>
        </div>
        <!-- 成果资助标注  begin-->
        <%@ include file="pub_funding_annotation.jsp"%>
        <!-- 成果资助标注  end-->
        <!-- 剩下的摘要、作者等公共部分 begin -->
        <%@ include file="author_and_other_common.jsp"%>
        <!-- 剩下的摘要、作者等公共部分 end -->
      </div>
    </div>
  </form>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0"></div>
  <!-- 科技领域弹出框 -->
</body>
</html>