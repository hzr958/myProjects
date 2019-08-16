<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="dialogs__box" style="width: 420px;" dialog-id="homepage_security_setting" cover-event="hide"
  id="homepage_security_setting">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.title.profile.setting' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form style="margin-left: 24px;">
        <div class="input-radio__box_vert">
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="radio" name="homepageSecurity" id="closeHomepage" value="0"> <i
                class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.profile.set.to.invisible' />
            </div>
          </div>
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="radio" name="homepageSecurity" id="openHomepage" value="1"> <i
                class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.profile.set.to.public' />
            </div>
          </div>
        </div>
        <div class="input-radio__box_vert" style="margin-left: 32px;">
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="checkbox" name="briefConf" class="conf_input" value="<s:property value='#CNF_BRIEF'/>">
              <i class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.about' />
            </div>
          </div>
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="checkbox" name="keywordsConf" class="conf_input" value="<s:property value='#CNF_EXPERTISE'/>">
              <i class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.research.area.and.keywords' />
            </div>
          </div>
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="checkbox" name="workHistoryConf" class="conf_input" value="<s:property value='#CNF_WORK'/>">
              <i class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.work' />
            </div>
          </div>
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="checkbox" name="eduHistoryConf" class="conf_input" value="<s:property value='#CNF_EDU'/>">
              <i class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.edu' />
            </div>
          </div>
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="checkbox" name="representPubConf" class="conf_input" value="<s:property value='#CNF_PUB'/>">
              <i class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.featured.publications' />
            </div>
          </div>
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="checkbox" name="representPrjConf" class="conf_input" value="<s:property value='#CNF_PRJ'/>">
              <i class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.featured.projects' />
            </div>
          </div>
          <!-- 隐藏联系信息模块 -->
          <div class="input-radio__sxn">
            <div class="input-custom-style">
              <input type="checkbox" name="contactInfoConf" class="conf_input"
                value="<s:property value='#CNF_CONTACT'/>"> <i class="material-icons custom-style"></i>
            </div>
            <div class="input-radio__label">
              <s:text name='homepage.profile.title.contact.info' />
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="savePsnConfInfo(this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main button_primary-cancle" onclick="javascript:reloadHomePage(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</div>