<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${pubRcmdTotalCount > 0 }">
  <div class="pub-idx__main_add" style="padding-left: 20px; border: none;">
    <i class="material-icons pub-idx__main_add-tip" id="isCheck" onclick="changCheckBox()" style="width: 24px; height: 24px;">check_box</i><span
      class="pub-idx__main_add-detail">邀请我的合作者成为联系人</span>
  </div>
</c:if>
<c:choose>
    <c:when test="${whoFirst eq 'default' || whoFirst eq 'fullTextRcmd'}">
        <c:if test="${fullTextTotalCount > 0}">
          <c:forEach items="${fullTextList}" var="page" varStatus="stat">
              <div class="list_item_container" id="fulltext_confirmation_container_${page.id}" style="border: none;">
                <input class="rcmd_pub_fulltext_tr" id="rcmd_pub_fulltext_tr_${page.id}"
                  name="rcmd_pub_fulltext_tr_${page.id }" type="hidden" value="${page.id }">
                <input id="page.pub.zhTitle" name="page.pub.zhTitle" type="hidden" value="${page.pub.title}">
                <div style="flex-direction: column; border: none;width: 100vw; padding-left: 20px;">
                  <div class="pub_whole" style="border-top: 1px solid #ddd; padding: 20px 00px 20px 00px;">
                    <div class="pub_avatar pub_avatar-normal_size" onclick="Msg.downloadFTFile('${page.downloadUrl }');">
                      <img src="${page.fullTextImagePath}" onerror="javascript:this.src='${resmod}/images_v5/images2016/file_img1.jpg'"
                        style="border: 1px solid #ccc;">
                    </div>
                    <div class="pub_information">
                      <div style="color: #333; font-weight: bold; width: 90%;" onclick="Msg.openPubfulltextDetail('${stat.index+1}','centerMsg',document.getElementById('whoFirst').value)">${page.pub.title}</div>
                      <div class="author">${page.pub.authorNames}</div>
                      <div class="source" style="width:90%">${page.pub.briefDesc}</div>
                      <div style="width: 100%; display: flex; justify-content: flex-start;">
                        <div class="operations" style="transform: translateX(-8px); display: flex; width: 100%;">
                           <div onclick="Msg.doConfirmPubft(${page.id},'',this);" class="btn_normal btn_bg_blue dev_yes_botton">是成果的全文</div>
                           <div onclick="Msg.doRejectPubft(${page.id},'',this);" class="btn_normal btn_bg_origin  btn_normal-not_mine dev_not_botton">不是成果的全文</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </c:forEach>
      </c:if>
      <c:if test="${pubRcmdTotalCount > 0 }">
         <div class="body_content">
            <div class="body_content_container">
              <div class="list_container" id="listdiv">
                <c:forEach items="${pubRcmdList}" var="pub" varStatus="stat">
                    <div class="list_item_container" id="pub_${pub.pubId}" name="dataCount"
                      des3PubId="<iris:des3 code='${pub.pubId }'/>" des3PsnId="<iris:des3 code='${psnId}'/>" style="border: none; padding: 0px;">
                      <div class="list_item_section"></div>
                      <div class="list_item_section" style="padding-left: 20px;">
                        <div class="pub_whole" style="padding: 20px 0px 20px 0px; border-top:1px solid #ddd;">
                          <div class="pub_avatar" onclick="mobile.pub.downloadFTFile('${pub.fullTextDownloadUrl }')">
                            <c:if test="${not empty pub.fullTextFieId}">
                              <img src="${pub.fullTextImgUrl}"
                                onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" class="pub_avatar"
                                style="border: 1px solid #ccc;">
                            </c:if>
                            <c:if test="${empty pub.fullTextFieId}">
                              <img src="${resmod}/images_v5/images2016/file_img.jpg" class="pub_avatar"
                                style="border: 1px solid #ccc;">
                            </c:if>
                          </div>
                          <div class="pub_information">
                            <div class="title webkit-multipleline-ellipsis"
                              onclick="mobile.pub.pdwhDetails('<iris:des3 code='${pub.pubId }'/>');">
                              <c:out value="${pub.title}" escapeXml="false" />
                            </div>
                            <div class="author">
                              <c:out value="${pub.authorNames}" escapeXml="false" />
                            </div>
                            <div class="source" style="width: 90%;">
                              <c:out value="${pub.briefDesc}" escapeXml="false" />
                            </div>
                            <div class="operations" style="transform: translateX(-8px)">
                              <div onclick="Msg.AffirmConfirmPub(this,'${pub.pubId}');" class="btn_normal btn_bg_blue">这是我的成果</div>
                              <div onclick="Msg.IgnoreConfirmPub(this,'${pub.pubId}');"
                                class="btn_normal btn_bg_origin btn_normal-not_mine">不是我的成果</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </c:forEach>
              </div>
            </div>
          </div>
      </c:if>
      <c:if test="${pubRcmdTotalCount ne 0 || fullTextTotalCount ne 0}">
        <div style="border:none;padding-left:0px;border-top: 1px solid #ddd;" no-more-record = "no_more_record_tips">
           <div style='padding:20px;color:#999;text-align:center;font-size: 14px;'>没有更多记录</div>
        </div>
      </c:if>
      <c:if test="${pubRcmdTotalCount eq 0 && fullTextTotalCount eq 0}">
         <div class="response_no-result" style="padding-top: 67px; text-align: center;">
            <c:if test="${locale eq 'zh_CN'}">
                                        未找到符合条件的记录
            </c:if>
            <c:if test="${locale eq 'en_US'}">
                  No result found                  
            </c:if>
         </div>
      </c:if>
    </c:when>
    <c:otherwise>
          <c:if test="${pubRcmdTotalCount > 0 }">
         <div class="body_content">
            <div class="body_content_container">
              <div class="list_container" id="listdiv">
                <c:forEach items="${pubRcmdList}" var="pub" varStatus="stat">
                    <div class="list_item_container" id="pub_${pub.pubId}" name="dataCount"
                      des3PubId="<iris:des3 code='${pub.pubId }'/>" des3PsnId="<iris:des3 code='${psnId}'/>" style="border: none; padding: 0px;">
                      <div class="list_item_section"></div>
                      <div class="list_item_section" style="padding-left: 20px;">
                        <div class="pub_whole" style="padding: 20px 0px 20px 0px;border-top:1px solid #ddd;">
                          <div class="pub_avatar" onclick="mobile.pub.downloadFTFile('${pub.fullTextDownloadUrl }')">
                            <c:if test="${not empty pub.fullTextFieId}">
                              <img src="${pub.fullTextImgUrl}"
                                onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" class="pub_avatar"
                                style="border: 1px solid #ccc;">
                            </c:if>
                            <c:if test="${empty pub.fullTextFieId}">
                              <img src="${resmod}/images_v5/images2016/file_img.jpg" class="pub_avatar"
                                style="border: 1px solid #ccc;">
                            </c:if>
                          </div>
                          <div class="pub_information">
                            <div class="title webkit-multipleline-ellipsis"
                              onclick="mobile.pub.pdwhDetails('<iris:des3 code='${pub.pubId }'/>');">
                              <c:out value="${pub.title}" escapeXml="false" />
                            </div>
                            <div class="author">
                              <c:out value="${pub.authorNames}" escapeXml="false" />
                            </div>
                            <div class="source" style="width: 90%;">
                              <c:out value="${pub.briefDesc}" escapeXml="false" />
                            </div>
                            <div class="operations" style="transform: translateX(-8px)">
                              <div onclick="Msg.AffirmConfirmPub(this,'${pub.pubId}');" class="btn_normal btn_bg_blue">这是我的成果</div>
                              <div onclick="Msg.IgnoreConfirmPub(this,'${pub.pubId}');"
                                class="btn_normal btn_bg_origin btn_normal-not_mine">不是我的成果</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </c:forEach>
              </div>
            </div>
          </div>
      </c:if>
        <c:if test="${fullTextTotalCount > 0}">
          <c:forEach items="${fullTextList}" var="page" varStatus="stat">
              <div class="list_item_container" id="fulltext_confirmation_container_${page.id}" style="border: none;">
                <input class="rcmd_pub_fulltext_tr" id="rcmd_pub_fulltext_tr_${page.id}"
                  name="rcmd_pub_fulltext_tr_${page.id }" type="hidden" value="${page.id }">
                <input id="page.pub.zhTitle" name="page.pub.zhTitle" type="hidden" value="${page.pub.title}">
                <div style="flex-direction: column; border: none;width: 100vw; padding-left: 20px;">
                  <div class="pub_whole" style="border-top: 1px solid #ddd; padding: 20px 00px 20px 00px;">
                    <div class="pub_avatar pub_avatar-normal_size" onclick="Msg.downloadFTFile('${page.downloadUrl }');">
                      <img src="${page.fullTextImagePath}" onerror="javascript:this.src='${resmod}/images_v5/images2016/file_img1.jpg'"
                        style="border: 1px solid #ccc;">
                    </div>
                    <div class="pub_information">
                      <div style="color: #333; font-weight: bold; width: 90%;" onclick="Msg.openPubfulltextDetail('1','centerMsg',document.getElementById('whoFirst').value)">${page.pub.title}</div>
                      <div class="author">${page.pub.authorNames}</div>
                      <div class="source" style="width:90%">${page.pub.briefDesc}</div>
                      <div style="width: 100%; display: flex; justify-content: flex-start;">
                        <div class="operations" style="transform: translateX(-8px); display: flex; width: 100%;">
                           <div onclick="Msg.doConfirmPubft(${page.id},'',this);" class="btn_normal btn_bg_blue dev_yes_botton">是成果的全文</div>
                           <div onclick="Msg.doRejectPubft(${page.id},'',this);" class="btn_normal btn_bg_origin  btn_normal-not_mine dev_not_botton">不是成果的全文</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </c:forEach>
      </c:if>
      <c:if test="${pubRcmdTotalCount ne 0 || fullTextTotalCount ne 0}">
        <div style='border:none;padding-left:0px;border-top: 1px solid #ddd;' no-more-record = "no_more_record_tips">
           <div style='padding:20px;color:#999;text-align:center;font-size: 14px;'>没有更多记录</div>
        </div>
      </c:if>
      <c:if test="${pubRcmdTotalCount eq 0 && fullTextTotalCount eq 0}">
         <div class="response_no-result" style="padding-top: 65px; text-align: center;">
            <c:if test="${locale eq 'zh_CN'}">
                                        未找到符合条件的记录
            </c:if>
            <c:if test="${locale eq 'en_US'}">
                  No result found                  
            </c:if>
         </div>
      </c:if>
    </c:otherwise>
</c:choose>

