package com.smate.center.batch.service.psn;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;

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

  List<Long> isPsnPrivate(List<Long> psnIds) throws ServiceException;
}
