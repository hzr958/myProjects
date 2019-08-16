package com.smate.web.v8pub.service.sns;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.exception.PubException;
import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;
import com.smate.web.v8pub.service.BaseService;

import java.util.List;

public interface PubStatisticsService extends BaseService<Long, PubStatisticsPO> {
  /**
   * 更新评论统计数
   * 
   * @param pubId 成果id
   * @throws ServiceException
   */
  void updateCommentStatistics(Long pubId) throws ServiceException;

  /**
   * 更新分享统计数
   * 
   * @param pubId 成果id
   * @throws ServiceException
   */
  void updateShareStatistics(Long pubId) throws ServiceException;

  /**
   * 更新赞统计数
   * 
   * @param pubId 成果id
   * @param awardCount 赞数
   * @throws ServiceException
   */
  void updateAwardStatistics(Long pubId, Integer awardCount) throws ServiceException;

  /**
   * 更新阅读统计数
   * 
   * @param pubId 成果id
   * @throws ServiceException
   */
  void updateReadStatistics(Long pubId) throws ServiceException;

  /**
   * 更新引用统计数
   * 
   * @param pubId 成果id
   * @param refCount 引用数
   * @throws ServiceException
   */
  void updateRefStatistics(Long pubId, Integer refCount) throws ServiceException;

  void updateLikeStatistics(Long pubId, PubLikeStatusEnum action) throws ServiceException;

  /**
   * 根据基准库ID获取关联的个人库成果阅读数
   * 
   * @param pdwhPubId
   * @return
   * @throws PubException
   */
  public Long getSnsPubReadCounts(Long pdwhPubId) throws ServiceException;

  /**
   * 初始化统计信息
   * 
   * @param pubDetailVO
   * @throws ServiceException
   */
  void initStatistics(PubDetailVO pubDetailVO) throws ServiceException;

  /**
   * 是否已经收藏该成果
   */
  public Boolean isCollectedPub(Long psnId, Long pubId, PubDbEnum pubDb) throws ServiceException;

  /**
   * 获取成果的点赞数
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  int getAwardCounts(Long pubId) throws ServiceException;

  /**
   * 获取成果的分享数
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  int getShareCounts(Long pubId) throws ServiceException;

  /**
   * 统计所有成果的统计数之和
   * 
   * @param pubIdList
   * @return
   */
  Long countPubTotalCitations(List<Long> pubIdList) throws ServiceException;
}
