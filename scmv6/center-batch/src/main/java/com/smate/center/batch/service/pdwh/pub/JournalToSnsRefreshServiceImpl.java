package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.JournalDao;
import com.smate.center.batch.dao.pdwh.pub.JournalToSnsRefreshDao;
import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.pdwh.pub.JournalToSnsRefresh;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 同步期刊到sns 服务实现类
 * 
 * @author tsz
 * 
 */
@Service("journalToSnsRefreshService")
@Transactional(rollbackFor = Exception.class)
public class JournalToSnsRefreshServiceImpl implements JournalToSnsRefreshService {

  @Autowired
  private JournalToSnsRefreshDao journalToSnsRefreshDao;

  @Autowired
  private JournalDao journalDao;

  @Override
  public Journal findJournalById(Long jid) throws BatchTaskException {
    return journalDao.get(jid);
  }

  @Override
  public List<Journal> getNeedRefresh(Integer maxsize) throws BatchTaskException {

    return journalToSnsRefreshDao.findNeedRefresh(maxsize);
  }

  @Override
  public List<JournalToSnsRefresh> findNeedRefresh(Integer maxsize) throws BatchTaskException {
    return journalToSnsRefreshDao.getNeedRefresh(maxsize);
  }

  @Override
  public void updateRefresh(Long jId) throws BatchTaskException {
    JournalToSnsRefresh j = journalToSnsRefreshDao.findJ(jId);
    if (j != null) {
      j.setJidStatus(0);
    } else {
      j = new JournalToSnsRefresh();
      j.setJid(jId);
      j.setJidStatus(0);
    }
    journalToSnsRefreshDao.save(j);
  }

}
