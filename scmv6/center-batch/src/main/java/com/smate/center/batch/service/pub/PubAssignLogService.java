package com.smate.center.batch.service.pub;

import java.util.List;

public interface PubAssignLogService {

  List<Long> getDupPubConfirm(Long zhTitleHash, Long enTitleHash, Long titleHashValue, Integer pubType, String pubYear,
      Long psnId);

  void autoConfirmPubSimple(List<Long> dupPubConfirmIds, Long psnId) throws Exception;
}
