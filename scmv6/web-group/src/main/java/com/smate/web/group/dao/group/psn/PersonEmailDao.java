package com.smate.web.group.dao.group.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.PersonEmailRegister;


/**
 * 人员邮件数据接口.
 * 
 * @author zx
 */
@Repository
public class PersonEmailDao extends SnsHibernateDao<PersonEmailRegister, Long> {


  /**
   * 检索用户是否已存在具体邮件
   * 
   * @param email
   */
  @SuppressWarnings("unchecked")
  public List<Long> isExitEmailVerify(String email) {
    String hql = "select t.person.personId from PersonEmailRegister t where t.email=:email and t.isVerify=1";
    return super.createQuery(hql).setParameter("email", email).list();
  }

  /**
   * 查找人员的对应的邮箱记录
   * 
   * @param psnId
   * @param email
   * @return
   */
  public PersonEmailRegister getPsnEmail(Long psnId, String email) {
    String hql = "from PersonEmailRegister t where t.person.personId = :psnId and t.email = :email";
    return (PersonEmailRegister) super.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email)
        .uniqueResult();
  }

}
