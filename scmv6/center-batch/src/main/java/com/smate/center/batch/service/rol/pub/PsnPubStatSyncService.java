package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubPsnRol;

/**
 * 个人成果统计同步服务.
 * 
 * @author yamingd
 * 
 */
public interface PsnPubStatSyncService {

  /**
   * 添加成果人员关系.
   * 
   * @param pubId
   * @param insId
   * @param psnId
   * @param assignMode
   * @param isConfrim
   */
  PubPsnRol addPubPsn(Long pubId, Long insId, Long psnId, Integer assignMode, boolean isConfirm, Long pmId)
      throws ServiceException;

  /**
   * 添加成果人员关系.
   * 
   * @param pubId
   * @param insId
   * @param psnId
   * @param assignMode
   * @param isConfrim
   */
  PubPsnRol addPubPsn(Long pubId, Long insId, Long psnId, Float score, Integer seqNo, Integer assignMode,
      boolean isConfirm, Long pmId) throws ServiceException;

  /**
   * 添加成果人员关系.
   * 
   * @param pubId
   * @param insId
   * @param psnId
   * @param assignMode
   * @param isConfrim
   */
  PubPsnRol addPubPsn(Long pubId, Long insId, Long psnId, Float score, Integer seqNo, Integer assignMode,
      boolean isConfirm) throws ServiceException;

  /**
   * 删除成果、人员关系.
   * 
   * @param pubId
   * @param insId
   * @param psnId
   */
  void removePubPsn(Long pubId, Long insId, Long psnId) throws ServiceException;

  /**
   * 删除成果、人员关系.
   * 
   * @param pubId
   * @param insId
   */
  void removePubPsn(Long pubId, Long insId) throws ServiceException;

  /**
   * 删除成果作者、人员关系.
   * 
   * @param pubId
   * @param pmId
   * @throws ServiceException
   */
  void removePubPsnByPmId(Long pubId, Long pmId) throws ServiceException;
}
