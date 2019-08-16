<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="inbox-chat__date" scm_id="chat_content_page" scm_totalcount="${page.totalCount }" scm_pageno=${page.pageNo }
  scm_pagetotalPages=${page.totalPages }></div>
<!--列表循环 start  -->
<s:iterator value="msgShowInfoList" var="msil" status="st">
  <!--发送者是自己--显示在右-->
  <s:if test="#msil.IAmSender==1">
    <div class="inbox-chat__batch-messages messages_self dev_inbox-chat-item"
      dev_create_date="<fmt:formatDate value='${msil.createDate }' pattern='yyyy-MM-dd HH:mm'/>"
      smateInsideLetterType="${msil.smateInsideLetterType }">
      <input type="hidden" value="${msil.downloadUrl}" name="downloadUrl" />
  </s:if>
  <!--否则--显示在左-->
  <s:elseif test="#msil.IAmSender==0">
    <div class="inbox-chat__batch-messages dev_inbox-chat-item"
      dev_create_date="<fmt:formatDate value='${msil.createDate }' pattern='yyyy-MM-dd HH:mm'/>"
      smateInsideLetterType="${msil.smateInsideLetterType }">
      <input type="hidden" value="${msil.downloadUrl}" name="downloadUrl" />
  </s:elseif>
  <div class="inbox-chat__avatar">
    <a href="${msil.senderShortUrl }" target="_Blank"> <img src="${msil.senderAvatars }"></a>
  </div>
  <div class="inbox-chat__content">
    <div class="inbox-chat__record">
      <!-- 站内信类型 text-->
      <s:if test="#msil.smateInsideLetterType=='text'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <c:out value="${msil.content }" escapeXml="false" />
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:if>
      <!-- 站内信类型 个人主页-->
      <s:if test="#msil.smateInsideLetterType=='psnUrl'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <c:out value="${msil.psnProfileUrl }" escapeXml="false" />
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:if>
      <!-- 站内信类型 机构主页-->
      <s:if test="#msil.smateInsideLetterType=='ins'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <c:out value="${msil.insHomeUrl }" escapeXml="false" />
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:if>
      <!-- 站内信类型 pub -->
      <s:elseif test="#msil.smateInsideLetterType=='pub'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img" style="cursor: default;position: relative;">
                    <s:if test="#msil.hasPubFulltext=='true' && #msil.pubFulltextPermit=='permit'">
                      <s:if test="#msil.pubFulltextImagePath!=null ">
                        <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}"> <img
                          src="${msil.pubFulltextImagePath }"
                          onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img1.jpg'" />
                        </a>
                      </s:if>
                      <s:else>
                        <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}"> <img
                          src="/resmod/images_v5/images2016/file_img1.jpg" />
                        </a>
                      </s:else>
                    </s:if>
                    <s:elseif test="#msil.hasPubFulltext=='true'">
                      <!-- 全文存在，但是没有权限-->
                      <img class="request-add__paper-avator__img" src="${msil.pubFulltextImagePath }"
                           onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">

                      <div class=" pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1" des3id="${msil.des3PubId}">
                        <div class="fileupload__box" onclick="Pubdetails.requestFullText(this,'${msil.des3PubOwnerPsnId}','${msil.des3PubId}');" title="<s:text name="dyn.msg.center.fulltextRequest"/>">
                          <div class="fileupload__core initial_shown">
                            <div class="fileupload__initial">
                              <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                              <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                              <input type="file" class="fileupload__input" style="display: none;">
                            </div>
                          </div>
                        </div>
                      </div>

                    </s:elseif>
                    <s:else>
                      <img src="/resmod/images_v5/images2016/file_img.jpg" />
                    </s:else>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <!-- <div class="pub-idx__main_title"> -->
                      <a href="javascript:void(0);" class="pub-idx__main_titlea" des3PubId="<iris:des3 code='${msil.pubId }'/>"
                        des3relationid="<iris:des3 code='${msil.msgRelationId }'/>"
                        onclick="MsgBase.showPubDetails(this)"> <c:if test="${locale == 'en_US' }">
                            						${msil.pubTitleEn }
                            					</c:if> <c:if test="${locale == 'zh_CN' }">
                            						${msil.pubTitleZh }
                            					</c:if>
                      </a>
                    <!-- </div> -->
                    <div class="pub-idx__main_author">${msil.pubAuthorName }</div>
                    <div class="pub-idx__main_src">
                      <c:if test="${locale == 'en_US' }">
                            					${msil.pubBriefEn }
                            				</c:if>
                      <c:if test="${locale == 'zh_CN' }">
                            					${msil.pubBriefZh }
                            				</c:if>
                    </div>
                  </div>
                  <div class="multiple-button-container">
                    <s:if test="#msil.hasPubFulltext=='true' && #msil.pubFulltextPermit=='permit' ">
                      <div class="button__box button__model_rect">
                        <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}">
                          <div
                            class="button__main button__style_flat button__size_dense button__color-outline color-display_blue ripple-effect">
                            <span> <s:text name="dyn.msg.center.downloadfulltext" />
                            </span>
                          </div>
                        </a>
                      </div>
                    </s:if>
                    <div class="button__box button__model_rect">
                      <a href="javascript:void(0);" des3PubId="<iris:des3 code='${msil.pubId }'/>"
                        des3relationid="<iris:des3 code='${msil.msgRelationId }'/>"
                        onclick="MsgBase.showPubDetails(this)">
                        <div
                          class="button__main button__style_flat button__size_dense button__color-outline color-display_blue ripple-effect">
                          <span> <s:text name="dyn.msg.center.pubDetails" />
                          </span>
                        </div>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:elseif>
      <!-- 站内信类型 pdwhpub -->
      <s:elseif test="#msil.smateInsideLetterType=='pdwhpub'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img" style="cursor: default;">
                    <s:if test="#msil.hasPubFulltext=='true' && #msil.pubFulltextPermit=='permit'">
                      <s:if test="#msil.pubFulltextImagePath!=null ">
                        <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}"> <img
                          src="${msil.pubFulltextImagePath }"
                          onerror="this.onerror=null;this.src='/resscmwebsns/images_v5/images2016/file_img1.jpg'" />
                        </a>
                      </s:if>
                      <s:else>
                        <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}"> <img
                          src="/resmod/images_v5/images2016/file_img1.jpg" />
                        </a>
                      </s:else>
                    </s:if>
                    <s:else>
                      <img src="/resmod/images_v5/images2016/file_img.jpg" />
                    </s:else>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                   <!--  <div class="pub-idx__main_title"> -->
                      <a class="pub-idx__main_titlea" href="${msil.pubShortUrl }" target="_blank"> <c:if test="${locale == 'en_US' }">
                            						${msil.pubTitleEn }
                            					</c:if> <c:if test="${locale == 'zh_CN' }">
                            						${msil.pubTitleZh }
                            					</c:if>
                      </a>
                   <!--  </div> -->
                    <div class="pub-idx__main_author">${msil.pubAuthorName }</div>
                    <div class="pub-idx__main_src">
                      <c:if test="${locale == 'en_US' }">
                            					${msil.pubBriefEn }
                            				</c:if>
                      <c:if test="${locale == 'zh_CN' }">
                            					${msil.pubBriefZh }
                            				</c:if>
                    </div>
                  </div>
                  <div class="multiple-button-container">
                    <s:if test="#msil.hasPubFulltext=='true' && #msil.pubFulltextPermit=='permit' ">
                      <div class="button__box button__model_rect">
                        <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}">
                          <div
                            class="button__main button__style_flat button__size_dense button__color-outline color-display_blue ripple-effect">
                            <span> <s:text name="dyn.msg.center.downloadfulltext" />
                            </span>
                          </div>
                        </a>
                      </div>
                    </s:if>
                    <div class="button__box button__model_rect">
                      <a href="${msil.pubShortUrl }" target="_blank">
                        <div
                          class="button__main button__style_flat button__size_dense button__color-outline color-display_blue ripple-effect">
                          <span> <s:text name="dyn.msg.center.pubDetails" />
                          </span>
                        </div>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:elseif>
      <!-- 站内信类型 file -->
      <s:elseif test="#msil.smateInsideLetterType=='file'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img">
                    <a onclick="MsgBase.downloadFile('${msil.fileDownloadUrl}',this)"> <s:if
                        test='#msil.fileType=="txt" '>
                        <img src="${resmod}/smate-pc/img/fileicon_txt.png"
                          onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
                      </s:if> <s:elseif test='#msil.fileType=="ppt" || msil.fileType=="pptx" '>
                        <img src="${resmod}/smate-pc/img/fileicon_ppt.png"
                          onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
                      </s:elseif> <s:elseif test='#msil.fileType=="doc" || msil.fileType=="docx" '>
                        <img src="${resmod}/smate-pc/img/fileicon_doc.png"
                          onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
                      </s:elseif> <s:elseif test='#msil.fileType=="rar" || msil.fileType=="zip" '>
                        <img src="${resmod}/smate-pc/img/fileicon_zip.png"
                          onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
                      </s:elseif> <s:elseif test='#msil.fileType=="xls" || msil.fileType=="xlsx" '>
                        <img src="${resmod}/smate-pc/img/fileicon_xls.png"
                          onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
                      </s:elseif> <s:elseif test='#msil.fileType=="pdf" '>
                        <img src="${resmod}/smate-pc/img/fileicon_pdf.png"
                          onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
                      </s:elseif> <s:elseif test='#msil.fileType=="imgIc"  || msil.fileType=="jpg" || msil.fileType=="png" '>
                        <img src="${msil.imgThumbUrl}" onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                      </s:elseif> <s:else>
                        <img src="${resmod}/smate-pc/img/fileicon_default.png">
                      </s:else>
                    </a>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <!-- <div class="pub-idx__main_title"> -->
                      <a class="pub-idx__main_titlea" onclick="MsgBase.downloadFile('${msil.fileDownloadUrl}',this)" style="word-wrap: break-word;">${msil.fileName }</a>
                    <!-- </div> -->
                    <div class="pub-idx__main_author"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:elseif>
      <!-- 站内信类型 news -->
      <s:elseif test="#msil.smateInsideLetterType=='news'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img">
                    <a  href="${msil.newsUrl}" target="_blank">
                      <img src="${msil.newsImg}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_newsdefault.png'">
                    </a>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <!-- <div class="pub-idx__main_title"> -->
                    <a class="pub-idx__main_titlea" href="${msil.newsUrl}" target="_blank"  style="word-wrap: break-word;">${msil.newsTitle }</a>
                    <!-- </div> -->
                    <div class="pub-idx__main_author">${msil.newsBrief }</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
                 onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:elseif>
      <!-- 站内信类型  基金 -->
      <s:elseif test="#msil.smateInsideLetterType=='fund'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="file-left file-right_avator-container mt5">
                  <!-- <div class="pub-idx__full-text_img"> -->
                    <a href="/prjweb/funddetails/show?encryptedFundId=<iris:des3 code="${msil.fundId}"/>"
                      target="_blank"> <img src="${msil.fundLogoUrl}"
                      onerror="this.onerror=null;this.src='${ressns}/images/default/default_fund_logo.jpg'" style="width: 60px; height: 60px;">
                    </a>
                  <!-- </div> -->
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                   <!--  <div class="pub-idx__main_title"> -->
                      <a  class="pub-idx__main_titlea" href="/prjweb/funddetails/show?encryptedFundId=<iris:des3 code="${msil.fundId}"/>"
                        target="_blank">${msil.showFundTitle }</a>
                    <!-- </div> -->
                    <div class="pub-idx__main_author">${msil.showDesc }</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:elseif>
      <!-- 站内信类型 全文 -->
      <s:elseif test="#msil.smateInsideLetterType=='fulltext'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img">
                    <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}"> <img
                      src="${msil.pubFulltextImagePath }"
                      onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img1.jpg'" />
                    </a>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                   <!--  <div class="pub-idx__main_title"> -->
                      <a  class="pub-idx__main_titlea"  href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}">${msil.fileName }</a>
                    <!-- </div> -->
                    <div class="pub-idx__main_author"></div>
                  </div>
                  <div class="multiple-button-container">
                    <div class="button__box button__model_rect">
                      <a href="${msil.downloadUrl == '' ? 'javascript:void(0);' : msil.downloadUrl}">
                        <div
                          class="button__main button__style_flat button__size_dense button__color-outline color-display_blue ripple-effect">
                          <span> <s:text name="dyn.msg.center.downloadfulltext" />
                          </span>
                        </div>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons" onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:elseif>
      <!-- 站内信类型  项目 -->
      <s:elseif test="#msil.smateInsideLetterType=='prj'">
        <div class="inbox-chat__record_content">
          <div class="inbox-chat__record_text">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img">
                    <a href="${msil.prjUrl}" target="_blank"> <img src="${msil.prjImg}"
                      onerror="this.onerror=null;this.src='${ressns}/images/default/default_fund_logo.jpg'">
                    </a>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <!-- <div class="pub-idx__main_title"> -->
                      <a class="pub-idx__main_titlea" href="${msil.prjUrl}" target="_blank">${msil.title }</a>
                    <!-- </div> -->
                    <div class="pub-idx__main_author">${msil.authorName }</div>
                    <div class="pub-idx__main_src">${msil.brief }</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <s:if test="#msil.IAmSender==1">
            <div class="inbox-chat__record_delete material-icons"
              onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
          </s:if>
        </div>
      </s:elseif>
      <!-- 站内信类型  资助机构 -->
      <s:elseif test="#msil.smateInsideLetterType=='agency'">
        <%@include file="msg_chat_agency_info.jsp"%>
      </s:elseif>
    </div>
  </div>
  </div>
</s:iterator>
