package com.smate.center.batch.service.rol.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubInsSync;
import com.smate.center.batch.service.pub.mq.PubInsSyncMessage;
import com.smate.core.base.utils.model.Page;

/**
 * pub-ins同步服务.
 * 
 */
public interface PubInsSyncRolService {

  /**
   * 同步:保存成果和单位的关系.
   * 
   * @param pubId
   * @param insId
   * @throws ServiceException
   */
  void savePubIns(PubInsSyncMessage syncMess) throws ServiceException;

  /**
   * 同步:删除成果和单位的关系.
   * 
   * @param pubId
   * @param insId
   * @throws ServiceException
   */
  void deletePubIns(PubInsSyncMessage syncMess) throws ServiceException;

  /**
   * 更新同步记录的提交状态，过滤条件.
   * 
   * @param snsPubId
   * @param insId
   * @param flag
   * @throws ServiceException
   */
  void updateSnsPubSubmittedFlag(Long snsPubId, Long insId, boolean flag) throws ServiceException;

  /**
   * 查询未提交成果列表.
   * 
   * @param insId
   * @param psnIds
   * @param startYear
   * @param endYear
   * @param pubTypeId
   * @param pubTitle
   * @param orderBy
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws ServiceException
   */
  Page<PubInsSync> queryPrepareOutputsForRO(Long insId, List<Long> psnIds, Integer startYear, Integer endYear,
      Integer pubTypeId, String pubTitle, String orderBy, Integer pageIndex, Integer pageSize) throws ServiceException;

  /**
   * 获取单位人员成果总数.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws ServiceException
   */
  Long getInsPsnPubCount(Long psnId, Long insId) throws ServiceException;
}
