package com.smate.center.batch.dao.pdwh.pub;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetDesc;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubKeywordsSubsetDescDao extends PdwhHibernateDao<PubKeywordsSubsetDesc, Long> {
  public List<String> getNsfcKwStrsDiscipline() {
    String sql = "select t.keyword from nsfc_keywords_discipline t";
    return (List<String>) this.getSession().createSQLQuery(sql).list();
  }

  public List<String> getNsfcKwStrsDiscipline(Integer language) {
    String sql = "select t.keyword from nsfc_pdwh_kw t where t.language =:language";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameter("language", language).setMaxResults(35000)
        .list();
  }

  public void saveNsfcKwFromPrj(String pubId, String kws, String discode, Integer size) {
    String sql =
        "insert into nsfc_project_keywords_disc t (t.prj_code,t.keywords_nsfc,t.discode, t.kw_size) values (:pubId, :kws, :discode,:size)";
    this.getSession().createSQLQuery(sql).setParameter("pubId", pubId).setParameter("kws", kws)
        .setParameter("discode", discode).setParameter("size", size).executeUpdate();
  }

  public List<BigDecimal> getTohandlePdwhPubJournal() {
    String sql = "select t.pub_id from pdwh_pub_journal_id_tmp t where t.status = 0";
    return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setMaxResults(2000).list();
  }

  public void updateTohandlePdwhPubJournal(Integer status, Long pubId) {
    String sql = "update pdwh_pub_journal_Id_tmp t set t.status =:status where t.pub_id =:pubId";
    this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("pubId", pubId).executeUpdate();
  }

  public void updateTohandlePdwhPubJournalJnlId(Long jnlId, Long pubId) {
    String sql = "update pdwh_pub_journal_Id_tmp t set t.jnl_id =:jnlId, t.status=1 where t.pub_id =:pubId";
    this.getSession().createSQLQuery(sql).setParameter("jnlId", jnlId).setParameter("pubId", pubId).executeUpdate();
  }
}
