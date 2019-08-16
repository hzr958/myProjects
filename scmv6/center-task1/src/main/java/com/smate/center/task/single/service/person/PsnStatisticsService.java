package com.smate.center.task.single.service.person;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.PsnStatisticsRefresh;

/**
 * 人员信息统计，该统计会同步sie.
 * 
 * @author lichangwen
 * 
 */
public interface PsnStatisticsService extends Serializable {
  /**
   * @param psnId
   * @return @
   */
  PsnStatistics getPsnStatistics(Long psnId);

  /**
   * 获取用户待认领成果数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Integer getPsnPendingConfirmPubNum(Long psnId);

  /**
   * 获取用户的成果全文推荐总数_MJG_SCM-5991.
   * 
   * @param psnId
   * @return
   */
  Integer getPubFulltextReSum(Long psnId);

  /**
   * 更新用户待认领成果数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public void updateStatisticsPcfPub(Long psnId, int pendingCfmPubNum) throws ServiceException;

  public void savePsnStatistics(PsnStatistics p);

  /**
   * 更新个人 统计数.
   */
  public void updatePsnStatistics(Long psnId) throws Exception;

  /**
   * 更新刷新表记录.
   * 
   * @throws Exception
   */
  public void ToRefresh() throws Exception;

  public void updatePsnStatisticsRefresh(PsnStatisticsRefresh psr) throws Exception;

  /**
   * 获取待更新的记录.
   * 
   * @throws Exception
   */
  public List<PsnStatisticsRefresh> getToBeRefresh(Long startId, int size) throws Exception;
}
