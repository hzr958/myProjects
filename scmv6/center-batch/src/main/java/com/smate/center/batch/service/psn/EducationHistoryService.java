package com.smate.center.batch.service.psn;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.utils.exception.SysServiceException;

/**
 * 教育经历服务接口.
 * 
 * @author lichangwen
 * 
 */
public interface EducationHistoryService {

  /**
   * 获取用户的教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<EducationHistory> findByPsnId(Long psnId) throws SysServiceException;

  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isEduHistoryExit(Long psnId) throws ServiceException;
}
