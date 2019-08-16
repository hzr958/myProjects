package com.smate.center.task.single.service.pub;

import java.util.List;

import com.smate.center.task.model.snsbak.PatentHisData;

public interface PatentHisDataService {

  void HandlePatentHisData(PatentHisData patData);

  List<PatentHisData> getPatList(Integer size);

}
