<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="pub_head_res.jsp"%>
<title>科研之友</title>
<script type="text/javascript">
$(function(){
    //设置radio
    var type = $(".json_conferencePaper_paperType").val();
    if(type==""){
        type="INVITED";
        $(".json_conferencePaper_paperType").val(type);
    }
    $(".dev_conferencePaper_paperType[value='"+type+"']").removeClass("selected-oneself").addClass("selected-oneself_confirm");

    //设置radio
});

function callbackJournal(obj,otherStr){
	//alert(otherStr);
  if(otherStr && otherStr!=""){
      var other =  JSON.parse(otherStr);
      var $parent = $(obj).closest(".json_journal");
      //SCM-19844
      //$parent.find(".json_journal_jid").val(other.code);
      $parent.find(".json_journal_issn").val(other.issn);
  }
};

</script>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--会议论文--%>
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
        <div class="handin_import-content_container-around">
          <!-- 会议类型 begin-->
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.paperType" /></span>
          </div>
          <div class="handin_import-content_container-right_collect"
            style="margin-right: 12px; justify-content: flex-start;">
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 20px 0px 0px;">
              <i class="selected-oneself dev_conferencePaper_paperType" value="INVITED"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.invited" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 20px;">
              <i class="selected-oneself dev_conferencePaper_paperType" value="GROUP"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.oral" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 20px;">
              <i class="selected-oneself dev_conferencePaper_paperType" value="POSTER"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.poster" /></span>
            </div>
            <input type="hidden" class="json_conferencePaper_paperType" name="paperType"
              value="${pubVo.pubTypeInfo.paperType }" />
          </div>
          <!-- 会议类型 end-->
        </div>
        <!-- 成果标题  begin-->
        <div class="handin_import-content_container-center json_journal" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.title" />:</span>
          </div>
          <div class="handin_import-content_container-right">
            <div
              class="handin_import-content_container-right_input  dev-detailinput_container-input error_import-tip_border">
              <input type="text" class="dev-detailinput_container full_width json_title js_autocompletebox"  autocomplete="off"
                title_msg="<spring:message code="pub.enter.title"/>"
                request-data="PubEdit.getAutoCompleteJson('conferenceTitle');" request-url="/psnweb/ac/ajaxgetComplete"
                other-event="callbackJournal" maxlength="2000" name="title" value="${pubVo.title }" /> <input
                type="hidden" class="json_journal_jid" value="" /> <input type="hidden" class="json_journal_issn"
                value="" />
            </div>
            <div class="json_title_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 成果标题  end-->
        <!-- 会议名称  begin-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.conferenceName" /></span>
          </div>
          <div class="handin_import-content_container-right">
            <div
              class="handin_import-content_container-right_input  dev-detailinput_container-input error_import-tip_border">
              <input type="text"
                class="dev-detailinput_container full_width json_conferencePaper_name" 
                maxlength="500" name="name" value="${pubVo.pubTypeInfo.name }" />
            </div>
            <div class="json_conferencePaper_name_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 会议名称  end-->
        <!-- 会议组织者  begin-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span style="color: red;"></span><span><spring:message code="pub.enter.paperOrganizer" /></span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input  dev-detailinput_container-input">
              <input type="text"
                class="dev-detailinput_container full_width json_conferencePaper_organizer js_autocompletebox"  autocomplete="off"
                request-data="PubEdit.getAutoCompleteJson('confOrganizer');" request-url="/psnweb/ac/ajaxgetComplete"
                maxlength="50" name="organizer" value="${pubVo.pubTypeInfo.organizer }" />
            </div>
            <div class="json_conferencePaper_organizer_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 会议组织者  end-->
        <div class="handin_import-content_container-around">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.countryArea" /></span>
          </div>
          <div class="handin_import-content_container-right_state">
            <!-- 国家或地区  begin-->
            <div class="dev-select_contry" style="padding-left: 12px;">
              <div class="handin_import-content_container-right_contry">
                <%@ include file="pub_country_or_area.jsp"%>
              </div>
            </div>
            <!-- 国家或地区  end-->
          </div>
        </div>
        
         <!-- 发表日期  begin-->
         <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
               <span class="handin_import-content_container-tip">*</span> 
               <span><spring:message code="pub.enter.paperPublishDate" /></span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input error_import-tip_border"
                  style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 80% !important;">
                  <input class="json_publishDate dev-detailinput_container" itemevent="callbackDate" type="text"
                    name="publishDate" id="publishDate" onfocus="this.blur()" readonly unselectable="on" datepicker
                    date-format="yyyy-mm-dd" value='<iris:dateFormat dateStr="${pubVo.publishDate }" splitChar="-"/>' />
                </div>
                <div class="json_publishDate_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 发表日期 end-->

        <!-- 论文集名 begin-->
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span><spring:message code="pub.enter.paperContainer" /></span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input  dev-detailinput_container-input">
              <input type="text" class="dev-detailinput_container full_width json_conferencePaper_papers"
                maxlength="200" name="papers" value="${pubVo.pubTypeInfo.papers }" />
            </div>
            <div class="json_conferencePaper_papers_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 论文集名 end-->
        <!-- 会议日期和起止页码  begin-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.meetingDate" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div style="display: flex; flex-direction: column; width: 430px;">
              <div style="display: flex; align-items: center;">
                <div class="handin_import-content_container-right_area" style="width: 45%;">
                  <div
                    class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input error_import-tip_border">
                    <input class="json_conferencePaper_startDate dev-detailinput_container" itemevent="callbackDate"
                      type="text" style="padding-right: 8px;" name="startDate" id="startDate" unselectable="on"
                      onfocus="this.blur()" readonly datepicker date-format="yyyy-mm-dd"
                      value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.startDate }" splitChar="-"/>' />
                  </div>
                </div>
                <span style="margin: 0px 12px;"><spring:message code="pub.enter.zhi" /></span>
                <div class="handin_import-content_container-right_area  error_import-tip_border" style="width: 45%;">
                  <div
                    class="handin_import-content_container-right_area-content input__box dev-detailinput_container-input error_import-tip_border">
                    <input class="json_conferencePaper_endDate dev-detailinput_container" itemevent="callbackDate"
                      type="text" style="padding-right: 8px;" name="endDate" id="endDate" readonly unselectable="on"
                      onfocus="this.blur()" datepicker date-format="yyyy-mm-dd"
                      value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.endDate }" splitChar="-"/>' />
                  </div>
                </div>
              </div>
              <div class="json_conferencePaper_startDate_msg json_conferencePaper_endDate_msg" style="display: none"></div>
            </div>
            <div class="handin_import-content_container-right_sub-area" style="width: 160px; margin-left: 9px;">
              <span style="color: red;"></span><span><spring:message code="pub.enter.startPageAndEndPage" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="height: auto; border: none; flex-direction: column; align-items: flex-start; width: 225px">
              <div
                class="handin_import-content_container-right_area-content  dev-detailinput_container-input handin_import-content_rightbox-border error_import-tip_border">
                <input type="text" class="dev-detailinput_container json_conferencePaper_pageNumber" maxlength="17"
                  id="pageNumber" name="" placeholder='<spring:message code="pub.enter.pageNumberMsg"/>'
                  title='<spring:message code="pub.enter.pageNumberMsg"/>' value="${pubVo.pubTypeInfo.pageNumber }">
              </div>
            </div>
          </div>
        </div>
        <!-- 会议日期和起止页码  end-->
        <!-- 成果资助标注  begin-->
        <%@ include file="pub_funding_annotation.jsp"%>
        <!-- 成果资助标注  end-->
        <!-- 成果收录情况  begin-->
        <div class="handin_import-content_container-around">
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span><spring:message code="pub.enter.sitations" /></span>
          </div>
          <div class="handin_import-content_container-right_collect">
            <!-- 补充收录丢失的字段 -->
            <input type="hidden" class="json_sourceUrl" value="${pubVo.sourceUrl }"> <input type="hidden"
              class="json_sourceId" value="${pubVo.sourceId }"> <input type="hidden" class="json_srcDbId"
              value="${pubVo.srcDbId }">
            <c:forEach items="${pubVo.situations}" var="sit">
              <div class="handin_import-content_container-right_collect-item json_situation" value="${sit.libraryName }">
                <input type="hidden" class="json_situation_libraryName" name="libraryName" value="${sit.libraryName }">
                <c:choose>
                  <c:when test="${sit.sitStatus}">
                    <i class="selected-author_confirm"></i>
                    <input type="hidden" class="json_situation_sitStatus" name="sitStatus" value="true">
                    <input type="hidden" class="json_situation_sitOriginStatus" name="sitOriginStatus"
                      value="${sit.sitOriginStatus}">
                    <input type="hidden" class="json_situation_srcUrl" name="srcUrl" value="${sit.srcUrl}">
                    <input type="hidden" class="json_situation_srcDbId" name="srcDbId" value="${sit.srcDbId}">
                    <input type="hidden" class="json_situation_srcId" name="srcId" value="${sit.srcId}">
                  </c:when>
                  <c:otherwise>
                    <i class="selected-author"></i>
                    <input type="hidden" class="json_situation_sitStatus" name="sitStatus" value="false">
                    <input type="hidden" class="json_situation_sitOriginStatus" name="sitOriginStatus" value="false">
                  </c:otherwise>
                </c:choose>
                <span class="selected-author_confirm-detaile">${sit.showName }</span>
              </div>
            </c:forEach>
          </div>
        </div>
        <!-- 成果收录情况  end-->
        <!-- 成果引用数  begin-->
        <%@ include file="pub_cited_times.jsp"%>
        <!-- 成果引用数  end-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span style="color: red;"></span><span>DOI:</span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input  dev-detailinput_container-input">
              <input type="text" class="dev-detailinput_container full_width json_doi" maxlength="100" name="doi"
                value="${pubVo.doi }" />
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
  <form action="/pub/autoFillPub" method="post" id="autoFillPubForm">
    <input type="hidden" id="des3pdwhPubId" name="des3pdwhPubId" value="${des3pdwhPubId }" /> <input type="hidden"
      name="des3PubId" value="${pubVo.des3PubId }" /> <input type="hidden" name="des3GrpId" value="${pubVo.des3GrpId }" />
    <input type="hidden" name="isProjectPub" value="${pubVo.isProjectPub }" /> <input type="hidden" id="membersJsonStr"
      name="membersJsonStr" value="" /> <input type="hidden" id="des3FileId" name="des3FileId" value="" />
  </form>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0"></div>
  <!-- 科技领域弹出框 -->
</body>
</html>