package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubCitationsPO;
import com.smate.web.v8pub.service.BaseService;

public interface PubCitationsService extends BaseService<Long, PubCitationsPO> {

  boolean isExistsPubCitations(Long pubId) throws ServiceException;
}
