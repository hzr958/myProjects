package com.smate.sie.center.task.pdwh.json.service;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

public interface PubSaveOrUpdateService {

  // 新增
  public SiePublication createPublication(PubJsonDTO pubJson, PubPdwhPO pdwhPublications) throws ServiceException;

  // 更新
  public SiePublication updatePublication(PubJsonDTO pubJson, PubPdwhPO pdwhPublications) throws ServiceException;

}
