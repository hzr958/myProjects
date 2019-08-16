package com.smate.center.task.dao.sns.quartz;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.KwRmcGroup;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class KwRmcGroupDao extends SnsHibernateDao<KwRmcGroup, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getKwRmcGids(Collection<String> kws) {
    String hql = "select gid from KwRmcGroup where kwTxt in(:kws)";
    return super.createQuery(hql).setParameterList("kws", kws).list();
  }

  public Long querySeq() {
    String seq = "select seq_psn_kw_rmc_group_gid.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(seq);
    BigDecimal id = (BigDecimal) query.uniqueResult();
    return id.longValue();

  }

  public Long queryGid(String keyWordTxt) {
    String seq = "select t.gid from KwRmcGroup t where t.kwTxt=:keyWordTxt ";
    return (Long) super.createQuery(seq).setParameter("keyWordTxt", keyWordTxt).setMaxResults(1).uniqueResult();
  }

  public Long queryKwCount(String keyWordTxt) {
    String hql = "select count(1) from KwRmcGroup t where t.kwTxt=:kw";
    return (Long) super.createQuery(hql).setParameter("kw", keyWordTxt).uniqueResult();
  }
}
