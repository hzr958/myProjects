<!-- 成果统计模块 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
      $(function(){
    	  if(document.getElementsByClassName("main-list-header__title-item_tip").length>0){
    	   	   var hoverele = document.getElementsByClassName("main-list-header__title-item_tip")[0]; 
    	   	     hoverele.onmouseover = function(){
    	   	      this.setAttribute("title",this.innerHTML.trim());
               } 
    	  }
          var clicelemlist = document.getElementsByClassName("sort-container_item-list");
          for(var i = 0 ; i < clicelemlist.length;i++){
              clicelemlist[i].onclick = function(){
                this.closest(".sort-container").querySelector(".sort-container_header-title").innerHTML = this.querySelector(".sort-container_item_name").innerHTML;
              }     
          }
      })
</script>
<div class="main-list__header">
  <div class="main-list-header__title" style="display: flex;">
    <div class="main-list-header__title-item" style="margin: 0px 8px; font-size: 14px;">
      <spring:message code="psn.pub.statistics.hindex" />
      : <span class="main-list-header__title-item_num">${pubListVO.hIndex }</span>
    </div>
    <div class="main-list-header__title-item" style="font-size: 14px;">
      <spring:message code="psn.pub.statistics.pubCount" />
      : <span class="main-list-header__title-item_num" id="pubTotalCount">${pubListVO.totalCount }</span>
    </div>
    <div class="main-list-header__title-item " style="margin: 0px 8px; font-size: 14px;">
      <spring:message code="psn.pub.statistics.citedCount" />
      : <span class="main-list-header__title-item_num">${pubListVO.citedSum }</span>
    </div>
    <c:if test="${pubListVO.self != 'yes' && pubListVO.hasPrivatePub}">
    
      <div class="main-list-header__title-item_tip" title='<spring:message code="psn.pub.show.hide.tips"/>'>
        <spring:message code="psn.pub.show.hide.tips" />
      </div>
    </c:if>
  </div>
  <div class="sort-container js_filtersection">
    <div class="sort-container_header" style="${locale eq 'en_US' ? 'width:130px;' : ''}">
      <div class="sort-container_header-tip">
        <i class="sort-container_header-flag sort-container_header-up"></i><i class="sort-container_header-down"></i>
      </div>
      <div class="sort-container_header-title filter-section__title" style="${locale eq 'en_US' ? 'width:120px;' : ''}">
        <spring:message code="pub.filter.recentPublish" />
      </div>
    </div>
    <div class="sort-container_item" style="${locale eq 'en_US' ? 'width:108px;' : ''}">
      <div class="filter-list vert-style option_has-stats" list-filter="psnpub">
        <div class="filter-list__section js_filtersection" filter-section="orderBy" filter-method="compulsory"
          style="margin: 0; padding: 0;">
          <ul class="filter-value__list">
            <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="publishYear">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption sort-container_item_name"
                style="font-size: 14px !important;">
                <spring:message code="pub.filter.recentPublish" />
              </div>
              <div class="filter-value__stats js_filterstats"></div>
            </li>
            <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="updateDate">
              <div class="input-custom-style">
                <input type="checkbox"><i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption sort-container_item_name"
                style="font-size: 14px !important;">
                <spring:message code="pub.filter.recentModify" />
              </div>
              <div class="filter-value__stats js_filterstats"></div>
            </li>
            <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="citedTimes">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption sort-container_item_name"
                style="font-size: 14px !important;">
                <spring:message code="pub.filter.recentCitations" />
              </div>
              <div class="filter-value__stats js_filterstats"></div>
            </li>
            <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="title">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption sort-container_item_name"
                style="font-size: 14px !important;">
                <spring:message code="pub.filter.pubTitle" />
              </div>
              <div class="filter-value__stats js_filterstats"></div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <c:if test="${pubListVO.self == 'yes'}">
    <button class="button_main button_primary-reverse" onclick="Pub.addPub();">
      <spring:message code="pub.add" />
    </button>
    <button class="button_main button_primary-changestyle" onclick="updateCite();">
      <spring:message code="maint.more.update_citation" />
    </button>
  </c:if>
</div>