package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.ImportPdwhPub;

public interface ImportPdwhPubService {

  public List<ImportPdwhPub> getImportPdwhPubList(Integer size);

  public void updateImportPdwhPub(ImportPdwhPub imp) throws SysServiceException;

  // 把全部的记录状态更改为1
  public void updateAllImportPdwhPub() throws SysServiceException;

  public Long getInsCountByStatus() throws SysServiceException;
}
