package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.PersonEmail;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 用户邮箱dao
 * 
 * @author ChuanjieHou
 * @date 2017年10月18日
 */
@Repository
public class PersonEmailDao extends SnsHibernateDao<PersonEmail, Long> {

  /**
   * 根据人员id和email查找,并筛选是登录邮件的记录
   * 
   * @author ChuanjieHou
   * @date 2017年10月18日
   * @param psnId
   * @param email
   * @return
   */
  public PersonEmail getByPsnIdAndLoginEmail(Long psnId, String email) {
    String hql =
        "select t from PersonEmail t where t.person.personId=:psnId and t.email=:email and t.loginMail=:isLoginEmail";
    PersonEmail personEmail = (PersonEmail) this.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("email", email).setParameter("isLoginEmail", true).uniqueResult();
    return personEmail;
  }

}
