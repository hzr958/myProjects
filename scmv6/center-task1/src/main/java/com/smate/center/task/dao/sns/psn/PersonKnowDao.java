package com.smate.center.task.dao.sns.psn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PersonKnow;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员关键字
 * 
 * @author tsz
 *
 */
@Repository
public class PersonKnowDao extends SnsHibernateDao<PersonKnow, Long> {
  @SuppressWarnings("unchecked")
  public List<Long> findPersonKnow(Long lastPsnId, int batchSize) {
    String hql =
        "select personId from PersonKnow where isLogin=1 and enabled=1 and personId > :psnId order by personId asc";
    return super.createQuery(hql).setParameter("psnId", lastPsnId).setMaxResults(batchSize).list();
  }

  public List<Long> findTaskPsnRefRecommendIds(int batchSize) {
    String sql = "select t.psn_id from task_psn_ref_recommend_ids t where t.status=:status order by t.psn_id asc";
    SQLQuery sqlQuery =
        (SQLQuery) super.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    sqlQuery.setParameter("status", 1);
    sqlQuery.setMaxResults(batchSize);
    List list = sqlQuery.list();
    List<Long> resultList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (Object obj : list) {
        Map map = (Map) obj;
        resultList.add(NumberUtils.toLong(ObjectUtils.toString(map.get("PSN_ID"))));
      }
    }
    return resultList;
  }

  public void updateTaskPsnRefRecommendId(Long psnId) {
    String sql = "update task_psn_ref_recommend_ids set status=? where psn_id=?";
    super.update(sql, new Object[] {0, psnId});
  }
}
