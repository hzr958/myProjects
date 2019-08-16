package com.smate.center.batch.dao.dynamic;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 动态id获取Dao
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
@Repository
public class InspgDynamicSeqDao extends SnsHibernateDao<InspgDynamicRefresh, Long> {

  public Long getNewDynId() {
    String sql = "select V_SEQ_INSPG_DYNAMIC.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Long id = (Long) query.uniqueResult();
    return id;
  }

}
