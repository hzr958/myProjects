package com.smate.center.batch.service.mail;

import java.io.UnsupportedEncodingException;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 
 * @author tsz 邮件退订url服务接口 不需要生成退订链接的 默认返回 个人设置-邮件设置 地址
 * 
 */
public interface MailUnsubscribeUrlService {

  /**
   * 生成退订url
   * 
   * @throws UnsupportedEncodingException
   * @throws ServiceException
   */
  String getUnsubscribeMailUrl(Long receivePsnId, String tempName)
      throws UnsupportedEncodingException, ServiceException;

}
