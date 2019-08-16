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
import com.smate.sie.core.base.utils.dao.statistics.KpiPayImpactDao;
import com.smate.sie.core.base.utils.dao.statistics.SieSTImpactExtendRefreshDao;
import com.smate.sie.core.base.utils.dao.statistics.SieSTImpactSyncRefreshDao;
import com.smate.sie.core.base.utils.model.statistics.ImpactConsts;
import com.smate.sie.core.base.utils.model.statistics.KpiPayImpact;
import com.smate.sie.core.base.utils.model.statistics.SieSTImpactExtendRefresh;
import com.smate.sie.core.base.utils.model.statistics.SieSTImpactSyncRefresh;

@Service("sieKpiPayImpactExtendService")
@Transactional(rollbackFor = Exception.class)
public class SieKpiPayImpactExtendServiceImpl implements SieKpiPayImpactService<SieSTImpactExtendRefresh> {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieSTImpactExtendRefreshDao sieSTImpactExtendRefreshDao;
  @Autowired
  private KpiPayImpactDao kpiPayImpactDao;
  @Autowired
  private SieSTImpactSyncRefreshDao sieSTImpactSyncRefreshDao;

  @Override
  public Long cntNeedDeal(Date nowDate) throws ServiceException {
    try {
      return kpiPayImpactDao.cntNeedDeal(nowDate);

    } catch (Exception e) {
      logger.error("读取KPI_PAY_IMPACT表中需要处理的单位总数出错 ！", e);
      throw new ServiceException("读取KPI_PAY_IMPACT表中需要处理的单位总数出错 ！", e);
    }
  }

  @Override
  public void setNeedDealall(Date now) throws ServiceException {
    try {
      Page<KpiPayImpact> tempPage = new Page<KpiPayImpact>(100);
      tempPage = kpiPayImpactDao.LoadNeedDealRecords(now, tempPage);
      if (tempPage.getTotalCount() > 0) {
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = kpiPayImpactDao.LoadNeedDealRecords(now, tempPage);
          }
          for (KpiPayImpact ipact : tempPage.getResult()) {
            SieSTImpactExtendRefresh refresh = new SieSTImpactExtendRefresh(ipact.getInsId(), ImpactConsts.STATUS_WAIT);
            sieSTImpactExtendRefreshDao.save(refresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("设置ST_IMPACT_EXTEND_REFRESH表中需要处理的单位出错 ！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<SieSTImpactExtendRefresh> LoadNeedDealRecords(int maxSize) throws ServiceException {
    List<SieSTImpactExtendRefresh> Inss = null;
    try {
      Inss = sieSTImpactExtendRefreshDao.LoadNeedDealRecords(maxSize);
    } catch (Exception e) {
      logger.error("读取ST_IMPACT_EXTEND_REFRESH表中需要处理的单位出错 ！", e);
      throw new ServiceException(e);
    }
    return Inss;
  }

  @Override
  public void saveSieSTImpactSyncRefresh(SieSTImpactExtendRefresh refresh) throws ServiceException {
    try {
      sieSTImpactExtendRefreshDao.save(refresh);
    } catch (Exception e) {
      logger.error("保存数据到取ST_IMPACT_EXTEND_REFRESH表出错 ！insId:{}; status:{}; lastRunTime:{}",
          new Object[] {refresh.getInsId(), refresh.getStatus(), refresh.getLastRunTime(), e});
      throw new ServiceException("保存数据到取ST_IMPACT_EXTEND_REFRESH表出错 ！insId: " + refresh.getInsId() + "; status:"
          + refresh.getStatus() + "; lastRunTime:" + refresh.getLastRunTime(), e);
    }

  }


  @Override
  public Boolean alreadyDealBase(Long insId) throws ServiceException {
    Boolean flag = true;
    try {
      SieSTImpactSyncRefresh refresh = sieSTImpactSyncRefreshDao.get(insId);
      if (refresh != null && refresh.getStatus() == ImpactConsts.STATUS_WAIT) {
        flag = false;
      }
    } catch (Exception e) {
      logger.error("获取ST_IMPACT_SYNC_REFRESH表中某单位是否处理完成异常 ！insId:{}", new Object[] {insId, e});
      throw new ServiceException("获取ST_IMPACT_SYNC_REFRESH表中某单位是否处理完成异常 ！insId:" + insId);
    }
    return flag;

  }

}
