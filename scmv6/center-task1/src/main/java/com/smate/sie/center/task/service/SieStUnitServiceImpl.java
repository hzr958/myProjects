package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.center.task.dao.Sie6InsUnitDao;
import com.smate.sie.center.task.model.Sie6InsUnit;
import com.smate.sie.core.base.utils.dao.admin.SieInsUnitAdminDao;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.statistics.SiePsnStatisticsDao;
import com.smate.sie.core.base.utils.dao.statistics.SieUnitRefreshDao;
import com.smate.sie.core.base.utils.dao.statistics.SieUnitStatisticsDao;
import com.smate.sie.core.base.utils.model.statistics.SieUnitRefresh;
import com.smate.sie.core.base.utils.model.statistics.SieUnitStatistics;

@Service("sieStUnitService")
@Transactional(rollbackFor = Exception.class)
public class SieStUnitServiceImpl implements SieStInsService<SieUnitRefresh> {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieUnitRefreshDao sieUnitRefreshDao;
  @Autowired
  private SieUnitStatisticsDao sieUnitStatisticsDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private SieInsUnitAdminDao sieInsUnitAdminDao;
  @Autowired
  private Sie6InsUnitDao sieInsUnitDao;
  @Autowired
  private SiePsnStatisticsDao siePsnStatisticsDao;

  @Override
  public void doStatistics(Long unitId) throws ServiceException {

    try {
      SieUnitStatistics insStat = null;
      insStat = sieUnitStatisticsDao.get(unitId);
      if (insStat == null) {
        insStat = new SieUnitStatistics();
      }
      insStat.setUnitId(unitId);
      List<Long> psnIds = sieInsPersonDao.getPsnByUnitId(unitId);
      insStat.setPubSum(siePsnStatisticsDao.getUnitPubSumByPsn(psnIds).intValue());
      insStat.setPrjSum(siePsnStatisticsDao.getUnitPrjSumByPsn(psnIds).intValue());
      insStat.setPtSum(siePsnStatisticsDao.getUnitPafSumByPsn(psnIds).intValue());
      insStat.setPdSum(siePsnStatisticsDao.getUnitPdSumByPsn(psnIds).intValue());
      insStat.setPsnSum(psnIds == null ? 0 : psnIds.size());
      insStat.setUnitAdminSum(sieInsUnitAdminDao.getInsUnitAdminSumByUnitId(unitId).intValue());
      insStat.setUpdateDate(new Date());
      sieUnitStatisticsDao.saveOrUpdate(insStat);
    } catch (Exception e) {
      logger.error("部门统计任务，统计数据时出错,unitId: " + unitId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<SieUnitRefresh> loadNeedCountKeyId(int maxSize) throws ServiceException {
    List<SieUnitRefresh> insIds = null;
    try {
      insIds = sieUnitRefreshDao.loadNeedCountUnitId(maxSize);
    } catch (Exception e) {
      logger.error("读取ST_UNIT_REFRESH表中需要统计的部门Id出错 ！", e);
      throw new ServiceException(e);
    }
    return insIds;
  }

  @Override
  public void updateStatus() throws ServiceException {
    try {
      sieUnitRefreshDao.updateStatus();
    } catch (Exception e) {
      logger.error("本次部门统计任务处理完成，更新ST_UNIT_REFRESH表所有记录的status为0出错。", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Long countNeedCountKeyId() throws ServiceException {
    try {

      return sieUnitRefreshDao.countNeedCountUnitId();

    } catch (Exception e) {
      logger.error("读取ST_UNIT_REFRESH表中需要统计的部门总数出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveStRefresh(SieUnitRefresh stRefresh) throws ServiceException {
    try {
      sieUnitRefreshDao.save(stRefresh);
    } catch (Exception e) {
      logger.error(
          "部门统计任务：根据处理结果更新ST_UNIT_REFRESH表状态出错。unitId: " + stRefresh.getUnitId() + ",status: " + stRefresh.getStatus(),
          e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void setNeedCountKeyId() throws ServiceException {
    try {
      sieUnitRefreshDao.deleteAll();
      Page<Sie6InsUnit> tempPage = new Page<Sie6InsUnit>(1000);
      tempPage = sieInsUnitDao.getAllUnits(tempPage);
      if (tempPage.getTotalCount() > 0) {
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = sieInsUnitDao.getAllUnits(tempPage);
          }
          for (Sie6InsUnit unit : tempPage.getResult()) {
            SieUnitRefresh fresh = new SieUnitRefresh(unit.getId(), 0, 0);
            sieUnitRefreshDao.save(fresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("人员统计任务：初始化ST_UNIT_REFRESH表出错。 ", e);
    }

  }

}
