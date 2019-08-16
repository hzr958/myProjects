package com.smate.center.task.dao.sns.quartz;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwEptGroup;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员关键词分组dao
 * 
 * @author zjh
 *
 */
@Repository
public class PsnKwEptGroupDao extends SnsHibernateDao<PsnKwEptGroup, Long> {
  public Long getCount(String keyWordTxt) {
    String hql = "select count(1) from PsnKwEptGroup t where t.kwTxt=:keyWordTxt ";
    return (Long) super.createQuery(hql).setParameter("keyWordTxt", keyWordTxt).uniqueResult();
  }

  public Long querySeq() {
    String sql = "select  seq_psn_kw_ept_group.nextval from dual";
    BigDecimal seq = (BigDecimal) this.getSession().createSQLQuery(sql).uniqueResult();
    return seq.longValue();
  }

  public Long querySeq1() {
    String sql = "select SEQ_PSN_KW_EPT_KWGID.Nextval from dual";
    BigDecimal gid = (BigDecimal) this.getSession().createSQLQuery(sql).uniqueResult();
    return gid.longValue();
  }

  public void addNew(Long kgid, String keywordTxt, Long gid) {
    String sql = "insert into psn_kw_ept_group(id,Keyword_Txt,Gid) values(?,?,?)";
    super.update(sql, new Object[] {kgid, keywordTxt, gid});

  }

  public Long queryGid(String keywordTxt) {
    String hql = "select t.gid from PsnKwEptGroup t where t.kwTxt=:keywordTxt";
    return (Long) super.createQuery(hql).setParameter("keywordTxt", keywordTxt).setMaxResults(1).uniqueResult();

  }
}
