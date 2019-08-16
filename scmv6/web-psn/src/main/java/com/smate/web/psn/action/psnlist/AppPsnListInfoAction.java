package com.smate.web.psn.action.psnlist;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;

/**
 * app好友请求
 * 
 * @author LJ
 *
 *         2017年9月15日
 */
public class AppPsnListInfoAction extends ActionSupport implements ModelDriven<PsnListViewForm>, Preparable {

  private static final long serialVersionUID = 6761350583163156313L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private PsnListViewForm form;
  @Resource(name = "psnListViewService")
  private PsnListViewService psnListViewService;
  @Autowired
  private FriendService friendService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnListViewForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public PsnListViewForm getModel() {
    return form;
  }

  /**
   * 好友请求列表
   */
  @Actions({@Action("/app/psnweb/friendreq/ajaxlist"), @Action("/app/psnweb/friendreq")})
  public String friendReq() {
    try {
      List<Long> reqPsnIds = friendService.showFriendRequest(form);
      if (CollectionUtils.isNotEmpty(friendService.findSendFriendReqHistory(form))) {
        form.setHasSendReqList(true);
      }
      if (CollectionUtils.isNotEmpty(reqPsnIds)) {
        form.setPsnIds(reqPsnIds);
        form.setServiceType("common");
        psnListViewService.getPsnListViewInfo(form);
      }
      total = form.getPsnInfoList() == null ? 0 : form.getPsnInfoList().size();
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      logger.error("好友请求列表,出错,psnId=" + form.getPsnId(), e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }
    AppActionUtils.renderAPPReturnJson(form.getPsnInfoList(), total, status);
    return null;

  }

}
