package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.SieKpiInsFundRefresh;
import com.smate.sie.center.task.service.SieClearGrantAgencyService;

/**
 * 单位关注的基金机构和基金机会冗余和缺失数据处理任务
 * 
 * @author lijianming
 *
 */
public class SieSynBpoGrantInfoTask extends TaskAbstract {
  Logger logger = LoggerFactory.getLogger(getClass());

  private static int batchSize = 100;

  @Autowired
  private SieClearGrantAgencyService sieClearGrantAgencyService;

  public SieSynBpoGrantInfoTask() {
    super();
  }

  public SieSynBpoGrantInfoTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException, SysServiceException {
    if (!super.isAllowExecution()) {
      return;
    }
    // 统计bpo的基金机构数和基金机会数及其增减情况
    sieClearGrantAgencyService.StatAgencyGrantAndChangeCondition();
    // 获取表中剩下待处理记录数
    Long count = sieClearGrantAgencyService.countNeedCountInsId();
    // 没有待处理数据，把表中所有记录更新为待处理状态
    if (count.intValue() == 0) {
      // 更新KPI_INS_FUND_REFRESH的表数据
      // sieClearGrantAgencyService.RefreshInsFundTable();
      try {
        // 插入到刷新表的数据
        sieClearGrantAgencyService.insertInsDataToRefreshTable();
        // 删除刷新表的数据
        sieClearGrantAgencyService.deleteInsDataFromRefreshTable();
        // 更新刷新表数据的状态
        sieClearGrantAgencyService.changRefreshDataStatus();
      } catch (Exception e) {
        logger.error("更新KPI_INS_FUND_REFRESH的表数据出错", e);
        throw new ServiceException(e);
      }
    }
    while (true) {
      // 读取需要进行数据处理的insId
      List<SieKpiInsFundRefresh> insIds = sieClearGrantAgencyService.loadNeedHandleDataInsId(batchSize);
      if (insIds == null || insIds.size() == 0) {
        return;
      }
      // 有待处理数据
      for (SieKpiInsFundRefresh refresh : insIds) {
        Long insId = refresh.getInsId();
        // 判断每个单位下的基金机构是否有冗余数据
        try {
          // 处理冗余数据的问题
          sieClearGrantAgencyService.hanleRedundantData(insId);
          // 处理单位基金机构下的基金机会缺少数据的问题
          // sieClearGrantAgencyService.handleAbsentData(insId);
          // 从基金机会业务表获取需要检查的基金机会所属的基金机构Id
          // 处理因基金机会转移到其他基金机构造成基金机会业务表冗余的问题
          sieClearGrantAgencyService.handleFocusedSchemeMovedQue(insId);
          List<Long> agencyIds = sieClearGrantAgencyService.getAgencyIdListFromFocusedScheme(insId);
          // 检查单位的基金机构的基金机会是否有数据缺失
          for (Long agencyId : agencyIds) {
            List<Long> grantIds = sieClearGrantAgencyService.checkSchemeIsAbsence(agencyId, insId);
            // 若存在数据缺失，先根据agencyId删掉基金机会业务表再重新添加数据
            if (grantIds.size() > 0) {
              sieClearGrantAgencyService.deleteSchemeBeforeInsert(insId, agencyId);
              sieClearGrantAgencyService.insertSchemesAfterDelete(insId, agencyId);
            }
          }
          // 检查单位的基金机构关注表是否存在，基金机构下没有基金机会的关注数据
          sieClearGrantAgencyService.handleFocusedAgencyWithoutScheme(insId);
          // 处理成功
          refresh.setStatus(1);
          sieClearGrantAgencyService.saveRefreshData(refresh);
        } catch (Exception e) {
          // 处理失败
          refresh.setStatus(99);
          sieClearGrantAgencyService.saveRefreshData(refresh);
          logger.error("单位处理基金机构和基金机会冗余数据和缺少数据任务出错", e);
        }
      }
    }
  }

}
