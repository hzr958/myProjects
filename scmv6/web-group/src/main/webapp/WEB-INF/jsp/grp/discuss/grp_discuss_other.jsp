<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	var buttonlist = document.getElementsByClassName("button_primary");
	clickDynamicreact(buttonlist);
})
</script>
<s:if test="prjInfo != null">
<div class="module-card__box">
  <div class="module-card__header">
    <div class="module-card-header__title">
           其他
    </div>
  </div>
  <div class="main-list__list">
      <div class="main-list__item">
        <div class="main-list__item_content">
                                    负责单位：
               <c:if test="${!empty prjInfo.insName }">
               ${prjInfo.insName }
               </c:if>
               <c:if test="${empty prjInfo.insName}">
                                         暂无
               </c:if>
           <br/>
           项目日期：
               <c:if test="${!empty prjInfo.startYear }">
                <iris:startToEndDateFormat startYear="${prjInfo.startYear }" endYear="${prjInfo.endYear }" startMonth="${prjInfo.startMonth }" endMonth="${prjInfo.endMonth }" 
                 startDay="${prjInfo.startDay }" endDay="${prjInfo.endDay }" character=" - " dateCharacter="."/>
              </c:if>
              <c:if test="${empty prjInfo.startYear }">
                                  暂无
              </c:if>
       <br/>
           项目金额：
               <c:if test="${!empty prjInfo.amount }">
                  ${prjInfo.amountUnit }&nbsp;${prjInfo.amountFormat }
               </c:if>
               <c:if test="${empty prjInfo.amount }">
                                             暂无
              </c:if>
      </div>
  </div>
</div>
</s:if>
