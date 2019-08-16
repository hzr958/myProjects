<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="new-mobilegroup_header" id="grp_info_div">
    <div class="new-mobilegroup_header-avator">
       <img src="${grpInfo.grpBaseInfo.grpAuatars }"
          onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
    </div>
    <div class="new-mobilegroup_header-infor">
         <div class="new-mobilegroup_header-title">
            <div class="new-mobilegroup_header-title_detail">${grpInfo.grpBaseInfo.grpName }</div>
            <div class="new-mobilegroup_header-title_check" onclick="Group.goToDetails('${dto.des3GrpId }');">详细</div>
         </div>
         <div class="new-mobilegroup_body-item_infor-key" style="margin: 0px 0px 6px -6px;">
              <c:if test="${not empty grpInfo.firstDisciplinetName}">
                 <div class="new-mobilegroup_body-item_infor-key_item">
                      ${grpInfo.firstDisciplinetName}                 
                 </div>
               </c:if>
               <c:if test="${not empty grpInfo.secondDisciplinetName}">                       
                 <div class="new-mobilegroup_body-item_infor-key_item">
                      ${grpInfo.secondDisciplinetName}                       
                 </div>
               </c:if>
               <c:set value="${fn:split(grpInfo.grpKwDisc.keywords,';') }" var="keylist"></c:set>
               <c:forEach items="${keylist }" var="key">
                   <c:if test="${not empty key}">                       
                     <div class="new-mobilegroup_body-item_infor-key_item">
                          ${key}                       
                     </div>
                   </c:if>                          
               </c:forEach>
         </div>
         <div class="new-mobilegroup_header-infor_introduction">                         
          <!--  课程群组 成员，文献，课件，作业  -->
          <c:if test="${grpInfo.grpBaseInfo.grpCategory ==10 }">             
            <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpStatistic.sumMember }</div>
               <div class="new-mobilegroup_body-item_infor-right_name">成员</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpStatistic.sumPubs}</div>
               <div class="new-mobilegroup_body-item_infor-right_name">文献</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpCourseFileSum }</div>
               <div class="new-mobilegroup_body-item_infor-right_name">课件</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpWorkFileSum }</div>
               <div class="new-mobilegroup_body-item_infor-right_name">作业</div>
           </div>
          </c:if>
          <!--  项目群组 成员，成果，文献，文件   -->
          <c:if test="${grpInfo.grpBaseInfo.grpCategory ==11 }">              
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpStatistic.sumMember }</div>
               <div class="new-mobilegroup_body-item_infor-right_name">成员</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpProjectPubSum}</div>
               <div class="new-mobilegroup_body-item_infor-right_name">成果</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpProjectRefSum}</div>
               <div class="new-mobilegroup_body-item_infor-right_name">文献</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpStatistic.sumFile }</div>
               <div class="new-mobilegroup_body-item_infor-right_name">文件</div>
           </div>
          </c:if>
          <c:if test="${grpInfo.grpBaseInfo.grpCategory !=10 &&  grpInfo.grpBaseInfo.grpCategory !=11}">                        
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpStatistic.sumMember }</div>
               <div class="new-mobilegroup_body-item_infor-right_name">成员</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpStatistic.sumPubs}</div>
               <div class="new-mobilegroup_body-item_infor-right_name">成果</div>
           </div>
           <div>
               <div class="new-mobilegroup_body-item_infor-right_num">${grpInfo.grpStatistic.sumFile}</div>
               <div class="new-mobilegroup_body-item_infor-right_name">文件</div>
           </div>              
          </c:if>
         </div>
    </div>
</div>