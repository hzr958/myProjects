package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubCitationsPO;
import com.smate.web.v8pub.service.BaseService;

public interface PdwhPubCitationsService extends BaseService<Long, PdwhPubCitationsPO> {

  /**
   * 通过pubId和dbId获取PdwhPubCitationsPO对象
   * 
   * @param pubId
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public PdwhPubCitationsPO getByPubIdAndDbId(Long pubId, Integer dbId) throws ServiceException;

}
