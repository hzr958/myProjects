package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.core.base.exception.ServiceException;

/**
 * 合并成果社交化行为服务 合并sns、pwdh到sie
 * 
 * @author hd
 *
 */
public interface SieSyncPubSocialBehaveService {
  /**
   * 同步社交化数据到sie
   * 
   * @param insId
   * @throws ServiceException
   */
  public void doSync(Long insId) throws ServiceException;

  /**
   * 同步基准库分享操作数据到sie
   * 
   * @param siePubId
   * @param pdwhPubId
   * @throws ServiceException
   */
  public Integer syncPDWHPubShare(Long siePubId, Long pdwhPubId) throws ServiceException;

  /**
   * 同步SNS分享操作数据到sie
   * 
   * @param siePubId
   * @param snsPubIds
   * @throws ServiceException
   */
  public Integer syncSNSPubShare(Long siePubId, List<Long> snsPubIds) throws ServiceException;

  /**
   * 同步SNS阅读操作数据到sie
   * 
   * @param siePubId
   * @param snsPubIds
   * @return
   * @throws ServiceException
   */
  public Integer syncSNSPubRead(Long siePubId, List<Long> snsPubIds) throws ServiceException;

  /**
   * 同步基准库阅读操作数据到sie
   * 
   * @param siePubId
   * @param pdwhPubId
   * @return
   * @throws ServiceException
   */
  public Integer syncPDWHPubRead(Long siePubId, Long pdwhPubId) throws ServiceException;

  /**
   * 同步基准库赞操作数据到sie
   * 
   * @param siePubId
   * @param pdwhPubId
   * @throws ServiceException
   */
  public Integer syncPDWHPubAward(Long siePubId, Long pdwhPubId) throws ServiceException;

  /**
   * 同步SNS赞操作数据到sie
   * 
   * @param siePubId
   * @param snsPubIds
   * @throws ServiceException
   */
  public Integer syncSNSPubAward(Long siePubId, List<Long> snsPubIds) throws ServiceException;

  /**
   * 统计
   * 
   * @param siePubId
   * @param awardNum
   * @param shareNum
   * @param readNum
   * @throws ServiceException
   */
  public void doStatistics(Long siePubId, Long awardNum, Long shareNum, Long readNum) throws ServiceException;


}
