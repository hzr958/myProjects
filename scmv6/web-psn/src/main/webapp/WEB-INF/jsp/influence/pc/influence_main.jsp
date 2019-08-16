<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>科研之友</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<script type="text/javascript" src="${resmod}/js_v5/echarts/echarts-3.8.5.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/echarts/china.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/echarts/theme/walden.js"></script>
<script type="text/javascript">
var locale = "${locale}";
$(function(){
	addFormElementsEvents();
	//hideFriendSum(); //英文环境不显示联系人总数
    Influence.getPsnStatistics();//获取人员各项统计数
	Influence.showVisitMap();//显示阅读人员分布图
	Influence.showLineMap();//显示影响力折现图
	Influence.showBarMap();//显示引用趋势图
	Influence.showHindexMap();//H-index
	//Influence.showHindexPub();//显示H-index提升推荐的成果列表,在Influence.showHindexMap后执行
	Influence.getVisitPos();//职称分布
	Influence.getVisitIns();//单位分布
	Influence.getHindexRankingInFriends();//hindex排名
	Influence.getVisitRankingInFriends(); //访问数排名
//	Influence.checkHindex();//检查更新hindex
});

/* function hideFriendSum(){
    if("en_US" == locale){
        $(".psn_friend_sum").remove();
    }
} */
</script>
</head>
<body>
  <div class="dev_select_tab container__effect">
    <!-- 各个统计数模块 -->
    <%@ include file="psn_influence_statistics.jsp"%>
    <div style="width: 100%; display: flex; justify-content: space-between;">
      <!-- 阅读趋势图 -->
      <%@ include file="psn_influence_visit_trend.jsp"%>
      <div class="container__effect-reader">
        <div class="container__effect-title">
          <span><s:text name="psnInfluence.visit.map" /></span>
        </div>
        <div class="container__effect-reader_container" id="visitmap"></div>
      </div>
    </div>
    <div class="container__effect-work">
      <!-- 单位分布 -->
      <div class="container__effect-title_left   container__effect-work_details" id="visitinsDiv"></div>
      <!-- 职称分布 -->
      <div class="container__effect-title_right  container__effect-work_details" id="visitposDiv"></div>
    </div>
    <div class="dev_cite_thread" style="width: 100%;">
      <div class="container__effect-title">
        <span><s:text name="psnInfluence.visit.citeTrend" /></span> <span> <span
          class="container__effect-paper_box"> <s:text name="psnInfluence.visit.parenthesis.left" /> <%-- <s:text name="psnInfluence.visit.citedTotal"/>
                <label class="container__effect-paper_content dev_pub_cite_total">${citedSum}
                </label><s:text name="psnInfluence.visit.friend.rank.tip"/> --%> <label
            class="container__effect-paper_content dev_frd_total psn_friend_sum">${frdSum} </label> <s:text
              name="psnInfluence.visit.friend.rank.tip2" /><label class="container__effect-paper_content dev_cite_rank">${citeRank}</label>
            <s:text name="psnInfluence.visit.parenthesis.right" /></span> &nbsp <span class="container__effect-paper_how"
          title="<s:text name="psnInfluence.visit.citeMethod1"/>&#10;<s:text name="psnInfluence.visit.citeMethod2"/> &#10;<s:text name="psnInfluence.visit.citeMethod3"/>&#10;<s:text name="psnInfluence.visit.citeMethod4"/>"><s:text
              name="psnInfluence.visit.citeMethod" /></span>
        </span>
      </div>
      <div id="barmap" class="container__effect-quote_container"></div>
    </div>
    <div class="container__effect-rank" style="margin-bottom: 30px;">
      <div class="container__effect-title_left" id="influence_hindex_div">
        <div class="container__effect-hindex_title">
          <span class="container__effect-hindex_title-content">H-index</span> <span
            class="container__effect-hindex_title-rank"><s:text name="psnInfluence.visit.parenthesis.left" /> <s:text
              name="psnInfluence.visit.friend.rank.tip3" /><label class="psn_friend_sum">0</label> <s:text
              name="psnInfluence.visit.h_indexRank" /><label id="psn_hindex_ranking">1</label> <s:text
              name="psnInfluence.visit.parenthesis.right" /></span>
        </div>
        <div id="hindexmap" class="container__effect-hindex-container"></div>
      </div>
      <!-- ======================H-index提升推荐论文-start==================================== -->
      <div id="dev_pubindex_list" class="container__effect-title_right"></div>
      <!-- ======================H-index提升推荐论文-end==================================== -->
    </div>
  </div>
  <jsp:include page="/common/smate.share.jsp" />
  <!-- 分享操作 -->
</body>
</html>