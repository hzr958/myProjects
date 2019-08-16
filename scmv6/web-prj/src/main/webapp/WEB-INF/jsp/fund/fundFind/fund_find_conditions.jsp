<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var seniorityVal = "${seniority}";
var local='${locale}';
var regionArray=new Array();
$(function(){
  $(".new-mainpage_left-sublist_header-onkey").each(function(){
    $(this).bind("click",function(){
      if(this.classList.contains("new-mainpage_left-sublist_header-open")){
        this.classList.remove("new-mainpage_left-sublist_header-open");
        this.closest(".new-mainpage_left-sublist").querySelector(".new-mainpage_left-sublist_body").style.display = "none";
    }else{
        this.classList.add("new-mainpage_left-sublist_header-open");
        this.closest(".new-mainpage_left-sublist").querySelector(".new-mainpage_left-sublist_body").style.display = "block";
    }
    })
  })
  
  $(".new-mainpage_left-sublist_body-item").each(function(){
    $(this).bind("click",function(){
      if(this.classList.contains("new-mainpage_left-sublist_body-item_selected")){
        this.classList.remove("new-mainpage_left-sublist_body-item_selected");
    }else{
        this.classList.add("new-mainpage_left-sublist_body-item_selected");
    }
      FundFind.loadFundFindList(false);
    })
  })
  //搜索框绑定回车检索事件
  $("#searchKey").keypress(function (e) {
    if (e.which == 13) {
      FundFind.loadFundFindList(false);
    }
});
  //加载地区分页
  //FundFind.funPagingConditions();
  //FundFind.loadFundFindList(false);
  
})

