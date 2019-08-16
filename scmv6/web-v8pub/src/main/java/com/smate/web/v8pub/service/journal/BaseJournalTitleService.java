package com.smate.web.v8pub.service.journal;

import com.smate.web.v8pub.exception.ServiceException;

public interface BaseJournalTitleService {

  /**
   * 基准库通过jname和jissn进行期刊匹配
   * 
   * @param jName
   * @param jIssn
   * @return
   * @throws ServiceException
   */
  public Long searchJournalMatchBaseJnlId(String jName, String jIssn) throws ServiceException;
}
