package com.smate.center.task.quartz.bdsp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.model.snsbak.bdsp.BdspProposal;
import com.smate.center.task.model.snsbak.bdsp.PubPdwhAddrStandard;
import com.smate.center.task.service.snsbak.quartz.BdspStatisticsAnalysisService;

/**
 * 统计对比分任务
 * 
 * @author zzx
 *
 */
public class BdspStatisticsAnalysisTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 200;
  @Autowired
  private BdspStatisticsAnalysisService bdspStatisticsAnalysisService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    List<BdspProject> list1 = null;
    List<BdspProposal> list2 = null;
    List<PubPdwhAddrStandard> list3 = null;
    boolean status = true;
    Long count = 0L;
    bdspStatisticsAnalysisService.initDelTempData();
    do {
      count++;
      try {
        list1 = bdspStatisticsAnalysisService.findProjectList(batchSize);
        list2 = bdspStatisticsAnalysisService.findProposalList(batchSize);
        list3 = bdspStatisticsAnalysisService.findPaperPatentList(batchSize);
      } catch (Exception e) {
        logger.error("统计对比分析任务 -查询待处理数据 -出错", e);
      }
      if ((list1 == null || list1.size() <= 0) && (list2 == null || list2.size() <= 0)
          && (list3 == null || list3.size() <= 0)) {
        status = false;
        try {
          /**
           * 处理临时数据 1、删除PUB_PDWH_ADDR_STANDARD_TEMP中pubId重复的数据 TODO 新增
           */
          bdspStatisticsAnalysisService.initDealTempData();
          /**
           * 清空结果表 TODO 新增
           */
          bdspStatisticsAnalysisService.initDelOldData();
          /**
           * 统计到结果表 TODO 新增
           */
          bdspStatisticsAnalysisService.dealPaperData();
          bdspStatisticsAnalysisService.dealPatentData();
          bdspStatisticsAnalysisService.dealPrjData();
          bdspStatisticsAnalysisService.dealPrpData();
          bdspStatisticsAnalysisService.dealFundRateData();

        } catch (Exception e) {
          logger.error("BdspStatisticsAnalysisTask跑完任务后，处理任务数据出错！", e);
        }
        try {
          super.closeOneTimeTask();
        } catch (TaskException e) {
          logger.error("关闭BdspStatisticsAnalysisTask出错！", e);
        }
      } else {
        if (list1 != null && list1.size() > 0) {
          for (BdspProject one : list1) {
            try {
              bdspStatisticsAnalysisService.doProjectRegister(one);
            } catch (Exception e) {
              logger.error("统计分析 -项目 -任务处理出错", e);
            }
          }
        }
        if (list2 != null && list2.size() > 0) {
          for (BdspProposal one : list2) {
            try {
              bdspStatisticsAnalysisService.doProposalRegister(one);
            } catch (Exception e) {
              logger.error("统计分析 -申请书 -任务处理出错", e);
            }
          }
        }
        if (list3 != null && list3.size() > 0) {
          for (PubPdwhAddrStandard one : list3) {
            try {
              bdspStatisticsAnalysisService.doPaperPatentRegister(one);
            } catch (Exception e) {
              logger.error("统计分析 -论文和专利-任务处理出错", e);
            }
          }
        }
      }
    } while (status && count < 777777);
  }

  public BdspStatisticsAnalysisTask() {
    super();
  }

  public BdspStatisticsAnalysisTask(String beanName) {
    super(beanName);
  }
}
