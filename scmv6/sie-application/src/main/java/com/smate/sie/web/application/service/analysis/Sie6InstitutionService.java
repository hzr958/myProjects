package com.smate.sie.web.application.service.analysis;

import com.smate.core.base.utils.exception.SysServiceException;

public interface Sie6InstitutionService {

  public String findInsPortalByInsName(String insName) throws SysServiceException;
}
