package com.smate.center.batch.service.psn;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.exception.SysServiceException;

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
}
