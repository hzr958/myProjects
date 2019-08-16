package com.smate.web.v8pub.service.pdwh;

import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;
import com.smate.web.v8pub.service.BaseService;

public interface PdwhPubStatisticsService extends BaseService<Long, PdwhPubStatisticsPO> {
  /**
   * 更新评论统计数
   * 
   * @param pdwhPubId 基准库成果id
   * @throws ServiceException
   */
  void updateCommentStatistics(Long pdwhPubId) throws ServiceException;

  /**
   * 更新分享统计数
   * 
   * @param pdwhPubId 基准库成果id
   * @throws ServiceException
   */
  void updateShareStatistics(Long pdwhPubId) throws ServiceException;

  /**
   * 更新赞统计数
   * 
   * @param pdwhPubId 基准库成果id
   * @param awardCount 赞数
   * @throws ServiceException
   */
  void updateAwardStatistics(Long pdwhPubId, Integer awardCount) throws ServiceException;

  /**
   * 更新阅读统计数
   * 
   * @param pdwhPubId 基准库成果id
   * @throws ServiceException
   */
  void updateReadStatistics(Long pdwhPubId) throws ServiceException;

  /**
   * 更新引用统计数
   * 
   * @param pdwhPubId 基准库成果id
   * @param refCount 引用数
   * @throws ServiceException
   */
  void updateRefStatistics(Long pdwhPubId, Integer refCount) throws ServiceException;

  void updateLikeStatistics(Long pdwhPubId, PubLikeStatusEnum action) throws ServiceException;

  /**
   * 初始化统计信息
   * 
   * @param pubDetailVO
   * @throws ServiceException
   */
  void pdwhInitStatistics(PubDetailVO pubDetailVO) throws ServiceException;

  int getAwardCounts(Long pdwhPubId) throws ServiceException;

  /**
   * 获取成果的分享数
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  int getShareCounts(Long pdwhPubId) throws ServiceException;

  /**
   * 获取统计信息
   * 
   * @param pdwhPubId
   * @param pubDetailVO
   * @throws ServiceException
   */
  void getPdwhStatistics(Long pdwhPubId, PubDetailVO pubDetailVO) throws ServiceException;

}
