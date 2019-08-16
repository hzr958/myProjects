package com.smate.center.batch.dao.sns.psn.register;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.psn.register.PersonEmailRegister;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 用户邮箱
 * 
 * @author tsz
 *
 */
@Repository
public class PersonEmailRegisterDao extends SnsHibernateDao<PersonEmailRegister, Long> {

  public PersonEmailRegister findByPsnIdAndEmail(Long psnId, String email) {
    String hql = "from PersonEmailRegister t where t.psnId =:psnId and t.email=:email";
    List list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email).list();
    if (list != null && list.size() > 0) {
      return (PersonEmailRegister) list.get(0);
    }
    return null;
  }

}
