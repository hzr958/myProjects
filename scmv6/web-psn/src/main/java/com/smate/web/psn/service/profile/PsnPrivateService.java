package com.smate.web.psn.service.profile;

import java.util.List;

import com.smate.web.psn.exception.PsnException;

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
  boolean isPsnPrivate(Long psnId) throws PsnException;

  List<Long> isPsnPrivate(List<Long> psnIds) throws PsnException;
}
