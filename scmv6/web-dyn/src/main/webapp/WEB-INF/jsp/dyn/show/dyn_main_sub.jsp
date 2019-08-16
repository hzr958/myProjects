<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    window.onpageshow = function(event){
        //event.persisted 判断浏览器是否有缓存, 有为true, 没有为false
        if (event.persisted) {  
            window.location.reload();
        }
    }
</script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:if test="dynLists.size > 0">
  <c:forEach items="${dynLists}" var="dynContent" varStatus="status">
    <div class="main-list__item global__padding_0 dyn_pageready_unread">
      <div class="container__card">
        <div class="dynamic__box">
          ${dynContent}
          <div class="dynDetailDiv" style="display: none;"></div>
        </div>
      </div>
    </div>
  </c:forEach>
</s:if>
<%-- <s:if test="page.pageNo*10>=page.totalCount && platform == 'mobile'">
	<div class="bottom_tip mt20" style="margin-top: 6px;">
		<div class="bottom_tip_c">关注更多专家获得更多动态<a href="/pubweb/mobile/search/main" class="find_experts">发现专家</a></div>
	</div>
</s:if> --%>
