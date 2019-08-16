package com.smate.web.psn.action.share;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.smate.web.psn.model.share.FundMainForm;
import com.smate.web.psn.service.share.FundShareService;

/**
 * APP基金分享给个人接口
 * 
 * @author LJ
 *
 *         2017年10月30日
 */
public class APPShareFundToPsnAction extends ActionSupport
    implements ModelDriven<FundMainForm>, Preparable, Serializable {

  private static final long serialVersionUID = -6337155232087631881L;
  private FundMainForm form;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  @Autowired
  private FundShareService fundShareService;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundMainForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public FundMainForm getModel() {
    return form;
  }

  /**
   * 分享基金给好友
   * 
   * @return
   */
  @Action("/app/psnweb/fund/ajaxshare")
  public String shareFundToPsn() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L && StringUtils.isNoneBlank(form.getDes3FundIds())
          && StringUtils.isNoneBlank(form.getDes3ReceiverIds())) {
        fundShareService.shareFunds(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
        map.put("result", "error");
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("分享基金到好友出错， fundId = " + form.getFundId() + ", sharePsnId = " + SecurityUtils.getCurrentUserId()
          + ", receivePsnId = " + form.getReceiverId(), e);
      map.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

}
