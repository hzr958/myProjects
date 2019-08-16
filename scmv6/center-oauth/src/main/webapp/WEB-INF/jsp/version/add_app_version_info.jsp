<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<div class="dialogs__box oldDiv js_dialognoscroll setnormalZindex" style="width: 560px; height: 680px; top: 100px; left: 651.5px;  opacity: 1;" id="edit_version_info" dialog-id="edit_version_info" cover-event="hide" visibility="hidden">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        编辑版本信息</div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="">
      
      
      
      
      
      
      <form class="form-horizontal" name="versionForm">
          <input type="hidden" name="optType" id="optType" value="${optType }">
          <input type="hidden" name="versionSize" id="versionSize" value="">
          <input type="hidden" name="downloadUrl" id="downloadUrl" value="">
          <input type="hidden" name="versionId" id="versionId" value="">
          <fieldset>
            <!-- app类型 -->
            <div class="control-group">
              <label class="control-label">APP类型</label>
              <div class="controls">
                <label class="radio">
                    <input type="radio" id="appType_ios" value="IOS" name="appType" checked="" />IOS
                </label>
                <label class="radio">
                    <input type="radio" id="appType_android" value="android" name="appType" checked=""/>android
                </label>
                <p class="help-block" id="appType_error" style="color:red;"></p>
              </div>
            </div>
            <!-- app类型 -->
            <div class="control-group">
              <label class="control-label">是否强制更新</label>
              <div class="controls">
                <label class="radio">
                    <input type="radio" id="must_update" value="1" name="mustUpdate" checked="" />是
                </label>
                <label class="radio">
                    <input type="radio" id="not_must_update" value="0" name="mustUpdate" checked=""/>否
                </label>
                <p class="help-block" id="must_update_error" style="color:red;"></p>
              </div>
            </div>
            <!-- 版本号 -->
            <div class="control-group">
              <label class="control-label" for="versionName">版本号</label>
              <div class="controls">
                <input type="text" class="input-xlarge focused" id="versionName" name="versionName" oninput="App.validateNeedInfo();">
                <p class="help-block" id="version_name_error" style="color:red;"></p>
              </div>
            </div>
            <!-- 版本code -->
            <div class="control-group">
              <label class="control-label" for="versionCode">版本code</label>
              <div class="controls">
                <input type="text" class="input-xlarge focused" id="versionCode" name="versionCode" oninput="App.validateNeedInfo();">
                <p class="help-block" id="version_code_error" style="color:red;"></p>
              </div>
            </div>
            <!-- 版本描述 -->
            <div class="control-group hidden-phone">
              <label class="control-label" for="versionDesc">版本描述</label>
              <div class="controls">
                <textarea class="cleditor" id="versionDesc" rows="3" name="versionDesc" style="margin: 0px; width: 269px; height: 106px;" oninput="App.validateNeedInfo();" maxlength="100"></textarea>
                <p class="help-block" id="version_desc_error" style="color:red;"></p>
              </div>
            </div>
            <!-- 版本code -->
            <div class="control-group">
              <label class="control-label" for="newMd5">MD5</label>
              <div class="controls">
                <input type="text" class="input-xlarge focused" id="newMd5" name="newMd5">
                <p class="help-block" id="new_md5_error" style="color:red;"></p>
              </div>
            </div>
            <!-- 上传app文件 -->
            <div class="control-group">
              <label class="control-label" for="fileInput">上传APP文件</label>
              <div class="controls">
                <div id="show_file_info_div" style="display:none;">
                <input type="text" value="" id="show_file_input" disabled="disabled" style="border: none;">
                <a class="btn btn-danger" onclick="App.removeFile();">
                    <i class="icon-trash "></i> 
                </a>
                </div>
                <input class="input-file uniform_on" id="fileInput" type="file" onchange="App.fileInputChangeEvent();"/>
                <p class="help-block" id="file_error" style="color:red;"></p>
              </div>
            </div>          
          </fieldset>
        </form>  
      
      
      
      
      
      
      
      
      
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="App.updateVersionInfo();">
        保存</button>
      <button class="button_main button_primary-cancle" onclick="App.cancelEditVersionInfo();">
        取消</button>
    </div>
  </div>
</div>