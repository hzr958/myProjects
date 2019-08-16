package com.smate.center.open.dao.open;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.open.PersonOpen;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * psn_id 与open_id 关系DAO
 * 
 * @author tsz
 *
 */
@Repository
public class PersonOpenDao extends SnsHibernateDao<PersonOpen, Long> {

  /**
   * 在序列中获取 下一个作为newopenid
   * 
   * @return
   */
  public Long getNewOpenId() {
    String sql = "select V_SEQ_OPEN_ID.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Long newOpenId = Long.parseLong(query.uniqueResult().toString());
    return newOpenId;
  }

  /**
   * 
   * @param openId
   * @return
   */
  public PersonOpen getPersonOpenByOpenId(Long openId) {
    String hql = "From PersonOpen t where t.openId=:openId ";
    Object obj = super.createQuery(hql).setParameter("openId", openId).uniqueResult();
    if (obj != null) {
      return (PersonOpen) obj;
    }
    return null;
  }

  /**
   * 
   * @param psnId
   * @return
   */
  public PersonOpen getPersonOpenByPsnId(Long psnId) {
    String hql = "From PersonOpen t where t.psnId=:psnId ";
    Object obj = super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (obj != null) {
      return (PersonOpen) obj;
    }
    return null;
  }

}
