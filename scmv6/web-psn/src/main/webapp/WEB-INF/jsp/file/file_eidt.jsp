
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="dialogs__box dialogs__childbox_limited-big" dialog-id="file_eidt_dialog" flyin-direction="bottom" style="width: 480px" file_id="">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <c:if test="${locale  == 'en_US'}">
        <div class="dialogs__header_title">File Description</div>
      </c:if>
      <c:if test="${locale  == 'zh_CN'}">
        <div class="dialogs__header_title">文件描述</div>
      </c:if>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form>
        <div class="form__sxn_row">
          <div class="input__box">
            <c:if test="${locale  == 'en_US'}">
              <label class="input__title">Briefly describe the file</label>
            </c:if>
            <c:if test="${locale == 'zh_CN'}">
              <label class="input__title">文件描述</label>
            </c:if>
            <div class="input__area input__area-container" style="margin-top: 4px; padding-top: 4px;">
              <textarea class="dev_input-edit-area" maxlength="200"></textarea>
              <div class="textarea-autoresize-div"></div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <c:if test="${locale == 'en_US'}">
        <button class="button_main button_primary-reverse" onclick="VFileMain.saveMyFileDesc(this);">Confirm</button>
        <button class="button_main" onclick="VFileMain.hideFileEdit();">Cancel</button>
      </c:if>
      <c:if test="${locale  == 'zh_CN'}">
        <button class="button_main button_primary-reverse" onclick="VFileMain.saveMyFileDesc(this);">保存</button>
        <button class="button_main" onclick="VFileMain.hideFileEdit();">取消</button>
      </c:if>
    </div>
  </div>
</div>