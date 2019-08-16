package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.center.task.v8pub.sns.po.PubStatisticsPO;

public interface PubPdwhSnsRelationService {

  List<PubStatisticsPO> getPubStatisticsPOList(Long pdwhPubId);
}
