package com.smate.center.task.service.rol.quartz;

public interface PubRolSubmissionStatService {

  void refreshPubRolSubmissionStat(Long insId);

  void refreshPubRolSubmissionStat(Long insId, Long psnId);

}
