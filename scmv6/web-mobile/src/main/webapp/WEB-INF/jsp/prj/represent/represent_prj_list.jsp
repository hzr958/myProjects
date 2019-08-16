<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="js_listinfo" smate_count='${prjListVO.totalCount}'></div>
<c:forEach items="${prjListVO.resultList}" var="prj" varStatus="stat">
    <div class="paper main-list__item" style="display: flex; align-items: center;" des3prjid="${prj.des3Id }">
      <div class="paper_cont" style="margin-left: 0px !important;width: 93%;" href="javascript:void(0);" onclick="">
        <p>
          <a class="pubTitle">${prj.showTitle}</a>
        </p>
        <p class="p1">${prj.showAuthorNames}</p>
        <p class="f999">
          <em>${prj.showBriefDesc }</em>
        </p>
      </div>
       <div class="new-Represent_achieve-item_func" style="height: 24px; width: 24px; justify-content: center;">
          <i class="material-icons" onclick="RepresentPrj.removeRepresentPrj(this)">close</i>
      </div> 
    </div>

</c:forEach>
<div class="response_no-result">没有更多记录</div>
