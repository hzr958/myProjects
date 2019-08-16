<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

</script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="groupDynShowInfoList" var="groupDynShowInfo" status="gds">
  <div class="main-list__item global__padding_0">
    <div class="container__card">
      <div class="dynamic__box load_sample_comment discuss_box" dynTime="${groupDynShowInfo.dynDateForShow }"
        permission="${groupDynShowInfo.resPremission }" resFullTextFileId="${groupDynShowInfo.resFullTextFileId }"
        resFullTextImage="${groupDynShowInfo.resFullTextImage}" dynId="${groupDynShowInfo.dynId }"
        des3DynId="<iris:des3 code='${groupDynShowInfo.dynId }'/>" id="discuss_box_${groupDynShowInfo.dynId }">
        ${groupDynShowInfo.dynContent }
        <div class="dynamic-social__list">
          <div class="dynamic-social__item" onclick="loginFromOutside('discuss');">
            <a> <!--  1  表示已经赞了 --> <c:if test="${awardstatus ==1 }">
                                取消赞<s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
              </c:if> <c:if test="${awardstatus ==0 }">
                                赞<s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
              </c:if>
            </a>
          </div>
          <s:if test="#groupDynShowInfo.resType != 'fund' && #groupDynShowInfo.resType != 'agency'">
          <div class="dynamic-social__item comment_account"
            onclick="GrpDiscussComment.showOutsideDiscussCommentList(this)">
            <a> 评论<s:if test="#groupDynShowInfo.commentCount!=null&&#groupDynShowInfo.commentCount!=0">(${groupDynShowInfo.commentCount })</s:if>
            </a>
          </div>
          </s:if>
          <c:if
            test="${groupDynShowInfo.resId !=0 && groupDynShowInfo.resId !=null && not empty groupDynShowInfo.resId}">
            <div class="dynamic-social__item" onclick="loginFromOutside('discuss');"
              resid="${groupDynShowInfo.des3ResId}" des3resid="" nodeid="1" restype="1" dbid="">
              <a id="share_count_${groupDynShowInfo.des3DynId}">分享 <s:if
                  test="#groupDynShowInfo.shareCount!=null&&#groupDynShowInfo.shareCount!=0">(${shareCount })</s:if>
              </a>
            </div>
          </c:if>
          <c:if test="${groupDynShowInfo.resType =='pub'}">
            <div class="dynamic-social__item"
              onclick="Pub.showPubQuote('/pub/ajaxpubquote','<iris:des3 code='${groupDynShowInfo.resId}'/>',this)">引用</div>
          </c:if>
          <c:if test="${groupDynShowInfo.resType =='pdwhpub'}">
            <div class="dynamic-social__item" id="dynamicCite" des3ResId="${groupDynShowInfo.des3ResId}">
              <a title="引用" class="thickbox"
                onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${groupDynShowInfo.des3ResId}',this)">引用
              </a>
            </div>
          </c:if>
          <c:if
            test="${groupDynShowInfo.resId !=0 && groupDynShowInfo.resId !=null && not empty groupDynShowInfo.resId}">
            <s:if test="#groupDynShowInfo.resType =='pub' || #groupDynShowInfo.resType =='pdwhpub' ">
              <div class="dynamic-social__item" onclick="loginFromOutside('discuss');">
                <a>收藏</a>
              </div>
            </s:if>
            <s:if test="#groupDynShowInfo.resType =='grpfile'">
              <div class="dynamic-social__item" onclick="loginFromOutside('discuss');">
                <a>收藏</a>
              </div>
            </s:if>
            <s:if test="#groupDynShowInfo.resType =='fund'">
              <div class="dynamic-social__item  collect_${groupDynShowInfo.resId }"
                onclick="loginFromOutside('discuss');">
                <a>收藏</a>
              </div>
            </s:if>
            <s:if test="#groupDynShowInfo.resType =='agency'">
              <div class="dynamic-social__item agency_interest_opt" agencyDes3Id="${groupDynShowInfo.des3ResId}"
                onclick="loginFromOutside('discuss');">
                <a class="agency_interest_word"> 关注<a
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