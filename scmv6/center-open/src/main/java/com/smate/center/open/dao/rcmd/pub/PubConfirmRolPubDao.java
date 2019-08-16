package com.smate.center.open.dao.rcmd.pub;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.rcmd.pub.PubConfirmRolPub;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 成果提交持久层.
 * 
 * @author LY
 * 
 */
@Repository
public class PubConfirmRolPubDao extends RcmdHibernateDao<PubConfirmRolPub, Long> {

  public Date findPubUpdateTime(Long rolPubId) {
    String hql = " select   p.updateDate  from  PubConfirmRolPub p where  p.rolPubId=:rolPubId   ";
    Object obj = this.createQuery(hql).setParameter("rolPubId", rolPubId).uniqueResult();
    if (obj != null) {
      return (Date) obj;
    }
    return null;
  }

  public String findPubFulltextUrl(Long rolPubId) {
    String hql = " select   p.fulltextUrl  from  PubConfirmRolPub p where  p.rolPubId=:rolPubId   ";
    Object obj = this.createQuery(hql).setParameter("rolPubId", rolPubId).uniqueResult();
    if (obj != null) {
      return obj.toString();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<String> getKwRcmdTestDic() {
    String sql = "select t.kw_str from nsfc_project_kw_test_isi_dis t";
    return (List<String>) this.getSession().createSQLQuery(sql).list();
  }

  public List<String> getKwByCategory(String category) {
    String sql =
        "select t.keyword from nsfc_keywords_discipline t where substr(t.nsfc_application_code,0,3) =:category ";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameter("category", category).list();
  }

}
