package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.RefKwForm;


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
