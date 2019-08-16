<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty prjInfo }">
<div class="new-mobilegroup_body-showitem_header">
   <span class="new-mobilegroup_body-showitem_title">基本信息</span>
   <i></i>
</div>
<div class="new-mobilegroup_body-showitem_body"  style="padding: 4px 0px; letter-spacing: 0.1px;">
   <div class="new-mobilegroup_body-showitem_body-item">
      <span class="new-mobilegroup_body-showitem_title">资助机构:</span>
      <span class="new-mobilegroup_body-showitem_content">
      <c:if test="${not empty prjInfo.agencyName || !empty prjInfo.enAgencyName  }">
           <iris:lable zhText="${prjInfo.agencyName }" enText="${prjInfo.enAgencyName }"></iris:lable>
      </c:if>
      <c:if test="${empty prjInfo.agencyName && empty prjInfo.enAgencyName }">       
                          暂无
      </c:if>      
      </span>
   </div>
   <div class="new-mobilegroup_body-showitem_body-item">
      <span class="new-mobilegroup_body-showitem_title">负责单位:</span>
      <span class="new-mobilegroup_body-showitem_content">
          <c:if test="${not empty prjInfo.insName }">
               ${prjInfo.insName }
           </c:if>
           <c:if test="${empty prjInfo.insName}">
                                     暂无
           </c:if>
      </span>
   </div>
   <div class="new-mobilegroup_body-showitem_body-item">
      <span class="new-mobilegroup_body-showitem_title">项目日期:</span>
      <span class="new-mobilegroup_body-showitem_content">
        <c:if test="${not empty prjInfo.startYear }">
            <iris:startToEndDateFormat startYear="${prjInfo.startYear }" endYear="${prjInfo.endYear }" startMonth="${prjInfo.startMonth }" endMonth="${prjInfo.endMonth }" 
             startDay="${prjInfo.startDay }" endDay="${prjInfo.endDay }" character=" - " dateCharacter="."/>
        </c:if>
        <c:if test="${empty prjInfo.startYear }">
                            暂无
        </c:if>
      </span>
   </div>
   <div class="new-mobilegroup_body-showitem_body-item">
      <span class="new-mobilegroup_body-showitem_title">项目金额:</span>
      <span class="new-mobilegroup_body-showitem_content">
          <c:if test="${not empty prjInfo.amount }">
          ${prjInfo.amountUnit }&nbsp;<span id="prj_amount_span">${prjInfo.amount }</span>
          </c:if>
          <c:if test="${empty prjInfo.amount }">
                         暂无
          </c:if>
      
      </span>
   </div>
</div>
</c:if>