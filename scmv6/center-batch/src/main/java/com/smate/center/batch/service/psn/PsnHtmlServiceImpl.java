package com.smate.center.batch.service.psn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnHtmlRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnHtmlRefresh;

@Service("snsPsnHtmlService")
@Transactional(rollbackFor = Exception.class)
public class PsnHtmlServiceImpl implements PsnHtmlService {

  @Autowired
  private PsnHtmlRefreshDao psnHtmlRefreshDao;

  @Override
  public void updatePsnHtmlRefresh(Long psnId) throws ServiceException {
    PsnHtmlRefresh psnHtmlRefresh = psnHtmlRefreshDao.findByPsnId(psnId);
    if (psnHtmlRefresh == null) {
      psnHtmlRefresh = new PsnHtmlRefresh();
      psnHtmlRefresh.setPsnId(psnId);
      psnHtmlRefresh.setTempCode(0);// 该字段赋０值，
      psnHtmlRefresh.setStatus(1);
    } else if (psnHtmlRefresh.getStatus() == 0) {
      psnHtmlRefresh.setStatus(1);
    }
    psnHtmlRefreshDao.save(psnHtmlRefresh);

  }

  // 刷新人员html
  @Override
  public void saveToRefreshTask(Long psnId) throws ServiceException {
    PsnHtmlRefresh refresh = psnHtmlRefreshDao.findByPsnId(psnId);
    if (refresh == null) {
      refresh = new PsnHtmlRefresh();
      refresh.setPsnId(psnId);
    }
    refresh.setTempCode(0);
    refresh.setStatus(1);
    psnHtmlRefreshDao.save(refresh);

  }



}
