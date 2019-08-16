
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>
<s:iterator value="psnInfoList" var="pi" status="st">
  <div class="main-list__item" style="display: none;">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="psn-idx_small">
          <div class="psn-idx__base-info">
            <div class="psn-idx__avatar_box">
              <div class="psn-idx__avatar_img">
                <img src="${pi.person.avatars}">
              </div>
            </div>
            <div class="psn-idx__main_box">
              <div class="psn-idx__main">
                <div class="psn-idx__main_name">${pi.person.name}</div>
                <div class="psn-idx__main_title">香港城市大学</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense">忽略</button>
      <button class="button_main button_dense button_primary-reverse">同意</button>
    </div>
  </div>
</s:iterator>
