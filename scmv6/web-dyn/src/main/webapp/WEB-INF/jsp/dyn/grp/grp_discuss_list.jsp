<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

</script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="groupDynShowInfoList" var="groupDynShowInfo" status="gds">
  <div class="main-list__item global__padding_0">
    <div class="container__card">
      <div class="dynamic__box load_sample_comment discuss_box" isCanDel="${groupDynShowInfo.isCanDel }"
        permission="${groupDynShowInfo.resPremission }" resFullTextFileId="${groupDynShowInfo.resFullTextFileId }"
        resFullTextImage="${groupDynShowInfo.resFullTextImage}" dynTime="${groupDynShowInfo.dynDateForShow }"
        dynId="${groupDynShowInfo.dynId }" id="discuss_box_${groupDynShowInfo.dynId }"
        des3DynId="<iris:des3 code='${groupDynShowInfo.dynId }'/>">
        ${groupDynShowInfo.dynContent}
        <div class="dynamic-social__list">
          <div class="dynamic-social__item" onclick="GrpDiscussList.award('${groupDynShowInfo.dynId }',this)">
            <a> <!--  1  表示已经赞了 --> <c:if test="${awardstatus == 1 }">
                <s:text name='groups.base.dynamic.unlike' />
                <s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
              </c:if> <c:if test="${awardstatus == 0 }">
                <s:text name='groups.base.dynamic.like' />
                <s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
              </c:if>
            </a>
          </div>
          <s:if test="#groupDynShowInfo.resType != 'fund' && #groupDynShowInfo.resType != 'agency' && #groupDynShowInfo.resType != 'news'">
            <div class="dynamic-social__item comment_account" onclick="GrpDiscussComment.showDiscussCommentList(this)">
              <a> <s:text name='groups.base.dynamic.comment' /> <s:if
                  test="#groupDynShowInfo.commentCount!=null&&#groupDynShowInfo.commentCount!=0">(${groupDynShowInfo.commentCount })</s:if>
              </a>
            </div>
          </s:if>
          <c:if
            test="${groupDynShowInfo.resId !=0 && groupDynShowInfo.resId !=null && not empty groupDynShowInfo.resId}">
            <div class="dynamic-social__item" id="dymGrpShare" onclick="GrpDiscussList.shareGrpDynamic(this);"
              resid="${groupDynShowInfo.des3ResId}" des3resid="" nodeid="1" restype="${groupDynShowInfo.resType}"
              databaseType="" resInfoId="${groupDynShowInfo.dynId }" notEncodeId="${groupDynShowInfo.resId }">
              <a id="share_count_${groupDynShowInfo.des3DynId}"> <s:text name='groups.base.dynamic.share' /> <s:if
                  test="#groupDynShowInfo.shareCount!=null&&#groupDynShowInfo.shareCount!=0">(${shareCount })</s:if>
              </a>
            </div>
          </c:if>
          <c:if test="${groupDynShowInfo.resType =='pub'}">
            <div class="dynamic-social__item" id="dynamicCite" style="cursor: pointer;"
              onclick="Pub.showPubQuote('/pub/ajaxpubquote','<iris:des3 code='${groupDynShowInfo.resId}'/>',this)">
              <s:text name='groups.base.dynamic.cite' />
            </div>
          </c:if>
          <c:if test="${groupDynShowInfo.resType =='pdwhpub'}">
            <div class="dynamic-social__item" id="dynamicCite" des3ResId="${groupDynShowInfo.des3ResId}">
              <a title="<s:text name='groups.base.dynamic.cite' />" class="thickbox"
                onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${groupDynShowInfo.des3ResId}',this)"> <s:text
                  name='groups.base.dynamic.cite' />
              </a>
            </div>
          </c:if>
          <c:if
            test="${groupDynShowInfo.resId !=0 && groupDynShowInfo.resId !=null && not empty groupDynShowInfo.resId}">
            <s:if test="#groupDynShowInfo.resType =='pub' || #groupDynShowInfo.resType =='pdwhpub' ">
              <s:if test="#groupDynShowInfo.hasCollenciton">
                <div class="dynamic-social__item" id="dynamicCollection" collected="1"
                  onclick="GrpDiscussList.dealCollectedPub('<iris:des3 code="${groupDynShowInfo.resId}"/>','${groupDynShowInfo.resType}',this)">
                  <a> <s:text name='groups.base.dynamic.collect.cancel' />
                  </a>
                </div>
              </s:if>
              <s:else>
                <div class="dynamic-social__item" id="dynamicCollection" collected="0"
                  onclick="GrpDiscussList.dealCollectedPub('<iris:des3 code="${groupDynShowInfo.resId}"/>','${groupDynShowInfo.resType}',this)">
                  <a> <s:text name='groups.base.dynamic.save' />
                  </a>
                </div>
              </s:else>
            </s:if>
            <s:if test="#groupDynShowInfo.resType =='grpfile'">
              <div class="dynamic-social__item" onclick="Grp.grpDyncollectionGrpFile(this);">
                <a> <s:text name='groups.base.dynamic.save' />
                </a>
              </div>
            </s:if>
            <s:if test="#groupDynShowInfo.resType =='fund'">
              <div class="dynamic-social__item  collectCancel_${groupDynShowInfo.resId }" style="display: none;"
                onclick="javascript:FundRecommend.dynCollectCoperation($(this), '${groupDynShowInfo.des3ResId}', 1, '${groupDynShowInfo.resId }');">
                <a> <s:text name="groups.base.dynamic.collect.cancel" />
                </a>
              </div>
              <div class="dynamic-social__item  collect_${groupDynShowInfo.resId }"
                onclick="javascript:FundRecommend.dynCollectCoperation($(this), '${groupDynShowInfo.des3ResId}', 0, '${groupDynShowInfo.resId }');">
                <a> <s:text name="groups.base.dynamic.collect" />
                </a>
              </div>
            </s:if>
            <s:if test="#groupDynShowInfo.resType =='agency'">
              <div class="dynamic-social__item agency_cancel_interest_opt" style="display: none;"
                agencyDes3Id="${groupDynShowInfo.des3ResId}"
                onclick="javascript:PCAgency.ajaxDynamicInterest($(this), '${groupDynShowInfo.des3ResId}', 0);">
                <a class="agency_cancel_interest_word"> <s:text name="groups.base.dynamic.interest.cancel" /><a
                  class="agency_cancel_interest_num"></a>
                </a>
              </div>
              <div class="dynamic-social__item agency_interest_opt" agencyDes3Id="${groupDynShowInfo.des3ResId}"
                onclick="javascript:PCAgency.ajaxDynamicInterest($(this), '${groupDynShowInfo.des3ResId}', 1);">
                <a class="agency_interest_word"> <s:text name="groups.base.dynamic.interest" /><a
                  class="agency_interest_num"></a>
                </a>
              </div>
            </s:if>
          </c:if>
        </div>
        <div class="dynamic-cmt__sample grp_discuss_sample_comment"></div>
        <div class="dynamic-cmt grp_discuss_comment_box" style="display: none"></div>
      </div>
    </div>
  </div>
</s:iterator>