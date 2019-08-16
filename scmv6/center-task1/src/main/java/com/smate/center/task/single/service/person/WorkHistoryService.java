package com.smate.center.task.single.service.person;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.Page;

public interface WorkHistoryService {

  /**
   * 获取指定人员的工作经历单位(带判断权限).
   * 
   * @param psnId
   * @return
   * @throws SysServiceException
   */
  public List<WorkHistory> findWorkHistoryByPsnId(Long psnId) throws SysServiceException;

  /**
   * 根据psnId获取该人的首要工作单位名称.
   * 
   * @param id
   * @return
   */
  public String getPrimaryWorkNameByPsnId(Long id);

  /**
   * 获取指定人员的工作经历单位ID.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> findWorkByPsnId() throws ServiceException;

  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isWorkHistoryExit(Long psnId) throws ServiceException;

  /**
   * @param lastPsnId
   * @return
   * @throws ServiceException
   */

  List<WorkHistory> findWorkByAutoRecommend(List<Long> psnIds) throws ServiceException;

  /**
   * 
   * @param insId
   * @param firstResult
   * @param maxResults
   * @return
   * @throws ServiceException
   */

  Page<WorkHistory> findWork(Long insId, Long psnId, Page<WorkHistory> page) throws ServiceException;
}
