<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend_${locale }.js"></script>
<script type="text/javascript">
var seniorityVal = "${seniority}";
var local='${locale}';
$(document).ready(function(){
	//addFormElementsEvents();
	var targetlist = document.getElementsByClassName("conditions");
	for(var i = 0; i < targetlist.length ;i++ ){
		targetlist[i].onmouseover =  function(){
			var text = this.innerText;
			if(text == null){
				text = this.textContent;
			}//兼容火狐
			this.setAttribute("title",text);
		}
	}
	$(".dev_delete_agency").each(function(){//绑定关注资助机构删除事件
		$(this).bind("click",function(){
			FundRecommend.deleteFundAgency(this);
			FundRecommend.loadRecommendFundList();
		});
	});
	$(".dev_delete_area").each(function(){//绑定科技领域删除事件
		$(this).bind("click",function(){
			FundRecommend.deleteFundArea(this);
			FundRecommend.loadRecommendFundList();
		});
	});
	$("li.item_list-align").each(function(){//显示删除图标
		$(this).hover(function(){
			if(!$(this).find(".item_list-align_item").hasClass("dev_slected")){
				$(this).find(".material-icons").toggle();		
			}
		});
	});
	$(".setting-btn_icon").each(function(){
		$(this).bind("click",function(){
			$(this).parent(".setting-title").next(".setting-list").toggle();
			if($(this).text()=="expand_more"){				
				$(this).text("expand_less");
			}else{
				$(this).text("expand_more");				
			}
		});
	});
	$(".item_list-align_item").each(function(){//绑定点击选中事件
		$(this).bind("click",function(){
			$("#firstIn").val("false");
			$(this).toggleClass("dev_slected");
			if($(this).hasClass("dev_slected")){
				$(this).next(".material-icons").hide();
				if($(this).attr("code") == 0 || $(this).hasClass("dev_timeFlag")){//不限时候取消其他的选中,时间是单选的
					$(this).closest("ul").find(".item_list-align_item:not([code='"+$(this).attr("code")+"'])").removeClass("dev_slected");
				}else{
					$(this).closest("ul").find(".item_list-align_item[code='0']").removeClass("dev_slected");
				}
			}else{
				$(this).next(".material-icons").show();
			}
			FundRecommend.loadRecommendFundList();
			return;
		});
	});
	
    var clicelemlist = document.getElementsByClassName("sort-container_item-list");
    for(var i = 0 ; i < clicelemlist.length;i++){
        clicelemlist[i].onclick = function(){//选择单位要求
          var seniorityName = $.trim(this.querySelector(".sort-container_item_name").innerHTML);
          this.closest(".sort-container").querySelector(".sort-container_header-title").innerHTML = seniorityName;
          var code=this.querySelector(".sort-container_item_name").getAttribute("code");          
          this.closest(".sort-container").querySelector(".sort-container_header-title").setAttribute("select_sen","true");
          this.closest(".sort-container").querySelector(".sort-container_header-title").setAttribute("code",code);
          this.closest(".sort-container").querySelector(".sort-container_header-title").setAttribute("title",seniorityName);
          saveSeniority(code);
        }     
    }

$(".dev_seniority").attr('title',$.trim($(".dev_seniority").text()));

FundRecommend.funPagingConditions();//资助机构翻页
});



