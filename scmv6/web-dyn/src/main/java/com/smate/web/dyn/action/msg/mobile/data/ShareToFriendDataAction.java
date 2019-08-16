package com.smate.web.dyn.action.msg.mobile.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.service.msg.OptMsgService;
import com.smate.web.dyn.service.msg.ShowMsgService;
import com.smate.web.dyn.service.share.ShareStatisticsService;

/**
 * app消息操作
 * 
 * @author LJ
 *
 *         2017年9月28日
 */
public class ShareToFriendDataAction extends ActionSupport implements Preparable, ModelDriven<MsgShowForm> {
  private static final long serialVersionUID = 8734956948650987766L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MsgShowForm form;
  @Resource
  private ShowMsgService showMsgService;
  @Resource
  private OptMsgService optMsgService;
  @Resource
  private ShareStatisticsService shareStatisticsService;
  @Value("${domainscm}")
  private String domainscm;



  /**
   * 成果分享给好友
   * 
   * @return
   */
  @Action("/dyndata/sharepub/tofriend")
  public void ajaxSendPubShareToFriend() {
    Map<String, String> result = new HashMap<String, String>();
    String status = "error";
    try {
      if (StringUtils.isNotBlank(form.getDes3ReceiverIds()) && form.getPubIds() != null) {
        optMsgService.sendPubShareToFriend(form);
        for (Long p : form.getPubIds()) {
          shareStatisticsService.addBatchShareRecord(form.getPsnId(), 1, p);
        }
        status = "success";
      }
    } catch (Exception e) {
      logger.error("成果分享异常,pubId=" + form.getPubId(), e.toString());
    }
    result.put("status", status);
    Struts2Utils.renderJson(result, "encoding: utf-8");
  }

  /**
   * 分享项目给联系人
   * 
   * @return
   */
  @Action("/dyndata/shareprj/tofriend")
  public void sharePrjToFriends() {
    Map<String, Object> result = new HashMap<String, Object>();
    String status = "error";
    try {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
      form.setPsnId(psnId);
      TheadLocalPsnId.setPsnId(psnId);
      showMsgService.sendMsg(form);
      optMsgService.sendTextMsg(form);
      if (form.getNotPermissionPsnIds() != null && form.getNotPermissionPsnIds().size() > 0) {
        status = "noPermit";
      } else {
        // 发送邮件通知
        showMsgService.sendInsideMsgEmail(form);
        status = "success";
      }
    } catch (Exception e) {
      logger.error("发送消息出错,form=" + form, e);
    }
    result.put("status", status);
    Struts2Utils.renderJson(result, "encoding: utf-8");
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
