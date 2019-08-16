package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;

public interface JournalAreaClassifyService {

  /**
   * @param issn
   * @return
   * @throws ServiceException
   */
  List<String> getJournalAreaClassify(String issn) throws ServiceException;
}
