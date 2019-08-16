package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PublicationRol;

public interface PublicationRolService {

  PublicationRol getPublicationById(Long insPubId) throws ServiceException;

}
