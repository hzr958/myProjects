package com.smate.center.task.service.group;

import java.util.List;

import com.smate.center.task.model.grp.GrpBaseinfo;

public interface SendGrpPubRcmdMailService {

  List<GrpBaseinfo> getNeedSendMailJzData(Long grpId, Integer size);

  List<GrpBaseinfo> getNeedSendMailJtData(Long grpId, Integer size);

  void sendMailToGroupOwner(GrpBaseinfo jtGrpBaseInfo);

}
