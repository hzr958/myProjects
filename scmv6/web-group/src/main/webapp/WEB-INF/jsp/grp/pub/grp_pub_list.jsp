<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var snsctx = "${snsctx}";
$(function(){
    //弹出框
});
</script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="page.result" var="pub" status="st">
  <div class="main-list__item pub_info_box dev_pub_list_div" isOwn=${pub.isOwn } pubId='<iris:des3 code="${pub.pubId}"/>'
    drawer-id='<iris:des3 code="${pub.pubId}"/>'>
    <div class="main-list__item_content">
      <div class="pub-idx_medium">
        <div class="pub-idx__base-info" style="align-items: flex-start;">
          <div class="pub-idx__full-text_box"
            style="display: flex; align-items: center; flex-direction: row; height: 94px;">
            <div class="main-list__item_checkbox" style="height: 24px; height: 24px;">
              <div class="input-custom-style">
                <input type="checkbox" name="">
                <i class="material-icons custom-style"></i>
              </div>
            </div>
            <div class="pub-idx__full-text_img pub_info_fulltextimage" style="position: relative; text-align: center;">
              <s:if test="#pub.hasFulltext==1">
                <!-- 是自己的成果-上传全文 -->
                <s:if test="#pub.isOwn==1">
                  <a href="${pub.fullTextUrl}">
                    <s:if test="#pub.fullTextImaUrl!=null">
                      <img src="${pub.fullTextImaUrl }"
                        onerror="this.src='/resscmwebsns/images_v5/images2016/file_img1.jpg'"
                        title='<s:text name="groups.pub.member.download.fulltext"/>' />
                      <a href="${pub.fullTextUrl}" class="new-tip_container-content"
                        title="<s:text name="groups.pub.member.download.fulltext"></s:text>">
                        <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
                        <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                      </a>
                    </s:if>
                    <s:else>
                      <img src="/resscmwebsns/images_v5/images2016/file_img1.jpg"
                        title='<s:text name="groups.pub.member.download.fulltext"/>' />
                    </s:else>
                    <a href="${pub.fullTextUrl}" class="new-tip_container-content"
                      title="<s:text name="groups.pub.member.download.fulltext"></s:text>">
                      <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
                      <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                    </a>
                  </a>
                </s:if>
                <!-- 不是自己的成果-请求全文 -->
                <s:if test="#pub.isOwn==0">
                  <s:if test="#pub.fullTextPermission==0">
                    <a href="${pub.fullTextUrl}">
                      <img src="${pub.fullTextImaUrl }"
                        onerror="this.src='/resscmwebsns/images_v5/images2016/file_img1.jpg'"
                        title='<s:text name="groups.pub.member.download.fulltext"/>' />
                      <a href="${pub.fullTextUrl}" class="new-tip_container-content"
                        title="<s:text name="groups.pub.member.download.fulltext"></s:text>">
                        <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
                        <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                      </a>
                      <a href="${pub.fullTextUrl}" class="new-tip_container-content"
                        title="<s:text name="groups.pub.member.download.fulltext"></s:text>">
                        <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
                        <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                      </a>
                    </a>
                  </s:if>
                  <s:if test="#pub.fullTextPermission!=0">
                    <img src="${pub.fullTextImaUrl }"
                      onerror="this.src='/resscmwebsns/images_v5/images2016/file_img1.jpg'"
                      title='<s:text name="groups.pub.member.request.fulltext"/>' />
                    <div class=" pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                      des3Id="${des3PubId}">
                      <div class="fileupload__box"
                        onclick="GrpPub.requestFullText('${pub.des3RecvPsnId}','${des3PubId}');"
                        title='<s:text name="groups.pub.member.request.fulltext"/>'>
                        <div class="fileupload__core initial_shown">
                          <div class="fileupload__initial">
                            <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                            <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                            <input type="file" class="fileupload__input" style='display: none;'>
                          </div>
                        </div>
                      </div>
                    </div>
                  </s:if>
                </s:if>
              </s:if>
              <s:else>
                <!-- 该成果不存在全文 -->
                <img src="/resscmwebsns/images_v5/images2016/file_img.jpg" />
                <!-- 是自己的成果-上传全文 -->
                <s:if test="#pub.isOwn==1">
                  <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1" des3Id="${des3PubId}">
                    <div class="fileupload__box" onclick="GrpPub.fileuploadBoxOpenInputClick(event)"
                      title='<s:text name="groups.pub.member.upload.fulltext"/>'>
                      <div class="fileupload__core initial_shown">
                        <div class="fileupload__initial">
                          <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                          <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                          <input type="file" class="fileupload__input" style='display: none;'>
                        </div>
                        <div class="fileupload__progress">
                          <canvas width="56" height="56"></canvas>
                          <div class="fileupload__progress_text"></div>
                        </div>
                        <div class="fileupload__saving">
                          <div class="preloader"></div>
                          <div class="fileupload__saving-text"></div>
                        </div>
                        <div class="fileupload__finish"></div>
                        <div class="fileupload__hint-text" style="display: none">添加全文</div>
                      </div>
                    </div>
                  </div>
                </s:if>
                <!-- 不是自己的成果-请求全文 -->
                <s:if test="#pub.isOwn==0">
                  <div class=" pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                    des3Id="${des3PubId}">
                    <div class="fileupload__box"
                      onclick="GrpPub.requestFullText('${pub.des3RecvPsnId}','${des3PubId}');"
                      title='<s:text name="groups.pub.member.request.fulltext"/>'>
                      <div class="fileupload__core initial_shown">
                        <div class="fileupload__initial">
                          <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                          <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                          <input type="file" class="fileupload__input" style='display: none;'>
                        </div>
                      </div>
                    </div>
                  </div>
                </s:if>
              </s:else>
              <c:if test="${pub.labeld==1 }">
                <span class="pub-idx__icon_grant-mark" style="margin: 0px;" title = "<s:text name='groups.pub.member.financialInstitutionMark'/>"></span>
              </c:if>
              <c:if test="${pub.updateMark==1}">
                <i class="demo-tip_onlineimport-icon" style="margin: 0px;"
                  title="<s:text name='groups.pub.member.updatemark1' /> "></i>
              </c:if>
              <c:if test="${pub.updateMark==2}">
                <i class="demo-tip_inlineimport-icon" style="margin: 0px;"
                  title="<s:text name='groups.pub.member.updatemark2' /> "></i>
              </c:if>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main" style="min-height: 82px;">
              <div class="pub-idx__main_title pub-idx__main_ellipsis"
                style="height: 40px; line-height: 20px; overflow: hidden;">
                <s:if test="#pub.pubIndexUrl!=null && #pub.pubIndexUrl!=''">
                  <a class="pub_info_title multipleline-ellipsis__content-box dev_pub_title" style="white-space: inherit;" href="${pub.pubIndexUrl}" target="_Blank" title="${pub.zhTitle}">
                    <s:if test="#locale=='zh_CN'">
                      <s:if test="#pub.zhTitle==null || #pub.zhTitle==''">${pub.enTitle }</s:if>
                      <s:else>${pub.zhTitle }</s:else>
                    </s:if>
                    <s:else>
                      <s:if test="#pub.enTitle==null || #pub.enTitle==''">${pub.zhTitle }</s:if>
                      <s:else>${pub.enTitle } </s:else>
                    </s:else>
                  </a>
                </s:if>
                <s:else>
                  <a class="pub_info_title multipleline-ellipsis__content-box dev_pub_title"
                    onclick="DiscussOpenDetail.openPubDetail('<iris:des3 code="${pub.pubId}"/>',event)"  style="white-space: inherit;" title="${pub.zhTitle}">
                    <s:if test="#locale=='zh_CN'">
                      <s:if test="#pub.zhTitle==null || #pub.zhTitle==''">${pub.enTitle }</s:if>
                      <s:else>${pub.zhTitle }</s:else>
                    </s:if>
                    <s:else>
                      <s:if test="#pub.enTitle==null || #pub.enTitle==''">${pub.zhTitle }</s:if>
                      <s:else>${pub.enTitle }</s:else>
                    </s:else>
                  </a>
                </s:else>
                <%--      <s:if test="#locale=='zh_CN'">
                  <s:if test="#pub.zhTitle==null || #pub.zhTitle==''">${pub.enTitle }</s:if> <s:else>${pub.zhTitle }</s:else>
           </s:if>
                
           <s:else>
                  <s:if test="#pub.enTitle==null || #pub.enTitle==''">${pub.zhTitle }</s:if><s:else>${pub.enTitle }</s:else>
           </s:else>
                
                </a> --%>
                <c:if test="${ grpCategory !=10  &&  pub.isProjectPub==1 }">
                </c:if>
              </div>
              <div class="pub-idx__main_author pub_info_authers"><span title="${pub.noneHtmlLableAuthorNames }">${pub.authors }</span></div>
              <div class="pub-idx__main_src pub_info_brif"><span title="${pub.zhBrif }">${pub.zhBrif }</span></div>
            </div>
            <ul class="idx-social__list">
              <li class="idx-social__item <s:if test='#pub.isAward==0'>is_award</s:if><s:else>award</s:else>"
                onclick="GrpPub.pubAward(this,'<iris:des3 code="${pub.pubId}"/>');">
                <s:if test="#pub.isAward==0">
                  <i class="icon-praise"></i>
                  <span class="dev_pub_award_item">
                    <s:text name='groups.pub.dynamic.like' />
                </s:if>
                <s:else>
                  <i class="icon-praise-award"></i>
                  <span class="dev_pub_award_item">
                    <s:text name='groups.pub.dynamic.unlike' />
                </s:else>
                <c:if test="${pub.awardCount!=0}">
                            (${pub.awardCount})
                            </c:if>
                </span>
              </li>
              <li class="idx-social__item  dev_pub_share_${pub.pubId}" onclick="Pub.pubCite(this);" 
                pubMainListIsAnyUser="${pub.permission}" type="list" owner="${pub.isOwn}"
                des3ResId="<iris:des3 code="${pub.pubId}"/>" pubId="${pub.pubId}" nodeid="1" resType="1" dbid="">
                <i class="icon-share"></i>
                <s:text name='groups.pub.dynamic.share' />
                <c:if test="${pub.shareCount!=0 }">   
                        (${pub.shareCount})
                        </c:if>
              </li>
              <li class="idx-social__item"
                onclick="Pub.showPubQuote('/pub/ajaxpubquote','<iris:des3 code="${pub.pubId}"/>',this)">
                <i class="icon-reference"></i>
                <s:text name='groups.pub.dynamic.cite' />
              </li>
              <!-- <li class="idx-social__item">更新引用</li> -->
              <c:if test="${pub.canEdit==1 }">
                <li class="idx-social__item"
                  onclick="GrpPub.editGrpPub('<iris:des3 code="${pub.pubId}"/>','${pub.canEdit}');">
                  <i class="new-normal_funcicon-edit"></i>
                  <s:text name='groups.pub.dynamic.edit' />
                </li>
              </c:if>
              <c:if test="${pub.canEdit==1 || role==1 || role==2}">
                <li class="idx-social__item"
                  onclick="GrpPub.deleteGrpPubConfirm('<iris:des3 code="${pub.pubId}"/>',${pub.canDelete});">
                  <i class="new-normal_funcicon-delete"></i>
                  <s:text name='groups.pub.dynamic.del' />
                </li>
              </c:if>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <c:if test="${pub.relevance == 1}">
        <div class="grp-correlation degree_2"></div>
      </c:if>
      <c:if test="${pub.relevance == 2}">
        <div class="grp-correlation degree_3"></div>
      </c:if>
      <c:if test="${pub.relevance >= 3}">
        <div class="grp-correlation degree_4"></div>
      </c:if>
      <c:if test="${pub.relevance == null || relevance == 0}">
        <div class="grp-correlation degree_1"></div>
      </c:if>
    </div>
  </div>
</s:iterator>
