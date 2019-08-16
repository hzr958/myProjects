package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.psn.model.PsnScoreRefresh;



/**
 * 人员信息完整度service接口.
 * 
 * @author zll
 * 
 */
public interface PsnScoreService {
  /**
   * 获取要刷新个人信息计分列表.
   */
  List<PsnScoreRefresh> getPsnScoreRefreshList(int batchSize) throws ServiceException;

  /**
   * 处理个人信息度计分刷新.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void handlerPsnScoreInit(Long psnId) throws ServiceException;

  /**
   * 根据评分项常量更新人员的信息完整度
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void updatePersonScore(Long psnId, List<Integer> updateScoreType) throws ServiceException;

  /**
   * 删除刷新记录.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void deletePsnScoreRefresh(Long psnId) throws ServiceException;


}
