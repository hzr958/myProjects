package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rcmd.quartz.RcmdPsnJournalDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.RcmdPsnJournal;


@Service("psnJournalService")
@Transactional(rollbackFor = Exception.class)
public class PsnJournalServiceImpl implements PsnJournalService {

  @Autowired
  private RcmdPsnJournalDao psnJournalDao;

  @Override
  public int getPsnJnlMaxGrade(Long psnId) throws ServiceException {
    return psnJournalDao.getPsnJnlMaxGrade(psnId);
  }

  @Override
  public List<RcmdPsnJournal> getPsnJournal(Long psnId) throws ServiceException {
    return psnJournalDao.getPsnAllJournal(psnId);
  }

  @Override
  public int getPsnJnlCountByIssn(Long psnId, String issn) throws ServiceException {
    return psnJournalDao.getPsnJnlCountByIssn(psnId, issn);
  }
}