function showAgencyCondition(){
	var selectAgencyCodes = "";
    $(".dev_agencyName").each(function(){
        var code = $(this).attr("code");
        if(code && code!="0"){
        	selectAgencyCodes += "," + code;  
        }
    }); 
    $.ajax({
        url : "/prjweb/fundAgency/ajaxeditfund",
        type : "post",
        dataType : "html",
        data : {
            "selectAgencyCodes" : selectAgencyCodes,
            "maxSelectFund" : 10
        },
        success : function(data) {
            $("#login_box_refresh_currentPage").val("false");//登录不跳转                
            BaseUtils.ajaxTimeOut(data , function(){
                $("#dialogsBox").html(data);
                showDialog("dialogsBox");
                addFormElementsEvents(document
                        .getElementById("research-agency-list"));
            });
        },
        error : function() {
        }
    });
};
function showAreaCondition(){
    var areaIds = "";
    $(".dev_scienceArea").each(function(){
    	var code = $(this).attr("code");
    	if(code && code!="0"){
	    	areaIds += "," + code;	
    	}
    });
    $.ajax({
        url : "/psnweb/sciencearea/ajaxaddarea",
        type : "post",
        dataType : "html",
        data : {
            "scienceAreaIds" : areaIds,
            "scienceAreaNum" : 3
        },
        success : function(data) {
            $("#login_box_refresh_currentPage").val("false");//登录不跳转                
            BaseUtils.ajaxTimeOut(data , function(){
                $("#dialogsBox").html(data);
                showDialog("dialogsBox");
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
    hideDialog("dialogsBox");
};
//隐藏
hideFundAgenceBox = function(obj) {
    hideDialog("dialogsBox");
};
saveScienceArea = function() {
	if($("#choosed_area_list").find(".main-list__item").length>3){
        scmpublictoast(fundRecommend.addAreaSizeFail, 1500);
        return;
	}
	if($("#choosed_area_list").find(".main-list__item").length==0){
        scmpublictoast(fundRecommend.addAreaEmptyFail, 1500);
        return;
	}
    setTimeout(function() {
		var codes = "";
	    $("#choosed_area_list").find(".main-list__item").each(function(index) {
	    	codes += $(this).attr("areaid")+",";
	    });      
	    codes = codes.replace(/,$/, "");
	    
	    $.ajax({
	        url:"/prjweb/fundconditions/ajaxsavefundarea",
	        type:"post",
	        data:{
	            "areaCodes":codes,
	        },
	        dataType:"json",
	        success : function(data){
	            if(data.result=="success"){
	                if(data.areaList){
	                    var areaList=data.areaList;
	                    var _unlimit = $(".dev_scienceArea[code='0']").parent("li").clone(true);
	                    $(".dev_scienceArea[code!='clone']").parent("li").remove();
	                    $.each(areaList,function(n,item){               
	                        var _addLi = $(".dev_area_clone").find("li").clone(true);
	                        _addLi.find(".dev_scienceArea").attr("code",item["scienceAreaId"]).attr("title",item["showName"]).text(item["showName"]);       
	                        $(".dev_area_ul").append(_addLi).show();
	                    });
	                    $(".dev_area_ul").append(_unlimit).show();  
	                }
	                FundRecommend.loadRecommendFundList();
	            }else{
	                scmpublictoast(fundRecommend.addFail,2000); 
	            }
	        }
	    });
    }, 100);
    hideDialog("dialogsBox");
};
function saveFundAgence(){
	if($("#choosed_agency_list").find(".main-list__item").length>10){
        scmpublictoast(fundRecommend.addFundSizeFail, 1500);
        return;
    }
    if($("#choosed_agency_list").find(".main-list__item").length==0){
        scmpublictoast(fundRecommend.addFundEmptyFail, 1500);
        return;
    }
    setTimeout(function() {
        var codes = "";
        $("#choosed_agency_list").find(".main-list__item").each(function(index) {
            codes += $(this).attr("agencyid")+",";
        });      
        codes = codes.replace(/,$/, "");
        
        $.ajax({
            url:"/prjweb/fundconditions/ajaxsaveagencyInterest",
            type:"post",
            data:{
                "saveAgencyIds":codes,
            },
            dataType:"json",
            success : function(data){
                if(data.result=="success"){
                    if(data.allFundAgencyInterest){
                        var areaList=data.allFundAgencyInterest;
                        var _unlimit = $(".dev_agencyName[code='0']").parent("li").clone(true);
                        $(".dev_agencyName[code!='clone']").parent("li").remove();
                        $.each(areaList,function(n,item){               
                            var _addLi = $(".dev_agency_clone").find("li").clone(true);
                            _addLi.find(".dev_agencyName").attr("code",item["agencyId"]).attr("title",item["showName"]).text(item["showName"]);       
                            $(".dev_agency_ul").append(_addLi).show();
                        });
                        $(".dev_agency_ul").append(_unlimit).show();  
                    }
                    FundRecommend.funPagingConditions();//资助机构翻页
                    FundRecommend.loadRecommendFundList();
                }else{
                    scmpublictoast(fundRecommend.addFail,2000); 
                }
            }
        });
    }, 100);
    hideDialog("dialogsBox");
};
function saveSeniority(code){
	$.ajax({
		url:"/prjweb/fundconditions/ajaxsaveseniority",
		type:"post",
		data:{"seniorityCode" : code},
		dataType:"json",
		success:function(data){
		    BaseUtils.ajaxTimeOut(data, function(){
				if(data.result == "success"){
					FundRecommend.loadRecommendFundList();
				}
		    });
		},
		error:function(){
			
		}
	});
}
</script>
<input type="hidden" name="firstIn" value="true" id="firstIn" />
<div id="conditions_div-container" style="width: 270px; height: auto;">
  <div class="l_setting_tit">
    <s:text name='homepage.fundmain.recommend.set' />
  </div>
  <div class="setting">
    <div class="setting-parent">
      <div class="setting-title" style="padding-left: 0px; width: 220px;">
        <strong><s:text name='homepage.fundmain.recommend.fund' /></strong> <i class="material-icons setting-btn_icon">expand_less</i>
      </div>
      <div class="setting-list condition-list">
        <div class="setting-list_searchbtn" onclick="showAgencyCondition()">
        <s:text name='homepage.fundmain.recommend.fundagency' />
        </div>
        <ul class="dev_agency_ul">
          <s:if test="fundAgencyInterestList != null && fundAgencyInterestList.size() > 0">
            <s:iterator value="fundAgencyInterestList" id="agency" status="sta">
              <s:if test="#agency.showName != null && #agency.showName != ''">
                <li class="item_list-align item_list-container dev_li_agencyName setting-list_page-item"
                  style="width: 220px !important;">
                  <div class="item_list-align_item dev_agencyName" code="${agency.agencyId}"
                    title='${agency.showName }' style="width: 220px !important;">
                    ${agency.showName }
                  </div> <!-- 	            		 <i class="material-icons filter-value__cancel dev_delete_agency" style="display:none;" onclick="">close</i>
 -->
                </li>
              </s:if>
            </s:iterator>
          </s:if>
        </ul>
        <div class="setting-list_page">
          <div class="setting-list_page-btn setting-list_page-up" title="上一页">
            <i class="material-icons">keyboard_arrow_left</i>
          </div>
          <div class="setting-list_page-btn setting-list_page-down" title="下一页">
            <i class="material-icons">keyboard_arrow_right</i>
          </div>
        </div>
        <!-- 节点复制111-->
        <div class="dev_agency_clone" style="display: none">
          <li class="item_list-align item_list-container dev_li_agencyName setting-list_page-item"
            style="width: 220px !important;">
            <div class="item_list-align_item dev_agencyName" code="clone" title='' style="width: 220px !important;">
            </div> <!-- 	                <i class="material-icons filter-value__cancel dev_delete_agency" style="display:none;" onclick="">close</i> -->
          </li>
        </div>
        <!-- 节点复制-->
      </div>
    </div>
    <div class="setting-parent">
      <div class="setting-title" style="padding-left: 0px; width: 220px;">
        <strong><s:text name='homepage.fundmain.recommend.areas' /></strong> <i class="material-icons setting-btn_icon">expand_less</i>
      </div>
      <div class="setting-list condition-list">
        <div class="search-box_container js_autocompletebox setting-list_searchbtn" onclick="showAreaCondition()">
          <s:text name='homepage.fundmain.recommend.areas' />
          </div>
        <ul class="dev_area_ul">
          <s:if test="fundAreaList != null && fundAreaList.size() > 0">
            <s:iterator value="fundAreaList" id="sca" status="sta">
              <s:if test="#sca.showName != null && #sca.showName != ''">
                <li class="item_list-align item_list-container dev_li_scienceArea" style="width: 220px !important;">
                  <div class="item_list-align_item dev_scienceArea" code="${sca.scienceAreaId}"
                    title='${sca.showName }' style="width: 220px !important;">
                    ${sca.showName }
                  </div> <!-- 	            		 <i class="material-icons filter-value__cancel dev_delete_area" style="display:none;" onclick="">close</i>
 -->
                </li>
              </s:if>
            </s:iterator>
          </s:if>
        </ul>
        <!-- 节点复制-->
        <div class="dev_area_clone" style="display: none">
          <li class="item_list-align item_list-container dev_li_scienceArea" title='' style="width: 220px !important;">
            <div class="item_list-align_item dev_scienceArea" code="clone" title='' style="width: 220px !important;">
            </div> <!--            		 <i class="material-icons filter-value__cancel dev_delete_area" style="display:none;" onclick="">close</i>
 -->
          </li>
        </div>
        <!-- 节点复制-->
      </div>
    </div>
    <div class="setting-parent">
      <div class="setting-title" style="padding-left: 0px; width: 220px;">
        <strong><s:text name='homepage.fundmain.recommend.eligibility' /></strong> <i
          class="material-icons setting-btn_icon">expand_less</i>
      </div>
      <div class="sort-container js_filtersection setting-list">
        <div class="sort-container_header" style="${locale eq 'en_US' ? 'width:220px;' : ''} width: 220px;">
          <div class="sort-container_header-title filter-section__title dev_seniority"
            style="${locale eq 'en_US' ? 'width:120px;' : ''} white-space: nowrap;overflow: hidden;text-overflow: ellipsis; display: block; text-align: left; width: 198px;"
            select_sen="false" code="${seniorityCode}" title="">
            <c:if test="${seniorityCode == 1}">
              <s:text name='homepage.fundmain.recommend.enterprise' />
            </c:if>
            <c:if test="${seniorityCode == 2}">
              <s:text name='homepage.fundmain.recommend.institution' />
            </c:if>
            <c:if test="${seniorityCode == 0}">
              <s:text name='homepage.fundmain.recommend.unlimit' />
            </c:if>
          </div>
          <div class="sort-container_header-tip">
            <i class="sort-container_header-flag sort-container_header-up"></i><i class="sort-container_header-down"></i>
          </div>
        </div>
        <div class="sort-container_item"
          style="${locale eq 'en_US' ? 'width:108px;' : ''}; z-index: 999; width: 218px!important;">
          <div class="filter-list vert-style option_has-stats" list-filter="psnpub">
            <div class="filter-list__section js_filtersection" filter-section="orderBy" filter-method="compulsory"
              style="margin: 0; padding: 0;">
              <ul class="filter-value__list">
                <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="publishYear">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption sort-container_item_name" code="1"
                    style="font-size: 14px !important;">
                    <s:text name='homepage.fundmain.recommend.enterprise' />
                  </div>
                  <div class="filter-value__stats js_filterstats"></div>
                </li>
                <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="publishYear">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption sort-container_item_name" code="2"
                    style="font-size: 14px !important;"
                    title="<s:text name='homepage.fundmain.recommend.institution' />">
                    <s:text name='homepage.fundmain.recommend.institution' />
                  </div>
                  <div class="filter-value__stats js_filterstats"></div>
                </li>
                <li class="filter-value__item js_filtervalue sort-container_item-list" filter-value="publishYear">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption sort-container_item_name dev_seniority_unlimit"
                    code="0" style="font-size: 14px !important;">
                    <s:text name='homepage.fundmain.recommend.unlimit' />
                  </div>
                  <div class="filter-value__stats js_filterstats"></div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <%--   <div class="setting-list condition-list">
        <select class="dev_seniority" onchange="saveSeniority(this)">
          <option  value = "1" <c:if test="${seniorityCode == 1}">selected = "selected"</c:if> ><s:text name='homepage.fundmain.recommend.enterprise' /></option>
          <option  value = "2" <c:if test="${seniorityCode == 2}">selected = "selected"</c:if> ><s:text name='homepage.fundmain.recommend.institution' /></option>
          <option  value = "0" <c:if test="${seniorityCode == 0}">selected = "selected"</c:if> ><s:text name='homepage.fundmain.recommend.unlimit' /></option>
        </select>
        </div> --%>
    </div>
    <div class="setting-parent">
      <div class="setting-title" style="padding-left: 0px; width: 220px;">
        <strong><s:text name='homepage.fundmain.recommend.endDate' /></strong> <i
          class="material-icons setting-btn_icon">expand_less</i>
      </div>
      <div class="setting-list condition-list">
        <ul>
          <li class="item_list-align item_list-container" style="width: 220px !important;">
            <div class="item_list-align_item dev_timeFlag" code="1" style="width: 220px !important;">
              <s:text name='homepage.fundmain.recommend.week' />
            </div>
          </li>
          <li class="item_list-align item_list-container" style="width: 220px !important;">
            <div class="item_list-align_item dev_timeFlag" code="2" style="width: 220px !important;">
              <s:text name='homepage.fundmain.recommend.month' />
            </div>
          </li>
          <li class="item_list-align item_list-container" style="width: 220px !important;">
            <div class="item_list-align_item dev_timeFlag" code="3" style="width: 220px !important;">
              <s:text name='homepage.fundmain.recommend.months' />
            </div>
          </li>
          <li class="item_list-align item_list-container" style="width: 220px !important;">
            <div class="item_list-align_item dev_timeFlag" code="0" style="width: 220px !important;">
              <s:text name='homepage.fundmain.recommend.unlimit' />
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
