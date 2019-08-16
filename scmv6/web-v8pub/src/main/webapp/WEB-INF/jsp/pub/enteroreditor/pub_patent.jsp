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
	//专利国家
	var area = $(".json_patent_area").val();
	var type = $(".json_patent_type").val();
    if(area==""){
    	area="CHINA";
    	$(".json_patent_area").val(area);
    }
	$(".dev_patent_area[value='"+area+"']").removeClass("selected-oneself").addClass("selected-oneself_confirm");
	PubEdit.changePatentArea();
	$(".json_patent_type").val(type);
	//专利类型
    if(type!=""){
    	if(type=="51" || type=="52" || type=="53" || type=="54"){
    	    $(".dev_patent_type").removeClass("selected-oneself_confirm").addClass("selected-oneself");//全掉选中
	        $(".dev_patent_type[value='"+type+"']").removeClass("selected-oneself").addClass("selected-oneself_confirm");  		
    	}else{
    		$(".dev_patent_type_input").val(type);
    	}
    }else{
      type="51";
      $(".dev_patent_type[value='"+type+"']").removeClass("selected-oneself").addClass("selected-oneself_confirm");  
    }
    $(".dev_patent_type_input").on("input",function(e){
        var value = $(this).val();
        $(".json_patent_type").val(value);
    });
    //专利状态
    var status = $(".json_patent_status").val();
    if(status==""){
    	status="0";
    	$(".json_patent_status").val(status);
    }	
    PubEdit.changePartentStatus();

    $(".dev_patent_status[value='"+status+"']").removeClass("selected-oneself").addClass("selected-oneself_confirm");
    //专利转化状态
    var transitionStatus = $(".json_patent_transitionStatus").val(); 
    if(transitionStatus==""){
    	transitionStatus = "NONE";
    	$(".json_patent_transitionStatus").val("NONE"); 
    }
    PubEdit.changeTransitionStatus();
    $(".dev_patent_transitionStatus[value='"+transitionStatus+"']").removeClass("selected-oneself").addClass("selected-oneself_confirm");
    //控制交易金额
    $(".json_patent_price").on("input",function(e){
    	var value = $(this).val();
    	if(!/^\d{0,5}(\.\d{0,4})?$/.test($.trim(value))){
    		$(this).val(value.replace(/.$/,""));
    	}
    });
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
    <%--专利 --%>
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
        <!-- 专利国家 begin -->
        <div class="handin_import-content_container-around">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.patentArea" /></span>
          </div>
          <div class="handin_import-content_container-right_collect"
            style="margin-right: 12px; justify-content: space-between;">
            <div class="handin_import-content_container-right_collect-item">
              <i class="selected-oneself dev_patent_area" value="CHINA"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.CHINA" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item">
              <i class="selected-oneself dev_patent_area" value="USA"></i><span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.USA" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item">
              <i class="selected-oneself dev_patent_area" value="EUROPE"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.EUROPE" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item">
              <i class="selected-oneself dev_patent_area" value="WIPO"></i><span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.WIPO" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item">
              <i class="selected-oneself dev_patent_area" value="JAPAN"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.JAPAN" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item">
              <i class="selected-oneself dev_patent_area" value="OTHER"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.OTHER" /></span>
            </div>
            <input type="hidden" class="json_patent_area" name="area" value="${pubVo.pubTypeInfo.area }" />
          </div>
        </div>
        <!-- 专利国家 end -->
        <!-- 专利类别 begin -->
        <div class="handin_import-content_container-around">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.patentType" /></span>
          </div>
          <div class="handin_import-content_container-right_collect dev_patent_type_i"
            style="margin-right: 12px; justify-content: flex-start;">
            <div class="handin_import-content_container-right_collect-item dev_type_51"
              style="margin: 0px 20px 0px 0px;">
              <i class="selected-oneself dev_patent_type" value="51"></i> <span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.INVENTION" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item dev_type_52" style="margin: 0px 20px;">
              <i class="selected-oneself dev_patent_type" value="52"></i> <span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.PRACTICAL" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item dev_type_53" style="margin: 0px 20px;">
              <i class="selected-oneself dev_patent_type" value="53"></i> <span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.DESIGN" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item dev_type_54" style="margin: 0px 20px;">
              <i class="selected-oneself dev_patent_type" value="54"></i> <span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.BOTANY" /></span>
            </div>
          </div>
          <div class="handin_import-content_container-right dev_type_other"
            style="width: 46%; display: flex; align-items: center;">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container dev_patent_type_input" maxlength="20" value="">
            </div>
            <span style="color: #333; font-size: 14px; min-width: 240px;">&nbsp;&nbsp;<spring:message
                code="pub.label.patent_type_other_remind" /></span>
          </div>
          <input type="hidden" class="json_patent_type" name="type" value="${pubVo.pubTypeInfo.type }" />
        </div>
        <!-- 专利类别 end -->
        <!-- 成果标题  begin-->
        <div class="handin_import-content_container-center json_journal">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.patentTitle" />:</span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container json_title js_autocompletebox" autocomplete="off"
                title_msg="<spring:message code="pub.enter.patentTitle"/>"
                request-data="PubEdit.getAutoCompleteJson('patentTitle');" request-url="/psnweb/ac/ajaxgetComplete"
                other-event="callbackJournal" maxlength="2000" id="title" name="title" value="${pubVo.title }">
              <input type="hidden" class="json_journal_jid" value="" />
            </div>
            <div class="json_title_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 成果标题  end-->
        <div class="handin_import-content_container-center">
          <!-- 专利状态 begin -->
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.patentStatus" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 28px 0px 0px;">
              <i class="selected-oneself dev_patent_status" value="0"></i><span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.patentRequest" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 53px;">
              <i class="selected-oneself dev_patent_status" value="1"></i><span class="selected-author_confirm-detaile"><spring:message
                  code="pub.enter.patentsq" /></span>
            </div>
            <input type="hidden" class="json_patent_status" name="status" value="${pubVo.pubTypeInfo.status }" />
            <!-- 专利状态 end -->
            <!-- 专利号和申请号 begin -->
            <div class="handin_import-content_container-right_sub-area" style="width: 26%; margin-left: 6px;">
              <span class="handin_import-content_container-tip">*</span> <span class="dev_patentNo_msg"> <c:if
                  test="${pubVo.pubTypeInfo.status!='1' }">
                  <spring:message code="pub.enter.applicationNo" />
                </c:if> <c:if test="${pubVo.pubTypeInfo.status=='1' }">
                  <spring:message code="pub.enter.patentNo" />
                </c:if>
              </span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                <input type="text" class="dev-detailinput_container json_patent_applicationNo" maxlength="20"  autocomplete="off" 
                  value="${pubVo.pubTypeInfo.applicationNo }" />
              </div>
              <div class="json_patent_applicationNo_msg json_patent_publicationOpenNo_msg" style="display: none"></div>
            </div>
            <!-- 专利号和申请号  end -->
          </div>
        </div>
        <!-- IPC 和  CPC begin -->
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span><spring:message code="pub.enter.IPC" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border">
                <input type="text" class="dev-detailinput_container json_patent_ipc" maxlength="20" id="ipc" name="ipc"
                  value="${pubVo.pubTypeInfo.IPC }">
              </div>
              <div class="json_patent_ipc_msg" style="display: none"></div>
            </div>
            <div class="handin_import-content_container-right_sub-area">
              <span style="color: red;"></span><span><spring:message code="pub.enter.CPC" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border">
                <input type="text" class="dev-detailinput_container json_patent_cpc" maxlength="20" id="cpc" name="cpc"
                  value="${pubVo.pubTypeInfo.CPC }">
              </div>
              <div class="json_patent_cpc_msg" style="display: none"></div>
            </div>
          </div>
        </div>
        <!-- IPC 和  CPC end -->
        <div class="handin_import-content_container-center">
          <!-- 申请人和专利权人 begin -->
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span class="dev_patent_patentee"><spring:message
                code="pub.enter.applier" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border">
                <c:if test="${pubVo.pubTypeInfo.status!='1' }">
                  <input type="text" class="dev-detailinput_container json_patent_applier" maxlength="60" autocomplete="off"
                    value="${pubVo.pubTypeInfo.applier }">
                </c:if>
                <c:if test="${pubVo.pubTypeInfo.status=='1' }">
                  <input type="text" class="dev-detailinput_container json_patent_patentee" maxlength="60" autocomplete="off"
                    value="${pubVo.pubTypeInfo.patentee }">
                </c:if>
              </div>
              <div class="json_patent_applier_msg json_patent_patentee_msg" style="display: none"></div>
            </div>
            <!-- 申请人和专利权人 end -->
            <!-- 申请日期和公开日期 begin -->
            <div class="handin_import-content_container-right_sub-area">
              <span class="handin_import-content_container-tip">*</span> <span class="dev_publishDate_msg"><spring:message
                  code="pub.enter.shenqingriqi" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div
                class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input error_import-tip_border"
                style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 100% !important;">
                <input class="json_patent_applicationDate dev-detailinput_container" itemevent="callbackDate"
                  type="text" name="applicationDate" id="applicationDate" unselectable="on" onfocus="this.blur()"
                  readonly datepicker date-format="yyyy-mm-dd"
                  value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.applicationDate }" splitChar="-"/>' />
              </div>
              <div class="json_patent_applicationDate_msg json_patent_publicationOpenNo_msg" style="display: none"></div>
            </div>
            <!-- 申请日期和公开日期 end -->
          </div>
        </div>
        <div class="handin_import-content_container-center dev_partent_start_end_date">
          <!-- 公开号 begin -->
          <div class="handin_import-content_container-right_sub-area" style="width: 12%; padding-left: 4px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.publicationOpenNo" /></span>
          </div>
          <div class="handin_import-content_container-right_area"
            style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
            <div
              class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input error_import-tip_border"
              style="border: 1px solid #ddd; border-radius: 3px; height: 30px; width: 87% !important;">
              <input type="text" class="dev-detailinput_container json_patent_publicationOpenNo" maxlength="20" autocomplete="off" 
                value="${pubVo.pubTypeInfo.publicationOpenNo }" />
            </div>
          </div>
          <!-- 公开号end -->
          <!-- 有效日期 begin -->
          <div class="handin_import-content_container-left" style="padding-left: 5px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.youxiaoriqi" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center; width: 26%;">
            <div class="handin_import-content_container-right_area" style="width: 50%;">
              <div
                class="handin_import-content_container-right_area-content input__box dev-detailinput_container-input input_not-null error_import-tip_border">
                <input class="json_patent_startDate dev-detailinput_container" itemevent="callbackDate" type="text"
                  name="startDate" id="startDate" readonly="" unselectable="on" onfocus="this.blur()" datepicker=""
                  date-format="yyyy-mm-dd" style="width: 85% !important;"
                  value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.startDate }" splitChar="-"/>'>
              </div>
            </div>
            <span style="margin: 0px 12px;"><spring:message code="pub.enter.zhi" /></span>
            <div class="handin_import-content_container-right_area" style="width: 50%;">
              <div
                class="handin_import-content_container-right_area-content input__box dev-detailinput_container-input input_not-null error_import-tip_border">
                <input class="json_patent_endDate dev-detailinput_container" itemevent="callbackDate" type="text"
                  name="endDate" id="endDate" readonly="" unselectable="on" onfocus="this.blur()" datepicker=""
                  date-format="yyyy-mm-dd" style="width: 85% !important;"
                  value='<iris:dateFormat dateStr="${pubVo.pubTypeInfo.endDate }" splitChar="-"/>'>
              </div>
            </div>
            <div class="json_patent_startDate_msg json_patent_endDate_msg" style="display: none"></div>
          </div>
          <!-- 有效日期 begin -->
        </div>
        <div class="handin_import-content_container-center dev_patent_transition">
          <!-- 转化状态begin -->
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span><spring:message code="pub.enter.transitionStatus" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_collect" style="width: 28%; margin-right: 12px;">
              <div class="handin_import-content_container-right_collect-item">
                <i class="selected-oneself dev_patent_transitionStatus" value="LICENCE"></i><span><spring:message
                    code="pub.enter.patentLicence" /></span>
              </div>
              <div class="handin_import-content_container-right_collect-item">
                <i class="selected-oneself dev_patent_transitionStatus" value="TRANSFER"></i><span><spring:message
                    code="pub.enter.patentTransfer" /></span>
              </div>
              <div class="handin_import-content_container-right_collect-item">
                <i class="selected-oneself dev_patent_transitionStatus" value="INVESTMENT"></i><span><spring:message
                    code="pub.enter.patentInvestment" /></span>
              </div>
              <div class="handin_import-content_container-right_collect-item">
                <i class="selected-oneself dev_patent_transitionStatus" value="NONE"></i><span><spring:message
                    code="pub.enter.patentNone" /></span>
              </div>
              <input type="hidden" class="json_patent_transitionStatus" name=""
                value="${pubVo.pubTypeInfo.transitionStatus }" />
            </div>
            <!-- 交易金额 begin -->
            <div class="dev_patent_price"
              style="display: block; width: 395px; display: flex; align-items: center; margin-left: 125px;">
              <div class="handin_import-content_container-right_sub-area" style="width: 18%;">
                <span style="color: red;"></span><span><spring:message code="pub.enter.patentPrice" /></span>
              </div>
              <div class="handin_import-content_container-right_area" style="width: 68%;">
                <div class="handin_import-content_container-right_area-content">
                  <input type="text" class="dev-detailinput_container json_patent_price" maxlength="10"
                    placeholder="如：12345.0000" id="price" name="price" value="${pubVo.pubTypeInfo.price }">
                </div>
              </div>
              <span style="color: #333; font-size: 14px;">&nbsp;&nbsp;万元</span>
            </div>
          </div>
        </div>
        <!-- 成果资助标注  begin-->
        <%@ include file="pub_funding_annotation.jsp"%>
        <!-- 成果资助标注  end-->
        <!-- 成果引用数  begin-->
        <%@ include file="pub_cited_times.jsp"%>
        <!-- 成果引用数  end-->
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
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0"></div>
  <!-- 科技领域弹出框 -->
</body>
</html>