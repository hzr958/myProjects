package com.smate.sie.web.application.service.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;

@Service("sie6InstitutionService")
@Transactional(rollbackFor = Exception.class)
public class Sie6InstitutionServiceImpl implements Sie6InstitutionService {

  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;

  @Override
  public String findInsPortalByInsName(String insName) throws SysServiceException {
    Sie6Institution ins = sie6InstitutionDao.findInstitutionByInsName(insName);
    if (ins != null) {
      Sie6InsPortal protal = sie6InsPortalDao.get(ins.getId());
      return protal.getDomain();
    }
    return null;
  }

}
