package com.smate.center.task.service.sns.psn;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;


/**
 * 个人申请基金代码.
 * 
 * @author liangguokeng
 * 
 */
public interface PsnPrjDiscodeService extends Serializable {
  /**
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<String> findPsnDisCodeByPsnId(Long psnId) throws ServiceException;
}
