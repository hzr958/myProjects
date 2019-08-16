package com.smate.center.open.dao.register;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.register.PersonRegister;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 注册人员基本信息DAO.
 * 
 * @author tsz
 * 
 */

@Repository
public class PersonRegisterDao extends HibernateDao<PersonRegister, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 保存人员信息.
   * 
   * @param person
   * @throws DaoException
   */
  public void savePerson(PersonRegister person) {
    this.save(person);
    // super.getSession().flush();
  }
}
