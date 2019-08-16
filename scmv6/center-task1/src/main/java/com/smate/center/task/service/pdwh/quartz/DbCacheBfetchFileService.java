package com.smate.center.task.service.pdwh.quartz;

import com.smate.center.task.exception.ServiceException;

public interface DbCacheBfetchFileService {

  public void readerFile(String dir, Integer xmlType) throws ServiceException;

}
