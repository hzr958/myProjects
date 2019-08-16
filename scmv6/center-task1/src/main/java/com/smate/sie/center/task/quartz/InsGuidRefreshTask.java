package com.smate.sie.center.task.quartz;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.InsGuid;
import com.smate.sie.center.task.service.InstitutionSieService;



/**
 * 单位guid刷新任务.
 * 
 * 
 * 
 */
public class InsGuidRefreshTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());


  private static final int BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private InstitutionSieService institutionSieService;


  public InsGuidRefreshTask() {
    super();
  }

  public InsGuidRefreshTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    int size = BATCH_SIZE;
    try {
      while (true) {
        List<InsGuid> list = this.institutionSieService.getNeedRefresh(size);
        if (list == null || list.size() == 0) {
          return;
        }
        for (InsGuid insGuid : list) {
          try {
            institutionSieService.refreshInsGuid(insGuid);
          } catch (Exception e) {
            logger.error("刷新单位guid任务出现异常", e);
          }
        }
      }
    } catch (Exception e) {
      logger.error("刷新单位guid任务出现异常", e);
    }
  }


}
