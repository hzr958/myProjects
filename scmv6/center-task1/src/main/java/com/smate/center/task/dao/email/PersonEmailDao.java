package com.smate.center.task.dao.email;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.email.PersonEmail;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人电子邮件Dao
 * 
 * @author zk
 *
 */
@Repository
public class PersonEmailDao extends SnsHibernateDao<PersonEmail, Long> {

  /**
   * 判断邮件是否验证过
   * 
   * @param psnId
   * @param email
   * @return
   */
  public boolean checkIsVerify(Long psnId, String email) {
    String hql =
        "select new PersonEmail(p.psnId,p.email) from PersonEmail p where p.psnId =:psnId and p.email = :email and p.isVerify = 1";
    Object pe = super.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email).uniqueResult();
    return pe == null ? false : true;
  }

  /**
   * 获取personemail
   * 
   * @param psnId
   * @param email
   * @return
   */
  public PersonEmail getPsnEmailForVerify(Long psnId, String email) {
    String hql = " from PersonEmail p where p.psnId =:psnId and p.email = :email";
    return (PersonEmail) super.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findPsnIdByEmail(String email) {
    String hql = "select psnId from PersonEmail  where  email = :email";
    return super.createQuery(hql).setParameter("email", email).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getPsnEmails(Long psnId) {
    String hql = "select p.email from PersonEmail p where p.psnId =:psnId ";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
