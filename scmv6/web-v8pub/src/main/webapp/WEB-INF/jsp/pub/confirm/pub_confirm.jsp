<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var addlist = document.getElementsByClassName("pub-idx__main_add-tip");
for(var i = 0; i < addlist.length; i++ ){
    addlist[i].onclick = function(){
        if(this.innerHTML!="check_box"){
            this.innerHTML = "check_box";
        }else{
            this.innerHTML = "check_box_outline_blank";
        }
    }
}
</script>
<c:if test="${not empty pubListVO.resultList }">
  <input type="hidden" class="dev_confirm_pub_count" value="0" />
  <div class="dev_nofulltext_count">
    <c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
      <div class="container__card dev_nofulltext_div">
        <div class="promo__box" style="display: flex; flex-direction: column;">
          <div class="promo__box-header">
            <div class="promo__box-title">
              <spring:message code="pub.confrim" />
            </div>
            <div class="promo__box-check_all" onclick="Pub.pubConfirmAll();">
              <spring:message code='pub.home.more' />
            </div>
          </div>
          <div class="promo__box-container">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img" style="position: relative; width: 72px; height: 92px;">
                    <c:if test="${not empty pub.fullTextFieId }">
                      <a href="${pub.fullTextDownloadUrl }"> <img src="${pub.fullTextImgUrl}"
                         onerror="this.src='/resmod/images_v5/images2016/file_img1.jpg'"></a>
                      <a href="${pub.fullTextDownloadUrl }" class="new-tip_container-content"
                         title="<spring:message code="pub.download"/>"> <img src="/resmod/smate-pc/img/file_ upload1.png"
                         class="new-tip_container-content_tip1"> <img src="/resmod/smate-pc/img/file_ upload.png"
                          class="new-tip_container-content_tip2">
                      </a>
                    </c:if>
                    <c:if test="${empty pub.fullTextFieId }">
                      <img src="${pub.fullTextImgUrl}" style="cursor: default;"
                        onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'" />
                    </c:if>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <div class="pub-idx__main_title dev_pub_title">
                      <a href="${pub.pubIndexUrl}" target="_blank"
                        title='<c:out value="${pub.title}" escapeXml="false"/>'><c:out value="${pub.title}"
                          escapeXml="false" /></a>
                    </div>
                    <div class="pub-idx__main_author dev_pub_author" title="${pub.authorNames}">
                      <c:out value="${pub.authorNames}" escapeXml="false" />
                    </div>
                    <div class="pub-idx__main_src dev_pub_src" title="${pub.briefDesc}">
                      <c:out value="${pub.briefDesc}" escapeXml="false" />
                    </div>
                    <div class="pub-idx__main_add">
                      <i class="material-icons pub-idx__main_add-tip">check_box</i> <span
                        class="pub-idx__main_add-detail"> <c:if test="${locale=='en_US' }">Send friend invitation to my co-authors</c:if>
                        <c:if test="${locale=='zh_CN' }">邀请我的合作者成为联系人</c:if>
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="promo__box-check">
              <!-- Pub.yesConfirmPub('${pub.pubId }');Pub.importMyPubPdwh(this,'${pub.pubId}'); -->
              <div class="promo__box-comfirm" onclick="Pub.affirmConfirmPub(this,'${pub.pubId }')"><spring:message code="pub.confirm.opt" /></div>
              <div class="promo__box-refuse" onclick="Pub.ignoreConfirmPub(this,'${pub.pubId }');"><spring:message code="pub.ignore.opt" /></div>
            </div>
          </div>
          <!--<i onclick="closeNofulltext();" class="promo__close material-icons">close</i>-->
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>
<c:if test="${empty pubListVO.resultList }">
  <input type="hidden" class="dev_no_pupconfirm" value="no" />
</c:if>
