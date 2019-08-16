package com.smate.center.merge.dao.person;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.person.Person;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * personDao.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Repository
public class PersonDao extends SnsHibernateDao<Person, Long> {
  /**
   * 发送邮件用人员信息
   * 
   * @param psnId
   * @return
   */
  public Person findPsnInfoForEmail(Long psnId) {
    String hql = "from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 通过email查询psnId
   * 
   * @param email
   * @return
   */
  public Long findPsnIdByEmail(String email) {
    String hql = "select t.personId from Person t where t.email=:email";
    List<Long> list = this.createQuery(hql).setParameter("email", email).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
