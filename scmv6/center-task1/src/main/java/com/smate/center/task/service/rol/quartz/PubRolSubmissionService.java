package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;

public interface PubRolSubmissionService {

  void submitConfirmPub(Long snsPubId, Long insPubId, Long psnId, Long insId) throws ServiceException;

}
