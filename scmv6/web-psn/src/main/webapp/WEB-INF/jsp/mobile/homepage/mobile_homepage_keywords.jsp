<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function showIdentifyPsnList(discId){
	var isOthers = 1;
	if(!"true"==$("#outHomePage").val()){
		isOthers = 0;
	}
	document.location.href = "/psnweb/outside/mobile/identifypsn?des3PsnId="+$("#des3PsnId").val()+"&discId="+discId+"&serviceType=kwIdentific&isOthers="+isOthers;
}

function doEditKeywords(){
	document.location.href = "/psnweb/mobile/improvekeywords?isHomepageEdit=1";
}

</script>
<div class="app_psn-main_page-body_item-title">
  <span>关键词</span> <i class="app_psn-main_page-body_item-icon edit_icon_i" onclick="doEditKeywords();" style="display: none;"></i>
</div>
<s:if test="keywords != null && keywords.size()>0">
  <div class="app_psn-main_page-body_keyword-detail">
    <s:iterator value="keywords" var="keyword">
      <div class="app_psn-main_page-body_keyword-detail_item mobile_keywords_item">
        <div class="app_psn-main_page-body_keyword-detail_item-left">
          <s:if test="#keyword.identificationSum > 0">
            <div class="body_keyword-detail_item-left_num" onclick="showIdentifyPsnList('${keyword.id}');"
              id="idenSum_${keyword.id }">
          </s:if>
          <s:else>
            <div class="body_keyword-detail_item-left_num" id="idenSum_${keyword.id }">
          </s:else>
          ${keyword.identificationSum }
        </div>
        <div class="body_keyword-detail_item-left_name overflow_hidden">${keyword.keyWords }</div>
        <div class="body_keyword-detail_item-left_agree" id="area_${keyword.id }"
          style="${(isMyself || keyword.hasIdentified) ? 'display:none;' : ''}"
          onclick="identifyKeyword('${keyword.id}')">
          <i class="normal-global_icon normal-global_identification-icon"></i>
        </div>
        <%--  <div class="kw-stick__endorse " id="area_${keyword.id }" style="${(isMyself || keyword.hasIdentified) ? 'display:none;' : ''}" onclick="identifyKeyword('${keyword.id}')">
                  <i class="normal-global_icon  normal-global_identification-icon" title="认同" style="color: #333;">认同</i>
              </div> --%>
      </div>
      <s:if test="#keyword.identificationSum > 0">
        <div class="body_keyword-detail_item-right_avator-box dev_avatar_div_${keyword.id }"
          onclick="showIdentifyPsnList('${keyword.id}');">
      </s:if>
      <s:else>
        <div class="body_keyword-detail_item-right_avator-box dev_avatar_div_${keyword.id }">
      </s:else>
      <s:if test="#keyword.identifyAvatars != null && #keyword.identifyAvatars.size() > 0">
        <s:iterator value="#keyword.identifyAvatars" var="avatar" status="sta">
          <img src="${avatar }" onerror="/resmod/smate-pc/img/logo_psndefault.png"
            class="dev_avatar_sum body_keyword-detail_item-right_avator${sta.index + 1 }">
        </s:iterator>
      </s:if>
  </div>
  </div>
  </s:iterator>
  </div>
</s:if>