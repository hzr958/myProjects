package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PubThesisOrg;

public interface PubThesisOrgService {

  // 模糊查询
  public List<PubThesisOrg> findAllPubThesisOrg(String searchKey) throws SysServiceException, DaoException;

  // 保存
  public void savePubThesisOrg(String thesisOrg, Long pdwhId) throws SysServiceException;

}
