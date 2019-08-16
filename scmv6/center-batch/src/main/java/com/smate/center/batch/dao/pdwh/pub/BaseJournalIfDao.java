package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.BaseJournalIfTo;

@Repository
public class BaseJournalIfDao extends HibernateTemplateDao<BaseJournalIfTo, Long> {
  public BaseJournalIfTo getJifIdByJnlId(Long jnl_id, Long dbId, String year) {
    String sql = "from BaseJournalIfTo where jnlId=? and dbId=? and ifYear=?";
    return findUnique(sql, jnl_id, dbId, year);
  }

  @SuppressWarnings("rawtypes")
  public Double getJnlLastYearIf(Long jnlId) {
    Double result = Double.valueOf(9999);
    try {
      String hql = "select jouIf from BaseJournalIfTo where jnlId=? order by ifYear desc";
      Query q = super.createQuery(hql, jnlId);
      q.setMaxResults(1);
      List list = q.list();
      if (CollectionUtils.isEmpty(list) || list.get(0) == null)
        return result;
      else
        return Double.valueOf(list.get(0).toString());
    } catch (Exception e) {
      logger.error("getJnlLastYearIf error jnlId={}", jnlId, e);
    }
    return result;
  }

  // 获取基础期刊最近一年的影响因子
  @SuppressWarnings("rawtypes")
  public Map getBjnlLastYearIf(Long bjId) {
    String sql =
        "select * from(select t.if_year,t.jou_if from base_journal_isi_if t where t.jnl_id=? order by t.if_year desc)where rownum=1";
    List list = super.queryForList(sql, new Object[] {bjId});
    return CollectionUtils.isEmpty(list) ? null : (Map) list.get(0);
  }
}
