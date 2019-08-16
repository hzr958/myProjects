package com.smate.center.batch.service.psn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rcmd.journal.RcmdPsnJournalDao;
import com.smate.center.batch.dao.rcmd.psn.PsnAreaClassifyDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.psn.PsnAreaClassify;

/**
 * 人员领域大类，默认使用前两个项目大类，无项目情况下使用成果期刊大类.
 * 
 * 
 * @author liqinghua
 * 
 */
@Service("psnAreaClassifyService")
@Transactional(rollbackFor = Exception.class)
public class PsnAreaClassifyServiceImpl implements PsnAreaClassifyService {

  /**
   * 
   */
  private static final long serialVersionUID = 7518356602396170222L;
  // 保留人员领域大类个数.
  private static final int PSN_AREA_CLASSIFY_N = 2;
  @Autowired
  private PsnAreaClassifyDao psnAreaClassifyDao;
  @Autowired
  private RcmdPsnJournalDao rcmdPsnJournalDao;


  @Override
  public List<String> getPsnClassify(Long psnId) throws ServiceException {
    return psnAreaClassifyDao.getPsnAreaClassifyStr(psnId);
  }


  @Override
  public void savePsnAreaClassify(PsnAreaClassify psnAreaClassify) throws ServiceException {
    psnAreaClassifyDao.save(psnAreaClassify);
  }
}
