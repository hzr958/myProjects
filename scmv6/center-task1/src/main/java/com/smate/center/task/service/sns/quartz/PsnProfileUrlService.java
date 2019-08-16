package com.smate.center.task.service.sns.quartz;

import java.io.Serializable;

import com.smate.center.task.exception.ServiceException;


/**
 * 个人主页URL接口.
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnProfileUrlService extends Serializable {

  /**
   * 查找个人主页的URL.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String findUrl(Long psnId) throws ServiceException;


}
