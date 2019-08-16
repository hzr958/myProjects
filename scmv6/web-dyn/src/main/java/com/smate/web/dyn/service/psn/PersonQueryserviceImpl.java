package com.smate.web.dyn.service.psn;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.model.security.Person;

/**
 * 人员查询服务类
 * 
 * @author zk
 *
 */
@Service("personQueryservice")
@Transactional(rollbackOn = Exception.class)
public class PersonQueryserviceImpl implements PersonQueryservice {

  @Autowired
  private PersonDao personDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;


  @Override
  public Person findPersonInsAndPos(Long psnId) throws DynException {
    return personDao.findPersonInsAndPos(psnId);
  }

  @Override
  public Person findPsnName(Long psnId) throws DynException {
    return personDao.getPersonName(psnId);
  }

  @Override
  public Person findPerson(Long psnId) throws DynException {
    return personDao.findPerson(psnId);
  }

  @Override
  public String getPsnName(Person person, String locale) {
    String psnName = "";
    if ("zh_CN".equals(locale)) {
      if (StringUtils.isNotBlank(person.getName())) {
        psnName = person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        psnName = person.getFirstName() + " " + person.getLastName();
      } else {
        psnName = person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        psnName = person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        psnName = person.getFirstName() + " " + person.getLastName();
      } else {
        psnName = person.getName();
      }
    }
    return psnName;
  }

  @Override
  public Person findPersonBase(Long psnId) throws DynException {
    return personDao.findPersonBase(psnId);
  }

  @Override
  public String findAvatarsById(Long psnId) throws DynException {
    return personDao.getPsnImgByObjectId(psnId);
  }
}
