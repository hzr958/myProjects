package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.ImportThirdPsns;
import com.smate.sie.center.task.service.ImportThirdPsnsHistoryService;
import com.smate.sie.center.task.service.ImportThirdPsnsService;
import com.smate.sie.center.task.service.InsPersonService;

/**
 * 导入第三方人员信息任务.
 * 
 * @author xys
 *
 */
public class SieImportThirdPsnsTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 500;// 一次最多处理数量

  @Autowired
  private ImportThirdPsnsService importThirdPsnsService;
  @Autowired
  private ImportThirdPsnsHistoryService importThirdPsnsHistoryService;
  @Autowired
  private InsPersonService insPersonService;

  public SieImportThirdPsnsTask() {

  }

  public SieImportThirdPsnsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      while (true) {
        List<ImportThirdPsns> list = this.importThirdPsnsService.getThirdPsnsNeedImport(BATCH_SIZE);
        if (list == null || list.size() == 0) {
          return;
        }
        for (ImportThirdPsns importThirdPsns : list) {
          try {
            this.insPersonService.refreshInsPsn(importThirdPsns);
            importThirdPsns.setStatus(1);
            this.importThirdPsnsService.saveImportThirdPsns(importThirdPsns);
            this.importThirdPsnsHistoryService.saveImportThirdPsnsHistory(importThirdPsns);
          } catch (Exception e) {
            importThirdPsns.setStatus(9);
            this.importThirdPsnsService.saveImportThirdPsns(importThirdPsns);
            this.importThirdPsnsHistoryService.saveImportThirdPsnsHistory(importThirdPsns);
            logger.error("刷新单位人员信息出错了喔,insId:{},email:{},zhName:{}", new Object[] {importThirdPsns.getPk().getInsId(),
                importThirdPsns.getPk().getEmail(), importThirdPsns.getZhName(), e});
          }
        }
      }
    } catch (Exception e) {
      logger.error("刷新单位人员信息出错了喔", e);
    }
  }
}
