package com.smate.center.open.dao.pdwh.jnl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 期刊级别DAO.
 * 
 * @author xys
 *
 */
@Repository
@SuppressWarnings("rawtypes")
public class JnlLevelDao extends PdwhHibernateDao {

  /**
   * 获取国际期刊jids.
   * 
   * @param jids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getIntJnlJids(List<Long> jids) throws Exception {
    String sql =
        "select t.jid from journal t where t.jid in (:jids) and exists(select 1 from base_journal_db t2 where t2.jnl_id=t.match_base_jnl_id and t2.dbid is not null and t2.dbid not in(4,18))";
    List<BigDecimal> list = super.getSession().createSQLQuery(sql).setParameterList("jids", jids).list();
    List<Long> newList = null;
    if (!CollectionUtils.isEmpty(list)) {
      newList = new ArrayList<Long>();
      for (BigDecimal jid : list) {
        newList.add(jid.longValue());
      }
    }
    return newList;
  }

  /**
   * 获取国内核心期刊jids.
   * 
   * @param jids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getHomeCoreJnlJids(List<Long> jids) throws Exception {
    String sql =
        "select t.jid from journal t where t.jid in (:jids) and exists(select 1 from base_journal_core t2 where t2.jnl_id=t.match_base_jnl_id and t2.core_flag=1)";
    List<BigDecimal> list = super.getSession().createSQLQuery(sql).setParameterList("jids", jids).list();
    List<Long> newList = null;
    if (!CollectionUtils.isEmpty(list)) {
      newList = new ArrayList<Long>();
      for (BigDecimal jid : list) {
        newList.add(jid.longValue());
      }
    }
    return newList;
  }
}
