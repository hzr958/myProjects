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
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.share.FundMainForm;
import com.smate.web.psn.service.share.FundShareService;

/**
 * 基金分享到好友操作
 * 
 * @author WSN
 *
 *         2017年8月29日 上午11:53:32
 *
 */
public class ShareFundToPsnAction extends ActionSupport implements ModelDriven<FundMainForm>, Preparable, Serializable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FundMainForm form;
  @Autowired
  private FundShareService fundShareService;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundMainForm();
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
  @Action("/psnweb/fund/ajaxshare")
  public String shareFundToPsn() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() != null && form.getPsnId() != 0L && StringUtils.isNoneBlank(form.getDes3FundIds())
          && StringUtils.isNoneBlank(form.getDes3ReceiverIds())) {
        fundShareService.shareFunds(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("分享基金到好友出错， fundId = " + form.getFundId() + ", sharePsnId = " + SecurityUtils.getCurrentUserId()
          + ", receivePsnId = " + form.getReceiverId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

}
