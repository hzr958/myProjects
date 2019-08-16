package com.smate.web.dyn.action.msg.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.service.msg.ShowMsgService;

/**
 * APP消息通知
 * 
 * @author LJ
 *
 *         2017年9月26日
 */
public class AppMsgShowAction extends ActionSupport implements ModelDriven<MsgShowForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = -8094549832772263208L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private MsgShowForm form;
  @Resource
  private ShowMsgService showMsgService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  private Integer pageNo = 1;

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;

  }

  /**
   * 获取站内信聊天会话列表
   * 
   * @return
   */
  @Action("/app/dynweb/showmsg/ajaxgetchatpsnlist")
  public String getChatPsnList() {
    if (form.getPsnId() != null && form.getPsnId() > 0L) {
      try {
        form.getPage().setPageNo(pageNo);
        showMsgService.getChatPsnList(form);
        status = IOSHttpStatus.OK;
        total = form.getPage().getTotalCount().intValue();
        // 防止重复加载
        if (pageNo != null && pageNo > form.getPage().getTotalPages()) {
          form.setMsgShowInfoList(null);
        }

      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("获取站内信聊天列表出错,form=" + form, e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(form.getMsgShowInfoList(), total, status);
    return null;

  }

  /**
   * app-获取消息列表
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxgetmsglist")
  public String getMsg() {
    if (form.getPsnId() != null && form.getPsnId() > 0L) {
      form.getPage().setPageNo(pageNo);
      try {
        showMsgService.getMsgInfo(form);
        status = IOSHttpStatus.OK;
        total = form.getPage().getTotalCount().intValue();
        // 防止重复加载
        if (pageNo != null && pageNo > form.getPage().getTotalPages()) {
          form.setMsgShowInfoList(null);
        }
      } catch (Exception e) {
        if (form.getMsgShowInfoList() != null) {
          status = IOSHttpStatus.OK;
        } else {
          status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        }
        logger.error("获取消息列表出错,form=" + form, e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(form.getMsgShowInfoList(), total, status);
    return null;
  }

  /**
   * app-标记消息为已读
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxsetread")
  public String setReadMsg() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0L) {
        showMsgService.setReadMsg(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
      }
    } catch (Exception e) {
      map.put("result", "error");
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("移动端-标记消息为已读出错,form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

  /**
   * app-获取未读消息数
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxcountmsg")
  public String getMsgTips() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0L) {
        form.setResultMap(new HashMap<String, String>());
        showMsgService.countUnreadMsg(form);
        Long pubConfirmCount = showMsgService.getPubConfirmCount(form.getPsnId());
        Long pubFulltextCount = showMsgService.getFulltextCount(form.getPsnId());
        Long pCount = showMsgService.getTempPsnCount(form.getPsnId());
        form.getResultMap().put("pCount", pCount.toString());
        form.getResultMap().put("pubConfirmCount", pubConfirmCount.toString());
        form.getResultMap().put("pubFulltextCount", pubFulltextCount.toString());
        map.put("unReadMsg", form.getResultMap());
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app-获取未读消息出错,form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("unReadMsg"), total, status);
    return null;
  }

  /**
   * 站内信删除聊天消息
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxdelchatmsg")
  public String ajaxdelchatMsg() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0L) {
        showMsgService.delChatMsg(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app-删除chat消息出错,form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

  /**
   * app-删除消息中心消息
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxdelmsg")
  public String ajaxdelMsg() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0L) {
        showMsgService.delMsg(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("移动端-删除消息出错,form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
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
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }
}
