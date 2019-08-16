package com.smate.center.task.service.statistics;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.ETemplateInfluenceCount;

public interface InfluenceStatisticalService {

  final String mailTemplate = "Influence_Statistical_Template";

  int sendMail(ETemplateInfluenceCount influence) throws ServiceException;

}
