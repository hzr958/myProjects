<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="detail-pub__cognize dev_pubdetails_title">
  <c:if test="${not empty pubDetailVO.authorPsnInfoList}">
    <c:forEach items="${pubDetailVO.authorPsnInfoList}" var="pinfo">
      <div class="detail-pub__cognize-item" status="${pinfo.status }"
        <c:if test="${pinfo.status==0 }">style="cursor:default"</c:if>>
        <div class="detail-pub__cognize-item_avator">
          <img src="${pinfo.avatars }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
        </div>
        <div class="detail-pub__cognize-item_detail" title="${pinfo.name }">${pinfo.name }</div>
        <c:if test="${not empty pinfo.infoList}">
          <div class="detail-pub__cognize-toast" style="display: none">
            <c:forEach items="${pinfo.infoList}" var="info" varStatus="s">
              <c:if test="${s.index<=2 }">
                <div class="detail-pub__cognize-toast_container">
                  <div class="detail-pub__cognize-toast_header">
                    <div class="detail-pub__cognize-toast_header-avator">
                      <a href="${info.psnUrl }" target="_Blank"> <img src="${info.avatars }"
                        onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                      </a>
                    </div>
                    <div class="detail-pub__cognize-toast_header-infor">
                      <div class="detail-pub__cognize-toast_header-author">
                        <a href="${info.psnUrl }" target="_Blank">${info.name }</a>
                      </div>
                      <div class="detail-pub__cognize-toast_header-work" title="${info.psnInfo}">${info.psnInfo}</div>
                    </div>
                  </div>
                  <div class="detail-pub__cognize-toast_body">
                    <div class="detail-pub__cognize-toast_body-item">
                      <span class="detail-pub__cognize-toast_body-item_cnt">${info.prjCount }</span> <span
                        class="detail-pub__cognize-toast_body-item_cnt"><spring:message
                          code="pub.details.search.psn_name1" /></span>
                    </div>
                    <div class="detail-pub__cognize-toast_body-item">
                      <span class="detail-pub__cognize-toast_body-item_cnt">${info.pubCount }</span> <span
                        class="detail-pub__cognize-toast_body-item_cnt"><spring:message
                          code="pub.details.search.psn_name2" /></span>
                    </div>
                    <div class="detail-pub__cognize-toast_body-item">
                      <span class="detail-pub__cognize-toast_body-item_cnt">${info.hIndex }</span> <span
                        class="detail-pub__cognize-toast_body-item_cnt"><spring:message
                          code="pub.details.search.psn_name3" /></span>
                    </div>
                  </div>
                  <div class="detail-pub__cognize-toast_footer">
                    <c:if test="${info.isFriend=='0' }">
                      <div class="detail-pub__cognize-toast_footer-add"
                        onclick="Pubdetails.addOneFriend('${info.des3PsnId }',this)">
                        <spring:message code="pub.details.search.psn_name4" />
                      </div>
                    </c:if>
                    <c:if test="${info.isFriend=='1' }">
                      <div class="detail-pub__cognize-toast_footer-add"
                        onclick="Pubdetails.sendMsg('${info.des3PsnId }',this)">
                        <spring:message code="pub.details.search.psn_name7" />
                      </div>
                    </c:if>
                  </div>
                </div>
              </c:if>
            </c:forEach>
            <%-- <div class="detail-pub__cognize-toast_container-footer">
							<c:if test="${pinfo.infoList != null && fn:length(pinfo.infoList) > 2}">
								<span class="detail-pub__cognize-toast_container-footer_detaile">
									<span class="dev_b_span">
										<s:text name="pub.details.search.psn_name5" />${fn:length(pinfo.infoList)}<s:text name="pub.details.search.psn_name6" />
									</span>
								</span>
							</c:if>
						</div> --%>
          </div>
        </c:if>
      </div>
    </c:forEach>
  </c:if>
</div>