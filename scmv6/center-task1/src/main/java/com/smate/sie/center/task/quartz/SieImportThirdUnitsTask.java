package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.ImportThirdUnits;
import com.smate.sie.center.task.service.ImportThirdUnitsHistoryService;
import com.smate.sie.center.task.service.ImportThirdUnitsService;
import com.smate.sie.center.task.service.Sie6InsUnitService;

/**
 * 导入第三方部门信息任务.
 * 
 * @author xys
 *
 */
public class SieImportThirdUnitsTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 500;// 一次最多处理数量

  @Autowired
  private ImportThirdUnitsService importThirdUnitsService;
  @Autowired
  private ImportThirdUnitsHistoryService importThirdUnitsHistoryService;
  @Autowired
  private Sie6InsUnitService sie6InsUnitService;

  public SieImportThirdUnitsTask() {

  }

  public SieImportThirdUnitsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      while (true) {
        List<ImportThirdUnits> list = this.importThirdUnitsService.getThirdUnitsNeedImport(BATCH_SIZE);
        if (list == null || list.size() == 0) {
          return;
        }
        for (ImportThirdUnits importThirdUnits : list) {
          try {
            this.sie6InsUnitService.refreshInsUnit(importThirdUnits);
            importThirdUnits.setStatus(1);
            this.importThirdUnitsService.saveImportThirdUnits(importThirdUnits);
            this.importThirdUnitsHistoryService.saveImportThirdUnitsHistory(importThirdUnits);
          } catch (Exception e) {
            importThirdUnits.setStatus(9);
            this.importThirdUnitsService.saveImportThirdUnits(importThirdUnits);
            this.importThirdUnitsHistoryService.saveImportThirdUnitsHistory(importThirdUnits);
            logger.error("刷新单位部门信息出错了喔,insId:{},unitId:{},unitName:{},pid:{}",
                new Object[] {importThirdUnits.getPk().getInsId(), importThirdUnits.getPk().getUnitId(),
                    importThirdUnits.getZhName(), importThirdUnits.getSuperUnitId(), e});
          }
        }
      }
    } catch (Exception e) {
      logger.error("刷新单位部门信息出错了喔", e);
    }
  }
}
