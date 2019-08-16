<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="js_listinfo" smate_count='${totalCount}'></div>
<c:forEach items="${ dynList}" var="groupDynShowInfo" varStatus="gds">
  <div class="new-mobilegroup_body-item main-list__item" style=" flex-direction: column; margin: 0px;">
    
      <div class="dynamic__box load_sample_comment discuss_box" isCanDel="${groupDynShowInfo.isCanDel }"
        permission="${groupDynShowInfo.resPremission }" resFullTextFileId="${groupDynShowInfo.resFullTextFileId }"
        resFullTextImage="${groupDynShowInfo.resFullTextImage}" dynTime="${groupDynShowInfo.dynDateForShow }"
        dynId="${groupDynShowInfo.dynId }" id="discuss_box_${groupDynShowInfo.dynId }"
        des3DynId="<iris:des3 code='${groupDynShowInfo.dynId }'/>">
        <%-- ${groupDynShowInfo.dynContent} --%>
        <c:set var="jsonDynInfo" value="${groupDynShowInfo.jsonDynInfo}"></c:set>
        <%@ include file="grp_dyn_content.jsp" %>
        
        <div class="new-Standard_Function-bar" style="margin-top: 12px; padding: 0px 16px; width: 90vw;">
            <a class="manage-one mr20" onclick="GroupDyn.award('${groupDynShowInfo.des3DynId }', '${groupDynShowInfo.resType }',
             '${groupDynShowInfo.des3ResId}', '${groupDynShowInfo.dynType}', this)"> 
             <div class="new-Standard_Function-bar_item" style="margin-left: 0px;width:100%">
                  <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> 
                  <span class="new-Standard_Function-bar_item-title span_award">
                    <c:if test="${groupDynShowInfo.awardstatus == 1 }">取消赞</c:if> 
                    <c:if test="${groupDynShowInfo.awardstatus == 0 }">赞</c:if>
                    <iris:showCount count="${groupDynShowInfo.awardCount }" preFix="(" sufFix=")"/>
                  </span>
               </div>
           </a>
          
          <c:if test="${ groupDynShowInfo.resType != 'fund' && groupDynShowInfo.resType != 'agency'}">
            <a class="manage-one mr20 dev_pub_comment" onclick="Group.dynComment('<iris:des3 code="${groupDynShowInfo.dynId }"/>')">
              <div class="new-Standard_Function-bar_item" style="width:100%;">
                <i class="new-Standard_function-icon new-Standard_comment-icon"></i> 
                <span class="new-Standard_Function-bar_item-title span_comment" >
                                                        评论<iris:showCount count="${groupDynShowInfo.commentCount }" preFix="(" sufFix=")"/>
                </span>
              </div>
            </a>
            
          </c:if>
          <c:if test="${groupDynShowInfo.resId !=0 && groupDynShowInfo.resId !=null && not empty groupDynShowInfo.resId}">
            <a class="manage-one mr20 dev_pub_share" from="grp_dyn" des3DynId="${groupDynShowInfo.des3DynId }" onclick="Group.shareRes('${groupDynShowInfo.des3ResId}', '${groupDynShowInfo.resType}', '${jsonDynInfo.RES_NOT_EXISTS }', this);"
              resid="${groupDynShowInfo.des3ResId}" des3resid="" nodeid="1" restype="${groupDynShowInfo.resType}"
              databaseType="" resInfoId="${groupDynShowInfo.dynId }" notEncodeId="${groupDynShowInfo.resId }">
                <div class="new-Standard_Function-bar_item" style="width:100%;">
                  <i class="new-Standard_function-icon new-Standard_Share-icon"></i> 
                  <span class="new-Standard_Function-bar_item-title span_share">
                                                            分享<iris:showCount count="${groupDynShowInfo.shareCount }" preFix="(" sufFix=")"/>
                  </span>
                </div>
            </a>
          </c:if>
          
          <c:if
            test="${groupDynShowInfo.resId !=0 && groupDynShowInfo.resId !=null && not empty groupDynShowInfo.resId}">
            <!-- 收藏、取消收藏成果 -->
            <c:if test="${groupDynShowInfo.resType =='pub' || groupDynShowInfo.resType =='pdwhpub' }">
              <c:if test="${ groupDynShowInfo.hasCollenciton}">
                <a class="manage-one mr20" collected="1" onclick="GroupDyn.dealCollectedPub('<iris:des3 code="${groupDynShowInfo.resId}"/>','${groupDynShowInfo.resType}',this, '${jsonDynInfo.RES_NOT_EXISTS }')"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">取消收藏</span>
                   </div>
                </a>
              </c:if>
              <c:if test="${ !groupDynShowInfo.hasCollenciton}">
                <a class="manage-one mr20" collected="0" onclick="GroupDyn.dealCollectedPub('<iris:des3 code="${groupDynShowInfo.resId}"/>','${groupDynShowInfo.resType}',this, '${jsonDynInfo.RES_NOT_EXISTS }')"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">收藏</span>
                   </div>
                </a>
              </c:if>
            </c:if>
            <!-- 收藏文件 -->
            <c:if test="${ groupDynShowInfo.resType =='grpfile'}">
              <a class="manage-one mr20" onclick="GroupDyn.grpDyncollectionGrpFile(this, '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">收藏</span>
                   </div>
                </a>
            </c:if>
            
            <!-- 收藏、取消收藏基金 -->
            <c:if test="${groupDynShowInfo.resType =='fund'}">
              <a class="manage-one mr20 collectCancel_${groupDynShowInfo.resId }" style="${groupDynShowInfo.hasCollenciton ? '' : 'display: none;'}"
                onclick="GroupDyn.dynCollectCoperation($(this), '${groupDynShowInfo.des3ResId}', 1, '${groupDynShowInfo.resId }', '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">取消收藏</span>
                   </div>
                </a>
              
              <a class="manage-one mr20 collect_${groupDynShowInfo.resId }" style="${groupDynShowInfo.hasCollenciton ? 'display: none;' : ''}" 
                onclick="GroupDyn.dynCollectCoperation($(this), '${groupDynShowInfo.des3ResId}', 0, '${groupDynShowInfo.resId }', '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">收藏</span>
                   </div>
                </a>
            </c:if>
            
            <!-- 关注、取消关注资助机构 -->
            <c:if test="${ groupDynShowInfo.resType =='agency'}">
              <c:if test="${ groupDynShowInfo.hasCollenciton}">
              <a class="manage-one mr20 agency_cancel_interest_opt"
                agencyDes3Id="${groupDynShowInfo.des3ResId}"
                onclick="GroupDyn.ajaxDynamicInterest($(this), '${groupDynShowInfo.des3ResId}', 0, '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                 <div class="new-Standard_Function-bar_item" style="width:100%;" >
                       <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                       <span class="new-Standard_Function-bar_item-title span_collect">取消关注</span>
                 </div>
              </a>
              </c:if>
              
              <c:if test="${ !groupDynShowInfo.hasCollenciton}">
              <a class="manage-one mr20 agency_interest_opt" agencyDes3Id="${groupDynShowInfo.des3ResId}"
                onclick="GroupDyn.ajaxDynamicInterest($(this), '${groupDynShowInfo.des3ResId}', 1, '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">关注</span>
                   </div>
                </a>
                </c:if>
                
            </c:if>
          </c:if>
        </div>
      </div>
  </div>
</c:forEach>
