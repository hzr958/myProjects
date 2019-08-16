package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TmpPublicationForSnsGroup;

public interface PdwhPubForGroupService {
  List<TmpPublicationForSnsGroup> getPdwhPubInfo(Integer size, Long startPubId, Long endPubId);

  void fetchPubFundingInfo(TmpPublicationForSnsGroup tmpPublicationForSnsGroup) throws Exception;

  void saveStatus(TmpPublicationForSnsGroup tmpPublicationForSnsGroup, Integer status);
}
