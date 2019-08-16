package com.smate.center.open.dao.pdwh.jnl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.pdwh.jnl.Journal;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 期刊DAO.
 * 
 * @author xys
 * 
 */
@Repository
public class JournalDao extends PdwhHibernateDao<Journal, Long> {

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
  public List<Journal> queryJournalByNameIssn(String issn, String nameAlias, Long psnId) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select new Journal(t.id,t.zhName,t.enName,t.issn,t.matchBaseJnlId)  from Journal t where (t.matchBaseJnlId is not null or t.addPsnId=?) and (t.enameAlias = ? or t.zhNameAlias = ?) ");
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
    List<Journal> list = query.list();
    return list;
  }

  public void addJournal(Journal journal) throws Exception {
    super.save(journal);
  }

  // 同步更新journal刷新表journal_syncjnl_flag
  public void syncJournalFlag(Long jid) {
    String sql = "insert into journal_syncjnl_flag values(?,?,?,?)";
    super.update(sql, new Object[] {jid, 0, 0, 1});
  }

}
