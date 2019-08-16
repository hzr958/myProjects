package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubSituationPO;
import com.smate.web.v8pub.service.BaseService;

public interface PdwhPubSituationService extends BaseService<Long, PdwhPubSituationPO> {

  /**
   * 根据pdwhPubId删除基准库库成果引用情况，删除所有的引用情况
   * 
   * @param pdwhPubId 基准库成果id
   * @throws ServiceException
   */
  void deleteByPubId(Long pdwhPubId) throws ServiceException;


  List<String> listByPdwhPubId(Long pdwhPubId);

}
