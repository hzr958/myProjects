<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="fileShowInfoList" var="fsil" status="st">
  <div class="main-list__item">
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <input type="checkbox" name="pub-type"> <i class="material-icons custom-style"></i>
      </div>
    </div>
    <div class="main-list__item_content">
      <div class="file-idx_medium">
        <div class="file-idx__base-info">
          <div class="file-idx__snap_box">
            <div class="file-idx__snap_img">
              <img src="/img/file.png">
            </div>
          </div>
          <div class="file-idx__main_box">
            <div class="file-idx__main">
              <div class="file-idx__main_title">
                <a>2016世界社交网络分析报告.pdf</a>
              </div>
              <div class="file-idx__main_intro">A forum for anyone interested in Theoretical Physics. A platform
                to discuss ideas, thoughts and processes regardless of how far fetched they could be. As long as it is
                connected to Theoretical Physics.</div>
              <ul class="idx-social__list">
                <li class="idx-social__item">分享</li>
                <li class="idx-social__item">编辑</li>
                <li class="idx-social__item">删除</li>
              </ul>
            </div>
            <div class="file-idx__main_src">
              <div class="file-idx__src_time">2003-01-17</div>
              <div class="file-idx__src_uploader">上传人: 马建</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>