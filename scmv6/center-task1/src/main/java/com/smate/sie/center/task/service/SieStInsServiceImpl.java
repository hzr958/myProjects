package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.insunit.SieInsUnitDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsRoleDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.sie.core.base.utils.dao.admin.SieInsUnitAdminDao;
import com.smate.sie.core.base.utils.dao.prd.Sie6ProductDao;
import com.smate.sie.core.base.utils.dao.prj.SieProjectDao;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.dao.statistics.SieInsRefreshDao;
import com.smate.sie.core.base.utils.dao.statistics.SieInsStatisticsDao;
import com.smate.sie.core.base.utils.model.statistics.SieInsRefresh;
import com.smate.sie.core.base.utils.model.statistics.SieInsStatistics;

@Service("sieStInsService")
@Transactional(rollbackFor = Exception.class)
public class SieStInsServiceImpl implements SieStInsService<SieInsRefresh> {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieInsRefreshDao sieInsRefreshDao;
  @Autowired
  private SieInsStatisticsDao sieInsStatisticsDao;
  @Autowired
  private SieInsUnitDao sieInsUnitDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private Sie6InsRoleDao sie6InsRoleDao;
  @Autowired
  private SieInsUnitAdminDao sieInsUnitAdminDao;
  @Autowired
  private SiePublicationDao siePublicationDao;
  @Autowired
  private SiePatentDao siePatentDao;
  @Autowired
  private SieProjectDao sieProjectDao;
  @Autowired
  private Sie6ProductDao sie6ProductDao;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;

  @Override
  public void doStatistics(Long insId) throws ServiceException {

    try {
      SieInsStatistics insStat = null;
      insStat = sieInsStatisticsDao.get(insId);
      if (insStat == null) {
        insStat = new SieInsStatistics();
      }
      insStat.setInsId(insId);
      insStat.setPubSum(siePublicationDao.getConfirmPubTotalNumByInsId(insId).intValue());
      insStat.setPrjSum(sieProjectDao.getProjectTotalNumByInsId(insId).intValue());
      insStat.setPtSum(siePatentDao.getConfirmPtTotalNumByInsId(insId).intValue());
      insStat.setPdSum(sie6ProductDao.getProductTotalNumByInsId(insId).intValue());
      insStat.setPsnSum(sieInsPersonDao.getPsnTotalNumByInsId(insId).intValue());
      insStat.setUnitNum(sieInsUnitDao.getUnitSumByInsId(insId).intValue());
      insStat.setAdminSum(sie6InsRoleDao.getInsAdminSumByInsId(insId).intValue());
      insStat.setUnitAdminSum(sieInsUnitAdminDao.getInsUnitAdminSumByInsId(insId).intValue());
      insStat.setUpdateDate(new Date());
      sieInsStatisticsDao.saveOrUpdate(insStat);
    } catch (Exception e) {
      logger.error("单位统计任务，统计数据时出错,insId: " + insId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<SieInsRefresh> loadNeedCountKeyId(int maxSize) throws ServiceException {
    List<SieInsRefresh> insIds = null;
    try {
      insIds = sieInsRefreshDao.loadNeedCountInsId(maxSize);
    } catch (Exception e) {
      logger.error("读取ST_INS_REFRESH表中需要统计的单位Id出错 ！", e);
      throw new ServiceException(e);
    }
    return insIds;
  }

  @Override
  public void updateStatus() throws ServiceException {
    try {
      sieInsRefreshDao.updateStatus();
    } catch (Exception e) {
      logger.error("本次单位统计任务处理完成，更新ST_INS_REFRESH表所有记录的status为0出错。", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Long countNeedCountKeyId() throws ServiceException {
    try {

      return sieInsRefreshDao.countNeedCountInsId();

    } catch (Exception e) {
      logger.error("读取ST_INS_REFRESH表中需要统计的单位总数出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveStRefresh(SieInsRefresh insStatRefresh) throws ServiceException {
    try {
      sieInsRefreshDao.save(insStatRefresh);
    } catch (Exception e) {
      logger.error("单位统计任务：根据处理结果更新ST_INS_REFRESH表状态出错。，insId: " + insStatRefresh.getInsId() + ",status: "
          + insStatRefresh.getStatus(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void setNeedCountKeyId() throws ServiceException {
    try {
      sieInsRefreshDao.deleteAll();
      Page<Sie6Institution> tempPage = new Page<Sie6Institution>(1000);
      tempPage = sie6InstitutionDao.getInsIdByStatus(tempPage);
      if (tempPage.getTotalCount() > 0) {
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = sie6InstitutionDao.getInsIdByStatus(tempPage);
          }
          for (Sie6Institution ins : tempPage.getResult()) {
            SieInsRefresh fresh = new SieInsRefresh(ins.getId(), 0, 0);
            sieInsRefreshDao.save(fresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("人员统计任务：初始化ST_PSN_REFRESH表出错。 ", e);
    }

  }

}
