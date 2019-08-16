<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
 $(document).ready(function(){
		addFormElementsEvents();
	})	
  </script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="showInfos" var="sf">
  <div class="main-list__item"  drawer-id="${sf.des3GrpId}">
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <input type="checkbox" name="pub-type"> <i class="material-icons custom-style"></i>
      </div>
    </div>
    <div class="main-list__item_content">
      <div class="file-idx_medium">
        <div class="file-idx__base-info">
          <div class="file-idx__snap_box">
              <div class="file-idx__snap_img" style="    border-radius: 50%; width: 58px;height: 58px;">
                  <img src="${sf.image}"
                    onerror="this.src='/resmod/smate-pc/img/logo_grpdefault.png'">
              </div>
          </div>
          <div class="file-idx__main_box">
            <div class="file-idx__main" style="width: 530px;">
              <div class="file-idx__main_title pub-idx__main_title-multipleline" style="height: 40px; overflow: hidden;">
                <a href="/groupweb/grpinfo/main?des3GrpId=${sf.des3GrpId}" target="_blank">${sf.grpName}</a>
              </div>
              <div class="file-idx__main_intro multipleline-ellipsis">
                <div class="multipleline-ellipsis__content-box" style="margin-left: -2px !important;">${sf.brief}</div>
              </div>
            </div>
            <div class="file-idx__main_src">
              <div class="file-idx__src_time">
                <%--<fmt:formatDate value="${  sf.updateDate}" pattern="yyyy-MM-dd" />--%>
              </div>
              <div class="file-idx__src_uploader"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>
