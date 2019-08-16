package com.smate.center.open.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.profile.PersonEmail;
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

  /**
   * 根据人员id和email查找,筛选除了传入的email是登录邮件或首要邮件的记录
   * 
   * @author hd
   * @date 2018年4月25日
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PersonEmail> getByPsnIdAndLoginEmailAndFirstEmail(Long psnId, String email) {
    String hql =
        "select t from PersonEmail t where t.person.personId=:psnId  and t.email != :email and (t.loginMail=:isLoginEmail or t.firstMail=:isFirstMail)";
    List<PersonEmail> list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email)
        .setParameter("isLoginEmail", true).setParameter("isFirstMail", true).list();
    return list;
  }

  /**
   * 根据人员id和email查找记录
   * 
   * @author hd
   * @date 2018年4月25日
   * @param psnId
   * @param email
   * @return
   */
  public PersonEmail getByPsnId(Long psnId, String email) {
    String hql = "select t from PersonEmail t where t.person.personId=:psnId and t.email=:email";
    PersonEmail personEmail =
        (PersonEmail) this.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email).uniqueResult();
    return personEmail;
  }

  /**
   * 邮箱是否已被使用
   * 
   * @param psnId
   * @param email
   * @return
   */
  public Boolean emailHasOtherUsed(Long psnId, String email) {
    String hql =
        " from PersonEmail t where t.person.personId <> :psnId and (  t.firstMail =1 or t.loginMail =1) and t.email =:email";
    List list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 设置邮件为首要邮件/登录帐号.
   * 
   * @param mailId
   * @throws DaoException
   */
  public void setFirstLoginMail(Long mailId) {
    // 把这个人其他账号取消首要邮件
    this.createQuery(
        "update PersonEmail set firstMail = 0,loginMail = 0 where person.personId in(select person.personId from PersonEmail where id = ? )",
        mailId).executeUpdate();
  }

  /**
   * 设置首要邮件.
   * 
   * @param mailId
   * @throws DaoException
   */
  public void setFirstMail(Long mailId) {
    this.createQuery(
        "update PersonEmail set firstMail = 0 where person.personId in(select person.personId from PersonEmail where id = ? )",
        mailId).executeUpdate();
  }

}
