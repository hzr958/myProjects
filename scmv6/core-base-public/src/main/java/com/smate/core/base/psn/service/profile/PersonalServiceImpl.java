package com.smate.core.base.psn.service.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.utils.dao.security.PersonDao;

@Service("personalService")
@Transactional(rollbackFor = Exception.class)
public class PersonalServiceImpl implements PersonalService {
  @Autowired
  private PersonDao personDao;

  @Override
  public Personal getPersonal(Long psnId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long getInsIdByPsnId(Long psnId) {
    // TODO Auto-generated method stub
    return null;
  }

}
