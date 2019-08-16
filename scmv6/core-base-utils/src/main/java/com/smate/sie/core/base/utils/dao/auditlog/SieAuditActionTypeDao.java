package com.smate.sie.core.base.utils.dao.auditlog;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.auditlog.SieComAuditType;

/**
 * 审计日志类型.
 * 
 * @author yxy
 * 
 */
@Repository
public class SieAuditActionTypeDao extends SieHibernateDao<SieComAuditType, Long> {

  /**
   * 查询所有日志类型.
   */
  @SuppressWarnings("unchecked")
  public List<SieComAuditType> getActionTypeList() {
    StringBuffer hql = new StringBuffer();
    hql.append(" from SieComAuditType where parentId<>0 and status=1 ");
    hql.append(" order by parentId ");
    Query query = this.getSession().createQuery(hql.toString());
    query.setCacheable(true);
    List<SieComAuditType> list = query.list();
    return list;
  }

  /**
   * 根据父编号查询日志类型.
   */
  @SuppressWarnings("unchecked")
  public SieComAuditType getParentType(Long parentId) {
    Query query = this.getSession().createQuery("from SieComAuditType where id=?").setParameter(0, parentId);
    query.setCacheable(true);
    List<SieComAuditType> list = query.list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
