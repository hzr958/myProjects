package com.smate.web.psn.dao.profile;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;

/**
 * 人员邮件数据接口.
 * 
 * @author zx
 */
@Repository
public class PersonEmailDao extends SnsHibernateDao<PersonEmailRegister, Long> {

  /**
   * 根据指定人员的首要邮件.
   * 
   * @param personId
   * @return
   * @throws DaoException
   */
  public String getfirstMail(Long personId) throws DaoException {
    String hql = "select t.email from PersonEmailRegister t where t.person.personId = :personId and t.firstMail=1";
    return (String) super.createQuery(hql.toString()).setParameter("personId", personId).uniqueResult();
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

  /**
   * 清除人员的首要邮箱（将isFirstEmail设为0）
   * 
   * @param psnId
   */
  public void clearFirstEmail(Long psnId) {
    this.createQuery("update PersonEmailRegister t set t.firstMail = 0 where t.person.personId = :psnId")
        .setParameter("psnId", psnId).executeUpdate();
  }

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
   * 检索用户是否已存在具体邮件.
   * 
   * @param psnId
   * @param email
   * @return
   * @throws DaoException
   */
  public Boolean isEmailExit(Long psnId, String email) throws DaoException {
    Long count = super.findUnique("select count(1) from PersonEmailRegister where person.personId = ? and email = ? ",
        psnId, email);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 检索邮件是否被他人使用，排除当前用户
   * 
   * @param psnId
   * @param email
   * @return
   * @throws DaoException
   */
  public List<Long> findPsnIdByEmail(String email) throws DaoException {
    String hql = "select person.personId  from PersonEmailRegister where  email =:email ";
    List list = this.createQuery(hql).setParameter("email", email).list();
    return list;
  }

  /**
   * 设置邮件为首要邮件/登录帐号.
   * 
   * @param mailId
   * @throws DaoException
   */
  public void setFirstLoginMail(Long mailId) throws DaoException {
    // 把这个人其他账号取消首要邮件
    this.createQuery(
        "update PersonEmailRegister set firstMail = 0,loginMail = 0 where person.personId in(select person.personId from PersonEmailRegister where id = ? )",
        mailId).executeUpdate();
    PersonEmailRegister psnEmail = this.get(mailId);
    String[] emailPart = psnEmail.getEmail().split("@");
    psnEmail.setLeftPart(emailPart[0]);
    psnEmail.setRightPart(emailPart[1]);
    psnEmail.setFirstMail(1L);
    psnEmail.setLoginMail(1L);
    super.save(psnEmail);

  }

  /**
   * 设置首要邮件.
   * 
   * @param mailId
   * @throws DaoException
   */
  public void setFirstMail(Long mailId) throws DaoException {

    this.createQuery(
        "update PersonEmailRegister set firstMail = 0 where person.personId in(select person.personId from PersonEmailRegister where id = ? )",
        mailId).executeUpdate();

    // PersonEmail psnEmail = this.get(mailId);
    // String[] emailPart = psnEmail.getEmail().split("@");
    // psnEmail.setLeftPart(emailPart[0]);
    // psnEmail.setRightPart(emailPart[1]);
    // psnEmail.setFirstMail(true);
    // super.save(psnEmail);
    this.createQuery("update PersonEmailRegister set firstMail = 1 where id=?", mailId).executeUpdate();
  }

  /**
   * 根据人员编码获取人员邮件.
   * 
   * @param personId
   * @return List<PersonEmail>
   * @throws DaoException
   */
  public List<PersonEmailRegister> findListByPersonId(Long personId) throws DaoException {

    return this.find(
        "from PersonEmailRegister where person.personId = ? order by   loginMail desc   nulls last , id asc ",
        new Object[] {personId});
  }

  public int deletePersonEmail(Long mailId) {

    String sql = " delete  from PSN_EMAIL   where id= ? ";
    SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
    sqlQuery.setParameter(0, mailId);

    return sqlQuery.executeUpdate();
  }

  /**
   * 清除人员的首要邮箱（将isFirstEmail设为0） 登录邮箱
   * 
   * @param psnId
   */
  public void clearFirstLoginEmail(Long psnId) {
    this.createQuery(
        "update PersonEmailRegister t set t.firstMail = 0   ,  t.loginMail = 0  where t.person.personId = :psnId")
        .setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 更新确认邮件
   * 
   * @param psnId
   * @param email
   * @param status
   */
  public void updateValidateEmailState(Long psnId, String email, Long status) {
    this.createQuery(
        "update PersonEmailRegister t set  t.isVerify=:status    where t.person.personId = :psnId  and t.email =:email  ")
        .setParameter("psnId", psnId).setParameter("email", email).setParameter("status", status).executeUpdate();
  }

  public Boolean emailHasOtherUsed(Long psnId, String email) {
    String hql = " from PersonEmailRegister t where   ( t.person.personId > :psnId or  t.person.personId < :psnId  ) "
        + " and (  t.firstMail =1 or t.loginMail =1) and t.email =:email";
    List list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("email", email).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }
}
