package com.smate.center.task.single.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.pdwh.pub.DbCacheCfetch;

public interface DbCacheCfetchService {

  List<DbCacheCfetch> getTohandleList(int batchSize);

  Long saveOriginalPdwhPubRelation(Map<String, Object> pubData, long createpsnId, Long crossrefId);

  void saveError(Long crossrefId, String message);

  void saveSuccess(Long crossrefId);

  void handleCrossrefData(Long originalId);


}
