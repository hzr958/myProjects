package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.PubCategoryPatentTemp;

public interface PdwhPubPatentMatchIpcService {

  List<PubCategoryPatentTemp> getList(int batchSize) throws Exception;

  void doHandle(PubCategoryPatentTemp one) throws Exception;
}
