package com.smate.web.v8pub.dao.journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.journal.JournalPO;

@Repository
public class JournalDAO extends SnsHibernateDao<JournalPO, Long> {

  /**
   * 
   * @param issn 期刊的issn号
   * @param nameAlias
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<JournalPO> queryJournalByNameIssn(String issn, String nameAlias, Long psnId) {
    List<Object> params = new ArrayList<Object>();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "from JournalPO t where (t.matchBaseJnlId is not null or t.addPsnId=?) and (t.enameAlias = ? or t.zhNameAlias = ?) ");
    params.add(psnId);
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      sql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    sql.append(" order by t.id desc");
    return super.createQuery(sql.toString(), params.toArray()).setMaxResults(100).list();
  }


  // 录入添加期刊调用，获取匹配上的基础期刊
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> queryJournalByBJId(String issn, String nameAlias) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select distinct t.matchBaseJnlId from JournalPO t where (t.enameAlias = ? or t.zhNameAlias = ?) and t.matchBaseJnlId is not null ");
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      sql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    Query query = super.createQuery(sql.toString(), params.toArray());
    List<Long> list = query.list();
    return list;
  }

  // 录入添加期刊调用，获取本人未匹配上的期刊
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<JournalPO> queryJournalByPsn(String issn, String nameAlias, Long psnId) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select new JournalPO(t.id,t.zhName,t.enName,t.issn)  from JournalPO t where t.addPsnId=? and (t.enameAlias = ? or t.zhNameAlias = ?) and t.matchBaseJnlId is null ");
    params.add(psnId);
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      sql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    Query query = super.createQuery(sql.toString(), params.toArray());
    query.setMaxResults(100);
    List<JournalPO> list = query.list();
    return list;
  }

  public String findImpactFactors(Long jid) {
    String sql = "select b.IMPACT_FACTORS  from   base_journal b  where "
        + " exists (  select 1 from journal  where jid =:jid and  MATCH_BASE_JNL_ID = b.JNL_ID )";
    List list = this.getSession().createSQLQuery(sql).setParameter("jid", jid).list();
    if (list != null && list.size() > 0) {
      return Objects.toString(list.get(0), "");
    }
    return "";

  }

}
