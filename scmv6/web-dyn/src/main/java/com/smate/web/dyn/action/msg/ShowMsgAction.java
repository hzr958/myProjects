package com.smate.web.dyn.action.msg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.service.msg.ShowMsgService;

/**
 * 消息显示处理Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "msg_main", location = "/WEB-INF/jsp/msg/msg_main.jsp"),
    @Result(name = "msg_center_main", location = "/WEB-INF/jsp/msg/msg_notifications.jsp"),
    @Result(name = "pm_main", location = "/WEB-INF/jsp/msg/msg_inbox.jsp"),
    @Result(name = "msg_chat_list", location = "/WEB-INF/jsp/msg/msg_chat_list.jsp"),
    @Result(name = "msg_content_list", location = "/WEB-INF/jsp/msg/msg_chat_content_list.jsp"),
    @Result(name = "msg_list", location = "/WEB-INF/jsp/msg/msg_list.jsp"),
    @Result(name = "msg_request_list", location = "/WEB-INF/jsp/msg/msg_request_list.jsp")})
public class ShowMsgAction extends ActionSupport implements Preparable, ModelDriven<MsgShowForm> {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MsgShowForm form;
  @Resource
  private ShowMsgService showMsgService;
  @Value("${domainscm}")
  private String domain;

  /**
   * 消息主页test
   * 
   * @return
   */
  @Action("/dynweb/showmsg/msgmain")
  public String msgMain() {
    Long chatPsnId = form.getChatPsnId();
    if ("chatMsg".equals(form.getModel()) && chatPsnId != null && chatPsnId > 0) {
      try {
        form.setReceiverId(chatPsnId);
        showMsgService.createMsgChat(form);
      } catch (Exception e) {
        logger.error("创建新会话失败", e);
      }
    }
    return "msg_main";
  }

  /**
   * 消息中心test
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxmsgcentermain")
  public String msgCenterMain() {
    try {
    } catch (Exception e) {
      logger.error("获取消息列表出错,form=" + form, e);
    }
    return "msg_center_main";
  }

  /**
   * 站内信test
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxpmmain")
  public String privatemsgMain() {
    try {
    } catch (Exception e) {
      logger.error("获取消息列表出错,form=" + form, e);
    }
    return "pm_main";
  }

  /**
   * 获取站内信聊天会话列表
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxgetchatpsnlist")
  public String getChatPsnList() {
    try {
      showMsgService.getChatPsnList(form);
    } catch (Exception e) {
      logger.error("获取站内信聊天列表出错,form=" + form, e);
    }
    return "msg_chat_list";
  }

  /**
   * 获取站内信聊天某个人的第一条聊天记录
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxgetchatpsnLastRecord")
  public String getChatPsnLastRecord() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (form.getPsnId() != null && form.getChatPsnId() != null) {
        showMsgService.getChatPsnLastRecord(form);
        if (form.getMsgShowInfoList() != null && form.getMsgShowInfoList().size() > 0) {
          map.put("contentNewest", form.getMsgShowInfoList().get(0).getContentNewest());
          Date createDate = form.getMsgShowInfoList().get(0).getCreateDate();
          Date nowDate = new Date();
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
          // 当天
          if (format.format(nowDate).equals(format.format(createDate))) {
            format = new SimpleDateFormat("HH:mm");
          } else {
            format = new SimpleDateFormat("MM-dd");
          }
          map.put("updateDate", format.format(form.getMsgShowInfoList().get(0).getCreateDate()));
          map.put("result", "success");
        }
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取站内信聊天列表出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取消息列表
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxgetmsglist")
  public String getMsg() {
    try {
      showMsgService.getMsgInfo(form);
    } catch (Exception e) {
      logger.error("获取消息列表出错,form=" + form, e);
    }
    if (MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER.equals(form.getMsgType())) {
      return "msg_content_list";
    } else if (MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST.equals(form.getMsgType())) {
      return "msg_request_list";
    } else {
      return "msg_list";
    }
  }

  /**
   * 获取未读消息统计数
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxcountunreadmsg")
  public String countUnreadMsg() {
    try {
      form.setResultMap(new HashMap<String, String>());
      showMsgService.countUnreadMsg(form);
      Long pubConfirmCount = showMsgService.getPubConfirmCount(form.getPsnId());
      Long pubFulltextCount = showMsgService.getFulltextCount(form.getPsnId());
      // 成果认领未读数量
      form.getResultMap().put(MsgConstants.MSG_TYPE_PUB_COMFIRM, pubConfirmCount.toString());
      // 全文认领未读数量
      form.getResultMap().put(MsgConstants.MSG_TYPE_PUB_FULLTEXT_COMFIRM, pubFulltextCount.toString());
      showMsgService.countReadMsg(form);
      showMsgService.countChatPsn(form);
    } catch (Exception e) {
      logger.error("获取未读消息统计数出错,form=" + form, e);
      form.getResultMap().put("result", "error");
    }
    form.getResultMap().put("result", "success");
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;
  }

  /**
   * SCM-13823 在好友和群组菜单上增加提醒
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxmenumsgprompt")
  public String menuMsgPrompt() {
    try {
      showMsgService.menuMsgPrompt(form);
      form.getResultMap().put("result", "success");
    } catch (Exception e) {
      logger.error("SCM-13823 在好友和群组菜单上增加提醒出错,form=" + form, e);
      form.getResultMap().put("result", "error");
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;
  }

  /**
   * 删除消息
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxdelmsg")
  public String ajaxdelMsg() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      showMsgService.delMsg(form);
    } catch (Exception e) {
      logger.error("删除消息出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 删除chat消息
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxdelchatmsg")
  public String ajaxdelchatMsg() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      showMsgService.delChatMsg(form);
    } catch (Exception e) {
      logger.error("删除chat消息出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 标记消息为已读
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxsetread")
  public String ajaxSetReadMsg() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      showMsgService.setReadMsg(form);
    } catch (Exception e) {
      logger.error("标记消息为已读出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
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

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

}
