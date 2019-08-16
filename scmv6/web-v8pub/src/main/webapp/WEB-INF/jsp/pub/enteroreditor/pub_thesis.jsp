<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>成果编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="pub_head_res.jsp"%>
<script type="text/javascript">
$(function(){
    //设置radio
    var type = $(".json_thesis_degree").val();
    if(type==""){
    	$(".json_thesis_degree").val("OTHER");
	    type = "OTHER";
    }
    $(".dev_thesis_degree[value='"+type+"']").removeClass("selected-oneself").addClass("selected-oneself_confirm");

    //设置radio
});
</script>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--学位论文--%>
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
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.title" />:</span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container json_title" autocomplete="off"
                title_msg="<spring:message code="pub.enter.title"/>" maxlength="2000" id="title" name="title"
                value="${pubVo.title }">
            </div>
            <div class="json_title_msg" style="display: none">必选字段</div>
          </div>
        </div>
        <div class="handin_import-content_container-around" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.degree" /></span>
          </div>
          <div class="handin_import-content_container-right_state" style="justify-content: space-between; width: 876px;">
            <!-- 学位 begin-->
            <div style="display: flex; flex-direction: column; align-items: flex-start; width: 395px;">
              <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
                <div class="handin_import-content_container-right_collect-item" style="margin: 0px 0px;">
                  <i class="selected-oneself dev_thesis_degree" value="MASTER"></i><span
                    class="selected-author_confirm-detaile"><spring:message code="pub.enter.MASTER" /></span>
                </div>
                <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px;">
                  <i class="selected-oneself dev_thesis_degree" value="DOCTOR"></i><span
                    class="selected-author_confirm-detaile"><spring:message code="pub.enter.DOCTOR" /></span>
                </div>
                <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px;">
                  <i class="selected-oneself dev_thesis_degree" value="OTHER"></i><span
                    class="selected-author_confirm-detaile"><spring:message code="pub.enter.OTHER" /></span>
                </div>
                <input type="hidden" class="json_thesis_degree" name="degree" value="${pubVo.pubTypeInfo.degree }">
              </div>
              <div class="json_thesis_degree_msg" style="margin-left: 12px; display: none"></div>
            </div>
            <!-- 学位end-->
            <div class="handin_import-content_container-right_state-sub" style="width: 454px;">
              <!-- 答辩日期  begin-->
              <div class="handin_import-content_container-right_sub-area" style="width: 142px;">
                <span class="handin_import-content_container-tip">*</span> <span><spring:message
                    code="pub.enter.defenseDate" /></span>
              </div>
              <div class="handin_import-content_container-right_area"
                style="width: 89%; border: none; flex-direction: column; align-items: flex-start; height: auto;">
                <div class="handin_import-content_container-right_area-content input__box error_import-tip_border"
                  style="border: 1px solid #ddd; width: 100% !important; height: 28px; border-radius: 3px;">
                  <input class="json_thesis_defenseDate dev-detailinput_container" itemevent="callbackDate"
                    style="width: 97% !important; padding-right: 0px;" type="text" name="defenseDate" id="defenseDate"
                    unselectable="on" onfocus="this.blur()" readonly datepicker date-format="yyyy-mm-dd"
                    value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.defenseDate }" splitChar="-"/>' />
                </div>
                <div class="json_thesis_defenseDate_msg" style="display: none"></div>
              </div>
              <!-- 答辩日期 end-->
            </div>
          </div>
        </div>
        <div class="handin_import-content_container-center">
          <!-- 颁发单位begin-->
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.thesisAuthority" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                <input type="text" class="dev-detailinput_container json_thesis_issuingAuthority js_autocompletebox"
                  request-data="PubEdit.getAutoCompleteJson('thesisOrg');" request-url="/psnweb/ac/ajaxgetComplete"
                  maxlength="100" id="issuingAuthority" name="issuingAuthority"
                  value="${pubVo.pubTypeInfo.issuingAuthority }">
              </div>
              <div class="json_thesis_issuingAuthority_msg" style="display: none"></div>
            </div>
            <!-- 专业部门begin-->
            <div class="handin_import-content_container-right_sub-area">
              <span class="handin_import-content_container-tip">*</span> <span><spring:message
                  code="pub.enter.department" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                <input type="text" class="dev-detailinput_container json_thesis_department" maxlength="100"
                  id="department" name="department" value="${pubVo.pubTypeInfo.department }">
              </div>
              <div class="json_thesis_department_msg" style="display: none"></div>
            </div>
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
        <!-- 成果引用数  begin-->
        <%@ include file="pub_cited_times.jsp"%>
        <!-- 成果引用数  end-->
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
    process-time="0"></div>
  <!-- 科技领域弹出框 -->
</body>
</html>