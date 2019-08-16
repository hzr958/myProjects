<!-- 检索条件 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	var nowYear = new Date().getFullYear();
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
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
    	}
    	targetlist[i].querySelector("input").onblur = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";
    	}
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

</script>
<div class="module-home__fixed-layer_filter"  style="width: 265px;">
  <div class="searchbox__container main-list__searchbox" list-search="psnpub" style="width: 90%;">
    <div class="searchbox__main" style="width: 250px;">
      <input placeholder='<spring:message code="psn.pub.search" />'>
      <div class="searchbox__icon material-icons"></div>
    </div>
  </div>
  <div class="filter-list vert-style option_has-stats filter-list__specStyle" list-filter="psnpub">
    <div class="filter-list__section js_filtersection" filter-section="pubType" filter-method="multiple">
      <div class="filter-section__header">
        <div class="filter-section__title">成果类型</div>
        <i class="material-icons filter-section__toggle">expand_less</i>
      </div>
      <ul class="filter-value__list">
        <li class="filter-value__item js_filtervalue option_no-data" filter-value="4">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">期刊论文</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="3">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">会议论文</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="5">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">专利</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="1">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">奖励</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="2">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">书/著作</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="10">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">书籍章节</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="8">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">学位论文</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="12">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">标准</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="13">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">软件著作权</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="7">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">其他</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
      </ul>
    </div>
    <div class="filter-list__section js_filtersection" filter-section="includeType" filter-method="multiple">
      <div class="filter-section__header">
        <div class="filter-section__title">收录类别</div>
        <i class="material-icons filter-section__toggle">expand_more</i>
      </div>
      <ul class="filter-value__list" style="display: none;">
        <li class="filter-value__item js_filtervalue" filter-value="ei">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">EI</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="scie">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">SCIE</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="ssci">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">SSCI</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="istp">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">ISTP</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="cssci">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">CSSCI</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="pku">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title='北大核心'>北大核心</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
      </ul>
    </div>
    <div class="filter-list__section js_filtersection" filter-section="publishYear" filter-method="single">
      <div class="filter-section__header">
        <div class="filter-section__title">发表年份</div>
        <i class="material-icons filter-section__toggle">expand_more</i>
      </div>
      <ul class="filter-value__list" style="display: none;">
        <li class="filter-value__item js_filtervalue" filter-value="2018" id="recentOneYears">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="" id="oneYearTitle">2018 年以来</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="2018,2017,2016" id="recentThreeYears">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="" id="threeYearTitle">2016年以来</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="2018,2017,2016,2015,2014" id="recentFiveYears">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="" id="fiveYearTitle">2014 年以来</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue not_limit" filter-value="">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">不限</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
      </ul>
    </div>
  </div>
</div>
