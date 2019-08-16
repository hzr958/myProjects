package com.smate.web.dyn.service.statistic;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.utils.exception.DynException;

/**
 * 
 * @author Administrator
 * 
 */

public interface AwardStatisticsService {

  /**
   * 添加赞操作统计记录.
   * 
   * @param psnId 赞操作的人
   * @param resNodeId 被赞的东西的节点ID
   * @param actionKey 被赞的东西的主键
   * @param actionType 被赞东西的类型 详情请看DynamicConstant.java
   * @param action 动作（0：取消赞，1：赞）
   * @throws ServiceException
   */
  public void addAwardRecord(Long psnId, Integer resNodeId, Long actionKey, Integer actionType, LikeStatusEnum action,
      Long awardPsnId) throws DynException;

  /**
   * 赞成果要同步的处理
   * 
   * @throws DynException
   */
  public void AwardPubSync(Long actionKey, Long awardPsnId, LikeStatusEnum likeStatusEnum) throws DynException;;

}
