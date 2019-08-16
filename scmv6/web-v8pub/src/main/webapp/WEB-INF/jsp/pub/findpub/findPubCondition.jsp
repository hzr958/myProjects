<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="new-mainpage_left-item" style="height: auto;">
    <div class="new-mainpage_left-list" name="searchArea">
        <div  class="new-mainpage_left-list_header">
            <span class="new-mainpage_left-list_header-title"><spring:message code="pub.find.area" /></span>
            <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
        </div>
        <div class="new-mainpage_left-list_body">
          <c:forEach items="${pubVo.scienceAreas }" var="area">
            <div class="new-mainpage_left-sublist dev_area_parent" value="${area.areaId }"  title="${area.showName }">
                <div class="new-mainpage_left-sublist_header">
                    <i class="new-mainpage_left-sublist_header-onkey"></i>
                    <span class="new-mainpage_left-sublist_header-title">${area.showName }</span>
                </div>
                <div class="new-mainpage_left-sublist_body" style="display: none;">
                    <c:forEach items="${area.second }" var="areaSecond">
                      <div class="new-mainpage_left-sublist_body-item" value="${areaSecond.areaId }" title="${areaSecond.showName }">
                          <span class="new-mainpage_left-sublist_body-detail">${areaSecond.showName }</span>
                          <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                      </div>
                    </c:forEach> 
                </div>
            </div>
           </c:forEach> 
        </div>
    </div>
    
    <div class="new-mainpage_left-list" name="searchPubType">
        <div  class="new-mainpage_left-list_header">
            <span class="new-mainpage_left-list_header-title"><spring:message code="pub.filter.research" /></span>
            <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
        </div>
        <div class="new-mainpage_left-list_body new-mainpage_left-list_body-limit">
             <div class="new-mainpage_left-sublist">
                <div class="new-mainpage_left-sublist_body ">
                    <div class="new-mainpage_left-sublist_body-item" value="4" title='<spring:message code="pub.filter.journalArticle" />'>
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="pub.filter.journalArticle" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="3" title='<spring:message code="pub.filter.conferencePaper" />'>
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="pub.filter.conferencePaper" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="5" title='<spring:message code="pub.filter.patent" />'>
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="pub.filter.patent" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>        
                    <div class="new-mainpage_left-sublist_body-item" value="7" title='<spring:message code="pub.filter.indexes.others" />'>
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="pub.filter.indexes.others" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="new-mainpage_left-list" name="includeType">
        <div  class="new-mainpage_left-list_header">
            <span class="new-mainpage_left-list_header-title"><spring:message code="publicationEdit.label.pub_list" /></span>
            <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
        </div>
        <div class="new-mainpage_left-list_body new-mainpage_left-list_body-limit">
             <div class="new-mainpage_left-sublist">
                <div class="new-mainpage_left-sublist_body ">
                    <div class="new-mainpage_left-sublist_body-item" value="14" title='EI'>
                        <span class="new-mainpage_left-sublist_body-detail">EI</span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="16" title='SCIE'>
                        <span class="new-mainpage_left-sublist_body-detail">SCIE</span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="17" title='SSCI'>
                        <span class="new-mainpage_left-sublist_body-detail">SSCI</span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="15" title='ISTP'>
                        <span class="new-mainpage_left-sublist_body-detail">ISTP</span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>                   
                </div>
            </div>
        </div>
    </div>

    <div class="new-mainpage_left-list" name="publishYear">
        <div  class="new-mainpage_left-list_header">
            <span class="new-mainpage_left-list_header-title"><spring:message code="psn.leftmenu.pubYear" /></span>
            <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
        </div>
        <div class="new-mainpage_left-list_body new-mainpage_left-sublist_body-single">
             <div class="new-mainpage_left-sublist">
                <div class="new-mainpage_left-sublist_body">
                    <div class="new-mainpage_left-sublist_body-item" value="${pubVo.currentYear }" title=''>
                        <span class="new-mainpage_left-sublist_body-detail">
                          <c:if test="${locale == 'en_US' }">
                          <spring:message code="pub.recommend.pubyear" /> ${pubVo.currentYear }
                          </c:if>
                          <c:if test="${locale != 'en_US' }">
                          ${pubVo.currentYear }<spring:message code="pub.recommend.pubyear" /> 
                          </c:if>                         
                        </span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="${pubVo.currentYear },${pubVo.currentYear - 1},${pubVo.currentYear - 2}" title=''>
                        <span class="new-mainpage_left-sublist_body-detail">
                          <c:if test="${locale == 'en_US' }">
                          <spring:message code="pub.recommend.pubyear" /> ${pubVo.currentYear - 2 }
                          </c:if>
                          <c:if test="${locale != 'en_US' }">
                          ${pubVo.currentYear - 2 }<spring:message code="pub.recommend.pubyear" /> 
                          </c:if>                        
                        </span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="${pubVo.currentYear },${pubVo.currentYear - 1},${pubVo.currentYear - 2},${pubVo.currentYear - 3},${pubVo.currentYear - 4}" title=''>
                        <span class="new-mainpage_left-sublist_body-detail">
                          <c:if test="${locale == 'en_US' }">
                          <spring:message code="pub.recommend.pubyear" /> ${pubVo.currentYear - 4 }
                          </c:if>
                          <c:if test="${locale != 'en_US' }">
                          ${pubVo.currentYear - 4 }<spring:message code="pub.recommend.pubyear" /> 
                          </c:if>
                        </span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="0" title='<spring:message code="psn.pub.publish.year.unlimited" />'>
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="psn.pub.publish.year.unlimited" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                </div>
            </div>
        </div>
    </div>

   <%--  <div class="new-mainpage_left-list" name="searchLanguage">
        <div  class="new-mainpage_left-list_header">
            <span class="new-mainpage_left-list_header-title"><spring:message code="pub.find.language" /></span>
            <i class="material-icons new-mainpage_left-list_header-onkey">expand_less</i>
        </div>
        <div class="new-mainpage_left-list_body new-mainpage_left-sublist_body-single">
             <div class="new-mainpage_left-sublist">
                <div class="new-mainpage_left-sublist_body ">
                    <div class="new-mainpage_left-sublist_body-item" value="zh_CN">
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="pub.find.chinese" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="en_US">
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="pub.find.other" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                    <div class="new-mainpage_left-sublist_body-item" value="0">
                        <span class="new-mainpage_left-sublist_body-detail"><spring:message code="psn.pub.publish.year.unlimited" /></span>
                        <i class="material-icons  new-mainpage_left-sublist_body-onclose">close</i>
                    </div>
                </div>
            </div>
        </div>
    </div> --%>

</div>