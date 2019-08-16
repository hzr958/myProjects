package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.BaseJournalDao;
import com.smate.center.batch.dao.pdwh.pub.BaseJournalToSnsRefreshDao;
import com.smate.center.batch.model.pdwh.pub.BaseJournal;
import com.smate.center.batch.model.pdwh.pub.BaseJournalToSnsRefresh;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 同步基准期刊到sns 服务实现类
 * 
 * @author tsz
 * 
 */
@Service("baseJournalToSnsRefreshService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalToSnsRefreshServiceImpl implements BaseJournalToSnsRefreshService {

  @Autowired
  private BaseJournalToSnsRefreshDao baseJournalToSnsRefreshDao;
  @Autowired
  private BaseJournalDao baseJournalDao;

  @Override
  public BaseJournal findBaseJournalById(Long jouId) throws BatchTaskException {
    return baseJournalDao.get(jouId);
  }

  @Override
  public List<BaseJournal> getNeedRefresh(Integer maxsize) throws BatchTaskException {
    return baseJournalToSnsRefreshDao.getNeedRefresh(maxsize);
  }

  @Override
  public List<BaseJournalToSnsRefresh> findNeedRefresh(Integer maxsize) throws BatchTaskException {
    return baseJournalToSnsRefreshDao.findNeedRefresh(maxsize);
  }

  @Override
  public void updateRefresh(Long jouId) throws BatchTaskException {
    BaseJournalToSnsRefresh b = baseJournalToSnsRefreshDao.findJ(jouId);
    if (b != null) {
      b.setStatus(0);
    } else {
      b = new BaseJournalToSnsRefresh();
      b.setBjid(jouId);
      b.setStatus(0);
    }
    baseJournalToSnsRefreshDao.save(b);
  }

}
