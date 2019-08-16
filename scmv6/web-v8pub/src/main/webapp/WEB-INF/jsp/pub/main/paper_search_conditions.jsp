
<!-- 检索条件 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	var searchTips = '<spring:message code="pub.search.tips" />';
	var nowYear = "${currentYear}";
	$("#recentOneYears").attr("filter-value", findRecentYearsStr(nowYear, 1));
	$("#recentThreeYears").attr("filter-value", findRecentYearsStr(nowYear, 3));
	$("#recentFiveYears").attr("filter-value", findRecentYearsStr(nowYear, 5));
	var oneYearTitle = locale == "zh_CN" ? (nowYear)+pubi18n.i18n_recentOneYear :pubi18n.i18n_recentOneYear+(nowYear);
	var threeYearTitle = locale == "zh_CN" ? (nowYear-2)+pubi18n.i18n_recentThreeYear :pubi18n.i18n_recentThreeYear+(nowYear-2);
	var fiveYearTitle = locale == "zh_CN" ? (nowYear-4)+pubi18n.i18n_recentFiveYear :pubi18n.i18n_recentFiveYear+(nowYear-4);
	$("#oneYearTitle").html(oneYearTitle);
	$("#threeYearTitle").html(threeYearTitle);
	$("#fiveYearTitle").html(fiveYearTitle);
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
    	targetlist[i].querySelector("input").onfocus = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;width:183px!important;";
    	}
    	targetlist[i].querySelector("input").onblur = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;width:183px!important;";
    	}
    }
    if(document.getElementsByClassName("setting").length>0){
      document.getElementsByClassName("setting")[0].style.height = window.innerHeight - 118 + "px";    
    }
});

//获取最近几年年份拼接字符串
function findRecentYearsStr(nowYear, yearCount) {
	var startYear = nowYear - yearCount + 1;
	var yearStr = "";
	yearStr += nowYear;
	if (startYear > 0) {
		for (var i = nowYear - 1; i >= startYear; i--) {
			yearStr += "," + i;
		}
	}
	return yearStr;
}
function searchPaperFocus(){
	$("#searchPaperInput").focus();
}
</script>
<div class="cont_l" style="overflow: hidden; width: 240px; padding-right: 0px;">
  <div class="filter-list" list-filter="paperlist" style="width: 240px; overflow: hidden; display: block !important;">
    <div style="height: auto; width: 260px;">
      <!-- 1，关键词 -->
      <!-- 搜索框 -->
      <div class="searchbox__container main-list__searchbox" list-search="paperlist"
        style="width: 220px; margin-bottom: 10px; margin-left: 0px;">
        <div class="searchbox__main" style="width: 217px !important;">
          <input id="searchPaperInput" placeholder='<spring:message code="pub.search.tips" />'>
          <div class="searchbox__icon material-icons" onclick="searchPaperFocus();"></div>
        </div>
      </div>
      <!-- 2，收录索引 -->
      <div class="filter-list__section js_filtersection setting-parent" filter-section="includeType"
        filter-method="multiple">
        <div class="filter-section__header setting-title" style="padding-left: 0px; width: 220px;">
          <div class="filter-section__title" style="height: 32px; line-height: 32px;">
            <strong><spring:message code='pub.filter.indexes' /></strong>
          </div>
          <i class="filter-section__toggle material-icons click-target_btn" style="">expand_less</i>
        </div>
        <div class="setting-list">
          <ul class="filter-value__list">
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="ei">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">EI</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="sci">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">SCIE</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="ssci">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">SSCI</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="istp">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">ISTP</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="cssci">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">CSSCI</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="pku">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.indexes.pku' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="other">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.indexes.others' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
          </ul>
        </div>
      </div>
      <!-- 3，成果类型 -->
      <div class="filter-list__section js_filtersection setting-parent" filter-section="pubType"
        filter-method="multiple">
        <div class="filter-section__header setting-title" style="padding-left: 0px; width: 220px;">
          <div class="filter-section__title" style="height: 32px; line-height: 32px;">
            <strong><spring:message code='pub.filter.research' /></strong>
          </div>
          <i class="filter-section__toggle material-icons click-target_btn" style="">expand_less</i>
        </div>
        <div class="setting-list">
          <ul class="filter-value__list">
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="4">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.journalArticle' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="5">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.patent' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="3">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.conferencePaper' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="1">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.award' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="2">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.bookAndMonograph' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="10">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.bookChapter' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="8">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.thesis' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="12">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.standard' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="13">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.softwarecopyright' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item" filter-value="7">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.other' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
          </ul>
        </div>
      </div>
      <!-- 4，出版年份 -->
      <div class="filter-list__section js_filtersection setting-parent" filter-section="publishYear"
        filter-method="single">
        <div class="filter-section__header setting-title" style="padding-left: 0px; width: 220px;">
          <div class="filter-section__title" style="height: 32px; line-height: 32px;">
            <strong><spring:message code='pub.filter.publish.year' /></strong>
          </div>
          <i class="filter-section__toggle material-icons click-target_btn" style="">expand_less</i>
        </div>
        <div class="setting-list">
          <ul class="filter-value__list">
            <li class="filter-value__item js_filtervalue item_list-align  filter-value__list-item" filter-value="2018"
              id="recentOneYears">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;"
                id="oneYearTitle">一年以内</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item"
              filter-value="2018,2017,2016" id="recentThreeYears">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;"
                id="threeYearTitle">三年以内</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align filter-value__list-item"
              filter-value="2018,2017,2016,2015,2014" id="recentFiveYears">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;"
                id="fiveYearTitle">五年以内</div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
            <li class="filter-value__item js_filtervalue item_list-align not_limit filter-value__list-item"
              filter-value="">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption item_list-align_item" style="width: 120px;">
                <spring:message code='pub.filter.publish.year.unlimited' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel"></i>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>