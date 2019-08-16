<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container__effect-paper">
  <div class="container__effect-title container__effect-array">
    <span><s:text name="psnInfluence.visit.readTrend" /></span> <span> <span class="container__effect-paper_box"
      id="read_trend_tips"><s:text name="psnInfluence.visit.parenthesis.left" /> <s:text
          name="psnInfluence.visit.readTotals" /><label class="container__effect-paper_content" id="psn_visit_sum">0</label>
        <s:text name="psnInfluence.visit.friend.rank.tip" /><label class="psn_friend_sum">0</label> <s:text
          name="psnInfluence.visit.friend.rank.tip2" /><label class="container__effect-paper_content"
        id="psn_visit_ranking">1</label> <s:text name="psnInfluence.visit.parenthesis.right" /></span> &nbsp <span
      class="container__effect-paper_how"
      title="<s:text name='psnInfluence.visit.readCalculateMethod1'/>&#10;<s:text name='psnInfluence.visit.readCalculateMethod2'/>&#10;<s:text name='psnInfluence.visit.readCalculateMethod3'/>
            "><s:text
          name="psnInfluence.visit.readCalculateMethod" /></span>
    </span>
  </div>
  <div class="container__effect-paper_container" id="linemap"></div>
</div>