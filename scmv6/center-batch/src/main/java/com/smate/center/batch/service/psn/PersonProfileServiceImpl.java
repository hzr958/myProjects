package com.smate.center.batch.service.psn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.model.security.Person;

@Service("personProfileService")
@Transactional(rollbackFor = Exception.class)
public class PersonProfileServiceImpl implements PersonProfileService {
  @Autowired
  private PersonProfileDao personProfileDao;

  @Override
  public Person getpsnInfo(Long psnId) {
    return personProfileDao.get(psnId);
  }
}
