<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var ispending = "${ispending}";
$(document).ready(function(){
	if(ispending==2){
		GrpPub.showGrpPubRcmdBox();
		ispending=0;
		history.pushState("", "",  window.location.href.replace("ispending=2","ispending=0"));
	}
	<s:if test="pubInfo==null">
	$("#grp_pub_rcmd").remove();
	</s:if>
});
</script>
<s:if test="pubInfo!=null">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">项目成果认领</div>
      <button class="button_main button_link" onclick="GrpPub.showGrpPubRcmdBox();">查看全部</button>
    </div>
    <div class="main-list__list item_vert-style item_no-border">
      <div class="main-list__item">
        <div class="main-list__item_content">
          <div class="pub-idx_small">
            <div class="pub-idx__base-info">
              <div class="pub-idx__full-text_box">
                <div class="pub-idx__full-text_img">
                  <s:if test="pub.hasFulltext==1">
                    <s:if test="pub.fullTextImaUrl!=null">
                      <img src="${pubInfo.fullTextImaUrl }" />
                    </s:if>
                    <s:else>
                      <img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" />
                    </s:else>
                  </s:if>
                  <s:else>
                    <img src="/resscmwebsns/images_v5/images2016/file_img.jpg" />
                  </s:else>
                </div>
              </div>
              <div class="pub-idx__main_box">
                <div class="pub-idx__main">
                  <div class="pub-idx__main_title">
                    <a onclick="DiscussOpenDetail.openPubDetail('<iris:des3 code="${pubInfo.pubId}"/>',event)"> <s:if
                        test="#locale=='zh_CN'">
                        <s:if test="pubInfo.zhTitle==null || pubInfo.zhTitle==''">${pubInfo.enTitle }</s:if>
                        <s:else>${pubInfo.zhTitle }</s:else>
                      </s:if> <s:else>
                        <s:if test="pubInfo.enTitle==null || pubInfo.enTitle==''">${pubInfo.zhTitle }</s:if>
                        <s:else>${pubInfo.enTitle }</s:else>
                      </s:else>
                    </a>
                  </div>
                  <div class="pub-idx__main_author">${pubInfo.authors }</div>
                  <div class="pub-idx__main_src">${pubInfo.zhBrif }</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="main-list__item_actions">
          <button class="button_main button_dense" onclick="GrpPub.optionRcmdGrpPub(${pubInfo.pubId},2)">忽略</button>
          <button class="button_main button_dense button_primary-reverse"
            onclick="GrpPub.optionRcmdGrpPub(${pubInfo.pubId},1)">同意</button>
        </div>
      </div>
    </div>
  </div>
</s:if>