package com.smate.sie.center.task.pdwh.json.service;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 新增或更新专利接口
 * 
 * @author lijianming
 *
 */
public interface PatSaveOrUpdateService {

  // 新增
  public SiePatent createPatent(PubJsonDTO pubJson) throws ServiceException;

  // 更新
  public SiePatent updatePatent(PubJsonDTO pubJson, PubPdwhPO pdwhPublications) throws ServiceException;
}
