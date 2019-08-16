package com.smate.center.batch.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;

@Service("personService")
@Transactional(rollbackFor = Exception.class)
public class PersonServiceImpl implements PersonService {

  /**
   * 
   */
  private static final long serialVersionUID = -9152113787948491839L;

  @Autowired
  PersonDao personDao;

  @Override
  public Person getPeronsForEmail(Long psnId) throws ServiceException {
    Person person = personDao.getPeronsForEmail(psnId);
    return person;
  }

  @Override
  public String getEmailLanguByPsnId(Long psnId) throws ServiceException {
    String lang = personDao.getEmailLanguByPsnId(psnId);
    return lang;
  }

  @Override
  public String getLangByPsnId(Long psnId) throws ServiceException {
    String lang = personDao.getLangByPsnId(psnId);
    return lang;

  }
}
