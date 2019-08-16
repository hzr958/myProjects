package com.smate.web.v8pub.dao.journal;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.journal.BaseJournal2;
import com.smate.web.v8pub.po.journal.BaseJournalPO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * sns冗余基础期刊数据dao
 * 
 * @author tsz
 * 
 */
@Repository
public class BaseJournalDAO extends PdwhHibernateDao<BaseJournalPO, Long> {

  @SuppressWarnings("unchecked")
  public List<BaseJournalPO> findBaseJournalSns(List<Long> ids) {
    String hql = "from BaseJournalPO where jouId in(:ids)";
    return super.createQuery(hql).setParameterList("ids", ids.toArray()).list();
  }

  /**
   * 新加期刊 匹配基础期刊
   * 
   * @param jname
   * @param issn
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long searchJnlMatchBaseJnlId(String jname, String issn) {
    String hql = "";
    List<Long> result = new ArrayList<Long>();
    if (StringUtils.isNotBlank(issn)) {
      hql =
          "select distinct b.jouId from BaseJournalPO b,BaseJournalTitleTo  t where (b.titleEn=? or b.titleXx=? or t.titleAbbr=?) and (b.pissn=? or b.eissn=?) and t.jnlId=b.jouId";
      result = super.createQuery(hql, jname, jname, jname, issn, issn).list();
    } else {
      hql =
          "select distinct b.jouId from BaseJournalPO b,BaseJournalTitleTo  t where (b.titleEn=? or b.titleXx=? or t.titleAbbr=?)  and t.jnlId=b.jouId";
      result = super.createQuery(hql, jname, jname, jname).list();
    }

    if (CollectionUtils.isEmpty(result)) {
      return null;
    } else {
      return NumberUtils.toLong(result.get(0).toString());
    }
  }

  public BaseJournal2 getBaseJournal2Title(Long baseJnlId) {
    String hql = "select new BaseJournal2(jouId,titleEn,titleXx) from BaseJournal2 where jouId=?";
    return findUnique(hql, baseJnlId);
  }

  /**
   * 新加期刊 匹配基础期刊
   * 
   * @param jname
   * @param issn
   * @return
   */
  public Long snsJnlMatchBaseJnlId(String jname, String issn) {
    String hql = "select jouId from BaseJournalPO  where (titleEn=? or titleXx=?) and (pissn=?)";
    return findUnique(hql, jname, jname, issn);
  }

  @SuppressWarnings("rawtypes")
  public String findImpactFactors(Long jnlId ,Integer publishYear) {
    String sql =
        "select  JOU_IF , IF_YEAR  from   base_journal_isi_if   where  JNL_ID =:jnlId and JOU_IF is not null order by IF_YEAR desc nulls LAST ";
    List list = this.getSession().createSQLQuery(sql).setParameter("jnlId", jnlId).list();
    String impactFactors = "";
    if (list != null && list.size() > 0) {
      if (publishYear != null) {
        for (Object obj : list) {
          Object[] result = (Object[]) obj;
          if (result.length == 2 &&  result[1] != null && result[1].toString().equals(publishYear.toString())) {
              if(result[0] != null) {
                  impactFactors =result[0].toString();
              }
          }
        }
      }
      if (StringUtils.isBlank(impactFactors)) {
        Object[] result = (Object[]) list.get(0);
        impactFactors = result[0].toString();
      }

    }
    return impactFactors;
  }
}
