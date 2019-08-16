<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test='${list.size() ge 1 }'>
  <div class="main-list__read-item global__padding_0 dyn_pageready_read">
    <c:forEach items="${list}" var="ft" varStatus="stat">
      <input type="hidden" class="remdNewsId" id="remdNewsId" value="${ft.des3NewsId }">
      <div class="container__card">
        <div class="dynamic__box">
          <div class="dynamic-creator__box" style="justify-content: space-between;">
            <div class="dyn-publish_content">
              <span class="dyn-publish_content-author"> 
                 <s:text name="dyn.remd.res.suggestnews"/>
              </span>
            </div>
            <div class="dynamic-post__operation-box dynamic-post__operation-check">
              <span onclick="dynamic.viewMoreRemd(3)"><s:text name="dyn.remd.res.viewmore" /></span>
            </div>
          </div>
          <div class="dynamic-content__main">
            <div class="dynamic-main__setbox" style="flex-direction: column;">
              <div class="dynamic-main__att new-Recommend_container" style="width: 100%;">
                <div class="new-Recommend_infor-avator" style="width: 40px; height:50px; margin-top: 4px;border:0px;">
                    <div class="file-left__border  file-left__download fileupload__box-border"
                      style="position: relative;">
                      <a href="/dynweb/mobile/news/details?des3NewsId=${ft.des3NewsId }" target="_blank"> <img
                        src="${empty ft.image ? false :ft.image}"
                        onerror="this.onerror=null;this.src='${resmod }/smate-pc/img/logo_newsdefault.png'"  style="width: 40px; height:50px;"></a>
                    </div>
                </div>
                <div class="new-Recommend_infor-box"  style="width: 85%;">
                  <div class="new-Recommend_infor-box_title">
                    <a onclick="dynamic.viewRemdRes('${ft.des3NewsId }',3);">${ft.title }</a>
                  </div>
                  <%-- <div class="new-Recommend_infor-box_author">${ft.resAuthorNames }</div> --%>
                  <div class="new-Recommend_infor-box_time">
                    <em style="font-style: normal;">${ft.brief }</em>
                  </div>
              <div class="new-Recommend_infor-func_tool" style="width: 100%; justify-content: flex-start; margin-bottom: 12px; margin-top: 16px;">
                <div class="new-Recommend_infor-func_cancle"
                  onclick="dynamic.notViewRemdRes(this,'${ft.des3NewsId }',3);">
                  <s:text name="dyn.remd.res.notview" />
                </div>
                <div class="new-Recommend_infor-func_comfire"
                  onclick="dynamic.viewRemdRes('${ft.des3NewsId }',3);">
                  <s:text name="dyn.remd.res.view" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
    </c:forEach>
  </div>
</c:if>
