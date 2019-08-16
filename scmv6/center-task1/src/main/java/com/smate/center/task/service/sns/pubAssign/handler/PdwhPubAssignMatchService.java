package com.smate.center.task.service.sns.pubAssign.handler;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;

public interface PdwhPubAssignMatchService {
  String handler(PubAssginMatchContext context, PubAssignLog assignLog) throws ServiceException;
}
