<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var seniorityVal = "${seniority}";
$(document).ready(function(){
	addFormElementsEvents();
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
	$(".dev_delete_region").each(function(){//绑定关注地区删除事件
		$(this).bind("click",function(){
			FundRecommend.deleteFundRegion(this);
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
});



function callbackRegion(){
	var regionStr = $("#region_input").val();
	var regionCode = $("#region_input").attr("code");
	$.ajax({
		url:"/prjweb/fundconditions/ajaxsavefundregion",
		type:"post",
		data:{
			"regionCode":regionCode,
		},
		dataType:"json",
		success:function(data){
			if(data.result == "success"){
				if(data.regionCodeList && data.allRegionList){
	      			var regionCodeList=data.regionCodeList;//选中的region
	      			var allRegionList=data.allRegionList;//全部的region

   	      			$(".dev_regionName.dev_slected").each(function(){
   	      				regionCodeList.push($(this).attr("code"))
   	      			});
      				var _unlimit = $(".dev_regionName[code='0']").parent("li").clone(true);
      				$(".dev_regionName[code='0']").parent("li").remove();
      				for(var i=0;i<allRegionList.length;i++){
      					var item = allRegionList[i];
      					var code = item["regionId"]
      					$(".dev_regionName[code='"+code+"']").parent("li").remove();
      					var isSelect = false;//是否选中
          				for(var j=0;j<regionCodeList.length;j++){
          					if(regionCodeList[j] == code){
          						isSelect = true;
          						break;
          					}
          				}
          				var _addLi = $(".dev_region_clone").find("li").clone(true);
          				_addLi.find(".dev_regionName").attr("code",item["regionId"]).attr("title",item["showName"]).text(item["showName"]);	       					
      					_addLi.find(".material-icons").hide();
      					if(isSelect){//是否选中
      						_addLi.find(".dev_regionName").addClass("dev_slected");      						
      					}
      					$(".dev_region_ul").append(_addLi).show();
      				}
      				$(".dev_region_ul").append(_unlimit);
	      				
				}
				$("#region_input").val("");
				FundRecommend.loadRecommendFundList();
			}else{
				scmpublictoast(fundRecommend.addFail,2000);
			}
			$("#region_input").focus();
		},
		error:function(){
			scmpublictoast(fundRecommend.addFail,2000);
		}
	});
	
};

function callbackArea(){
	var areaStr = $("#area_input").val();
	var areaCode = $("#area_input").attr("code");
	if($(".dev_scienceArea").length < 5){//不限和隐藏的有两个
		$.ajax({
			url:"/prjweb/fundconditions/ajaxsavefundarea",
			type:"post",
			data:{
				"areaCode":areaCode,
			},
			dataType:"json",
			success : function(data){
				if(data.result=="success"){
					if(data.areaList){
	      				$(".dev_scienceArea").each(function(n,val){
	      					var code = $(this).attr("code");		      			
	      					if(code == areaCode){//输入添加的且重复的   
      							$(this).parent(".dev_li_scienceArea").remove();
	      					}
	      				});
		      			var areaList=data.areaList;
	      				var _unlimit = $(".dev_scienceArea[code='0']").parent("li").clone(true);
	      				$(".dev_scienceArea[code='0']").parent("li").remove();
		      			$.each(areaList,function(n,item){
		      				var _addLi = $(".dev_area_clone").find("li").clone(true);
	      					if(item["scienceAreaId"] == areaCode){ 
		      					_addLi.find(".dev_scienceArea").attr("code",item["scienceAreaId"]).attr("title",item["showName"]).text(item["showName"]);		
		      					_addLi.find(".dev_scienceArea").addClass("dev_slected");
		      					_addLi.find(".material-icons").hide();
		      					$(".dev_area_ul").append(_addLi).show();
	      					}
		      			});
		      			$(".dev_area_ul").append(_unlimit).show();	
					}
					$("#area_input").val("");
					FundRecommend.loadRecommendFundList();
				}else{
					scmpublictoast(fundRecommend.addFail,2000);	
				}
				$("#area_input").focus();
			}
		});
	}else{
		scmpublictoast(fundRecommend.addAreaSizeFail,2000);
	}
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
        <strong><s:text name='homepage.fundmain.recommend.location' /></strong> <i
          class="material-icons setting-btn_icon">expand_less</i>
      </div>
      <div class="setting-list condition-list">
        <div class="search-box_container" style="width: 216px;">
          <input item-event="callbackRegion()" maxlength="25"
            class="search-box chip-panel__manual-input js_autocompletebox" id="region_input" type="text"
            contenteditable="true" request-url="/psnweb/homepage/ajaxautoregion"
            placeholder="<s:text name='homepage.fundmain.search.regionName' />">
        </div>
        <ul class="dev_region_ul">
          <s:if test="fundRegionList != null && fundRegionList.size() > 0">
            <s:iterator value="fundRegionList" id="reg" status="sta">
              <s:if test="#reg.showName != null && #reg.showName != ''">
                <li class="item_list-align item_list-container dev_li_regionName" style="width: 220px !important;">
                  <div class="item_list-align_item dev_regionName" code="${reg.regionId}"
                    title='<c:out value="${reg.showName }"/>' style="width: 220px !important;">
                    <c:out value="${reg.showName }" />
                  </div> <i class="material-icons filter-value__cancel dev_delete_region" style="display: none;" onclick="">close</i>
                </li>
              </s:if>
            </s:iterator>
          </s:if>
          <li class="item_list-align item_list-container" style="width: 220px !important;">
            <div class="item_list-align_item dev_regionName" code="0" style="width: 220px !important;">
              <s:text name='homepage.fundmain.recommend.unlimit' />
            </div>
          </li>
        </ul>
        <!-- 节点复制111-->
        <div class="dev_region_clone" style="display: none">
          <li class="item_list-align item_list-container dev_li_regionName" title='' style="width: 220px !important;">
            <div class="item_list-align_item dev_regionName" code="" title='' style="width: 220px !important;"></div> <i
            class="material-icons filter-value__cancel dev_delete_region" style="display: none;" onclick="">close</i>
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
        <div class="search-box_container js_autocompletebox" style="width: 216px;">
          <input item-event="callbackArea()" maxlength="25"
            class="search-box chip-panel__manual-input js_autocompletebox" id="area_input" type="text"
            contenteditable="true" request-url="/psnweb/pubrecommend/ajaxgetScienceArea"
            placeholder="<s:text name='homepage.fundmain.search.area' />">
        </div>
        <ul class="dev_area_ul">
          <s:if test="fundAreaList != null && fundAreaList.size() > 0">
            <s:iterator value="fundAreaList" id="sca" status="sta">
              <s:if test="#sca.showName != null && #sca.showName != ''">
                <li class="item_list-align item_list-container dev_li_scienceArea" style="width: 220px !important;">
                  <div class="item_list-align_item dev_scienceArea" code="${sca.scienceAreaId}"
                    title='<c:out value="${sca.showName }"/>' style="width: 220px !important;">
                    <c:out value="${sca.showName }" />
                  </div> <i class="material-icons filter-value__cancel dev_delete_area" style="display: none;" onclick="">close</i>
                </li>
              </s:if>
            </s:iterator>
          </s:if>
          <li class="item_list-align item_list-container" style="width: 220px !important;">
            <div class="item_list-align_item dev_scienceArea" code="0" style="width: 220px !important;">
              <s:text name='homepage.fundmain.recommend.unlimit' />
            </div>
          </li>
        </ul>
        <!-- 节点复制-->
        <div class="dev_area_clone" style="display: none">
          <li class="item_list-align item_list-container dev_li_scienceArea" title='' style="width: 220px !important;">
            <div class="item_list-align_item dev_scienceArea" code="" title='' style="width: 220px !important;"></div> <i
            class="material-icons filter-value__cancel dev_delete_area" style="display: none;" onclick="">close</i>
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
<div class="dialogs__box" dialog-id="scienceArea_Box" style="width: 720px;" cover-event="" id="scienceArea_Box"></div>
<%@ include file="fund_conditions_box.jsp"%>