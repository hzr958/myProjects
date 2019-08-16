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
  <div class="main-list__item pub_info_box" pubId='<iris:des3 code="${pub.pubId}"/>'
    drawer-id='<iris:des3 code="${pub.pubId}"/>'>
    <div class="main-list__item_content">
      <div class="pub-idx_medium">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box" style="height: 120px;">
            <div class="pub-idx__full-text_img pub_info_fulltextimage" style="position: relative;">
              <s:if test="#pub.hasFulltext==1">
              <s:if test="#pub.fullTextPermission==0">
                <a href="${pub.fullTextUrl}"> <s:if test="#pub.fullTextImaUrl!=null">
                    <img src="${pub.fullTextImaUrl }" />
                    <a href="${pub.fullTextUrl}" class="new-tip_container-content"
                      title="<s:text name="groups.outside.pub.member.download.fulltext"></s:text>"> <img
                      src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                      src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                    </a>
                  </s:if> <s:else>
                    <img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" />
                  </s:else> <a href="${pub.fullTextUrl}" class="new-tip_container-content"
                  title="<s:text name="groups.outside.pub.member.download.fulltext"></s:text>"> <img
                    src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                    src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
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
                        onclick="loginFromReqFulltext();"
                        title='<s:text name="groups.outside.pub.member.request.fulltext"/>'>
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
              <s:else>
                <!-- 该成果不存在全文，需要请求全文 -->
                <img src="/resscmwebsns/images_v5/images2016/file_img.jpg" />
                <!-- 请求全文 start -->
                <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1" des3Id="${des3PubId}">
                  <div class="fileupload__box" onclick="loginFromReqFulltext();"
                    title='<s:text name="groups.outside.pub.member.request.fulltext"/>'>
                    <div class="fileupload__core initial_shown">
                      <div class="fileupload__initial">
                        <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                          src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                        <!-- 这个 input 框要加上,要不上传全文插件scm-pc_filedragbox.js报错-->
                        <input type="file" class="fileupload__input" style='display: none;'>
                      </div>
                    </div>
                  </div>
                </div>
              </s:else>

              <div style="width: 100%; display: flex; justify-content: center; align-items: center; height: 24px; line-height: 24px; ">
              <c:if test="${pub.labeld==1 }">
                <span class="pub-idx__icon_grant-mark" style="margin: 0px 3px;" title = "<s:text name='groups.pub.member.financialInstitutionMark'/>"></span>
              </c:if>
              <c:if test="${pub.updateMark==1}">
                <i class="demo-tip_onlineimport-icon" style="margin: 0px 3px; "
                   title="<s:text name='pub.statistics.member.updatemark1'/>"></i>
              </c:if>
              <c:if test="${pub.updateMark==2}">
                <i class="demo-tip_inlineimport-icon" style="margin: 0px 3px;"
                   title="<s:text name='pub.statistics.member.updatemark2'/>"></i>
              </c:if>
              </div>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title pub-idx__main_ellipsis" style="height: 40px;line-height: 20px;overflow: hidden;" title="${pub.zhTitle }">
                <s:if test="#pub.pubIndexUrl!=null">
                  <a class="pub_info_title multipleline-ellipsis__content-box dev_pub_title" href="${pub.pubIndexUrl}" target="_Blank">
                </s:if>
                <s:else>
                  <a class="pub_info_title" onclick="openPubDetail('<iris:des3 code="${pub.pubId}"/>',event)">
                </s:else>
                <s:if test="#locale=='zh_CN'">
                  <s:if test="#pub.zhTitle==null || #pub.zhTitle==''">${pub.enTitle }</s:if>
                  <s:else>${pub.zhTitle }</s:else>
                </s:if>
                <s:else>
                  <s:if test="#pub.enTitle==null || #pub.enTitle==''">${pub.zhTitle }</s:if>
                  <s:else>${pub.enTitle }</s:else>
                </s:else>
                </a>
              </div>
              <div class="pub-idx__main_author pub_info_authers" title="${pub.noneHtmlLableAuthorNames }">${pub.authors }</div>
              <div class="pub-idx__main_src pub_info_brif" title="${pub.zhBrif }">${pub.zhBrif }</div>
            </div>
            <ul class="idx-social__list">
            <li class="idx-social__item <s:if test='#pub.isAward==0'>is_award</s:if><s:else>award</s:else>"
                onclick="GrpPub.pubAward(this,'<iris:des3 code="${pub.pubId}"/>');">
                  <i class="icon-praise"></i>
                  <span class="dev_pub_award_item">
                    <s:text name='groups.outside.pub.like' />
                    <c:if test="${pub.awardCount!=0}">
                            (${pub.awardCount})
                            </c:if>
                </span>
              </li>
              <li class="idx-social__item  dev_pub_share_${pub.pubId}" onclick="outsideSharePub(this);"
                resid="<iris:des3 code="${pub.pubId}"/>" pubId="${pub.pubId}" des3resid="" nodeid="1" restype="1"
                dbid=""><i class="icon-share"></i> <s:text name='groups.outside.pub.share' /> <c:if
                  test="${pub.shareCount!=0 }">   
                        (${pub.shareCount})
                        </c:if></li>
              <li class="idx-social__item"
                onclick="Pub.showPubQuote('/pub/ajaxpubquote','<iris:des3 code="${pub.pubId}"/>',this)"><i
                class="icon-reference"></i> <s:text name='groups.outside.pub.cite' /></li>
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
