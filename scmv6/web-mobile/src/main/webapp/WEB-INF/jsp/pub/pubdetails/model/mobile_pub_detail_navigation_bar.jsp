<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- 快速分享按钮 -->
<div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
  onclick="javascript: $('#dynamicShare').hide();">
  <div class="screening_box" style="display: flex; justify-content: center;">
    <div class="screening" style="max-width: 400px" onclick="mobile.pub.quickShareDyn(1,$('#des3PubId').val());">
      <h2>
        <a href="javascript:;">立即分享到科研之友</a>
      </h2>
    </div>
  </div>
</div>

<!-- 头部导航栏 -->
<div class="new-edit_keword-header" style="overflow-x: initial;">
    <a href="javascript:void();" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');" class="fl"><i class="material-icons"
      style="font-size: 30px">keyboard_arrow_left</i></a> 
    <li class="new-edit_keword-header_title" id="pub_detail_head_li">详情</li>
      <i class="material-icons" style="position: relative;" onclick="showTab();">
              more_horiz
              <div class="sig__out-box sig__out-box__insign" style="z-index: 10001;display: none;">
                  <em class="sig__out-header"></em>
                  <div class="sig__out-body sig__out-body-container" style="height: auto; border: 1px solid #fefefe; width: 104px;">
                      <div class="sig__out-body-container_list" onclick="sharePub()">
                          <div class="sig__out-body-container_icon">
                              <i class="material-icons">share</i>
                          </div>
                          <span class="sig__out-body-container_detail">分享</span>
                      </div>
                      <div class="sig__out-body-container_line"></div>
                      <div class="sig__out-body-container_list" onclick="Pub.getfulltextUrlDownload(this)" style="display:none;" id="more_opt_download_fulltext">
                          <div class="sig__out-body-container_icon">
                              <i class="material-icons">vertical_align_bottom</i>
                          </div>
                           <span class="sig__out-body-container_detail">下载全文</span>
                      </div>
                      <div class="sig__out-body-container_list" onclick="requestPubFullText()" style="display:none;" id="more_opt_req_fulltext">
                          <div class="sig__out-body-container_icon">
                              <i class="material-icons">vertical_align_top</i>
                          </div>
                           <span class="sig__out-body-container_detail">请求全文</span>
                      </div>
                  </div>
              </div>
          </i>
  </div>