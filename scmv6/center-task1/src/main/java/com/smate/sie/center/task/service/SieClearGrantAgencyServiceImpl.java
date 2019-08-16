package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.SieFundFocusAgencyDao;
import com.smate.sie.center.task.dao.SieFundFocusSchemeDao;
import com.smate.sie.center.task.dao.SieFundSchemeViewDao;
import com.smate.sie.center.task.dao.SieKpiInsFundRefreshDao;
import com.smate.sie.center.task.model.SieInsFocusAgency;
import com.smate.sie.center.task.model.SieInsFocusScheme;
import com.smate.sie.center.task.model.SieKpiInsFundRefresh;
import com.smate.sie.core.base.utils.dao.statistics.DiffKpiInsGrantDao;
import com.smate.sie.core.base.utils.dao.statistics.SieFundAgencyViewDao;
import com.smate.sie.core.base.utils.model.statistics.DiffKpiInsGrant;
import com.smate.sie.core.base.utils.model.statistics.SieConstFundAgencyView;

/**
 * sieClearGrantAgencyTask相关应用实现类
 * 
 * @author lijianming
 *
 */
@Service("sieClearGrantAgencyService")
@Transactional(rollbackOn = Exception.class)
public class SieClearGrantAgencyServiceImpl implements SieClearGrantAgencyService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieFundFocusAgencyDao sieFundFocusAgencyDao;
  @Autowired
  private SieFundAgencyViewDao sieFundAgencyViewDao;
  @Autowired
  private SieFundSchemeViewDao sieFundSchemeViewDao;
  @Autowired
  private DiffKpiInsGrantDao diffKpiInsGrantDao;
  @Autowired
  private SieKpiInsFundRefreshDao sieKpiInsFundRefreshDao;
  @Autowired
  private SieFundFocusSchemeDao sieFundFocusSchemeDao;

  @Override
  public void RefreshInsFundTable() throws SysServiceException {
    try {
      // 在基金机构业务表收集insId
      List<Long> insIdList = sieFundFocusAgencyDao.getInsIdListByFocusAgency();
      List<SieKpiInsFundRefresh> refreshList = sieKpiInsFundRefreshDao.getAll();
      // 如果基金机构业务表有数据则更新刷新表
      if (CollectionUtils.isNotEmpty(insIdList)) {
        // 判空处理
        if (CollectionUtils.isNotEmpty(refreshList)) {
          // 先删除KPI_INS_FUND_REFRESH表中数据
          deleteAllRefreshTableData();
        }
        // 将收集到的单位id保存到KPI_INS_FUND_REFRESH表中
        saveInsIdListIntoRefreshTable(insIdList);
      } else { // 如果基金机构业务表没有数据则删除刷新表所有数据
        deleteAllRefreshTableData();
      }
    } catch (Exception e) {
      logger.error("更新基金数据处理日志表数据失败", e);
      throw new ServiceException(e);
    }
  }

  // 删除刷新表的所有数据
  private void deleteAllRefreshTableData() {
    try {
      sieKpiInsFundRefreshDao.deleteAllRefreshData();
    } catch (Exception e) {
      logger.error("删除基金数据处理日志表数据失败", e);
      throw new ServiceException(e);
    }
  }

  // 保存单位Id到刷新表
  private void saveInsIdListIntoRefreshTable(List<Long> list) {
    try {
      // 此数组表示单位统计刷新表的status字段为默认值为0，prior_code字段的默认值为0
      // int[] sign = new int[] { 0, 0 };
      // SieKpiInsFundRefresh refresh = null;
      for (Long insId : list) {
        /* SieKpiInsFundRefresh refresh = SieKpiInsFundRefreshFactory.getSieKpiInsFundRefresh(sign); */
        SieKpiInsFundRefresh refresh = new SieKpiInsFundRefresh();
        refresh.setInsId(insId);
        refresh.setStatus(0);
        refresh.setPriorCode(0);
        sieKpiInsFundRefreshDao.save(refresh);
      }
    } catch (Exception e) {
      logger.error("保存基金数据处理日志表数据失败", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String StatAgencyGrantAndChangeCondition() throws SysServiceException {
    try {
      // 此时视图表的基金机构数
      Long allAgencyCount = sieFundAgencyViewDao.countAllAgency();
      // 此时视图表的基金机会数
      Long allSchemeCount = sieFundSchemeViewDao.countAllScheme();
      // 查找时间最近的一条记录进行统计对比
      DiffKpiInsGrant diffGrant = diffKpiInsGrantDao.getInfoByRecentTime();
      DiffKpiInsGrant newDiffGrant = new DiffKpiInsGrant();
      if (diffGrant != null) {
        newDiffGrant.setAgencyCount(allAgencyCount);
        newDiffGrant.setGrantCount(allSchemeCount);
        // 当前基金机构数大于最近时间的基金机构数
        if (allAgencyCount >= diffGrant.getAgencyCount()) {
          newDiffGrant.setAgencyInsertCount(allAgencyCount - diffGrant.getAgencyCount());
          newDiffGrant.setAgencyDeleteCount(Long.valueOf(0));
        } else {// 当前基金机构数小于最近时间的基金机构数
          newDiffGrant.setAgencyInsertCount(Long.valueOf(0));
          newDiffGrant.setAgencyDeleteCount(diffGrant.getAgencyCount() - allAgencyCount);
        }
        // 当前基金机会数大于最近时间的基金机会数
        if (allSchemeCount >= diffGrant.getGrantCount()) {
          newDiffGrant.setGrantInsertCount(allSchemeCount - diffGrant.getGrantCount());
          newDiffGrant.setGrantDeleteCount(Long.valueOf(0));
        } else {// 当前基金机会数小于最近时间的基金机会数
          newDiffGrant.setGrantInsertCount(Long.valueOf(0));
          newDiffGrant.setGrantDeleteCount(diffGrant.getGrantCount() - allSchemeCount);
        }
        newDiffGrant.setCreateDate(new Date());
        // 当数据发生变化时，保存到变化表里
        if (!diffGrant.getAgencyCount().equals(allAgencyCount) || !diffGrant.getGrantCount().equals(allSchemeCount)) {
          diffKpiInsGrantDao.save(newDiffGrant);
        }
      } else {
        newDiffGrant.setAgencyCount(allAgencyCount);
        newDiffGrant.setGrantCount(allSchemeCount);
        newDiffGrant.setAgencyDeleteCount(Long.valueOf(0));
        newDiffGrant.setAgencyInsertCount(Long.valueOf(0));
        newDiffGrant.setGrantDeleteCount(Long.valueOf(0));
        newDiffGrant.setGrantInsertCount(Long.valueOf(0));
        newDiffGrant.setCreateDate(new Date());
        // 保存到变化表里
        diffKpiInsGrantDao.save(newDiffGrant);
      }
    } catch (Exception e) {
      logger.error("统计bpo基金机构和基金机会数及其变化数出错 ！", e);
      throw new ServiceException(e);
    }
    return null;
  }

  @Override
  public Long countNeedCountInsId() throws SysServiceException {
    try {
      return sieKpiInsFundRefreshDao.countInsIdByStatusZero();
    } catch (Exception e) {
      logger.error("读取KPI_INS_FUND_REFRESH表中需要统计的单位总数出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<SieKpiInsFundRefresh> loadNeedHandleDataInsId(int batchSize) throws SysServiceException {
    List<SieKpiInsFundRefresh> insIds = null;
    try {
      insIds = sieKpiInsFundRefreshDao.getListByStatusZero(batchSize);
    } catch (Exception e) {
      logger.error("读取KPI_INS_FUND_REFRESH表中需要进行数据处理的单位Id出错 ！", e);
      throw new ServiceException(e);
    }
    return insIds;
  }

  @Override
  public void hanleRedundantData(Long insId) throws SysServiceException {
    try {
      // 检查单位下的基金机构是否有冗余数据
      List<Long> agencyIds = sieFundFocusAgencyDao.checkFocusDataIsUnnecessary(insId);
      if (agencyIds.size() > 0) {
        // 先删除基金机构下的基金机会
        deleteUnnecessaryFocusedScheme(agencyIds, insId);
        // 删除基金机构下的基金机会数据成功后，删除基金机构冗余数据
        deleteUnnecessaryFocusedAgency(agencyIds, insId);
      }
    } catch (Exception e) {
      logger.error("删除insId为" + insId + "的单位删除冗余数据失败 ！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void handleAbsentData(Long insId) throws SysServiceException {
    try {
      // 从基金机会业务表获取需要检查的基金机会所属的基金机构Id
      List<Long> agencyIds = sieFundFocusSchemeDao.getAgencyIdListByFocusedScheme(insId);
      // 检查单位的基金机构的基金机会是否有数据缺失
      for (Long agencyId : agencyIds) {
        List<Long> grantIds = sieFundSchemeViewDao.checkFocusedSchemeIsAbsence(agencyId, insId);
        // 若存在数据缺失，先根据agencyId删掉基金机会业务表再重新添加数据
        if (grantIds.size() > 0) {
          deleteFocusedSchemeBeforeInsert(insId, agencyId);
          insertFocusedSchemes(insId, agencyId);
        }
      }
    } catch (Exception e) {
      logger.error("处理基金机会业务表缺失数据失败，insId = " + insId, e);
      throw new ServiceException(e);
    }
  }

  // 在插入数据前，先清空该单位基金机构下的基金机会业务表的数据
  private void deleteFocusedSchemeBeforeInsert(Long insId, Long agencyId) {
    try {
      sieFundFocusSchemeDao.deleteFocusedSchemeByAgencyIdAndInsId(agencyId, insId);
    } catch (Exception e) {
      logger.error("在插入缺失数据前，删除基金机会业务表数据失败，insId = " + insId + ",agencyId = " + agencyId, e);
      throw new ServiceException(e);
    }
  }

  // 插入最新的该单位基金机构下的基金机会的最新数据到业务表
  private void insertFocusedSchemes(Long insId, Long agencyId) {
    try {
      List<Long> latestSchemeIds = sieFundSchemeViewDao.getSchemesByAgencyId(agencyId);
      SieInsFocusScheme focusScheme = null;
      for (Long schemeId : latestSchemeIds) {
        focusScheme = new SieInsFocusScheme();
        focusScheme.setAgencyId(agencyId);
        focusScheme.setGrantId(schemeId);
        focusScheme.setInsId(insId);
        sieFundFocusSchemeDao.save(focusScheme);
      }
    } catch (Exception e) {
      logger.error("清空数据后，保存最新基金机会业务表数据失败，insId = " + insId + ",agencyId = " + agencyId, e);
      throw new ServiceException(e);
    }
  }

  // 删除单位下基金机构下的冗余基金机会数据
  private void deleteUnnecessaryFocusedScheme(List<Long> agencyIds, Long insId) {
    try {
      sieFundFocusSchemeDao.deleteFocusedSchemeByAgencyId(agencyIds, insId);
    } catch (Exception e) {
      logger.error("删除insId为" + insId + "的单位关注的基金机构下的冗余基金机会数据出错 ！", e);
      throw new ServiceException(e);
    }
  }

  // 删除单位下冗余基金机构数据
  private void deleteUnnecessaryFocusedAgency(List<Long> agencyIds, Long insId) {
    try {
      sieFundFocusAgencyDao.deleteFocusedAgencyByAgencyListAndInsId(agencyIds, insId);
    } catch (Exception e) {
      logger.error("删除insId为" + insId + "的单位关注的基金机构冗余数据出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveRefreshData(SieKpiInsFundRefresh sieKpiInsFundRefresh) throws SysServiceException {
    try {
      sieKpiInsFundRefreshDao.save(sieKpiInsFundRefresh);
    } catch (Exception e) {
      logger.error("单位处理基金机构和基金机会冗余数据和缺少数据任务：根据处理结果更新KPI_INS_FUND_REFRESH表状态出错。，insId: "
          + sieKpiInsFundRefresh.getInsId() + ",status: " + sieKpiInsFundRefresh.getStatus(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void insertInsDataToRefreshTable() throws SysServiceException {
    try {
      // 查找在刷新表中需要插入的单位
      List<Long> needInsertInsIds = sieFundFocusAgencyDao.checkInsIdNeedInsertInReshshTable();
      if (needInsertInsIds.size() > 0) {
        saveInsIdListIntoRefreshTable(needInsertInsIds);
      }
    } catch (Exception e) {
      logger.error("将单位保存到刷新表时出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteInsDataFromRefreshTable() throws SysServiceException {
    try {
      // 查找需要删除的单位
      List<Long> needDeleteInsIds = sieKpiInsFundRefreshDao.checkInsIdsIsDeleteInRefreshTable();
      if (needDeleteInsIds.size() > 0) {
        sieKpiInsFundRefreshDao.deleteRefreshDataByInsIdList(needDeleteInsIds);
      }
    } catch (Exception e) {
      logger.error("删除刷新表数据时出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void changRefreshDataStatus() throws SysServiceException {
    try {
      sieKpiInsFundRefreshDao.updateRefreshDataStatus();
    } catch (Exception e) {
      logger.error("更新刷新表数据状态时出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> getAgencyIdListFromFocusedScheme(Long insId) throws SysServiceException {
    try {
      return sieFundFocusSchemeDao.getAgencyIdListByFocusedScheme(insId);
    } catch (Exception e) {
      logger.error("从基金机会业务表获取基金机构Id列表时出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> checkSchemeIsAbsence(Long agencyId, Long insId) throws SysServiceException {
    try {
      return sieFundSchemeViewDao.checkFocusedSchemeIsAbsence(agencyId, insId);
    } catch (Exception e) {
      logger.error("检查单位的基金机构的基金机会是否有数据缺失时出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteSchemeBeforeInsert(Long insId, Long agencyId) throws SysServiceException {
    deleteFocusedSchemeBeforeInsert(insId, agencyId);
  }

  @Override
  public void insertSchemesAfterDelete(Long insId, Long agencyId) throws SysServiceException {
    insertFocusedSchemes(insId, agencyId);
  }

  @Override
  public void handleFocusedSchemeMovedQue(Long insId) throws SysServiceException {
    try {
      List<SieInsFocusScheme> list = sieFundFocusSchemeDao.CheckAgencyIdAndGrandIdIsMate(insId);
      if (CollectionUtils.isNotEmpty(list)) {// 存在基金机会转移问题导致的冗余数据
        for (SieInsFocusScheme scheme : list) {
          sieFundFocusSchemeDao.delete(scheme);
        }
      }
    } catch (Exception e) {
      logger.error("删除因基金机会转移导致基金机会关注业务表产生的冗余数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void handleFocusedAgencyWithoutScheme(Long insId) throws SysServiceException {
    // 获得基金机构关注表的在单位的关注列表
    List<SieInsFocusAgency> focusedAgeList = sieFundFocusAgencyDao.getListByInsId(insId);
    if (focusedAgeList != null && focusedAgeList.size() > 0) {
      for (SieInsFocusAgency agency : focusedAgeList) {
        SieConstFundAgencyView agencyView = sieFundAgencyViewDao.getAgencyById(agency.getAgencyId());
        if (agencyView != null) { // 若机构存在于基金机构表，则继续判断在该机构是否有基金机会
          List<Long> schemeIdList = sieFundSchemeViewDao.getSchemesByAgencyId(agency.getAgencyId());
          if (CollectionUtils.isEmpty(schemeIdList)) { // 机构下没有基金机会，说明在关注表是可删除数据
            sieFundFocusAgencyDao.deleteFocusedAgencyByIdAndInsId(agency.getAgencyId(), insId);
          }
        }
      }
    }
  }

}
