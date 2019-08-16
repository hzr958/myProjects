package com.smate.core.base.psn.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.AccountEmailCheckLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 账号邮箱检查记录
 * 
 * @author aijiangbin
 *
 */
@Repository
public class AccountEmailCheckLogDao extends SnsHibernateDao<AccountEmailCheckLog, Long> {

  /**
   * 查找最新账号邮箱记录 ，
   * 
   * @param psnId
   * @return
   */
  public AccountEmailCheckLog findNewestAccountEmailCheckLog(Long psnId, String account) {
    String hql =
        "from  AccountEmailCheckLog  t where  t.psnId =:psnId and t.account =:account order by t.sendTime desc ";
    List list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("account", account).list();
    if (list != null && list.size() > 0) {
      return (AccountEmailCheckLog) list.get(0);
    }
    return null;
  }

  /**
   * 检查需要验证的 邮件记录
   * 
   * @param psnId
   * @param account
   * @return
   */
  public AccountEmailCheckLog checkValidate(Long psnId) {
    String hql = "from  AccountEmailCheckLog  t where  t.psnId =:psnId  and t.dealStatus = 0   ";
    Object obj = this.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (obj != null) {
      return (AccountEmailCheckLog) obj;
    }
    return null;
  }

  /**
   * 检查需要验证的 邮件记录
   * 
   * @param psnId
   * @param account
   * @return
   */
  public AccountEmailCheckLog checkValidate(Long psnId, String account) {
    String hql = "from  AccountEmailCheckLog  t where  t.psnId =:psnId and t.account =:account   and t.dealStatus = 0";
    Object obj = this.createQuery(hql).setParameter("psnId", psnId).setParameter("account", account).uniqueResult();
    if (obj != null) {
      return (AccountEmailCheckLog) obj;
    }
    return null;
  }

  /**
   * 查找已经验证的邮件记录
   * 
   * @param psnId
   * @param account
   * @return
   */
  public AccountEmailCheckLog findHasConfirm(Long psnId, String account) {
    String hql =
        "from  AccountEmailCheckLog  t where  t.psnId =:psnId and t.account =:account   and t.dealStatus = 1   ";
    List list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("account", account).list();
    if (list != null && list.size() > 0) {
      return (AccountEmailCheckLog) list.get(0);
    }
    return null;
  }

  /**
   * 更新当前人，账号需要重新发送的账号
   * 
   * @param psnId
   * @param account
   * @return
   */
  public int upadeNeedResendAccout(Long psnId, String account) {
    String hql =
        "update  AccountEmailCheckLog  t   set t.dealStatus=2 , t.dealTime =:dealTime where  t.psnId =:psnId and t.account =:account   and t.dealStatus = 0   ";
    int count = this.createQuery(hql).setParameter("psnId", psnId).setParameter("account", account)
        .setParameter("dealTime", new Date()).executeUpdate();
    return count;
  }

  /**
   * 得到下一个主键
   * 
   * @return
   */
  public Long getNextId() {
    String sql = "select  SEQ_V_ACCOUNT_EMAIL_CHECK_LOG.NEXTVAL    from  dual ";
    long id = this.queryForLong(sql);
    return id;
  }

  @Transactional(rollbackFor = Exception.class)
  public void confirmPsnEmail(Long psnId, String email) {
    String sql = "update PSN_EMAIL  set IS_VERIFY = 1  where PSN_ID=:psnId and email =:email";
    this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).setParameter("email", email).executeUpdate();
  }
}
