package com.smate.center.open.dao.nsfc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcKwForSorting;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class NsfcKwForSortingDao extends PdwhHibernateDao<NsfcKwForSorting, Long> {

    @SuppressWarnings("unchecked")
    public List<String> getToHandleDiscode(Integer size) {
        String sql = "select t.discode from nsfc_discode t where t.status = 0";
        return (List<String>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
    }

    public void saveNsfcDiscode(Integer status, String discode) {
        String sql = "update nsfc_discode t set t.status =:status where t.discode =:discode";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("discode", discode)
                .executeUpdate();
    }

    public List<String> getSortedKws(Set<String> kwSet) {
        String sql =
                "select p.kw from (select t.kw ,t.tf+t.cotf_sum as counts from nsfc_kw_sorting t where t.kw in (:kwSet)) p group by p.kw order by length(p.kw) desc, sum(p.counts) desc";
        return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("kwSet", kwSet).list();
    }

    public List<Map<String, Object>> getKwsInfo(Set<String> kwSet) {
        String sql =
                "select t1.kw_str,t1.tf,t1.kw_length,t1.kw_type from (select t.kw_str,max(t.tf)over(partition by t.kw_str) as tf,t.kw_length,max(t.type)over(partition by t.kw_str) as kw_type from nsfc_project_kw_test_isi_all t where t.kw_str in (:kwSet)) t1 group by t1.kw_str,t1.tf,t1.kw_length,t1.kw_type";
        return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("kwSet", kwSet).list();
    }

    public void saveOrUpdateKGPubKws(Long pubId, String kws, Integer num) {
        BigDecimal count = (BigDecimal) this.getSession()
                .createSQLQuery("select count(1) from knowledgegraph_pub_kw t where t.pdwh_pub_id =:pubId")
                .setParameter("pubId", pubId).uniqueResult();
        String sql = "";
        if (count.intValue() > 0) {
            sql = "update knowledgegraph_pub_kw t set t.kw =:kws, t.kw_num =:num where t.pdwh_pub_id =:pubId";
        } else {
            sql = "insert into knowledgegraph_pub_kw values(:pubId,:kws,:num)";
        }
        this.getSession().createSQLQuery(sql).setParameter("kws", kws).setParameter("num", num)
                .setParameter("pubId", pubId).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<String> getKwDic() {
        String sql = "select distinct(t.kw_str) from nsfc_project_kw_test_isi4 t";
        return (List<String>) this.getSession().createSQLQuery(sql).list();
    }

    @SuppressWarnings("unchecked")
    public List<String> getKwDic1() {
        String sql = "select t.kw_str from nsfc_project_kw_test_isi_dis t";
        return (List<String>) this.getSession().createSQLQuery(sql).list();
    }

    public List<String> getKwByTfExcludePubKw(List<String> titleKws, List<String> absKws, Integer language) {
        String sql =
                "select p.kw2 from (select t2.kw_str as kw2,t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and  t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str having count(1)>1) p group by p.kw2 order by count(1) desc, length(p.kw2) desc";
        return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
                .setParameterList("absKws", absKws).setParameter("language", language).list();
    }

    // language=1外文；language=2中文
    public List<Map<String, Object>> getKwByCoTfCoutExcludePubKw(List<String> titleKws, List<String> absKws,
            Integer language) {
        String sql =
                "select t3.kw2 as kw, count(1) as counts from (select t2.kw_str as kw2, t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str having count(1)>1) t3 group by t3.kw2 order by count(1) desc, length(t3.kw2) desc";
        return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
                .setParameterList("absKws", absKws).setParameter("language", language).list();
    }

    @SuppressWarnings("unchecked")
    public List<String> getNsfcKwDic() {
        String sql = "select t.kw_str from nsfc_kw_ytf_unique t";
        return (List<String>) this.getSession().createSQLQuery(sql).list();
    }
}
