package com.smate.center.task.service.email;

import java.util.List;

public interface GroupEmailService {

  List<Long> getNeedSendMailGrpId(List<Long> grpIds);

  List<Long> getInstGrpPsnId();

  List<Long> getPsnInstGrpIds(Long psnId);

  void sendGrpDnyUpdateEmail(List<Long> sendMailGrp, Long psnId);

}
