package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.SiePdwhInsAddrConstRefresh;
import com.smate.sie.center.task.service.SiePdwhInsAddrConstService;

/**
 * 处理单位别名表的后台任务。 每天执行一次，当达到条件时执行操作。
 * 
 * @author 叶星源
 */
public class SieGenPdwhInsAddrConstTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private SiePdwhInsAddrConstService pdwhInsAddrConstService;

  public SieGenPdwhInsAddrConstTask() {
    super();
  }

  public SieGenPdwhInsAddrConstTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }

    while (true) {
      List<SiePdwhInsAddrConstRefresh> PdwhInsAddrConstRefreshList =
          pdwhInsAddrConstService.loadNeedDealSizeData(BATCH_SIZE);
      if (PdwhInsAddrConstRefreshList == null || PdwhInsAddrConstRefreshList.size() == 0) {
        return;
      }
      // 有待处理数据
      for (SiePdwhInsAddrConstRefresh pdwhInsAddrConstRefresh : PdwhInsAddrConstRefreshList) {
        try {
          pdwhInsAddrConstService.refreshInsName(pdwhInsAddrConstRefresh);
        } catch (Exception e) {
          // 处理失败
          logger.error("处理单位别名表的后台定时任务出现异常", e);
          pdwhInsAddrConstRefresh.setRefreshStatus(99);
          this.pdwhInsAddrConstService.saveStRefresh(pdwhInsAddrConstRefresh);

        }
      }
    }

  }

}
