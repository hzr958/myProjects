package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PubBookPublisher;

public interface PubBookPublisherService {

  // 模糊查询
  public List<PubBookPublisher> findAllPubBookPublisher(String searchKey) throws SysServiceException, DaoException;

  // 保存
  public void savePubBookPublisher(String orgName, Long pdwhId) throws SysServiceException;

}