</script>
<input type="hidden" name="regionNamesSelect" id="regionNamesSelect" value>
<input type="hidden" name="seniorityCodeSelect" id="seniorityCodeSelect" value="0">
<input type="hidden" name="scienceCodeSelect" id="scienceCodeSelect" value>
 <!-- 所在地区 -->
 <div class="new-mainpage_container">
        <div class="new-mainpage_left-container" style="height: auto; border-right:1px solid #ddd;">
            <div class="new-mainpage_left-item" style="height: auto;">
                <div class="new-mainpage_left-list">
                    <div class="new-mainpage_left-list_header">
                        <span class="new-mainpage_left-list_header-title"><s:text name="homepage.fundmain.region"/></span>
                        <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
                    </div>
                    <div class="new-mainpage_left-list_body">
                        <div class="new-mainpage_left-list_body-search" onkeydown="FundFind.searchRegion();">
                            <input type="text" placeholder="<s:text name='homepage.fundmain.search.regionName'/>" id="searchRegion" value>
                        </div>
            <div class="dev_region_ul">
                <li class="item_list-align item_list-container dev_li_agencyName setting-list_page-item" onclick="FundFind.clickRegion(this);" style="width: 220px !important;" value="0">
                  <c:if test="${locale == 'zh_CN' }">
                      <div class="item_list-align_item dev_agencyName" code="157" title="国家" style="width: 220px !important;cursor: pointer;">
                      <span style="padding-left: 24px;">国家</span></div> 
                      </c:if>
                      <c:if test="${locale != 'zh_CN' }">
                      <div class="item_list-align_item dev_agencyName" code="157" title="Country" style="width: 220px !important;cursor: pointer;">
                      <span style="padding-left: 24px;">Country</span></div>
                  </c:if>
                </li>
            <c:if test="${regionList != null && regionList.size() > 0 }">
            <c:forEach items="${regionList }" var="reg" varStatus="regStatus">
                <li class="item_list-align item_list-container dev_li_agencyName setting-list_page-item"
                  onclick="FundFind.clickRegion(this);" style="width: 220px !important;" value="${regStatus.count}">
                  <c:if test="${locale == 'zh_CN' }">
                    <div class="item_list-align_item dev_agencyName" code="${reg.id}"
                      title='<c:out value="${reg.zhName }"/>' style="width: 220px !important; cursor: pointer;">
                      <span style="padding-left: 24px;"><c:out value="${reg.zhName }" /></span>
                  </c:if> <c:if test="${locale != 'zh_CN' }">
                    <div class="item_list-align_item dev_agencyName" code="${reg.id}"
                      title='<c:out value="${reg.enName }"/>' style="width: 220px !important; cursor: pointer;">
                      <span style="padding-left: 24px;"><c:out value="${reg.enName }" /></span>
                  </c:if></div> 
                  </li>
              </c:forEach>
          </c:if>
          </div>
        <div class="setting-list_page">
          <div class="setting-list_page-btn setting-list_page-up" onclick="FundFind.prePage(this)" title="上一页">
            <i class="material-icons">keyboard_arrow_left</i>
          </div>
          <div class="setting-list_page-btn setting-list_page-down" onclick="FundFind.nextPage(this)" title="下一页">
            <i class="material-icons">keyboard_arrow_right</i>
          </div>
        </div>
         </div>
          </div>
        <!-- 单位类型 -->
        <div class="new-mainpage_left-list">
                    <div  class="new-mainpage_left-list_header">
                        <span class="new-mainpage_left-list_header-title"><s:text name="homepage.fundmain.type.kind"/></span>
                        <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
                    </div>
                    <div class="new-mainpage_left-list_body">
                        <div class="new-mainpage_left-list_body-item" onclick="FundFind.clickType(this,'2');">
                        <span style="padding-left: 24px;"><s:text name="homepage.fundmain.type.institution"/></span></div>
                        <div class="new-mainpage_left-list_body-item" onclick="FundFind.clickType(this,'1');">
                        <span style="padding-left: 24px;"><s:text name="homepage.fundmain.type.enterprise"/></span></div>
                        <div class="new-mainpage_left-list_body-item" onclick="FundFind.clickType(this,'0');">
                        <span style="padding-left: 24px;"><s:text name="homepage.fundmain.type.all"/></span></div>
                    </div>
       </div>
        <!-- 科技领域-->
         <div class="new-mainpage_left-list">
                    <div  class="new-mainpage_left-list_header">
                        <span class="new-mainpage_left-list_header-title"><s:text name="homepage.fundmain.areas"/></span>
                        <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
                    </div>
                    <div class="new-mainpage_left-list_body">
                        <c:forEach items="${categoryMap['CategoryMap_first'] }" var="firstLevel" varStatus="firstStatus">
                            <div class="new-mainpage_left-sublist">
                            <c:set var="subKey" value="CategoryMap_sub${firstLevel.categryId}"></c:set>
                            <input type="hidden" value="${subKey }" />
                            <div class="new-mainpage_left-sublist_header">
                                <i class="new-mainpage_left-sublist_header-onkey" ></i>
                                <span class="new-mainpage_left-sublist_header-title" value="${firstLevel.showCategory }">${firstLevel.showCategory }</span>
                            </div>
                            <div class="new-mainpage_left-sublist_body" style="display:none">
                            <c:forEach items="${categoryMap[subKey] }" var="subLevel">
                                <div class="new-mainpage_left-sublist_body-item"  id="${subLevel.categryId }">
                                    <div class="new-mainpage_left-sublist_body-detail" title="${subLevel.showCategory }">${subLevel.showCategory }</div>
                                    <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                                </div>
                                </c:forEach>
                                </div>
                                </div>
                                </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        <!-- 条件检索 -->
        <div class="new-mainpage_right-container" style="overflow: visible; margin-right: -21px; border: none;">
            
            <div class="new-mainpage_right-container_search" style="width: 940px;">
                <div class="new-mainpage_right-container_search-box">
                    <input type="text" id="searchKey" placeholder="<s:text name='homepage.fundmain.search.fundNameOrKeywords'/>">
                    <i class="searchbox__icon" onclick="FundFind.loadFundFindList();"></i>
                </div>
                <div class="new-mainpage_right-container_search-btn" onclick="FundFind.loadFundFindList();"><s:text name='homepage.fundmain.search'/></div>
            </div>
         <!-- 基金列表 -->
              <div class="main-list new-mainpage_right-container-scroll" style="height: auto;">
                <div>
                     <div class="main-list__list  main-list__list-onscroll" list-main="collectFundList" style="display: block;">
                  
                      </div>
                      <div class="main-list__footer" style="width: 960px;">
                          <div class="pagination__box" list-pagination="collectFundList">
                            <!-- 翻页 -->
                          </div>
                      </div>
                    
                </div>
              </div>
        </div>
    </div>
     <%-- <div id="fund_find_footer"><jsp:include page="/skins_v6/footer_infor.jsp" /></div> --%>
</body>
</html> 