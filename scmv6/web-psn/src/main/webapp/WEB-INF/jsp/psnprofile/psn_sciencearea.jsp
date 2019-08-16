<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- <script type="text/javascript">
$(document).ready(function() {
    if(!document.getElementsByClassName("sciencearea-item")){
        document.getElementById("sciencearea").style.padding="0px";
    };
}
</script> -->
<c:if test="${not empty scienceAreaList || isMySelf== 'true'}">
  <div class="container__card" id="scienceAreaDiv">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name='homepage.profile.title.researchArea' />
        </div>
        <button class="button_main button_icon button_light-grey operationBtn" onclick="showScienceAreaBox();">
          <i class="material-icons">edit</i>
        </button>
      </div>
      <%-- <c:if test="${!empty scienceArea.showScienceArea }"> --%>
      <div class="global__padding_16" id="sciencearea" style="padding: 0px !important;">
        <div class="kw__box" id="scienceArea_list">
          <c:forEach items="${scienceAreaList}" var="scienceArea" varStatus="status">
            <div class="kw-chip_medium sciencearea-item">${scienceArea.showScienceArea  }</div>
          </c:forEach>
        </div>
      </div>
      <%-- </c:if> --%>
    </div>
  </div>
  <div class="dialogs__box" dialog-id="scienceAreaBox" cover-event="hide" style="width: 720px;" id="scienceAreaBox">
  </div>
</c:if>
