<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="js_listinfo" smate_count='${prjListVO.totalCount}'></div>
<c:forEach items="${prjListVO.resultList}" var="prj" varStatus="stat">
    <div class="paper main-list__item" style="display: flex;align-items: center;" des3prjid="${prj.des3Id }">
      <div class="paper_cont" style="margin-left: 0px !important;width: 83vw;" href="javascript:void(0);" onclick="">
        <p>
          <a class="pubTitle" href="/prjweb/project/detailsshow?des3PrjId=${prj.des3Id}">${prj.title}</a>
        </p>
        <p class="p1">${prj.authors}</p>
        <p class="f999">
          <em>${prj.showBriefDesc }</em>
        </p>
      </div>
      <i class="material-icons add_prj_div" onclick="RepresentPrj.addRepresentPrj(this)">add</i>
    </div>
</c:forEach>
<c:if test="${(prjListVO.page.pageNo-1)*10+prjListVO.resultList.size() == prjListVO.totalCount}">
  <div class="response_no-result">没有更多记录</div>
</c:if>