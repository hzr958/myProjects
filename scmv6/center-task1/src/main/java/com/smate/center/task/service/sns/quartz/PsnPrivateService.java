package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;


/**
 * 隐私人员信息服务接口
 * 
 * @author xieyushou
 * 
 */
public interface PsnPrivateService {

  /**
   * 判断是否隐私人员
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isPsnPrivate(Long psnId) throws ServiceException;

}
