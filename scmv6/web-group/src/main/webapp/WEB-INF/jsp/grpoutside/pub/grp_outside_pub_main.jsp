<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$("#grp_pubs_List").doLoadStateIco({
		style:"height:28px; width:28px; margin:auto;margin-top:24px;",
		status:1
	});
	
	var module="${module}"  ;
	var extraParams="";
	if(module ==="projectPub"){
		extraParams ="?showPrjPub=1";
	}else{
		extraParams ="?showRefPub=1";
	}
	var myurl="/groupweb/grpinfo/outside/ajaxgrppublist"+extraParams;
	GrpPub.showGrpPubList(myurl);
});
</script>
<div class="container__horiz_left width-8">
  <div class="container__card">
    <div class="main-list">
      <div class="main-list__list" id="grp_pubs_List" list-main="grppub">
        <!-- 群组成果列表 -->
      </div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="grppub">
          <div class="pagination__per-page"></div>
          <div class="pagination__main js_pagimain">
            <div class="pagination__pages_box">
              <div class="pagination__pages_prev js_pagiprev">
                <i class="material-icons">chevron_left</i>
              </div>
              <ul class="pagination__pages_list"></ul>
              <div class="pagination__pages_next js_paginext">
                <i class="material-icons">chevron_right</i>
              </div>
            </div>
            <div class="pagination__goto">
              <s:text name='groups.outside.pub.goto' />
              ：
              <div class="input__box">
                <div class="input__area">
                  <input>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="container__horiz_right width-4">
  <div class="container__card" id="grp_pub_rcmd"></div>
  <div class="container__card" id="grp_member_pub"></div>
</div>
<jsp:include page="/common/smate.share.jsp" />