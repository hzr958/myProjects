package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.PrjRelatedPubRefresh;

/**
 * 项目相关成果刷新SERVICE接口.
 * 
 * @author xys
 * 
 */
public interface PrjRelatedPubRefreshService extends Serializable {

  /**
   * 保存项目相关成果待刷新记录.
   * 
   * @param prjId
   * @param pubId
   * @param psnId
   * @param refreshSource
   * 
   * @throws ServiceException
   */
  void savePrjRelatedPubNeedRefresh(Long prjId, Long pubId, Long psnId, Integer refreshSource) throws ServiceException;

  /**
   * 保存项目相关成果刷新记录.
   * 
   * @param prjRelatedPubRefresh
   * @throws ServiceException
   */
  void savePrjRelatedPubRefresh(PrjRelatedPubRefresh prjRelatedPubRefresh) throws ServiceException;


}
