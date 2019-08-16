package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.BaseJournalSnsDao;
import com.smate.center.batch.model.pdwh.pub.BaseJournal;
import com.smate.center.batch.model.sns.pub.BaseJournalSns;
import com.smate.center.batch.service.pdwh.pub.BaseJournalToSnsRefreshService;
import com.smate.center.batch.service.pdwh.pub.JournalService;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * sns 冗余基础期刊Service实现类
 * 
 * @author tsz
 * 
 */
@Service("baseJournalSnsService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalSnsServiceImpl implements BaseJournalSnsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BaseJournalSnsDao baseJournalSnsDao;

  @Override
  public void addBaseJournalSns(BaseJournal bj, JournalService journalService,
      BaseJournalToSnsRefreshService baseJournalToSnsRefreshService) throws Exception {

    try {
      if (bj == null) {
        return;
      }
      // 通过id获取影响因子
      String impactFactors = journalService.getBaseJournalImpactors(bj.getJouId());
      // 够造对象 直接保存
      BaseJournalSns bjsns = new BaseJournalSns(bj.getJouId(), bj.getTitleEn(), bj.getTitleXx(), bj.getPissn(),
          impactFactors, bj.getTitleEn() != null ? bj.getTitleEn().toLowerCase() : bj.getTitleEn(),
          bj.getTitleXx() != null ? bj.getTitleXx().toLowerCase() : bj.getTitleXx());
      baseJournalSnsDao.save(bjsns);

      // 更新刷新状态状态
      baseJournalToSnsRefreshService.updateRefresh(bj.getJouId());
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("冗余期刊信息失败");
      throw new Exception(e);
    }
  }

  @Override
  public BaseJournalSns getById(Long jouId) throws BatchTaskException {

    return baseJournalSnsDao.get(jouId);
  }

}
