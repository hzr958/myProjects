package com.smate.center.task.service.tmp;

import java.util.List;

import com.smate.center.task.model.sns.pub.ConstRegion;

public interface BaiduMapGetInsAddsService {

  List<Long> batchGetProcessedData(Integer size);

  void startProcessing(Long id, List<ConstRegion> cnZhname, List<ConstRegion> allName) throws Exception;

  void updateStatusById(Long id, int i);

  List<ConstRegion> getAllCNZhname();

  List<ConstRegion> getAllName();

}
