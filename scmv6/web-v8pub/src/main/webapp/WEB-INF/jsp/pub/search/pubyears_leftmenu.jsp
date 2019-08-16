<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	PdwhPubSearch.moveFundingYear("");
});
</script>
<div class="menuParent" style="padding: 0px;">
  <div class="ListTitlePanel">
    <div class="ListTitle" style="width: 230px; border-bottom: 1px solid #ddd; padding-left: 0px;  margin-left: 35px;  border-top: none; ">
      <span><spring:message code='psn.leftmenu.pubYear' /></span>
      <div class="leftbgbt">
        <i class="material-icons" id="iconQh"> &#xE5CE;</i>
        <!-- <i class="material-icons keyboard_arrow_up" id="iconQh"> &#xE5CE;</i> -->
      </div>
    </div>
  </div>
  <div class="menuList" id="years" style="border-top: none;">
    <c:forEach items="${pubListVO.yearMap }" var="year">
      <div name="year" style="display: none;">
        <a href="javascript:;" onclick="page.searchByYear('${year.key}',this)" style="display: flex; align-items: center; justify-content: space-between;"> <span  class="menuList-papper__style">${year.key}</span> <em
          class="fr">(${year.value})</em><i name="year" class="material-icons close" style="display: none;visibility: unset;">close</i>
        </a>
      </div>
    </c:forEach>
    <div class="menuList_sc" id="menuListsc" style="display: flex; justify-content: space-around;">
      <a href="#" class="fl" style="width: 68px!important;" onclick="PdwhPubSearch.moveFundingYear('left')"
        title="<spring:message code='page.prePage'/>"><i id="yearlastpage" style="color: #333"
        class="material-icons keyboard_arrow_left">&#xe314;</i></a> <a href="#" class="" style="width: 68px!important;"
        onclick="PdwhPubSearch.moveFundingYear('right')" title="<spring:message code='page.nextPage'/>"><i
        id="yearnextpage" style="color: #333" class="material-icons keyboard_arrow_right">&#xe315;</i></a>
    </div>
    <input type="hidden" id="currentyears" value="1">
    <div class="menuList_st" style="display: none;">
      <a href="javascript:;"><spring:message code='psn.leftmenu.btn.confirm' /></a><input type="text"><span
        class="fl">-</span><input type="text">
    </div>
  </div>
</div>