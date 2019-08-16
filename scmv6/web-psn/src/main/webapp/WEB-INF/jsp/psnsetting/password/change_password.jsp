<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="set-email__main-title" id="email_mail_tilte" des3PsnId="${des3PsnId }">
  <span class="set-email__main-heading" style="font-size: 20px; font-weight: normal;"><s:text
      name="psnset.pwdset" /></span> <span class="set-email__main-title__tip">&nbsp<s:text
      name="psnset.required.options" /></span>
</div>
<div class="set-container__right-content">
  <div class="set-email__title" style="display: flex;">
    <div style="width: 200px; text-align: right;">
      <s:text name="psnset.first.email" />
      <s:text name="psnset.colon" />
    </div>
    <span style="margin-left: 16px;">${username }</span> <span class="set-email__edit"
      onclick="PsnsettingPwd.loadPsnEmail()"><s:text name="psnset.edit" /></span>
  </div>
  <form action="/psnweb/psnsetting/ajaxsavepwd" method="POST" id="changePwdForm">
    <div>
      <div class="set-email__edit-bind_item">
        <div class="set-email__edit-bind_item-title">
          <s:text name="psnset.bind.account" />
        </div>
        <div class="set-email__edit-bind_item-infor" style="width: 150px;">
          <i class="set-email__edit-binding_qq"></i>
          <s:if test="bindQQ">
            <span title="${nickNameQQ }" style="width: 90px !important;">${nickNameQQ }</span>
          </s:if>
          <s:else>
            <span class="set-email__edit-bind_item-func_title"><s:text name="psnset.bind.no.bind" /></span>
          </s:else>
        </div>
        <div class="set-email__edit-bind_item-func">
          <s:if test="bindQQ">
            <div class="set-email__edit-bind_item-func_delete" onclick="PsnsettingPwd.unbindQQ();">
              <s:text name="psnset.bind.unbind" />
            </div>
          </s:if>
          <s:else>
            <div class="set-email__edit-bind_item-func_now" onclick="bindQQ();">
              <s:text name="psnset.bind.bind" />
            </div>
          </s:else>
        </div>
      </div>
      <div class="set-email__edit-bind_item">
        <div class="set-email__edit-bind_item-title"></div>
        <div class="set-email__edit-bind_item-infor" style="width: 150px;">
          <i class="set-email__edit-binding_WeChat"></i>
          <s:if test="bindWX">
            <span title="${nickNameWC }" style="width: 90px !important;">${nickNameWC }</span>
            <%-- <span class="set-email__edit-bind_item-func_title"><s:text name="psnset.bind.has.bind"/></span> --%>
          </s:if>
          <s:else>
            <span class="set-email__edit-bind_item-func_title"><s:text name="psnset.bind.no.bind" /></span>
          </s:else>
        </div>
        <div class="set-email__edit-bind_item-func">
          <s:if test="bindWX">
            <div class="set-email__edit-bind_item-func_delete" onclick="PsnsettingPwd.unbindWX();">
              <s:text name="psnset.bind.unbind" />
            </div>
          </s:if>
          <s:else>
            <div class="set-email__edit-bind_item-func_now" onclick="bindWeChat()">
              <s:text name="psnset.bind.bind" />
            </div>
          </s:else>
        </div>
      </div>
      <!--  绑定手机号-->
      <div class="set-email__edit-bind_item" <c:if test="${ipCheck=='2' }">style="display:none"</c:if>>
        <div class="set-email__edit-bind_item-title"></div>
        <div class="set-email__edit-bind_item-infor" style="width: 150px;">
          <i class="material-icons" style="margin: 0px 8px;color: #2882d8;">phone_iphone</i>
          <s:if test="bindMobile">
            <span title="${mobileNum }" style="width: 92px !important;">${mobileNum }</span>
            <%-- <span class="set-email__edit-bind_item-func_title"><s:text name="psnset.bind.has.bind"/></span> --%>
          </s:if>
          <s:else>
            <span class="set-email__edit-bind_item-func_title"><s:text name="psnset.bind.no.bind" /></span>
          </s:else>
        </div>
        <div class="set-email__edit-bind_item-func">
          <s:if test="bindMobile">
            <div class="set-email__edit-bind_item-func_delete" onclick="PsnsettingPwd.unBindMobileNum(this);">
              <s:text name="psnset.bind.unbind" />
            </div>
          </s:if>
          <s:else>
            <div class="set-email__edit-bind_item-func_now" onclick="PsnsettingPwd.showAddMobileNumBox(this)">
              <s:text name="psnset.bind.bind" />
            </div>
          </s:else>
        </div>
      </div>
    </div>
    <div class="set-email__resetpass-title">
      <s:text name="psnset.pwdset" />
    </div>
    <div class="set-email__forpass">
      <span class="set-email__forpass-list__box"> <span class="set-email__forpass-symbol">*</span> <span
        class="set-email__main-heading set-email__main-heading__title"><s:text name="psnset.old.pwd" /> <s:text
            name="psnset.colon" /></span>
      </span>
      <div class="set-email__forpass-list">
        <div class="targetloca" style="display: flex; flex-direction: column;">
          <input class="set-email__inputpass" maxlength="40" id="oldpassword" name="oldpassword" type="password"
            placeholder="" style="" />
        </div>
      </div>
      <span class="set-email__forpass-reminder"> <s:text name="psnset.pwd.tip.part1" /> <span
        class="set-email__forpass-reminder__click" onclick="PsnsettingPwd.forgetPwd('${username }');"><s:text
            name="psnset.pwd.tip.part3" /></span> <s:text name="psnset.pwd.tip.part2" />
      </span>
    </div>
    <div class="set-email__forpass">
      <div class="set-email__forpass-list ">
        <span class="set-email__forpass-list__box"> <span class="set-email__forpass-symbol">*</span> <span
          class="set-email__main-heading set-email__main-heading__title"><s:text name="psnset.new.pwd" /> <s:text
              name="psnset.colon" /></span>
        </span>
        <div class="targetloca" style="display: flex; flex-direction: column;">
          <input class="set-email__inputpass" maxlength="40" id="newpassword" name="newpassword" type="password"
            placeholder="" />
        </div>
      </div>
      <span class="set-email__forpass-reminder"><s:text name="psnset.pwd.tip.part4" /></span>
    </div>
    <div class="set-email__forpass">
      <div class="set-email__forpass-list">
        <span class="set-email__forpass-list__box"> <span class="set-email__forpass-symbol">*</span> <span
          class="set-email__main-heading set-email__main-heading__title"> <s:text name="psnset.renew.pwd" /> <s:text
              name="psnset.colon" />
        </span>
        </span>
        <div class="targetloca" style="display: flex; flex-direction: column;">
          <input class="set-email__inputpass" maxlength="40" type="password" id="renewpassword" name="renewpassword"
            placeholder="" />
        </div>
      </div>
    </div>
    <div class="set-email__forsave">
      <div class="set-email__savebtn" onclick="PsnsettingPwd.savePwd();" style="margin-left: 212px;">
        <s:text name="psnset.sava.pwd" />
      </div>
    </div>
  </form>
</div>
