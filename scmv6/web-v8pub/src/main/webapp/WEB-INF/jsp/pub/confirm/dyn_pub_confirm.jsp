<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var addlist = document.getElementsByClassName("pub-idx__main_add-tip");
var randomModule = "${randomModule}";
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
<style type="text/css">
.line_overflow_hide {
  line-height: 1rem;
  height: 2.1rem;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
<c:if test="${not empty pubListVO.resultList }">
  <input id="dev_homeconfirm_isall" type="hidden" value="one" />
  <input id="" type="hidden" value="" />
  <input type="hidden" class="dev_home_opt_confirm_count" value="0" />
  <div class="dev_Achieve-Claim">
    <div class="dev_Achieve-Claim_title">
      <h1>
        <spring:message code="pub.confrim" />
      </h1>
      <a href="javascript:;" onclick="Rm.pubConfirmAll();" class="Claim_title-check">
        <button class="button_main button_link">
          <spring:message code='pub.home.more' />
        </button>
      </a>
    </div>
    <c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
      <div class="dev_Achieve-Claim_content">
        <p class="dev_Achieve-Claim_content-author">
          <spring:message code='pub.home.author' />
        </p>
        <div class="dev_Achieve-Claim_content-infor">
          <div class="multipleline-ellipsis full-request__content-title"
            style="overflow: hidden; height: 44px; max-height: 44px;">
            <div class="multipleline-ellipsis__content-box">
              <a href="${pub.pubIndexUrl}" target="_blank" title='<c:out value="${pub.title}" escapeXml="false"/>'><c:out
                  value="${pub.title}" escapeXml="false" /></a>
            </div>
          </div>
          <div class="main-list__list-item__container-name">
            <p class="p1">
              <c:out value="${pub.authorNames}" escapeXml="false" />
            </p>
            <p class="p2">
              <c:out value="${pub.briefDesc}" escapeXml="false" />
            </p>
          </div>
          <div class="pub-idx__main_add">
            <i class="material-icons pub-idx__main_add-tip">check_box</i> <span class="pub-idx__main_add-detail">
              <c:if test="${locale=='en_US' }">Send contact invitation to my co-authors</c:if> <c:if
                test="${locale=='zh_CN' }">邀请我的合作者成为联系人</c:if>
            </span>
          </div>
        </div>
        <div class="dev_Achieve-Claim_content-select">
          <a onclick="Rm.ignoreConfirmPub(this,'${pub.pubId }');" class="content-select-title content-select-style"><spring:message
              code='pub.dyn.home.ignore' /></a> <a onclick="Rm.affirmConfirmPub(this,'${pub.pubId }')"
            class="content-select-title content-selected"><spring:message code='pub.dyn.home.confirm' /></a>
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>