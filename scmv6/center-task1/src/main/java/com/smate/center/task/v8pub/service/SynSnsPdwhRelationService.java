package com.smate.center.task.v8pub.service;

import java.util.List;

import com.smate.center.task.exception.ServiceException;

public interface SynSnsPdwhRelationService {
  /**
   * 获取未处理的pdwhIds
   * 
   * @param size
   * @return
   * @throws ServiceException
   */
  List<Long> getPubPdwhIdList(Long startId, Long endId, Integer size) throws ServiceException;

  /**
   * 通过pdwhPubId获取snsPubIds
   * 
   * @param pdwhPubId
   * @return
   * @throws ServiceException
   */
  List<Long> getSnsPubListByPdwhId(Long pdwhPubId) throws ServiceException;

  /**
   * 关联成果，将个人库的赞记录同步到基准库
   * 
   * @param pdwhPubId
   * @param snsPubId
   * @throws ServiceException
   */
  void synSnsLikeToPdwh(Long pdwhPubId, Long snsPubId) throws ServiceException;

  /**
   * 初始化基准库的赞统计数
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void initPdwhLikeStatistics(Long pdwhPubId) throws ServiceException;

  /**
   * 将关联成果基准库的赞记录同步到个人库
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void synPdwhLikeToSns(Long pdwhPubId) throws ServiceException;

  /**
   * 更新任务状态
   * 
   * @param pdwhPubId
   * @param status
   * @param errMsg
   * @throws ServiceException
   */
  void updatePubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) throws ServiceException;

  List<Long> getCommentPubPdwhIdList(Long startId, Long endId, Integer size) throws ServiceException;

  /**
   * 关联成果，将个人库的评论记录同步到基准库
   * 
   * @param pdwhPubId
   * @param snsPubId
   * @throws ServiceException
   */
  void synSnsCommentToPdwh(Long pdwhPubId, Long snsPubId) throws ServiceException;

  /**
   * 初始化基准库的评论统计数
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void initPdwhCommentStatistics(Long pdwhPubId) throws ServiceException;

  /**
   * 将关联成果基准库的评论记录同步到个人库
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void synPdwhCommentToSns(Long pdwhPubId) throws ServiceException;

  void updateCommentPubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) throws ServiceException;

  List<Long> getSharePubPdwhIdList(Long startId, Long endId, Integer size) throws ServiceException;

  /**
   * 关联成果，将个人库的分享记录同步到基准库
   * 
   * @param pdwhPubId
   * @param snsPubId
   * @throws ServiceException
   */
  void synSnsShareToPdwh(Long pdwhPubId, Long snsPubId) throws ServiceException;

  /**
   * 初始化基准库的分享统计数
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void initPdwhShareStatistics(Long pdwhPubId) throws ServiceException;

  /**
   * 将关联成果基准库的分享记录同步到个人库
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void synPdwhShareToSns(Long pdwhPubId) throws ServiceException;

  void updateSharePubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) throws ServiceException;

  public void synShare(Long pdwhPubId) throws ServiceException;

  public void synLike(Long pdwhPubId) throws ServiceException;

  public void synComment(Long pdwhPubId) throws ServiceException;
}
