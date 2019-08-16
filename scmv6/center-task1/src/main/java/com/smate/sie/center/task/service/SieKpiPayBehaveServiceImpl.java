package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.dao.statistics.KpiPayBehaveDao;
import com.smate.sie.core.base.utils.dao.statistics.SieSTPatSyncRefreshDao;
import com.smate.sie.core.base.utils.dao.statistics.SieSTPubSyncRefreshDao;
import com.smate.sie.core.base.utils.model.statistics.BhConsts;
import com.smate.sie.core.base.utils.model.statistics.KpiPayBehave;
import com.smate.sie.core.base.utils.model.statistics.SieSTPatSyncRefresh;
import com.smate.sie.core.base.utils.model.statistics.SieSTPubSyncRefresh;

/**
 * 社交行为付费表服务实现
 * 
 * @author hd
 *
 */
@Service("sieKpiPayBehaveService")
@Transactional(rollbackFor = Exception.class)
public class SieKpiPayBehaveServiceImpl implements SieKpiPayBehaveService {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private KpiPayBehaveDao kpiPayBehaveDao;
  @Autowired
  private SieSTPubSyncRefreshDao sieSTPubSyncRefreshDao;
  @Autowired
  private SieSTPatSyncRefreshDao sieSTPatSyncRefreshDao;

  @Override
  public Long cntNeedDeal(Date nowDate) throws ServiceException {
    try {
      return kpiPayBehaveDao.cntNeedDeal(nowDate);
    } catch (Exception e) {
      logger.error("读取KPI_PAY_BEHAVE表中需要处理的单位总数出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void setNeedDealPuball(Date now) throws ServiceException {
    try {
      Page<KpiPayBehave> tempPage = new Page<KpiPayBehave>(100);
      tempPage = kpiPayBehaveDao.LoadNeedDealRecords(now, tempPage);
      if (tempPage.getTotalCount() > 0) {
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = kpiPayBehaveDao.LoadNeedDealRecords(now, tempPage);
          }
          for (KpiPayBehave behave : tempPage.getResult()) {
            SieSTPubSyncRefresh refresh = new SieSTPubSyncRefresh(behave.getInsId(), BhConsts.STATUS_WAIT);
            sieSTPubSyncRefreshDao.save(refresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果：设置ST_PUB_SYNC_REFRESH表中需要处理的单位出错 ！", e);
      throw new ServiceException(e);
    }

  }


  @Override
  public void setNeedDealPatall(Date now) throws ServiceException {
    try {
      Page<KpiPayBehave> tempPage = new Page<KpiPayBehave>(100);
      tempPage = kpiPayBehaveDao.LoadNeedDealRecords(now, tempPage);
      if (tempPage.getTotalCount() > 0) {
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = kpiPayBehaveDao.LoadNeedDealRecords(now, tempPage);
          }
          for (KpiPayBehave behave : tempPage.getResult()) {
            SieSTPatSyncRefresh refresh = new SieSTPatSyncRefresh(behave.getInsId(), BhConsts.STATUS_WAIT);
            sieSTPatSyncRefreshDao.save(refresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("专利：设置ST_PAT_SYNC_REFRESH表中需要处理的单位出错 ！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<SieSTPubSyncRefresh> LoadNeedDealPubRecords(int maxSize) throws ServiceException {
    List<SieSTPubSyncRefresh> Inss = null;
    try {
      Inss = sieSTPubSyncRefreshDao.LoadNeedDealRecords(maxSize);
    } catch (Exception e) {
      logger.error("读取ST_PUB_SYNC_REFRESH表中需要处理的单位出错 ！", e);
      throw new ServiceException(e);
    }
    return Inss;
  }

  @Override
  public List<SieSTPatSyncRefresh> LoadNeedDealPatRecords(int maxSize) throws ServiceException {
    List<SieSTPatSyncRefresh> Inss = null;
    try {
      Inss = sieSTPatSyncRefreshDao.LoadNeedDealRecords(maxSize);
    } catch (Exception e) {
      logger.error("读取ST_PAT_SYNC_REFRESH表中需要处理的单位出错 ！", e);
      throw new ServiceException(e);
    }
    return Inss;
  }

  @Override
  public void saveSTPubSyncRefresh(SieSTPubSyncRefresh refresh) throws ServiceException {
    try {
      sieSTPubSyncRefreshDao.save(refresh);

    } catch (Exception e) {
      logger.error("保存数据到取ST_PUB_SYNC_REFRESH表出错 ！insId:{}; status:{}; lastRunTime:{}",
          new Object[] {refresh.getInsId(), refresh.getStatus(), refresh.getLastRunTime(), e});
      throw new ServiceException("保存数据到取ST_PUB_SYNC_REFRESH表出错 ！insId: " + refresh.getInsId() + "; status:"
          + refresh.getStatus() + "; lastRunTime:" + refresh.getLastRunTime(), e);
    }

  }

  @Override
  public void saveSTPatSyncRefresh(SieSTPatSyncRefresh refresh) throws ServiceException {
    try {
      sieSTPatSyncRefreshDao.save(refresh);
    } catch (Exception e) {
      logger.error("保存数据到取ST_PAT_SYNC_REFRESH表出错 ！insId:{}; status:{}; lastRunTime:{}",
          new Object[] {refresh.getInsId(), refresh.getStatus(), refresh.getLastRunTime(), e});
      throw new ServiceException("保存数据到取ST_PAT_SYNC_REFRESH表出错 ！insId: " + refresh.getInsId() + "; status:"
          + refresh.getStatus() + "; lastRunTime:" + refresh.getLastRunTime(), e);
    }

  }



}
