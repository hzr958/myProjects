package com.smate.core.base.utils.dao.security;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.reg.RegisterTemp;

@Repository
public class RegisterTempDao extends SnsHibernateDao<RegisterTemp, Long> {
  public Long getRegisterTempId() {
    String sql = "select SEQ_V_REGISTER_TEMP.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 根据邮件和状态查询记录
   * 
   * @param email
   * @param status
   * @return
   */
  public Long getCountByEmail(String email, Integer status) {
    String hql = "select count(*) from RegisterTemp t where t.email=:email and status=:status";
    return (Long) this.createQuery(hql).setParameter("email", email).setParameter("status", status).uniqueResult();
  }

  /**
   * 根据邮件和状态查询记录
   * 
   * @param email
   * @param status
   * @return
   */
  public RegisterTemp getByEmail(String email) {
    String hql = " from RegisterTemp t where t.email=:email order by t.token desc limit 1";
    return (RegisterTemp) this.createQuery(hql).setParameter("email", email).uniqueResult();
  }

  /**
   * 根据邮件和状态查询记录
   * 
   * @param email
   * @param status
   * @return
   */
  public List<RegisterTemp> getRegisterTempByEmail(String email, Integer status) {
    String hql = "from RegisterTemp t where t.email=:email and status=:status";
    return this.createQuery(hql).setParameter("email", email).setParameter("status", status).list();
  }

}
