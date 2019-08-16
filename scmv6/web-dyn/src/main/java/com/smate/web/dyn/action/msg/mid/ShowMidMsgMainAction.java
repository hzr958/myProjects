package com.smate.web.dyn.action.msg.mid;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.service.msg.ShowMsgService;

import ch.qos.logback.core.status.StatusUtil;

/**
 * 显示-移动端消息页面MainAction
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "mid2_msgchat_psn_list", location = "/WEB-INF/jsp/mid_msg/mid2_msgchat_psn_list.jsp"),
    @Result(name = "mid2_msgchat_search_psn_list", location = "/WEB-INF/jsp/mid_msg/mid2_msgchat_search_psn_list.jsp"),
    @Result(name = "mid2_msgcenter_list", location = "/WEB-INF/jsp/mid_msg/mid2_msgcenter_list.jsp"),
    @Result(name = "mid3_msgchat_box_msg_list", location = "/WEB-INF/jsp/mid_msg/mid3_msgchat_box_msg_list.jsp"),
    @Result(name = "mid2_msgfulltextreq_list", location = "/WEB-INF/jsp/mid_msg/mid2_msgfulltextreq_list.jsp"),
    @Result(name = "mid3_new_chat_ui", location = "/WEB-INF/jsp/mid_msg/mid3_new_chat_ui.jsp"),
    @Result(name = "mid3_chat_ui", location = "/WEB-INF/jsp/mid_msg/mid3_chat_ui.jsp")})
public class ShowMidMsgMainAction extends WechatBaseAction
    implements ModelDriven<MsgShowForm>, Preparable, Serializable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MsgShowForm form;
  @Resource
  private ShowMsgService showMsgService;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 聊天窗口新开的页面
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxshowchatui")
  public String showChatUI() {
    try {
      showMsgService.showChatUI(form);
    } catch (Exception e) {
      try {
        Struts2Utils.getResponse().sendRedirect(domainMobile + "/psnweb/mobile/msgbox?model=chatMsg");
        return null;
      } catch (IOException e1) {
      }
      return null;
    }
    return "mid3_chat_ui";
  }

  /**
   * 聊天新建消息ui ,直接显示
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxshownewcreatemsgui")
  public String showNewCreateMsgUI() {
    try {
      showMsgService.showNewChatUI(form);
    } catch (Exception e) {
      try {
        Struts2Utils.getResponse().sendRedirect(domainMobile + "/psnweb/mobile/msgbox?model=chatMsg");
        return null;
      } catch (IOException e1) {
      }
    }
    return "mid3_new_chat_ui";
  }

  /**
   * 获取消息列表
   * 
   * MsgType消息类型 7=站内信 11=全文请求 else=其他消息类型
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxgetmidmsglist")
  public String getMsg() {
    try {
      showMsgService.getMsgInfo(form);
    } catch (Exception e) {
      logger.error("移动端获取消息列表出错,psnId=" + form.getPsnId(), e);
    }
    if (MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER.equals(form.getMsgType())) {
      return "mid3_msgchat_box_msg_list";
    } else if (MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST.equals(form.getMsgType())) {
      return "mid2_msgfulltextreq_list";
    } else {
      return "mid2_msgcenter_list";
    }
  }

  /**
   * 移动端站内信聊天会话列表
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxgetchatpsnlist")
  public String getChatPsnList() {
    try {
      showMsgService.getChatPsnList(form);
    } catch (Exception e) {
      logger.error("获取站内信聊天列表出错,psnId=" + form.getPsnId(), e);
    }
    return "mid2_msgchat_psn_list";
  }

  /**
   * 移动端站内信聊天检索会话列表
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxgetsearchchatpsnlist")
  public String getSearchChatPsnList() {
    try {
      showMsgService.getSearchChatPsnList(form);
    } catch (Exception e) {
      logger.error("检索会话列表出错,psnId=" + form.getPsnId(), e);
    }
    return "mid2_msgchat_search_psn_list";
  }

  @Override
  public MsgShowForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new MsgShowForm();
    }
  }
}
