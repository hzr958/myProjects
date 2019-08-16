<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
       $(function(){
       })
    </script>
<c:if test="${psnInfoList != null && fn:length(psnInfoList) > 0}">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <c:if test="${ locale=='zh_CN'}">
                                            按组员查看成果
              </c:if>
        <c:if test="${ locale!='zh_CN'}">
                View Members' Publications
              </c:if>
      </div>
    </div>
    <div class="friend-selection__box">
      <s:iterator value="psnInfoList" var="pi" status="st">
        <div class="friend-selection__item-2" onclick='GrpPub.selectShowGrpImpPubMember(this)'
          des3PsnId='<iris:des3 code="${pi.person.personId}"/>'>
          <div class="psn-idx_small">
            <div class="psn-idx__base-info">
              <div class="psn-idx__avatar_box">
                <div class="psn-idx__avatar_img">
                  <img src="${pi.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                </div>
              </div>
              <div class="psn-idx__main_box">
                <div class="psn-idx__main">
                  <div class="psn-idx__main_name psn-idx__main_name-length_limit" title="${pi.name}">${pi.name}</div>
                  <div class="psn-idx__main_title">
                    <s:text name='groups.pub.pubsNo' />
                    ：${pi.pubSum}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </s:iterator>
    </div>
  </div>
</c:if>
