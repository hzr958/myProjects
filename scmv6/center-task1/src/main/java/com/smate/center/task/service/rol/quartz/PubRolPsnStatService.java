package com.smate.center.task.service.rol.quartz;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;

public interface PubRolPsnStatService extends Serializable {
  /**
   * 更新单位成果指派统计数.
   * 
   * @param insId
   * @throws ServiceException
   */
  public void refreshPubRolPsnStat(Long insId) throws ServiceException;

  /**
   * 更新单位人员成果指派统计数.
   * 
   * @param insId
   * @param psnId
   */
  public void refreshPubRolPsnStat(Long insId, Long psnId);

  /**
   * 更新单位人员成果指派统计数.
   * 
   * @param insId
   * @param psnIds
   * @throws ServiceException
   */
  public void refreshPubRolPsnStat(Long insId, List<Long> psnIds) throws ServiceException;

  /**
   * 更新成果人员指派统计数.
   * 
   * @param insId
   * @param pubId
   * @throws ServiceException
   */
  public void refreshPubPsnStat(Long insId, Long pubId) throws ServiceException;

}
