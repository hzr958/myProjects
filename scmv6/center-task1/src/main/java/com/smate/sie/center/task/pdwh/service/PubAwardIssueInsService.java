package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PubAwardIssueIns;

public interface PubAwardIssueInsService {

  // 模糊查询
  public List<PubAwardIssueIns> findAllPubAwardIssueIns(String searchKey) throws SysServiceException, DaoException;

  // 保存
  public void savePubAwardIssueIns(String issueInsName, Long pdwhId) throws SysServiceException;

}
