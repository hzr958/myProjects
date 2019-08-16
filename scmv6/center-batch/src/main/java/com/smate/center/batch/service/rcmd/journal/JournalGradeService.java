package com.smate.center.batch.service.rcmd.journal;

import com.smate.center.batch.exception.pub.ServiceException;


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

  /**
   * 是否核心期刊，ISI\中文核心期刊>=3.
   * 
   * @param issn
   * @return
   */
  public boolean isHxJ(String issn) throws ServiceException;
}
