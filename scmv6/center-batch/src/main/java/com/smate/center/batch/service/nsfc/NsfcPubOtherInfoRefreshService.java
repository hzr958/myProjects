package com.smate.center.batch.service.nsfc;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.nsfc.NsfcPubOtherInfoRefresh;

/**
 * 成果其他信息刷新服务接口.
 * 
 * @author xys
 *
 */
public interface NsfcPubOtherInfoRefreshService {

  /**
   * 批量获取待刷新记录.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  List<NsfcPubOtherInfoRefresh> getNsfcPubOtherInfoNeedRefreshBatch(int maxSize) throws ServiceException;

  /**
   * 更新成果其他信息.
   * 
   * @param nsfcPubOtherInfoRefresh
   * @throws ServiceException
   */
  void updateNsfcPubOtherInfo(NsfcPubOtherInfoRefresh nsfcPubOtherInfoRefresh) throws ServiceException;

  /**
   * 保存刷新记录.
   * 
   * @param nsfcPubOtherInfoRefresh
   * @throws ServiceException
   */
  void saveNsfcPubOtherInfoRefresh(NsfcPubOtherInfoRefresh nsfcPubOtherInfoRefresh) throws ServiceException;
}
