<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$(".needInit").each(function(){
		var $this = $(this)[0];
		//监听点击事件
		$this.addEventListener("tap",function () {
			window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId="+$(this).attr("psnId");
		});
		$(this).removeClass("needInit");
	});
})

</script>
<input type="hidden" name="pageNo" id="pageNo" value="${page.pageNo }">
<input type="hidden" name="totalPage" id="totalPage" value="${page.totalPages }">
<c:forEach var="psnInfo" items="${psnInfoList}" varStatus="psnInfoList">
  <div class="list_item_container identify_psn_item needInit" psnId="${psnInfo.des3PsnId }">
    <div class="list_item_section">
      <div class="person_namecard_whole hasBg">
        <div class="avatar">
          <img class="avatar" src="${psnInfo.avatarUrl }" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
        </div>
        <div class="person_information">
          <div class="name">${psnInfo.name }</div>
          <div class="institution">${psnInfo.insInfo }</div>
        </div>
      </div>
    </div>
  </div>
</c:forEach>
