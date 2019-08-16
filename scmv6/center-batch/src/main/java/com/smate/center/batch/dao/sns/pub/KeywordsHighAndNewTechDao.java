package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.KeywordsHighAndNewTech;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class KeywordsHighAndNewTechDao extends PdwhHibernateDao<KeywordsHighAndNewTech, Long> {
  @SuppressWarnings("unchecked")
  public List<String> getAllKwStrByCategory(String category) {
    String hql = "select t.content from KeywordsHighAndNewTech t where t.category=:category";
    return super.createQuery(hql).setParameter("category", category).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getAllKwStr() {
    String hql = "select t.content from KeywordsHighAndNewTech t";
    return super.createQuery(hql).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getAllKwStrByCategoryByLanguage(String category, Integer language) {
    String hql = "select t.content from KeywordsHighAndNewTech t where t.category=:category and t.language=:language";
    return super.createQuery(hql).setParameter("category", category).setParameter("language", language).list();
  }

  public List<BigDecimal> getHighTechPubToHandleList(Integer size) {
    String sql = "select t.pub_id from pdwh_pub_classification_ht t where t.status = 0 order by t.pub_id asc";
    return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
  }

  public BigDecimal getHighTechPubToHandleNum() {
    String sql = "select count(1) from pdwh_pub_classification_ht t where t.status = 0";
    return (BigDecimal) this.getSession().createSQLQuery(sql).uniqueResult();
  }

  public void updateHighTechPubToHandleStatus(Long pubId, Integer status) {
    String sql = "update pdwh_pub_classification_ht t set t.status =:status where t.pub_id =:pubId";
    this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("pubId", pubId).executeUpdate();
  }

  public List<Map<String, Object>> getHighTechClassification(Set<String> kwSet) {
    String sql =
        "select t.category, count(1) kwnum from keywords_high_new_tech t where t.content in (:kwSet) group by t.category order by count(1) desc";
    return this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameterList("kwSet", kwSet).list();
  }

  public void saveHighTechRs(Long pubId, String category, Integer num) {
    String sql = "insert into pub_category_ht values (seq_pdwh_ins_addr_const.nextval,?,?,?)";
    super.update(sql, new Object[] {pubId, category, num});
  }

  public void deleteHighTechRs(Long pubId) {
    String sql11 = "select count(1) from pub_category_ht t where t.pub_id =:pubId";
    BigDecimal pubCount =
        (BigDecimal) this.getSession().createSQLQuery(sql11).setParameter("pubId", pubId).uniqueResult();
    if (pubCount != null && pubCount.longValue() > 0L) {
      String sql = "delete pub_category_ht t where t.pub_id =:pubId";
      this.getSession().createSQLQuery(sql).setParameter("pubId", pubId).executeUpdate();
    }
  }
}
