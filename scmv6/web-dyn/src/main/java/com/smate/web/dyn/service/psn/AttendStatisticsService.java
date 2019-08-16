package com.smate.web.dyn.service.psn;


/**
 * 关注统计服务类
 * 
 * @author zk
 * 
 */
public interface AttendStatisticsService {

  /**
   * 增加关注人员的记录信息
   * 
   * @param psnId 关注人的ID
   * @param attPsnId 被关注人的ID
   * @param action 动作（0：取消关注，1：关注）
   * @throws Exception
   */
  public void addAttRecord(Long psnId, Long attPsnId, Integer action) throws Exception;

}
