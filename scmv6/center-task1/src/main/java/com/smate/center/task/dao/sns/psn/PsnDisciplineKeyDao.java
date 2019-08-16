package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author zll
 * 
 */
@Repository
public class PsnDisciplineKeyDao extends SnsHibernateDao<PsnDisciplineKey, Long> {

  /**
   * 查询某人的关键词数据.
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<PsnDisciplineKey> findByPsnId(Long psnId) {
    return super.createQuery("from PsnDisciplineKey t where t.psnId = ? and t.status=1", psnId).list();
  }

  public String getKwNameById(Long id) {
    return (String) super.createQuery("select keyWords from PsnDisciplineKey t where t.id = ? and t.status=1", id)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<String> getPsnKeywords(Long psnId) {
    return super.createQuery("select t.keyWords from PsnDisciplineKey t where t.psnId = ? and t.status=1", psnId)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getSameKeywords(List<String> grpKwList, Long psnId) {
    String hql =
        "select t.keyWords from PsnDisciplineKey t where t.psnId = :psnId and t.status=1 and lower(t.keyWords) in (:keywords)";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("keywords", grpKwList).list();
  }
}
