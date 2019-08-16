<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<title>项目列表</title>
<script type="text/javascript">
var jsessionId = "<%=session.getId() %>";
var $createGrpChipBox;
$(function(){
    //初始化filter-value
    var prjListMainDate = new Date();
    var prjListMainCurrYear = prjListMainDate.getFullYear();
    var prjListMainThreeYear = Number(prjListMainCurrYear) -2;
    var prjListMainFiveYear = Number(prjListMainCurrYear) -4;
    var prjListMainObj = $(".filter-list__section[filter-section='fundingYear'] .filter-value__list").children();
    if (prjListMainObj.length > 0) {
      for (var i = 0;i < prjListMainObj.length;i++) {
        switch (i) {
          case 0:
            $(prjListMainObj[i]).attr("filter-value",prjListMainCurrYear);
            break;
          case 1:
            $(prjListMainObj[i]).attr("filter-value",prjListMainThreeYear);
            break;
          case 2:
            $(prjListMainObj[i]).attr("filter-value",prjListMainFiveYear);
            break;
          default:
            $(prjListMainObj[i]).attr("filter-value",0);
            break;
        }
      }
    } 
	var outsideDes3PsnId=$("#outsideDes3PsnId").val();
	project.outsidePrjNumberAmount(outsideDes3PsnId);
	project.ajaxOutSideRecommendList(outsideDes3PsnId);
	project.cooperator();
	addFormElementsEvents();//横线的样式添加
	$(".filter-list__section[filter-section=fundingYear] .filter-value__list").hide();
	var changflag = document.getElementsByClassName("filter-section__toggle");
    for(var i = 0;i<changflag.length;i++){
    	changflag[i].onclick = function(){
    		if(this.innerHTML == "expand_less"){
    			this.innerHTML="expand_more";
                this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
    		}else{
    			this.innerHTML = "expand_less";
    			 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
    		}
    	} 
    }
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
</script>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
  <input type="hidden" id="othersSee" value="${othersSee}">
  <input type="hidden" id="des3CurrentId" value="${des3CurrentId}">
  <input type="hidden" id="prjIds" value="">
  <div class="container__horiz">
    <div class="container__card" id="hidefulltext" style="display: none"></div>
    <div style="display: flex; width: 100%;">
      <div style="width: 280px; height: auto; margin-right: 16px;">
        <div class="module-home__fixed-layer_filter" style="width: 265px;">
          <div class="searchbox__container main-list__searchbox" list-search="prjlist" style="width: 90%;">
            <div class="searchbox__main">
              <input placeholder='<s:text name="project.search"/>'>
              <div class="searchbox__icon material-icons"></div>
            </div>
          </div>
          <div class="filter-list vert-style option_has-stats" list-filter="prjlist">
            <!-- =============================所在机构_end -->
            <!-- =============================所在地区_start -->
            <div class="filter-list__section js_filtersection" filter-method="multiple" filter-section="agency">
              <div class="filter-section__header">
                <div class="filter-section__title">
                  <s:text name="project.agency"></s:text>
                </div>
                <i class="material-icons filter-section__toggle">expand_less</i>
              </div>
              <ul id="agencyNames" class="filter-value__list"></ul>
            </div>
            <div class="filter-list__section js_filtersection" filter-section="fundingYear" filter-method="single">
              <div class="filter-section__header">
                <div class="filter-section__title">
                  <s:text name="project.filter.fundYear"></s:text>
                </div>
                <i class="material-icons filter-section__toggle">expand_more</i>
              </div>
              <ul class="filter-value__list">
                <li class="filter-value__item js_filtervalue" filter-value="2018">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption" title="">
                    <s:text name="project.oneyear"></s:text>
                  </div>
                  <div class="filter-value__stats js_filterstats">(0)</div> <i
                  class="material-icons filter-value__cancel filter_need_cancel js_filtercancel">close</i>
                </li>
                <li class="filter-value__item js_filtervalue" filter-value="2016">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption" title="">
                    <s:text name="project.threeyear"></s:text>
                  </div>
                  <div class="filter-value__stats js_filterstats">(0)</div> <i
                  class="material-icons filter-value__cancel filter_need_cancel js_filtercancel">close</i>
                </li>
                <li class="filter-value__item js_filtervalue" filter-value="2014">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption" title="">
                    <s:text name="project.fiveyear"></s:text>
                  </div>
                  <div class="filter-value__stats js_filterstats">(0)</div> <i
                  class="material-icons filter-value__cancel filter_need_cancel js_filtercancel">close</i>
                </li>
                <li class="filter-value__item js_filtervalue" filter-value="0">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption" title="">
                    <s:text name="project.allyear"></s:text>
                  </div>
                  <div class="filter-value__stats js_filterstats">(0)</div> <i
                  class="material-icons filter-value__cancel filter_need_cancel js_filtercancel">close</i>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div id="prjcooperation"></div>
        <div class="dialogs__box" style="width: 720px;" dialog-id="dev_lookall_pubcooperator_back">
          <div class="dialogs__childbox_fixed">
            <div class="dialogs__header">
              <div class="dialogs__header_title">
                <s:text name="psnweb.cooperator"></s:text>
              </div>
              <i class="list-results_close" onclick="Pub.closePubCooperatorBack();"></i>
            </div>
          </div>
          <div class="dialogs__childbox_adapted" style="height: 560px;">
            <div class="main-list__list item_no-border" list-main="pubcooperator"></div>
          </div>
        </div>
      </div>
      <div class="main-list" style="width: 100%; max-width: 960px;">
        <div class="main-list__header">
          <div class="main-list-header__title" style="display: flex;">
            <div class="main-list-header__title-item">
              <s:text name="project.number"></s:text>
              &nbsp;<span class="main-list-header__title-item_num" id="prjNumbers">0</span>
            </div>
            <div class="main-list-header__title-item" style="margin: 0px 8px 0px 16px;">
              <s:text name="project.amount"></s:text>
              &nbsp;<span class="main-list-header__title-item_num" id="prjTotalAmounts">0</span>
            </div>
            <div class="main-list-header__title-item_tip" style="color: #ccc !important;" title='<s:text name ="project.helpful.hints"/>'><s:text name="project.helpful.hints"></s:text> </div>
          </div>
        </div>
        <div class="main-list__list" list-main="prjlist"></div>
        <div class="main-list__footer">
          <div class="pagination__box" list-pagination="prjlist">
            <!-- 翻页 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</html>