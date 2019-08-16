<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}" scm_id="mobile_msg_chatbox__list"
  scm_totalcount="${page.totalCount }" scm_pageno=${page.pageNo } scm_pagetotalPages=${page.totalPages }></div>
<!-- 站内信 会话聊天框 消息列表 -->
<s:iterator value="msgShowInfoList" var="msil" status="st">
  <!--发送者是自己--显示在右-->
  <s:if test="#msil.IAmSender==0">
    <div class="state-content__left main-list__item" type="${msil.smateInsideLetterType }"
      des3RelationId="${msil.des3MsgRelationId }"
      dev_create_date="<fmt:formatDate value='${msil.createDate }' pattern='yyyy-MM-dd HH:mm'/>">
      <input type="hidden" value="${msil.downloadUrl}" name="downloadUrl" />
      <c:if test="${msil.ifShowDate==true }">
        <div class="state-content__commit-time">
          <fmt:formatDate value='${msil.createDate }' pattern='yyyy-MM-dd HH:mm' />
        </div>
      </c:if>
      <div class="state-content__left-infor"><!--否则--显示在左-->
        <a class="state-content__left-infor" href="${msil.senderShortUrl }"> <img class="state-content__left-avator"
          src="${msil.senderAvatars }">
          <div class="state-content__left-name">
            <c:if test="${locale == 'en_US' }">
	                        ${msil.senderEnName }
	                    </c:if>
            <c:if test="${locale == 'zh_CN' }">
	                        ${msil.senderZhName }
	                    </c:if>
          </div>
        </a>
      </div>
      <s:if test="#msil.smateInsideLetterType=='text'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail">${msil.content }</div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='pub'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail" des3FileId='<iris:des3 code="${msil.pubFulltextId}"/>'
            des3PubId="${msil.des3PubId }"  style=" align-items: flex-start;">
            <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
              class="state-content__left-detail__avator" src="${msil.pubFulltextImagePath }"
              onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
            </a> <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
            <span class="state-content__left-detail__name state-content__detail-abbreviations" style="font-weight: bold;"> <c:if
                  test="${locale == 'en_US' }">
                            ${msil.pubTitleEn }
                        </c:if> <c:if test="${locale == 'zh_CN' }">
                            ${msil.pubTitleZh }
                        </c:if>
             </span>
             <span class="state-content__right-detail__author">
              ${msil.pubAuthorName }
             </span>
             <span class="state-content__right-detail__source">
             <c:if test="${locale == 'en_US' }">${msil.pubBriefEn }</c:if>
              <c:if test="${locale == 'zh_CN' }"> ${msil.pubBriefZh }</c:if>
             </span>
            </a>
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='pdwhpub'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail" des3PubId="${msil.des3PubId }" dbid="${msil.dbid }"  style=" align-items: flex-start;">
            <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
              class="state-content__left-detail__avator" src="${msil.pubFulltextImagePath }"
              onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
            </a> <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
            <span class="state-content__left-detail__name state-content__detail-abbreviations" style="font-weight: bold;"> <c:if
                  test="${locale == 'en_US' }">
                            ${msil.pubTitleEn }
                        </c:if> <c:if test="${locale == 'zh_CN' }">
                            ${msil.pubTitleZh }
                        </c:if> 
            </span>
            <span class="state-content__right-detail__author">
            
              <c:if test="${locale == 'en_US' }">${msil.pubBriefEn }</c:if>
              <c:if test="${locale == 'zh_CN' }"> ${msil.pubBriefZh }</c:if>
            </span>
            <span class="state-content__right-detail__source">
            </span>
            </a>
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='file'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail"
            des3ArchiveFileId='<iris:des3 code="${msil.archiveFileId}"/>'>
            <input type="hidden" value="${msil.fileDownloadUrl }" name="fileDownloadUrl" /> <a
              onclick="midmsg.downloadMain(event)" style="width: 72px;"> <s:if test='#msil.fileType=="txt"  '>
                <img class="state-content__left-detail__avator" src="${resmod}/smate-pc/img/fileicon_txt.png"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
              </s:if> <s:elseif test='#msil.fileType=="ppt" || msil.fileType=="pptx" '>
                <img class="state-content__left-detail__avator" src="${resmod}/smate-pc/img/fileicon_ppt.png"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
              </s:elseif> <s:elseif test='#msil.fileType=="doc" || msil.fileType=="docx" '>
                <img class="state-content__left-detail__avator" src="${resmod}/smate-pc/img/fileicon_doc.png"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
              </s:elseif> <s:elseif test='#msil.fileType=="rar" || msil.fileType=="zip" '>
                <img class="state-content__left-detail__avator" src="${resmod}/smate-pc/img/fileicon_zip.png"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
              </s:elseif> <s:elseif test='#msil.fileType=="xls" || msil.fileType=="xlsx" '>
                <img class="state-content__left-detail__avator" src="${resmod}/smate-pc/img/fileicon_xls.png"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
              </s:elseif> <s:elseif test='#msil.fileType=="pdf" '>
                <img class="state-content__left-detail__avator" src="${resmod}/smate-pc/img/fileicon_pdf.png"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
              </s:elseif> <s:elseif test='#msil.fileType=="imgIc"  || msil.fileType=="jpg" || msil.fileType=="png" '>
                <img class="state-content__left-detail__avator" src="${msil.imgThumbUrl }"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_default.png'">
              </s:elseif> <s:else>
                <img class="state-content__left-detail__avator" src="${resmod}/smate-pc/img/fileicon_default.png"
                  onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_default.png'">
              </s:else>
            </a> <span class="state-content__left-detail__name state-content__detail-abbreviations"
              onclick="midmsg.downloadMain(event)">${msil.fileName }</span>
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='fund'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail" des3FundId='<iris:des3 code="${msil.fundId}"/>'>
            <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 60px;"> <img
              class="state-content__left-detail__avator" src="${msil.fundLogoUrl}"
              onerror="this.onerror=null;this.src='${ressns}/images/default/default_fund_logo.jpg'">
            </a> 
            
            <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="margin-left: 12px;">
             <span class="state-content__left-detail__name state-content__detail-abbreviations"  style="font-weight: bold;"> ${msil.showFundTitle }</span>
              <c:set value="${fn:split(msil.showDesc,'</br>') }" var="showDesc"></c:set>
               <c:if test="${not empty showDesc[0] }">
                    <span class="state-content__right-detail__author" style="font-weight: normal;">${showDesc[0] }</span>
               </c:if>
               <c:if test="${not empty showDesc[1] }">
                    <span class="state-content__right-detail__author" style="font-weight: normal;">${showDesc[1] } </span>
               </c:if>
               <c:if test="${not empty showDesc[2] }">
                     <span class="state-content__right-detail__source" style="font-weight:normal;"> ${showDesc[2] }</span>
               </c:if> 
            </a>
           
                     
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='fulltext'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail"
            des3ArchiveFileId='<iris:des3 code="${msil.archiveFileId}"/>'>
            <a onclick="midmsg.downloadMain(event)" style="width: 72px;"> <img
              class="state-content__left-detail__avator" src="${msil.pubFulltextImagePath }"
              onerror="this.onerror=null;this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
            </a> <span class="state-content__left-detail__name state-content__detail-abbreviations"
              onclick="midmsg.downloadMain(event)">${msil.fileName }</span>
          </div>
        </div>
      </s:if>
      <!-- 站内信类型  项目 -->
      <s:if test="#msil.smateInsideLetterType=='prj'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail" des3PrjId="${msil.des3PrjId }"  style=" align-items: flex-start;">
            <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
              class="state-content__left-detail__avator" src="${msil.prjImg}"
              onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
            </a> 
            <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
              <span class="state-content__left-detail__name state-content__detail-abbreviations" style="font-weight: bold;"> ${msil.title } </span> 
              <span class="state-content__right-detail__author"> ${msil.authorName } </span>
              <span class="state-content__right-detail__source"> ${msil.brief }</span>
            </a>
          </div>
        </div>
      </s:if>
      <!-- 站内信类型  资助机构 -->
      <s:if test="#msil.smateInsideLetterType=='agency'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail" des3AgencyId="${msil.des3AgencyId }"  style=" align-items: flex-start;">
            <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
              class="state-content__left-detail__avator" src="${msil.agencyLogoUrl}"
              onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_instdefault.png'">
            </a> <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
             <span class="state-content__left-detail__name state-content__detail-abbreviations" style="font-weight: bold;">
                ${msil.showAgencyTitle } </span> 
             <span class="state-content__right-detail__author"> ${msil.showDesc } </span>
             <span class="state-content__right-detail__source"></span>
            </a>
          </div>
        </div>
      </s:if>
      <!-- 站内信类型  新闻消息 -->
      <s:if test="#msil.smateInsideLetterType=='news'">
        <div class="state-content__left-detail__container">
          <div class="state-content__left-detail dev_detail" style=" align-items: flex-start;">
            <a  href="${msil.newsUrl}" target="_blank">
              <img class="state-content__right-detail__avator" src="${msil.newsImg}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_newsdefault.png'">
            </a>
            <span style="width: 70%;">
             <span class="state-content__left-detail__name state-content__detail-abbreviations" style="font-weight: bold;">
             <a class="pub-idx__main_titlea" href="${msil.newsUrl}" target="_blank"  style="word-wrap: break-word;">${msil.newsTitle }</a>
              </span>
            <span class="state-content__right-detail__author"> ${msil.newsBrief } </span>
            </span>
          </a>
          </div>
        </div>
      </s:if>
    </div>
  </s:if>
  </div>
  <!--否则--显示在左-->
  <s:elseif test="#msil.IAmSender==1">
    <div class="state-content__right main-list__item" type="${msil.smateInsideLetterType }"
      des3RelationId="${msil.des3MsgRelationId }"
      dev_create_date="<fmt:formatDate value='${msil.createDate }' pattern='yyyy-MM-dd HH:mm'/>">
      <input type="hidden" value="${msil.downloadUrl}" name="downloadUrl" />
      <c:if test="${msil.ifShowDate==true }">
        <div class="state-content__commit-time">
          <fmt:formatDate value='${msil.createDate }' pattern='yyyy-MM-dd HH:mm' />
        </div>
      </c:if>
      <div class="state-content__right-infor">
        <a class="state-content__right-infor" href="${msil.senderShortUrl }"> <span
          class="state-content__right-name"> <c:if test="${locale == 'en_US' }">
	                        ${msil.senderEnName }
	                    </c:if> <c:if test="${locale == 'zh_CN' }">
	                        ${msil.senderZhName }
	                    </c:if>
        </span> <img src="${msil.senderAvatars }" class="state-content__right-avator">
        </a>
      </div>
      <s:if test="#msil.smateInsideLetterType=='text'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail">${msil.content }</div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='pub'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail" des3PubId="${msil.des3PubId }"
            des3FileId='<iris:des3 code="${msil.pubFulltextId}"/>'>
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info" style=" align-items: flex-start;">
                <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
                  class="state-content__right-detail__avator" src="${msil.pubFulltextImagePath }"
                  onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                </a> 
                <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
                  <span  class="state-content__right-detail__name state-content__detail-abbreviations" style="font-weight: bold;"> <c:if
                        test="${locale == 'en_US' }">
                              ${msil.pubTitleEn }
                          </c:if> <c:if test="${locale == 'zh_CN' }">
                              ${msil.pubTitleZh }
                          </c:if>
                  </span>
                  <span class="state-content__right-detail__author">
                      ${msil.pubAuthorName }
                  </span>
                  <span class="state-content__right-detail__source">
                  <c:if test="${locale == 'en_US' }">${msil.pubBriefEn }</c:if>
                      <c:if test="${locale == 'zh_CN' }"> ${msil.pubBriefZh }</c:if>
                  </span>
                </a>
              </div>
            </div>
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='pdwhpub'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail" des3PubId="${msil.des3PubId }">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info" style=" align-items: flex-start;">
                <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
                  class="state-content__right-detail__avator" src="${msil.pubFulltextImagePath }"
                  onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                </a> 
                <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
                  <span class="state-content__right-detail__name state-content__detail-abbreviations" style="font-weight: bold;"> <c:if
                        test="${locale == 'en_US' }">
                              ${msil.pubTitleEn }
                          </c:if> <c:if test="${locale == 'zh_CN' }">
                              ${msil.pubTitleZh }
                          </c:if>
                   </span>
                   <span class="state-content__right-detail__author">
                        <c:if test="${locale == 'en_US' }">${msil.pubBriefEn }</c:if>
                        <c:if test="${locale == 'zh_CN' }"> ${msil.pubBriefZh }</c:if>
                  </span>
                  <span class="state-content__right-detail__source"></span>
                </a>
              </div>
            </div>
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='file'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail"
            des3ArchiveFileId='<iris:des3 code="${msil.archiveFileId}"/>'>
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <input type="hidden" value="${msil.fileDownloadUrl }" name="fileDownloadUrl" /> <a
                  onclick="midmsg.downloadMain(event)" style="width: 60px;"> <s:if test='#msil.fileType=="txt"  '>
                    <img class="state-content__right-detail__avator" src="${resmod}/smate-pc/img/fileicon_txt.png"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
                  </s:if> <s:elseif test='#msil.fileType=="ppt" || msil.fileType=="pptx" '>
                    <img class="state-content__right-detail__avator" src="${resmod}/smate-pc/img/fileicon_ppt.png"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
                  </s:elseif> <s:elseif test='#msil.fileType=="doc" || msil.fileType=="docx" '>
                    <img class="state-content__right-detail__avator" src="${resmod}/smate-pc/img/fileicon_doc.png"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
                  </s:elseif> <s:elseif test='#msil.fileType=="rar" || msil.fileType=="zip" '>
                    <img class="state-content__right-detail__avator" src="${resmod}/smate-pc/img/fileicon_zip.png"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
                  </s:elseif> <s:elseif test='#msil.fileType=="xls" || msil.fileType=="xlsx" '>
                    <img class="state-content__right-detail__avator" src="${resmod}/smate-pc/img/fileicon_xls.png"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
                  </s:elseif> <s:elseif test='#msil.fileType=="pdf" '>
                    <img class="state-content__right-detail__avator" src="${resmod}/smate-pc/img/fileicon_pdf.png"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
                  </s:elseif> <s:elseif test='#msil.fileType=="imgIc"  || msil.fileType=="jpg" || msil.fileType=="png" '>
                    <img class="state-content__right-detail__avator" src="${msil.imgThumbUrl}"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                  </s:elseif> <s:else>
                    <img class="state-content__right-detail__avator" src="${resmod}/smate-pc/img/fileicon_default.png"
                      onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                  </s:else>
                </a> <span class="state-content__right-detail__name state-content__detail-abbreviations"
                  onclick="midmsg.downloadMain(event)" style="width: 70%;">${msil.fileName }</span>
              </div>
            </div>
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='fund'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail" des3FundId='<iris:des3 code="${msil.fundId}"/>'>
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info" style="align-items: flex-start;">
                <div class="paper_content-container_list-avator">
                  <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 60px;"> <img
                    class="state-content__right-detail__avator" src="${msil.fundLogoUrl}"
                    onerror="this.onerror=null;this.src='${ressns}/images/default/default_fund_logo.jpg'">
                  </a>
                </div>
                <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
                   <span class="state-content__right-detail__name state-content__detail-abbreviations" style="font-weight: bold;">${msil.showFundTitle }</span>
                   <span class="state-content__right-detail__author" >${msil.showDesc }</span>
                   <span class="state-content__right-detail__source"></span>
                </a>
              </div>
            </div>
          </div>
        </div>
      </s:if>
      <s:if test="#msil.smateInsideLetterType=='fulltext'">
      <div class="state-content__right-detail__container">
        <div class="state-content__right-detail dev_detail"
             des3ArchiveFileId='<iris:des3 code="${msil.archiveFileId}"/>'>
          <div class="pub-idx_medium">
            <div class="pub-idx__base-info">
              <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
                      class="state-content__right-detail__avator" src="${msil.pubFulltextImagePath }"
                      onerror="this.onerror=null;this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
              </a> <span class="state-content__right-detail__name state-content__detail-abbreviations"
                         onclick="midmsg.downloadMain(event)" style="width: 70%;">${msil.fileName }</span>
            </div>
          </div>
        </div>
      </div>
    </s:if>
      <!-- 站内信类型  项目 -->
      <s:if test="#msil.smateInsideLetterType=='prj'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail" des3PrjId="${msil.des3PrjId }">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info" style="align-items: flex-start;">
                <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
                  class="state-content__right-detail__avator" src="${msil.prjImg}"
                  onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                </a> <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
                <span class="state-content__right-detail__name state-content__detail-abbreviations" style="font-weight: bold;"> 
                     ${msil.title } 
                </span>
                <span class="state-content__right-detail__author">
                    ${msil.authorName } 
                 </span> 
                 <span  class="state-content__right-detail__source"> 
                    ${msil.brief } 
                 </span>
                </a>
              </div>
            </div>
          </div>
        </div>
      </s:if>
      <!-- 站内信类型  资助机构 -->
      <s:if test="#msil.smateInsideLetterType=='agency'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail" des3AgencyId="${msil.des3AgencyId }">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info" style="align-items: flex-start;">
                <a onclick="midmsg.downloadMain(event)" style="width: 60px;"> <img
                  class="state-content__right-detail__avator" src="${msil.agencyLogoUrl}"
                  onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_instdefault.png'">
                </a> <a onclick="midmsg.openchatdetails('${msil.smateInsideLetterType}',event)" style="width: 70%;"> 
                <span class="state-content__right-detail__name state-content__detail-abbreviations" style="font-weight: bold;">
                    ${msil.showAgencyTitle } 
                </span> 
                <span class="state-content__right-detail__author"> 
                ${msil.showDesc }
                </span>
                 <span class="state-content__right-detail__source"></span>
                </a>
              </div>
            </div>
          </div>
        </div>
      </s:if>



      <s:if test="#msil.smateInsideLetterType=='news'">
        <div class="state-content__right-detail__container">
          <div class="state-content__right-detail dev_detail">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <a  href="${msil.newsUrl}" target="_blank">
                  <img class="state-content__right-detail__avator" src="${msil.newsImg}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_newsdefault.png'">
                </a>
                <span style="width: 70%;">
                    <span class="state-content__right-detail__name state-content__detail-abbreviations" style="width: 70%;">
                       <a class="pub-idx__main_titlea" href="${msil.newsUrl}" target="_blank"  style="word-wrap: break-word;">
                           ${msil.newsTitle }</a>
                     </span>
                    <span class="state-content__right-detail__author"> ${msil.newsBrief } </span>
                </span>
              </div>
            </div>
          </div>
        </div>
      </s:if>





    </div>
  </s:elseif>
</s:iterator>
