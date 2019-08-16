package com.smate.center.task.service.publicpub;

import java.util.List;

public interface HandlePubRelationService {

  List<Long> getPdwhPubIds(Long lastPubId, Integer size);

  void deleteErrorData(Long pdwhPubId);

}
