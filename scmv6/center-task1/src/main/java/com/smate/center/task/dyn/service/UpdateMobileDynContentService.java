package com.smate.center.task.dyn.service;

import java.util.List;

import com.smate.center.task.dyn.model.base.MobileDynContentUpdate;

public interface UpdateMobileDynContentService {

  List<MobileDynContentUpdate> getDynMsgInfo(int batchSize) throws Exception;

  void updateMobileDynContentById(MobileDynContentUpdate dynId) throws Exception;

  boolean getTaskStatus();

}
