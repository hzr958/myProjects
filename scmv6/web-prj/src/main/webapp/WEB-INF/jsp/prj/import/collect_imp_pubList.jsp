<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${pubListVO.totalCount}"></div>
<c:forEach items="${pubListVO.resultList }" var="pubInfo">
  <input type="hidden" name="queryString" value="${queryString}" />
  <!-- 内容 -->
  <div class="import_Achieve-item-body_content <c:if test='${pubInfo.authermatch !=1 }'>bg01</c:if>">
    <input type="hidden" id="pdwh_pubId" name="pdwh_pubId" value="${pubInfo.pubId}" />
    <div class="item_content item_content-selector" name="is_select">
      <i class="item_selector-choose item_selector-tip"></i>
    </div>
    <div class="item_content item-content_detaile">
      <div class="item-content_detaile-title multipleline-ellipsis" style="max-height: 40px; overflow: hidden;">
        <a href="${pubInfo.pubIndexUrl}" onclick="Pub.stopBubble();"
          title='<iris:lable zhText="${pubInfo.title}"></iris:lable>' target="_blank"><div
            class="multipleline-ellipsis__content-box" style="font-size: 15px; white-space: normal;">
            <iris:lable zhText="${pubInfo.title}"></iris:lable>
          </div></a>
      </div>
      <span class="item-content_detaile-author" title="${pubInfo.authorNames }">${ pubInfo.authorNames} <br /></span> <span
        class="item-content_detaile-time" title="${pubInfo.briefDesc}">${pubInfo.briefDesc}</span>
    </div>
    <div class="item_content item_content-prj">
      <c:set var="EI_checked">0</c:set>
      <c:set var="SCI_checked">0</c:set>
      <c:set var="ISTP_checked">0</c:set>
      <c:set var="SSCI_checked">0</c:set>
      <c:if test="${pubInfo.dbid eq 14 || pubInfo.listEi>0}">
        <c:set var="EI_checked">1</c:set>
      </c:if>
      <c:if test="${pubInfo.dbid eq 16 || pubInfo.listSci>0}">
        <c:set var="SCI_checked">1</c:set>
      </c:if>
      <c:if test="${pubInfo.dbid eq 15 || pubInfo.listIstp>0}">
        <c:set var="ISTP_checked">1</c:set>
      </c:if>
      <c:if test="${pubInfo.dbid eq 17 || pubInfo.listSsci>0}">
        <c:set var="SSCI_checked">1</c:set>
      </c:if>
      <div class="item_content-prj_sub">
        <i class="item_selector-include item_selector-tip"></i> <input type="checkbox" name="selected-list"
          class="textbox" ${EI_checked==1?'checked=true disabled=disabled':'' } value="EI"
          style="display: none; ime-mode: disabled;" /> <span class="item_selector-content"> EI</span>
      </div>
      <div class="item_content-prj_sub">
        <i class="item_selector-include item_selector-tip"></i> <input type="checkbox" name="selected-list"
          class="textbox" ${SCI_checked==1?'checked=true disabled=disabled':'' } value="SCIE"
          style="display: none; ime-mode: disabled;" /> <span class="item_selector-content"> SCIE</span>
      </div>
      <div class="item_content-prj_sub">
        <i class="item_selector-include item_selector-tip"></i> <input type="checkbox" name="selected-list"
          class="textbox" ${ISTP_checked==1?'checked=true disabled=disabled':'' } value="ISTP"
          style="display: none; ime-mode: disabled;" /> <span class="item_selector-content"> ISTP</span>
      </div>
      <div class="item_content-prj_sub">
        <i class="item_selector-include item_selector-tip"></i> <input type="checkbox" name="selected-list"
          class="textbox" ${SSCI_checked==1?'checked=true disabled=disabled':'' } value="SSCI"
          style="display: none; ime-mode: disabled;" /> <span class="item_selector-content"> SSCI</span>
      </div>
    </div>
    <div class="item_content item_content-paper">
      <select name="pubtype" class="pubtype inp_text"
        style="width: 84px; font-family: Noto Sans SC, Hiragino Sans GB W3, Heiti SC, Microsoft Yahei, Segoe UI;"
        onchange="checkDup(this.value,'${index}');">
        <c:forEach items="${pubListVO.pubTypes }" var="pubType">
          <option value="${pubType.id }" <c:if test="${pubType.id eq pubInfo.pubType}">selected='true'</c:if>>
            ${pubType.name}</option>
        </c:forEach>
      </select>
    </div>
    <c:if test="${not empty pubInfo.dupPubId }">
      <div class="item_content item_content-same">
        <input type="hidden" id="dup_pubId" name="dup_pubId" value="${pubInfo.dupPubId}" />
        <c:if test="${pubInfo.isInsert eq 1}">
          <input type="hidden" id="is_insert" class="is_inset" alt="${pubInfo.pubType}" value="${pubInfo.isInsert}" />
        </c:if>
        <div style="width: 100%; text-align: center; color: red;">
          <s:text name='referencesearch.import.same' />
        </div>
        <select class="repeatSelect" onclick="selectTip(this)"
          style="min-width: 45px; margin: 5px 0px; font-family: Noto Sans SC, Hiragino Sans GB W3, Heiti SC, Microsoft Yahei, Segoe UI;">
          <option value="1"><s:text name='referencelist.select.update' /></option>
          <option value="2"><s:text name='referencelist.select.notduplicated' /></option>
          <option value="0"><s:text name='referencelist.select.skip' /></option>
        </select>
        <div style="width: 100%; text-align: center; color: gray;">
          <s:text name='referencelist.label.overwrite' />
        </div>
      </div>
    </c:if>
  </div>
</c:forEach>
</form>
