
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
    if(document.getElementsByClassName("new_edit-psninfor_body-content-email_input")){
        var onfclist = document.getElementsByClassName("new_edit-psninfor_body-content-email_input");
        for(var i = 0;i < onfclist.length; i++){
            onfclist[i].onfocus = function(){
                this.closest(".new_edit-psninfor_body-maition_content").classList.add("new_edit-psninfor_body-bottom");
            }
            onfclist[i].onblur = function(){
                this.closest(".new_edit-psninfor_body-maition_content").classList.remove("new_edit-psninfor_body-bottom");
            }
         }
    }
    $("#tel").blur(function(){
        checkPhone();
    });
  
})

    
</script>
<div class="bckground-cover" id="contactInfoBox" style="display: none;">
  <form id="telForm" method="post" action="/psnweb/contact/ajaxsave">
    <div class="new_edit-psninfor">
      <div class="new_edit-psninfor_title">
        <span class="new_edit-psninfor_title-container"><s:text name='homepage.profile.title.contact' /></span> <i
          class="material-icons"></i>
      </div>
      <div class="new_edit-psninfor_body">
        <div class="new_edit-psninfor_body-item"">
          <i class="new_edit-psninfor_body-phone"></i>
          <div class="new_edit-psninfor_body-content_tip">
            <div class="new_edit-psninfor_body-phone_content new_edit-psninfor_body-maition_content">
              <input type="text" id="tel" name="tel" value="" maxLength="50"
                class="new_edit-psninfor_body-phone_input new_edit-psninfor_body-content-email_input">
            </div>
          </div>
        </div>
        <label id="tel-error" style="display: block;margin-left: -57px;" class="error" for="tel">请填写正确的电话号码！</label>
        <div class="new_edit-psninfor_body-item">
          <i class="new_edit-psninfor_body-email"></i>
          <div class="new_edit-psninfor_body-content_tip">
            <div class="new_edit-psninfor_body-email_content new_edit-psninfor_body-maition_content">
              <input type="text" readonly="readonly" id="email" name="email" value="" maxLength="50" unselectable="on"
                class="new_edit-psninfor_body-email_content-email_input new_edit-psninfor_body-content-email_input">
              <span class="new_edit-psninfor_body-email_edit" onclick="editPsnEmailAccount(this);"><s:text
                  name="homepage.profile.contact.edit.email" /></span>
            </div>
          </div>
        </div>
      </div>
      <div class="new_edit-psninfor_footer">
        <div class="new_edit-psninfor_footer-continue positionfix-cancle" onclick="hideContactInfoBox(this);">
          <s:text name='homepage.profile.btn.cancel' />
        </div>
        <div class="new_edit-psninfor_footer-cancle positionfix-cancle" onclick="saveContactInfo(this);">
          <s:text name='homepage.profile.btn.save' />
        </div>
      </div>
    </div>
  </form>
</div>