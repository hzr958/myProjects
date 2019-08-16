package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;


public interface KpiValidateMainService {

  Long countNeedHandleKeyId();

  List<KpiValidateMain> loadNeedHandleKeyId(int maxSize);

  void updateKpiValidateMain(KpiValidateMain kpiVdMain);

}
