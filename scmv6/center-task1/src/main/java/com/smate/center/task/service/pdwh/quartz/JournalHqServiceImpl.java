package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.JournalHqDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.RefKwForm;



@Service("journalHqService")
@Transactional(rollbackFor = Exception.class)
public class JournalHqServiceImpl implements JournalHqService {

  @Autowired
  private JournalHqDao journalHqDao;

  @Override
  public List<RefKwForm> filterJournalHq(List<RefKwForm> refKwFormList) throws ServiceException {
    for (int i = refKwFormList.size() - 1; i >= 0; i--) {
      String issn = refKwFormList.get(i).getIssn();
      boolean isJnlHq = journalHqDao.isJnlHqExist(issn);
      if (!isJnlHq) {
        refKwFormList.remove(i);
      }
    }
    return refKwFormList;
  }

}
