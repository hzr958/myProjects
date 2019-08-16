<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${totalCount}'></div>
<c:if test="${not empty fileList && fn:length(fileList) > 0}">
    <c:forEach items="${fileList}" var="fileShowInfo" varStatus="status">
        <input type = "hidden" value = "${fileShowInfo.des3GrpFileId}" id = "m_grp_file_des3GrpFileId"/>
        <div class="new-mobilegroup_body-item main-list__item" style=" flex-direction: column; border-bottom:1px dashed #ddd; margin: 0px; padding: 0px 0px 12px 16px!important; ">
            <div class="new-mobilegroup_fabulous-container">
               <div class="new-mobilegroup_fabulous-avator" onclick="MobileGrpFile.downloadFile('${fileShowInfo.downloadUrl }')">
                    <img src="${fileShowInfo.imgThumbUrl}" onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
               </div>
               <div class="new-mobilegroup_body-item_body" style="text-align: left; width: 55vw;">
                   <div class="new-mobilegroup_fabulous-title">
                        <a href="javascript:MobileGrpFile.downloadFile('${fileShowInfo.downloadUrl }');"> ${fileShowInfo.fileName } </a>
                   </div>
                   <div class="new-mobilegroup_fabulous-infor" style="width: 95%;">
                     <div  class="new-mobilegroup_fabulous-infor_time" style="width: 95%;">
                         ${fileShowInfo.fileDesc }
                     </div>
                   </div>
                   
                   <div class="new-Standard_Function-bar" style="margin-top: 12px;">
                      <a class="manage-one mr20 dev_pub_share">
                          <div class="new-Standard_Function-bar_item" style="width:100%;">
                            <i class="new-Standard_function-icon new-Standard_Share-icon"></i> 
                            <span class="new-Standard_Function-bar_item-title span_share" id = "file_item_${fileShowInfo.des3GrpFileId }" onclick = "shareGrpFile('${fileShowInfo.des3GrpFileId }','grpFile')">分享
                                <%-- <iris:showCount count="${fileShowInfo.shareCount}" preFix="(" sufFix=")"/> --%>
                            </span>
                          </div>
                      </a>
                    </div>           
             </div>
               
             <div class="file-idx__main_src" style="font-size: 12px; color: #666;">
              <div class="file-idx__src_time">
                <jsp:useBean id="uploadDate" class="java.util.Date"/>
                 <jsp:setProperty name="uploadDate" property="time" value="${fileShowInfo.uploadDate}"/>
                 <fmt:formatDate value="${uploadDate}" pattern="yyyy-MM-dd" />
              </div>
              <div class="file-idx__src_uploader"></div>
            </div>
            </div>
            
       </div>
    </c:forEach>
</c:if>
