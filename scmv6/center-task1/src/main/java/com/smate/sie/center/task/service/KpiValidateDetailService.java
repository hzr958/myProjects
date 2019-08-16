package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.core.base.utils.model.validate.KpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.KpiValidateLog;

/**
 * 
 * @author ztg
 *
 */
public interface KpiValidateDetailService {

  List<KpiValidateDetail> getByUUID(String uuId);

  void doValidate(KpiValidateDetail kpiVdDetail);

  Long countNeedHandleKeyId(String uuId);

  Long countStatusIsNull(String uuId);

  void saveKpiValidateDetail(KpiValidateDetail kpiVdDetail);

  void saveKpiValidateLog(KpiValidateLog kpiVdLog);

}
