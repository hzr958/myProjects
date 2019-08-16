package com.smate.center.task.service.group;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.group.PrjGrpTmp;

public interface SendPrjGrpEmailService {

  List<PrjGrpTmp> getPrjGrpInfo(int size);

  Map<String, Object> buildEamilInfo(PrjGrpTmp prjGrpTmp, Map<String, Object> params);

  void saveOptResult(Long grpId, int result);

  Long getCountGroupPub(Long grpId);

}
