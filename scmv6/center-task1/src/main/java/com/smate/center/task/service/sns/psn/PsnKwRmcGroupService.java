package com.smate.center.task.service.sns.psn;

import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;


/**
 * @author lichangwen
 * 
 */
public interface PsnKwRmcGroupService {

  /**
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  int getMatchPsnKwRmcGids(Long psnId, List<String> refKws) throws ServiceException;


}
