package com.smate.center.task.single.service.person;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.Page;

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

  /**
   * 智能推荐.
   * 
   * @param startPsnId
   * @param endPsnId
   * @return
   * @throws ServiceException
   */
  List<EducationHistory> findEduByAutoRecommend(List<Long> psnIds) throws ServiceException;

  /**
   * 获取工作经历为某单位的人员列表.
   * 
   * @param insId
   * @param insName
   * @return
   * @throws ServiceException
   */

  Page<EducationHistory> findEducationHistory(Long insId, Long psnId, Page<EducationHistory> page)
      throws ServiceException;
}
