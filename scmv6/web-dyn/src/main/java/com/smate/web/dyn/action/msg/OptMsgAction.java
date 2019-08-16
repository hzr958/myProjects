package com.smate.web.dyn.action.msg;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.web.annotation.RequestMethod;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.service.msg.OptMsgService;
import com.smate.web.dyn.service.msg.ShowMsgService;
import com.smate.web.dyn.service.share.ShareStatisticsService;

/**
 * 消息操作处理Action
 * 
 * @author zzx
 *
 */
@Results({})
public class OptMsgAction extends ActionSupport implements Preparable, ModelDriven<MsgShowForm> {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MsgShowForm form;
  @Resource
  private ShowMsgService showMsgService;
  @Resource
  private OptMsgService optMsgService;
  @Resource
  private ShareStatisticsService shareStatisticsService;

  /**
   * 发送消息
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxsendmsg")
  public String ajaxSendMsg() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      boolean flag = true;
      if ("text".equals(form.getSmateInsideLetterType())) {
        flag = false;
      }
      if (!flag) {
        if (StringUtils.isNotBlank(form.getContent())) {
          handleMsgInChat(map);
        } else {
          map.put("msg", "发送内容有误，请重新输入");
        }
      } else {
        handleMsgInChat(map);
      }
    } catch (Exception e) {
      logger.error("发送消息出错,form=" + form, e);
      map.put("status", "error");
      map.put("msg", "服务器异常，消息发送失败");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 站内信处理消息
   */
  public void handleMsgInChat(Map<String, Object> map) {
    try {
      showMsgService.buildReceivers(form);// 将传递过来的接收者邮箱进行判断，判断当前邮箱是否注册
      showMsgService.sendMsg(form);
      if (form.getNotPermissionPsnIds() != null && form.getNotPermissionPsnIds().size() > 0) {
        map.put("status", "noPermit");
        map.put("msg", getText("dyn.msg.center.notSentMsg"));
      } else {
        map.put("msgRelationId", form.getMsgRelationId());
        map.put("status", "success");
        if (form.getSmateInsideLetterType().equalsIgnoreCase("prj")) {// 分享项目时，发送邮件进行通知,发送项目分享邮件
          showMsgService.sendPrjShareEmail(form);
        } else {// 普通站内信可以直接发送站内信通知邮件，分享资源产生的站内信不需要发送
          // 发送站内信通知邮件，通知人员有站内信
          showMsgService.sendInsideMsgEmail(form);
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  /**
   * 创建会话
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxcreatemsgchat")
  public String createMsgChat() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      showMsgService.createMsgChat(form);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("创建会话出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 删除站内信会话
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxdelmsgchatrelation")
  public String delMsgChatRelation() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      showMsgService.delMsgChatRelation(form);
    } catch (Exception e) {
      logger.error("删除站内信会话出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 处理全文请求，只接收POST请求. <br>
   * 处理全文请求还需要处理全文请求记录表，这里仅仅处理了站内消息，此方法不再使用。
   * 为保证同一事务内处理，新的全文请求处理逻辑迁移至Web-pub项目PubFullTextReqAction#pubFullTextReqUpdate方法
   * 
   * @param dealStatus : 1==同意 2==忽略/拒绝 3==上传全文
   * @param msgId MsgRelation表的ID
   * @param pubId 成果id
   */
  @RequestMethod(RequestMethod.POST)
  @Deprecated
  @Action("/dynweb/showmsg/ajaxoptfulltextrequest")
  public String ajaxOptFulltextRequest() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      showMsgService.optFulltextRequest(form);
      map.put("result", "success");
      map.put("msg", iszhCN ? "操作成功" : "Operated successfully");
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "操作失败" : "Operated failed");
      logger.error("全文请求接受/忽略,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 发送全文请求站内信
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxsendfulltextrequest")
  public String ajaxSendFulltextRequest() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      if (StringUtils.isNotBlank(form.getDes3ReceiverId()) && form.getPubId() != null) {
        optMsgService.sendFulltextRequest(form);
        map.put("result", "success");
        map.put("msg", iszhCN ? "请求发送成功" : "Request sent successfully");
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "系统错误，请稍候再试" : "System error occured, please try again later");
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "请求发送失败" : "Request sent failed");
      logger.error("全文请求发送异常,pubId=" + form.getPubId(), e.toString());
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 成果分享给好友
   * 
   * @return
   */
  @Action("/dynweb/showmsg/ajaxsendpubsharetofriend")
  public String ajaxSendPubShareToFriend() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      optMsgService.dealEmailForPsn(form);
      if (StringUtils.isNotBlank(form.getDes3ReceiverIds()) && form.getPubIds() != null) {
        optMsgService.sendPubShareToFriend(form);
        // for (Long p : form.getPubIds()) {
        // form.setResId(p);
        // optMsgService.addShareRecord(form);
        // }
        map.put("result", "success");
        map.put("msg", iszhCN ? "成果分享成功" : "Shared successfully");
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "系统错误，请稍候再试" : "System error occured, please try again later");
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "成果分享失败" : "Shared failed");
      logger.error("成果分享异常,pubId=" + form.getPubId(), e);
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
}
