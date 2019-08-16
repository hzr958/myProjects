package com.smate.web.psn.service.statistics;



import com.smate.web.psn.exception.ServiceException;


/**
 * 
 * @author Administrator
 * 
 */
public interface AttendStatisticsService {

  /**
   * 增加关注人员的记录信息
   * 
   * @param psnId 关注人的ID
   * @param attPsnId 被关注人的ID
   * @param action 动作（0：取消关注，1：关注）
   * @throws ServiceException
   */
  public void addAttRecord(Long psnId, Long attPsnId, Integer action) throws ServiceException;
}
