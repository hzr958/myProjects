package com.smate.web.dyn.service.share;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 分享统计
 * 
 * @author zk
 * 
 */
public interface ShareStatisticsService {

  /**
   * 批量添加分享记录
   * 
   * @param psnId 分享人PsnID @param jsonParameter Json参数 @throws
   */
  public void addBatchShareRecord(Long psnId, Integer resType, Long resId) throws DynException;

  /**
   * 添加分享记录
   * 
   * @param psnId
   * @param sharePsnId
   * @param actionKey
   * @param actionType
   * @throws DynException
   */
  void addShareRecord(Long psnId, Long sharePsnId, Long actionKey, Integer actionType) throws DynException;

  /**
   * 基准库批量添加分享记录
   * 
   * @param psnId
   * @param resType
   * @param resId
   * @throws DynException
   */
  void addPdwhBatchShareRecord(Long psnId, Integer resType, Long resId) throws DynException;

  /**
   * 添加分享记录
   * 
   * @param psnId
   * @param resId
   * @param resType
   * @throws DynException
   */
  void addPdwhShareRecord(Long psnId, Long resId, Integer resType) throws DynException;

  void addNewShareRecord(DynamicForm form) throws DynException;

  /**
   * 更新动态分享统计数
   * 
   * @param dynId
   */
  public void updateDynStatistics(Long dynId);

  /**
   * 更新基金分享统计数
   * 
   * @param fundId
   */
  public void updateFundShareNum(Long fundId) throws ServiceException;

  /**
   * 更新项目分享统计数
   * 
   * @param fundId
   */
  public void updatePrjShareNum(Long prjId) throws ServiceException;

  /**
   * 更新个人库成果分享统计数
   * 
   * @param form
   * @param snsId
   * @throws ServiceException
   */
  public void updateSnsShareNum(DynamicForm form, Long snsId) throws ServiceException;

  /**
   * 更新基准库成果分享统计数
   * 
   * @param form
   * @param pdwhId
   * @throws ServiceException
   */
  public void updatePdwhShareNum(DynamicForm form, Long pdwhId) throws ServiceException;
}
