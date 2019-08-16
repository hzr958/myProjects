package com.smate.sie.center.task.journal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.journal.model.SieJournal;

@Repository
public class SieJournalDao extends SieHibernateDao<SieJournal, Long> {

  /**
   * 表单模糊匹配(查询本单位添加且jnlid为空的)
   * 
   * @param name
   * @param insId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieJournal> queryJournalByName(String name, Long insId, int size) {
    List<Object> params = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer();
    hql.append(
        "from SieJournal t where t.addInsId=? and (lower(t.zhName) like ? or lower(t.enName) like ?)  and t.matchBaseJnlId is null ");
    params.add(insId);
    params.add("%" + name.trim().toLowerCase() + "%");
    params.add("%" + name.trim().toLowerCase() + "%");
    hql.append(" order by t.id asc");
    return super.createQuery(hql.toString(), params.toArray()).setMaxResults(size).list();
  }

  /**
   * 
   * @param issn
   * @param nameAlias
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieJournal> queryJournalByNameIssn(String issn, String nameAlias, Long insId) {
    List<Object> params = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer();
    hql.append(
        "from SieJournal t where (t.matchBaseJnlId is not null or t.addInsId=?) and (t.enameAlias = ? or t.zhNameAlias = ?) ");
    params.add(insId);
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      hql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    hql.append(" order by t.id asc");
    return super.createQuery(hql.toString(), params.toArray()).setMaxResults(100).list();
  }

  /**
   * 根据jnlid查找journal
   * 
   * @param jnlId
   * @return
   */
  @SuppressWarnings("unchecked")
  public SieJournal queryJournalByBJnlId(Long jnlId) {
    String hql = "from SieJournal t where t.matchBaseJnlId = ? order by t.id asc";
    List<SieJournal> list = this.getSession().createQuery(hql).setParameter(0, jnlId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  // 录入添加期刊调用，获取本单位未匹配上的期刊
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<SieJournal> queryJournalByIns(String issn, String nameAlias, Long insId) {
    List params = new ArrayList();
    StringBuffer hql = new StringBuffer();
    hql.append(
        "select new SieJournal(t.id,t.zhName,t.enName,t.issn)  from SieJournal t where t.addInsId=? and (t.enameAlias = ? or t.zhNameAlias = ?) and t.matchBaseJnlId is null ");
    params.add(insId);
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      hql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    hql.append(" order by t.id asc");
    Query query = super.createQuery(hql.toString(), params.toArray());
    query.setMaxResults(100);
    List<SieJournal> list = query.list();
    return list;
  }

}
