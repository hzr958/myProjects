package com.smate.center.task.service.rcmd.quartz;

import com.smate.center.task.exception.ServiceException;



/**
 * 期刊等级服务.
 * 
 * @author lqh
 * 
 */
public interface JournalGradeService {

  /**
   * 获取期刊等级
   * 
   * @param issn
   * @return
   * @throws ServiceException
   */
  int getJnlGrade(String issn) throws ServiceException;

}
