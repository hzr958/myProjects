package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.SieKpiInsFundRefresh;

/**
 * sieClearGrantAgencyTask相关应用层
 * 
 * @author lijianming
 *
 */
public interface SieClearGrantAgencyService {
  // 更新KPI_INS_FUND_REFRESH的表数据
  public void RefreshInsFundTable() throws SysServiceException;

  // 插入单位数据到KPI_INS_FUND_REFRESH的表
  public void insertInsDataToRefreshTable() throws SysServiceException;

  // 在KPI_INS_FUND_REFRESH表删除不必要的单位数据
  public void deleteInsDataFromRefreshTable() throws SysServiceException;

  // 更新KPI_INS_FUND_REFRESH表数据状态
  public void changRefreshDataStatus() throws SysServiceException;

  // 统计bpo的基金机构数和基金机会数及变化情况
  public String StatAgencyGrantAndChangeCondition() throws SysServiceException;

  // 统计需要统计的单位
  public Long countNeedCountInsId() throws SysServiceException;

  // 获取需要进行数据处理的单位信息
  public List<SieKpiInsFundRefresh> loadNeedHandleDataInsId(int batchSize) throws SysServiceException;

  // 处理基金业务表的冗余数据
  public void hanleRedundantData(Long insId) throws SysServiceException;

  // 保存刷新表的数据
  public void saveRefreshData(SieKpiInsFundRefresh sieKpiInsFundRefresh) throws SysServiceException;

  // 处理单位基金机构下的基金机会缺少数据问题
  public void handleAbsentData(Long insId) throws SysServiceException;

  // 从基金机会业务表获取基金机构Id列表
  public List<Long> getAgencyIdListFromFocusedScheme(Long insId) throws SysServiceException;

  // 检查单位的基金机构的基金机会是否有数据缺失
  public List<Long> checkSchemeIsAbsence(Long agencyId, Long insId) throws SysServiceException;

  // 在插入数据前，先清空该单位基金机构下的基金机会业务表的数据
  public void deleteSchemeBeforeInsert(Long insId, Long agencyId) throws SysServiceException;

  // 插入最新的该单位基金机构下的基金机会的最新数据到业务表
  public void insertSchemesAfterDelete(Long insId, Long agencyId) throws SysServiceException;

  // 处理因基金机会转移到其他基金机构造成基金机会业务表冗余的问题
  public void handleFocusedSchemeMovedQue(Long insId) throws SysServiceException;

  // 处理关注表中基金机构下没有基金机会的数据
  public void handleFocusedAgencyWithoutScheme(Long insId) throws SysServiceException;
}
