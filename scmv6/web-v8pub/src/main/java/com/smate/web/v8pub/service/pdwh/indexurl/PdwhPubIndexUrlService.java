package com.smate.web.v8pub.service.pdwh.indexurl;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.service.BaseService;

public interface PdwhPubIndexUrlService extends BaseService<Long, PdwhPubIndexUrl> {

  /**
   * 基准库获取成果短地址
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  String getIndexUrlByPubId(Long pubId) throws ServiceException;

}
