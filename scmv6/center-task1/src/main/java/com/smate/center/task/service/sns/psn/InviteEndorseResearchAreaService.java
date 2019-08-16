package com.smate.center.task.service.sns.psn;

import java.util.List;

public interface InviteEndorseResearchAreaService {

  List<Long> getUpdateRAPsnId(int size);

  void sendEmail(Long psnId);

}
