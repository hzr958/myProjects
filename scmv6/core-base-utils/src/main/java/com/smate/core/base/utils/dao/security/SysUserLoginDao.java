package com.smate.core.base.utils.dao.security;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.CasHibernateDao;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;

/**
 * 判断用户是否登录过Dao.
 * 
 * 
 * @author lhd
 */
@Repository
public class SysUserLoginDao extends CasHibernateDao<SysUserLogin, Long> {

  public Date getLastLoginTimeById(Long psnId) {
    String hql = "select t.lastLoginTime from SysUserLogin t where t.id = :psnId ";
    return (Date) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取前一周登录过的人员
   * 
   * @return
   */
  public List<Long> getPsnIdsDayBeforeByLogin() {
    String hql =
        "select t.id from SysUserLogin t where to_char(t.lastLoginTime,'YYYY-MM-DD')>=to_char(sysdate-7,'YYYY-MM-DD') and to_char(t.lastLoginTime,'YYYY-MM-DD')<=to_char(sysdate-1,'YYYY-MM-DD')";
    return find(hql);
  }

  /**
   * 获取前半年登录过的人员
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public Object getIsLogin(Long psnId) {
    String sql =
        "select t.PSN_ID from SYS_USER_LOGIN t where t.PSN_ID = :psnId and trunc(t.LAST_LOGIN_TIME)  between add_months(trunc(sysdate),-6) and trunc(sysdate)";
    return (Object) super.getSession().createSQLQuery(sql).setParameter("psnId", psnId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getLoginPsnIds(Long lastPsnId) {
    String hql = "select t.id from SysUserLogin t where t.id > :lastPsnId order by t.id ";
    return super.createQuery(hql).setParameter("lastPsnId", lastPsnId).setMaxResults(1000).list();
  }
}
