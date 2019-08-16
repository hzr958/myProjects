<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="pub_head_res.jsp"%>
<title>科研之友</title>
<script type="text/javascript">
var yifabiao = '<spring:message code="pub.enter.yifabiao"/>';
var weifabiao =	'<spring:message code="pub.enter.weifabiao"/>';
var publishDate = '<spring:message code="pub.enter.pulishDate"/>';
var acceptDate = '<spring:message code="pub.enter.acceptDate"/>';

$(function(){
	removeHtmlTags();
	var status = $("#publishStatus").val();//设置发表状态
	if(status == "A"){
        $("#publishStatusInput").val(weifabiao);
        $("#publishDate_span").html(acceptDate);
	}else{
		$("#publishStatusInput").val(yifabiao);
		$("#publishDate_span").html(publishDate);
	}
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


function changeDateText(publishStatus){
    if(publishStatus == "A"){
        $("#publishDate_span").html(acceptDate);
    }else{
        $("#publishDate_span").html(publishDate);
    }
}

//去除标题和摘要中的html标签，这个将会导致一些html中表示指数的标签失效如<sup><sub>标签，之后会将标题输入栏改造为支持html类型的,将不再需要这个方法
function removeHtmlTags(){
	var reg1=/<sup>/g;//匹配出所有数学指数标签
	var titleObj=$("input[name='title']");
	var summaryObj=$("textarea[name='summary']");
	var title=titleObj.val().replace(reg1,"^");
    title = BaseUtils.replaceHtml(title);
	var summary=summaryObj.val().replace(reg1,"^")
    summary = BaseUtils.replaceHtml(summary);
	titleObj.val(title);
	summaryObj.val(summary);
}

</script>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--期刊论文--%>
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
        <div class="handin_import-content_container-center json_journal" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.title" />:</span>
          </div>
          <!--  <input type="hidden" class="json_journal_jid" value="" />
            <input type="hidden" class="json_journal_issn" value="" /> -->
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border ">
              <input type="text" id="pubAcTitle" class="dev-detailinput_container json_title js_autocompletebox" autocomplete="off" 
                title_msg="<spring:message code="pub.enter.title"/>"
                request-data="PubEdit.getAutoCompleteJson('journalTitle');" request-url="/psnweb/ac/ajaxgetComplete"
                other-event="callbackJournal" maxlength="2000" name="title" value="${pubVo.title }" />
            </div>
            <div class="json_title_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 成果标题  end-->
        <!-- 成果期刊名称  begin-->
        <div class="handin_import-content_container-center json_journal" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.journalName" /></span>
          </div>
          <input type="hidden" class="json_journal_jid" value="" /> <input type="hidden" class="json_journal_issn"
            value="" />
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container json_journal_name js_autocompletebox" autocomplete="off"
                onchange="PubEdit.changeJournalName(this);" title_msg="<spring:message code="pub.enter.journalName"/>"
                request-data="PubEdit.getAutoCompleteJson('journal');" request-url="/psnweb/ac/ajaxgetComplete"
                other-event="callbackJournal" maxlength="250" name="name" value="${pubVo.pubTypeInfo.name }" />
            </div>
            <div class="json_journal_name_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 成果期刊名称  end-->
        <div class="handin_import-content_container-around">
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span><spring:message code="pub.enter.journalStatus" /></span>
          </div>
          <div class="handin_import-content_container-right_state">
            <!-- 成果状态  begin-->
            <div
              class=" handin_import-content_container-right_select handin_import-content_container-right_select-container">
              <div class="handin_import-content_container-right_select-box">
                <input type="text" class="dev-detailinput_container dev_select_input" readonly unselectable="on"
                  onfocus="this.blur()" name="publishStatusInput" id="publishStatusInput" /> <input type="hidden"
                  class="json_journal_publishStatus" id="publishStatus" name="publishStatus"
                  value="${pubVo.pubTypeInfo.publishStatus}" />
              </div>
              <i class="material-icons handin_import-content_container-right_select-tip">arrow_drop_down</i>
              <div class="handin_import-content_container-right_select-detail">
                <div class="handin_import-content_container-right_select-item dev_publish_status">
                  <span class="handin_import-content_container-right_select-item_detail" onclick="changeDateText('P');"
                    value='P'><spring:message code="pub.enter.yifabiao" /></span>
                </div>
                <div class="handin_import-content_container-right_select-item dev_publish_status">
                  <span class="handin_import-content_container-right_select-item_detail" onclick="changeDateText('A');"
                    value='A'><spring:message code="pub.enter.weifabiao" /></span>
                </div>
              </div>
            </div>
            <!-- 成果状态  end-->
            <div class="handin_import-content_container-right_state-sub">
              <!-- 发表日期  begin-->
              <div class="handin_import-content_container-right_sub-area">
                <span class="handin_import-content_container-tip">*</span> <span id="publishDate_span"><spring:message
                    code="pub.enter.pulishDate" /></span>
              </div>
              <div class="handin_import-content_container-right_area"
                style="width: 79%; flex-direction: column; align-items: flex-start; border: none; height: auto;">
                <div
                  class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input error_import-tip_border"
                  style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 100% !important;">
                  <input class="json_publishDate dev-detailinput_container" itemevent="callbackDate" type="text"
                    name="publishDate" id="publishDate" unselectable="on" onfocus="createDatepicker()" readonly
                    datepicker focusdata date-format="yyyy-mm-dd"
                    value='<iris:dateFormat dateStr="${pubVo.publishDate }" splitChar="-"/>' />
                </div>
                <div class="json_publishDate_msg" style="display: none"></div>
              </div>
              <!-- 发表日期 end-->
            </div>
          </div>
        </div>
        <!-- 成果期刊期刊引证  begin-->
        <div class="handin_import-content_container-around" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.volumeNoAndIssue" /></span>
          </div>
          <div class="handin_import-content_container-right_Citation">
            <div style="display: flex; align-items: flex-start; flex-direction: column;">
              <div style="display: flex; align-items: center;">
                <div class="handin_import-content_container-right_Citation-item error_import-tip_border"
                  style="width: 175px!important;">
                  <input type="text" class="dev-detailinput_container json_journal_volumeNo"
                    placeholder='<spring:message code="pub.enter.volumeNo"/>'
                    onblur="this.placeholder='<spring:message code='pub.enter.volumeNo'/>'" maxlength="20"
                    name="volumeNo" value="${pubVo.pubTypeInfo.volumeNo }" />
                </div>
                <div
                  class="handin_import-content_container-right_Citation-item handin_import-content_container-right_Citation-center error_import-tip_border"
                  style="width: 175px!important; margin-left: 10px;">
                  <input type="text" class="dev-detailinput_container json_journal_issue"
                    title='<spring:message code="pub.enter.issue"/>'
                    placeholder='<spring:message code="pub.enter.issue"/>'
                    onblur="this.placeholder='<spring:message code='pub.enter.issue'/>'" maxlength="20" name="issue"
                    value="${pubVo.pubTypeInfo.issue }" />
                </div>
              </div>
            </div>
            <!-- 成果期刊期刊引证  end-->
            <!-- 起止页码文章号  begin-->
            <div class="handin_import-content_container-right_area"
              style="margin-right: -6px; flex-direction: column; align-items: flex-start; border: none; height: auto;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                <input type="text" class="dev-detailinput_container json_journal_pageNumber" maxlength="100"
                  placeholder='<spring:message code="pub.enter.pageMsg"/>'
                  title='<spring:message code="pub.enter.pageMsg"/>' name="" value="${pubVo.pubTypeInfo.pageNumber}">
              </div>
            </div>
          </div>
          <!-- 起止页码  end-->
        </div>
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
        <!-- 成果DOI  begin-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
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
        <!-- 成果DOI  end-->
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
      name="membersJsonStr" value="" /><input type="hidden" id="des3FileId" name="des3FileId" value="" />
  </form>
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0"></div>
  <div class="background-cover cover_colored" id="addNewJournal" style="display: none;">
    <div class="new-add_periodical">
      <div class="new-add_periodical-header">
        <span class="new-add_periodical-header_content"><spring:message code="pub.addJournal.addNewJournal"/></span> <i
          class="new-add_periodical-header_close new-normal_close-tip" onclick="PubEdit.cancleAddNewJournal();"></i>
      </div>
      <div class="new-add_periodical-body">
        <div class="new-add_periodical-body_name">
          <span class="new-add_periodical-body_name-content"><span class="new-add_periodical-body_name-tip">*</span><span><spring:message code="pub.addJournal.name"/></span></span>
          <div class="new-add_periodical-body_input">
            <input type="text" value="" id="add_journal_name" class="new-add_periodical-body_input-box">
          </div>
          <span class="new-add_periodical-body_name-tip_content">(<span class="new-add_periodical-body_name-tip">*</span><span><spring:message code="pub.addJournal.hdwbt"/></span>)
          </span>
        </div>
        <div class="new-add_periodical-body_code">
          <span class="new-add_periodical-body_code-name">ISSN:</span>
          <div class="new-add_periodical-body_input">
            <input type="text" value="" id="add_journal_issn" placeholder="(e.g. 1234-5678)"
              class="new-add_periodical-body_input-box">
          </div>
        </div>
      </div>
      <div class="new-add_periodical-footer">
        <div class="new-add_periodical-footer_save" style="cursor: pointer" onclick="PubEdit.cancleAddNewJournal();"><spring:message code="pub.enter.pubCancel"/></div>
        <div class="new-add_periodical-footer_cancle" onclick="PubEdit.confirmAddNewJournal()"><spring:message code="pub.enter.savePub"/></div>
      </div>
    </div>
  </div>
</body>
</html>
