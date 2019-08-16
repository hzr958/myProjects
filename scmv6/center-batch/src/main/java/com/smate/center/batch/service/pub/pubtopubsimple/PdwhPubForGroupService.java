package com.smate.center.batch.service.pub.pubtopubsimple;

import java.util.List;

import com.smate.center.batch.model.tmp.pdwh.TmpPublicationForSnsGroup;

public interface PdwhPubForGroupService {
  List<TmpPublicationForSnsGroup> getPdwhPubInfo(Integer size, Long startPubId, Long endPubId);

  void fetchPubFundingInfo(TmpPublicationForSnsGroup tmpPublicationForSnsGroup) throws Exception;

  void saveStatus(TmpPublicationForSnsGroup tmpPublicationForSnsGroup, Integer status);
}
