package com.smate.center.task.service.sns.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PdwhFullTextFile;

public interface PdwhFullTextService {

  /**
   * 保存到PDWH_FULLTEXT_FILE表
   */
  public void saveFullTextFile(PdwhFullTextFile fulltextfile) throws ServiceException;

}
