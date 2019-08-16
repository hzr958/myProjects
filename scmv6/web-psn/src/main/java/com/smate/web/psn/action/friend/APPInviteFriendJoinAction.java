package com.smate.web.psn.action.friend;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.friend.InviteJionForm;
import com.smate.web.psn.service.friend.InvitefriendJionService;

/**
 * app分享操作获取好友名字
 * 
 * @author LIJUN
 *
 */
public class APPInviteFriendJoinAction extends ActionSupport implements Preparable, ModelDriven<InviteJionForm> {

  private InviteJionForm form;
  private static final long serialVersionUID = 7986458736317744057L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InvitefriendJionService invitefriendJionService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * 群组动态-分享到我的好友-获取我的好友名字
   * 
   * @return
   */
  @Action("/app/psnweb/friend/ajaxgetmyfriendnames")
  public String ajaxGetMyFriendNames() {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    try {
      invitefriendJionService.getMyFriendNames(form);
      total = form.getPage().getTotalCount().intValue();
    } catch (Exception e) {
      logger.error("群组动态-分享到我的好友-获取我的好友名字出错", e);
    }
    AppActionUtils.renderAPPReturnJson(form.getPsnInfoList(), total, status);
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new InviteJionForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public InviteJionForm getModel() {
    return form;
  }
}
