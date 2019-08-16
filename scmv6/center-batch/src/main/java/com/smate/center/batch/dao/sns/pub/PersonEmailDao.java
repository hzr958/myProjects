package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PersonEmail;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员邮件数据接口.
 * 
 * @author zt
 * 
 */
@Repository
public class PersonEmailDao extends SnsHibernateDao<PersonEmail, Long> {

  /**
   * 根据人员编码获取人员邮件.
   * 
   * @param personId
   * @return List<PersonEmail>
   * @throws DaoException
   */
  public List<PersonEmail> findListByPersonId(Long personId) throws DaoException {

    return this.find("from PersonEmail where psnId = ?", new Object[] {personId});
  }

  /**
   * 根据指定人员的首要邮件.
   * 
   * @param personId
   * @return
   * @throws DaoException
   */
  public String getfirstMail(Long personId) throws DaoException {
    return this.findUnique("select email from PersonEmail where psnId = ? and firstMail=1", personId);
  }

  /**
   * 根据指定人员的首要邮件.
   * 
   * @param personId
   * @return
   * @throws DaoException
   */
  public PersonEmail getfirstPersonEmail(Long personId) throws DaoException {
    return this.findUnique("from PersonEmail where psnId = ? and firstMail=1", personId);
  }

  /**
   * 取得指定人员未确认的首要邮件.
   * 
   * @param personId
   * @return
   * @throws DaoException
   */
  public PersonEmail getFirstAndNotVerifyPersonEmail(Long personId) throws DaoException {
    return this.findUnique("from PersonEmail where psnId = ? and firstMail=1 and isVerify=0", personId);
  }

  /**
   * 设置首要邮件.
   * 
   * @param mailId
   * @throws DaoException
   */
  public void setFirstMail(Long mailId) throws DaoException {

    this.createQuery("update PersonEmail set firstMail = 0 where psnId in(select psnId from PersonEmail where id = ? )",
        mailId).executeUpdate();

    // PersonEmail psnEmail = this.get(mailId);
    // String[] emailPart = psnEmail.getEmail().split("@");
    // psnEmail.setLeftPart(emailPart[0]);
    // psnEmail.setRightPart(emailPart[1]);
    // psnEmail.setFirstMail(true);
    // super.save(psnEmail);
    this.createQuery("update PersonEmail set firstMail = 1 where id=?", mailId).executeUpdate();
  }

  /**
   * 设置邮件为首要邮件/登录帐号.
   * 
   * @param mailId
   * @throws DaoException
   */
  public void setFirstLoginMail(Long mailId) throws DaoException {

    this.createQuery(
        "update PersonEmail set firstMail = 0,loginMail = 0 where psnId in(select psnId from PersonEmail where id = ? )",
        mailId).executeUpdate();
    PersonEmail psnEmail = this.get(mailId);
    String[] emailPart = psnEmail.getEmail().split("@");
    psnEmail.setLeftPart(emailPart[0]);
    psnEmail.setRightPart(emailPart[1]);
    psnEmail.setFirstMail(1);
    psnEmail.setLoginMail(1);
    super.save(psnEmail);

  }

  // /**
  // * 保存邮件.
  // *
  // * @param personEmail
  // * @throws DaoException
  // */
  // public void saveOrUpdate(PersonEmail personEmail) throws DaoException {
  // save(personEmail);
  // }

  /**
   * 删除邮件.
   * 
   * @param emailId
   * @throws DaoException
   */
  public void deletePersonEmail(Long emailId) throws DaoException {
    this.delete(emailId);
  }

  /**
   * 通过EMAIL查询用户ID，用户名列表.
   * 
   * @param email
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PersonEmail> findListByEmail(String email) throws DaoException {

    String hql =
        "select new PersonEmail(p.personId,p.name,p.ename,t.email) from PersonEmail t,Person p  where t.psnId = p.personId and t.email = ? ";

    return super.createQuery(hql, email).list();
  }

  /**
   * 通过EMAIL查询用户.
   * 
   * @param email
   * @return
   * @throws DaoException
   */
  public PersonEmail findPersonEmailByEmail(Long psnId, String email) throws DaoException {

    String hql = "from PersonEmail t where t.email = ? and t.psnId  = ? ";

    return super.findUnique(hql, new Object[] {email, psnId});
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
    Long count = super.findUnique("select count(psnId) from PersonEmail where psnId = ? and email = ? ", psnId, email);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 检索用户是否已存在具体邮件.
   * 
   * @param psnId
   * @param email
   * @return
   * @throws DaoException
   */
  public List<Long> isExitEmailVerify(String email) throws DaoException {
    return find("select psnId from PersonEmail where email = ? and  isVerify=1", email);
  }

  /**
   * 获取已经确认过的 用户邮件列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getConfirmEmail(Long psnId) {

    return super.createQuery("select email from PersonEmail where isVerify = 1 and psnId = ? ", psnId).list();
  }

  /**
   * 检索用户是否已对该邮件进行过确认.
   * 
   * @param psnId
   * @param email
   * @return
   * @throws DaoException
   */
  public boolean isPsnVerified(Long psnId, String email) throws DaoException {
    List<PersonEmail> lst =
        find("from PersonEmail where psnId=? and email = ? and isVerify=1 and firstMail=1", psnId, email);
    return lst.size() > 0 ? true : false;
  }

  /**
   * 取得将此邮箱设为首要邮件的人员总数
   * 
   * @param email
   * @return
   * @throws DaoException
   */
  public Long queryPersonEmailCountByEmail(String email) throws DaoException {
    String hql = "select count(t.id) from PersonEmail t where t.email=? and t.firstMail=1";

    return super.findUnique(hql, new Object[] {email});
  }

  /**
   * 只查询首要邮件、登录账号、确认过的邮箱三种
   * 
   * @param personId
   * @return
   * @throws DaoException
   */
  public List<String> findAvailableEmail(Long personId) throws DaoException {
    return this.find(
        "select distinct t.email from PersonEmail t where t.psnId = ? and (t.firstMail=1 or t.loginMail=1 or t.isVerify=1)",
        new Object[] {personId});
  }
}
