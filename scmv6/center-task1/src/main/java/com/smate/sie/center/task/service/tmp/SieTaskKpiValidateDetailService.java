package com.smate.sie.center.task.service.tmp;

import java.util.List;

import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateDetail;

/**
 * TASK_KPI_VALIDATE_DETAIL 表处理
 * 
 * @author ztg
 *
 */
public interface SieTaskKpiValidateDetailService {

  Long getAllCountByUUID(String uuId);

  List<SieTaskKpiValidateDetail> getByUUID(String uuId);

  void doSplit(SieTaskKpiValidateDetail kpiVdDetail) throws Exception;

  Long getSuccessCountByUUID(String uuId);

  Long getErrorCountByUUID(String uuId);

  void saveErrorForDetail(SieTaskKpiValidateDetail kpiVdDetail);
}
