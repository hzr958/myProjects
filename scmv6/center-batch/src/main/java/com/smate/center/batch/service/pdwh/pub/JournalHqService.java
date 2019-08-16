package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.RefKwForm;

public interface JournalHqService {

  /**
   * CSSCI核心期刊跟ISI的期刊
   * 
   * @param issn
   * @return
   * @throws ServiceException
   */
  List<RefKwForm> filterJournalHq(List<RefKwForm> refKwFormList) throws ServiceException;
}
