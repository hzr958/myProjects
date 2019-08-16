package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.dao.prd.Sie6ProductDao;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.statistics.SiePsnRefreshDao;
import com.smate.sie.core.base.utils.dao.statistics.SiePsnStatisticsDao;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.statistics.SiePsnRefresh;
import com.smate.sie.core.base.utils.model.statistics.SiePsnStatistics;

@Service("sieStPsnService")
@Transactional(rollbackFor = Exception.class)
public class SieStPsnServiceImpl implements SieStInsService<SiePsnRefresh> {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SiePsnRefreshDao siePsnRefreshDao;
  @Autowired
  private SiePsnStatisticsDao siePsnStatisticsDao;
  @Autowired
  private PsnStatisticsDao snsPsnStatisticsDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private Sie6ProductDao sie6ProductDao;

  @Override
  public void doStatistics(Long psnId) throws ServiceException {

    try {
      SiePsnStatistics insStat = null;
      insStat = siePsnStatisticsDao.get(psnId);
      if (insStat == null) {
        insStat = new SiePsnStatistics();
      }
      insStat.setPsnId(psnId);
      PsnStatistics psnStat = snsPsnStatisticsDao.get(psnId);
      if (psnStat != null) {
        insStat.setPubSum(psnStat.getPubSum());
        insStat.setPrjSum(psnStat.getPrjSum());
        insStat.setPtSum(psnStat.getPatentSum());
        insStat.setHindex(psnStat.getHindex());
        Long pdCnt = sie6ProductDao.getPdCount(psnId);
        insStat.setPdSum(pdCnt.intValue());
      }
      insStat.setUpdateDate(new Date());
      siePsnStatisticsDao.save(insStat);
    } catch (Exception e) {
      logger.error("人员统计任务，统计数据时出错,psnId: " + psnId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<SiePsnRefresh> loadNeedCountKeyId(int maxSize) throws ServiceException {
    List<SiePsnRefresh> psnIds = null;
    try {
      psnIds = siePsnRefreshDao.loadNeedCountPsnId(maxSize);
    } catch (Exception e) {
      logger.error("读取ST_PSN_REFRESH表中需要统计的人员Id出错 ！", e);
      throw new ServiceException(e);
    }
    return psnIds;
  }

  @Override
  public void updateStatus() throws ServiceException {
    try {
      siePsnRefreshDao.updateStatus();
    } catch (Exception e) {
      logger.error("本次人员统计任务处理完成，更新ST_PSN_REFRESH表所有记录的status为0出错。", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Long countNeedCountKeyId() throws ServiceException {
    try {

      return siePsnRefreshDao.countNeedCountPsnId();

    } catch (Exception e) {
      logger.error("读取ST_PSN_REFRESH表中需要统计的人员出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveStRefresh(SiePsnRefresh insStatRefresh) throws ServiceException {
    try {
      siePsnRefreshDao.save(insStatRefresh);
    } catch (Exception e) {
      logger.error("人员统计任务：根据处理结果更新ST_PSN_REFRESH表状态出错。，psnId: " + insStatRefresh.getPsnId() + ",status: "
          + insStatRefresh.getStatus(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void setNeedCountKeyId() throws ServiceException {
    try {
      siePsnRefreshDao.deleteAll();
      Page<SieInsPerson> tempPage = new Page<SieInsPerson>(1000);
      tempPage = sieInsPersonDao.getPsnIdByStatus(tempPage);
      if (tempPage.getTotalCount() > 0) {
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = sieInsPersonDao.getPsnIdByStatus(tempPage);
          }
          for (SieInsPerson psn : tempPage.getResult()) {
            SiePsnRefresh fresh = new SiePsnRefresh(psn.getPk().getPsnId(), 0, 0);
            siePsnRefreshDao.save(fresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("人员统计任务：根初始化ST_PSN_REFRESH表出错。 ", e);
    }
  }

}
