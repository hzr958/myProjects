package com.smate.web.psn.service.thrid.account;

import com.smate.web.psn.form.PsnSettingForm;

/**
 * 三方帐号关联
 * 
 * @author aijiangbin
 *
 */
public interface ThirdAccountRelationService {

  /**
   * 得到第三方帐号绑定的信息
   * 
   * @param form
   */
  public void getThirdAccountBindInfo(PsnSettingForm form) throws Exception;

  public void cancelQQBind(PsnSettingForm form) throws Exception;

  public void cancelWCBind(PsnSettingForm form) throws Exception;

}
