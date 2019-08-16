package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.service.sns.quartz.PsnKwRmcService;
import com.smate.center.task.service.sns.quartz.PsnKwRmcTmpService;

/**
 * 个人特征关键词任务
 * 
 * @author zjh
 *
 */
public class PsnKwRmcTask extends TaskAbstract {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnKwRmcService psnKwRmcService;
  @Autowired
  private PsnKwRmcTmpService psnKwRmcTmpService;

  public PsnKwRmcTask() {
    super();
  }

  public PsnKwRmcTask(String beanName) {
    super(beanName);
  }

  public void run() {
    try {
      logger.info("执行个人特征关键词任务");
      if (!super.isAllowExecution()) {
        return;
      }
      if (psnKwRmcService.getRefreshFlag() == 1) {
        while (true) {
          List<Long> psnKwRmcRefreshList = psnKwRmcService.getRefreshData();
          if (CollectionUtils.isEmpty(psnKwRmcRefreshList)) {
            return;
          }
          for (Long psnId : psnKwRmcRefreshList) {
            try {
              psnKwRmcService.deleteFromTmp();
              // 把数据放到临时表中
              psnKwRmcTmpService.HandlePsnKwRmcTmp(psnId);
              // HandlePsnKwRcmdTmp(psnId);
              // 统计psn_kw_rmc_tmp中的关键词信息，并计算得分
              psnKwRmcService.handleKwRcmdScore(psnId); // 反推热词
              psnKwRmcService.ItoratorZhKw(psnId);
              psnKwRmcService.ItoratorEnKw(psnId);
              psnKwRmcService.HandlesupplementPsnKwRmc(psnId);
              psnKwRmcService.handlePsnKwRmc(psnId);
            } catch (Exception e) {
              psnKwRmcService.updateRefreshData(psnId);
              logger.error("PsnKwRmcTask 人员关键词任务处理出错，psnId" + psnId, e);
            }

          }
        }

      }
    } catch (Exception e) {
      logger.error("PsnKwEptTask 人员关键词任务处理出错", e);
    }

  }

}
