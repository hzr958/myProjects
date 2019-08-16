package com.smate.web.v8pub.service.sns;

import java.util.List;

import com.smate.core.base.pub.po.PubPdwhSnsRelationPO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.BaseService;

public interface PubPdwhSnsRelationService extends BaseService<Long, PubPdwhSnsRelationPO> {

  /**
   * 根据pubId和pdwhId获取关系对象
   * 
   * @return
   * @throws ServiceException
   */
  PubPdwhSnsRelationPO getByPubIdAndPdwhId(Long pubId, Long pdwhId) throws ServiceException;

  Long getPdwhIdBySnsId(Long pubId);

  List<Long> getSnsIdByPdwhId(Long pdwhPubId);

  List<Long> getSnsPubIdsByPdwhId(Long pdwhPubId, Long snsPubId);

  public List<String> getPubRelationPsninfo(Long pubId, String keyword, Long ownerPsnId, Long currentUserId);

  void delPubPdwhSnsRelationByPdwhPubId(Long pdwhPubId) throws ServiceException;
}
