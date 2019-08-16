package com.smate.center.task.single.service.person;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;

/**
 * Person的实现类.
 * 
 * @author LJ
 *
 */
@Service("personService")
@Transactional(rollbackOn = Exception.class)
public class PersonServiceImpl implements PersonService {
  @Autowired
  PersonDao personDao;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 通过PsnId获取人员id,姓名，邮箱语言版本、性别.
   * 
   * @return Person
   */
  @Override
  public Person getPeronsForEmail(long psnId) {
    return personDao.getPeronsForEmail(psnId);
  }

  /**
   * 获取跑数据的人员.
   * 
   * @param size not null
   * 
   */
  @Override
  public List<Long> getPersonList(Long startPsnId, int size) {
    return personDao.findList(startPsnId, size);
  }
}
