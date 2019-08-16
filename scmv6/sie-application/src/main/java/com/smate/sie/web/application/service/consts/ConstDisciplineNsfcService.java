package com.smate.sie.web.application.service.consts;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.web.application.model.consts.ConstDisciplineNsfc;

public interface ConstDisciplineNsfcService {

  public ConstDisciplineNsfc findConstBySub3Dis(String sub3Dis) throws SysServiceException;
}
