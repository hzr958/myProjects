package com.smate.center.oauth.service.profile;

import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.profile.PersonEmail;

/**
 * 个人邮件管理.
 * 
 * @author new
 * 
 */
public interface PersonEmailService {

  /**
   * 设置对应email和psnId的邮件确认状态为已确认校验
   * 
   * @author ChuanjieHou
   * @date 2017年10月18日
   * @param psnId
   * @param email
   * @return
   */
  boolean setPsnEmailVerified(Long psnId, String email) throws ServiceException;

  /**
   * 获取PersonEmail
   * 
   * @param psnId
   * @param email
   * @return
   * @throws ServiceException
   */
  PersonEmail getPersonEmailBy(Long psnId, String email) throws ServiceException;

}
