package com.smate.center.batch.service.psn;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 人员统计信息更新服务.
 * 
 * @author lichangwen
 * 
 */
public interface PsnStatisticsUpdateService extends Serializable {

  /**
   * 初始化人员统计信息.
   * 
   * @param psnId
   * @return @
   */
  public PsnStatistics initPsnStatics(Long psnId);

  /**
   * 更新保存成果全文推荐总数_MJG_SCM-5991.
   * 
   * @param psnId
   * @param pubFullTextReSum
   */
  void updatePsnStatisByPubFull(Long psnId, Integer pubFullTextReSum);

  void updatePsnStatisticsByPrj(Long psnId) throws Exception;

  /**
   * 成果编辑、导入、删除时调用.
   * 
   * @param psnStatistics
   * @throws ServiceException
   */
  void updatePsnStatisticsByPub(Long psnId) throws ServiceException;

  /**
   * 个人加入的群组数发生变化时（新增、删除）调用.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void updatePsnStatisticsByGroup(Long psnId) throws ServiceException;

  /**
   * 个人好友数发生变化时（新增、删除）调用.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void updatePsnStatisticsByFrd(Long psnId) throws ServiceException;

  /**
   * 个人成果被赞的次数发生变化时（新增、删除）调用.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void updatePsnStatisticsByPubAward(Long psnId) throws ServiceException;

  /**
   * 设置用户待认领成果数据.
   * 
   * @param psnId
   * @param pubNum
   * @throws ServiceException
   */
  public void setPsnPendingConfirmPubNum(Long psnId, Integer pubNum) throws ServiceException;

  /**
   * 更新人员统计表群组数
   * 
   * @param psnId
   * @throws ServiceException
   */
  void refreshPsnGroupStatistics(Long psnId) throws ServiceException;

}
