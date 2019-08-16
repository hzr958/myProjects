package com.smate.web.psn.action.mobile.psnlist;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;

/**
 * 认同科技领域人员列表
 * 
 * @author LJ
 *
 *         2017年7月19日
 */
public class APPPsnListViewAction extends ActionSupport implements ModelDriven<PsnListViewForm>, Preparable {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final long serialVersionUID = 3940714191215164015L;
  private PsnListViewForm form;
  @Resource(name = "psnListViewService")
  private PsnListViewService psnListViewService;
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
   * 进入人员列表页面
   * 
   * @return
   */
  @Action("/app/psnweb/psnlistview")
  public String loadPsnList() {
    List<PsnInfo> psnInfoList = null;
    if (StringUtils.isNotBlank(form.getServiceType()) && StringUtils.isNotBlank(form.getScienceAreaId())) {
      try {
        psnListViewService.getPsnListViewInfo(form);
        if (form.getPsnInfoList() != null) {
          psnInfoList = form.getPsnInfoList();
          total = psnInfoList.size();
          status = IOSHttpStatus.OK;
        }
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("查询人员列表页面出错， psnId=" + form.getPsnId(), e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;
    }
    AppActionUtils.renderAPPReturnJson(psnInfoList, total, status);
    return null;
  }

  /**
   * 移动端获取关键词认同人员列表
   * 
   * @return
   */
  @Action("/app/psnweb/outside/mobile/identifypsn")
  public String getKeywordIdentifyPsnList() {
    try {
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      if (form.getPsnId().longValue() == SecurityUtils.getCurrentUserId().longValue()) {
        form.setIsOthers(0);
      }
      psnListViewService.getPsnListViewInfo(form);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("查看人员列表出错， 人员列表类型serviceType=" + form.getServiceType(), e);
    }
    AppActionUtils.renderAPPReturnJson(form.getPsnInfoList(), total, status);
    return null;
  }
}
