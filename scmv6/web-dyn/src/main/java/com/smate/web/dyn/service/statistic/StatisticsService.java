package com.smate.web.dyn.service.statistic;


import com.smate.core.base.utils.exception.DynException;


/**
 * 统计公用接口
 * 
 * @author zk
 * 
 */

public interface StatisticsService {
  public static final Integer VIEW_READ_DETAIL = 0;
  public static final Integer VIEW_AWARD_DETAIL = 1;
  public static final Integer VIEW_COMMENT_DETAIL = 2;
  public static final Integer VIEW_SHARE_DETAIL = 3;
  public static final Integer VIEW_COLLECT_DETAIL = 4;
  public static final Integer VIEW_DOWNLOAD_DETAIL = 5;

  /**
   * 查找资源的拥有者
   * 
   * @param actionKey
   * @param resNodeId
   * @param actionType
   * @return
   * @throws ServiceException
   */
  public Long findPsnId(Long actionKey, Integer resNodeId, Integer actionType) throws DynException;

  /**
   * 记录机构分享
   * 
   * @param psnId
   * @param platform
   * @param insId
   * @return
   * @throws Exception
   */
  public boolean addInsRecord(Long psnId, String platform, Long insId) throws Exception;


}
