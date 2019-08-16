package com.smate.sie.core.base.utils.dao.auditlog;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.auditlog.SieComAuditLog;

/**
 * @author yxy
 */

@Repository
public class SieAuditLogDao extends SieHibernateDao<SieComAuditLog, Long> {

  /**
   * 更新审计日志解析状态.
   */
  public void updateAuditTrailStatus(Long audId, Integer status) {
    StringBuffer sql = new StringBuffer();
    sql.append("update com_audit_trail t set t.status = :status where t.aud_id =:audId");
    SQLQuery query = this.getSession().createSQLQuery(sql.toString());
    query.setParameter("status", status).setParameter("audId", audId);
    query.executeUpdate();
  }

  /**
   * 获取待解析的日志记录.
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getAuditLogList(Integer startId, Integer fetchSize) {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select t.aud_user,t.aud_client_ip,t.aud_server_ip,t.aud_resource,t.aud_action,t.aud_date,t.aud_content,t.status,t.aud_id from com_audit_trail t where t.status=0 ");
    sql.append(" and t.aud_action in(select t.aud_action from com_audit_type t where t.status=1) order by t.aud_id");
    SQLQuery query = (SQLQuery) this.getSession().createSQLQuery(sql.toString())
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    query.setFirstResult(startId);
    query.setMaxResults(fetchSize);
    return query.list();
  }

}
