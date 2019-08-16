package com.smate.web.v8pub.service;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.handler.PubDTO;

/**
 * 成果错误信息记录接口
 * 
 * @author YJ
 *
 *         2018年8月7日
 */
public interface PubErrorMessageService {

  /**
   * 保存错误信息
   * 
   * @param entity
   * @throws ServiceException
   */
  public void save(PubDTO pub, String error) throws ServiceException;

}
