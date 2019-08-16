package com.smate.center.task.service.sns.psn;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.PsnKnowWorkEdu;


/**
 * @author lcw
 * 
 */
public interface KnowPsnWorkEduService {

  /**
   * @param psnWorkEduList
   * @throws ServiceException
   */
  void matchKnowPsnWorkEdu(List<PsnKnowWorkEdu> psnWorkEduList) throws ServiceException;

  /**
   * @throws ServiceException
   */
  void deleteAll() throws ServiceException;

}
