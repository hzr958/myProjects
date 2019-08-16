<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$("#grp_member_List").doLoadStateIco({
		style:"height:28px; width:28px; margin:auto;margin-top:24px;",
		status:1
	});
	//群组成员列表
	var $grpID = parseInt(document.getElementById("grp_params").getAttribute("smate_grp_id"));
	var myurl="/groupweb/grpinfo/outside/ajaxshowgrpmembers";
	GrpMember.refreshMember($grpID,function(){},myurl);
});
</script>
<div class="container__horiz_left width-8">
  <div class="container__card">
    <div class="main-list">
      <div class="main-list__list" id="grp_member_List" list-main="grpmember">
        <!-- 群组成员列表 -->
      </div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="grpmember">
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
              <s:text name='groups.outside.member.goto' />
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
  <div class="container__card" id="grp_proposer">
    <!-- 群组申请人员 -->
  </div>
  <div class="container__card" id="grp_referrers_List">
    <!-- 群组推荐成员列表 -->
  </div>
</div>
