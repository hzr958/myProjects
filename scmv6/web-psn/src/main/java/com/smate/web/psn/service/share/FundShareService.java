package com.smate.web.psn.service.share;

import com.smate.web.psn.model.share.FundMainForm;

/**
 * 基金分享服务
 * 
 * @author WSN
 *
 *         2017年8月29日 下午12:00:16
 *
 */
public interface FundShareService {

  /**
   * 批量分享基金
   * 
   * @param form
   * @throws Exception
   */
  public void shareFunds(FundMainForm form) throws Exception;
}
