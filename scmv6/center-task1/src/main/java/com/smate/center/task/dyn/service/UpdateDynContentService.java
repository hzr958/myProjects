package com.smate.center.task.dyn.service;

import java.util.List;

import com.smate.center.task.dyn.model.base.DynContentUpdateStatus;

public interface UpdateDynContentService {

  List<DynContentUpdateStatus> getDynMsgInfo(int batchSize) throws Exception;

  void updateDynContentById(DynContentUpdateStatus dynId) throws Exception;

  boolean getTaskStatus();


}
