package com.smate.center.batch.dao.pdwh.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.AcJournal;
import com.smate.center.batch.model.pdwh.pub.BaseJournal;
import com.smate.center.batch.model.pdwh.pub.BaseJournal2;
import com.smate.center.batch.model.sns.prj.PsnPublishedJnl;
import com.smate.core.base.utils.model.Page;

/**
 * 基础期刊数据Dao(查找、修改、审核).
 * 
 * @author cwli
 */
@Repository
public class BaseJournalDao extends HibernateTemplateDao<BaseJournal, Long> {

  @Override
  public void saveOrUpdate(BaseJournal entity) {
    super.saveOrUpdate(entity);
  }

  @Override
  protected DetachedCriteria searchCriteria(Object entity) {
    BaseJournal journal = (BaseJournal) entity;
    DetachedCriteria criteria = DetachedCriteria.forClass(journal.getClass());
    if (journal.getJouId() != null) {
      criteria.add(Restrictions.eq("jouId", journal.getJouId()));
    }
    if (StringUtils.isNotBlank(journal.getPissn())) {
      criteria.add(Restrictions.eq("pissn", journal.getPissn().trim()));
    }
    if (StringUtils.isNotBlank(journal.getTitleXx())) {
      criteria.add(Restrictions.or(Restrictions.like("titleXx", journal.getTitleXx().trim(), MatchMode.ANYWHERE),
          Restrictions.like("titleEn", journal.getTitleXx().trim(), MatchMode.ANYWHERE)));
    }
    criteria.addOrder(Order.desc("jouId"));
    return criteria;
  }

  public void updateEissn(String eissn, Long jnlId) {
    super.update("update base_journal t set t.eissn=? where t.jnl_id=?", new Object[] {eissn, jnlId});
  }

  public List<BaseJournal> findBaseJnl(String titleXx, String titleEn, String pissn, String eissn) {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    if (StringUtils.isNotBlank(titleXx)) {
      criterionList.add(Restrictions.eq("titleXx", titleXx));
    }
    if (StringUtils.isNotBlank(titleEn)) {
      criterionList.add(Restrictions.eq("titleEn", titleEn).ignoreCase());
    }
    if (StringUtils.isNotBlank(pissn)) {
      criterionList.add(Restrictions.eq("pissn", pissn).ignoreCase());
    }
    if (StringUtils.isNotBlank(eissn)) {
      criterionList.add(Restrictions.eq("eissn", eissn));
    }
    return find(criterionList.toArray(new Criterion[criterionList.size()]));
  }

  @SuppressWarnings("unchecked")
  public Page<BaseJournal> findBaseJournalByThrows(Page page, int type) {
    String hql = "";
    if (type == 1) {
      hql =
          "select new BaseJournal2(t1.jouId,t1.titleEn,t1.titleXx,t1.pissn) from BaseJournal2 t1,BaseJournalTitleTo t2 where t1.jouId=t2.jnlId and (t1.titleEn<>t2.titleEn or t1.titleXx<>t2.titleXx) and (t2.titleStatus is null or t2.titleStatus=0)";
    }
    if (type == 2) {
      hql =
          "select new BaseJournal2(t1.jouId,t1.titleEn,t1.titleXx,t1.pissn) from BaseJournal2 t1,BaseJournalTitleTo t2 where t1.jouId=t2.jnlId and t1.pissn<>t2.pissn";
    }
    Query q = createQuery(hql);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<BaseJournal2> result = q.list();
    List<BaseJournal> newJnlList = new ArrayList<BaseJournal>();
    if (CollectionUtils.isNotEmpty(result)) {
      for (BaseJournal2 baseJournal2 : result) {
        BaseJournal newjnl = new BaseJournal();
        newjnl.setJouId(baseJournal2.getJouId());
        newjnl.setTitleEn(baseJournal2.getTitleEn());
        newjnl.setTitleXx(baseJournal2.getTitleXx());
        newjnl.setPissn(baseJournal2.getPissn());
        if (type == 1) {
          newjnl.setIsNewTitle(true);
        }
        if (type == 2) {
          newjnl.setIsNewPissn(true);
        }
        newJnlList.add(newjnl);
      }
    }
    page.setResult(newJnlList);
    return page;
  }

