<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>项目列表</title>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend_${locale}.js"></script>
  <script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base.js"></script>
  <script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base_${locale }.js"></script>
  <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_chipbox.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script src="${resmod}/js/loadStateIco.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript">
        var jsessionId = "<%=session.getId() %>";
        var $createGrpChipBox;
        $(function () {
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
          
            project.prjNumberAmount('${des3CurrentId}');
            project.recommendList('${des3CurrentId}');
            project.ajaxprjcooperation('${des3CurrentId}');
            $createGrpChipBox = window.ChipBox({name: "chipcodeprjcreate", maxItem: 10});
            addFormElementsEvents();
            $(".filter-list__section[filter-section=fundingYear] .filter-value__list").hide();
            var changflag = document.getElementsByClassName("filter-section__header");
            for (var i = 0; i < changflag.length; i++) {
                changflag[i].onclick = function () {
                    if (this.querySelector(".filter-section__toggle").innerHTML == "expand_less") {
                        this.querySelector(".filter-section__toggle").innerHTML = "expand_more";
                        this.closest(".filter-list__section").querySelector(".filter-value__list").style.display = "none";
                    } else {
                        this.querySelector(".filter-section__toggle").innerHTML = "expand_less";
                        this.closest(".filter-list__section").querySelector(".filter-value__list").style.display = "block";
                    }
                }
            }

            var targetlist = document.getElementsByClassName("searchbox__main");
            for (var i = 0; i < targetlist.length; i++) {
                targetlist[i].querySelector("input").onfocus = function () {
                    this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
                }
                targetlist[i].querySelector("input").onblur = function () {
                    this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";
                }
            }
            $(function () {
                if (document.getElementsByClassName("main-list-header__title-item_tip").length > 0) {
                    var hoverele = document.getElementsByClassName("main-list-header__title-item_tip")[0];
                    hoverele.onmouseover = function () {
                        this.title = this.innerHTML;
                    }
                }
                var clicelemlist = document.getElementsByClassName("sort-container_item-list");
                for (var i = 0; i < clicelemlist.length; i++) {
                    clicelemlist[i].onclick = function () {
                        this.closest(".sort-container").querySelector(".sort-container_header-title").innerHTML = this.querySelector(".sort-container_item_name").innerHTML;
                    }
                }
                var href=window.location.href;
                if(href.indexOf("prjweb/project/prjmain") > 0){
                  $("#prjSearchboxDiv").css("margin-top","14px");
                }
            });
            document.onkeydown = function(event){
                if(event.keyCode == 27){
                    event.stopPropagation();
                    event.preventDefault();
                    closeSelectPrjFileMethod();
                }
            }
        });
        
        //显示创建群组的弹出框
        function showCreateGrp(obj, externalNo, keywords) {
            BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
	            $("div#disciplinesAndKeywords").remove();
	            showDialog("create_grp_ui");
	            var title = $(obj).closest(".main-list__item").find("input[name='prjTitle__']").val();
	            var prjAbstracts = $(obj).closest(".main-list__item").find("input[name='prjAbstracts__']").val();
	            var prjId = $(obj).closest(".main-list__item").attr("drawer-id");
	            var secondCategoryId = $(obj).closest(".main-list__item").find("input[name='secondCategoryId']").val();
	            $(".input_grpname_val").val(title);
	            $(".input_projectno_val").val(externalNo);
	            $(".input_grpdescription_val").val(prjAbstracts);
	            $("#prjIds").val("");
	            $("#prjIds").val(prjId);
	            if (keywords != "") {
	                project.showDisciplinesAndKeywordsPrjList(keywords);
	            }
	            if(secondCategoryId != ""){
                    project.showDisciplines(obj);
                }else{
                    project.hideDisciplines(obj);
                }
	            $createGrpChipBox.chipBoxInitialize();
	            // 初始化弹出框中的数据
	            updateDialogData();
	            addFormElementsEvents($("#create_grp_ui")[0]);
            }, 1);
        }

        var defaultDisciplineName = '<s:text name="project.choose.the.first.level.discipline" />';
        //隐藏创建群组的弹出框
        function hideCreateGrp() {
            $(".chip__box").remove();
/*             var $selectBox1 = document.querySelector('.sel__box[selector-id="1st_discipline"]');
            $selectBox1.setAttribute("sel-value","");
            $(".sel__box .sel__value_selected").text(defaultDisciplineName);
            var $selectBox2 = document.querySelector('.sel__box[selector-id="2nd_discipline"]');
            $selectBox2.setAttribute("sel-value","");
            $selectBox2.style.visibility="hidden"; */
            $createGrpChipBox.resetChipBoxData();
            hideDialog("create_grp_ui");
        }
        
        function updateDialogData(){
        	//遍历所有的文本框或者输入框，添加class
        	$("#create_grp_ui .input__box").find("input").each(function(index,element){
        		var content = $.trim($(element).val());
        		if (content != "" && content != null && content != undefined) {
        			$(".input__box").eq(index).addClass("input_not-null");
                } else {
                	$(".input__box").eq(index).removeClass("input_not-null");
                }
        	});
        	$("#create_grp_ui .input__box").find("textarea").each(function(index,element){
        		var content = $.trim($(element).val());
                if (content != "" && content != null && content != undefined) {
                    $(".input__box").eq(index).addClass("input_not-null");
                } else {
                    $(".input__box").eq(index).removeClass("input_not-null");
                }
            });
        }

        //显示添加项目
        function addprjFile() {
            BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
                showDialog("select_import_grp_file_method");
            }, 1);
        }

        //隐藏添加项目弹出框
        function closeSelectPrjFileMethod() {
            hideDialog("select_import_grp_file_method");
        }

        //关闭上传全文的弹出框
        function closeNoPrjFullTextList() {
            hideDialog("noprjfulltextlist");
            $prjlist.reloadCurrentPage();
            $prjlist.initializeDrawer();
        }

        //上传全文确定上传成功
        function enSureNoPrjFullTextList() {
            hideDialog("noprjfulltextlist");
            $prjlist.reloadCurrentPage();
            $prjlist.initializeDrawer();
        }

        //关闭合作者列表弹出框
        function closePrjAllCooperationt() {
            hideDialog("prjCooperation");
            $prjlist.reloadCurrentPage();
            $prjlist.initializeDrawer();
        }

        //关闭上传全文
        function closeFulltext() {
            $("div#hidefulltext").remove();
        }

        function prjEdit(des3Id) {
            // 保存过滤条件
            $prjlist.setCookieValues();
            var forwardUrl = "/prjweb/prj/edit?des3Id=" +des3Id;
            BaseUtils.forwardUrlRefer(true, forwardUrl);
        }
        function shareCallback(dynId, textConent){
        	
        }
        
        function showAreaCondition(){
          var areaIds = $("#2nd_discipline").val();
          $.ajax({
              url : "/psnweb/sciencearea/ajaxaddarea",
              type : "post",
              dataType : "html",
              data : {
                  "scienceAreaIds" : areaIds,
                  "scienceAreaNum" : 1
              },
              success : function(data) {
                  $("#login_box_refresh_currentPage").val("false");//登录不跳转                
                  BaseUtils.ajaxTimeOut(data , function(){
                      $("#dialogs__box").html(data);
                      showDialog("dialogs__box");
                      addFormElementsEvents(document
                              .getElementById("research-area-list"));
                  });
              },
              error : function() {
              }
          });
      };
      // 隐藏
      hideScienceAreaBox = function(obj) {
          hideDialog("dialogs__box");
          $("#1st_discipline").val("");
          $("#2nd_discipline").val("");
      };
      saveScienceArea = function() {
        if($("#choosed_area_list").find(".main-list__item").length==0){
          scmpublictoast(fundRecommend.addAreaEmptyFail, 1500);
          return;
        }
        $("#research-area-list").find(".dev_i_has_select").each(function(){
          if($(this).text()=="check"){
            var firstName = $(this).closest(".dev_first_area_list").attr("name");
            var secondName = $(this).closest(".nav-cascade__item").attr("name");
            $("#1st_discipline").val($(this).attr("parentid"));
            $("#2nd_discipline").val($(this).attr("value"));            
            setTimeout(function() {
                $(".dev_sel__box[selector-id='1st_discipline']").find(".sel__value_selected").html(firstName);
                $(".dev_sel__box[selector-id='2nd_discipline']").css("visibility", "");
                $(".dev_sel__box[selector-id='2nd_discipline']").find(".sel__value_selected").html(secondName);
            }, 100);
          }
        });
        hideDialog("dialogs__box");
      };
    </script>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
  <input type="hidden" id="othersSee" value="${othersSee}">
  <input type="hidden" id="des3CurrentId" value="${des3CurrentId}">
  <input type="hidden" id="prjIds" value="">
  <div class="container__horiz">
    <c:if test="${OthersSee !=true}">
    </c:if>
    <div style="display: flex; width: 100%;">
      <div id="prjSearchboxDiv" style="width: 280px; height: auto; margin-right: 16px;">
        <div class="module-home__fixed-layer_filter" style="width: 265px;">
          <div class="searchbox__container main-list__searchbox" list-search="prjlist" style="width: 90%;">
            <div class="searchbox__main">
              <input placeholder='<s:text name="project.search"/>'>
              <div class="searchbox__icon material-icons"></div>
            </div>
          </div>
          <div class="filter-list vert-style option_has-stats filter-list__specStyle" list-filter="prjlist">
            <!-- =============================所在机构_end -->
            <!-- =============================所在地区_start -->
            <div class="filter-list__section js_filtersection" filter-method="multiple" filter-section="agency">
              <div class="filter-section__header">
                <div class="filter-section__title">
                  <s:text name="project.agency"></s:text>
                </div>
                <i class="material-icons filter-section__toggle">expand_less</i>
              </div>
              <ul id="agencyNames" class="filter-value__list">
              </ul>
            </div>
            <div class="filter-list__section js_filtersection" filter-section="fundingYear" filter-method="single">
              <div class="filter-section__header">
                <div class="filter-section__title">
                  <s:text name="project.filter.fundYear"></s:text>
                </div>
                <i class="material-icons filter-section__toggle">expand_more</i>
              </div>
              <ul class="filter-value__list">
                <li class="filter-value__item js_filtervalue" filter-value="${currentYear }">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption" title="">
                    <s:text name="project.oneyear"></s:text>
                  </div>
                  <div class="filter-value__stats js_filterstats">(0)</div> <i
                  class="material-icons filter-value__cancel filter_need_cancel js_filtercancel">close</i>
                </li>
                <li class="filter-value__item js_filtervalue" filter-value="${currentYear-2 }">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption" title="">
                    <s:text name="project.threeyear"></s:text>
                  </div>
                  <div class="filter-value__stats js_filterstats">(0)</div> <i
                  class="material-icons filter-value__cancel filter_need_cancel js_filtercancel">close</i>
                </li>
                <li class="filter-value__item js_filtervalue" filter-value="${currentYear-4 }">
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
      </div>
      <div class="main-list" style="width: 100%; max-width: 960px;">
        <div class="main-list__header">
          <div class="main-list-header__title" style="display: flex; font-size: 15px;">
            <div class="main-list-header__title-item">
              <s:text name="project.number"></s:text>
              &nbsp;<span class="main-list-header__title-item_num" id="prjNumbers">0</span>
            </div>
            <div class="main-list-header__title-item" style="margin: 0px 8px 0px 16px;">
              <s:text name="project.amount"></s:text>
              &nbsp;<span class="main-list-header__title-item_num" id="prjTotalAmounts">0</span>
            </div>
            <c:if test="${OthersSee ==true}">
              <div class="main-list-header__title-item_tip" style="color: #ccc !important;" title='<s:text name="project.helpful.hints"/>'><s:text name="project.helpful.hints"></s:text></div>
            </c:if>
          </div>
          <div class="sort-container js_filtersection">
            <div class="sort-container_header" style="${locale eq 'en_US' ? 'width:130px;' : ''}">
              <div class="sort-container_header-tip">
                <i class="sort-container_header-flag sort-container_header-up"></i><i class="sort-container_header-down"></i>
              </div>
              <div class="sort-container_header-title filter-section__title"
                style="${locale eq 'en_US' ? 'width:120px;' : ''}">
                <s:text name="project.filter.recentModify" />
              </div>
            </div>
            <div class="sort-container_item"
              style="${locale eq 'en_US' ? 'width:108px;' : ''} z-index: 99; width: 218px;">
              <div class="filter-list vert-style option_has-stats" list-filter="prjlist">
                <div class="filter-list__section js_filtersection" filter-section="orderBy" filter-method="compulsory"
                  style="margin: 0; padding: 0;">
                  <ul class="filter-value__list">
                    <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="updateDate">
                      <div class="input-custom-style">
                        <input type="checkbox"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option js_filteroption sort-container_item_name"
                        style="font-size: 14px !important;">
                        <s:text name="project.filter.recentModify" />
                      </div>
                      <div class="filter-value__stats js_filterstats"></div>
                    </li>
                    <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="publishYear">
                      <div class="input-custom-style">
                        <input type="checkbox"><i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option js_filteroption sort-container_item_name"
                        style="font-size: 14px !important;">
                        <s:text name="project.filter.fundYear" />
                      </div>
                      <div class="filter-value__stats js_filterstats"></div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <c:if test="${OthersSee !=true}">
            <button class="button_main button_primary-reverse" onclick="addprjFile()">
              <s:text name="project.add"></s:text>
            </button>
          </c:if>
        </div>
        <div class="main-list__list" list-main="prjlist"></div>
        <div class="main-list__footer">
          <div class="pagination__box" list-pagination="prjlist">
            <!-- 翻页 -->
          </div>
        </div>
      </div>
    </div>
    <div class="dialogs__box dialogs__childbox_limited-normal " style="width: auto;" dialog-id="select_import_grp_file_method"
      id="select_import_grp_file_method">
      <div class="dialogs__childbox_fixed">
        <div class="dialogs__header">
          <div class="dialogs__header_title">
            <s:text name="project.select.import.mode"></s:text>
          </div>
          <i class="list-results_close " onclick="closeSelectPrjFileMethod()"></i>
        </div>
      </div>
      <div class="dialogs__childbox_adapted">
        <div class="import-methods__box">
            <div class="import-methods__sxn ">
                <div class="import-methods__sxn_logo online-search" onclick ="project.onlineSearch()"></div>
                <div class="import-methods__sxn_name"><s:text name="project.online.search"></s:text></div>
                <div class="import-methods__sxn_explain"><s:text name="project.seach.import.description"></s:text></div>
            </div>
            <div class="import-methods__sxn ">
            <div class="import-methods__sxn_logo Batch-processing" onclick ="project.batchImport()"></div>
            <div class="import-methods__sxn_name">
                  <s:text name="project.manual.batch"></s:text>
             </div>
            <div class="import-methods__sxn_explain">
                  <s:text name="project.manual.batch.description"></s:text> 
            </div>
            </div>
            <div class="import-methods__sxn">
            <div class="import-methods__sxn_logo manual-entry" onclick="project.manualEntry()"></div>
            <div class="import-methods__sxn_name">
              <s:text name="project.manual.entry"></s:text>
            </div>
            <div class="import-methods__sxn_explain" style="margin-left: 17px">
              <s:text name="project.manual.import.description"></s:text>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="dialogs__box" style="width: 800px;" dialog-id="create_grp_ui" id="create_grp_ui">
      <div class="dialogs__childbox_fixed">
        <div class="dialogs__header">
          <div class="dialogs__header_title">
            <s:text name="project.create.group"></s:text>
          </div>
        </div>
      </div>
      <div class="dialogs__childbox_adapted">
        <div class="dialogs__content global__padding_24">
          <form>
            <div class="form__sxn_row">
              <div class="input__box">
                <label class="input__title" id="prjtitle"><s:text name="project.title"></s:text></label>
                <div class="input__area">
                  <input class="input_grpname_val" required maxlength="199">
                </div>
                <div class="input__helper" helper-text="" invalid-message=""></div>
              </div>
            </div>
            <div class="form__sxn_row">
              <div class="input__box">
                <label class="input__title"><s:text name="project.approval.number"></s:text></label>
                <div class="input__area">
                  <input class="input_projectno_val" required maxlength="99">
                </div>
                <div class="input__helper" helper-text="" invalid-message=""></div>
              </div>
            </div>
            <div class="form__sxn_row">
              <div class="input__box">
                <label class="input__title"><s:text name="project.paper"></s:text></label>
                <div class="input__area">
                  <textarea class="input_grpdescription_val" style="line-height: 18px; padding-bottom: 2px;" maxlength="1999"></textarea>
                  <div class="textarea-autoresize-div"></div>
                </div>
                <div class="input__helper" invalid-message=""></div>
              </div>
            </div>
            <div class="form__sxn_row">
              <input type="hidden" id="1st_discipline" value=""/>
              <input type="hidden" id="2nd_discipline" value=""/>
              <div class="input__box no-flex input_not-null">
                <label class="input__title"><s:text name="project.research.field"></s:text></label>
                <div class="dev_sel__box" selector-id="1st_discipline" onclick="showAreaCondition();">
                  <div class="sel__value" style="display: flex; align-items: center;">
                    <span class="sel__value_selected sel__value_placeholder sel__value_selected-content"
                      style="padding-bottom: 4px; border-bottom: 1px solid #999; color: #333 !important; font-size: 14px;">
                      <s:text name="project.choose.the.first.level.discipline"></s:text>
                    </span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
                  </div>
                </div>
                <div class="input__helper" invalid-message=""></div>
              </div>
              <div class="input__box no-flex input_not-null">
                <label class="input__title"></label>
                <div class="dev_sel__box" selector-id="2nd_discipline"  onclick="showAreaCondition();" style="visibility: hidden">
                  <div class="sel__value" style="display: flex; align-items: center;">
                    <span class="sel__value_selected sel__value_placeholder sel__value_selected-content"
                      style="padding-bottom: 4px; border-bottom: 1px solid #999; color: #333 !important; font-size: 14px;">
                      <s:text name="project.choose.secondary.disciplines"></s:text>
                    </span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
                  </div>
                </div>
                <div class="input__helper" invalid-message=""></div>
              </div>
            </div>
            <div class="form__sxn_row">
              <div class="input__box no-input-area">
                <label class="input__title"><s:text name="project.keywords"></s:text></label>
                <div class="chip-panel__box" id="create_keywords" chipbox-id="chipcodeprjcreate">
                  <!-- 关键词 -->
                  <div class="chip-panel__manual-input js_autocompletebox" id="autokeywords"
                    request-url="/groupweb/mygrp/ajaxautoconstkeydiscs" contenteditable="true"></div>
                </div>
                <div class="global__para_caption"><s:text name="project.keywords.describe"></s:text></div>
              </div>
            </div>
            <div class="form__sxn_row" style="flex-direction: column;">
              <div class="input__box no-input-area">
                <label class="input__title"><s:text name="project.set.group.permissions"></s:text></label>
                <div class="input-radio__box_vert" style="display: flex; flex-direction: inherit; padding: 0px;">
                  <div class="input-radio__sxn"  onclick="grpChangePublic('myGrp')" title="<s:text name='project.public.describe'/>">
                    <div class="input-custom-style">
                      <input type="radio" name="choose-authority" value="O"> <i
                        class="material-icons custom-style"></i>
                    </div>
                    <div class="input-radio__label">
                      <s:text name="project.public"></s:text>
                    </div>
                  </div>
                  <div class="input-radio__sxn" onclick="grpChangeSemiPublic('myGrp')"  style="margin: 0px 48px;" title="<s:text name='project.semi.public.describe'/>">
                    <div class="input-custom-style">
                      <input type="radio" value="H" name="choose-authority" checked=""> <i
                        class="material-icons custom-style"></i>
                    </div>
                    <div class="input-radio__label">
                      <s:text name="project.semi.public"></s:text>
                    </div>
                  </div>
                  <div class="input-radio__sxn" onclick="grpChangeSemiPrivacy('myGrp')" title="<s:text name='project.privacy.describe'/>">
                    <div class="input-custom-style">
                      <input type="radio" value="P" name="choose-authority"> <i
                        class="material-icons custom-style"></i>
                    </div>
                    <div class="input-radio__label">
                      <s:text name="project.privacy"></s:text>
                    </div>
                  </div>
                </div>
              </div>
              <div>
                <div id="grpEditPublicDescribe" style="display: none;">
                  <div class="input-radio_grp-auth">
                    <div class="input-radio__box_horiz" id="openType">
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox" checked="checked" disabled="disabled" name="isIndexDiscussOpen1">
                          <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          首页</div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox" checked="checked" name="isIndexMemberOpen1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          成员</div>
                      </div>
                      <!--  项目群组 成员，成果，文献，文件   -->
                      <span style="display: flex;" name="project_module">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isPrjPubShow1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        成果</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox"  checked="checked" name="isPrjRefShow1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文献</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isIndexFileOpen1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文件</div>
                    </div></span>
                    </div>
                  </div>
                </div>
                <div id="grpEditSemiPublicDescribe" style="display: block;">
                  <div class="input-radio_grp-auth">
                    <div class="input-radio__box_horiz" id="halfType">
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox" checked="checked" disabled="disabled" name="isIndexDiscussOpen1">
                          <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          首页</div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox" checked="checked" name="isIndexMemberOpen2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          成员</div>
                      </div>
                      <!--  项目群组 成员，成果，文献，文件   -->
                      <span style="display: flex;" name="project_module">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isPrjPubShow2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        成果</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox"  checked="checked" name="isPrjRefShow2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文献</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isIndexFileOpen2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文件</div>
                    </div>
                    </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="dialogs__childbox_fixed">
        <div class="dialogs__footer">
          <button class="button_main button_primary-reverse" onclick="project.doGrpBaseCreate()">
            <s:text name="project.create.group.ensure"></s:text>
          </button>
          <button class="button_main button_primary-cancle" onclick="hideCreateGrp()">
            <s:text name="project.create.group.cancel"></s:text>
          </button>
        </div>
      </div>
    </div>
  </div>
  <div class="sel-dropdown__box" selector-data="1st_discipline">
    <div class="sel-dropdown__list">
      <div class="sel-dropdown__item" sel-itemvalue="1" onclick="project.resetSecondDiscipline()">
        <s:text name="project.agricultural.science"></s:text>
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="2" onclick="project.resetSecondDiscipline()">
        <s:text name="project.neo-confucianism"></s:text>
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="3" onclick="project.resetSecondDiscipline()">
        <s:text name="project.humanities.and.social.science"></s:text>
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="4" onclick="project.resetSecondDiscipline()">
        <s:text name="project.economy.and.management"></s:text>
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="5" onclick="project.resetSecondDiscipline()">
        <s:text name="project.engineering"></s:text>
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="6" onclick="project.resetSecondDiscipline()">
        <s:text name="project.information.science"></s:text>
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="7" onclick="project.resetSecondDiscipline()">
        <s:text name="project.medical.science"></s:text>
      </div>
    </div>
  </div>
  <div class="sel-dropdown__box" selector-data="2nd_discipline" data-src="request"
    request-url="/groupweb/mygrp/ajaxgetseconddiscipline" request-data="project.getjson()"></div>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="dialogs__box" style="width: 720px;" cover-event="" id="dialogs__box"
    process-time="0"></div>
  <!-- 科技领域弹出框 -->    
</body>
</html>
