package com.smate.web.v8pub.service.sns;

import java.util.HashMap;
import java.util.List;

import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSituationPO;
import com.smate.web.v8pub.service.BaseService;

public interface PubSituationService extends BaseService<Long, PubSituationPO> {

  /**
   * 根据pubId删除个人库成果引用情况，删除所有的引用情况
   * 
   * @param pubId
   * @throws ServiceException
   */
  void deletePubSituationByPubId(Long pubId) throws ServiceException;

  /**
   * 根据人员psnId获取该人员下所有成果的收录情况
   * 
   * @param psnId
   * @return 以pubId为key，PubSituationPO为value
   * @throws ServiceException
   */
  HashMap<Long, PubSituationPO> mapSituationByPsnId(Long psnId) throws ServiceException;

  /**
   * 删除所有收录情况记录
   * 
   * @param pubId
   * @throws ServiceException
   */
  void deleteAll(Long pubId) throws ServiceException;

  /**
   * 补全完善srcDbId
   * 
   * @param saveList
   * @throws ServiceException
   */
  void perfectSrcDbId(List<PubSituationDTO> saveList) throws ServiceException;

}
