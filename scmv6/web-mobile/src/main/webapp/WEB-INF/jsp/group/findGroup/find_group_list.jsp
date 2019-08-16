<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="js_listinfo" smate_count='${totalCount}'></div>
<c:forEach items="${goupList}" var="grpShowInfo" varStatus="status">
    <div class="new-mobilegroup_body-item main-list__item" style="padding: 20px 0px 20px 20px!important; margin: 0px;">
       <div class="new-mobilegroup_body-item_avator">
            <div class="grp-idx__logo_img">
              <a href="javaScript: openGrpDetail('<iris:des3 code="${grpShowInfo.grpBaseInfo.grpId}"/>')"> <img
                src="${grpShowInfo.grpBaseInfo.grpAuatars}"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
            <div class="grp-idx__grp-type new-mobilegroup_type">
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory ==12}">
                <span>兴趣群组</span>
              </c:if>
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory ==11}">
                <span>项目群组</span>
              </c:if>
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory ==10}">
                <span>课程群组</span>
              </c:if>
            </div>
       </div>
       <div class="new-mobilegroup_body-item_body">
          <div class="new-mobilegroup_body-item_infor">
                <div class="new-mobilegroup_body-item_infor-left" >
                   <div class="new-mobilegroup_body-item_infor-title">
                       <a href="javaScript: openGrpDetail('<iris:des3 code="${grpShowInfo.grpBaseInfo.grpId}"/>')">${grpShowInfo.grpBaseInfo.grpName}</a>
                   </div>
                   <div class="new-mobilegroup_body-item_infor-key">
                       <c:if test="${not empty grpShowInfo.firstDisciplinetName}">
                         <div class="new-mobilegroup_body-item_infor-key_item">
                              ${grpShowInfo.firstDisciplinetName}                 
                         </div>
                       </c:if>
                       <c:if test="${not empty grpShowInfo.secondDisciplinetName}">                       
                         <div class="new-mobilegroup_body-item_infor-key_item">
                              ${grpShowInfo.secondDisciplinetName}                       
                         </div>
                       </c:if>
                       <c:set value="${fn:split(grpShowInfo.grpKwDisc.keywords,';') }" var="keylist"></c:set>
                       <c:forEach items="${keylist }" var="key">
                           <c:if test="${not empty key}">                       
                             <div class="new-mobilegroup_body-item_infor-key_item">
                                  ${key}                       
                             </div>
                           </c:if>                          
                       </c:forEach>
                   </div>
                </div>
                <div class="new-mobilegroup_body-item_infor-right" >   
                    <div>
                         <div class="new-mobilegroup_body-item_infor-right_num">${grpShowInfo.grpStatistic.sumMember}</div>
                         <div class="new-mobilegroup_body-item_infor-right_name">成员</div>
                     </div>          
               </div>
          </div>
          <div class="new-mobilegroup_body-item_func">
             <div class="new-mobilegroup_body-item_func-add" onclick="Group.optRcmdGrp(this,'${grpShowInfo.des3GrpId }' , 1 ,'${grpShowInfo.grpBaseInfo.openType}')">加入</div>
             <div class="new-mobilegroup_body-item_func-ignore" onclick="Group.optRcmdGrp(this,'${grpShowInfo.des3GrpId }',9)">忽略</div>
          </div>
       </div>
    </div>
</c:forEach>
