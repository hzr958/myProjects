package com.smate.center.batch.dao.rcmd.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rcmd.pub.DynRecommendPsnTask;
import com.smate.core.base.utils.data.RcmdHibernateDao;


/**
 * 推荐动态人员定时器任务数据库处理类.
 * 
 * @author mjg
 * 
 */
@Repository
public class NsfcKwsRcmdDao extends RcmdHibernateDao<DynRecommendPsnTask, Long> {
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
