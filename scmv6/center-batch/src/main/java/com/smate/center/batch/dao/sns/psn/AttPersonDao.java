package com.smate.center.batch.dao.sns.psn;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.AttPerson;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;

@Repository
public class AttPersonDao extends SnsHibernateDao<AttPerson, Long> {

  public void updatePersonInfo(Person person) throws DaoException {
    try {
      String hql =
          "update AttPerson t set t.refPsnName=?,t.refFirstName=?,t.refLastName=?,t.refHeadUrl=?,t.refTitle=?,t.refInsName=? where t.refPsnId=?";
      super.createQuery(hql, new Object[] {person.getName(), person.getFirstName(), person.getLastName(),
          person.getAvatars(), person.getTitolo(), person.getInsName(), person.getPersonId()}).executeUpdate();
    } catch (Exception e) {

      logger.error("同步关注人员{}冗余数据失败！", person.getPersonId());
      throw new DaoException(e);

    }
  }
}
