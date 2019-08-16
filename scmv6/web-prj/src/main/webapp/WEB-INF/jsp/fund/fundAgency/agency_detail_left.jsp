<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <s:if test="disciplineList != null && disciplineList.size() > 0">
<s:iterator value="disciplineList" var="discipline" status="stu">
<div class="dev_condition" value="${discipline.id}">${discipline.zhName}<div></div></div>
</s:iterator>
<div class="dev_condition" value="0">其他<div></div></div>
</s:if>
<div class="dev_insType2" value="insType2">科研机构<div></div></div>
<div class="dev_insType1" value="insType1">企业<div></div></div>
<div class="dev_insType3" value="insType3">其他<div></div></div> --%>
<script type="text/javascript">
$(function(){
    var closelist = document.getElementsByClassName("funding-agencies_container-left_title");
    for(var i = 0; i < closelist.length; i++){
    	closelist[i].onclick = function(){
    		if(this.querySelector(".filter-section__toggle").innerHTML=="expand_less"){
    			this.querySelector(".filter-section__toggle").innerHTML="expand_more";
    			$(this).next().find(".filter-value__list").hide();
    		}else{
    		
    			this.querySelector(".filter-section__toggle").innerHTML="expand_less";
    			$(this).next().find(".filter-value__list").show();
    		}
    	}
    }	

});
</script>
<div class="funding-agencies_container-left">
  <div class="funding-agencies_container-left_item">
    <div class="funding-agencies_container-left_title">
      <span class="funding-agencies_container-left_title-detaile"><s:text name="homepage.fundmain.researchArea" /></span>
      <i class="funding-agencies_container-left_title-flag material-icons filter-section__toggle">expand_less</i>
    </div>
    <div class="filter-list__section funding-agencies_container-left_area setting-list">
      <ul class="filter-value__list">
        <s:iterator value="disciplineList" var="discipline" status="stu">
          <li class="funding-agencies_container-left_area-item"
            onclick="FundDetail.LeftOnclick(this);FundDetail.showFundList();"><span
            class="funding-agencies_container-left_area-detaile dev_condition" value="${discipline.categoryId}">
              <c:if test="${locale=='en_US'}">
							${discipline.categoryEn}						
						</c:if> <c:if test="${locale=='zh_CN'}">
							${discipline.categoryZh}
						</c:if>
          </span> <span><i class="funding-agencies_container-left_area-num"></i></span></li>
        </s:iterator>
        <li class="funding-agencies_container-left_area-item"
          onclick="FundDetail.LeftOnclick(this);FundDetail.showFundList();"><span
          class="funding-agencies_container-left_area-detaile dev_condition" value="0"><s:text
              name="homepage.fundmain.others" /></span> <span><i class="funding-agencies_container-left_area-num"></i></span></li>
      </ul>
    </div>
  </div>
  <div class="funding-agencies_container-left_item">
    <div class="funding-agencies_container-left_title">
      <span class="funding-agencies_container-left_title-detaile"><s:text name="homepage.fundmain.eligibility" /></span>
      <i class="funding-agencies_container-left_title-flag material-icons filter-section__toggle">expand_less</i>
    </div>
    <div class="filter-list__section funding-agencies_container-left_area setting-list">
      <ul class="filter-value__list">
        <li class="funding-agencies_container-left_area-item"
          onclick="FundDetail.LeftOnclick(this);FundDetail.showFundList();"><span
          class="funding-agencies_container-left_area-detaile dev_insType" value="1"><s:text
              name="homepage.fundmain.enterprise" /></span> <span><i class="funding-agencies_container-left_area-num"></i></span>
        </li>
        <li class="funding-agencies_container-left_area-item"
          onclick="FundDetail.LeftOnclick(this);FundDetail.showFundList();"><span
          class="funding-agencies_container-left_area-detaile dev_insType" value="2"><s:text
              name="homepage.fundmain.institution" /></span> <span><i class="funding-agencies_container-left_area-num"></i></span>
        </li>
        <li class="funding-agencies_container-left_area-item"
          onclick="FundDetail.LeftOnclick(this);FundDetail.showFundList();"><span
          class="funding-agencies_container-left_area-detaile dev_insType" value="0"><s:text
              name="homepage.fundmain.others" /></span> <span><i class="funding-agencies_container-left_area-num"></i></span></li>
      </ul>
    </div>
  </div>
</div>