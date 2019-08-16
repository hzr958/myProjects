package com.smate.center.open.service.profile;


public interface UserSettingsService {

  /**
   * 出初始化用户隐私设置
   * 
   * @param psnId
   */
  public void initPrivacySettingsConfig(Long psnId) throws Exception;

}
