package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStat;
import com.smate.core.base.utils.model.Page;

/**
 * 成果提交统计数.
 * 
 * @author liqinghua
 * 
 */
public interface PubRolSubmissionStatService extends Serializable {

  /**
   * 更新单位成果提交统计数.
   * 
   * @param insId
   * @throws ServiceException
   */
  public void refreshPubRolSubmissionStat(Long insId) throws ServiceException;

  /**
   * 更新单位人员成果统计数.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void refreshPubRolSubmissionStat(Long insId, Long psnId) throws ServiceException;

  /**
   * 设置个人成果总数.
   * 
   * @param insId
   * @param psnId
   * @param total
   * @throws ServiceException
   */
  public void setPubTotal(Long insId, Long psnId, Long total, Long snsSubmitTotal) throws ServiceException;

  /**
   * 设置最后提交时间.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void setSubmitDate(Long insId, Long psnId) throws ServiceException;

  /**
   * 检索催交成果人员列表.
   * 
   * @param page
   * @param unitName
   * @param psnName
   * @return
   * @throws ServiceException
   */
  public Page<PubRolSubmissionStat> queryUrgencyList(Page<PubRolSubmissionStat> page, Long unitId, String psnName)
      throws ServiceException;
}
