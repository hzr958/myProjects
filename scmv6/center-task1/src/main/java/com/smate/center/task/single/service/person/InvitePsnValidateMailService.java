package com.smate.center.task.single.service.person;

import java.util.List;

import com.smate.center.task.model.sns.psn.InvitePsnValidate;

public interface InvitePsnValidateMailService {

  List<InvitePsnValidate> getInvitePsnValidate();

  void updateSendStatus(Long psnId, Integer result);

  Integer sendInviteEmailToPsn(InvitePsnValidate invitePsnValidate);

}
