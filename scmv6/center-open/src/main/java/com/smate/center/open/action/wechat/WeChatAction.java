package com.smate.center.open.action.wechat;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.open.service.wechat.MessageHandlerService;
import com.smate.center.open.utils.wechat.SignUtil;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 微信Action.
 * 
 * @author xys
 *
 */
public class WeChatAction extends ActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 5230866641163498273L;

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MessageHandlerService messageHandlerService;

  @Action("/open/wechat")
  public void weChat() {
    String signature = Struts2Utils.getParameter(WeChatConstant.SIGNATURE);
    String timestamp = Struts2Utils.getParameter(WeChatConstant.TIMESTAMP);
    String nonce = Struts2Utils.getParameter(WeChatConstant.NONCE);
    String echostr = Struts2Utils.getParameter(WeChatConstant.ECHOSTR);

    PrintWriter out;
    try {
      out = Struts2Utils.getResponse().getWriter();
      if (SignUtil.checkSignature(signature, timestamp, nonce)) {
        if (StringUtils.isBlank(echostr)) {
          messageHandlerService.handleMessage(Struts2Utils.getRequest(), true, null);
        } else {
          out.print(echostr);
        }
      }
      out.close();
    } catch (Exception e) {
      logger.error("handle message error", e);
    }
    out = null;
  }

}
