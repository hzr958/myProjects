package com.smate.center.task.service.sns.pubAssign.handler;

import java.util.List;

import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;

public interface PdwhPubAssignService {

  void deletePubAssign(Long pdwhPubId, Long psnId);

  String getPubDupucheckStatus(Long psnId, Long pdwhPubId);

  void dealpubAssignLogDetail(PubAssginMatchContext context);

  PubAssignLog getPubAssignLog(Long pdwhPubId, Long psnId);

  void removeAllContext(PubAssginMatchContext context);

  void PsnPubCopartnerRcmd(Long psnId, Long pdwhPubId);

  List<PdwhPubAuthorSnsPsnRecord> getPsnRecordByPubId(Long pdwhPubId);

  void savePubPdwhSnsRelation(Long snsPubId, Long pdwhPubId);


}
