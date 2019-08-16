package com.smate.web.psn.action.share.data;

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
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.share.FundMainForm;
import com.smate.web.psn.service.share.FundShareService;

/**
 * 分享基金给好友
 * 
 * @author wsn
 * @date May 29, 2019
 */
public class ShareFundToPsnDataAction extends ActionSupport
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
  @Action("/psndata/sharefund/tofriend")
  public void shareFundToFriend() {
    Map<String, Object> map = new HashMap<String, Object>();
    String status = "error";
    try {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(form.getDes3FundIds())
          && StringUtils.isNoneBlank(form.getDes3ReceiverIds())) {
        form.setPsnId(psnId);
        TheadLocalPsnId.setPsnId(psnId);
        fundShareService.shareFunds(form);
        status = "success";
      }
    } catch (Exception e) {
      logger.error("分享基金到好友出错， fundIds = {}, receiverPsnId = {}, currentPsnId={} ", form.getDes3FundIds(),
          form.getDes3ReceiverIds(), form.getDes3PsnId(), e);
    }
    map.put("status", status);
    Struts2Utils.renderJson(map, "encoding: UTF-8");
  }

}
