package com.smate.sie.web.application.service.consts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.web.application.dao.consts.ConstDisciplineNsfcDao;
import com.smate.sie.web.application.model.consts.ConstDisciplineNsfc;

@Service("constDisciplineNsfcService")
@Transactional(rollbackFor = Exception.class)
public class ConstDisciplineNsfcServiceImpl implements ConstDisciplineNsfcService {

  @Autowired
  private ConstDisciplineNsfcDao constDisciplineNsfcDao;

  @Override
  public ConstDisciplineNsfc findConstBySub3Dis(String sub3Dis) throws SysServiceException {
    return constDisciplineNsfcDao.findConstBySub3Dis(sub3Dis);
  }

}
