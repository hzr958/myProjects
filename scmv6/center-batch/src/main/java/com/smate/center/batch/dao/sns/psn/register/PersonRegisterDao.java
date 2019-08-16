package com.smate.center.batch.dao.sns.psn.register;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员注册
 * 
 * @author tsz
 *
 */
@Repository
public class PersonRegisterDao extends SnsHibernateDao<PersonRegister, Long> {


  /**
   * 保存人员信息.
   * 
   * @param person
   * @throws DaoException
   */
  public void savePerson(PersonRegister person) {
    this.save(person);
    super.getSession().flush();
  }
}
