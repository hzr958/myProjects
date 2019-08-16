package com.smate.center.batch.service.psn.register;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.register.UserGuideConfig;

public interface UserGuideService extends Serializable {

  String PUB = "pub";
  String COOPERATOR = "cooperator";
  String DISC = "disc";

  /**
   * 查找个人新手指南配置信息,如果没有,则新建一份
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  UserGuideConfig findUserGuideConfig(Long psnId) throws ServiceException;

  /**
   * 保存个人新手指南配置信息
   * 
   * @param config
   * @throws ServiceException
   */
  void saveUserGuideConfig(UserGuideConfig config) throws ServiceException;

}
