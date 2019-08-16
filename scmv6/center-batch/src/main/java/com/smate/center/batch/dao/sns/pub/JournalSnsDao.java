package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.AcJournal;
import com.smate.center.batch.model.sns.pub.JournalSns;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * sns冗余期刊数据dao
 * 
 * @author tsz
 * 
 */
@Repository
public class JournalSnsDao extends SnsHibernateDao<JournalSns, Long> {

  /**
   * 添加期刊.
   * 
   * @param journal 期刊实体
   * @throws DaoException DaoException
   */
  public void addJournal(JournalSns journal) throws DaoException {
    super.save(journal);
  }

  /**
   * 添加期刊.
   * 
   * @param journal 期刊实体
   * @throws DaoException DaoException
   */
  @SuppressWarnings("unchecked")
  public void addJournalForTask(JournalSns journal) throws DaoException {
    String sql =
        "insert into JournalSns(id,zhName,enName,issn,enameAlias,zhNameAlias,addPsnId,matchBaseStatus,matchBaseJnlId) values(?,?,?,?,?,?,?,?,?)";
    List params = new ArrayList();
    params.add(journal.getId());
    params.add(journal.getZhName());
    params.add(journal.getEnName());
    params.add(journal.getIssn());
    params.add(journal.getAddPsnId());
    params.add(journal.getEnameAlias());
    params.add(journal.getZhNameAlias());
    params.add(journal.getMatchBaseStatus());
    params.add(journal.getMatchBaseJnlId());

    Query query = super.createQuery(sql.toString(), params.toArray());
    query.executeUpdate();

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
  public List<JournalSns> queryJournalByNameIssn(String issn, String nameAlias, Long psnId) throws DaoException {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select new JournalSns(t.id,t.zhName,t.enName,t.issn,t.matchBaseJnlId)  from JournalSns t where (t.matchBaseJnlId is not null or t.addPsnId=?) and (t.enameAlias = ? or t.zhNameAlias = ?) ");
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
    List<JournalSns> list = query.list();
    return list;
  }

  /**
   * 得到自动提示数据
   * 
   * @param startWith
   * @param size
   * @param uid
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcJournal> getAcJournalByPsn(String startWith, int size, Long uid) throws DaoException {
    List<AcJournal> newList = new ArrayList<AcJournal>();
    boolean isEnglish = StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    // 判断是否是非英文,查询本人数据()
    if (isEnglish) {
      hql = "from JournalSns t where lower(t.enName) like ? and t.addPsnId=?";
    } else {
      hql = "from JournalSns t where lower(t.zhName) like ? and t.addPsnId=?";
    }
    Query query = super.createQuery(hql, new Object[] {"%" + startWith.trim().toLowerCase() + "%", uid});
    query.setMaxResults(size);
    List<JournalSns> list = query.list();
    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (JournalSns cr : list) {
        AcJournal acjnl = new AcJournal();
        acjnl.setCode(cr.getId());
        if (StringUtils.isNotBlank(cr.getIssn()))
          acjnl.setIssn(cr.getIssn());
        if (isEnglish) {
          acjnl.setName(cr.getEnName());
        } else {
          acjnl.setName(cr.getZhName());
        }
        newList.add(acjnl);
      }
    }
    return newList;
  }

  // 录入添加期刊调用，获取匹配上的基础期刊
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> queryJournalByBJId(String issn, String nameAlias) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select distinct t.matchBaseJnlId from JournalSns t where (t.enameAlias = ? or t.zhNameAlias = ?) and t.matchBaseJnlId is not null ");
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
  public List<JournalSns> queryJournalByPsn(String issn, String nameAlias, Long psnId) {
    List params = new ArrayList();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select new JournalSns(t.id,t.zhName,t.enName,t.issn)  from JournalSns t where t.addPsnId=? and (t.enameAlias = ? or t.zhNameAlias = ?) and t.matchBaseJnlId is null ");
    params.add(psnId);
    params.add(nameAlias);
    params.add(nameAlias);
    if (StringUtils.isNotBlank(issn)) {
      sql.append(" and lower(t.issn)=?");
      params.add(issn.toLowerCase());
    }
    Query query = super.createQuery(sql.toString(), params.toArray());
    query.setMaxResults(100);
    List<JournalSns> list = query.list();
    return list;
  }

}

