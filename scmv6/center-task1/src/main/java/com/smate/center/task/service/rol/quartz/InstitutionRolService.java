package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.InstitutionRol;

public interface InstitutionRolService {

  InstitutionRol getInstitution(Long insId) throws ServiceException;

}