  @SuppressWarnings({"unchecked"})
  public PsnPublishedJnl publicshedJnlMatchBaseJnl(Long jnlId) {
    String hql =
        "select t2.jouIf,t2.ifYear,t2.dbId from BaseJournal2 t1,BaseJournalIfTo t2 where t1.jouId=t2.jnlId and t1.jouId=? order by t2.ifYear desc";
    List<Object[]> list = super.createQuery(hql, new Object[] {jnlId}).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }

    String impactFactors = "";
    String year = "";
    String dbCodes = "";
    String tempdbId = "";
    for (int i = 0; i < list.size(); i++) {
      Object[] objects = list.get(i);
      if (i == 0) {
        impactFactors = ObjectUtils.toString(objects[0]);
        year = ObjectUtils.toString(objects[1]);
      }
      String dbid = ObjectUtils.toString(objects[2]);
      if (tempdbId.indexOf(dbid) == -1) {
        tempdbId += dbid + ",";
        if ("16".equals(dbid)) {
          dbCodes += StringUtils.isNotBlank(dbCodes) ? ", SCI" : "SCI";
        } else if ("17".equals(dbid)) {
          dbCodes += StringUtils.isBlank(dbCodes) ? "SSCI" : ",SSCI";
        } else if ("19".equals(dbid)) {
          dbCodes += StringUtils.isBlank(dbCodes) ? "SCIE" : ",SCIE";
        }
      }
    }

