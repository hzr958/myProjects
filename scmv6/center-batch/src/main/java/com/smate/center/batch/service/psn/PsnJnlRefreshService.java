package com.smate.center.batch.service.psn;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 个人成果、文献期刊刷新记录.
 * 
 * @author WeiLong Peng
 *
 */
public interface PsnJnlRefreshService extends Serializable {

  /**
   * 标记个人成果、文献期刊统计刷新记录.
   * 
   * @param pubId
   * @param psnId
   * @param articleType
   * @param isDel
   * @throws ServiceException
   */
  void markPsnJnlRefresh(Long pubId, Long psnId, Integer articleType, Integer isDel) throws ServiceException;

}
