package com.smate.web.management.service.psn;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.management.model.psn.PsnInfoForm;

/**
 * Person的实现类
 * 
 * @author zll
 *
 */
@Service("personService")
@Transactional(rollbackOn = Exception.class)
public class PersonServiceImpl implements PersonService {

  @Autowired
  private PersonDao personDao;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<Person> getAllPsn(PsnInfoForm form) {
    return personDao.getAllPsn(form.getPage(), form.getNameSearchContent(), form.getEmailSearchContent(),
        form.getSearchType());
  }

}
