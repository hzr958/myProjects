package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

public interface InitPdwhPubCleanDoiHashService {

  void startProcessing(Long pubId);

  List<Long> batchGetPubIdList(Integer size);

}
