package com.smate.center.task.service.sns.quartz;

import com.smate.center.task.model.rol.quartz.PubAssignSyncMessage;

public interface PubConfirmSyncService {

  Long receiveSyncPub(PubAssignSyncMessage msg);

}
