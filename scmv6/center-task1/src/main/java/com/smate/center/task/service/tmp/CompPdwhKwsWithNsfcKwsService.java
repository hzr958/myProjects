package com.smate.center.task.service.tmp;

import java.util.List;

public interface CompPdwhKwsWithNsfcKwsService {

  List<Long> batchGetHasKwsPdwhPubIds(Integer size);

  void startMacthKeywords(Long pdwhPubId) throws Exception;

  void generatePubKeywordHash() throws Exception;

  void updateMatchStatus(Long id, Integer status);

}
