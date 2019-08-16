package com.smate.sie.center.task.quartz;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.SieDataSrvPatTmpRefresh;
import com.smate.sie.center.task.service.SieDataSrvPatTmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 数据服务：生成单位专利数据 。注意：每次调用之前必须先手动清空表DATA_SRV_PAT_TMP。
 * 
 * @author 叶星源
 * @Date 20180911
 */
public class SieDataSrvPatTmpTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private SieDataSrvPatTmpService sieDataSrvPatTmpService;

  public SieDataSrvPatTmpTask() {
    super();
  }

  public SieDataSrvPatTmpTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      while (true) {
        // 获取需要更新的数据
        List<SieDataSrvPatTmpRefresh> list = this.sieDataSrvPatTmpService.getNeedRefreshData(BATCH_SIZE);
        if (list == null || list.size() == 0) {
          return;
        }
        for (SieDataSrvPatTmpRefresh sieDataSrvPatTmpRefresh : list) {
          try {
            sieDataSrvPatTmpService.refreshData(sieDataSrvPatTmpRefresh);
          } catch (Exception e) {
            logger.error("数据服务：生成单位专利数据失败：", e);
            sieDataSrvPatTmpService.saveSieDataSrvPatTmpRefresh(sieDataSrvPatTmpRefresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("数据服务：生成单位专利数据错误：", e);
    }

  }
}
