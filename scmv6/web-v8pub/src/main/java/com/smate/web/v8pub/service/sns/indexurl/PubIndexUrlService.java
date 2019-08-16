package com.smate.web.v8pub.service.sns.indexurl;

import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.BaseService;

public interface PubIndexUrlService extends BaseService<Long, PubIndexUrl> {

  /**
   * 生成个人成果短地址
   * 
   * @param pubId
   * @param type
   * @return
   * @throws Exception
   */
  public String producePubShortUrl(Long pubId, String type) throws ServiceException;
}
