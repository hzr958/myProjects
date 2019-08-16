package com.smate.center.batch.service.pub.rcmd;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.DynRecommendPsnTask;


/**
 * 动态推荐人员服务.
 * 
 * @author mjg
 * 
 */
public interface DynRecomPsnTaskService {

  /**
   * 获取指定数量的待刷新人员.
   * 
   * @param maxSize
   * @author xys
   * 
   * @return
   */
  List<DynRecommendPsnTask> getPsnsNeedRefresh(int maxSize);

  /**
   * 
   * @param psnId
   * @param dynReType
   */
  void saveDynRePsnTask(Long psnId, int dynReType);

  /**
   * 保存推荐动态人员数据.
   * 
   * @param dynRcmdPsnTask
   */
  void saveDynRePsnTask(DynRecommendPsnTask dynRcmdPsnTask) throws ServiceException;;
}
