package com.smate.center.task.service.sns.psn;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.PsnPrjGrant;


/**
 * 
 * 
 * @author liangguokeng
 * 
 */
public interface PsnPrjGrantService extends Serializable {
  /**
   * 
   * @param psnId
   * @param prjRole
   * @return
   * @throws ServiceException
   */
  List<PsnPrjGrant> findPsnPrjGrant(Long psnId, int prjRole) throws ServiceException;
}
