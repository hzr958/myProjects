package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;


public interface JournalAreaClassifyService {

  /**
   * @param issn
   * @return
   * @throws ServiceException
   */
  List<String> getJournalAreaClassify(String issn) throws ServiceException;
}
