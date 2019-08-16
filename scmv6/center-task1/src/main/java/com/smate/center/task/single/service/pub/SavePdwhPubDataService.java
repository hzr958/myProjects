package com.smate.center.task.single.service.pub;

import java.util.List;

import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelationHis;

public interface SavePdwhPubDataService {

  List<OriginalPdwhPubRelation> getHandleData();

  void handleOriginalPubData(OriginalPdwhPubRelation originalPdwhPubRelation);

  void saveHandleResult(OriginalPdwhPubRelation originalPdwhPubRelation);

  List<OriginalPdwhPubRelation> getRemoveData();

  void saveOriginalPdwhPubRelationHis(OriginalPdwhPubRelationHis relationHis);

  void deleteOriginalPdwhPubRelation(OriginalPdwhPubRelation originalPdwhPubRelation);

}
