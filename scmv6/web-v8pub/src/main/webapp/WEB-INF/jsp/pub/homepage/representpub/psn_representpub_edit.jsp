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
<input type="hidden" name="searchPubPageNo" id="searchPubPageNo" value="${pubVO.searchPubPageNo }" />
<input type="hidden" name="pubPageNo" id="pubPageNo" value="${page.pageNo }" />
<div class="dialogs__childbox_fixed">
  <div class="dialogs__header">
    <div class="dialogs__header_title">
      <spring:message code='homepage.profile.title.select.featured.publications' />
    </div>
  </div>
</div>
<div class="dialogs__childbox_adapted">
  <div class="dialogs__content global__padding_24">
    <div class="sugg-picker">
      <div class="sugg-picker__header">
        <spring:message code='homepage.profile.note.select.featured.publications' />
      </div>
      <div class="sugg-picker__panel">
        <div class="sugg-panel">
          <div class="sugg-panel__title">
            <div class="searchbox__container main-list__searchbox" list-search="psnOpenPubList">
              <div class="searchbox__main">
                <input placeholder="<spring:message code='homepage.profile.note.search.pub'/>">
                <div class="searchbox__icon material-icons"></div>
              </div>
            </div>
          </div>
          <div class="sugg-panel__content" id="openPubListContent" style="height: 480px;">
            <div class="main-list__list global_no-border" list-main="psnOpenPubList" id="psnOpenPubList"></div>
          </div>
        </div>
        <div class="sugg-panel right-panel">
          <div class="sugg-panel__title">
            <spring:message code='homepage.profile.note.selected.pub' />
          </div>
          <div class="sugg-panel__content">
            <div class="main-list__list global_no-border" id="addedPubList">
              <c:if test="${!empty pubVO.pubInfoList}">
                <c:forEach items="${pubVO.pubInfoList }" var="representPub" varStatus="status">
                  <div class="main-list__item" des3pubid="${representPub.des3PubId }"  seq_pub="${ status.index + 1}">
                    <div class="main-list__item_content">
                      <div class="pub-idx_x-small">
                        <div class="pub-idx__base-info">
                          <div class="pub-idx__full-text_box">
                            <div class="pub-idx__full-text_img">
                              <img src="">
                            </div>
                          </div>
                          <div class="pub-idx__main_box">
                            <div class="pub-idx__main">
                              <div class="pub-idx__main_title selected_pub_title">${representPub.title}</div>
                              <div class="pub-idx__main_author selected_pub_author">${representPub.authorNames }</div>
                              <div class="pub-idx__main_author selected_pub_BriefDesc">${representPub.briefDesc }</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="main-list__item_actions">
                      <i class="selected-func_down arrow_down" onclick="downMoveRepresentPub(this);"></i>
                      <i class="selected-func_up arrow_up" onclick="upMoveRepresentPub(this);"></i>
                      <i class="material-icons arrow_close" onclick="delRepresentPub(this);">close</i>
                    </div>
                  </div>
                </c:forEach>
              </c:if>
              <div class="main-list__item" id="addedPubItem" style="display: none;" des3pubid="">
                <div class="main-list__item_content">
                  <div class="pub-idx_x-small">
                    <div class="pub-idx__base-info">
                      <div class="pub-idx__full-text_box">
                        <div class="pub-idx__full-text_img">
                          <img src="">
                        </div>
                      </div>
                      <div class="pub-idx__main_box">
                        <div class="pub-idx__main">
                          <div class="pub-idx__main_title selected_pub_title"></div>
                          <div class="pub-idx__main_author selected_pub_author"></div>
                          <div class="pub-idx__main_author selected_pub_BriefDesc"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="main-list__item_actions">
                  <i class="selected-func_down arrow_down" onclick="downMoveRepresentPub(this);"></i>
                  <i class="selected-func_up arrow_up" onclick="upMoveRepresentPub(this);"></i>
                  <i class="material-icons arrow_close" onclick="delRepresentPub(this);">close</i>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__childbox_fixed">
  <div class="dialogs__footer">
    <button class="button_main button_primary-reverse" onclick="savePsnRepresentPub(this);">
      <spring:message code='homepage.profile.btn.save' />
    </button>
    <button class="button_main button_primary-cancle" onclick="hideRepresentPubBox(this);">
      <spring:message code='homepage.profile.btn.cancel' />
    </button>
  </div>
</div>
