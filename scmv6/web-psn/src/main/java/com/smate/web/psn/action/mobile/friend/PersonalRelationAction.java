package com.smate.web.psn.action.mobile.friend;

import java.io.Serializable;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.psn.model.friend.PersonalRelationForm;
import com.smate.web.psn.service.friend.FriendService;

/**
 * 移动端"联系"页面用Action
 *
 * @author wsn
 *
 */
@Results({@Result(name = "relationMain", location = "/WEB-INF/jsp/mobile/friend/mobile_psnrelation_main.jsp")})
public class PersonalRelationAction extends WechatBaseAction
    implements ModelDriven<PersonalRelationForm>, Preparable, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6330125447341236095L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private PersonalRelationForm form; // form对象
  @Autowired
  private FriendService friendService;

  @Override
  public PersonalRelationForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonalRelationForm();
    }
  }

  @Action("/psnweb/mobile/relationmain")
  public String relationMain() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/mobile/relationmain" + this.handleRequestParams());
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId() + ""));
      form.setFriendCount(friendService.getPsnFriendCount(form.getPsnId()));
    } catch (Exception e) {
      logger.error("进入移动端联系主页面出错，wxOpenId=" + Struts2Utils.getSession().getAttribute("wxOpenId") + "psnId="
          + SecurityUtils.getCurrentUserId());
    }
    return "relationMain";
  }

}
