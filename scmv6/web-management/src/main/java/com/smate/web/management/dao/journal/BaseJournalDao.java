package com.smate.web.management.dao.journal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.journal.BaseJournal;
import com.smate.web.management.model.journal.BaseJournal2;

@Repository
public class BaseJournalDao extends PdwhHibernateDao<BaseJournal, Long> {

  /**
   * 查找基础期刊 动态条件拼接
   * 
   * @param title_xx
   * @param title_en
   * @param pissn
   * @param eissn
   * @return
   */
  public List<BaseJournal> findBaseJnl(String title_xx, String title_en, String pissn, String eissn) {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    if (StringUtils.isNotBlank(title_xx))
      criterionList.add(Restrictions.eq("titleXx", title_xx));
    if (StringUtils.isNotBlank(title_en))
      criterionList.add(Restrictions.eq("titleEn", title_en).ignoreCase());
    if (StringUtils.isNotBlank(pissn))
      criterionList.add(Restrictions.eq("pissn", pissn).ignoreCase());
    if (StringUtils.isNotBlank(eissn))
      criterionList.add(Restrictions.eq("eissn", eissn));
    return find(criterionList.toArray(new Criterion[criterionList.size()]));
  }

  public void deleteSyncJournalFlag(Long jnlId) {
    String sql = "delete from base_journal_sync_flag where jnl_id=?";
    super.update(sql, new Object[] {jnlId});
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

  public Page<BaseJournal> throwsType(Page page, Integer throwsType) {
    String hql = "";
    if (throwsType == 1) {
      hql =
          "select new BaseJournal2(t1.jnlId,t1.titleEn,t1.titleXx,t1.pissn) from BaseJournal2 t1,BaseJournalTitleTo t2 where t1.jouId=t2.jnlId and (t1.titleEn<>t2.titleEn or t1.titleXx<>t2.titleXx) and (t2.titleStatus is null or t2.titleStatus=0)";
    }
    if (throwsType == 2) {
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
        newjnl.setJnlId(baseJournal2.getJouId());
        newjnl.setTitleEn(baseJournal2.getTitleEn());
        newjnl.setTitleXx(baseJournal2.getTitleXx());
        newjnl.setPissn(baseJournal2.getPissn());
        if (throwsType == 1) {
          newjnl.setIsNewTitle(true);
        }
        if (throwsType == 2) {
          newjnl.setIsNewPissn(true);
        }
        newJnlList.add(newjnl);
      }
    }
    page.setResult(newJnlList);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<BaseJournal> findBaseJournalByThrows(Page page, int type) {
    String hql = "";
    if (type == 1) {
      hql =
          "select new BaseJournal(t1.jnlId,t1.titleEn,t1.titleXx,t1.pissn) from BaseJournal t1,BaseJournalTitle t2 where t1.jnlId=t2.jnlId and (t1.titleEn<>t2.titleEn or t1.titleXx<>t2.titleXx) and (t2.titleStatus is null or t2.titleStatus=0)";
    }
    if (type == 2) {
      hql =
          "select new BaseJournal(t1.jnlId,t1.titleEn,t1.titleXx,t1.pissn) from BaseJournal t1,BaseJournalTitle t2 where t1.jnlId=t2.jnlId and t1.pissn<>t2.pissn";
    }
    Query q = createQuery(hql);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<BaseJournal> result = q.list();
    List<BaseJournal> newJnlList = new ArrayList<BaseJournal>();
    if (CollectionUtils.isNotEmpty(result)) {
      for (BaseJournal baseJournal : result) {
        BaseJournal newjnl = new BaseJournal();
        newjnl.setJnlId(baseJournal.getJnlId());
        newjnl.setTitleEn(baseJournal.getTitleEn());
        newjnl.setTitleXx(baseJournal.getTitleXx());
        newjnl.setPissn(baseJournal.getPissn());
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

  public Page<BaseJournal> getBaseJournal(Page<BaseJournal> page, BaseJournal journal) {
    StringBuilder hql = new StringBuilder("from BaseJournal where 1=1 ");
    List<Object> params = new ArrayList<>();
    if (journal.getJnlId() != null) {
      hql.append(" and jnlId =?");
      params.add(journal.getJnlId());
    }
    if (StringUtils.isNotBlank(journal.getPissn())) {
      hql.append(" and pissn=?");
      params.add(journal.getPissn().trim());
    }
    if (StringUtils.isNotBlank(journal.getTitleXx())) {
      hql.append(" and upper(titleXx) like ? or upper(titleEn) like ?");
      String trim = journal.getTitleXx().trim();
      params.add("%" + trim.toUpperCase() + "%");
      params.add("%" + trim.toUpperCase() + "%");
    }
    hql.append(" order by jnlId desc");
    return super.findPage(page, hql.toString(), params.toArray());
  }
}
