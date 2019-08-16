package com.smate.center.batch.dao.pdwh.prj;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.prj.NsfcPrjKeywords;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class NsfcPrjKeywordsDao extends PdwhHibernateDao<NsfcPrjKeywords, Long> {

  public List<String> getKwStrByPrjIdAndLanguage(Long prjId, Integer language) {
    String hql = "select t.kwStr from NsfcPrjKeywords t where t.prjId =:prjId and t.language =:language";
    return super.createQuery(hql).setParameter("prjId", prjId).setParameter("language", language).list();
  }

  public List<Map<String, Object>> getCotfSum(String kw, String discode, Integer language) {
    String sql =
        "select p.prj_id as prjId,count(1) as counts from NSFC_PROJECT_KW p where exists(select 1 from NSFC_PROJECT_KW t where t.kw_str =:kw and t.nsfc_category =:discode and p.prj_id = t.prj_id) and p.language =:language group by p.prj_id";
    return this.getSession().createSQLQuery(sql).setParameter("kw", kw).setParameter("discode", discode)
        .setParameter("language", language).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  public List<String> getKwByDiscode(String discode, Integer language) {
    String sql =
        "select distinct(t.kw_str) from  NSFC_PROJECT_KW t where t.nsfc_category =:discode and t.language =:language";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameter("discode", discode)
        .setParameter("language", language).list();
  }

  public BigDecimal getTf(String kw, String discode, Integer language) {
    String sql =
        "select count(distinct(t.prj_id)) from NSFC_PROJECT_KW t where t.kw_str =:kw and t.nsfc_category =:discode and t.language =:language";
    return (BigDecimal) this.getSession().createSQLQuery(sql).setParameter("kw", kw).setParameter("discode", discode)
        .setParameter("language", language).uniqueResult();
  }

  public BigDecimal getApplicationNum(String discode, Integer[] lastThreeYears) {
    String sql =
        "select max(t.application_num) from nsfc_application_num t where t.apply_year in (:lastThreeYears) and t.discode =:discode";
    return (BigDecimal) this.getSession().createSQLQuery(sql).setParameterList("lastThreeYears", lastThreeYears)
        .setParameter("discode", discode).uniqueResult();
  }

  public BigDecimal getProjectNum(String discode, Integer[] lastThreeYears) {
    String sql =
        "select max(p1.counts) from (select p.year as year, count(p.year) as counts from (select t.year, t.prj_id from NSFC_PROJECT_KW t where t.nsfc_category =:discode and t.year in (:lastThreeYears) group by t.year,t.prj_id) p group by p.year) p1";
    return (BigDecimal) this.getSession().createSQLQuery(sql).setParameter("discode", discode)
        .setParameterList("lastThreeYears", lastThreeYears).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> queryNeedDownloadFile(Long lastPubId, int batchSize) {
    String sql =
        "select t.pub_Id,t.file_path from TMP_PUB_KW_FULLTEXT3 t where t.pub_Id>:lastPubId order by t.pub_Id asc";
    return super.getSession().createSQLQuery(sql).setParameter("lastPubId", lastPubId).setMaxResults(batchSize)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

}