    PsnPublishedJnl publishedJnl = new PsnPublishedJnl();
    publishedJnl.setQuality(dbCodes + "<br/>" + impactFactors + "&nbsp;(" + year + ")");
    return publishedJnl;
  }

  public BaseJournal2 getBaseJournal2(Long jnlId) {
    String hql = "from BaseJournal2 where jouId=?";
    return findUnique(hql, jnlId);
  }

  public BaseJournal2 getBaseJournal2Title(Long jnlId) {
    String hql = "select new BaseJournal2(jouId,titleEn,titleXx) from BaseJournal2 where jouId=?";
    return findUnique(hql, jnlId);
  }

  @SuppressWarnings("unchecked")
  public String getJnlQuality(Long jnlId) {
    String hql = "select t2.jouIf,t2.ifYear,t2.dbId from BaseJournalIfTo t2 where t2.jnlId=? order by t2.ifYear desc";
    List<Object[]> list = super.createQuery(hql, new Object[] {jnlId}).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    String impactFactors = "";
    String year = "";
    String dbCodes = "";
    String tempdbId = "";
    for (int i = 0; i < list.size(); i++) {
      Object[] objects = list.get(i);
      if (i == 0) {
        impactFactors = ObjectUtils.toString(objects[0]);
        year = ObjectUtils.toString(objects[1]);
      }
      String dbid = ObjectUtils.toString(objects[2]);
      if (tempdbId.indexOf(dbid) == -1) {
        tempdbId += dbid + ",";
        if ("16".equals(dbid)) {
          dbCodes += StringUtils.isNotBlank(dbCodes) ? ", SCI" : "SCI";
        } else if ("17".equals(dbid)) {
          dbCodes += StringUtils.isBlank(dbCodes) ? "SSCI" : ", SSCI";
        } else if ("19".equals(dbid)) {
          dbCodes += StringUtils.isBlank(dbCodes) ? "SCIE" : ", SCIE";
        }
      }
    }
    return dbCodes + "<br/>" + impactFactors + "&nbsp;(" + year + ")";
  }

  @SuppressWarnings("unchecked")
  public List<AcJournal> getAcJournal(String startWith, int size) throws DaoException {
    boolean isEnglish = StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    // 判断是否是非英文,查询本人数据()
    if (isEnglish) {
      hql = "from BaseJournal2 t where lower(t.titleEn) like ? order by t.pissn";
    } else {
      hql = "from BaseJournal2 t where lower(t.titleXx) like ? order by t.pissn";
    }
    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);
    List<AcJournal> newList = new ArrayList<AcJournal>();
    List<BaseJournal2> list = query.list();
    // 赋予正确的值给name属性
    if (CollectionUtils.isNotEmpty(list)) {
      for (BaseJournal2 cr : list) {
        AcJournal acjnl = new AcJournal();
        acjnl.setCode(cr.getJouId());
        if (StringUtils.isNotBlank(cr.getPissn())) {
          acjnl.setIssn(cr.getPissn());
        }
        if (isEnglish) {
          acjnl.setName(cr.getTitleEn());
        } else {
          acjnl.setName(cr.getTitleXx());
        }
        newList.add(acjnl);
      }
    }
    return newList;
  }

  @SuppressWarnings("unchecked")
  public Long snsJnlMatchBaseJnlId(String jname, String issn) {
    String hql = "";
    List<Long> result = new ArrayList<Long>();
    if (StringUtils.isNotBlank(issn)) {
      hql =
          "select distinct b.jouId from BaseJournal2 b,BaseJournalTitleTo  t where (b.titleEn=? or b.titleXx=? or t.titleAbbr=?) and (b.pissn=? or b.eissn=?) and t.jnlId=b.jouId";
      result = super.createQuery(hql, jname, jname, jname, issn, issn).list();
    } else {
      hql =
          "select distinct b.jouId from BaseJournal2 b,BaseJournalTitleTo  t where (b.titleEn=? or b.titleXx=? or t.titleAbbr=?)  and t.jnlId=b.jouId";
      result = super.createQuery(hql, jname, jname, jname).list();
    }

    if (CollectionUtils.isEmpty(result)) {
      return null;
    } else {
      return NumberUtils.toLong(result.get(0).toString());
    }
  }

  @SuppressWarnings("unchecked")
  public List<BaseJournal2> findBaseJournal2(List<Long> ids) {
    String hql = "from BaseJournal2 where jouId in(:ids)";
    return super.createQuery(hql).setParameterList("ids", ids.toArray()).list();
  }

  // 同步更新baseJournal刷新表base_journal_sync_flag
  public void syncBaseJournalFlag(Long jnlId, int isdel) {
    String sqldup = "select count(1) from base_journal_sync_flag where jnl_id=?";
    int count = super.queryForInt(sqldup, new Object[] {jnlId});
    if (count > 0) {
      String sql = "update base_journal_sync_flag set del=? where jnl_id=?";
      super.update(sql, new Object[] {isdel, jnlId});
    } else {
      String sql = "insert into base_journal_sync_flag values(?,?,?)";
      super.update(sql, new Object[] {jnlId, isdel, 0});
    }
  }

  @SuppressWarnings("unchecked")
  public List<BaseJournal2> findJnlIds(Collection<Long> jidList) {
    String hql = "from BaseJournal2 where jouId in(:jidList)";
    return super.createQuery(hql).setParameterList("jidList", jidList).list();
  }

  public String getRomeoColourByJid(Long jid) {
    String hql = "select t.romeoColour from BaseJournal t,Journal t2 where t.jouId=t2.matchBaseJnlId and t2.id=?";
    return findUnique(hql, jid);
  }

  /**
   * 通过pissn获取base_journal
   * 
   * @author zk
   * @param issns
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<BaseJournal> getBaseJournalsByIssns(List<String> issns) {

    String hql =
        "select new BaseJournal(lower(t.pissn),t.titleEn,t.titleXx) from BaseJournal t where lower(t.pissn) in (:issns)";
    return super.createQuery(hql).setParameterList("issns", issns).list();
  }
}
