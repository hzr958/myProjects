package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.JournalRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 期刊Dao(查找、新增、修改).
 * 
 * @author yamingd
 */
@Repository
public class JournalRolDao extends RolHibernateDao<JournalRol, Long> {

  /**
   * 添加期刊.
   * 
   * @param journal 期刊实体
   * @throws DaoException DaoException
   */
  public void addJournal(JournalRol journal) throws DaoException {
    super.save(journal);
  }

  /**
   * rol批量导入调用.
   * 
   * @param jname
   * @param issn
   * @param nameAlias
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<JournalRol> queryJournalByNameIssn(String jname, String issn, String nameAlias) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select new JournalRol(t.id,t.zhName,t.enName,t.issn,t.matchBaseJnlId)  from JournalRol t where (t.enameAlias = ? or t.zhNameAlias = ?) ");
    params.add(nameAlias);
    params.add(nameAlias + String.valueOf(jname.hashCode()));
    if (StringUtils.isNotBlank(issn)) {
      sql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    sql.append(" order by t.id desc");
    Query query = super.createQuery(sql.toString(), params.toArray());
    List<JournalRol> list = query.list();
    return list;
  }

  /**
   * 导入调用.
   * 
   * @param jname
   * @param issn
   * @param nameAlias
   * @param psnId
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<JournalRol> queryJournalByNameIssn(String issn, String nameAlias, Long psnId) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select new JournalRol(t.id,t.zhName,t.enName,t.issn,t.matchBaseJnlId)  from JournalRol t where (t.matchBaseJnlId is not null or t.addPsnId=?) and (t.enameAlias = ? or t.zhNameAlias = ?) ");
    params.add(psnId);
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      sql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    sql.append(" order by t.id desc");
    Query query = super.createQuery(sql.toString(), params.toArray());
    query.setMaxResults(100);
    List<JournalRol> list = query.list();
    return list;
  }

  // 录入添加期刊调用，获取匹配上的基础期刊
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> queryJournalByBJId(String issn, String nameAlias) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select distinct t.matchBaseJnlId from JournalRol t where (t.enameAlias = ? or t.zhNameAlias = ?) and t.matchBaseJnlId is not null ");
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
  public List<JournalRol> queryJournalByPsn(String issn, String nameAlias, Long psnId) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select new JournalRol(t.id,t.zhName,t.enName,t.issn)  from JournalRol t where t.addPsnId=? and (t.enameAlias = ? or t.zhNameAlias = ?) and t.matchBaseJnlId is null ");
    params.add(psnId);
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      sql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    Query query = super.createQuery(sql.toString(), params.toArray());
    query.setMaxResults(100);
    List<JournalRol> list = query.list();
    return list;
  }


  @SuppressWarnings("unchecked")
  public List<JournalRol> findJournal(Long lastId, int size) {
    String hql =
        "select new JournalRol(id,zhName,enName,issn) from JournalRol where id>? and (matchBaseStatus is null or matchBaseStatus=0 or matchBaseStatus=4) order by id asc";
    Query query = super.createQuery(hql, lastId);
    query.setMaxResults(size);
    return query.list();
  }

  /**
   * 按MATCH_BASE_STATUS为1和MATCH_BASE_JNL_ID为空查询，即该期刊已经匹配，但未匹配上.
   * 
   * @throws DaoException
   * 
   */
  @SuppressWarnings("unchecked")
  public Page<JournalRol> findJournalPage(Page<JournalRol> page, JournalRol journal) {
    String hql = "from JournalRol where matchBaseStatus=1 and matchBaseJnlId is null order by issn asc,zhName,enName";
    Query queryCt = super.createQuery("select count(id) " + hql);
    Long count = (Long) queryCt.uniqueResult();
    page.setTotalCount(count.intValue());
    // 查询数据实体
    Query queryResult = super.createQuery(hql.toString());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  public JournalRol findJournal(Long jId) {
    String hql = " from JournalRol where id=?";
    Query query = super.createQuery(hql, jId);
    return (JournalRol) query.uniqueResult();
  }

  public void saveOrUpdateJournal(JournalRol journal) throws DaoException {
    super.getSession().saveOrUpdate(journal);
  }

  // 多个jid查询多个Journal
  @SuppressWarnings("unchecked")
  public List<JournalRol> findJournalList(List<Long> jidList) {
    String hql = " from JournalRol where id in(:id)";
    Query query = super.createQuery(hql).setParameterList("id", jidList);
    return query.list();
  }

  public Long getJnlByMatchBaseJnlId(Long jid) {
    String hql = "select matchBaseJnlId from JournalRol where id=?";
    return findUnique(hql, jid);
  }

  /**
   * 通过jnlId查询出与之对应的jid.
   * 
   * @param jnlId
   * @return
   * @throws DaoServiceException
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryJidByJnlId(Long jnlId) throws DaoException {
    return super.createQuery("select t.id from JournalRol t where t.matchBaseJnlId = ? order by t.id", jnlId)
        .setMaxResults(500).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> snsJnlIdMatchBaseJnlId(List<Long> snsJids) {
    String hql = "select distinct matchBaseJnlId from JournalRol where id in(:id)";
    snsJids = snsJids.size() > 99 ? snsJids.subList(0, 98) : snsJids;
    Query query = super.createQuery(hql).setParameterList("id", snsJids);
    return query.list();
  }

  public void setMatchJnlId(Long jid, Long matchJid) {
    String hql = "update JournalRol set matchBaseJnlId=? where id=?";
    super.createQuery(hql, matchJid, jid).executeUpdate();
  }

  public void updateMatchJnlId(Long bjid, Long newbjid) {
    String hql = "update JournalRol set matchBaseJnlId=? where matchBaseJnlId=?";
    super.createQuery(hql, newbjid, bjid).executeUpdate();
  }

  // 同步更新journal刷新表journal_syncjnl_flag
  public void syncJournalFlag(Long jid) {
    String sql = "insert into journal_syncjnl_flag values(?,?,?,?)";
    super.update(sql, new Object[] {jid, 0, 0, 1});
  }

  /**
   * 人员合并
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<JournalRol> getJournalByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from JournalRol where addPsnId=?", psnId).list();
  }
}
