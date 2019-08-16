package com.smate.sie.center.task.service.tmp;

import java.util.List;

import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateMain;

/**
 * TASK_KPI_VALIDATE_MAIN 表处理
 * 
 * @author ztg
 *
 */
public interface SieTaskKpiValidateMainService {

  Long countNeedHandleKeyId();

  List<SieTaskKpiValidateMain> loadNeedHandleKeyId(int batchSize);

  void doSplit(SieTaskKpiValidateMain tmpkpiVdMain);

  void saveTmpMain(SieTaskKpiValidateMain tmpkpiVdMain);

}
