<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    fundAgencyList();
    var closelist = document.getElementsByClassName("filter-section__header");
    for(var i = 0; i < closelist.length; i++){
    	closelist[i].onclick = function(){
    		if(this.querySelector(".filter-section__toggle").innerHTML=="expand_less"){
    			this.querySelector(".filter-section__toggle").innerHTML="expand_more";
    			this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
    		}else{
    		
    			this.querySelector(".filter-section__toggle").innerHTML="expand_less";
                this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
    		}
    	}
    }
    document.getElementsByClassName("filter-list__section")[0].style.height = "auto";
    var totalheight =document.documentElement.clientHeight || document.body.clientHeight;
    var totalheight =document.documentElement.clientHeight;
   /*  document.getElementsByClassName("main-list")[0].style.height = totalheight - 110 + "px"; */
    var setheight = window.innerHeight - 180 - 95;
   /*  var contentlist = document.getElementsByClassName("content-details_container");
    for(var i = 0; i < contentlist.length; i++){
        contentlist[i].style.minHeight = setheight + 60 + "px";
    } */
    document.getElementsByClassName("nav__underline")[0].style.width =  95 + "px";
    document.getElementsByClassName("nav__underline")[0].style.left =  100 + "px";
    document.getElementsByClassName("nav__underline")[0].style.top= 38 + "px";
});
function fundAgencyList(){
     fundList = window.Mainlist({
        name:"fundList",
        listurl: "/prjweb/fundAgency/ajaxfundlist",
        listdata: {},
        listcallback: function(){
            PCAgency.ajaxInitOptStatusAndCount();
        },
        statsurl:"/prjweb/fundAgency/ajaxfundcount",
        method: "pagination",
    })
}


function shareAgencyFrdCallback(data, agencyId){
    var shareCount = data.shareCount;
    var domItem = $("div[des3Id='"+agencyId+"']:first");
    //初始化分享数
    PCAgency.initShareOpt(domItem, shareCount);
}

function shareAgencyDynCallback(data, des3ResId, comments){
    $.ajax({
        url: "/prjweb/share/ajaxupdate",
        type: "post",
        data:{
            "Des3FundAgencyId": des3ResId,
            "shareToPlatform": 1,
            "comments": comments
        },
        dataType: "json",
        success: function(data){
            if(data.result = "success"){
                var shareCount = data.shareCount;
                var domItem = $("div[des3Id='"+des3ResId+"']:first");
                PCAgency.initShareOpt(domItem, shareCount);
            }
        },
        error: function(){}
    });
}

function shareAgencyGrpCallback(data, des3ResId, des3GroupId, comments){
    $.ajax({
        url: "/prjweb/share/ajaxupdate",
        type: "post",
        data:{
            "Des3FundAgencyId": des3ResId,
            "shareToPlatform": 3,
            "des3GroupId": des3GroupId,
            "comments": comments
        },
        dataType: "json",
        success: function(data){
            if(data.result = "success"){
                var shareCount = data.shareCount;
                var domItem = $("div[des3Id='"+des3ResId+"']:first");
                PCAgency.initShareOpt(domItem, shareCount);
            }
        },
        error: function(){}
    });
}
</script>
<input type="hidden" id="interest_agencys_id" value="${des3AgencyIds }">
<div class="funding-agencies_container" style="margin-top: -16px;">

  <div class="funding-agencies_container-left" style="width: 240px;">
    <div class="filter-list vert-style option_has-stats filter-list__specStyle" list-filter="fundList"
      style="overflow-x: hidden; height: auto;">
      <div class="filter-list__section filter-list__section-height" id="filter-list__section" filter-method="multiple" filter-section="regionAgency"
        style="width: 260px; overflow-x: hidden; height: auto;">
        <div class="filter-section__header" style="line-height: 40px; margin-top: 8px; justify-content: flex-start;">
          <div class="filter-section__title" style="font-size: 14px; color: #333; font-weight: bold; width: 72%; flex-grow: 0">
            <s:text name="homepage.fundmain.Location" />
          </div>
          <i class="material-icons filter-section__toggle">expand_less</i>
        </div>
        <ul class="filter-value__list filter-left_list-container" style="margin-top: 12px; height: auto;">
          <c:if test="${!empty regionList && regionList.size()>0}">
            <s:iterator value="regionList" var="region">
              <li class="filter-value__item js_filtervalue" filter-value="${region.id}" style="width: 200px !important;">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption" title="" style="font-size: 14px;">
                  <c:if test="${locale=='zh_CN' }">${zhName }</c:if>
                  <c:if test="${locale=='en_US' }">${enName }</c:if>
                </div>
                <div class="filter-value__stats js_filterstats" style="font-size: 14px;">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
            </s:iterator>
          </c:if>
        </ul>
      </div>
    </div>
  </div>


  <div class="funding-agencies_container-right" style="width: 92%; margin: 0px; margin-left: 20px; margin-top: 20px;">
    <div class="content-details_container" style="padding-left: 20px; height: auto;">
         <div class="main-list__list  main-list__list-onscroll" list-main="fundList" style="display: block;">
    
        </div>
        <div class="main-list__footer" style="width: 960px;">
            <div class="pagination__box" list-pagination="fundList">
              <!-- 翻页 -->
            </div>
        </div>
  </div>
</div>
</div> 
</div>
<%-- <div>
     <jsp:include page="/skins_v6/footer_infor.jsp" />
</div>  --%>
