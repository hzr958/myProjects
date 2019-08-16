package com.smate.web.dyn.action.msg.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.dyn.form.msg.mobile.MobileMsgShowForm;
import com.smate.web.dyn.service.msg.ShowMsgService;

/**
 * 移动端-消息action
 * 
 * @author lhd
 *
 */
@Results({@Result(name = "mobile_msg_list", location = "/WEB-INF/jsp/mobile/msg/mobile_msg_list.jsp")})
public class MobileMsgShowAction extends WechatBaseAction implements ModelDriven<MobileMsgShowForm>, Preparable {

  private static final long serialVersionUID = 406438250727505441L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private MobileMsgShowForm form;
  @Resource
  private ShowMsgService showMsgService;

  /**
   * 移动端-获取消息列表
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxgetmsglist")
  public String getMsg() {
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0l) {
        buildWeChatData("/dynweb/mobile/ajaxgetmsglist");
        showMsgService.getMobileMsgInfo(form);
      }
    } catch (Exception e) {
      logger.error("获取消息列表出错,form=" + form, e);
    }
    return "mobile_msg_list";
  }

  /**
   * 移动端-标记消息为已读
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxsetread")
  public String setReadMsg() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0l) {
        buildWeChatData("/dynweb/mobile/ajaxsetread");
        int count = showMsgService.mobileSetReadMsg(form);
        if (count > 0) {
          map.put("result", "success");
        }
      }
    } catch (Exception e) {
      logger.error("移动端-标记消息为已读出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 移动端-获取未读消息
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxcountmsg")
  public String getMsgTips() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0l) {
        buildWeChatData("/dynweb/mobile/ajaxcountmsg");
        Long msgCount = showMsgService.mobileCountUnreadMsg(form);
        map.put("unReadMsg", msgCount);
      }
    } catch (Exception e) {
      logger.error("移动端-获取未读消息出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 移动端-删除消息
   * 
   * @return
   */
  @Action("/dynweb/mobile/ajaxdelmsg")
  public String ajaxdelMsg() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0l) {
        buildWeChatData("/dynweb/mobile/ajaxdelmsg");
        showMsgService.mobileDelMsg(form);
        map.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("移动端-删除消息出错,form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 构建微信端数据信息
   * 
   * @param currentUrl
   * @throws Exception
   */
  private void buildWeChatData(String currentUrl) {
    try {
      if (super.isWechatBrowser()) {
        if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
          this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        }
        this.handleWxJsApiTicket(this.getDomain() + currentUrl + this.handleRequestParams());
      }
    } catch (Exception e) {
      logger.error("构建微信端数据信息出错", e);
    }
  }

  @Override
  public MobileMsgShowForm getModel() {
    if (form == null) {
      form = new MobileMsgShowForm();
    }
    return form;
  }

}
