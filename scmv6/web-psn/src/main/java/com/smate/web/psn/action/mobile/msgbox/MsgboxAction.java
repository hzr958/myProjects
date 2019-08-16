package com.smate.web.psn.action.mobile.msgbox;

import java.io.Serializable;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.psn.model.msgbox.MsgBoxForm;

/**
 * 移动端-消息菜单Action
 * 
 * @author lhd
 *
 */
@Results({@Result(name = "mobile_msgbox_main", location = "/WEB-INF/jsp/mobile/msgbox/mobile_msgbox_main.jsp"),
    @Result(name = "mid_msg_main", location = "/WEB-INF/jsp/mobile/msgbox/mid_msg_main.jsp")})
public class MsgboxAction extends WechatBaseAction implements ModelDriven<MsgBoxForm>, Preparable, Serializable {

  private static final long serialVersionUID = -5119642682276448014L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MsgBoxForm form;

  /**
   * 移动端-消息菜单-消息中心页面
   * 
   * @return
   */
  @Action("/psnweb/mobile/msgbox")
  public String getMsgbox() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/mobile/msgbox" + this.handleRequestParams());
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId() + ""));
    } catch (Exception e) {
      logger.error("进入移动端联系主页面出错，wxOpenId=" + Struts2Utils.getSession().getAttribute("wxOpenId") + "psnId="
          + SecurityUtils.getCurrentUserId());
    }
    return "mid_msg_main";
  }

  @Override
  public MsgBoxForm getModel() {
    return form;
  }

  @Override
  public void prepare() {
    if (form == null) {
      form = new MsgBoxForm();
    }
  }
}
