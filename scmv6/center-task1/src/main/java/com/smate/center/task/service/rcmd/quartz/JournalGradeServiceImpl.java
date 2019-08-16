package com.smate.center.task.service.rcmd.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rcmd.quartz.RcmdJournalGradeDao;
import com.smate.center.task.exception.ServiceException;



/**
 * 期刊等级服务.
 * 
 * @author lqh
 * 
 */
@Service("journalGradeService")
@Transactional(rollbackFor = Exception.class)
public class JournalGradeServiceImpl implements JournalGradeService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RcmdJournalGradeDao journalGradeDao;

  @Override
  public int getJnlGrade(String issn) throws ServiceException {
    return journalGradeDao.getJournalGrade(issn);
  }


}
