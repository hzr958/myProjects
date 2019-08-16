<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jstl/xml_rt"%>
<c:set var="resprj" value="/ressns/js/prj" /> 
<html>
<%@ include file="enter_head.jsp"%>
<body>
  <script type="text/javascript">
    function back() {
        var backType = $("#backType").val();
        var url = "/prjweb/backurl";
        if(backType == 1){
            url = "/psnweb/homepage/show?module=prj&frompage=prjEdit";
        }else if(backType == 2){
            url = "/prjweb/project/detailsshow?des3PrjId=${des3Id}";
        }
        window.location.href = url;
    }
var bckpermission = "${bckpermission}";
</script>
  <div id="content">
    <div class="main-box">
      <form name="mainform" id="mainform" action="/prjweb/prj/save" method="post">
        <s:token></s:token>
        <x:parse xml="${prjXml}" var="myoutput" />
        <c:set var="prjId">
          <x:out select="$myoutput/data/prj_meta/@prj_id" />
        </c:set>
        <c:set var="fromPrjId">
          <x:out select="$myoutput/data/prj_meta/@from_prj_id" />
        </c:set>
        <input type="hidden" id="des3Id" name="des3Id" value="<iris:des3 code="${prjId }"/>" /> <input type="hidden"
          id="_prj_meta_prj_id" name="/prj_meta/@prj_id" value="${prjId }" /> <input type="hidden"
          id="_prj_meta_tmpl_form" name="/prj_meta/@tmpl_form" value="scholar" /> <input type="hidden"
          id="_prj_meta_from_prj_id" name="/prj_meta/@from_prj_id" value="${fromPrjId }" /> <input type="hidden"
          name="tagIndex" id="tagIndex" value="${tagIndex }" /> <input type="hidden" name="groupFolderId"
          id="groupFolderId" value="${groupFolderId }" /> <input type="hidden" name="groupId" id="groupId"
          value="${groupId }" /> <input type="hidden" name="prjNodeId" id="prjNodeId" value="${prjNodeId}" /> <input
          type="hidden" name="from" id="from" value="${from}" /> <input type="hidden" name="backType" id="backType"
          value="${backType}" /> <input type="hidden" name="des3GroupId" id="des3GroupId" value="${des3GroupId}" /> <input
          type="hidden" name="recordFrom" id="recordFrom" value="${recordFrom}" />
       <div class="bar_btn">
          <span class="Fleft bar_prjenter_width"> <a class="uiButton uiButtonConfirm saveOutputButton"
            style="cursor: pointer;"><s:text name="projectEdit.button.save" /></a> <s:if test="backType>=1">
              <a class="uiButton" style="cursor: pointer;" onclick="back();"><s:text name="projectEdit.button.back1" /></a>
            </s:if> <s:else>
              <a class="uiButton" style="cursor: pointer;" onclick="back();"><s:text name="projectEdit.button.back" /></a>
            </s:else>
          </span> <span class="Fleft prjStateError" style="display: none;"> <label class="error"><s:text
                name="project.state.error" /></label>
          </span>
          <c:if test="${!empty prjId}">
            <span class="Fright mtop5"><span class="f666"><s:text name="projectEdit.label.serialno" /> <s:text
                  name="colon.all" />${prjId }</span></span>
          </c:if>
        </div>
        <c:if test="${!empty prjId}">
          <div class="ba_title" style="line-height: normal;">
            <p class="fblue16">
              <s:text name="projectEdit.label.ctitle" />
              <c:set var="zh_title">
                <x:out select="$myoutput/data/project/@zh_title" escapeXml="false" />
              </c:set>
              <c:set var="en_title">
                <x:out select="$myoutput/data/project/@en_title" escapeXml="false" />
              </c:set>
              <c:choose>
                <c:when test="${lang eq 'zh' && !empty zh_title}">${zh_title}</c:when>
                <c:otherwise>${empty en_title?zh_title:en_title}</c:otherwise>
              </c:choose>
            </p>
            <p class="f666">
              <s:text name="projectEdit.label.author" />
              <s:text name="colon.all" />
              <c:set var="author_names_zh">
                <x:out select="$myoutput/data/project/@author_names" escapeXml="false" />
              </c:set>
              <c:set var="author_names_en">
                <x:out select="$myoutput/data/project/@author_names_en" escapeXml="false" />
              </c:set>
              <c:choose>
                <c:when test="${lang eq 'zh' && !empty author_names_zh}">${author_names_zh}</c:when>
                <c:otherwise>${empty author_names_en?author_names_zh:author_names_en}</c:otherwise>
              </c:choose>
            </p>
          </div>
        </c:if>
        <div class="Menubox">
          <ul>
            <li id="one1" class="hover" onclick="ScmMaint.setMenuTab('one',1,3)"><s:text
                name="projectEdit.label.tabBasic" /></li>
            <li id="one2" onclick="ScmMaint.setMenuTab('one',2,3)"><s:text name="projectEdit.label.tabAuthor" /></li>
            <li id="one3" onclick="ScmMaint.setMenuTab('one',3,3)"><s:text name="projectEdit.label.tabAttach" /></li>
          </ul>
          <span class="Fright"><s:text name="projectEdit.label.mandatory" /></span>
        </div>
        <div class="Contentbox">
          <div style="display: block;" id="con_one_1" class="hover">
            <ul class="basic_info">
              <c:choose>
                <c:when test="${webContextType eq 'scmwebrol'}">
                  <%@ include file="prjbasic_detail.jsp"%>
                </c:when>
                <c:otherwise>
                  <%@ include file="prjbasic_authority.jsp"%>
                  <%@ include file="prjbasic_detail.jsp"%>
                </c:otherwise>
              </c:choose>
            </ul>
          </div>
          <div style="display: none;" id="con_one_2">
            <c:choose>
              <c:when test="${webContextType eq 'scmwebrol'}">
                <%@ include file="prjAuthorRol_detail.jsp"%>
              </c:when>
              <c:otherwise>
                <%@ include file="prjAuthor_detail.jsp"%>
              </c:otherwise>
            </c:choose>
          </div>
          <div style="display: none;" id="con_one_3">
            <%@ include file="prjAttachment_detail.jsp"%>
          </div>
        </div>
        <div class="bar_btn2">
          <span class="bar_width"> <a class="uiButton uiButtonConfirm saveOutputButton" style="cursor: pointer;"><s:text
                name="projectEdit.button.save" /></a> <s:if test="backType>=1">
              <a class="uiButton" style="cursor: pointer;" onclick="back();"><s:text name="projectEdit.button.back1" /></a>
            </s:if> <s:else>
              <a class="uiButton" style="cursor: pointer;" onclick="back();"><s:text name="projectEdit.button.back" /></a>
            </s:else>
          </span>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
